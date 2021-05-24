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


/*
class DisplayImagesAdaptor(val photoList: ArrayList<PictureDetailsModel>, val mcontext: Context) :
    RecyclerView.Adapter<DisplayImagesAdaptor.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_preview_photo = view.findViewById<ImageView>(R.id.iv_preview_photo)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_single_item_layout, parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photoListItem = photoList.get(position)
        val picasso = Picasso.get()
        picasso.load(photoListItem.PreviewUrl).into(holder.iv_preview_photo)
        holder.iv_preview_photo.setOnClickListener(View.OnClickListener {
            val startPhotoDetailsActivityIntent = Intent(
                mcontext,
                ImageDetailsActivity::class.java
            )
            startPhotoDetailsActivityIntent.putExtra(
                "PhotoOriginalUrl",
                photoListItem.LargeImageUrl
            )
            startPhotoDetailsActivityIntent.putExtra("CommentsCount", photoListItem.Comments)
            startPhotoDetailsActivityIntent.putExtra("LikesCount", photoListItem.Likes)
            startPhotoDetailsActivityIntent.putExtra("FavouritesCount", photoListItem.Favorites)
            startPhotoDetailsActivityIntent.putExtra("UserName", photoListItem.userName)
            startPhotoDetailsActivityIntent.putExtra("Type", photoListItem.Type)
            startPhotoDetailsActivityIntent.putExtra("Tags", photoListItem.Tag)
            mcontext.startActivity(startPhotoDetailsActivityIntent)
        })

    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}

*/




class DisplayImagesAdaptor(
    private val picturesList: List<ImageDetailsModel>,
    context: Context
) :
    RecyclerView.Adapter<DisplayImagesAdaptor.ImagesViewHolder>() {

    val mcontext = context
    override fun getItemCount(): Int = picturesList.size

    class ImagesViewHolder(val binding: SingleItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val photoListItem = picturesList.get(position)
        holder.binding.detailsModel = photoListItem


        holder.binding.ivPreviewPhoto.setOnClickListener(View.OnClickListener {
            val startPhotoDetailsActivityIntent = Intent(
                mcontext,
                ImageDetailsActivity::class.java
            )
            startPhotoDetailsActivityIntent.putExtra(
                "PhotoOriginalUrl",
                photoListItem.LargeImageUrl
            )
            startPhotoDetailsActivityIntent.putExtra(
                "CommentsCount",
                photoListItem.Comments
            )
            startPhotoDetailsActivityIntent.putExtra("LikesCount", photoListItem.Likes)
            startPhotoDetailsActivityIntent.putExtra(
                "FavouritesCount",
                photoListItem.Favorites
            )
            startPhotoDetailsActivityIntent.putExtra("UserName", photoListItem.userName)
            startPhotoDetailsActivityIntent.putExtra("Type", photoListItem.Type)
            startPhotoDetailsActivityIntent.putExtra("Tags", photoListItem.Tag)

            mcontext.startActivity(startPhotoDetailsActivityIntent)
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