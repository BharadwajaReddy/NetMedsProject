package com.bharadwaja.netmeds.ui.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bharadwaja.netmeds.data.models.PictureDetailsModel
import com.bharadwaja.netmeds.data.repository.SearchKeyRepository
import com.bharadwaja.netmeds.utilities.networking.CheckInternetAvailability

class SearchInPixabayViewModel(application: Application) : AndroidViewModel(application) {


    var searchRepository: SearchKeyRepository
    lateinit var searchResultsLivedata: LiveData<ArrayList<PictureDetailsModel>>
    val context = application

    init {

        searchRepository = SearchKeyRepository()
    }

    fun getSearchResults(query: String) {
        val checkInternet = CheckInternetAvailability()
        if (checkInternet.isNetworkAvailbale(context)) {
            searchRepository.searchKeyInPixabay(query)
            searchResultsLivedata = searchRepository.getKeySearchResults()
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    fun getSearchResultsFromRepository(): LiveData<ArrayList<PictureDetailsModel>> {
        return searchResultsLivedata
    }
}