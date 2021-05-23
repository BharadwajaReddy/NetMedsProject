package com.bharadwaja.netmeds.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bharadwaja.netmeds.utilities.networking.RetrofitInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.lang.Exception


class DownloadImageRepository {

    lateinit var downloadImageStatusMutableLiveData: MutableLiveData<Boolean>
    lateinit var mContext: Context
    val TAG: String = "NetworkCall"
    fun downloadImageToAppSandbox(mContextt: Context, filename: String, childPath: String) {

        mContext = mContextt
        downloadImageStatusMutableLiveData = MutableLiveData()
        val httpClient = OkHttpClient.Builder()
        val builder = Retrofit.Builder().baseUrl("https://pixabay.com/")
        val retrofit = builder.client(httpClient.build()).build()
        val downloadService = retrofit.create(RetrofitInterface::class.java)
        val call: Call<ResponseBody?>? =
            downloadService.downloadFileByUrl(childPath)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Got the body for the file")
                    CoroutineScope(Dispatchers.IO).launch {
                        // response.body()?.let { DownloadToApppackage(it, filename) }

                        response.body()?.let {
                            DownloadToImages(it, filename)
                           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                DownloadToImages(it, filename)
                            } else {
                                Toast.makeText(
                                    mContext,
                                    "Feature Built for Naugot and above versions only.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }*/
                        }
                    }
                } else {
                    Log.d(TAG, "Connection failed " + response.errorBody())
                }
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }
        })

    }


    private suspend fun DownloadToApppackage(body: ResponseBody, filename: String) =
        withContext(Dispatchers.IO) {

            try {
                File("/data/data/" + mContext.getPackageName().toString() + "/Pixabay").mkdir()
                val destinationFile =
                    File(
                        "/data/data/" + mContext.getPackageName()
                            .toString() + "/Pixabay/" + filename
                    )
                var `is`: InputStream? = null
                var os: OutputStream? = null
                try {
                    val filesize = body.contentLength()
                    Log.d(TAG, "File Size=" + body.contentLength())
                    `is` = body.byteStream()
                    os = FileOutputStream(destinationFile)
                    val data = ByteArray(1024)
                    var count: Int
                    var progress = 0
                    while (`is`.read(data).also { count = it } != -1) {
                        os.write(data, 0, count)
                        progress += count
                        // publishing the progress....
                        if (filesize > 0) {
                        } // only if total length is known
                        // downloadDataProgress.postValue((progress * 100 / filesize))
                        Log.d(
                            TAG,
                            "Progress: " + (progress * 100 / filesize)
                        )
                    }
                    os.flush()

                    downloadImageStatusMutableLiveData.postValue(true)
                    Log.d(TAG, "File saved successfully!")
                    // return
                } catch (e: IOException) {
                    downloadImageStatusMutableLiveData.postValue(false)
                    e.printStackTrace()
                    Log.d(TAG, "Failed to save the file!")
                    //  return
                } finally {
                    if (`is` != null) `is`.close()
                    if (os != null) os.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d(TAG, "Failed to save the file!")
                // return
            }
        }



    fun DownloadToImages(body: ResponseBody, filename: String) {
        try {
            val path = mContext.getExternalFilesDir("Pixabay")
            val destinationFile = File(path, filename)
            // Make sure the Pictures directory exists.
            // path.mkdirs();
            var `is`: InputStream? = null
            var os: OutputStream? = null
            try {
                val filesize = body.contentLength()
                Log.d(TAG, "File Size=" + body.contentLength())
                `is` = body.byteStream()
                os = FileOutputStream(destinationFile)
                val data = ByteArray(1024)
                var count: Int
                var progress = 0
                while (`is`.read(data).also { count = it } != -1) {
                    os.write(data, 0, count)
                    progress += count
                    // publishing the progress....
                    if (filesize > 0) {
                    } // only if total length is known
                    // downloadDataProgress.postValue((progress * 100 / filesize))
                    Log.d(
                        TAG,
                        "Progress: " + (progress * 100 / filesize)
                    )
                }
                os.flush()

                downloadImageStatusMutableLiveData.postValue(true)
                Log.d(TAG, "File saved successfully!")
                // return
            } catch (e: IOException) {
                downloadImageStatusMutableLiveData.postValue(false)
                e.printStackTrace()
                Log.d(TAG, "Failed to save the file!")
                //  return
            } finally {
                if (`is` != null) `is`.close()
                if (os != null) os.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d(TAG, "Failed to save the file!")
            downloadImageStatusMutableLiveData.postValue(false)
            // return
        }


    }

    fun getFileDownloadStatus(): LiveData<Boolean> {
        return downloadImageStatusMutableLiveData
    }
}