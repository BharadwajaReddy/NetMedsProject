package com.bharadwaja.netmeds.ui.activities

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bharadwaja.netmeds.R
import com.bharadwaja.netmeds.data.models.ImageDetailsModel
import com.bharadwaja.netmeds.databinding.ActivitySearchInPixabayLayoutBinding
import com.bharadwaja.netmeds.ui.adaptors.DisplayImagesAdaptor
import com.bharadwaja.netmeds.ui.viewmodels.SearchInPixabayViewModel
import com.bharadwaja.netmeds.utilities.general.CustomProgressBar
import com.bharadwaja.netmeds.utilities.networking.CheckInternetAvailability


class SearchInPixabayActivity : AppCompatActivity() {
    lateinit var displayImagesAdaptor: DisplayImagesAdaptor
    lateinit var searchList: ArrayList<ImageDetailsModel>
    lateinit var searchInPixabayViewModel: SearchInPixabayViewModel
    lateinit var observerObj: Observer<ArrayList<ImageDetailsModel>>
    lateinit var binding: ActivitySearchInPixabayLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_in_pixabay_layout)
        searchList = ArrayList()
        displayImagesAdaptor = DisplayImagesAdaptor(searchList, this)
        binding.recyclerView.apply {
            setLayoutManager(
                StaggeredGridLayoutManager(
                    2,
                    StaggeredGridLayoutManager.VERTICAL
                )
            )

            adapter = displayImagesAdaptor

        }



        binding.searchContent.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                if (binding.searchContent.text.length > 0) {
                    val checkInternet = CheckInternetAvailability()
                    if (checkInternet.isNetworkAvailbale(this)) {
                        searchQuery(binding.searchContent.text.toString())
                    } else {
                        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Enter text to search", Toast.LENGTH_LONG).show()
                }
            }
            true
        }

        searchInPixabayViewModel =
            ViewModelProvider(this).get(SearchInPixabayViewModel::class.java)

        binding.noResults.apply { visibility = GONE }


    }


    fun searchQuery(query: String) {
        val progressbar = CustomProgressBar(this)
        progressbar.setProgressDialog()
        searchList.clear()
        searchInPixabayViewModel.getSearchResults(query)
        observerObj = Observer {
            progressbar.HideProgressDialog()
            if (it.size > 0) {
                searchList.addAll(it)
                binding.noResults.apply { visibility = GONE }
                displayImagesAdaptor.notifyDataSetChanged()
            } else {
                displayImagesAdaptor.notifyDataSetChanged()
                binding.noResults.apply { visibility = VISIBLE }
            }
        }
        searchInPixabayViewModel.getSearchResultsFromRepository()
            .observe(this, observerObj)
    }

}