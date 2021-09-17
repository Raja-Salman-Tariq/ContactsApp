package com.example.a4v4.database

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date


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

    @Ignore val log =   ArrayList<CallLogEntry>()

    /*------------------------------ C O N S T R U C T  I O N ---------------------------------*/

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
    }

    /*---------------------- " S T A T I C   F U N C T I O N A L I T Y  "-------------------------*/

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

    /*------------------------------ M E T H O D S ---------------------------------*/

    // convert Contact object to a coma seperated string for writing to file and for logging
    override fun toString(): String {
        return "$idx,$name,$number,$type\n"
    }

    // acquire photo from contact if present, else use placeholder image
     fun getImgUri(): Uri =
        Uri.withAppendedPath(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idx), ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
            ?:
            Uri.parse("android.resource://com.example.a4v4/drawable/image_name")

    fun fetchCallLog(context: Context): ArrayList<CallLogEntry> {
        val contact: Cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            ContactsContract.Contacts._ID + "=?",
            arrayOf(idx.toString()),
            null,
            null
        )!!
        if (contact != null && contact.moveToNext()) {
            val lookupKey: String =
                contact.getString(contact.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY))
            val contactId: Int =
                contact.getInt(contact.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
            val contactUri = ContactsContract.Contacts.getLookupUri(contactId.toLong(), lookupKey)
            val calls: Cursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                CallLog.Calls.CACHED_LOOKUP_URI + "=?",
                arrayOf(contactUri.toString()),
                CallLog.Calls.DATE + " DESC"
            )!!
            while (calls.moveToNext()) {
                log.add(CallLogEntry(
                    calls.getString(calls.getColumnIndexOrThrow(CallLog.Calls.TYPE)),
                    calls.getString(calls.getColumnIndexOrThrow(CallLog.Calls.DURATION)),
                    Date(calls.getString(calls.getColumnIndexOrThrow(CallLog.Calls.DATE)).toLong()).toString()
                ))
//                Log.d(
//                    "tmp",
//                    "type: " + calls.getString(calls.getColumnIndex(CallLog.Calls.TYPE))
//                        .toString() + ", number: " + calls.getString(calls.getColumnIndex(CallLog.Calls.NUMBER))
//                        .toString() + ", " + "cached_name: " + calls.getString(
//                        calls.getColumnIndexOrThrow(
//                            CallLog.Calls.CACHED_NAME
//                        )
//                    )
//                        .toString() + ", " + "date: " + calls.getString(calls.getColumnIndex(CallLog.Calls.DATE))
//                )
            }
        }
        return log
    }
}