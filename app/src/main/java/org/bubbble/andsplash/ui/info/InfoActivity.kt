package org.bubbble.andsplash.ui.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ActivityInfoBinding
import org.bubbble.andsplash.util.dp
import org.bubbble.andsplash.util.load

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            binding.photo.load(intent.getIntExtra("photo", R.drawable.photo_3))

            val tags = arrayOf("design", "app", "app design", "food", "mobile", "ui", "food delivery app", "application", "food app", "mobile app", "mobile app design", "food delivery service")
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