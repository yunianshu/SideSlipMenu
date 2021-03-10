package com.sideslip.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import androidx.recyclerview.widget.RecyclerView

/**
 * created by wyu on 2021/3/8.
 */
class SlideLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    HorizontalScrollView(context, attrs) {
    private var mLeftMenuWidth = 0
    private var mRightMenuWidth = 0
    var isOpen = false
        private set

    fun setLeftMenuWidth(leftMenuWidth: Int) {
        mLeftMenuWidth = leftMenuWidth
    }

    fun setRightMenuWidth(rightMenuWidth: Int) {
        mRightMenuWidth = rightMenuWidth
    }

    fun close() {
        isOpen = false
        smoothScrollTo(mLeftMenuWidth, 0)
    }

    fun openLeftMenu() {
        isOpen = true
        smoothScrollTo(0, 0)
        onOpenMenu()
    }

    fun openRightMenu() {
        isOpen = true
        smoothScrollBy(mRightMenuWidth + mLeftMenuWidth + mRightMenuWidth, 0)
        onOpenMenu()
    }

    val adapter: SlideAdapter
        get() {
            var view: View = this
            while (true) {
                view = view.parent as View
                if (view is RecyclerView) {
                    break
                }
            }
            return (view as RecyclerView).adapter as SlideAdapter
        }

    private fun onOpenMenu() {
        adapter!!.holdOpenItem(this)
    }

    fun closeOpenMenu() {
        if (!isOpen) {
            adapter!!.closeOpenItem()
        }
    }

    var scrollingItem: SlideLayout?
        get() = adapter!!.scrollingItem
        set(scrollingItem) {
            adapter!!.scrollingItem = scrollingItem
        }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        scrollTo(mLeftMenuWidth, 0)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        closeOpenMenu()
        if (scrollingItem != null && scrollingItem !== this) {
            return false
        }
        scrollingItem = this
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downTime = System.currentTimeMillis()
                closeOpenMenu()
                scrollingItem = this
            }
            MotionEvent.ACTION_UP -> {
                scrollingItem = null
                val scrollX = scrollX
                if (System.currentTimeMillis() - downTime <= 100 && scrollX == mLeftMenuWidth) {
                    if (mCustomOnClickListener != null) {
                        mCustomOnClickListener!!.onClick()
                    }
                    return false
                }
                if (scrollX < mLeftMenuWidth / 2) {
                    openLeftMenu()
                }
                if (scrollX >= mLeftMenuWidth / 2 && scrollX <= mLeftMenuWidth + mRightMenuWidth / 2) {
                    close()
                }
                if (scrollX > mLeftMenuWidth + mRightMenuWidth / 2) {
                    openRightMenu()
                }
                return false
            }
        }
        return super.onTouchEvent(ev)
    }

    var downTime: Long = 0

    interface CustomOnClickListener {
        fun onClick()
    }

    private var mCustomOnClickListener: CustomOnClickListener? = null
    fun setCustomOnClickListener(listener: CustomOnClickListener?) {
        mCustomOnClickListener = listener
    }

    init {
        overScrollMode = OVER_SCROLL_NEVER
        isHorizontalScrollBarEnabled = false
    }
}