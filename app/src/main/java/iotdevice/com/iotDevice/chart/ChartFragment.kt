package iotdevice.com.iotDevice.chart

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iotDevice.barchart.BarChartActivity
import iotdevice.com.iotDevice.common.ChartUtils
import iotdevice.com.iotDevice.common.ChartUtils.Companion.charItemList
import iotdevice.com.iotDevice.common.RecycleViewListener
import iotdevice.com.iotDevice.model.relateview.ImageModel
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.fragment_chart.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class ChartFragment: Fragment(), AnkoLogger, RecycleViewListener {


    private lateinit  var chartAdapter: ChartAdapter
    private var deviceId: Long = -1
    lateinit var chartViewModel: ChartViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        chartViewModel = ViewModelProviders.of(this).get(ChartViewModel::class.java)



        val arguments = arguments
        val selectedDevice = arguments.getParcelable<ImageModel>("device")
        deviceId =  selectedDevice.deviceId.toLong()
        info("deviceId : $deviceId")

        val titleStr = "${selectedDevice.displayName} " + getString(R.string.chart_title)
        activity.title = titleStr



        ChartUtils.updateChartListItem(activity, deviceId)
        chartAdapter = ChartAdapter(context, chartViewModel , charItemList, selectedDevice.displayName)
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

        chartViewModel.errorGetChart.observe(this, Observer { errMsg ->
            swipeRefreshLayout.isRefreshing = false
            chartRecyclerView.adapter.notifyDataSetChanged()

            // We don't display error here.
            // Fill each filed to 0
//            DialogUtils.createAlertDialog( activity, getString(R.string.chart_title), errMsg.toString() )

        })

        chartViewModel.headerItemLiveData.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = false
            chartRecyclerView.adapter.notifyDataSetChanged()

        })

        chartViewModel.totalOfDay.observe(this, Observer {
            it?.run {
                ChartUtils.charItemList[0].displayNumber = this
                chartRecyclerView.adapter.notifyDataSetChanged()
            }
        })

        chartViewModel.totalOfMonth.observe(this, Observer {
            it?.run {
                ChartUtils.charItemList[1].displayNumber = this
                chartRecyclerView.adapter.notifyDataSetChanged()
            }
        })

        chartViewModel.totalOfOperationTime.observe(this, Observer {
            it?.run {
                ChartUtils.charItemList[2].displayNumber = this
                chartRecyclerView.adapter.notifyDataSetChanged()
            }
        })

        chartViewModel.totalOfPCS.observe(this, Observer {
            it?.run {
                ChartUtils.charItemList[3].displayNumber = this
                chartRecyclerView.adapter.notifyDataSetChanged()
            }
        })

        swipeRefreshLayout.setOnRefreshListener({
            onRefresh()
        })
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }

    override fun onClick(bundle: Bundle) {
//        val barChartFragment = BarChartFragment()
//        ChartUtils.transmitFragment(fragmentManager, barChartFragment, bundle)

        val intent = Intent(context, BarChartActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun onRefresh() {
        chartViewModel.fetchCurrentListItem(deviceId)
        chartViewModel.fetchHeaderItem(deviceId)
    }
}