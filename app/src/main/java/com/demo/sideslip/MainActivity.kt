package com.demo.sideslip

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sideslip.face.BottomListener
import com.sideslip.face.FooterBind
import com.sideslip.face.HeaderBind
import com.sideslip.view.ItemBind
import com.sideslip.view.ItemType
import com.sideslip.view.ItemView
import com.sideslip.view.SlideAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val data: MutableList<DataBean> = ArrayList<DataBean>()
    val data2: MutableList<DataBean> = ArrayList<DataBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycle_view.layoutManager = LinearLayoutManager(this);

        initData()
        initRecycleView();
    }

    private fun initData() {
        for (i in 0..29) {
            data.add(DataBean("我是第" + i + "个item"))
        }

        for (i in 0..4) {
            data2.add(DataBean("我是一个新item"))
        }
    }

    private fun initRecycleView() {
        val itemBind: ItemBind<DataBean> = object : ItemBind<DataBean>() {
            override fun onBind(itemView: ItemView, data: DataBean, position: Int) {
                if (position % 2 == 0) {
                    itemView.setText(R.id.device_txt, data.info)
                        .setOnClickListener(View.OnClickListener {
                            showToast("click  1");
                        })
                        .setOnClickListener(R.id.device_btn, View.OnClickListener {
                            itemView.closeMenu()
                            itemView.openMenu()
                        })
                        .setOnClickListener(R.id.delIt,
                            View.OnClickListener {
                                showToast("click : delIt 2");
                            })
                } else {
                    itemView
                        .setOnClickListener(View.OnClickListener {
                            showToast("click : 3");
                        })
                        .setOnClickListener(R.id.delete, View.OnClickListener {
                            showToast("click : 点击喜欢菜单");
                            itemView.closeMenu()
                        })
                        .setOnClickListener(R.id.delIt,
                            View.OnClickListener {
                                showToast("click : 点击喜欢菜单 3");
                            })
                }
            }
        }

        SlideAdapter.load(data)
            .item(R.layout.item1)
            .item(R.layout.item, 0, 0f, R.layout.menu, 1.0f)
            .type(object : ItemType<DataBean>() {
                override fun getItemOrder(data: DataBean, position: Int): Int {
                    return if (position % 2 == 0) 1 else 2
                }
            })
            .padding(1)
            .footer(R.layout.foot, 0.1f)
            .bind(itemBind)
            .bind(object : HeaderBind {
                override fun onBind(header: ItemView, order: Int) {
                    header.setOnClickListener(
                        R.id.headText
                    ) {
                        showToast("head click");
                    }
                }
            }).bind(object : FooterBind {
                override fun onBind(footer: ItemView, order: Int) {
                    footer.setOnClickListener(
                        R.id.footerText
                    ) {
                        showToast("foot click ");
                    }
                }
            }).listen(object : BottomListener {
                override fun onBottom(footer: ItemView, slideAdapter: SlideAdapter) {
                    footer.setText(R.id.footerText, "正在加载，请稍后...")
                    Thread {
                        try {
                            Thread.sleep(1000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                        runOnUiThread {
                            slideAdapter.loadMore(data2)
                            footer.setText(R.id.footerText, "正在加载")
                        }
                    }.start()
                }
            })
            .into(recycle_view)

    }


    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}