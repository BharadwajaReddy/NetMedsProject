package com.bharadwaja.netmeds.ui.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bharadwaja.netmeds.data.repository.DownloadImageRepository
import com.bharadwaja.netmeds.data.repository.SearchKeyRepository
import com.bharadwaja.netmeds.utilities.networking.CheckInternetAvailability


class ImageDetailsViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var downloadStatusLiveData: LiveData<Boolean>
    var downloadImageRepository: DownloadImageRepository
    val context = application.applicationContext

    init {
        downloadImageRepository = DownloadImageRepository()
    }

    fun downloadFile(completeFilePath: String) {
        val checkInternet = CheckInternetAvailability()
        if (checkInternet.isNetworkAvailbale(context)) {
            val appContext: Context = getApplication()
            val fileName: List<String> = completeFilePath.split("/get/")




            downloadImageRepository.downloadImageToAppSandbox(
                appContext,
                fileName.get(1),
                "get/" + fileName.get(1)
            )
            downloadStatusLiveData = downloadImageRepository.getFileDownloadStatus()

        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    fun getDownloadStatus(): LiveData<Boolean> {
        return downloadStatusLiveData
    }
}