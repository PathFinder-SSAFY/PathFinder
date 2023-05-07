package ssafy.autonomous.pathfinder.global.util

import javax.servlet.http.HttpServletRequest

class HeaderUtil{
    companion object{
        const val HEADER_AUTHORIZATION: String = "Authorization"
        const val TOKEN_PREFIX: String = "Bearer "
    }

    fun getAccessToken(request: HttpServletRequest): String? {
        val headerValue = request.getHeader(HEADER_AUTHORIZATION) ?: return null
        return if (headerValue.startsWith(TOKEN_PREFIX)) {
            headerValue.substring(TOKEN_PREFIX.length)
        } else null
    }
}