package com.doubleslas.fifith.alcohol.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.doubleslas.fifith.alcohol.R
import com.doubleslas.fifith.alcohol.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient


class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val loginViewModel by lazy { LoginViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser

        KakaoSdk.init(this, NATIVE_APP_KEY)

        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()


        activityLoginBinding.btnLoginGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
        }

        activityLoginBinding.btnSignoutGoogle.setOnClickListener {
            firebaseAuth.signOut()
            googleSignInClient.signOut()
        }

        activityLoginBinding.btnLoginKakao.setOnClickListener {
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this) { token, error ->

                    // 액세스 토큰과 리프레시 토큰값 단순 출력
                    Log.i("kakao", "loginWithKakaoTalk $token $error")
                    updateLoginInfo()
                }
            } else {
                LoginClient.instance.loginWithKakaoAccount(this) { token, error ->
                    Log.i("kakao", "loginWithKakaoAccount $token $error")
                    updateLoginInfo()
                }
            }
        }

        activityLoginBinding.btnSignoutKakao.setOnClickListener {
            UserApiClient.instance.logout {
                updateLoginInfo()
            }
        }

        observeAuthenticationState()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                loginViewModel.firebaseAuthWithGoogle(account?.idToken!!)
            } catch (e: ApiException) {
            }
        }
    }


    private fun observeAuthenticationState() {
        loginViewModel.authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    activityLoginBinding.btnLoginGoogle.visibility = View.GONE
                }
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    activityLoginBinding.btnLoginGoogle.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        })
    }


    private fun updateLoginInfo() {
        UserApiClient.instance.me { user, error ->
            user?.let {
                activityLoginBinding.tvKakaoNickname.text = user.kakaoAccount?.profile?.nickname
                Glide.with(this).load(user.kakaoAccount?.profile?.thumbnailImageUrl).circleCrop()
                    .into(activityLoginBinding.profileKakao)
                activityLoginBinding.btnLoginKakao.visibility = View.GONE
                activityLoginBinding.btnSignoutKakao.visibility = View.VISIBLE
            }
            error?.let {
                activityLoginBinding.tvKakaoNickname.text = null
                activityLoginBinding.profileKakao.setImageBitmap(null)
                activityLoginBinding.btnLoginKakao.visibility = View.VISIBLE
                activityLoginBinding.btnSignoutKakao.visibility = View.GONE
            }
        }
    }


    companion object {
        private const val GOOGLE_SIGN_IN = 1001

        // 카카오 네이티브 앱 키
        private const val NATIVE_APP_KEY = "f4ce9d07643e80606b97e6b009366095"

    }

}

