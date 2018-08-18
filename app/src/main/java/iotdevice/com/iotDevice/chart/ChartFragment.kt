package iotdevice.com.iotDevice.chart

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iotDevice.common.ChartUtils
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.common.RecycleViewListener
import iotdevice.com.iotDevice.draw.BarChartFragment
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.fragment_chart.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class ChartFragment: Fragment(), AnkoLogger, RecycleViewListener {


    private lateinit  var chartAdapter: ChartAdapter
    private val charItemList: MutableList<ChartListItem> = mutableListOf()
    private var deviceId: Long = -1
    lateinit var chartViewModel: ChartViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_chart, container, false)
    }

    fun addChartListItem(deviceId: Long) {
        charItemList.clear()
//        charItemList.add(ChartListItem("header", null, "header", deviceId))
        charItemList.add(ChartListItem(resources.getString(R.string.hour_output_title), null, resources.getString(R.string.hour_output_description), deviceId))
        charItemList.add(ChartListItem(resources.getString(R.string.day_output_title), null, resources.getString(R.string.day_output_description), deviceId))
        charItemList.add(ChartListItem(resources.getString(R.string.operation_time_title), null, resources.getString(R.string.operation_time_description), deviceId))
        charItemList.add(ChartListItem(resources.getString(R.string.average_output_title), null, resources.getString(R.string.average_output_description), deviceId))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        chartViewModel = ViewModelProviders.of(this).get(ChartViewModel::class.java)

        chartViewModel.headerItemLiveData.observe(this, Observer {
            info("time return : $it")

        })
        val arguments = arguments
        deviceId = arguments.getLong("deviceId")
        info("deviceId : $deviceId")

        addChartListItem(deviceId)
        chartAdapter = ChartAdapter(context, chartViewModel , charItemList)
        chartAdapter.setChartListener(this)

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

        chartViewModel.errorGetChart.observe(this, Observer { _ ->
            DialogUtils.createAlertDialog( activity, getString(R.string.chart_title))
        })

    }

    override fun onResume() {
        super.onResume()
        chartViewModel.fetchHeaderItem(deviceId)
    }

    override fun onClick(bundle: Bundle) {
        val barChartFragment = BarChartFragment()
        ChartUtils.transmitFragment(fragmentManager, barChartFragment, bundle)
    }
}