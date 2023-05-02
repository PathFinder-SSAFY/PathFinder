package ssafy.autonomous.pathfinder.domain.auth.domain.oauth

enum class AuthProvider {
    NAVER;

    companion object {
        fun of(provider: String): AuthProvider = values().filter { it.name.equals(provider, ignoreCase = true) }.first()
    }

    fun equalWith(provider: String) = provider.equals(this.name, ignoreCase = true)
}