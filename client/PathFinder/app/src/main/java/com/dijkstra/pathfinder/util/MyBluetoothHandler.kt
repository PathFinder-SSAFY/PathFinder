package com.dijkstra.pathfinder.util

import android.os.Handler
import android.os.Looper
import android.os.Message

class MyBluetoothHandler(private val callback: (String) -> Unit): Handler(Looper.getMainLooper())  {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        callback("")
    }
}

