package com.poshey.upuasatt2

import android.os.Parcel
import android.os.Parcelable

data class ScannedData(  val fname: String,
                         val lname: String,
                         val number: String,
                         val time: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fname)
        parcel.writeString(lname)
        parcel.writeString(number)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScannedData> {
        override fun createFromParcel(parcel: Parcel): ScannedData {
            return ScannedData(parcel)
        }

        override fun newArray(size: Int): Array<ScannedData?> {
            return arrayOfNulls(size)
        }
    }
}

