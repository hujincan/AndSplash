package org.bubbble.andsplash.ui.editor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ActivityEditUserBinding

@AndroidEntryPoint
class EditUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserBinding

    private val viewModel: EditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.currentUserInfo.observe(this, {
            binding.run {
                username.setText(it.username)
                firstName.setText(it.first_name)
                lastName.setText(it.last_name)
                email.setText(it.email)
                portfolioUrl.setText(it.portfolio_url)
                instagramUsername.setText(it.instagram_username)
                location.setText(it.location)
                bio.setText(it.bio)
            }
        })

    }
}