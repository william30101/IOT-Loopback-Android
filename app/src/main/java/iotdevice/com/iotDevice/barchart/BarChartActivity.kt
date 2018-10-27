package iotdevice.com.iotDevice.barchart

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import iotdevice.com.iotDevice.chart.ChartListItem
import iotdevice.com.iotDevice.common.BaseActivity
import iotdevice.com.iotDevice.common.ChartUtils
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_barchart.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class BarChartActivity: BaseActivity(), AnkoLogger, ViewPager.OnPageChangeListener {

    private lateinit var pageList: MutableList<PageView>

    lateinit var fragmentBundle: Bundle
    var firstSelectedPosition: Int = 0
    val itemList = ChartUtils.charItemList
    var deviceName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_barchart)

        fragmentBundle = intent.extras
        val device = fragmentBundle.getParcelable<ChartListItem>("device")
        deviceName = fragmentBundle.getString("deviceName")

        firstSelectedPosition = itemList.indexOfFirst { it.title == device.title }

        pager.adapter = BarChartPagerAdapter(this, supportFragmentManager)
        pager.addOnPageChangeListener(this)
        pager.offscreenPageLimit = itemList.size
        pager.currentItem = firstSelectedPosition
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        info ("selected : $position")
        title = deviceName + " " + itemList[position].title
    }

    private inner class BarChartPagerAdapter(val context: Context, val fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {

            val eachBundle = Bundle()


            eachBundle.putString("deviceName", deviceName)


            return when (position) {
                0 -> {
                    eachBundle.putParcelable("device", itemList[0])
                    BarChartFragment().newInstance(eachBundle)
                }
                1 -> {
                    eachBundle.putParcelable("device", itemList[1])
                    BarChartFragment().newInstance(eachBundle)
                }
                2 -> {
                    eachBundle.putParcelable("device", itemList[2])
                    BarChartFragment().newInstance(eachBundle)
                }
                3 -> {
                    eachBundle.putParcelable("device", itemList[3])
                    BarChartFragment().newInstance(eachBundle)
                }
                else -> {
                    eachBundle.putParcelable("device", itemList[0])
                    BarChartFragment().newInstance(eachBundle)
                }
            }

        }

        override fun getCount(): Int {
            return itemList.size
        }
    }

}