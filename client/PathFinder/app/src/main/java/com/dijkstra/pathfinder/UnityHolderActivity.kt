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
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dijkstra.pathfinder.data.dto.Path
import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.util.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

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
    private lateinit var goalName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startPosition =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) intent.getParcelableExtra(
                INTENT_START_POSITION,
                Point::class.java
            )
            else intent.getParcelableExtra(INTENT_START_POSITION)
        val goal =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) intent.getParcelableExtra(
                INTENT_GOAL_POSITION,
                Point::class.java
            )
            else intent.getParcelableExtra(INTENT_GOAL_POSITION)
        goalName = intent.getStringExtra(INTENT_GOAL_NAME) ?: ""

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        roationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        initTTS()
        initUiLayout()
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

        val cameraRepositionButton: FloatingActionButton =
            findViewById<FloatingActionButton?>(R.id.camera_angle_reposition_button).apply {
                setOnClickListener {
                    cameraRepositionFlag = true
                }
            }
        val toggleMapButton: FloatingActionButton =
            findViewById<FloatingActionButton?>(R.id.toggle_map_button).apply {
                setOnClickListener {
                    unityViewModel.isMapDisplayed = !unityViewModel.isMapDisplayed
                    when (unityViewModel.isMapDisplayed) {
                        true -> setImageDrawable(
                            ContextCompat.getDrawable(
                                this@UnityHolderActivity,
                                R.drawable.wall_display_icon_visible
                            )
                        )
                        false -> setImageDrawable(
                            ContextCompat.getDrawable(
                                this@UnityHolderActivity,
                                R.drawable.wall_display_icon_invisible
                            )
                        )
                    }
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
                    LinearLayoutManager(this@UnityHolderActivity, RecyclerView.VERTICAL, true)
                adapter = navigationPathAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        this@UnityHolderActivity,
                        RecyclerView.VERTICAL
                    )
                )
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

        findViewById<ImageView>(R.id.sound_toggle_image_view).apply {
            setOnClickListener {
                unityViewModel.isVolumeMuted = !unityViewModel.isVolumeMuted
                when (unityViewModel.isVolumeMuted) {
                    true -> {
                        setImageDrawable(
                            ContextCompat.getDrawable(
                                this@UnityHolderActivity,
                                R.drawable.volume_off
                            )
                        )
                    }
                    false -> {
                        setImageDrawable(
                            ContextCompat.getDrawable(
                                this@UnityHolderActivity,
                                R.drawable.volume_on
                            )
                        )
                    }
                }
            }
        }
        findViewById<ImageView>(R.id.path_refresh_image_view).setOnClickListener {
            unityViewModel.navigate(
                start = Point(
                    x = unityViewModel.userCameraInfo.x.roundToInt().toDouble(),
                    y = unityViewModel.userCameraInfo.y.roundToInt().toDouble(),
                    z = unityViewModel.userCameraInfo.z.roundToInt().toDouble()
                ),
                goal = unityViewModel.goal
            )
        }
        findViewById<TextView>(R.id.goal_name_textview).text = goalName
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

//        unityViewModel.setUserCameraInfoAngle(
//            azimuth = orientationDeg[0],
//            pitch = orientationDeg[1],
//            roll = orientationDeg[2]
//        )
        unityViewModel.setUserCameraInfoAngle(
            azimuth = 90.toFloat(),
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
        currentPosition ?: return
        unityViewModel.setUserCameraInfoPosition(currentPosition)

        if (pathList.isEmpty()) return

        val nextPoint = pathList.first().getNextPoint()


        val distanceToNextPoint = getDistance(currentPosition, nextPoint);
        when (distanceToNextPoint) {
            in 0.0..1.0 -> {// 다음 위치에 도달한 경우
                pathList.removeFirst()
                when (pathList.size) {
                    0 -> { // 탐색 끝
                        textToSpeech.speak(
                            "목표 위치에 도달했습니다.",
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )
                        Toast.makeText(this, "목표 위치에 도달하였습니다. 잠시 후 종료됩니다.", Toast.LENGTH_SHORT)
                            .show()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(800)
                            this@UnityHolderActivity.finish()
                        }
                    }
                    1 -> { // 에러
                        Log.e(TAG, "getCameraPositionFromUnity: 경로 추적 에러")
                    }
                    else -> {
                        when (unityViewModel.isVolumeMuted) {
                            false -> {
                                when (pathList.first().direction) {
                                    Constant.RIGHT_TURN -> {
                                        textToSpeech.speak(
                                            "우회전 후 직진해주세요",
                                            TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            null
                                        )
                                    }
                                    Constant.LEFT_TURN -> {
                                        textToSpeech.speak(
                                            "좌회전 후 직진해주세요",
                                            TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            null
                                        )
                                    }
                                    else -> {}
                                } // End of when(pathList.frist().direction)
                            }
                            true -> {
                            }
                        } // End of when(unityViewModel.isVolumeMuted)
                        pathList.removeFirst()
                        Log.d(
                            TAG,
                            "next: cP : ${currentPosition}, nP: ${pathList.first().getNextPoint()}"
                        )
                        Log.d(
                            TAG,
                            "nextDistance: ${
                                getDistance(
                                    currentPosition,
                                    pathList.first().getNextPoint()
                                )
                            }"
                        )
                    } // End of else
                } // End of when(pathList.size)

                CoroutineScope(Dispatchers.Main).launch {
                    navigationPathAdapter.notifyDataSetChanged()
                    if (pathList.isEmpty()) {
                        Toast.makeText(
                            this@UnityHolderActivity,
                            "목표 위치에 도달하였습니다. 잠시 후 종료됩니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        delay(800)
                        this@UnityHolderActivity.finish()
                    }
                }
            }
            else -> {
                pathList[0] =
                    pathList[0].copy(distance = distanceToNextPoint, node = currentPosition)
                CoroutineScope(Dispatchers.Main).launch {
                    navigationPathAdapter.notifyItemChanged(0, Unit)
                }
                Log.d(TAG, "getCameraPositionFromUnity: $pathList")
            }
        } // End of when(distanceToNextPoint)
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

    companion object {
        const val INTENT_START_POSITION = "INTENT_START_POSITION"
        const val INTENT_GOAL_POSITION = "INTENT_GOAL_POSITION"
        const val INTENT_GOAL_NAME = "INTENT_GOAL_NAME"
    }

} // End of UnityHolderActivity