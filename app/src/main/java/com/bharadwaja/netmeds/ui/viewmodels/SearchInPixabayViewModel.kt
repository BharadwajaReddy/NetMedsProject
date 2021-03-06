package com.bharadwaja.netmeds.ui.viewmodels

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bharadwaja.netmeds.data.models.ImageDetailsModel
import com.bharadwaja.netmeds.data.repository.SearchKeyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SearchInPixabayViewModel(application: Application) : AndroidViewModel(application) {


    var searchRepository: SearchKeyRepository
    var searchResultsLivedata: LiveData<ArrayList<ImageDetailsModel>>
    val context = application

    init {

        searchRepository = SearchKeyRepository()
        searchResultsLivedata = MutableLiveData()
    }

    fun getSearchResults(query: String) {


        searchRepository.searchKeyInPixabay(query)
        searchResultsLivedata = searchRepository.getKeySearchResults()

    }

    fun getSearchResultsFromRepository(): LiveData<ArrayList<ImageDetailsModel>> {
        return searchResultsLivedata
    }

    override fun onCleared() {
        super.onCleared()
    }
}