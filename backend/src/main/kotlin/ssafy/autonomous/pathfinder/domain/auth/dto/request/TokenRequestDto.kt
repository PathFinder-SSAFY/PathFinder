package ssafy.autonomous.pathfinder.domain.auth.dto.request

data class TokenRequestDto(
    private val accessToken: String
){
    fun getAccessToken(): String {
        return this.accessToken
    }
}