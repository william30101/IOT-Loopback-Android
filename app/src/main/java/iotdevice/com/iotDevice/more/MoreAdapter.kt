package iotdevice.com.iotDevice.more

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iotDevice.common.RecycleViewListener
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.LayoutMoreHeaderBinding
import iotdevice.com.iot_device.databinding.LayoutMoreListItemBinding

class MoreAdapter(private val context: Context, private val userName: String): RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    val itemList: MutableList<String> = mutableListOf()
    var listener: RecycleViewListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> {
                return HeaderViewHolder(parent.context, LayoutInflater.from(context).inflate(R.layout.layout_more_header,parent, false))
            }
            ITEM_VIEW_TYPE_ITEM -> {
                return ItemViewHolder(parent.context, LayoutInflater.from(context).inflate(R.layout.layout_more_list_item,parent, false))
            }
        }
        throw IllegalArgumentException("Invalid item type")
    }

    override fun getItemCount(): Int {
        return itemList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        when(holder) {
            is HeaderViewHolder -> {
                holder.bindHeaderList(userName)
            }
            is ItemViewHolder -> {
                val itemVal = itemList[position - 1]
                holder.itemView.tag =  itemVal
                holder.itemView.setOnClickListener(this)
                holder.bindList(itemVal)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ITEM_VIEW_TYPE_HEADER else ITEM_VIEW_TYPE_ITEM
    }

    override fun onClick(v: View?) {
        v?.let {
            val clickItem = it.tag as String
            val bundle = Bundle()
            bundle.putString("clickItemString", clickItem)
            listener?.onClick(bundle)
        }
    }

    inner class ItemViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = LayoutMoreListItemBinding.bind(itemView)

        fun bindList(itemVal: String) {

            binding.run {
                listItemName.text = itemVal
            }
        }
    }

    inner class HeaderViewHolder(context: Context, headerView: View) : RecyclerView.ViewHolder(headerView) {
        private var binding = LayoutMoreHeaderBinding.bind(headerView)

        fun bindHeaderList(itemVal: String) {

            binding.run {
                userNameTextView.text = itemVal
            }
        }
    }

    companion object {
        private const val ITEM_VIEW_TYPE_HEADER = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }
}