package com.dijkstra.pathfinder

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dijkstra.pathfinder.util.MyBluetoothHandler
import com.dijkstra.pathfinder.util.NetworkResult
import com.dijkstra.pathfinder.util.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
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

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private var cameraInitFlag: Boolean = true
    private var cameraRepositionFlag = false
    private var cameraPositionValidateState = false
    private val pathList: MutableList<String> = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: ${this.applicationContext}")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        roationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        initTTS()
        initUiLayout()
        myBluetoothHandler = MyBluetoothHandler {
            Log.d(TAG, "onCreate: ${unityViewModel.userCameraInfoDto}")
            pathList.clear()
            pathList.add("x: ${unityViewModel.userCameraInfoDto.x}\n" +
                    "y: ${unityViewModel.userCameraInfoDto.y}\n" +
                    "z: ${unityViewModel.userCameraInfoDto.z}")
            navigationPathAdapter.notifyDataSetChanged()
            UnityPlayer.UnitySendMessage(
                "SystemController",
                "SetARCameraPosition",
                unityViewModel.userCameraInfoDto.toString()
            )
        }
        viewModelProvider = ViewModelFactory(application, myBluetoothHandler)
        unityViewModel = viewModelProvider.create(UnityViewModel::class.java)

        //todo delete
//        unityViewModel.beaconList.observe(ProcessLifecycleOwner.get()) {
//            Log.d(TAG, "observe: ${it}")
//            pathList.clear()
//            pathList.addAll(it.map { beacon ->
//                beacon.id3.toString()
//            })
//        }
        unityViewModel.nowLocation.observe(ProcessLifecycleOwner.get()) {
            Log.d(TAG, "observe: ${it}")
        }

    } // End of onCreate

    override fun onStart() {
        super.onStart()
        coroutineScope.launch {
            unityViewModel.navigationTestNetworkResultStateFlow.collect { testResult ->
                when(testResult) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "onStart: Success, ${testResult.data}")
                    }
                    is NetworkResult.Error -> {
                        Log.e(TAG, "onStart: Error, ${testResult.message}", )
                    }
                    is NetworkResult.Loading -> {
                        Log.d(TAG, "onStart: Loading..")
                    }
                }
            }
        }
    } // End of onStart

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }

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
    }

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

        bottomSheet = findViewById(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        //TODO DELETE
        (0..10).forEach {
            pathList.add("$it")
        }

        navigationPathAdapter = NavigationPathAdapter(pathList)
        navigationPathRecyclerView = findViewById<RecyclerView>(R.id.navigation_path_recyclerview).apply {
            layoutManager =
                LinearLayoutManager(this@UnityHolderActivity, RecyclerView.VERTICAL, false)
            adapter = navigationPathAdapter
            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
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
            unityViewModel.navigationTest()
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        sensorManager.registerListener(this, roationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        cameraInitFlag = true
        unityViewModel.startBeaconScanning()
    } // End of onResume

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        unityViewModel.stopBeaconScanning()
    } // End of onPause

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

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

        // todo delete
        unityViewModel.userCameraInfoDto.azimuth = orientationDeg[0]
        unityViewModel.userCameraInfoDto.pitch = orientationDeg[1]
        unityViewModel.userCameraInfoDto.roll = orientationDeg[2]

        if (cameraInitFlag) {
            initCameraPosition()
            cameraInitFlag = false
        }
        if (cameraRepositionFlag) {
            repositionCamera()
            cameraRepositionFlag = false
        }
    } // End of onSensorChanged

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {} // End of onAccuracyChanged

    private fun initCameraPosition() {
        UnityPlayer.UnitySendMessage(
            "SystemController",
            "InitializeARCameraAngle",
            unityViewModel.userCameraInfoDto.toString()
        )
    } // End of initCameraPosition

    private fun repositionCamera() {
        when (cameraPositionValidateState) {
            true -> {
                initCameraPosition()
            }
            false -> {
                Toast.makeText(this, "화면을 앞으로 살짝 기울여주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    } // End of repositionCamera

}