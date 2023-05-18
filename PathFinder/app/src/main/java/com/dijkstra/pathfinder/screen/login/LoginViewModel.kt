package com.dijkstra.pathfinder.screen.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.dijkstra.pathfinder.Application
import com.dijkstra.pathfinder.BuildConfig
import com.dijkstra.pathfinder.R
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfile
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel

private const val TAG = "LoginViewModel_싸피"

class LoginViewModel() : ViewModel() {
    var naverToken = mutableStateOf("")
    var loginStatus = mutableStateOf(false)
    var userId =
        mutableStateOf(NidProfile(
            null, null, null, null, null, null,
            null, null, null, null, null
        ))

    // https://github.com/naver/naveridlogin-sdk-android/blob/main/Samples/src/main/java/com/navercorp/nid/oauth/sample/MainActivity.kt
    fun startNaverLogin(context: Context) {

        val naverLoginCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(result: NidProfileResponse) {
                val userId = result.profile
                Log.d(TAG, "onSuccess: ${userId?.id}")
                Log.d(TAG, "onSuccess: ${userId?.email}")
                Log.d(TAG, "onSuccess: ${userId}")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        } // End of naverLoginCallback

        val oAuthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                naverToken.value = NaverIdLoginSDK.getAccessToken() ?: ""
                loginStatus.value = true

                Log.d(TAG, "AccessToken: ${NaverIdLoginSDK.getAccessToken()}")
                Log.d(TAG, "RefreshToken: ${NaverIdLoginSDK.getRefreshToken()}")
                Log.d(TAG, "Expires: ${NaverIdLoginSDK.getExpiresAt()}")
                Log.d(TAG, "Type: ${NaverIdLoginSDK.getTokenType()}")
                Log.d(TAG, "State: ${NaverIdLoginSDK.getState()}")

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(naverLoginCallback)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

                Log.e(
                    TAG,
                    "onFailure: ErrorCode ${errorCode},\n ErrorDescription $errorDescription"
                )
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        } // End of oAuthLoginCallback

        NaverIdLoginSDK.apply {
            showDevelopersLog(true)
            initialize(
                context,
                BuildConfig.NAVER_CLIENT_ID,
                BuildConfig.NAVER_CLIENT_SECRET,
                context.getString(R.string.app_name)
            )
            authenticate(context, oAuthLoginCallback)
        }
    } // End of startNaverLogin

    fun startNaverLogout() {
        NaverIdLoginSDK.logout()
        loginStatus.value = false
    }


}