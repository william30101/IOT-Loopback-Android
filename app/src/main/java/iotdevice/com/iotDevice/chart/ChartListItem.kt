package iotdevice.com.iotDevice.chart

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChartListItem(
        val title: String,
        val imgPath: String?,
        val description: String,
        val deviceId: Long,
        var displayNumber: Long): Parcelable