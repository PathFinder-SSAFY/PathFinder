package com.dijkstra.pathfinder.util

import android.app.Application
import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dijkstra.pathfinder.UnityViewModel

class ViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == UnityViewModel::class.java) {
            return UnityViewModel(
                Injection.provideNavigationRepository()
            ) as T
        }
        throw IllegalArgumentException("unknown model class $modelClass")
    }
}