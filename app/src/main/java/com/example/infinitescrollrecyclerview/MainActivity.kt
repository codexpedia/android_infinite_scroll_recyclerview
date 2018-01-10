package com.example.infinitescrollrecyclerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val linearLayoutManager = LinearLayoutManager(this)
    val itemList = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initial list items
        for (i in 0..20) {
            itemList.add(Item("Item " + i))
        }

        val itemArrayAdapter = ItemArrayAdapter(itemList)
        recyclerview.setLayoutManager(linearLayoutManager)
        recyclerview.setItemAnimator(DefaultItemAnimator())
        recyclerview.setAdapter(itemArrayAdapter)

        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // only load more items if it's currently not loading
                if (!itemArrayAdapter.isLoading()) {
                    // only load more items if the last visible item on the screen is the last item
                    Log.d("endlessscroll", "last visible position: ${linearLayoutManager.findLastCompletelyVisibleItemPosition()}, total count: ${linearLayoutManager.itemCount}")
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() >= linearLayoutManager.itemCount - 1 ) {

                        // add progress bar, the loading footer
                        recyclerview.post {
                            itemArrayAdapter.addFooter()
                        }

                        // load more items after 2 seconds, and remove the loading footer
                        val handler = Handler()
                        handler.postDelayed({
                            itemArrayAdapter.removeFooter()
                            val newItems = ArrayList<Item>()
                            for (i in itemList.size..itemList.size + 19) {
                                newItems.add(Item("Item " + i))
                            }
                            itemArrayAdapter.addItems(newItems)
                        }, 2000)
                    }
                }
            }
        })

    }

}
