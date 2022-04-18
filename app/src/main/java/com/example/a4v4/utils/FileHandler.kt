package com.example.a4v4.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.a4v4.database.ContactsModel
import com.example.a4v4.database.MyFiles
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.sql.Timestamp


class FileHandler(private val ctxt: Context) {

    var myFile          :   MyFiles?    =   null
    private var myTs    =   Timestamp((System.currentTimeMillis())).toString()

    init{
        try{
            fos =ctxt.openFileOutput(FILE_NAME +"_${myTs}.csv", MODE_PRIVATE)
        }catch (e:  Exception){
            fos =   null
            e.printStackTrace()
        }
    }

    /*--------------------------------------------------------------------------------------------*/
    // "static" stream object to ensure that stream is closed when app is terminating by calling fos.close()
    companion object{
        private const val FILE_NAME    =   "contacts"

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

    /*--------------------------------------------------------------------------------------------*/
    // function to generate contacts csv; called when user selects export option
    fun createCSV(contacts : List<ContactsModel>?): FileHandler {

        if (contacts == null) {
            return this
        }

        if (fos == null) {
            myTs    =   Timestamp((System.currentTimeMillis())).toString()
            fos = ctxt.openFileOutput(FILE_NAME +"_${myTs}.csv", MODE_PRIVATE)
        }

        try {

            // Successfully writing to file
            fos?.write("Index, Name, Number, Type\n".toByteArray())
            for (contact in contacts) {
                fos?.write(contact.toString().toByteArray())
            }

            myFile  =   MyFiles(FILE_NAME+"_$myTs.csv", myTs)

            // Sharing via android share sheet
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ctxt, "com.example.a4v4..fileprovider", ctxt.getFileStreamPath(
                    FILE_NAME+"_$myTs.csv"
                )))
                type = "text/csv"
            }
            ctxt.startActivity(Intent.createChooser(shareIntent, "Send through..."))

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return this
    }
}