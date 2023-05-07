//package ssafy.autonomous.pathfinder.domain.auth.service
//
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
//import ssafy.autonomous.pathfinder.domain.administrator.repository.AdministratorRepository
//import ssafy.autonomous.pathfinder.domain.auth.domain.AdministratorPrincipal
//import ssafy.autonomous.pathfinder.domain.auth.exception.AdministratorNotFoundException
//
//@Service
//@Transactional(readOnly = true)
//class CustomUserDetailsService(
//    private val AdministratorRepository: AdministratorRepository
//): UserDetailsService {
//
//    override fun loadUserByUsername(email: String): UserDetails {
//        val administrator = AdministratorRepository.findByEmail(email)
//        if(administrator != null){
//            return AdministratorPrincipal.of(administrator)
//        }
//        throw AdministratorNotFoundException()
//    }
//
//
//    /* 사용자 ID를 매개 변수로 받아와서,
//       해당 ID를 가진 사용자의 정보를 데이터베이스나 다른 데이터 소스에서 조회하고,
//       조회된 정보를 UserDetails 인터페이스를 구현한 객체로 변환하여 반환
//       - Principal : UserDetails, OAuth2User
//    */
//    fun loadAdministratorById(id : Long): UserDetails{
//        val administrator = AdministratorRepository.findById(id).orElseThrow{AdministratorNotFoundException()}
//
//        return AdministratorPrincipal.of(administrator)
//    }
//}