package com.dijkstra.pathfinder.util

import android.app.Application
import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dijkstra.pathfinder.UnityViewModel

class ViewModelFactory(private val application: Application, private val handler: Handler) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == UnityViewModel::class.java && handler is MyBluetoothHandler) {
            return UnityViewModel(
                application,
                Injection.provideNavigationRepository(),
                handler
            ) as T
        }
        throw IllegalArgumentException("unknown model class $modelClass")
    }
}