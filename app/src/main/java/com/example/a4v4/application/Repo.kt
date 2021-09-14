package com.example.a4v4.application

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.a4v4.database.ContactsDao
import com.example.a4v4.database.DummyModel
import com.example.assignment4.application.NetworkDiscerner


class Repo(
    private val context: Context,
    private val contactDao: ContactsDao
    ) {

    val allContacts: LiveData<List<DummyModel>> = contactDao.getContacts()

    var selectedContact : DummyModel? =   null

    val ufoneContacts: LiveData<List<DummyModel>>   =   contactDao.getContacts(DummyModel.TYPE_UFONE)   //MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}
    val telenorContacts: LiveData<List<DummyModel>> =   contactDao.getContacts(DummyModel.TYPE_TELENOR) //MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}
    val zongContacts: LiveData<List<DummyModel>>    =   contactDao.getContacts(DummyModel.TYPE_ZONG)    //MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}
    val jazzContacts: LiveData<List<DummyModel>>    =   contactDao.getContacts(DummyModel.TYPE_JAZZ)    //MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}
    val otherContacts: LiveData<List<DummyModel>>   =   contactDao.getContacts(DummyModel.TYPE_OTHER)   //MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}

    var id: Long?               =   null
    var name: String?           =   null
    private var num: String?            =   null
    private var email: String?          =   null
    private var address:String?         =   null
    private var organization:String?    =   null
    var title:String?           =   null


    fun getContacts(type:Short): LiveData<List<DummyModel>> {
        return when(type){
            DummyModel.TYPE_UFONE   ->  ufoneContacts
            DummyModel.TYPE_JAZZ    ->  jazzContacts
            DummyModel.TYPE_ZONG    ->  zongContacts
            DummyModel.TYPE_TELENOR ->  telenorContacts
            DummyModel.TYPE_OTHER   ->  otherContacts
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


            var toAdd   : DummyModel
            while (cursor.moveToNext()) {

                toAdd   =   buildContact(cursor)
                contactDao.insert(toAdd)
            }
        }

        cursor?.close()
    }

    private fun buildContact(cursor: Cursor): DummyModel {

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

        return DummyModel(
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
    suspend fun insert(contact: DummyModel) {
        contactDao.insert(contact)
    }
}