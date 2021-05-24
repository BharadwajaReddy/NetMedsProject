package com.bharadwaja.netmeds.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bharadwaja.netmeds.R
import com.bharadwaja.netmeds.databinding.ActivityImageDetailsLayoutBinding
import com.bharadwaja.netmeds.ui.viewmodels.ImageDetailsViewModel
import com.bharadwaja.netmeds.utilities.general.CustomProgressBar


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

    lateinit var binding: ActivityImageDetailsLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_details_layout)
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

        val picasso = Picasso.get()
        picasso.load(largeImageUrl).into(binding.ivOriginalPhoto)
        binding.tvComment.text = comments.toString()
        binding.tvFavourites.text = favourites.toString()
        binding.tvLikes.text = likes.toString()
        binding.tvUserName.text = username.toString()
        binding.ibShare.setOnClickListener(this)
        binding.ibInfo.setOnClickListener(this)
        binding.ibDownload.setOnClickListener(this)
        binding.ibClose.setOnClickListener(this)
    }


    fun downloadImageInAppSandbox(imageurl: String) {
        val progressbar = CustomProgressBar(this)
        progressbar.setProgressDialog()
        imageDetailsViewModel = ViewModelProvider(this).get(ImageDetailsViewModel::class.java)
        imageDetailsViewModel.downloadFile(imageurl)
        observerObj = Observer {
            progressbar.HideProgressDialog()
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