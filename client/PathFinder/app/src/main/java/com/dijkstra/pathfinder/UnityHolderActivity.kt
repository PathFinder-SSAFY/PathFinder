package com.dijkstra.pathfinder

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import kotlin.math.absoluteValue

private const val TAG = "_ssafy"

class UnityHolderActivity : UnityPlayerActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var roationVectorSensor: Sensor


    private val unityViewModel: UnityViewModel = UnityViewModel()
    private var cameraInitFlag: Boolean = true
    private var cameraRepositionFlag = false
    private var cameraPositionValidateState = false

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

        val cameraRepositionButton: Button = findViewById(R.id.camera_reposition_button)
        val toggleMapButton: Button = findViewById(R.id.toggle_map_button)

        cameraRepositionButton.setOnClickListener {
            cameraRepositionFlag = true
        }

        toggleMapButton.setOnClickListener {
            UnityPlayer.UnitySendMessage(
                "SystemController",
                "ToggleMapVisibility",
                ""
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

        //        Log.d(TAG, "onSensorChanged: ${event.values[1]}")
        cameraPositionValidateState = event.values[1].absoluteValue in (0.0f .. 0.7f)
        
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

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) { } // End of onAccuracyChanged

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

} // End of UnityHolderActivity