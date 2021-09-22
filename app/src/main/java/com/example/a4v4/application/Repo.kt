package com.example.a4v4.application

import android.content.Context
import android.content.pm.PackageInfo
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
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
    val items   =   Pager(PagingConfig(50, enablePlaceholders = true, maxSize = 200)){contactDao.getPagedContacts()}.flow //  for paging attempt 1

    var allContacts             : LiveData<List<ContactsModel>>     =   contactDao.getContacts()

    var selectedContact         = MutableLiveData<ContactsModel?>().apply { value = null }

    private val ufoneContacts   : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_UFONE)
    private val telenorContacts : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_TELENOR)
    private val zongContacts    : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_ZONG)
    private val jazzContacts    : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_JAZZ)
    private val otherContacts   : LiveData<List<ContactsModel>>     =   contactDao.getContacts(ContactsModel.TYPE_OTHER)

    private val myApps          =   MutableLiveData<ArrayList<PackageInfo>>().apply { value=ArrayList() }
    private val myFiles         :   LiveData<List<MyFiles>> =   myFilesDao.getFiles()
    private val myLogs          =   MutableLiveData<ArrayList<PackageInfo>>().apply { value=ArrayList() }

    private val loading         =   MutableLiveData<Boolean>().apply {value=true}
    val allContactsLoaded       =   MutableLiveData<Boolean>().apply { value=false }

    /*------------------------------ C O M M U N I C A T O R S ---------------------------------*/

    // communicator methods to share observable live data
    fun getLoading()    =   loading as LiveData<Boolean>

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
        loading.postValue(true)

        Log.d("loadarr", " * * * fetchContacts: true ")

        val sharedPref = context.getSharedPreferences("contacts", Context.MODE_PRIVATE)
        val lastCount       =   sharedPref.getInt("count", -1)

        val cursor      = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + ") ASC"
        )

        val currentCount    =   cursor?.count

        if (currentCount==lastCount) {
            loading.postValue(false)
            allContactsLoaded.postValue(true)
            Log.d("loadarr", " * * * fetchContacts: FALSE ")
            return
        }

        contactDao.deleteAll()

        val myContactBuilder    =   ContactBuilder(context)

        val listToAdd   =   ArrayList<ContactsModel>()

        if (cursor?.count!!> 0) {
            var toAdd   : ContactsModel
            while (cursor.moveToNext()) {
                Log.d("loadarr", "---------")
                toAdd   =   myContactBuilder.buildContact(cursor)
                contactDao.insert(toAdd)
//                listToAdd.add(myContactBuilder.buildContact(cursor))

                if (myContactBuilder.count>=30)
                    loading.postValue(false)
            }
        }
        cursor.close()

        with (sharedPref.edit()) {
            putInt("count", myContactBuilder.count)
            apply()
        }
        Log.d("loadarr", " * * * fetchContacts: FALSE ")

        allContactsLoaded.postValue(true)
    }


    fun fetchApps() {

        loading.postValue(true)

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

        loading.postValue(false)
    }

    fun fetchLogs() {
        TODO("Not yet implemented")
    }
}