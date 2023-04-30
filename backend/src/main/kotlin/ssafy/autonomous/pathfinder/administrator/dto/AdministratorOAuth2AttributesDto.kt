package ssafy.autonomous.pathfinder.administrator.dto

class AdministratorOAuth2AttributesDto  constructor(
    private val attributes: Map<String, Any>,
    private val nameAttributeKey: String,
    private val name: String,
    private val email: String,
    private val picture: String
) {
    fun toEntity(): User {
        return User.builder()
            .name(name)
            .email(email)
            .picture(picture)
            .role(Role.GUEST)
            .build()
    }

    companion object {
        fun of(registrationId: String, userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            return if ("naver" == registrationId) {
                ofNaver("id", attributes)
                )
        }

        private fun ofNaver(userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            val response = attributes["response"] as Map<String, Any>?
            return builder()
                .name(response!!["name"] as String?)
                .email(response["email"] as String?)
                .picture(response["profile_image"] as String?)
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build()
        }
    }
}