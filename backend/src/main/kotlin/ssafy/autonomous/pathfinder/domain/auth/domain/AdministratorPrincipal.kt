//package ssafy.autonomous.pathfinder.domain.auth.domain
//
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.oauth2.core.user.OAuth2User
//import ssafy.autonomous.pathfinder.domain.administrator.domain.Administrator
//
///*
//    ✔️ UserPrincipal
//    UserPrincipal : Spring Security에서 인증된 사용자의 정보를 제공하는 인터페이스 중 하나
//    UserPrincipal 인터페이스를 구현하는 클래스는 Spring Security에서 현재 인증된 사용자의 정보를 나타낸다.
//* */
//
//class AdministratorPrincipal(
//    private val id: Long,
//    private val email: String,
//    private val password: String,
//    private val authorities: Collection<GrantedAuthority>,
//    private val attributes: Map<String, Any>? = null
//) : UserDetails, OAuth2User {
//
//    companion object {
//        fun of(administrator: Administrator): AdministratorPrincipal {
//            return AdministratorPrincipal(
//                id = administrator.id!!,
//                email = administrator.email,
//                password = administrator.password,
//                authorities = listOf(SimpleGrantedAuthority(administrator.role.name))
//            )
//        }
//
//        fun of(administrator: Administrator, attributes: Map<String, Any>): AdministratorPrincipal {
//            return AdministratorPrincipal(
//                id = administrator.id!!,
//                email = administrator.email,
//                password = administrator.password,
//                attributes = attributes,
//                authorities = listOf(SimpleGrantedAuthority(administrator.role.name))
//            )
//        }
//    }
//
//    fun getId() = this.id
//
//    override fun getName() = this.id.toString()
//
//    override fun getAttributes() = this.attributes
//    override fun getAuthorities() = this.authorities
//    override fun getPassword() = this.password
//
//    override fun getUsername() = this.email
//
//    override fun isAccountNonExpired() = true
//
//    override fun isAccountNonLocked() = true
//    override fun isCredentialsNonExpired() = true
//
//    override fun isEnabled() = true
//
//
//}