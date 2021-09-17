package com.example.a4v4.application

import android.content.Context
import android.content.pm.PackageInfo
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a4v4.database.ContactsDao
import com.example.a4v4.database.ContactsModel
import com.example.a4v4.database.MyFiles
import com.example.a4v4.database.MyFilesDao
import com.example.a4v4.utils.ContactBuilder


class Repo(
    private val context: Context,
    private val contactDao: ContactsDao,
    private val myFilesDao: MyFilesDao,
    ) {
    /*------------------------------ C O R E    V A R I A B L E S ---------------------------------*/

    var allContacts             : LiveData<List<ContactsModel>>     =   contactDao.getContacts()

    var selectedContact         = MutableLiveData<ContactsModel?>().apply { value = ContactsModel(-1, "", "",  5, "", "", "", "", false) }

    private val ufoneContacts   : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_UFONE)
    private val telenorContacts : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_TELENOR)
    private val zongContacts    : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_ZONG)
    private val jazzContacts    : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_JAZZ)
    private val otherContacts   : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_OTHER)

    private val myApps          =   MutableLiveData<ArrayList<PackageInfo>>().apply { value=ArrayList() }
    private val myFiles         :   LiveData<List<MyFiles>> =   myFilesDao.getFiles()
    private val myLogs          =   MutableLiveData<ArrayList<PackageInfo>>().apply { value=ArrayList() }

    /*------------------------------ C O M M U N I C A T O R S ---------------------------------*/

    // communicator methods to share observable live data
    fun getApps()   =   myApps as LiveData<ArrayList<PackageInfo>>

    fun getFiles()  =   myFiles

    fun getLogs()   =   myLogs as LiveData<ArrayList<PackageInfo>>

    fun getSelectedContact()    =   selectedContact as LiveData<ContactsModel?>

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

    suspend fun insertFile(myFile:MyFiles)    =   myFilesDao.insert(myFile)

    /*------------------------------ F E T C H   M E T H O D S ---------------------------------*/

    suspend fun fetchContacts(){
        val sharedPref = context.getSharedPreferences("contacts", Context.MODE_PRIVATE)
        val lastCount       =   sharedPref.getInt("count", -1)

        val cursor      = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        val currentCount    =   cursor?.count

        if (currentCount==lastCount) {
            return
        }

        var go  =   10
        val myContactBuilder    =   ContactBuilder(context)

        if (cursor?.count!!> 0) {
            var toAdd   : ContactsModel
            while (cursor.moveToNext() && go!=0) {
                go-=1
                toAdd   =   myContactBuilder.buildContact(cursor)
                contactDao.insert(toAdd)
            }
        }
        cursor.close()

        with (sharedPref.edit()) {
            putInt("count", myContactBuilder.count)
            apply()
        }
    }


    fun fetchApps() {

        val arrayOfAppInfo =
            context.packageManager.getInstalledPackages(0)

//        for (app in arrayOfAppInfo)
//            when {
//                app.applicationInfo.flags!=null && ApplicationInfo.FLAG_UPDATED_SYSTEM_APP !== 0 -> {
//                    myApps.add(app)
//                }
//                app.applicationInfo.flags!=null && ApplicationInfo.FLAG_SYSTEM !== 0 -> {
//                    //Discard
//                }
//                else -> {
////                    myApps.add(app)
//                }
//            }

//        for (app   in  arrayOfAppInfo)
//            if (!(app.applicationInfo.flags!=null && ApplicationInfo.FLAG_SYSTEM!=null))
//                myApps.add(app)

        arrayOfAppInfo.sortWith(compareBy { it.applicationInfo.loadLabel(context.packageManager).toString()})
        myApps.postValue(arrayOfAppInfo as ArrayList<PackageInfo>)
    }

    fun fetchLogs() {
        TODO("Not yet implemented")
    }
}