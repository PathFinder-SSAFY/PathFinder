package ssafy.autonomous.pathfinder.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//
//@Configuration
class AWSS3Config {
    @Value("\${cloud.aws.credentials.access-key}")
    private lateinit var accssKey: String;

    @Value("\${cloud.aws.credentials.secret-key}")
    private lateinit var secretKey: String

    @Value("\${cloud.aws.region.static}")
    private lateinit var region: String

    @Bean
    fun BasicAWSCredentials(): BasicAWSCredentials {
        return BasicAWSCredentials(accssKey, secretKey)
    }

    @Bean
    fun amazonS3(basicAWSCredentials: BasicAWSCredentials): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(AWSStaticCredentialsProvider(basicAWSCredentials))
                .build()
    }
}