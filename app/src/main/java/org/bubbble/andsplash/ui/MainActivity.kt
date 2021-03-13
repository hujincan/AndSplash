package org.bubbble.andsplash.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ActivityMainBinding
import org.bubbble.andsplash.shared.data.ConnectionURL
import org.bubbble.andsplash.shared.result.EventObserver
import org.bubbble.andsplash.shared.util.logger
import org.bubbble.andsplash.ui.signin.SignInDialogFragment
import org.bubbble.andsplash.ui.signin.SignOutDialogFragment


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val DIALOG_SIGN_IN = "dialog_sign_in"
        private const val DIALOG_SIGN_OUT = "dialog_sign_out"

    }

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 开启动画
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(
            MaterialContainerTransformSharedElementCallback()
        )
        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.navigateToSignInDialogAction.observe(this, EventObserver {
            logger("openSignInDialog")
            openSignInDialog()
        })

        viewModel.navigateToSignOutDialogAction.observe(this, EventObserver {
            logger("openSignOutDialog")
            openSignOutDialog()
        })

    }

    override fun onStart() {
        super.onStart()
        if (intent.getBooleanExtra("openSignIn", false)) {
            intent.removeExtra("openSignIn")
            openSignInDialog()
        }
    }

    // 解决容器共享动画退出时动画问题
    override fun finishAfterTransition() {
        super.finish()
    }

    private fun openSignInDialog() {
        SignInDialogFragment().show(supportFragmentManager, DIALOG_SIGN_IN)
    }

    private fun openSignOutDialog() {
        SignOutDialogFragment().show(supportFragmentManager, DIALOG_SIGN_OUT)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null && intent.data != null && !TextUtils.isEmpty(intent.data!!.authority)
            && ConnectionURL.UNSPLASH_LOGIN_CALLBACK == intent.data!!.authority
        ) {
            val code = intent.data?.getQueryParameter("code")
            code?.let {
                viewModel.signIn(it)
            }
        }
    }
}