package org.bubbble.andsplash.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.bubbble.andsplash.ui.hello.HelloActivity
import org.bubbble.andsplash.shared.result.EventObserver

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: LaunchViewModel by viewModels()

        viewModel.launchDestination.observe(this, EventObserver { destination ->
            when (destination) {
                LaunchDestination.HELLO_ACTIVITY ->  startActivity(Intent(this, HelloActivity::class.java))
                LaunchDestination.MAIN_ACTIVITY -> startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        })
    }
}