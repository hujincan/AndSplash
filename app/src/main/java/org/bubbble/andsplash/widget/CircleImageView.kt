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
class CircleImageView(
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
        style = Paint.Style.FILL
        // 颜色
        color = strokeColor
    }
    private var viewMatrix = Matrix()
    private var width = 0F
    private var height = 0F
    private var radius = 0F
    private var strokeColor = Color.TRANSPARENT
    private var strokeWidth = 0F
    private var paddingWidth = 0F.dp

    init {
        paint.isAntiAlias = true
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
        strokeWidth = typeArray.getDimension(R.styleable.CircleImageView_strokeWidth, 0F)
        paddingWidth = typeArray.getDimension(R.styleable.CircleImageView_strokeWidth, 0F)
        strokeColor = typeArray.getColor(R.styleable.CircleImageView_strokeColor, Color.TRANSPARENT)
        typeArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = measuredWidth.toFloat()
        height = measuredHeight.toFloat()
        radius = (width.coerceAtMost(height) / 2)
    }

    override fun onDraw(canvas: Canvas?) {
        if (drawable is BitmapDrawable) {
            if (strokeWidth > 0) {
                paintStroke.color = strokeColor
                canvas?.drawCircle(width / 2F, width / 2F, width / 2F, paintStroke)
                paintStroke.color = Color.WHITE
                canvas?.drawCircle(width / 2F, width / 2F, width / 2F - paddingWidth, paintStroke)
            }
            paint.shader = initBitmapShader(drawable as BitmapDrawable) // 将着色器设置给画笔
            canvas!!.drawCircle(
                width / 2F,
                width / 2F,
                radius - strokeWidth - paddingWidth - paddingStart,
                paint
            ) // 使用画笔在画布上画圆
            return
        }

        super.onDraw(canvas)
    }

    /**
     * 获取ImageView中资源图片的Bitmap，利用Bitmap初始化图片着色器,通过缩放矩阵将原资源图片缩放到铺满整个绘制区域，避免边界填充
     */
    private fun initBitmapShader(drawable: BitmapDrawable): BitmapShader {
        val bitmap = drawable.bitmap
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val scale = ((width - paddingStart - paddingEnd) / bitmap.width).coerceAtLeast((width - paddingStart - paddingEnd) / bitmap.height)
        viewMatrix.setScale(scale, scale)
        viewMatrix.setTranslate(paddingStart.toFloat(), paddingStart.toFloat())
        bitmapShader.setLocalMatrix(viewMatrix)
        return bitmapShader
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        invalidate()
    }
}