package com.example.a4v4.rv

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.database.MyFiles
import com.example.a4v4.databinding.RvAppsLayoutBinding
import com.example.a4v4.databinding.RvFilesLayoutBinding
import com.example.a4v4.ui.apps.AppsFragment
import com.example.a4v4.ui.files.FilesFragment
import com.example.a4v4.utils.MyTimeStampFormatter

class MyFilesRvAdapter(
    private val fragment    :   FilesFragment,
    private val data        :   ArrayList<MyFiles>,
)   :   RecyclerView.Adapter<MyFilesRvAdapter.MyViewHolder>()
{

    private val myTsFormatter   =   MyTimeStampFormatter()

    /*-------------------------------  V I E W   H O L D E R  ------------------------------------*/

    class MyViewHolder(
        private val binding : RvFilesLayoutBinding,
        val name            :   TextView            =   binding.rvFilesName,
        val ts              :   TextView            =   binding.rvFilesTimestamp,
    )   : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding =
            RvFilesLayoutBinding.inflate(
                LayoutInflater.from(fragment.context),
                parent,
                false
            )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myData              =   data[position]
        holder.name.text        =   myData.name
        holder.ts.text          =   myTsFormatter.format(myData.timestamp)
    }

    /*--------------------------------------------------------------------------------------------*/

    override fun getItemCount() =   data.size

    /*-------------------------  C O N V E N I E N C E   M E T H O D  ----------------------------*/

    // handles live data updation and empty text visibility
    fun updateData(updData  :   List<MyFiles>) {
        data.clear()
        if (updData.isEmpty()) {
            fragment.binding.emptyRvFilesTxt.visibility  =   View.VISIBLE
        }
        else {
            fragment.binding.emptyRvFilesTxt.visibility = View.GONE
            data.addAll(updData)
        }
        notifyDataSetChanged()
    }
}