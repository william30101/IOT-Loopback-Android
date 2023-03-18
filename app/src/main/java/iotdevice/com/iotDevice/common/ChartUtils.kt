package iotdevice.com.iotDevice.common

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.chart.ChartListItem
import iotdevice.com.iotDevice.model.DeviceStatusModel
import iotdevice.com.iot_device.R

class ChartUtils {
    companion object {

        const val HOUR_OUTPUT = "HourOutput"
        const val DAY_OUTPUT =  "DayOutput"
        const val OPERATION_TIME = "OperationTime"
        const val AVERAGE_OUTPUT = "AverageOutput"

        var hourUnit = ""
        var minuteUnit = ""
        var productivityUnit = ""
        var dayUnit = ""
        var averageProductvityUnit = ""

        val charItemList: MutableList<ChartListItem> = mutableListOf()

        val resource: Resources  = App.sInstance.resources.apply {
            hourUnit = this.getString(R.string.hour_unit)
            minuteUnit = this.getString(R.string.minute_unit)
            productivityUnit = this.getString(R.string.productvity_unit)
            dayUnit = this.getString(R.string.day_unit)
            averageProductvityUnit = this.getString(R.string.average_productvity_unit)
        }

        private val axisMapping = listOf(
                AxisName(HOUR_OUTPUT, AxisXType.Hour, "PCS"),
                AxisName(DAY_OUTPUT, AxisXType.Month, "PCS"),
                AxisName(OPERATION_TIME, AxisXType.Month, "H"),
                AxisName(AVERAGE_OUTPUT, AxisXType.Month, "PCS/MIN")
        )

        fun updateChartListItem(context: Context, deviceId: Long) {
                charItemList.clear()
//        charItemList.add(ChartListItem("header", null, "header", deviceId))
                charItemList.add(ChartListItem(context.getString(R.string.hour_output_title), null, context.getString(R.string.hour_output_description), deviceId, 0))
                charItemList.add(ChartListItem(context.getString(R.string.day_output_title), null, context.getString(R.string.day_output_description), deviceId, 0))
                charItemList.add(ChartListItem(context.getString(R.string.operation_time_title), null, context.getString(R.string.operation_time_description), deviceId, 0))
                charItemList.add(ChartListItem(context.getString(R.string.average_output_title), null, context.getString(R.string.average_output_description), deviceId, 0))
        }

        fun combineProductivityToList(item: DeviceStatusModel) {
            item.productivityLit.add(0 ,item.productivity0)
            item.productivityLit.add(1 ,item.productivity1)
            item.productivityLit.add(2 ,item.productivity2)
            item.productivityLit.add(3 ,item.productivity3)
            item.productivityLit.add(4 ,item.productivity4)
            item.productivityLit.add(5 ,item.productivity5)
            item.productivityLit.add(6 ,item.productivity6)
            item.productivityLit.add(7 ,item.productivity7)
            item.productivityLit.add(8 ,item.productivity8)
            item.productivityLit.add(9 ,item.productivity9)
            item.productivityLit.add(10 ,item.productivity10)
            item.productivityLit.add(11 ,item.productivity11)
            item.productivityLit.add(12 ,item.productivity12)
            item.productivityLit.add(13 ,item.productivity13)
            item.productivityLit.add(14 ,item.productivity14)
            item.productivityLit.add(15 ,item.productivity15)
            item.productivityLit.add(16 ,item.productivity16)
            item.productivityLit.add(17 ,item.productivity17)
            item.productivityLit.add(18 ,item.productivity18)
            item.productivityLit.add(19 ,item.productivity19)
            item.productivityLit.add(20 ,item.productivity20)
            item.productivityLit.add(21 ,item.productivity21)
            item.productivityLit.add(22 ,item.productivity22)
            item.productivityLit.add(23 ,item.productivity23)
        }

        fun transmitFragment(fragmentManager: FragmentManager, fragment: Fragment, bundle: Bundle) {

            fragment.arguments = bundle

            fragmentManager.beginTransaction().apply {
                replace(R.id.baseFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }

        fun getMappingAxis(chartName: String):AxisName? {
            return axisMapping.find { it.chartName == chartName }
        }

        fun calHourAndMinutes(totalMinutes: Long): String {
            val hour = (totalMinutes / 60).toString()
            val minutes = (totalMinutes % 60).toString()
            return hour + resource.getString(R.string.hour_unit) +
                    minutes + resource.getString(R.string.minute_unit)
        }
    }

    data class AxisName(val chartName: String, val xAxisType: AxisXType, val yAxisUnit: String)
    enum class AxisXType { Hour, Month }




}