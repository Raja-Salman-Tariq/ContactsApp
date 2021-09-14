package com.example.a4v4.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.databinding.FragmentHomeBinding
import com.example.a4v4.rv.MyRvAdapter
import com.example.a4v4.application.MyApp
import com.google.android.material.snackbar.Snackbar


class HomeFragment(val mainActivity: MainActivity) : Fragment(R.layout.fragment_home) {
    val homeViewModel           :   HomeViewModel           by  viewModels{
    MyViewModelFactory((this.requireActivity().application as MyApp).repository!!)
    }
    private var _binding                :   FragmentHomeBinding?    =   null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get()           =   _binding!!

    private lateinit var recyclerView   : RecyclerView
    lateinit var myRvAdapter    : MyRvAdapter

    /*--------------------------------------------------------------------------------------------*/

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        getPermission(mainActivity)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpRv()

        return root
    }

    /*--------------------------------------------------------------------------------------------*/

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).let{
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.hamburger_icon)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)   //show back button
            it.setActionBarTitle(it.myDrawer.selectedTitle)
        }
    }

    private fun setUpRv() {
        recyclerView                =   binding.fragmentHomeRv
        myRvAdapter                 =   MyRvAdapter(this, arrayListOf())
        recyclerView.layoutManager  =   LinearLayoutManager(requireContext())
        recyclerView.adapter        =   myRvAdapter

        homeViewModel.data.observe(viewLifecycleOwner){
                data    ->  myRvAdapter.updateData(data)
        }


    }

    /*--------------------------------------------------------------------------------------------*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*--------------------------------------------------------------------------------------------*/

    fun getContacts(type:Short){
        homeViewModel.getContacts(type).observe(viewLifecycleOwner){
                data    ->  myRvAdapter.updateData(data)
        }
    }

    private fun getPermission(mainActivity: MainActivity){
        val permission: String = Manifest.permission.READ_CONTACTS
        val grant = ContextCompat.checkSelfPermission(mainActivity, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissionList = arrayOfNulls<String>(1)
            permissionList[0] = permission
            ActivityCompat.requestPermissions(mainActivity, permissionList, 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Snackbar
                    .make((mainActivity).binding.root, "Please grant permission for contacts to use this app.", Snackbar.LENGTH_LONG)
                    .setAction("Close App") {
                        mainActivity.finish()
                    }
                    .show()
            }
        }
    }
}