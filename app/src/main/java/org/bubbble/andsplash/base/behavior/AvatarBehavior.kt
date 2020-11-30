package org.bubbble.andsplash.base.behavior

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.bubbble.andsplash.R
import org.bubbble.andsplash.util.Utils.getStatusBarHeight
import org.bubbble.andsplash.util.half
import org.bubbble.andsplash.widget.CircleImageView

/**
 * @author Andrew
 * @date 2020/09/29 21:24
 */
class AvatarBehavior(private val mContext: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<CircleImageView>(
        mContext, attrs
    ) {
    // 整个滚动的范围
    private var mTotalScrollRange = 0

    // AppBarLayout高度
    private var mAppBarHeight = 0

    // AppBarLayout宽度
    private var mAppBarWidth = 0

    // 控件原始大小
    private var mOriginalSize = 0

    // 控件最终大小
    private var mFinalSize = 0

    // 控件最终缩放的尺寸,设置坐标值需要算上该值
    private var mScaleSize = 0f

    // 原始x坐标
    private var mOriginalX = 0f

    // 最终x坐标
    private var mFinalX = 0f

    // 起始y坐标
    private var mOriginalY = 0f

    // 最终y坐标
    private var mFinalY = 0f

    // ToolBar高度
    private var mToolBarHeight = 0

    // AppBar的起始Y坐标
    private var mAppBarStartY = 0f

    // 滚动执行百分比[0~1]
    private var mPercent = 0f

    // Y轴移动插值器
    private val mMoveYInterpolator: DecelerateInterpolator

    // X轴移动插值器
    private val mMoveXInterpolator: AccelerateInterpolator

    // 最终变换的视图，因为在5.0以上AppBarLayout在收缩到最终状态会覆盖变换后的视图，所以添加一个最终显示的图片
    private var mFinalView: CircleImageView? = null

    // 最终变换的视图离底部的大小
    private var mFinalViewMarginBottom = 0
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: CircleImageView,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: CircleImageView,
        dependency: View
    ): Boolean {
        if (dependency is AppBarLayout) {
            _initVariables(child, dependency)
            mPercent = (mAppBarStartY - dependency.getY()) * 1.0f / mTotalScrollRange
            val percentY = mMoveYInterpolator.getInterpolation(mPercent)
            setViewY(child, mOriginalY, mFinalY - mScaleSize, percentY)
            if (mPercent > ANIM_CHANGE_POINT) {
                val scalePercent = (mPercent - ANIM_CHANGE_POINT) / (1 - ANIM_CHANGE_POINT)
                val percentX = mMoveXInterpolator.getInterpolation(scalePercent)
                scaleView(child, mOriginalSize.toFloat(), mFinalSize.toFloat(), scalePercent)
                setViewX(child, mOriginalX, mFinalX - mScaleSize, percentX)
            } else {
                scaleView(child, mOriginalSize.toFloat(), mFinalSize.toFloat(), 0f)
                setViewX(child, mOriginalX, mFinalX - mScaleSize, 0f)
            }
            if (mFinalView != null) {
                if (percentY == 1.0f) {
                    // 滚动到顶时才显示
                    mFinalView!!.visibility = View.VISIBLE
                } else {
                    mFinalView!!.visibility = View.GONE
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && dependency is CollapsingToolbarLayout) {
            // 大于5.0才生成新的最终的头像，因为5.0以上AppBarLayout会覆盖变换后的头像
            if (mFinalView == null && mFinalSize != 0 && mFinalX != 0f && mFinalViewMarginBottom != 0) {
                mFinalView = CircleImageView(mContext)
                mFinalView!!.visibility = View.GONE
                // 添加为CollapsingToolbarLayout子视图
                dependency.addView(mFinalView)
                val params = mFinalView!!.layoutParams as FrameLayout.LayoutParams
                // 设置大小
                params.width = mFinalSize
                params.height = mFinalSize
                // 设置位置，最后显示时相当于变换后的头像位置
                params.gravity = Gravity.TOP
                params.leftMargin = mFinalX.toInt()
                params.bottomMargin = mFinalViewMarginBottom
                mFinalView!!.layoutParams = params
                mFinalView!!.setImageDrawable(child.drawable)
            }
            if (mFinalView != null && mFinalSize != 0 && mFinalX != 0f && mFinalViewMarginBottom != 0) {
                mFinalView!!.setImageDrawable(child.drawable)
            }
        }
        return true
    }

    /**
     * 初始化变量
     *
     * @param child
     * @param dependency
     */
    private fun _initVariables(child: CircleImageView, dependency: View) {
        if (mAppBarHeight == 0) {
            mAppBarHeight = dependency.height
            mAppBarStartY = dependency.y
        }
        if (mTotalScrollRange == 0) {
            mTotalScrollRange = (dependency as AppBarLayout).totalScrollRange
        }
        if (mOriginalSize == 0) {
            mOriginalSize = child.width
        }
        if (mFinalSize == 0) {
            mFinalSize = mContext.resources.getDimensionPixelSize(R.dimen.avatar_final_size)
        }
        if (mAppBarWidth == 0) {
            mAppBarWidth = dependency.width
        }
        if (mOriginalX == 0f) {
            mOriginalX = child.x
        }
        if (mFinalX == 0f) {
//            mFinalX = mContext.resources.getDimensionPixelSize(R.dimen.avatar_final_x).toFloat()
            mFinalX = dependency.width.toFloat().half - (mFinalSize / 2)
        }
        if (mOriginalY == 0f) {
            mOriginalY = child.y
        }
        if (mFinalY == 0f) {
            if (mToolBarHeight == 0) {
                mToolBarHeight = mContext.resources.getDimensionPixelSize(R.dimen.toolbar_height)
            }
            mFinalY = (mToolBarHeight - mFinalSize) / 2 + mAppBarStartY
        }
        if (mScaleSize == 0f) {
            mScaleSize = (mOriginalSize - mFinalSize) * 1.0f / 2
        }
        if (mFinalViewMarginBottom == 0) {
            mFinalViewMarginBottom = (mToolBarHeight - mFinalSize) / 2
        }
    }

    private fun setViewX(view: View, originalX: Float, finalX: Float, percent: Float) {
        val calcX = (finalX - originalX) * percent + originalX
        view.x = calcX
    }

    private fun setViewY(view: View, originalY: Float, finalY: Float, percent: Float) {
        val calcY = (finalY - originalY) * percent + originalY
        view.y = calcY
    }

    companion object {
        // 缩放动画变化的支点
        private const val ANIM_CHANGE_POINT = 0.2f
        fun scaleView(view: View, originalSize: Float, finalSize: Float, percent: Float) {
            val calcSize = (finalSize - originalSize) * percent + originalSize
            val caleScale = calcSize / originalSize
            view.scaleX = caleScale
            view.scaleY = caleScale
        }
    }

    init {
        mMoveYInterpolator = DecelerateInterpolator()
        mMoveXInterpolator = AccelerateInterpolator()
        if (attrs != null) {
            val a = mContext.obtainStyledAttributes(attrs, R.styleable.AvatarBehavior)
            mFinalSize = a.getDimension(R.styleable.AvatarBehavior_finalSize, 0f).toInt()
            mFinalX = a.getDimension(R.styleable.AvatarBehavior_finalXx, 0f)
            mToolBarHeight = a.getDimension(R.styleable.AvatarBehavior_toolBarHeight, 0f).toInt()
            a.recycle()
        }
    }
}