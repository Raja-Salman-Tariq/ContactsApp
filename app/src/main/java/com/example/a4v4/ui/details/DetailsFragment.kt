package com.example.a4v4.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.databinding.FragmentDetailsBinding
import com.example.a4v4.application.MyApp
import com.example.a4v4.database.ContactsModel

class DetailsFragment : Fragment() {

    private var _binding                :   FragmentDetailsBinding?    =   null
    private val binding get()           =   _binding!!


    private lateinit var nameText       :   TextView
    private lateinit var contactText    :   TextView
    private lateinit var emailText      :   TextView
    private lateinit var address        :   TextView
    private lateinit var org            :   TextView
    private lateinit var jobtitle       :   TextView

    private lateinit var contact        : ContactsModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contact     =   (requireActivity().application as MyApp).repository.selectedContact!!

        nameText    =   binding.fragDetailsNameText
        contactText =   binding.fragDetailsContactText
        emailText   =   binding.fragDetailsEmailText
        address     =   binding.fragDetailsAddressText
        org         =   binding.fragDetailsOrganizationText
        jobtitle    =   binding.fragDetailsJobTitleText


        nameText.text   =   contact.name
        contactText.text=   contact.number
        emailText.text  =   contact.email
        address.text    =   contact.address
        org.text        =   contact.org
        jobtitle.text   =   contact.title
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).let{
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.back_btn)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)   //show back button
        }
    }
}