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
        charItemList.add(ChartListItem("test1", null, "description 1", deviceId))
        charItemList.add(ChartListItem("test2", null, "description 2", deviceId))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deviceId = activity.intent.getBundleExtra("homeBundle").getLong("deviceId")
        info("deviceId : $deviceId")

        addChartListItem(deviceId)

        chartAdapter = ChartAdapter(context, charItemList)

        chartRecyclerView.layoutManager = GridLayoutManager(context, 2)

        chartRecyclerView.adapter = chartAdapter
        chartRecyclerView.adapter.notifyDataSetChanged()

    }
}