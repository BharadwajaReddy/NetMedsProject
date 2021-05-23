package com.bharadwaja.netmeds.ui.activities

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bharadwaja.netmeds.R
import com.bharadwaja.netmeds.data.models.PictureDetailsModel
import com.bharadwaja.netmeds.databinding.ActivitySearchInPixabayBinding
import com.bharadwaja.netmeds.ui.adaptors.DisplayImagesAdaptor
import com.bharadwaja.netmeds.ui.viewmodels.SearchInPixabayViewModel


class SearchInPixabayActivity : AppCompatActivity() {
    lateinit var displayImagesAdaptor: DisplayImagesAdaptor
    lateinit var photoList: ArrayList<PictureDetailsModel>
    lateinit var searchInPixabayViewModel: SearchInPixabayViewModel
    lateinit var observerObj: Observer<ArrayList<PictureDetailsModel>>
    lateinit var no_results: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_search_in_pixabay)
        val binding: ActivitySearchInPixabayBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_search_in_pixabay)

       // binding.
        photoList = ArrayList()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        displayImagesAdaptor = DisplayImagesAdaptor(photoList, this)
        StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        ).apply { recyclerView.layoutManager = this }
        recyclerView.adapter = displayImagesAdaptor
        val search_content = findViewById<EditText>(R.id.search_content)
        search_content.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                if (search_content.text.length > 0) {
                    println("==go_clicked==")
                    searchQuery(search_content.text.toString())
                } else {
                    Toast.makeText(this, "Enter text to search", Toast.LENGTH_LONG).show()
                }
            }
            true
        }

        searchInPixabayViewModel =
            ViewModelProvider(this).get(SearchInPixabayViewModel::class.java)

        no_results = findViewById(R.id.no_results)
        no_results.visibility = GONE
    }


    fun searchQuery(query: String) {
        photoList.clear()
        searchInPixabayViewModel.getSearchResults(query)
        observerObj = Observer {
            if (it.size > 0) {
                photoList.addAll(it)
                no_results.visibility = GONE
                displayImagesAdaptor.notifyDataSetChanged()
            } else {
                displayImagesAdaptor.notifyDataSetChanged()
                no_results.visibility = VISIBLE
            }
        }
        searchInPixabayViewModel.getSearchResultsFromRepository()
            .observe(this, observerObj)
    }

}