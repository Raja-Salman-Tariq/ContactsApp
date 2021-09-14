package com.example.a4v4.application

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.assignment4.application.NetworkDiscerner
import com.example.a4v4.database.ContactsDao
import com.example.a4v4.database.DummyModel
import kotlinx.coroutines.flow.Flow


class Repo(
    private val context: Context,
    private val contactDao: ContactsDao
    ) {

    val allContacts: LiveData<List<DummyModel>> = contactDao.getContacts()
    var myContacts  = listOf<DummyModel>()

    var selectedContact : DummyModel? =   null

    private var contactsToRet   =   MutableLiveData<List<DummyModel>>().apply { value = ArrayList()}

    private val contacts        =   MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}/*:   LiveData<List<DummyModel>>  =   contactDao.getAll()*/
    private val ufoneContacts   =   MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}
    private val telenorContacts =   MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}
    private val zongContacts    =   MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}
    private val jazzContacts    =   MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}
    private val otherContacts   =   MutableLiveData<ArrayList<DummyModel>>().apply { value = ArrayList()}

    var id: Long?               =   null
    var name: String?           =   null
    private var num: String?            =   null
    private var email: String?          =   null
    private var address:String?         =   null
    private var organization:String?    =   null
    var title:String?           =   null


    init {
        setContacts(0)
    }

    fun getContacts()   =   contactsToRet as LiveData<List<DummyModel>>

    fun setContacts(type    :   Short)    = when (type){
        DummyModel.TYPE_ZONG    ->  contactsToRet.postValue(zongContacts.value)
        DummyModel.TYPE_JAZZ    ->  contactsToRet.postValue(jazzContacts.value)
        DummyModel.TYPE_TELENOR ->  contactsToRet.postValue(telenorContacts.value)
        DummyModel.TYPE_UFONE   ->  contactsToRet.postValue(ufoneContacts.value)
        DummyModel.TYPE_OTHER   ->  contactsToRet.postValue(otherContacts.value)
        else                    ->  contactsToRet.postValue(allContacts.value)
    }

    suspend fun fetchContacts(){
//        if (contacts.value  ==  null){
//            contacts.value  =   ArrayList()
//        }

//        contacts?.value?.clear()

        val cursor      = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor?.count!!> 0) {

//            val newContacts =   ArrayList<DummyModel>()

            var toAdd   : DummyModel
            while (cursor.moveToNext()) {

                toAdd   =   buildContact(cursor)
//                newContacts.add(toAdd)
//                contacts?.value?.add(toAdd)
//
//                when (toAdd.type){
//                    DummyModel.TYPE_OTHER   ->  otherContacts.value?.add(toAdd)
//                    DummyModel.TYPE_JAZZ   ->  jazzContacts.value?.add(toAdd)
//                    DummyModel.TYPE_UFONE   ->  ufoneContacts.value?.add(toAdd)
//                    DummyModel.TYPE_ZONG   ->  zongContacts.value?.add(toAdd)
//                    DummyModel.TYPE_TELENOR   ->  telenorContacts.value?.add(toAdd)
//                }
                contactDao.insert(toAdd)
            }
        }

        cursor?.close()
        Log.d("xcrv", "fetched contacts: ${contacts?.value?.size}")
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