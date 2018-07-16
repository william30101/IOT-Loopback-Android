package iotdevice.com.iotDevice.chart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iotDevice.draw.BarChartActivity
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.layout_image_chart_list.view.*
import org.jetbrains.anko.AnkoLogger

class ChartAdapter(val context: Context, val charItemList: List<ChartListItem>): RecyclerView.Adapter<ChartAdapter.ItemViewHolder>(), View.OnClickListener, AnkoLogger {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> {
                ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_chart_header,parent, false))
            }
            ITEM_VIEW_TYPE_ITEM -> {
                ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_image_chart_list,parent, false))
            }
            else -> {
                ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_image_chart_list,parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return charItemList.size + 1
    }

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        if (isHeader(position)) {
            return
        }
        val chartListItem = charItemList[position - 1]
        holder!!.itemView.tag =  chartListItem
        holder.itemView.setOnClickListener(this)
        holder.bindChartList(chartListItem)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeader(position)) ITEM_VIEW_TYPE_HEADER else ITEM_VIEW_TYPE_ITEM
    }

    override fun onClick(v: View?) {
        val chartListItem = v!!.tag as ChartListItem

        val intent = Intent(context, BarChartActivity::class.java)
        val bundle = Bundle()
        bundle.putLong("deviceId", chartListItem.deviceId)
        bundle.putString("itemTitle", chartListItem.title)
        intent.putExtra("homeBundle", bundle)
        startActivity(context, intent, null)
    }


    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindChartList(chartListItem: ChartListItem) {
            itemView.chartTitle.text = chartListItem.title
//            itemView.ChartImg.setImageURI(Uri.parse(chartListItem.imgPath))
            itemView.chartDescription.text = chartListItem.description
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