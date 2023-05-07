package ssafy.autonomous.pathfinder.domain.administrator.service

import ssafy.autonomous.pathfinder.domain.administrator.domain.Administrator

interface AdministratorService {

    fun createAdministrator()
    fun saveAdministrator( Administrator: Administrator)
}