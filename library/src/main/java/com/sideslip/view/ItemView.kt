package com.sideslip.view

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * created by wyu on 2021/3/6.
 */
class ItemView internal constructor(
    private val mItemView: View,
    val contentView: View,
    val leftMenu: View?,
    val rightMenu: View?
) : RecyclerView.ViewHolder(
    mItemView
) {
    private val mViews: SparseArray<View?>
    fun <T : View?> getView(viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = mItemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    fun setText(viewId: Int, text: String?): ItemView {
        val textView = getView<TextView>(viewId)
        if (textView != null) {
            textView.text = text
        }
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): ItemView {
        val view = getView<ImageView>(viewId)
        view?.setImageResource(resId)
        return this
    }

    fun setOnClickListener(viewId: Int, listener: View.OnClickListener?): ItemView {
        val view = getView<View>(viewId)
        view?.setOnClickListener(listener)
        return this
    }

    fun setOnLongClickListener(viewId: Int, listener: OnLongClickListener?): ItemView {
        val view = getView<View>(viewId)
        view?.setOnLongClickListener(listener)
        return this
    }

    fun setOnClickListener(listener: View.OnClickListener): ItemView {
        (getView<View>(R.id.yhaolpz_slideLayout) as SlideLayout).setCustomOnClickListener(object :
            SlideLayout.CustomOnClickListener {
            override fun onClick() {
                listener?.onClick(mItemView)
            }
        })
        return this
    }

    fun closeMenu(): ItemView {
        (getView<View>(R.id.yhaolpz_slideLayout) as SlideLayout).adapter.closeOpenItem()
        return this
    }

    fun openMenu(): ItemView {
        (getView<View>(R.id.yhaolpz_slideLayout) as SlideLayout).openRightMenu()
        return this
    }

    companion object {
        @JvmStatic
        fun create(context: Context, parent: ViewGroup, normalItem: NormalItem): ItemView {
            return create(context, parent, SlideItem(normalItem.layoutId, 0, 0f, 0, 0f))
        }

        @JvmStatic
        fun create(context: Context, parent: ViewGroup, slideItem: SlideItem): ItemView {
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.yhaolpz_slide_layout, parent, false)
            val linearLayout = itemView.findViewById<LinearLayout>(R.id.yhaolpz_linearLayout)
            val content: View
            var leftMenu: View? = null
            var rightMenu: View? = null
            if (slideItem.leftMenuRatio != 0f) {
                leftMenu = LayoutInflater.from(context)
                    .inflate(slideItem.leftMenuLayoutId, linearLayout, false)
                linearLayout.addView(leftMenu)
            }
            content =
                LayoutInflater.from(context).inflate(slideItem.itemLayoutId, linearLayout, false)
            linearLayout.addView(content)
            if (slideItem.rightMenuLayoutId != 0) {
                rightMenu = LayoutInflater.from(context)
                    .inflate(slideItem.rightMenuLayoutId, linearLayout, false)
                linearLayout.addView(rightMenu)
            }
            return ItemView(itemView, content, leftMenu, rightMenu)
        }
    }

    init {
        mViews = SparseArray()
    }
}