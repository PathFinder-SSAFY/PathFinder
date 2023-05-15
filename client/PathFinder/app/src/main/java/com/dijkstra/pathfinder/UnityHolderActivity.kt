package com.dijkstra.pathfinder

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dijkstra.pathfinder.data.dto.Path
import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.util.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.absoluteValue

private const val TAG = "_ssafy"

class UnityHolderActivity : UnityPlayerActivity(),
    SensorEventListener { // End of UnityHolderActivity
    private lateinit var textToSpeech: TextToSpeech

    private lateinit var sensorManager: SensorManager
    private lateinit var roationVectorSensor: Sensor

    private lateinit var bottomSheet: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var navigationPathAdapter: NavigationPathAdapter
    private lateinit var navigationPathRecyclerView: RecyclerView

    private lateinit var myBluetoothHandler: MyBluetoothHandler
    private lateinit var viewModelProvider: ViewModelFactory
    private lateinit var unityViewModel: UnityViewModel

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private var cameraInitFlag: Boolean = true
    private var cameraRepositionFlag = false
    private var cameraPositionValidateState = false
    private val pathList: MutableList<Path> = mutableListOf<Path>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startPosition =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) intent.getParcelableExtra(
                "startPosition",
                Point::class.java
            )
            else intent.getParcelableExtra("startPosition")
        val goal =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) intent.getParcelableExtra(
                "goal",
                Point::class.java
            )
            else intent.getParcelableExtra("goal")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        roationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        initTTS()
        initUiLayout()
