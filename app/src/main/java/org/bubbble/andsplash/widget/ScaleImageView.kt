package org.bubbble.andsplash.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView
import org.bubbble.andsplash.util.dp

/**
 * @author Andrew
 * @date 2020/09/25 13:43
 */

class ScaleImageView(
    context: Context, attrs: AttributeSet?,
    @AttrRes defStyleAttr: Int
) : AppCompatImageView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context): this(context, null)

    // 比例值
    companion object {
        const val X = 4
        const val Y = 3
    }

    private var width = 0F
    private var height = 0F

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // ceil not round - avoid thin vertical gaps along the left/right edges
        val width = MeasureSpec.getSize(widthMeasureSpec)
        // 高度根据使得图片的宽度充满屏幕计算而得
        val height = width / X * Y
        setMeasuredDimension(width, height)
        this.width = width.toFloat()
        this.height = height.toFloat()
    }

}