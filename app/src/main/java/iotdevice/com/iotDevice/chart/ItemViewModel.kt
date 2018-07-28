package iotdevice.com.iotDevice.chart

class ItemViewModel {

    var title: String =""
    var imgPath: String? = ""
    var description: String =""
    var deviceId: Long = 0

    fun update(chartListItem: ChartListItem) {
        title = chartListItem.title
        imgPath = chartListItem.imgPath
        description = chartListItem.description
        deviceId = chartListItem.deviceId
    }
}