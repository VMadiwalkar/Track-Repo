package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter(val context:Context,private val rList:List<RepoModel>):RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
//    lateinit var  context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder (ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val card:CardView = itemView.findViewById(R.id.parentCard)
        val repoName:TextView = itemView.findViewById(R.id.repoName)
        val repoDesc:TextView = itemView.findViewById(R.id.repoDesc)
        val shareView:ImageButton = itemView.findViewById(R.id.shareView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = rList[position]

        // sets the image to the imageview from our itemHolder class


        // sets the text to the textview from our itemHolder class
        holder.repoName.text = ItemsViewModel.name
        holder.repoDesc.text = ItemsViewModel.description
        holder.shareView.setOnClickListener{
            shareRepo(ItemsViewModel.url)
        }
        holder.card.setOnClickListener {

            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(ItemsViewModel.url)
                )
            )

        }

    }

    override fun getItemCount(): Int {
        return rList.size
    }


    fun shareRepo(url:String){
        val myIntent = Intent(Intent.ACTION_SEND)
        myIntent.setType("text/html")
        val body = "Your body here"
        val sub = "Your Subject"
        myIntent.putExtra(Intent.EXTRA_SUBJECT,sub)
        myIntent.putExtra(Intent.EXTRA_TEXT,url)
        myIntent.setAction("Share Using")
        Log.i("Res",url)

        context.startActivity(Intent.createChooser(myIntent, "Share Using"))

    }
}