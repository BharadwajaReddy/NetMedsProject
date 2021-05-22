package com.bharadwaja.netmeds.data.repository


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bharadwaja.netmeds.data.models.PictureDetailsModel
import com.bharadwaja.netmeds.utilities.networking.RetrofitInterface
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class SearchKeyRepository {


    lateinit var searchResultsMutableLiveData: MutableLiveData<ArrayList<PictureDetailsModel>>

    val TAG: String = "NetworkCall"

    fun searchKeyInPixabay(query: String) {
        searchResultsMutableLiveData = MutableLiveData()
        val httpClient = OkHttpClient.Builder()
        val builder = Retrofit.Builder().baseUrl("https://pixabay.com/")
        val retrofit = builder.client(httpClient.build()).build()
        val downloadService = retrofit.create(RetrofitInterface::class.java)
        val call: Call<ResponseBody?>? =
            downloadService.getSearchResponse("api/?key=21687114-6a3e3f2f8050e229a3a3f8c8a&q=" + query + "&image_type=photo&pretty=true")
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                if (response.isSuccessful()) {


                    CoroutineScope(Dispatchers.IO).launch {
                        processResponseToArrayList(response)
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

    private suspend fun processResponseToArrayList(response: Response<ResponseBody?>) =
        withContext(Dispatchers.IO) {
            val searchItemsArraylist: ArrayList<PictureDetailsModel> = ArrayList()
            val responseJSONObject = JSONObject(response.body()?.string())
            val hitsJSONArray: JSONArray = responseJSONObject.getJSONArray("hits")

            for (i in 0 until hitsJSONArray.length()) {
                val singleHitJSONObject: JSONObject = hitsJSONArray.getJSONObject(i)
                val PreviewUrl: String = singleHitJSONObject.getString("previewURL")
                val LargeImageUrl: String = singleHitJSONObject.getString("largeImageURL")
                val Favorites: Int = singleHitJSONObject.getInt("favorites")
                val Likes: Int = singleHitJSONObject.getInt("likes")
                val Comments: Int = singleHitJSONObject.getInt("comments")
                val UserName: String = singleHitJSONObject.getString("user")
                val Type: String = singleHitJSONObject.getString("type")
                val Tag: String = singleHitJSONObject.getString("tags")
                searchItemsArraylist.add(
                    PictureDetailsModel(
                        PreviewUrl,
                        LargeImageUrl,
                        Favorites,
                        Likes,
                        Comments,
                        UserName,
                        Type,
                        Tag
                    )
                )
            }


            searchResultsMutableLiveData.postValue(searchItemsArraylist)
        }

    public fun getKeySearchResults(): LiveData<ArrayList<PictureDetailsModel>> {
        return searchResultsMutableLiveData
    }

    /* fun downloadImageToAppSandbox(mContextt: Context, filename: String, childPath: String) {

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
                         response.body()?.let { saveToDisk(it, filename) }
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


     private suspend fun saveToDisk(body: ResponseBody, filename: String) =
         withContext(Dispatchers.IO) {

             try {
                 File("/data/data/" + mContext.getPackageName().toString() + "/Pixabay").mkdir()
                 val destinationFile =
                     File(
                         "/data/data/" + mContext.getPackageName()
                             .toString() + "/images/" + filename
                     )
                 var `is`: InputStream? = null
                 var os: OutputStream? = null
                 try {
                     val filesize = body.contentLength()
                     Log.d(TAG, "File Size=" + body.contentLength())
                     `is` = body.byteStream()
                     os = FileOutputStream(destinationFile)
                     val data = ByteArray(4096)
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


     fun getFileDownloadStatus(): LiveData<Boolean> {
         return downloadImageStatusMutableLiveData
     }*/


}