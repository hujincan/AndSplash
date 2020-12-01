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

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        // 展开状态使用状态栏和导航栏暗色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_VISIBLE)
        }
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.WHITE

        //API支持就更改Android NavigationBar Color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        }else{
            window.navigationBarColor = Color.BLACK
        }

        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val picture = intent.getIntExtra("photo", R.drawable.photo_3)
        binding.run {
            photo.load(picture)

            photo.setOnClickListener {
                val intent = Intent(this@InfoActivity, PreviewActivity::class.java)
                intent.putExtra("photo", picture)
                startActivity(intent)
            }

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