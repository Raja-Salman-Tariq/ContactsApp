package com.example.a4v4.application

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import com.example.a4v4.database.ContactsModel
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class FileHandler(private val ctxt: Context) {

    init{
        try{
            fos =ctxt.openFileOutput(FILE_NAME, MODE_PRIVATE)
        }catch (e:  Exception){
            fos =   null
            e.printStackTrace()
        }

        Log.d("myfilesdirx", ":${ctxt.filesDir} ")
    }

    companion object{
        private const val FILE_NAME    =   "contacts.csv"

        var fos: FileOutputStream? = null

        fun closeFos(){
            if (fos != null) {
                try {
                    fos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun createCSV(contacts : List<ContactsModel>?): Boolean {

        if (contacts == null) {
            Log.d("myfilesdirx", "null recvd: ")
            return true
        }

        if (fos == null) {
            fos = ctxt.openFileOutput(FILE_NAME, MODE_PRIVATE)
        }

        try {
            fos?.write("Index, Name, Number, Type\n".toByteArray())
            for (contact in contacts) {
                fos?.write(contact.toString().toByteArray())
                Log.d("myfilesdirx", "successfully wrote: $contact")
            }

            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, ctxt.getFileStreamPath(FILE_NAME))
                type = "text/csv"
            }
            ctxt.startActivity(Intent.createChooser(shareIntent, "Send through..."))

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return true
    }
}