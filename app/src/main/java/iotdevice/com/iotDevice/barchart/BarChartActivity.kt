package iotdevice.com.iotDevice.barchart

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import iotdevice.com.iotDevice.chart.ChartListItem
import iotdevice.com.iotDevice.common.BaseActivity
import iotdevice.com.iotDevice.common.ChartUtils
import iotdevice.com.iot_device.databinding.ActivityBarchartBinding


class BarChartActivity: BaseActivity(), ViewPager.OnPageChangeListener {

    private lateinit var pageList: MutableList<PageView>

    lateinit var fragmentBundle: Bundle
    var firstSelectedPosition: Int = 0
    val itemList = ChartUtils.charItemList
    var deviceName = ""

    private lateinit var binding: ActivityBarchartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBarchartBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fragmentBundle = intent?.extras!!
        val device = fragmentBundle.getParcelable<ChartListItem>("device")
        deviceName = fragmentBundle.getString("deviceName").toString()


        firstSelectedPosition = itemList.indexOfFirst { it.title == device!!.title }

        binding.pager.adapter = BarChartPagerAdapter(this, supportFragmentManager)
        binding.pager.addOnPageChangeListener(this)
        binding.pager.offscreenPageLimit = itemList.size
        binding.pager.currentItem = firstSelectedPosition
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        Log.i(tag, "selected : $position")

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

    companion object {
        const val tag = "BarChartActivity"
    }

}