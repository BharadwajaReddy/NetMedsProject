package com.bharadwaja.netmeds.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bharadwaja.netmeds.data.models.ImageDetailsModel
import com.bharadwaja.netmeds.data.repository.SearchKeyRepository
import com.bharadwaja.netmeds.utilities.networking.CheckInternetAvailability

class SearchInPixabayViewModel(application: Application) : AndroidViewModel(application) {


    var searchRepository: SearchKeyRepository
    lateinit var searchResultsLivedata: LiveData<ArrayList<ImageDetailsModel>>
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

    fun getSearchResultsFromRepository(): LiveData<ArrayList<ImageDetailsModel>> {
        return searchResultsLivedata
    }
}