//        myBluetoothHandler = MyBluetoothHandler {
//            Log.d(TAG, "onCreate: ${unityViewModel.userCameraInfo}")
//            pathList.clear()
//            pathList.add(
//                "x: ${unityViewModel.userCameraInfo.x}\n" +
//                        "y: ${unityViewModel.userCameraInfo.y}\n" +
//                        "z: ${unityViewModel.userCameraInfo.z}"
//            )
//            navigationPathAdapter.notifyDataSetChanged()
//            UnityPlayer.UnitySendMessage(
//                "SystemController",
//                "SetARCameraPosition",
//                unityViewModel.userCameraInfo.toString()
//            )
//        }
        viewModelProvider = ViewModelFactory()

        unityViewModel = viewModelProvider.create(UnityViewModel::class.java)
        startPosition?.let {
            unityViewModel.startPosition = it
            unityViewModel.setUserCameraInfoPosition(startPosition)
        }
        goal?.let { unityViewModel.goal = it }

        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            unityViewModel.navigate(unityViewModel.startPosition, unityViewModel.goal)
        }

    } // End of onCreate

    override fun onStart() {
        super.onStart()
        coroutineScope.launch {
            launch {
                unityViewModel.navigationTestNetworkResultStateFlow.collect { testResult ->
                    when (testResult) {
                        is SubNetworkResult.Success -> {
                            Log.d(TAG, "onStart: Success, ${testResult.data}")
                        }
                        is SubNetworkResult.Error -> {
                            Log.e(TAG, "onStart: Error, ${testResult.message}")
                        }
                        is SubNetworkResult.Loading -> {
                            Log.d(TAG, "onStart: Loading test..")
                        }
                    } // End of when
                } // End of collect
            } // End of launch
            launch {
                unityViewModel.navigationNetworkResultStateFlow.collect { navigateNetworkResult ->
                    when (navigateNetworkResult) {
                        is SubNetworkResult.Success -> {
                            Log.d(TAG, "onStart: ${navigateNetworkResult.data}")
                            navigateNetworkResult.data?.steps?.let {
                                //Todo activate
                                pathList.clear()
                                pathList.addAll(it)
                                launch(Dispatchers.Main) {
                                    navigationPathAdapter.notifyDataSetChanged()
                                }
                            }
                            unityViewModel.setNavigationPathAtUnity()
                        }
                        is SubNetworkResult.Error -> {
                            Log.e(TAG, "onStart: Error, ${navigateNetworkResult.message}")
                        }
                        is SubNetworkResult.Loading -> {
                            Log.d(TAG, "onStart: Loading navigate..")
                        }
                    } // End of when
                } // End of collect
            } // End of launch
            launch {
                while (true) {
                    UnityPlayer.UnitySendMessage(
                        "SystemController",
                        "SendCameraPositionToAndroid",
                        ""
                    )
                    delay(1000)
                }
            }
        } // End of coroutineScope
    } // End of onStart

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    } // End of onStop

    private fun initTTS() {
        textToSpeech = TextToSpeech(this) { status ->
            when (status) {
                TextToSpeech.SUCCESS -> {
                    val result = textToSpeech.setLanguage(Locale.KOREA)
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                        Log.e(TAG, "onCreate: Language Not Supported")
                    } else {
                        Log.e(TAG, "onCreate: TTS Initialization successed!")
                    }
                }
                else -> {
                    Log.e(TAG, "onCreate: Initialization failed")
                }
            }
        }
    } // End of initTTS

    private fun initUiLayout() {
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ll = inflater.inflate(R.layout.activity_unity_holder, null) as CoordinatorLayout
        val paramll = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT
        )
        this.addContentView(ll, paramll)

        val cameraRepositionButton: Button =
            findViewById<Button?>(R.id.camera_reposition_button).apply {
                setOnClickListener {
                    cameraRepositionFlag = true
                }
            }
        val toggleMapButton: Button = findViewById<Button?>(R.id.toggle_map_button).apply {
            setOnClickListener {
                UnityPlayer.UnitySendMessage(
                    "SystemController",
                    "ToggleMapVisibility",
                    ""
                )
            }
        }

        findViewById<Button>(R.id.navigation_finish_button).setOnClickListener {
            finish()
        }

        bottomSheet = findViewById(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        navigationPathAdapter = NavigationPathAdapter(pathList)
        navigationPathRecyclerView =
            findViewById<RecyclerView>(R.id.navigation_path_recyclerview).apply {
                layoutManager =
                    LinearLayoutManager(this@UnityHolderActivity, RecyclerView.VERTICAL, false)
                adapter = navigationPathAdapter
                addItemDecoration(DividerItemDecoration(this@UnityHolderActivity, RecyclerView.VERTICAL))
                addOnScrollListener(
                    object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(
                            recyclerView: RecyclerView,
                            newState: Int
                        ) {
                            when (newState) {
                                RecyclerView.SCROLL_STATE_IDLE -> {
                                    bottomSheetBehavior.isDraggable = true
                                }
                                RecyclerView.SCROLL_STATE_DRAGGING -> {
                                    bottomSheetBehavior.isDraggable = false
                                }
                                RecyclerView.SCROLL_STATE_SETTLING -> {
                                    bottomSheetBehavior.isDraggable = false
                                }
                            }
                            super.onScrollStateChanged(recyclerView, newState)
                        } // End of onScrollStateChanged
                    })
            }

        findViewById<ImageView>(R.id.sound_toggle_button).setOnClickListener {
            textToSpeech.speak("안녕하세요", TextToSpeech.QUEUE_FLUSH, null, null)
        }
        findViewById<Button>(R.id.unity_map_toggle_button).setOnClickListener {
        }

    } // End of initUiLayout

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, roationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        cameraInitFlag = true
    } // End of onResume

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    } // End of onPause

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    } // End of onDestroy

    override fun onSensorChanged(event: SensorEvent) {
        //지구좌표계로 변환
        val rotationMatrix = FloatArray(16)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
        val orientation = FloatArray(3)
        SensorManager.getOrientation(rotationMatrix, orientation)
        val orientationDeg = FloatArray(3)

        //        Log.d(TAG, "onSensorChanged: ${event.values[1]}")
        cameraPositionValidateState = event.values[1].absoluteValue in (0.0f..0.7f)

        orientation.forEachIndexed { index, element ->
            orientationDeg[index] = (Math.toDegrees(element.toDouble()).toFloat() + 360 - 198) % 360
        }

        unityViewModel.setUserCameraInfoAngle(
            azimuth = orientationDeg[0],
            pitch = orientationDeg[1],
            roll = orientationDeg[2]
        )

        if (cameraInitFlag) {
            unityViewModel.initCamera()
            cameraInitFlag = false
        }
        if (cameraRepositionFlag) {
            changeCameraAngle()
            cameraRepositionFlag = false
        }
    } // End of onSensorChanged

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {} // End of onAccuracyChanged

    private fun changeCameraAngle() {
        when (cameraPositionValidateState) {
            true -> {
                unityViewModel.setCameraAngle()
            }
            false -> {
                Toast.makeText(this, "화면을 앞으로 살짝 기울여주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    } // End of repositionCamera

    private fun getCameraPositionFromUnity(args: String) {
        val gson = Gson()
        val currentPosition: Point? = gson.fromJson(args, Point::class.java)
        currentPosition?.let { unityViewModel.setUserCameraInfoPosition(currentPosition) }
//        Log.d(TAG, "ReceivedMessageByUnity: ${args} ")
//        Log.d(TAG, "ReceivedMessageByUnity: ${unityViewModel.userCameraInfo} ")
    } // End of getCameraPositionFromUnity

    private fun researchNavigationPath(args: String = "") {
//        unityViewModel.navigate(
//            Point(
//                unityViewModel.userCameraInfo.x.toDouble(),
//                unityViewModel.userCameraInfo.y.toDouble(),
//                unityViewModel.userCameraInfo.z.toDouble()
//            ),
//            unityViewModel.goal
//        )
    } // End of researchNavigationPath

} // End of UnityHolderActivity