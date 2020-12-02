package org.bubbble.andsplash.ui.personal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ActivityPersonalBinding

class PersonalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}