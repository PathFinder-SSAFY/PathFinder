package ssafy.autonomous.pathfinder.domain.administrator.service

import ssafy.autonomous.pathfinder.domain.administrator.domain.AdministratorOAuth2User

interface AdministratorOAuth2UserService {

    fun createAdministrator()
    fun saveAdministrator( administratorOAuth2User: AdministratorOAuth2User)
}