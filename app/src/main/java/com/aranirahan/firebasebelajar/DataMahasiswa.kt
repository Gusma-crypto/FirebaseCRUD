package com.aranirahan.firebasebelajar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataMahasiswa(
        var nim: String = "",
        var nama: String = "",
        var jurusan: String = "",
        var key: String = ""
) : Parcelable
//class DataMahasiswa(nim: String, nama: String, jurusan: String) {
//    var nim = nim
//    var nama = nama
//    var jurusan = jurusan
//    var key: String = ""
//}