package com.example.a4v4.database

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.graphics.BitmapFactory

import android.provider.ContactsContract

import android.content.ContentUris

import android.graphics.Bitmap

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.example.a4v4.R
import java.io.IOException
import java.io.InputStream


@Entity
class ContactsModel(
    var idx     :   Long,
    var name    :   String,
    @PrimaryKey
    var number  :   String,
    var type    :   Short,
    var email   :   String,
    var address :   String,
    var org     :   String,
    var title   :   String,
    var hasImg  :   Boolean =   false
){

    constructor(
        idx: Long?,
        name: String?,
        number: String?,
        type: Short?,
        email: String?,
        address: String?,
        org: String?,
        title: String?,
        hasImg: Boolean?
    ): this(
        getVal(idx),
        getVal(name),
        getVal(number),
        getVal(type),
        getVal(email),
        getVal(address),
        getVal(org),
        getVal(title),
        hasImg?:false
    )


    init {

        name    = getVal(name)
        number  = getVal(number)
        email   =   getVal(email)
        address =   getVal(address)
        org     =   getVal(org)
        title   =   getVal(title)



        Log.d("datastuv", ":\n" +
                "idx=$idx\n" +
                "name=$name\n  " +
                "num=$number\n " +
                "type=$type\n " +
                "email=$email\n " +
                "addr=$address\n " +
                "orga=$org\n" +
                "title=$title")
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


    fun retrieveContactPhoto(context: Context): Bitmap? {
        var photo = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.icon_place_holder
        )
        try {
            if (idx.toInt() != -1) {
                val inputStream: InputStream? =
                    ContactsContract.Contacts.openContactPhotoInputStream(
                        context.contentResolver,
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idx)
                    )
                if (inputStream != null) {
                    photo = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                } else return null
            } else return null
            return photo
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}