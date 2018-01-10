package com.example.infinitescrollrecyclerview

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item_progressbar.view.*
import java.util.ArrayList

class ItemArrayAdapter(private var itemList: ArrayList<Item> = ArrayList()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val REGULAR_ITEM = 0
    val FOOTER_ITEM = 1

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemList[position]
        if (item.type == REGULAR_ITEM) {
            return REGULAR_ITEM
        } else if (item.type == FOOTER_ITEM) {
            return FOOTER_ITEM
        }
        throw Exception("Error, unknown view type")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == REGULAR_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return RegularViewHolder(view)
        } else if (viewType == FOOTER_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_progressbar, parent, false)
            return FooterViewHolder(view)
        } else {
            throw RuntimeException("The type has to be ONE or TWO")
        }
    }

    // load data in each row element
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            REGULAR_ITEM -> {
                holder as RegularViewHolder
                holder.item.text = itemList[position].name
            }
            FOOTER_ITEM -> {
                // no data need to be assigned
            }
            else -> {
                // no data need to be assigned
            }
        }
    }

    // this is required to be called right before loading more items
    fun addFooter() {
        Log.d("endlessscroll", "addFooter")
        if (!isLoading()) {
            itemList.add(Item("Footer", 1))
            notifyItemInserted(itemList.size - 1)
        }
    }

    // this is required to be called right after finish loading the items
    fun removeFooter() {
        Log.d("endlessscroll", "removeFooter")
        if (isLoading()) {
            itemList.removeAt(itemList.size - 1)
            notifyItemRemoved(itemList.size - 1)
        }
    }

    // it is loading if the last item is footer
    fun isLoading() : Boolean {
        return itemList.last().type == FOOTER_ITEM
    }

    fun addItems(items : ArrayList<Item>) {
        val lastPos = itemList.size - 1
        itemList.addAll(items)
        notifyItemRangeInserted(lastPos, items.size)
    }

    inner class RegularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var item: TextView

        init {
            itemView.setOnClickListener(this)
            item = itemView.findViewById<View>(R.id.textview) as TextView
        }

        override fun onClick(view: View) {
            Log.d("onclick", "onClick " + layoutPosition + " " + item.text)
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar = itemView.progressbar
    }
}
