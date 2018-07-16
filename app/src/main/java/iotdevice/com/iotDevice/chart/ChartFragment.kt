package iotdevice.com.iotDevice.chart

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.fragment_chart.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class ChartFragment: Fragment(), AnkoLogger {


    private lateinit  var chartAdapter: ChartAdapter
    private val charItemList: MutableList<ChartListItem> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_chart, container, false)
    }

    fun addChartListItem(deviceId: Long) {
//        charItemList.add(ChartListItem("header", null, "header", deviceId))
        charItemList.add(ChartListItem(resources.getString(R.string.hour_output_title), null, resources.getString(R.string.hour_output_description), deviceId))
        charItemList.add(ChartListItem(resources.getString(R.string.day_output_title), null, resources.getString(R.string.day_output_description), deviceId))
        charItemList.add(ChartListItem(resources.getString(R.string.operation_time_title), null, resources.getString(R.string.operation_time_description), deviceId))
        charItemList.add(ChartListItem(resources.getString(R.string.average_output_title), null, resources.getString(R.string.average_output_description), deviceId))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deviceId = activity.intent.getBundleExtra("homeBundle").getLong("deviceId")
        info("deviceId : $deviceId")

        addChartListItem(deviceId)

        chartAdapter = ChartAdapter(context, charItemList)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycle_dimen)
        chartRecyclerView.addItemDecoration(GridLayoutDivider(2, spacingInPixels, true, 1))

        val manager = GridLayoutManager(context, 2)

        chartRecyclerView.layoutManager = manager

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (chartAdapter.isHeader(position)) manager.spanCount else 1
            }
        }

        chartRecyclerView.adapter = chartAdapter
        chartRecyclerView.adapter.notifyDataSetChanged()

    }
}