//package ssafy.autonomous.pathfinder.config
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//
//@Configuration
//class AWSS3Config {
//    @Value("\${cloud.aws.credentials.access-key}")
//    private lateinit var accssKey: String;
//
//    @Value("\${cloud.aws.credentials.secret-key}")
//    private lateinit var secretKey: String
//
//    @Value("\${cloud.aws.region.static}")
//    private lateinit var region: String
//
//    @Primary
//    @Bean
//    fun amazonS3Client(): Amazon? {
//        val awsCredentials = BasicAWSCredentials(accessKey, secretKey)
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(region)
//                .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
//                .build() as AmazonS3Client
//    }
//}