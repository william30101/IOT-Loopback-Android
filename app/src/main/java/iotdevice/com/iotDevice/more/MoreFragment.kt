package iotdevice.com.iotDevice.more


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iotDevice.chart.GridLayoutDivider
import iotdevice.com.iotDevice.common.RecycleViewListener
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.fragment_more.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MoreFragment: Fragment(), RecycleViewListener, AnkoLogger {

    private val moreItemList = mutableListOf("LogOut")
    val userName: String = "William"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val moreAdapter = MoreAdapter(activity, userName)
        moreAdapter.itemList.addAll(moreItemList)
        moreAdapter.listener = this

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycle_dimen)
        more_recyclew_view.addItemDecoration(GridLayoutDivider(1, spacingInPixels, true, 1))

        val manager = GridLayoutManager(context, 1)

        more_recyclew_view.layoutManager = manager
        more_recyclew_view.adapter = moreAdapter
    }

    override fun onClick(bundle: Bundle) {
        info { "data is : $bundle" }
    }
}