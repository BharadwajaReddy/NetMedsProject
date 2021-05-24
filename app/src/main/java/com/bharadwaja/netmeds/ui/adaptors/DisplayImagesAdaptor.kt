package com.bharadwaja.netmeds.ui.adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bharadwaja.netmeds.R
import com.bharadwaja.netmeds.data.models.ImageDetailsModel
import com.bharadwaja.netmeds.databinding.SingleItemLayoutBinding
import com.bharadwaja.netmeds.ui.activities.ImageDetailsActivity
import java.io.Serializable


class DisplayImagesAdaptor(
    private val ImagesInfoList: List<ImageDetailsModel>,
    context: Context
) :
    RecyclerView.Adapter<DisplayImagesAdaptor.ImagesViewHolder>() {

    val mcontext = context
    override fun getItemCount(): Int = ImagesInfoList.size

    class ImagesViewHolder(val binding: SingleItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val listItem = ImagesInfoList.get(position)
        holder.binding.detailsModel = listItem


        holder.binding.ivOriginalImage.setOnClickListener(View.OnClickListener {
            val startImageDetailsActivityIntent = Intent(
                mcontext,
                ImageDetailsActivity::class.java
            )
            startImageDetailsActivityIntent.putExtra(
                "OriginalImageUrl",
                listItem.LargeImageUrl
            )
            startImageDetailsActivityIntent.putExtra(
                "CommentsCount",
                listItem.Comments
            )
            startImageDetailsActivityIntent.putExtra("LikesCount", listItem.Likes)
            startImageDetailsActivityIntent.putExtra(
                "FavouritesCount",
                listItem.Favorites
            )
            startImageDetailsActivityIntent.putExtra("UserName", listItem.userName)
            startImageDetailsActivityIntent.putExtra("Type", listItem.Type)
            startImageDetailsActivityIntent.putExtra("Tags", listItem.Tag)

            mcontext.startActivity(startImageDetailsActivityIntent)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding: SingleItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.single_item_layout,
            parent,
            false
        )
        return ImagesViewHolder(binding)
    }


}