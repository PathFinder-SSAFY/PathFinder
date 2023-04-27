package com.dijkstra.pathfinder

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity

private const val TAG = "_ssafy"

class UnityHolderActivity : UnityPlayerActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var roationVectorSensor: Sensor


    private val unityViewModel: UnityViewModel = UnityViewModel()

    //todo delete
//    private var userCameraInfoDto: UserCameraInfoDto = UserCameraInfoDto()
    private var cameraInitFlag: Boolean = true

    // 0 ~ 360
//    private var azimuth = 0.0f
//    private var pitch = 0.0f
//    private var roll = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        roationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ll = inflater.inflate(R.layout.activity_unity_holder, null) as CoordinatorLayout
        val paramll = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT
        )
        this.addContentView(ll, paramll)
        val bottomSheet: LinearLayout = findViewById(R.id.bottom_sheet)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            Log.d(TAG, "onCreate: ${unityViewModel.userCameraInfoDto}")
            UnityPlayer.UnitySendMessage(
                "SystemController",
                "InitializeARCameraAngle",
                unityViewModel.userCameraInfoDto.toString()
            )
        }

        val behavior = BottomSheetBehavior.from(bottomSheet)
    } // End of onCreate

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, roationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        cameraInitFlag = true
    } // End of onResume

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    } // End of onPause

    override fun onSensorChanged(event: SensorEvent) {
        //지구좌표계로 변환
        val rotationMatrix = FloatArray(16)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
        val orientation = FloatArray(3)
        SensorManager.getOrientation(rotationMatrix, orientation)

        val orientationDeg = FloatArray(3)

        orientation.forEachIndexed { index, element ->
            orientationDeg[index] = (Math.toDegrees(element.toDouble()).toFloat() + 360) % 360
        }

        // todo delete
        unityViewModel.userCameraInfoDto.azimuth = orientationDeg[0]
        unityViewModel.userCameraInfoDto.pitch = orientationDeg[1]
        unityViewModel.userCameraInfoDto.roll = orientationDeg[2]

        if (cameraInitFlag) {
            //카메라 방향 초기화
            UnityPlayer.UnitySendMessage(
                "SystemController",
                "InitializeARCameraAngle",
                unityViewModel.userCameraInfoDto.toString()
            )
            cameraInitFlag = false
        }

    } // End of onSensorChanged

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    } // End of onAccuracyChanged
} // End of UnityHolderActivity