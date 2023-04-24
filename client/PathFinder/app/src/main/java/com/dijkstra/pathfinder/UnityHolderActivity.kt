package com.dijkstra.pathfinder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.unity3d.player.UnityPlayerActivity

private const val TAG = "_ssafy"
class UnityHolderActivity: UnityPlayerActivity() {
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ll = inflater.inflate(R.layout.activity_unity_holder, null) as CoordinatorLayout
        val paramll = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)
        this.addContentView(ll, paramll)

        val bottomSheet: LinearLayout = findViewById(R.id.bottom_sheet)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
//            UnityPlayer.UnitySendMessage("ScriptManager", "ReceiveMsg", "${++count}")
        }

        val behavior = BottomSheetBehavior.from(bottomSheet)

    }
}