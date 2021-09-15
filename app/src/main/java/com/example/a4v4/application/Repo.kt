package com.example.a4v4.application

import android.content.Context
import android.content.pm.ApplicationInfo
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.a4v4.database.ContactsDao
import com.example.a4v4.database.ContactsModel
import android.content.pm.PackageManager





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

    val myApps          =   ArrayList<ApplicationInfo>()

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
            ContactsContract.Contacts.CONTENT_URI,
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

//        sth(cursor)
        id              =   cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
        name            =   cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
        num             =   acquireNumbers(id.toString())//cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
        email           =   acquireEmails(id.toString())
        address         =   acquireAddresses(id.toString())//cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS))
        organization    =   acquireOrganizations(id.toString())//acquireOrganizations(id.toString())//cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.DISPLAY_NAME))
        title           =   acquireTitle(id.toString())//acquireTitles(id.toString())//cursor.getString(cursor?.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.TITLE))

        Log.d("idx", "fetchContacts: {id:${id}}-{name:$name}-{num:$num}-{email:$email}-{addr:$address}-{orga:$organization}")

        return ContactsModel(
            id,
            name,
            num,
            NetworkDiscerner(num).type,
            email,
            address,
            organization,
            title,
            false
        )
    }

    private fun acquireEmails(id:String): String{
        val emails  =   context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID+" = "+id,
            null,
            null
        )!!
        val emailList   =   ArrayList<String>()

        while (emails.moveToNext()){
            emailList.add(emails.getString(emails.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA)))
        }

        emails.close()
        return if (emailList.isNotEmpty()) emailList[0] else ""
    }

    private fun acquireNumbers(id:String): String{
        val cursor  =   context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+id,
            null,
            null
        )!!
        val contentList   =   ArrayList<String>()

        while (cursor.moveToNext()){
            contentList.add(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DATA)))
        }

        cursor.close()
        return if (contentList.isNotEmpty()) contentList[0] else ""
    }

    private fun acquireAddresses(id:String): String{
        val cursor  =   context.contentResolver.query(
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID+" = "+id,
            null,
            null
        )!!
        val contentList   =   ArrayList<String>()

        while (cursor.moveToNext()){
            contentList.add(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.DATA)))
        }

        cursor.close()
        return if (contentList.isNotEmpty()) contentList[0] else ""
    }

    private fun acquireOrganizations(id:String): String{
        val contentList   =   ArrayList<String>()

        val orgWhere =
            ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"
        val orgWhereParams = arrayOf(id,ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
        val orgCur: Cursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null, orgWhere, orgWhereParams, null
        )!!
        while (orgCur.moveToNext()) {
                contentList.add(orgCur.getString(orgCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.DATA)))
        }
        orgCur.close()
        return if (contentList.isNotEmpty()) contentList[0] else ""
    }

    private fun acquireTitle(id:String): String{
        val contentList   =   ArrayList<String>()

        val orgWhere =
            ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"
        val orgWhereParams = arrayOf(id,ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
        val orgCur: Cursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null, orgWhere, orgWhereParams, null
        )!!
        while (orgCur.moveToNext()) {
            contentList.add(orgCur.getString(orgCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.TITLE)).let{ it?: "" })
        }
        orgCur.close()
        return if (contentList.isNotEmpty()) contentList[0] else ""
    }

    fun fetchApps() {
        myApps.addAll(context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA))
    }
}