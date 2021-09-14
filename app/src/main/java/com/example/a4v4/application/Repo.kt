package com.example.a4v4.application

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.a4v4.database.ContactsDao
import com.example.a4v4.database.ContactsModel
import com.example.assignment4.application.NetworkDiscerner


class Repo(
    private val context: Context,
    private val contactDao: ContactsDao
    ) {

    val allContacts: LiveData<List<ContactsModel>> = contactDao.getContacts()

    var selectedContact : ContactsModel? =   null

    val ufoneContacts: LiveData<List<ContactsModel>>   =   contactDao.getContacts(ContactsModel.TYPE_UFONE)   //MutableLiveData<ArrayList<ContactsModel>>().apply { value = ArrayList()}
    val telenorContacts: LiveData<List<ContactsModel>> =   contactDao.getContacts(ContactsModel.TYPE_TELENOR) //MutableLiveData<ArrayList<ContactsModel>>().apply { value = ArrayList()}
    val zongContacts: LiveData<List<ContactsModel>>    =   contactDao.getContacts(ContactsModel.TYPE_ZONG)    //MutableLiveData<ArrayList<ContactsModel>>().apply { value = ArrayList()}
    val jazzContacts: LiveData<List<ContactsModel>>    =   contactDao.getContacts(ContactsModel.TYPE_JAZZ)    //MutableLiveData<ArrayList<ContactsModel>>().apply { value = ArrayList()}
    val otherContacts: LiveData<List<ContactsModel>>   =   contactDao.getContacts(ContactsModel.TYPE_OTHER)   //MutableLiveData<ArrayList<ContactsModel>>().apply { value = ArrayList()}

    var id: Long?               =   null
    var name: String?           =   null
    private var num: String?            =   null
    private var email: String?          =   null
    private var address:String?         =   null
    private var organization:String?    =   null
    var title:String?           =   null


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
        id      =   cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
        name    =   cursor.getString(cursor?.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(contact: ContactsModel) {
        contactDao.insert(contact)
    }
}