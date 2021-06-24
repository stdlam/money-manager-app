package com.yplay.yspending.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spend")
data class Spend constructor(var thingForPay: String, var price: Long, var day: Int, var month: Int, var year: Int) : Parcelable {
    @PrimaryKey(autoGenerate = true) var id: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(thingForPay)
        dest?.writeLong(price)
        dest?.writeInt(day)
        dest?.writeInt(month)
        dest?.writeInt(year)
    }

    companion object CREATOR: Parcelable.Creator<Spend> {
        override fun createFromParcel(source: Parcel?): Spend {
            source?.let {
                return Spend(it)
            }
            return Spend("", 0, 1, 1, 1)
        }

        override fun newArray(size: Int): Array<Spend?>? {
            return arrayOfNulls(size)
        }
    }
}