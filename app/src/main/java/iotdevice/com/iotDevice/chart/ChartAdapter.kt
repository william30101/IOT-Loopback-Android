package iotdevice.com.iotDevice.chart

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.LayoutChartHeaderBinding
import iotdevice.com.iot_device.databinding.LayoutImageChartListBinding
import org.jetbrains.anko.AnkoLogger

class ChartAdapter(val context: Context, val chartViewModel: ChartViewModel, val charItemList: List<ChartListItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener, AnkoLogger {

    var listener: ChartListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> {
                return HeaderViewHolder(parent.context, LayoutInflater.from(context).inflate(R.layout.layout_chart_header,parent, false))
            }
            ITEM_VIEW_TYPE_ITEM -> {
                return ItemViewHolder(parent.context, LayoutInflater.from(context).inflate(R.layout.layout_image_chart_list,parent, false))
            }
        }
        throw IllegalArgumentException("Invalid item type")
    }

    override fun getItemCount(): Int {
        return charItemList.size + 1
    }

    fun setChartListener(listener: ChartListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        when(holder) {
            is HeaderViewHolder -> {

                // Initialization
                holder.bindHeaderList(context, chartViewModel)
            }
            is ItemViewHolder -> {
                val chartListItem = charItemList[position - 1]
                holder.itemView.tag =  chartListItem
                holder.itemView.setOnClickListener(this)
                holder.bindChartList(chartListItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeader(position)) ITEM_VIEW_TYPE_HEADER else ITEM_VIEW_TYPE_ITEM
    }

    override fun onClick(v: View?) {
        val chartListItem = v!!.tag as ChartListItem

        val bundle = Bundle()
        bundle.putLong("deviceId", chartListItem.deviceId)
        bundle.putString("itemTitle", chartListItem.title)

        listener?.onChartClick(bundle)
    }

    inner class ItemViewHolder(val context: Context, itemView: View): RecyclerView.ViewHolder(itemView) {

        private var binding = LayoutImageChartListBinding.bind(itemView)

        fun bindChartList(chartListItem: ChartListItem) {

            binding.run {
                itemViewModel = ItemViewModel()
                itemViewModel?.update(chartListItem)
                executePendingBindings()
            }

            binding.chartTitle.text = chartListItem.title
//            itemView.ChartImg.setImageURI(Uri.parse(chartListItem.imgPath))
            binding.chartDescription.text = chartListItem.description
        }
    }

    inner class HeaderViewHolder(val context: Context, headerView: View): RecyclerView.ViewHolder(headerView) {

        private var binding = LayoutChartHeaderBinding.bind(headerView)

        fun bindHeaderList(context: Context, viewModel: ChartViewModel) {


            binding.run {
                if(context is LifecycleOwner) {
                    setLifecycleOwner(context)
                }
                chartViewModel = viewModel
            }
        }
    }

    fun isHeader(position: Int): Boolean {
        return position == 0
    }

    companion object {
        private val ITEM_VIEW_TYPE_HEADER = 0
        private val ITEM_VIEW_TYPE_ITEM = 1
    }
}