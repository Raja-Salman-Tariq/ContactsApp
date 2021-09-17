package com.example.a4v4.utils

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.example.a4v4.database.ContactsModel

class ContactBuilder(val context: Context){

    var count = 0

    /*------------------------------ H E L P E R    V A R I A B L E S ---------------------------------*/
    var id: Long?                       =   null
    var name: String?                   =   null
    private var num: String?            =   null
    private var email: String?          =   null
    private var address:String?         =   null
    private var organization:String?    =   null
    var title:String?                   =   null

    /*------------------------------ H E L P E R    F U N C T I O N S ---------------------------------*/

    fun buildContact(cursor: Cursor): ContactsModel {
        count++ //  to count new total instances of contacts being added for saving count in shared pref

        id              =   cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
        name            =   cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
        num             =   acquireNumbers(id.toString())
        email           =   acquireEmails(id.toString())
        address         =   acquireAddresses(id.toString())
        organization    =   acquireOrganizations(id.toString())
        title           =   acquireTitle(id.toString())


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
        val orgWhereParams = arrayOf(id, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
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
        val orgWhereParams = arrayOf(id, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
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

}