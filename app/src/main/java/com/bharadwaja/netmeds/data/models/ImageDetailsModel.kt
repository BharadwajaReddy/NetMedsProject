package com.bharadwaja.netmeds.data.models

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso


data class ImageDetailsModel(
    var PreviewUrl: String,
    var LargeImageUrl: String,
    var Favorites: Int,
    var Likes: Int,
    var Comments: Int,
    var userName: String,
    var Type: String,
    var Tag: String
) {

/*    private lateinit var PreviewUrl: String
    private lateinit var LargeImageUrl: String
    private var Favorites: Int = 0
    private var Likes: Int = 0
    private var Comments: Int = 0
    private lateinit var userName: String
    private lateinit var Type: String
    private lateinit var Tag: String*/

    companion object {
        @BindingAdapter("imageurl")
        @JvmStatic
        fun setImageUrl(imgView: ImageView, imgUrl: String?) {
            imgUrl?.let {
                val imgUri = it.toUri().buildUpon().scheme("https").build()
                val picasso = Picasso.get()
                picasso.load(imgUri).into(imgView)
            }
        }
    }

}