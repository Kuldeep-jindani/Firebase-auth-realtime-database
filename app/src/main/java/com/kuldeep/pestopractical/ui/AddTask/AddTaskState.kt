package com.kuldeep.pestopractical.ui.AddTask

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TasksModels(
    var title: String? = null,
    var desc: String? = null,
    var status: String? = null,
    var createStatus: String? = null,
    var date: String? = null,
    var key: String? = null
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
}