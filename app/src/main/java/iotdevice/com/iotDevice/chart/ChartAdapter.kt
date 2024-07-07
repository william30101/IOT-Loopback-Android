package iotdevice.com.iotDevice.chart

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iotDevice.common.RecycleViewListener
import iotdevice.com.iotDevice.R
import iotdevice.com.iotDevice.databinding.LayoutChartHeaderBinding
import iotdevice.com.iotDevice.databinding.LayoutImageChartListBinding
import org.jetbrains.anko.AnkoLogger

class ChartAdapter(val context: Context, val chartViewModel: ChartViewModel, val charItemList: List<ChartListItem>, val deviceName: String): RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener, AnkoLogger {

    var listener: RecycleViewListener? = null
    val listOutCircleList = arrayListOf(R.drawable.ic_list_out_circle_0,
            R.drawable.ic_list_out_circle_1,
            R.drawable.ic_list_out_circle_2,
            R.drawable.ic_list_out_circle_3)

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

    fun setChartListener(listener: RecycleViewListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is HeaderViewHolder -> {

                // Initialization
                holder.bindHeaderList(context, chartViewModel)
            }
            is ItemViewHolder -> {
                val chartListItem = charItemList[position - 1]
                holder.itemView.tag =  chartListItem
                holder.itemView.setOnClickListener(this)
                holder.bindChartList(chartListItem, position - 1)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeader(position)) ITEM_VIEW_TYPE_HEADER else ITEM_VIEW_TYPE_ITEM
    }

    override fun onClick(v: View?) {
        val chartListItem = v!!.tag as ChartListItem

        val bundle = Bundle()
        bundle.putParcelable("device", chartListItem)
        bundle.putString("deviceName", deviceName)

        listener?.onClick(bundle)
    }

    inner class ItemViewHolder(val context: Context, itemView: View): RecyclerView.ViewHolder(itemView) {

        private var binding = LayoutImageChartListBinding.bind(itemView)

        fun bindChartList(chartListItem: ChartListItem, position: Int) {

            binding.run {

                ChartImg.setImageDrawable(context.getDrawable(listOutCircleList[position]))
                itemViewModel = ItemViewModel()
                itemViewModel?.update(chartListItem)
                chartTitle.text = chartListItem.title
//            itemView.ChartImg.setImageURI(Uri.parse(chartListItem.imgPath))
                chartDescription.text = chartListItem.description
                executePendingBindings()
            }
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