package org.bubbble.andsplash.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView
import org.bubbble.andsplash.R
import org.bubbble.andsplash.util.dp


/**
 * @author Andrew
 * @date 2020/09/05 15:20
 * 圆角矩形图片
 */
class SquareImageView(
    context: Context, attrs: AttributeSet?,
    @AttrRes defStyleAttr: Int
) : AppCompatImageView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context): this(context, null)

    private val paint = Paint()
    private val paintStroke = Paint().apply {
        // 抗锯齿
        isAntiAlias = true
        // 填充
        style = Paint.Style.STROKE
        // 颜色
        color = strokeColor
    }
    private var strokeColor = Color.WHITE
    private var strokeWidth = 0F
    private var paddingWidth = 0F.dp
    private var viewMatrix = Matrix()
    private var width = 0F
    private var height = 0F
    private var radius = 6F.dp
    private var rectF: RectF

    init {
        paint.isAntiAlias = true
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView)
        strokeWidth = typeArray.getDimension(R.styleable.SquareImageView_strokeWidth, 0F)
        paddingWidth = typeArray.getDimension(R.styleable.SquareImageView_strokeWidth, 0F) / 2F
        strokeColor = typeArray.getColor(R.styleable.SquareImageView_strokeColor, Color.WHITE)
        rectF = RectF()
        typeArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        if (drawable is BitmapDrawable) {
            paint.shader = initBitmapShader(drawable as BitmapDrawable)
            rectF.set(0F, 0F,
                width, height)
            canvas?.drawRoundRect(rectF, radius, radius, paint)
            if (strokeWidth > 0) {
                paintStroke.color = strokeColor
                paintStroke.strokeWidth = strokeWidth
                rectF.set(0F + paddingWidth, 0F + paddingWidth, width - paddingWidth,
                    height - paddingWidth)
                canvas?.drawRoundRect(rectF, radius, radius, paintStroke)
            }
            return
        }

        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = measuredWidth.toFloat()
        height = measuredHeight.toFloat()
    }

    /**
     * 获取ImageView中资源图片的Bitmap，利用Bitmap初始化图片着色器,通过缩放矩阵将原资源图片缩放到铺满整个绘制区域，避免边界填充
     */
    private fun initBitmapShader(drawable: BitmapDrawable): BitmapShader {
        val bitmap = drawable.bitmap
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val scale = (width / bitmap.width).coerceAtLeast(width / bitmap.height)
        viewMatrix.setScale(scale, scale)
        bitmapShader.setLocalMatrix(viewMatrix)
        return bitmapShader
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        invalidate()
    }
}