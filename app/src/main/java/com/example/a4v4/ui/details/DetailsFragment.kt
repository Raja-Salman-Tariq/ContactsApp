package com.example.a4v4.ui.details

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.application.MyApp
import com.example.a4v4.database.ContactsModel
import com.example.a4v4.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class DetailsFragment : Fragment() {

    private var _binding                :   FragmentDetailsBinding?    =   null
    private val binding get()           =   _binding!!


    private lateinit var img            :   CircleImageView
    private lateinit var nameText       :   TextView
    private lateinit var contactText    :   TextView
    private lateinit var emailText      :   TextView
    private lateinit var address        :   TextView
    private lateinit var org            :   TextView
    private lateinit var jobtitle       :   TextView

    private lateinit var contact        : ContactsModel

    /*----------------------  L I F E C Y C L E   C A L L B A C K S ------------------------------*/

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

        contact     =   (requireActivity().application as MyApp).repository.selectedContact.value!!

        img         =   binding.fragDetailsImg
        nameText    =   binding.fragDetailsNameText
        contactText =   binding.fragDetailsContactText
        emailText   =   binding.fragDetailsEmailText
        address     =   binding.fragDetailsAddressText
        org         =   binding.fragDetailsOrganizationText
        jobtitle    =   binding.fragDetailsJobTitleText


        Picasso.get().load(contact.getImgUri()).placeholder(R.drawable.icon_place_holder).into(img)
        nameText.text   =   contact.name
        contactText.text=   contact.number
        emailText.text  =   contact.email
        address.text    =   contact.address
        org.text        =   contact.org
        jobtitle.text   =   contact.title

        (requireActivity() as MainActivity).setActionBarTitle(
            if (contact.name == "Unavailable") "Unnamed Contact" else contact.name
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).let{
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.back_btn)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)   //show back button
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_search)?.isVisible  =   false
        menu.findItem(R.id.calllog)?.isVisible      =   true

        super.onCreateOptionsMenu(menu, inflater)
    }
}