package com.dijkstra.pathfinder

import androidx.lifecycle.ViewModel
import com.dijkstra.pathfinder.data.dto.UserCameraInfoDto

class UnityViewModel: ViewModel() {
    var isVolumeMuted = false
    var userCameraInfoDto: UserCameraInfoDto = UserCameraInfoDto()
}