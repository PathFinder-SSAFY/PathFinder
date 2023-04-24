package com.dijkstra.pathfinder.screen.login

import android.util.Log
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
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel

private const val TAG = "LoginViewModel_싸피"

class LoginViewModel (private val application: Application) : AndroidViewModel(application) {

    // https://github.com/naver/naveridlogin-sdk-android/blob/main/Samples/src/main/java/com/navercorp/nid/oauth/sample/MainActivity.kt

    private fun startNaverLogin() {
        val context = application.applicationContext
        var naverToken: String? = ""

        val naverLoginCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(result: NidProfileResponse) {
                val userId = result.profile?.id
                Log.d(TAG, "onSuccess: ${result}")
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
                naverToken = NaverIdLoginSDK.getAccessToken()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(naverLoginCallback)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

        }

        NaverIdLoginSDK.apply {
            initialize(
                context,
                BuildConfig.NAVER_CLIENT_ID,
                BuildConfig.NAVER_CLIENT_SECRET,
                context.getString(R.string.app_name)
            )
            authenticate(context, oAuthLoginCallback)
        }
    } // End of startNaverLogin

}