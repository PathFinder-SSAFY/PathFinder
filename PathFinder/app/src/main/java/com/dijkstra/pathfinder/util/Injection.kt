package com.dijkstra.pathfinder.util

import com.dijkstra.pathfinder.Application
import com.dijkstra.pathfinder.domain.api.NavigationApi
import com.dijkstra.pathfinder.domain.repository.NavigationRepository

object Injection {
    fun provideNavigationRepository(): NavigationRepository = NavigationRepository(
        provideNavigationApi()
    )
    fun provideNavigationApi(): NavigationApi = Application.retrofit.create(NavigationApi::class.java)
}