package ssafy.autonomous.pathfinder.domain.oauth.exception

class TokenValidFailedException : RuntimeException {
    constructor() : super("Failed to generate Token.") {}
    private constructor(message: String) : super(message) {}
}