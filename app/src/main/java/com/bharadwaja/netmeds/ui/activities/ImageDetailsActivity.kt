package com.bharadwaja.netmeds.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bharadwaja.netmeds.R
import com.bharadwaja.netmeds.ui.viewmodels.ImageDetailsViewModel


import com.squareup.picasso.Picasso


class ImageDetailsActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var imageDetailsViewModel: ImageDetailsViewModel
    lateinit var observerObj: Observer<Boolean>
    var largeImageUrl: String? = null
    var comments: Int = 0
    var likes: Int = 0
    var favourites: Int = 0
    var username: String? = null
    var type: String? = null
    var tags: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_info)

        initviews()
    }

    fun initviews() {
        largeImageUrl = intent.getStringExtra("PhotoOriginalUrl").toString()
        comments = intent.getIntExtra("CommentsCount", 0)
        likes = intent.getIntExtra("LikesCount", 0)
        favourites = intent.getIntExtra("FavouritesCount", 0)
        username = intent.getStringExtra("UserName").toString()
        type = intent.getStringExtra("Type").toString()
        tags = intent.getStringExtra("Tags").toString()

        val iv_original_photo: ImageView = findViewById(R.id.iv_original_photo)
        val picasso = Picasso.get()
        picasso.load(largeImageUrl).into(iv_original_photo)
        val tv_comment: TextView = findViewById(R.id.tv_comment)
        val tv_favourites: TextView = findViewById(R.id.tv_favourites)
        val tv_likes: TextView = findViewById(R.id.tv_likes)
        val tv_user_name: TextView = findViewById(R.id.tv_user_name)
        tv_comment.text = comments.toString()
        tv_favourites.text = favourites.toString()
        tv_likes.text = likes.toString()
        tv_user_name.text = username.toString()

        val ib_share: ImageButton = findViewById(R.id.ib_share)
        ib_share.setOnClickListener(this)
        val ib_info: ImageButton = findViewById(R.id.ib_info)
        ib_info.setOnClickListener(this)
        val ib_download: ImageButton = findViewById(R.id.ib_download)
        ib_download.setOnClickListener(this)

        val ib_close: ImageButton = findViewById(R.id.ib_close)
        ib_close.setOnClickListener(this)
    }


    fun downloadImageInAppSandbox(imageurl: String) {
        imageDetailsViewModel = ViewModelProvider(this).get(ImageDetailsViewModel::class.java)
        imageDetailsViewModel.downloadFile(imageurl)
        observerObj = Observer {
            println("==here==")
            if (it) {
                Toast.makeText(
                    this,
                    "Image downloaded in to application package.",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                Toast.makeText(
                    this,
                    "Error while downloading the image",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
        imageDetailsViewModel.getDownloadStatus().observe(this, observerObj)

    }


    fun shareImageInOthers(imageurl: String?) {
        //check weather any activity is available or not for sharing the string. Because it returns null exception if not available.
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, imageurl)
            type = "text/plain"
        }
        if (sendIntent.resolveActivity(packageManager) != null) {
            startActivity(sendIntent)
        } else {
            Toast.makeText(this, "No app available to share the image", Toast.LENGTH_LONG).show()
        }
    }

    fun displayImageDetails(title: String?, message: String?) {
        val builder: AlertDialog.Builder = this.let { AlertDialog.Builder(it) }
        builder.setMessage(message)?.setTitle(title)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun closeWindow() {
        finish()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.ib_share) {
            shareImageInOthers(largeImageUrl)
        } else if (v?.id == R.id.ib_info) {
            displayImageDetails(type, tags)
        } else if (v?.id == R.id.ib_download) {
            downloadImageInAppSandbox(largeImageUrl.toString())
        } else if (v?.id == R.id.ib_close) {
            closeWindow()
        }
    }


}