package iotdevice.com.iotDevice.model.relateview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageModel ( val id: Int, val deviceId: Number, val imageName: String, val displayName: String) : Parcelable
