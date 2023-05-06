package ssafy.autonomous.pathfinder.domain.auth.domain//package ssafy.autonomous.pathfinder.domain.auth.domain.oauth
//
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
//class NaverOAuth2UserInfo(
//    override val attributes: Map<String, Any>
//):OAuth2UserInfo(attributes) {
//
//    private val mapType = object: TypeToken<Map<String, Any>>() {}.type
//    private val response : Map<String, Any> = Gson().fromJson(attributes.get("response").toString(), mapType)
//
//    override fun getOAuthId() = response.get("id").toString()
//    override fun getEmail() = response.get("email").toString()
//}