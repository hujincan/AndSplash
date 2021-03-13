package org.bubbble.andsplash.ui.preview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ActivityPreviewBinding
import org.bubbble.andsplash.ui.info.InfoActivity
import org.bubbble.andsplash.ui.pictures.PictureItem
import org.bubbble.andsplash.util.doOnApplyWindowInsets
import org.bubbble.andsplash.util.load


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private var picture = R.drawable.photo_3

    private val hideHandler = Handler(Looper.getMainLooper())

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        actionBar?.hide()
    }

    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        binding.actionBar.visibility = View.VISIBLE
        supportActionBar?.show()
    }
    private var isFullscreen: Boolean = true

    private val hideRunnable = Runnable { hide() }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        picture = intent.getIntExtra("photo", R.drawable.photo_3)
        binding.run {
            photoList.setOnClickListener {
                toggle()
            }
        }
        initView()
    }

    private fun initView() {
        val photoAdapter = PreviewAdapter(PreviewAdapter.PreviewViewType.PHOTO)
        val thumbnailAdapter = PreviewAdapter(PreviewAdapter.PreviewViewType.THUMBNAIL)
        val photos = mutableListOf<PictureItem>().apply {
            add(PictureItem(R.drawable.photo_3))
            add(PictureItem(R.drawable.photo_4))
            add(PictureItem(R.drawable.photo_8))
            add(PictureItem(R.drawable.photo_9))
            add(PictureItem(R.drawable.photo_10))
            add(PictureItem(R.drawable.photo_11))
            add(PictureItem(R.drawable.photo_12))
            add(PictureItem(R.drawable.photo_13))
            add(PictureItem(R.drawable.photo_14))
            add(PictureItem(R.drawable.photo_5))
            add(PictureItem(R.drawable.photo_6))
            add(PictureItem(R.drawable.photo_7))
            add(PictureItem(R.drawable.photo_1))
            add(PictureItem(R.drawable.photo_2))
        }
        binding.thumbnailList.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = thumbnailAdapter
        }
        binding.photoList.adapter = photoAdapter
        thumbnailAdapter.submitList(photos)
        photoAdapter.submitList(photos)

    }


    private fun toggle() {
        if (isFullscreen) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        binding.actionBar.visibility = View.INVISIBLE
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    companion object {
        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 100
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_preview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_info -> {
                val intent = Intent(this, InfoActivity::class.java)
                intent.putExtra("photo", picture)
                startActivity(intent)
            }
        }
        return true
    }
}

@BindingAdapter(
    "paddingLeftSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingRightSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    requireAll = false
)
fun applySystemWindows(
    view: View,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean
) {
    view.doOnApplyWindowInsets { view, insets, padding ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

        view.setPadding(
            padding.left + left,
            padding.top + top,
            padding.right + right,
            padding.bottom + bottom
        )
    }
}