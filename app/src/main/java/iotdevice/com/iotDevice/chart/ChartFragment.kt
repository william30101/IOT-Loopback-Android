package iotdevice.com.iotDevice.chart


import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import iotdevice.com.iotDevice.barchart.BarChartActivity
import iotdevice.com.iotDevice.common.ChartUtils
import iotdevice.com.iotDevice.common.ChartUtils.Companion.charItemList
import iotdevice.com.iotDevice.common.RecycleViewListener
import iotdevice.com.iotDevice.model.relateview.ImageModel
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.FragmentChartBinding

class ChartFragment: Fragment(), RecycleViewListener {


    private lateinit  var chartAdapter: ChartAdapter
    private var deviceId: Long = -1
    lateinit var chartViewModel: ChartViewModel

    private var _binding: FragmentChartBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        chartViewModel = ViewModelProvider(this)[ChartViewModel::class.java]

        val arguments = arguments
        val selectedDevice = arguments?.getParcelable<ImageModel>("device")
        deviceId =  selectedDevice?.deviceId!!.toLong()
        Log.i(tag, "deviceId : $deviceId")

        val titleStr = "${selectedDevice!!.displayName} " + getString(R.string.chart_title)
        activity?.title = titleStr



        ChartUtils.updateChartListItem(requireActivity(), deviceId)
        chartAdapter = ChartAdapter(requireContext(), chartViewModel , charItemList, selectedDevice!!.displayName)
        chartAdapter.setChartListener(this)



        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycle_dimen)
        binding.chartRecyclerView.addItemDecoration(GridLayoutDivider(2, spacingInPixels, true, 1))

        val manager = GridLayoutManager(context, 2)

        binding.chartRecyclerView.layoutManager = manager

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (chartAdapter.isHeader(position)) manager.spanCount else 1
            }
        }

        binding.chartRecyclerView.adapter = chartAdapter

        chartViewModel.errorGetChart.observe(viewLifecycleOwner, Observer { errMsg ->
            binding.swipeRefreshLayout.isRefreshing = false
            (binding.chartRecyclerView.adapter as ChartAdapter).notifyDataSetChanged()

            // We don't display error here.
            // Fill each filed to 0
//            DialogUtils.createAlertDialog( activity, getString(R.string.chart_title), errMsg.toString() )

        })

        chartViewModel.headerItemLiveData.observe(viewLifecycleOwner, Observer {
            binding.swipeRefreshLayout.isRefreshing = false
            (binding.chartRecyclerView.adapter as ChartAdapter).notifyDataSetChanged()

        })

        chartViewModel.totalOfDay.observe(viewLifecycleOwner, Observer {
            it?.run {
                ChartUtils.charItemList[0].displayNumber = this
                (binding.chartRecyclerView.adapter as ChartAdapter).notifyDataSetChanged()
            }
        })

        chartViewModel.totalOfMonth.observe(viewLifecycleOwner, Observer {
            it?.run {
                ChartUtils.charItemList[1].displayNumber = this
                (binding.chartRecyclerView.adapter as ChartAdapter).notifyDataSetChanged()
            }
        })

        chartViewModel.totalOfOperationTime.observe(viewLifecycleOwner, Observer {
            it?.run {
                ChartUtils.charItemList[2].displayNumber = this
                (binding.chartRecyclerView.adapter as ChartAdapter).notifyDataSetChanged()
            }
        })

        chartViewModel.totalOfPCS.observe(viewLifecycleOwner, Observer {
            it?.run {
                ChartUtils.charItemList[3].displayNumber = this
                (binding.chartRecyclerView.adapter as ChartAdapter).notifyDataSetChanged()
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener({
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