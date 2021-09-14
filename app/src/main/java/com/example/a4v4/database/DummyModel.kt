package com.example.a4v4.database

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DummyModel(
    var idx     :   Long,
    var name    :   String,
    @PrimaryKey
    var number  :   String,
    var type    :   Short,
    var email   :   String,
    var address :   String,
    var org     :   String,
    var title   :   String
){

    constructor(
        idx: Long?,
        name: String?,
        number: String?,
        type: Short?,
        email: String?,
        address: String?,
        org: String?,
        title: String?
    ): this(
        getVal(idx),
        getVal(name),
        getVal(number),
        getVal(type),
        getVal(email),
        getVal(address),
        getVal(org),
        getVal(title)
    )


    init {

        name    = getVal(name)
        number  = getVal(number)
        email   =   getVal(email)
        address =   getVal(address)
        org     =   getVal(org)
        title   =   getVal(title)



//        Log.d("datastuv", ":\n" +
//                "idx=$idx\n" +
//                "name=$name\n  " +
//                "num=$number\n " +
//                "type=$type\n " +
//                "email=$email\n " +
//                "addr=$address\n " +
//                "orga=$org\n" +
//                "title=$title")
    }

    companion object{
        const val TYPE_JAZZ     :   Short  =   1
        const val TYPE_TELENOR  :   Short  =   2
        const val TYPE_UFONE    :   Short  =   3
        const val TYPE_ZONG     :   Short  =   4
        const val TYPE_OTHER    :   Short  =   5

        fun getVal(idx   :   Long?) = idx ?: -1
        fun getVal(str   :   String?) = if (str.isNullOrEmpty()) "Unavailable" else str
        fun getVal(short :   Short?) = short ?: TYPE_OTHER
    }

    override fun toString(): String {
        return "$idx,$name,$number,$type\n"
    }


}