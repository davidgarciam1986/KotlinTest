package com.example.kotlintest.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.recycler.RecyclerViewAdapter
import com.example.kotlintest.recycler.SearchRecyclerViewAdapter


class DashboardActivity: AppCompatActivity(),
    DashboardView,
    RecyclerViewAdapter.ItemClickListener,
    SearchRecyclerViewAdapter.ItemClickListener
{

    //var presenter: DashboardPresenter? = null
    var since: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val progressbar: FrameLayout = findViewById(R.id.progressBarOverlay)
        progressbar.visibility = View.GONE
        val searchView: SearchView = findViewById(R.id.searchView)
        val searchRecyclerView: RecyclerView = findViewById(R.id.recyclerViewSearch)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                //return presenter.searchPublicRepositories()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        searchView.setOnCloseListener {
            if ( searchRecyclerView.visibility == View.GONE ) {
                searchView.clearFocus()
                searchView.visibility = View.GONE
            }
            else {
                searchRecyclerView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                searchView.clearFocus()
                searchView.visibility = View.GONE
            }
            false
        }
        val imageButton: ImageButton = findViewById(R.id.imageButton2)
        imageButton.setOnClickListener { searchView!!.visibility = View.VISIBLE }
        //presenter = DashboardPresenterImpl(this)
        //presenter.getPublicRepositoriesSince(0)
        since = 0
    }



    fun showProgressBar() {
        findViewById<FrameLayout>(R.id.progressBarOverlay).visibility = View.VISIBLE
    }

    fun hideProgress() {
        findViewById<FrameLayout>(R.id.progressBarOverlay).visibility = View.GONE
    }

    fun showError(error: String?) {
        //TODO mostrar error
    }

    fun updateRepositoryList(list: List<Repository>) {
        try {
            since = list[list.size - 1].getId()
            val layoutManager = LinearLayoutManager(this)
            if (recyclerView == null) {
                recyclerView = findViewById(R.id.recyclerView)
                recyclerView.setLayoutManager(layoutManager)
                adapter = RecyclerViewAdapter(this, list)
                adapter.setClickListener(this)
                recyclerView.setAdapter(adapter)
                recyclerView.addOnScrollListener(object : OnScrollListener() {
                    fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            if (layoutManager.findLastVisibleItemPosition() >= adapter.getItemCount() - 1) {
                                presenter.getPublicRepositoriesSince(since)
                            }
                        }
                    }
                })
            } else {
                adapter.addItems(list)
                adapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            showError("Has llegado al maximo de peticiones en esta IP. Prueba de nuevo en una hora.")
        }
    }

    fun showSearchResults(results: RepositorySearch) {
        try {
            val list: List<SearchItem> = results.getItems()
            if (searchRecyclerView == null) {
                searchRecyclerView = findViewById(R.id.recyclerViewSearch)
                searchRecyclerView.setLayoutManager(LinearLayoutManager(this))
                searchAdapter = SearchRecyclerViewAdapter(this, list)
                searchAdapter.setClickListener(this)
                searchRecyclerView.setAdapter(searchAdapter)
            }
            recyclerView.setVisibility(View.GONE)
            searchRecyclerView.setVisibility(View.VISIBLE)
            searchView!!.clearFocus()
        } catch (e: Exception) {
            //TODO mostrar error
        }
    }

    fun onItemClick(view: View?, position: Int) {
        try {
            if (searchRecyclerView == null || searchRecyclerView.getVisibility() === View.GONE) {
                val name: String = adapter.repositories.get(position).getFullName()
                val i = Intent(this, RepositoryViewActivity::class.java)
                i.putExtra("name", name)
                i.putExtra("tree", "master")
                startActivity(i)
            } else {
                val name: String = searchAdapter.repositories.get(position).getFullName()
                val i = Intent(this, RepositoryViewActivity::class.java)
                i.putExtra("name", name)
                i.putExtra("tree", "master")
                startActivity(i)
            }
        } catch (e: Exception) {
            //TODO mostrar error
        }
    }

    fun onBackPressed() {
        try {
            if (searchRecyclerView.getVisibility() === View.VISIBLE) {
                searchRecyclerView.setVisibility(View.GONE)
                recyclerView.setVisibility(View.VISIBLE)
                searchView!!.setQuery("", false)
                searchView!!.clearFocus()
            } else {
                super.onBackPressed()
            }
        } catch (e: Exception) {
            super.onBackPressed()
        }
    }
}

private fun SearchView.setOnQueryTextListener() {
    TODO("Not yet implemented")
}
