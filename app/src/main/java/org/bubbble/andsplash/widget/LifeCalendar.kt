package org.bubbble.andsplash.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.OverScroller
import androidx.core.view.ViewCompat
import org.bubbble.andsplash.widget.LifeCalendarDrawable.Companion.VERTICAL
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * @author Andrew
 * @date 2020/10/27 10:06
 */
class LifeCalendar(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    private val rect = Rect()

    private var mLastY = 0f

    private var mLastX = 0f

    private var mScroller: OverScroller = OverScroller(context)

    private var mVelocityTracker: VelocityTracker? = null

    private var mTarget: View? = null

    private var mScrollType = ViewCompat.TYPE_TOUCH

    private var mMaximumVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
    private var mMinimumVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity

    /**
     * 负责绘制及动画的Drawable
     */
    private val lifeCalendarDrawable = LifeCalendarDrawable(context, mutableListOf<Int>().apply {

        for (i in 0..4234) {
            add(Color.CYAN)
        }

        add(Color.YELLOW)

        for (i in 0..1834) {
            add(Color.LTGRAY)
        }
    })

    init {
        lifeCalendarDrawable.callback = this
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?:return
        lifeCalendarDrawable.draw(rect, canvas)
    }

    override fun invalidateDrawable(drawable: Drawable) {
        super.invalidateDrawable(drawable)
        if (drawable == lifeCalendarDrawable) {
            invalidate()
            invalidateOutline()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        rect.set(0, 0, measuredWidth, measuredHeight)
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

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (lifeCalendarDrawable.getDrawableHeight() <= rect.height() && lifeCalendarDrawable.getOrientation() == VERTICAL) {
            return false
        }

        initVelocityTrackerIfNotExists()
        mVelocityTracker!!.addMovement(event)
        val action = event.action
        val y = event.y
        val x = event.x

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastY = y
                mLastX = x
            }

            MotionEvent.ACTION_MOVE -> {
                if (lifeCalendarDrawable.getOrientation() == VERTICAL) {
                    val dy = y - mLastY
                    scrollBy(0, (-dy).toInt())
                    mLastY = y
                } else {
                    val dx = x - mLastX
                    scrollBy((-dx).toInt(), 0)
                    mLastX = x
                }
            }

            MotionEvent.ACTION_CANCEL -> {

                recycleVelocityTracker()
            }

            MotionEvent.ACTION_UP -> {

                if (lifeCalendarDrawable.getOrientation() == VERTICAL) {
                    mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                    val velocityY = mVelocityTracker!!.yVelocity.toInt()
                    if (abs(velocityY) > mMinimumVelocity) {
                        fling(-velocityY)
                    }
                    recycleVelocityTracker()
                } else {
                    mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                    val velocityX = mVelocityTracker!!.xVelocity.toInt()
                    if (abs(velocityX) > mMinimumVelocity) {
                        fling(-velocityX)
                    }
                    recycleVelocityTracker()
                }
            }
        }

        return true
    }

    fun fling(velocity: Int) {

        if (lifeCalendarDrawable.getOrientation() == VERTICAL) {
            // 第三步-二小步，fling调整最大Y偏移，以便把fling传递给nestedScrollingChild。
            mScroller.fling(0, scrollY, 0, velocity, 0, 0, 0, Int.MAX_VALUE)
            invalidate()
        } else {
            mScroller.fling(scrollX, 0, velocity, 0, 0, Int.MAX_VALUE, 0, 0)
            invalidate()
        }
    }

    private fun clearSelfFling() {
        if (!mScroller.isFinished) {
            mScroller.abortAnimation()
        }
    }

    private fun clearAllFling() {
        if (!mScroller.isFinished) {
            mScroller.abortAnimation()
        }
        if (mTarget != null) {
            ViewCompat.stopNestedScroll(mTarget!!, mScrollType)
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        if (lifeCalendarDrawable.getOrientation() == VERTICAL) {
            var y = y
            val limit = max(lifeCalendarDrawable.getDrawableHeight(), rect.bottom) - min(lifeCalendarDrawable.getDrawableHeight(), rect.bottom)

            if (y > limit) {
                super.scrollTo(0, limit)
                clearSelfFling()
            }

            if (y < 0) { y = 0 }
            if (y != scrollY && y < limit) {
                super.scrollTo(0, y)
            }
        } else {
            var x = x
            val limit = max(lifeCalendarDrawable.getDrawableWidth(), rect.right) - min(lifeCalendarDrawable.getDrawableWidth(), rect.right)

            if (x > limit) {
                super.scrollTo(limit, 0)
                clearSelfFling()
            }

            if (x < 0) { x = 0 }
            if (x != scrollX && x < limit) {
                super.scrollTo(x, 0)
            }
        }
    }

    override fun computeScroll() {
        if (lifeCalendarDrawable.getOrientation() == VERTICAL) {
            if (mScroller.computeScrollOffset()) {
                scrollTo(0, mScroller.currY)
                invalidate()
                return
            }
        } else {
            if (mScroller.computeScrollOffset()) {
                scrollTo(mScroller.currX, 0)
                invalidate()
                return
            }
        }
    }

}