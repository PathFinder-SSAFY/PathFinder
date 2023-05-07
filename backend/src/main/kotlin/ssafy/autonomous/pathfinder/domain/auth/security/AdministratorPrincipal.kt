package ssafy.autonomous.pathfinder.domain.auth.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ssafy.autonomous.pathfinder.domain.administrator.domain.Administrator
import ssafy.autonomous.pathfinder.domain.administrator.domain.Authority
import java.util.*


/*
    ✔️ UserPrincipal
    UserPrincipal : Spring Security에서 인증된 사용자의 정보를 제공하는 인터페이스 중 하나
    UserPrincipal 인터페이스를 구현하는 클래스는 Spring Security에서 현재 인증된 사용자의 정보를 나타낸다.
* */

// 필요한 사용자 정보를 가진 UserDetails 객체를 생성하여 Spring Security에서 사용자 인증 및 권한 검사에 활용
class AdministratorPrincipal(
    private val email: String,
    private val authority: Authority
) : UserDetails {

    companion object {
        fun of(administrator: Administrator): AdministratorPrincipal {
            return AdministratorPrincipal(
                email = administrator.email,
                authority = administrator.authority
            )
        }

//        fun of(administrator: Administrator, attributes: Map<String, Any>): AdministratorPrincipal {
//            return AdministratorPrincipal(
//                id = administrator.id!!,
//                email = administrator.email,
//                password = administrator.password,
//                attributes = attributes,
//                authorities = listOf(SimpleGrantedAuthority(administrator.role.name))
//            )
//        }
    }

    fun getEmail() = this.email
    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorityList: MutableList<GrantedAuthority> = ArrayList()
        when (authority){
            Authority.ROLE_USER -> authorityList.add(SimpleGrantedAuthority("ROLE_USER"))
            Authority.ROLE_ADMIN -> authorityList.add(SimpleGrantedAuthority("ROLE_ADMIN"))
        }
        return authorityList
    }


    override fun getPassword(): String {
        // 비밀번호 필드를 사용하지 않는다.
        return null!!
    }

    override fun getUsername() = this.email
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true

}