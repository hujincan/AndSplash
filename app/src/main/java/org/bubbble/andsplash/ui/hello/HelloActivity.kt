package org.bubbble.andsplash.ui.hello

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.bubbble.andsplash.base.ViewPagerFragmentAdapter
import org.bubbble.andsplash.ui.MainActivity
import org.bubbble.andsplash.databinding.ActivityHelloBinding
import org.bubbble.andsplash.ui.signin.SignInDialogFragment
import org.bubbble.andsplash.shared.result.EventObserver

@AndroidEntryPoint
class HelloActivity : AppCompatActivity() {

    private val helloViewModel: HelloViewModel by viewModels()
    private lateinit var binding: ActivityHelloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelloBinding.inflate(layoutInflater).apply {
            viewModel = helloViewModel
            pager.adapter = ViewPagerFragmentAdapter(supportFragmentManager)
                .addFragment(WelcomeFragment(), "Welcome")
        }

        helloViewModel.navigateToMainActivity.observe(this, EventObserver {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })

        helloViewModel.navigateToSignInDialogAction.observe(this, EventObserver {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openSignIn", true)
            startActivity(intent)
            finish()
        })
        setContentView(binding.root)
    }

    private fun openSignInDialog() {
        SignInDialogFragment().show(supportFragmentManager, DIALOG_SIGN_IN)
    }

    companion object {
        private const val DIALOG_SIGN_IN = "dialog_sign_in"
    }
}