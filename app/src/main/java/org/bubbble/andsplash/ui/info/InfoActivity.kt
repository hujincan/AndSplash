package org.bubbble.andsplash.ui.info

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ActivityInfoBinding
import org.bubbble.andsplash.ui.preview.PreviewActivity
import org.bubbble.andsplash.util.dp
import org.bubbble.andsplash.util.load

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        binding = ActivityInfoBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "照片信息"
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {

            val tags = arrayOf("design", "app", "app design", "food", "mobile", "ui", "food delivery app", "app", "app design", "food", "mobile", "ui", "food delivery app", "application", "food app", "mobile app", "mobile app design", "food delivery service")
            for (value in tags) {
                tagGroup.addView(Chip(tagGroup.context).apply {
                    text = value
                    isClickable = true
                    isFocusable = true
                    chipMinHeight = 36F.dp
                    chipBackgroundColor = ContextCompat.getColorStateList(this@InfoActivity, R.color.white)
                    chipStrokeWidth = 1F.dp
                    chipStrokeColor = ContextCompat.getColorStateList(this@InfoActivity, R.color.stroke_color)
                })
            }
        }
    }
}