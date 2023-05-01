package ssafy.autonomous.pathfinder.global.util

import javax.servlet.http.HttpServletRequest

class HeaderUtil(
        private val HEADER_AUTHORIZATION: String = "Authorization",
        private val TOKEN_PREFIX: String = "Bearer "
) {

    fun getAccessToken(request: HttpServletRequest): String? {
        val headerValue = request.getHeader(HEADER_AUTHORIZATION) ?: return null
        return if (headerValue.startsWith(TOKEN_PREFIX)) {
            headerValue.substring(TOKEN_PREFIX.length)
        } else null
    }
}