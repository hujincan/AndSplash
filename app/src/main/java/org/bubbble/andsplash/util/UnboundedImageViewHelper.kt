package org.bubbble.andsplash.util

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.view.*
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.MotionEvent.ACTION_POINTER_UP
import android.widget.ImageView
import android.widget.OverScroller
import androidx.core.view.ViewCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatValueHolder
import org.bubbble.andsplash.shared.util.logger
import org.bubbble.andsplash.widget.LifeCalendarDrawable
import kotlin.math.abs

/**
 * @author  yd
 * @date  2020/3/9 23:02
 */
class UnboundedImageViewHelper private constructor(private val imageView: ImageView): View.OnTouchListener,
    ScaleGestureDetector.OnScaleGestureListener{

    private lateinit var suppMatrix: Matrix

    /**
     * 初始矩阵(单位矩阵)
     */
    private val baseMatrix = Matrix()

    /**
     * 结果矩阵
     */
    private val drawableMatrix = Matrix()

    /**
     * 矩阵的位置Rect
     */
    private val displayRect = RectF()

    /**
     * View的高宽度
     */
    private var viewWidth = 0F

    /**
     * View的高度
     */
    private var viewHeight = 0F

    /**
     * 图像的高宽度
     */
    private var rectWidth = 0F

    /**
     * 图像的高度
     */
    private var rectHeight = 0F

    /**
     * 图像的高度
     */
    private lateinit var initialRect: RectF

    /**
     * 另一个点
     */
    private val offset = PointF(0F, 0F)

    /**
     * 一个点
     */
    private val downTouch = PointF()

    private val recordPointF = PointF()

    // 连击次数
    private var pointSize = 0
    // 按下时间，以此来判断区分点击和长按
    private var touchDownTime = 0L
    // 最大波动范围（手指抖动范围，规避滑动行为）
    private var maxFluctuation = -1
    // 按下位置
    private val touchDownPoint = PointF()
    // 是否激活本次点击
    private var active = false
    // 单次点击允许的最长手指按下时间
    private var maxKeepTime = 300L
    // 连击允许的超时时间
    private var continuouslyKeepTime = 50L
    // 点击事件任务
    private val clickTask = Runnable {
        callOnClick()
    }

    private var mVelocityTracker: VelocityTracker? = null

    private var mMaximumVelocity = ViewConfiguration.get(imageView.context).scaledMaximumFlingVelocity
    private var mMinimumVelocity = ViewConfiguration.get(imageView.context).scaledMinimumFlingVelocity

    /**
     * 触摸点ID
     */
    private var pointerId = 0

    companion object {
        @SuppressLint("ClickableViewAccessibility")
        fun with(view: ImageView): UnboundedImageViewHelper {
            val helper = UnboundedImageViewHelper(view)
            view.setOnTouchListener(helper)
            return helper
        }
    }

    init {
        imageView.post {
            viewWidth = (imageView.width - imageView.paddingLeft - imageView.paddingEnd).toFloat()
            viewHeight = (imageView.height - imageView.paddingTop - imageView.paddingBottom).toFloat()
            suppMatrix = Matrix(imageView.imageMatrix)
            imageView.scaleType = ImageView.ScaleType.MATRIX
            imageView.imageMatrix = getDrawMatrix()
//            notifyImageChange(1F, 0f, 0f)
            centerCrop()
        }
    }

    private val scaleGestureDetector = ScaleGestureDetector(imageView.context, this)

    /**
     * 设置结果矩阵
     */
    private fun notifyImageChange(scaleFactor: Float, focusX: Float, focusY: Float, isFling: Boolean) {
        if (checkMatrixBounds(scaleFactor, focusX, focusY, isFling)) {
            imageView.scaleType = ImageView.ScaleType.MATRIX
            imageView.imageMatrix = getDrawMatrix()
        }
    }

    private fun centerCrop() : Boolean {
        val rect = getDisplayRect(getDrawMatrix()) ?: return false
        initialRect = RectF(rect)
        rectHeight = rect.height()
        rectWidth = rect.width()

        return true
    }

    /**
     * 检擦边界
     */
    private fun checkMatrixBounds(scaleFactor: Float, focusX: Float, focusY: Float, isFling: Boolean): Boolean {
        // Matrix的位置值，默认左上角是0，0
        val rect = getDisplayRect(getDrawMatrix()) ?: return false
        val height = rect.height()
        val width = rect.width()

//        view比 > 1 & rect比 < view比 ->（上下）
//        view比 < 1 & rect比 < view比 ->（上下）

//        view比 > 1 & rect比 > view比 ->（左右）
//        view比 < 1 & rect比 > view比 ->（左右）
        val viewScale = viewWidth / viewHeight
        val rectScale = rect.width() / rect.height()

        var moveX: Float
        var moveY: Float
        if (isFling) {
            moveX = offsetFling.x
            moveY = offsetFling.y
        } else {
            moveX = offset.x - recordPointF.x
            moveY = offset.y - recordPointF.y

        }

        logger("initialRect: left: ${initialRect.left} top: ${initialRect.top} right: ${initialRect.right} bottom: ${initialRect.bottom}")

        if (rectScale < viewScale) {
            if (rect.top + moveY > 0 ) {
                moveY = 0 - rect.top
            }else if (rect.bottom + moveY < rectHeight) {
                moveY = rectHeight - rect.bottom
            }
        } else {
            if (rect.top + moveY > initialRect.top ) {
                moveY = initialRect.top - rect.top
            }else if (rect.bottom + moveY < initialRect.bottom) {
                moveY = initialRect.bottom - rect.bottom
            }
        }

        if (rectScale > viewScale) {
            if (rect.left + moveX > 0){
                moveX = 0 - rect.left
            }else if (rect.right + moveX < rectWidth){
                moveX = rectWidth - rect.right
            }
        } else {
            if (rect.left + moveX > initialRect.left){
                moveX = initialRect.left - rect.left
            }else if (rect.right + moveX < initialRect.right){
                moveX = initialRect.right - rect.right
            }
        }

        suppMatrix.postTranslate(moveX, moveY)
        recordPointF.set(offset.x, offset.y)

        // 缩小
        if (scaleFactor < 1F) {
            // 如果两个对边都已经到达View边界，那么不执行缩小
            if (rect.top == 0F && rect.bottom == rectHeight || rect.left == 0F && rect.right == rectWidth) return true

            // 是否可以缩小
            if (width * scaleFactor > rectWidth && height * scaleFactor > rectHeight) {
                suppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)

                imageView.scaleType = ImageView.ScaleType.MATRIX
                imageView.imageMatrix = getDrawMatrix()
                notifyImageChange(1F, 0F, 0F, false)
                return false
            }

        }else if (scaleFactor > 1F) {
            suppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
        }
        return true
    }

    /**
     * 获取Matrix的位置值
     */
    private fun getDisplayRect(matrix: Matrix): RectF? {
        return imageView.drawable?.let { drawable ->
            // 初始displayRect
            displayRect.set(0F, 0F, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
            // 映射矩阵的位置Rect
            matrix.mapRect(displayRect)
            displayRect
        }
    }

    /**
     * 设置结果矩阵并返回
     */
    private fun getDrawMatrix(): Matrix {
        drawableMatrix.set(baseMatrix)
        drawableMatrix.postConcat(suppMatrix)
        return drawableMatrix
    }

    private var pointerUp = false

    private val clickListenerList = ArrayList<ClickListener>()

    private fun callOnClick() {
        if (pointSize < 1) {
            return
        }
        clickListenerList.forEach {
            it.onClick(imageView, pointSize)
        }
        // 事件被消耗，清空
        reset()
    }

    /**
     * 点击成功
     */
    private fun clickSuccessful() {
        active = false
        pointSize++
        imageView.removeCallbacks(clickTask)
//        imageView.postDelayed(clickTask, maxKeepTime + continuouslyKeepTime)
        imageView.post(clickTask)
    }

    /**
     * 重置
     */
    private fun reset() {
        pointSize = 0
        imageView.removeCallbacks(clickTask)
        touchDownTime = 0L
        touchDownPoint.set(0F, 0F)
        active = false
    }

    fun addClickListener(listener: ClickListener) {
        clickListenerList.add(listener)
    }

    interface ClickListener {
        fun onClick(view: View, count: Int)
    }

    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    private val flingValueHolderX = FloatValueHolder()
    private val flingValueHolderY = FloatValueHolder()

    private val offsetFling = PointF(0F, 0F)
    private val recordFling = PointF(0F, 0F)

    private val flingAnimationX = FlingAnimation(flingValueHolderX)
    private val flingAnimationY = FlingAnimation(flingValueHolderY)

    private fun fling(velocityX: Float, velocityY: Float) {

        flingAnimationX.cancel()
        flingAnimationY.cancel()
        flingValueHolderX.value = 0.0f
        flingValueHolderY.value = 0.0f
        offsetFling.set(0F, 0F)
        recordFling.set(0F, 0F)

        logger("velocityX: $velocityX  velocityY: $velocityY")
        flingAnimationX.apply {
            setStartVelocity(velocityX)
            setMinValue(Float.NEGATIVE_INFINITY)
            setMaxValue(Float.MAX_VALUE)
            friction = 1F
            minimumVisibleChange = DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS
            addUpdateListener { _, value, _ ->
                offsetFling.x = -(value - recordFling.x)
                recordFling.x = value
                logger("flingValueX: ${offsetFling.x}")

                notifyImageChange(1F, 0F, 0F, true)
            }
            start()
        }

        flingAnimationY.apply {
            setStartVelocity(velocityY)
            setMinValue(Float.NEGATIVE_INFINITY)
            setMaxValue(Float.MAX_VALUE)
            friction = 1F
            minimumVisibleChange = DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS
            addUpdateListener { _, value, _ ->
                offsetFling.y = -(value - recordFling.y)
                recordFling.y = value
                logger("flingValueY: ${offsetFling.y}")

                notifyImageChange(1F, 0F, 0F, true)
            }
            start()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event?:return false
        v?:return false
        if (v.parent is ViewGroup){
            v.parent.requestDisallowInterceptTouchEvent(true)
        }

        initVelocityTrackerIfNotExists()
        mVelocityTracker!!.addMovement(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                flingAnimationX.cancel()
                flingAnimationY.cancel()
                downTouch.set(event.xById(), event.yById())
                // 按下，记录信息
                touchDownTime = System.currentTimeMillis()
                touchDownPoint.set(event.x, event.y)
                active = true
            }
            MotionEvent.ACTION_MOVE -> {

                if (pointerUp){
                    downTouch.set(event.xById(), event.yById())
                    pointerUp =! pointerUp
                }
                val x = event.xById()
                val y = event.yById()
                offset.x += x - downTouch.x
                offset.y += y - downTouch.y
                downTouch.set(x, y)
                notifyImageChange(1F, 0F, 0F, false)

                // 规避滑动
                if (maxFluctuation > 0 && (abs(x - touchDownPoint.x) > maxFluctuation ||
                            abs(y - touchDownPoint.y) > maxFluctuation)) {
                    reset()
                }
                if (x < 0 || y < 9 || x > viewWidth || y > viewHeight) {
                    reset()
                }
                // 发生超时，提前清理任务
                val now = System.currentTimeMillis()
                if (now - touchDownTime > maxKeepTime) {
                    reset()
                }
            }
            MotionEvent.ACTION_UP -> {

                mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val velocityY = mVelocityTracker!!.yVelocity
                val velocityX = mVelocityTracker!!.xVelocity
                if (abs(velocityY) > mMinimumVelocity || abs(velocityX) > mMinimumVelocity) {
                    fling(-velocityX, -velocityY)
                }
                recycleVelocityTracker()

                val now = System.currentTimeMillis()
                if (now - touchDownTime > maxKeepTime) {
                    reset()
                } else {
                    clickSuccessful()
                }
            }
            ACTION_POINTER_UP -> {
                pointerUp = true
                recordPointF.set(0F, 0F)
                offset.set(0F, 0F)

            }
            MotionEvent.ACTION_CANCEL -> {
                // 触发取消时间，放弃本轮所有计数
                reset()
                recycleVelocityTracker()
                return false
            }
            ACTION_POINTER_DOWN -> {
                // 多个指头，放弃事件
                reset()
                return false
            }
        }
        return scaleGestureDetector.onTouchEvent(event)
    }

    private fun MotionEvent.checkPointId() {
        val pointerIndex = this.findPointerIndex(pointerId)
        if (pointerIndex < 0) {
            pointerId = this.getPointerId(0)
        }
    }

    private fun MotionEvent.xById(): Float {
        checkPointId()
        return this.getX(this.findPointerIndex(pointerId))
    }

    private fun MotionEvent.yById(): Float {
        checkPointId()
        return getY(this.findPointerIndex(pointerId))
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
    }

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        detector?: return false
        val scaleFactor = detector.scaleFactor
        //中心点
        val focusX =  detector.focusX
        val focusY = detector.focusY

        if (java.lang.Float.isNaN(scaleFactor) || java.lang.Float.isInfinite(scaleFactor)) return false

        notifyImageChange(scaleFactor, focusX, focusY, false)
        return true
    }

}