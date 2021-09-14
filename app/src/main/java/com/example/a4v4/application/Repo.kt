package com.example.a4v4.application

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.a4v4.database.ContactsDao
import com.example.a4v4.database.ContactsModel


class Repo(
    private val context: Context,
    private val contactDao: ContactsDao
    ) {
    /*------------------------------ C O R E    V A R I A B L E S ---------------------------------*/

    val allContacts: LiveData<List<ContactsModel>> = contactDao.getContacts()

    var selectedContact : ContactsModel? =   null

    private val ufoneContacts   : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_UFONE)
    private val telenorContacts : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_TELENOR)
    private val zongContacts    : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_ZONG)
    private val jazzContacts    : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_JAZZ)
    private val otherContacts   : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_OTHER)

    /*------------------------------ H E L P E R    V A R I A B L E S ---------------------------------*/
    var id: Long?                       =   null
    var name: String?                   =   null
    private var num: String?            =   null
    private var email: String?          =   null
    private var address:String?         =   null
    private var organization:String?    =   null
    var title:String?                   =   null


    fun getContacts(type:Short): LiveData<List<ContactsModel>> {
        return when(type){
            ContactsModel.TYPE_UFONE   ->  ufoneContacts
            ContactsModel.TYPE_JAZZ    ->  jazzContacts
            ContactsModel.TYPE_ZONG    ->  zongContacts
            ContactsModel.TYPE_TELENOR ->  telenorContacts
            ContactsModel.TYPE_OTHER   ->  otherContacts
            else                    ->  allContacts
        }
    }

    suspend fun fetchContacts(){

        val cursor      = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor?.count!!> 0) {
            var toAdd   : ContactsModel
            while (cursor.moveToNext()) {

                toAdd   =   buildContact(cursor)
                contactDao.insert(toAdd)
            }
        }
        cursor?.close()
    }

    private fun buildContact(cursor: Cursor): ContactsModel {

        Log.d("idx", "fetchContacts: ${cursor.getLong(cursor.getColumnIndexOrThrow("_id"))}")
//        sth(cursor)
        id      =   cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
        name    =   cursor.getString(cursor?.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
        num     =   cursor.getString(cursor?.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
        email   =   cursor.getString(cursor?.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA))
        address =   cursor.getString(cursor?.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.DATA))
        cursor?.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.COMPANY).let {
            organization = if (it==null)
                null
            else
                cursor.getString(it)
        }
        title   =   cursor.getString(cursor?.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.TITLE))

        return ContactsModel(
            id,
            name,
            num,
            NetworkDiscerner(num).type,
            email,
            address,
            organization,
            title
        )
    }

//    fun sth(cur: Cursor){
//        id= cur.getLong(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
//        name= cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
//        if ((cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
//            val pCur: Cursor = context.contentResolver.query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
//                        + " = ?", arrayOf(id.toString()), null
//            )!!
//            while (pCur.moveToNext()) {
//                // Do something with phones
//                num = pCur.getString(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                val emailCur: Cursor = context.contentResolver.query(
//                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
//                    arrayOf(id.toString()),
//                    null
//                )!!
//                while (emailCur.moveToNext()) {
//                    email =emailCur.getString(emailCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA))
//                }
//                emailCur.close()
//            }
//            pCur.close()
//        }
//    }
}