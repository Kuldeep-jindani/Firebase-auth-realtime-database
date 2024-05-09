package com.kuldeep.pestopractical.ui.Model

import android.os.Parcel
import android.os.Parcelable

data class TasksModels(
    var title : String? = null,
    var desc : String? = null,
    var status : String? = null,
    var createStatus: String? = null,
    var date : String? = null,
    var key : String? = null
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(desc)
        parcel.writeString(status)
        parcel.writeString(createStatus)
        parcel.writeString(date)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TasksModels> {
        override fun createFromParcel(parcel: Parcel): TasksModels {
            return TasksModels(parcel)
        }

        override fun newArray(size: Int): Array<TasksModels?> {
            return arrayOfNulls(size)
        }
    }

}
enum class CreateStatus (){
    NEW,
    EDIT
}
