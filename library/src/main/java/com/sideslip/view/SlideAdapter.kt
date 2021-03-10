package com.sideslip.view

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sideslip.face.*
import com.sideslip.view.ItemView.Companion.create
import com.sideslip.view.ScreenSize.h
import com.sideslip.view.ScreenSize.w
import java.util.*

/**
 * created by wyu on 2021/3/7.
 */
class SlideAdapter private constructor(build: Builder?, recyclerView: RecyclerView) :
    RecyclerView.Adapter<ItemView>() {
    private val mData: MutableList<Any>?
    private val mSlideItems: List<SlideItem>?
    private val mIItemBind: IItemBind?
    private val mHeaderBind: HeaderBind?
    private val mFooterBind: FooterBind?
    private val mIItemType: IItemType?
    private var mHeadFootViewWidth = 0
    private var mItemViewWidth = 0
    private val mHeaders: List<NormalItem>?
    private val mFooters: List<NormalItem>?
    private var mBottomFooter: ItemView? = null
    private var mLoading = false
    private val mBottomListener: BottomListener?
    private val mItemPadding: Int
    private val mRecycleView: RecyclerView

    //侧滑相关
    private var mOpenItem: SlideLayout? = null
    var scrollingItem: SlideLayout? = null
    fun holdOpenItem(openItem: SlideLayout?) {
        mOpenItem = openItem
    }

    fun closeOpenItem() {
        if (mOpenItem != null && mOpenItem!!.isOpen) {
            mOpenItem!!.close()
            mOpenItem = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        if (isHeader(viewType)) {
            return create(parent.context, parent, mHeaders!![viewType - TYPE_HEADER_ORIGIN])
        }
        return if (isFooter(viewType)) {
            create(
                parent.context,
                parent,
                mFooters!![viewType - TYPE_FOOTER_ORIGIN]
            )
        } else create(
            parent.context,
            parent,
            mSlideItems!![viewType - 1]
        )
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        val contentView = holder.contentView
        val contentParams = contentView.layoutParams as LinearLayout.LayoutParams
        if (isHeader(getItemViewType(position))) {
            if (mHeaders!![position].heightRatio > 0) {
                contentParams.height = (h(contentView.context) *
                        mHeaders[position].heightRatio).toInt()
            }
            contentParams.width = mHeadFootViewWidth
            contentView.layoutParams = contentParams
            mHeaderBind?.onBind(holder, position + 1)
            return
        }
        if (isFooter(getItemViewType(position))) {
            if (mFooters!![position - headerNum - mData!!.size].heightRatio > 0) {
                contentParams.height = (h(contentView.context) *
                        mFooters[position - headerNum - mData.size].heightRatio).toInt()
            }
            contentParams.width = mHeadFootViewWidth
            contentView.layoutParams = contentParams
            mFooterBind?.onBind(holder, position - headerNum - mData.size + 1)
            if (position == headerNum + mData.size + footerNum - 1) {
                mBottomFooter = holder
            }
            return
        }
        contentParams.width = mItemViewWidth
        contentView.layoutParams = contentParams
        initLeftRightMenu(holder, position)
        mIItemBind?.bind(
            holder, mData!![position - headerNum]!!,
            position - headerNum
        )
    }

    private fun initLeftRightMenu(holder: ItemView, position: Int) {
        val item = mSlideItems!![getItemViewType(position) - 1]
        val rightMenu = holder.rightMenu
        if (rightMenu != null) {
            val rightMenuParams = rightMenu.layoutParams as LinearLayout.LayoutParams
            rightMenuParams.width = (w(holder.itemView.context) * item.rightMenuRatio).toInt()
            rightMenu.layoutParams = rightMenuParams
            (holder.getView<View>(R.id.yhaolpz_slideLayout) as SlideLayout?)!!.setRightMenuWidth(
                rightMenuParams.width
            )
        }
        val leftMenu = holder.leftMenu
        if (leftMenu != null) {
            val leftMenuParams = leftMenu.layoutParams as LinearLayout.LayoutParams
            leftMenuParams.width = (w(holder.itemView.context) * item.leftMenuRatio).toInt()
            leftMenu.layoutParams = leftMenuParams
            holder.getView<View>(R.id.yhaolpz_slideLayout)!!
                .scrollTo(leftMenuParams.width, 0)
            (holder.getView<View>(R.id.yhaolpz_slideLayout) as SlideLayout?)!!.setLeftMenuWidth(
                leftMenuParams.width
            )
        }
    }

    private fun isHeader(viewType: Int): Boolean {
        return viewType >= TYPE_HEADER_ORIGIN && viewType < TYPE_FOOTER_ORIGIN
    }

    private fun isFooter(viewType: Int): Boolean {
        return viewType >= TYPE_FOOTER_ORIGIN
    }

    val headerNum: Int
        get() = mHeaders?.size ?: 0
    val footerNum: Int
        get() = mFooters?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        if (headerNum > 0 && position < headerNum) {
            return TYPE_HEADER_ORIGIN + position
        }
        if (footerNum > 0 && position >= headerNum + mData!!.size) {
            return TYPE_FOOTER_ORIGIN + position - headerNum - mData.size
        }
        return if (mIItemType == null || mSlideItems!!.size == 1) 1 else mIItemType.type(
            mData!![position - headerNum]!!,
            position - headerNum
        )
    }

    override fun getItemCount(): Int {
        return mData!!.size + headerNum + footerNum
    }

    override fun onViewAttachedToWindow(holder: ItemView) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
            val position = holder.layoutPosition
            lp.isFullSpan =
                isHeader(getItemViewType(position)) || isFooter(getItemViewType(position))
        }
    }

    private fun onBottom() {
        if (mBottomListener != null) {
            if (!mLoading) {
                mLoading = true
                mBottomListener.onBottom(mBottomFooter!!, this@SlideAdapter)
            }
        }
    }

    fun loadMore(data: MutableList<Any>) {
        val pos = mData!!.size + headerNum
        mData.addAll(data)
        if (footerNum == 0) {
            this.notifyItemChanged(pos - 1)
        }
        notifyItemRangeInserted(pos, data.size)
        mLoading = false
    }

    private fun init() {
        mRecycleView.adapter = this
        mRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                scrollingItem = null
                closeOpenItem()
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(1)) {
                        onBottom()
                    }
                }
            }
        })
        if (mItemPadding > 0) {
            mRecycleView.addItemDecoration(SlideItemDecoration(mItemPadding))
        }
        val layoutParams = mRecycleView.layoutParams
        val recyclerViewPadding = mRecycleView.paddingLeft + mRecycleView.paddingRight
        var recyclerViewMargin = 0
        if (layoutParams is MarginLayoutParams) {
            recyclerViewMargin = layoutParams.leftMargin + layoutParams.rightMargin
        }
        val layoutManager = mRecycleView.layoutManager
        mHeadFootViewWidth = w(mRecycleView.context) - recyclerViewMargin - recyclerViewPadding
        if (layoutManager is LinearLayoutManager) {
            mItemViewWidth = mHeadFootViewWidth
        }
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isHeader(getItemViewType(position)) || isFooter(
                            getItemViewType(
                                position
                            )
                        )
                    ) layoutManager.spanCount else 1
                }
            }
            mItemViewWidth = (w(mRecycleView.context) - recyclerViewMargin - recyclerViewPadding
                    - mItemPadding * layoutManager.spanCount * 2) /
                    layoutManager.spanCount
            mHeadFootViewWidth -= mItemPadding * 2
        }
    }

    class Builder {
        var data: MutableList<Any>? = null
        var slideItems: MutableList<SlideItem>? = null
        var itemBind: IItemBind? = null
        var itemType: IItemType? = null
        var bottomListener: BottomListener? = null
        var headers: MutableList<NormalItem>? = null
        var footers: MutableList<NormalItem>? = null
        var headerBind: HeaderBind? = null
        var footerBind: FooterBind? = null
        var itemPadding = 0
        fun load(data: MutableList<Any>?): Builder {
            this.data = data
            return this
        }

        fun item(itemLayoutId: Int): Builder {
            this.item(itemLayoutId, 0, 0f, 0, 0f)
            return this
        }

        fun item(
            itemLayoutId: Int,
            leftMenuLayoutId: Int,
            leftMenuRatio: Float,
            rightMenuLayoutId: Int,
            rightMenuRatio: Float
        ): Builder {
            if (slideItems == null) {
                slideItems = ArrayList()
            }
            slideItems!!.add(
                SlideItem(
                    itemLayoutId,
                    leftMenuLayoutId,
                    leftMenuRatio,
                    rightMenuLayoutId,
                    rightMenuRatio
                )
            )
            return this
        }

        fun header(layoutId: Int): Builder {
            this.header(layoutId, 0f)
            return this
        }

        fun header(layoutId: Int, heightRatio: Float): Builder {
            if (headers == null) {
                headers = ArrayList()
            }
            headers!!.add(NormalItem(layoutId, heightRatio))
            return this
        }

        fun footer(layoutId: Int): Builder {
            this.footer(layoutId, 0f)
            return this
        }

        fun footer(layoutId: Int, heightRatio: Float): Builder {
            if (footers == null) {
                footers = ArrayList()
            }
            footers!!.add(NormalItem(layoutId, heightRatio))
            return this
        }

        fun padding(itemPadding: Int): Builder {
            this.itemPadding = itemPadding
            return this
        }

        fun bind(itemBind: IItemBind): Builder {
            this.itemBind = itemBind
            return this
        }

        fun bind(headerBind: HeaderBind): Builder {
            this.headerBind = headerBind
            return this
        }

        fun bind(footerBind: FooterBind): Builder {
            this.footerBind = footerBind
            return this
        }

        fun type(itemType: IItemType): Builder {
            this.itemType = itemType
            return this
        }

        fun listen(bottomListener: BottomListener): Builder {
            this.bottomListener = bottomListener
            return this
        }

        fun into(recyclerView: RecyclerView): SlideAdapter {
            val adapter = SlideAdapter(mBuilder, recyclerView)
            mBuilder = null
            return adapter
        }
    }

    companion object {
        private const val TYPE_HEADER_ORIGIN = 101
        private const val TYPE_FOOTER_ORIGIN = 201
        private var mBuilder: Builder? = null
        fun load(data: MutableList<Any>?): Builder {
            return builder!!.load(data)
        }

        private val builder: Builder?
            private get() {
                if (mBuilder == null) {
                    mBuilder = Builder()
                }
                return mBuilder
            }
    }

    init {
        mSlideItems = build!!.slideItems
        mIItemBind = build.itemBind
        mIItemType = build.itemType
        mData = build.data
        mHeaders = build.headers
        mFooters = build.footers
        mHeaderBind = build.headerBind
        mFooterBind = build.footerBind
        mBottomListener = build.bottomListener
        mItemPadding = build.itemPadding
        mRecycleView = recyclerView
        init()
    }
}