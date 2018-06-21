package iotdevice.com.iotDevice.repository

import com.google.common.collect.ImmutableMap
import com.strongloop.android.loopback.ModelRepository
import com.strongloop.android.loopback.callbacks.JsonArrayParser
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iotDevice.model.DeviceStatusModel
import org.json.JSONObject
import java.util.*




class DeviceStatusRepository : ModelRepository<DeviceStatusModel>("DeviceStatus", "DeviceStatuses", DeviceStatusModel::class.java) {

    fun filter(id: Long, callback: ListCallback<DeviceStatusModel>) {
        invokeStaticMethod("all",
                ImmutableMap.of("filter",
                        ImmutableMap.of("where",
                                ImmutableMap.of("deviceId", id))),
                JsonArrayParser(this, callback))
    }

    fun findTodayStatus(id: Long?, callback: ListCallback<DeviceStatusModel>) {
        // {"where" : { "timeStamp" : {"between" : [1527984000,1528070399 ]}}}

        val gcToday = GregorianCalendar()
        resetTimeOfHourMinuteSecond(gcToday)

        val gcTomorrow = GregorianCalendar()
        gcTomorrow.add(Calendar.DATE, 1)
        resetTimeOfHourMinuteSecond(gcTomorrow)


        val todayTimeStamp = gcToday.timeInMillis.toString()
        val tomorrowTimeStamp = gcTomorrow.timeInMillis.toString()


//        val findJson = JSONObject("""{"where":{"deviceId" : $id }, "order" : "id DESC", "limit" : "1"}""")

        val findJson = JSONObject("""{"where" : { "timeStamp" : { "between" : [$todayTimeStamp,$tomorrowTimeStamp]}}}""")

        invokeStaticMethod("all",
                mapOf("filter" to findJson),
                JsonArrayParser<DeviceStatusModel>(this, callback))

    }


    private fun resetTimeOfHourMinuteSecond(gregorianCal: GregorianCalendar) {
        gregorianCal.set(GregorianCalendar.HOUR, 0)
        gregorianCal.set(GregorianCalendar.MINUTE, 0)
        gregorianCal.set(GregorianCalendar.SECOND, 0)
    }


    fun findMonthStatus(id: Long?, callback: ListCallback<DeviceStatusModel>) {
        // {"where" : { "timeStamp" : {"between" : [1527984000,1528070399 ]}}}

        val gcToday = GregorianCalendar()
        resetTimeOfMonthHourMinuteSecond(gcToday)

        val gcNextMonth = GregorianCalendar()
        gcNextMonth.add(Calendar.MONTH, 1)
        resetTimeOfMonthHourMinuteSecond(gcNextMonth)


        val todayTimeStamp = gcToday.timeInMillis.toString()
        val nextMothTimeStamp = gcNextMonth.timeInMillis.toString()


//        val findJson = JSONObject("""{"where":{"deviceId" : $id }, "order" : "id DESC", "limit" : "1"}""")

        val findJson = JSONObject("""{"where" : { "timeStamp" : { "between" : [$todayTimeStamp,$nextMothTimeStamp]}}}""")

        invokeStaticMethod("all",
                mapOf("filter" to findJson),
                JsonArrayParser<DeviceStatusModel>(this, callback))

    }


    private fun resetTimeOfMonthHourMinuteSecond(gregorianCal: GregorianCalendar) {

        gregorianCal.set(GregorianCalendar.DAY_OF_MONTH, 1)
        gregorianCal.set(GregorianCalendar.HOUR, 0)
        gregorianCal.set(GregorianCalendar.MINUTE, 0)
        gregorianCal.set(GregorianCalendar.SECOND, 0)
    }


    fun findMonthDayilyStatus(id: Long?, callback: ListCallback<DeviceStatusModel>) {
        val findJson = JSONObject("""{"where":{"deviceId" : $id }, "order" : "id DESC", "limit" : "1"}""")

        invokeStaticMethod("all",
                mapOf("filter" to findJson),
                JsonArrayParser<DeviceStatusModel>(this, callback))

    }


}