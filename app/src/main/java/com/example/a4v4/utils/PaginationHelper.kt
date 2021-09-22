package com.example.a4v4.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.a4v4.MainActivity
import com.example.a4v4.database.ContactsModel
import com.example.a4v4.rv.MyRvAdapter
import com.example.a4v4.ui.home.HomeFragment

class PaginationHelper(
    private val fragmemt    :   HomeFragment,
    var adapter  :   MyRvAdapter?=null,
    _size     :   Int =   25,
    _cursor   :   Int =   0){

    init {
        if (size == null)
            size    =   _size

        if (cursor  ==  null)
            cursor  =   _cursor
    }

    companion object{
        private var size     :   Int?   =   null
        private var cursor   :   Int?   =   null
    }

    fun pageData(data   :   List<ContactsModel>):Boolean{

//        if (data.size % size!! != 0)
//            return true

        val mySize =         if (cursor!!+ size!!>= adapter!!.itemCount) adapter!!.itemCount else size

        if (adapter?.itemCount==0) {
            adapter?.addData(data)
        }

        Log.d("pagedata", "pageDataaa: sz ${data.size}, cursor@$cursor; adaItmCnt@${adapter?.itemCount}; size@$mySize ")
        if (cursor!! >= adapter!!.itemCount) {
            Log.d("pagedata", "returning cuz ended")
            return false
        }



        (fragmemt.requireActivity() as MainActivity).handleLoading(true)
        Log.d("pagedata", "adding")
        adapter?.addData(data.subList(cursor!!, cursor!! + mySize!!))
        Log.d("pagedata", "added")
        cursor = cursor?.plus(size!!)
        (fragmemt.requireActivity() as MainActivity).handleLoading(false)

        return cursor!!>= adapter!!.itemCount
    }

}