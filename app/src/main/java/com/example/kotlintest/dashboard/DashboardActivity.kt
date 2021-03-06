package com.example.kotlintest.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.common.model.Repository
import com.example.kotlintest.common.model.RepositorySearch
import com.example.kotlintest.common.model.SearchItem
import com.example.kotlintest.common.recycler.RecyclerViewAdapter
import com.example.kotlintest.common.recycler.SearchRecyclerViewAdapter
import com.example.kotlintest.databinding.ActivityDashboardBinding
import com.example.kotlintest.repository.RepositoryActivity
import kotlin.random.Random


class DashboardActivity: AppCompatActivity(),
    DashboardView,
    RecyclerViewAdapter.ItemClickListener,
    SearchRecyclerViewAdapter.ItemClickListener
{
    var lastAccess: Int = 0
    lateinit var presenter: DashboardPresenterImpl
    private lateinit var activityDashboardBinding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(activityDashboardBinding.root)

        //Mostrar barra de progreso y cargar datos
        val progressbar: FrameLayout = activityDashboardBinding.progressBarOverlay
        progressbar.visibility = View.GONE
        val searchView: SearchView = activityDashboardBinding.searchView
        val searchRecyclerView: RecyclerView = activityDashboardBinding.recyclerViewSearch
        val recyclerView: RecyclerView = activityDashboardBinding.recyclerView

        presenter = DashboardPresenterImpl(this)


        //Configurar busqueda
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                presenter.searchPublicRepositories(p0)
                return false
            }

            override fun onQueryTextChange(p0: String): Boolean {
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

        val imageButton: ImageButton = activityDashboardBinding.imageButton2
        imageButton.setOnClickListener { searchView.visibility = View.VISIBLE }

        //Busca un repositorio aleatorio
        lastAccess = Random.nextInt(200000000)
        presenter.getPublicRepositoriesSince(lastAccess)

    }



    override fun showProgressBar() {
        activityDashboardBinding.progressBarOverlay.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        activityDashboardBinding.progressBarOverlay.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show()
    }

    override fun updateRepositoryList(list: MutableList<Repository>) {
        //El servicio tiene un limite de llamadas
        try {
            lastAccess = list[list.size - 1].getId()

            val recyclerView: RecyclerView = activityDashboardBinding.recyclerView
            if (recyclerView.layoutManager == null) {
                val layoutManager = LinearLayoutManager(this)
                val adapter = RecyclerViewAdapter(this, list)
                recyclerView.setLayoutManager(layoutManager)
                adapter.setClickListener(this)
                recyclerView.setAdapter(adapter)

                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            if (layoutManager.findLastVisibleItemPosition() >= adapter.getItemCount() - 1) {
                                presenter.getPublicRepositoriesSince(lastAccess)
                            }
                        }
                    }
                })
            } else {
                val adapter = recyclerView.adapter as RecyclerViewAdapter
                adapter.addItems(list)
                adapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            showError("Has llegado al maximo de peticiones en esta IP. Prueba de nuevo en una hora.")
        }
    }

    override fun showSearchResults(results: RepositorySearch) {
        try {
            val resultsList: List<SearchItem> = results.getItems()
            val searchRecyclerView: RecyclerView = activityDashboardBinding.recyclerViewSearch

            if (searchRecyclerView.layoutManager == null) {
                searchRecyclerView.setLayoutManager(LinearLayoutManager(this))

            }

            val searchAdapter = SearchRecyclerViewAdapter(this, resultsList)
            searchAdapter.setClickListener(this)
            searchRecyclerView.setAdapter(searchAdapter)

            activityDashboardBinding.recyclerView.setVisibility(View.GONE)
            searchRecyclerView.setVisibility(View.VISIBLE)
            activityDashboardBinding.searchView.clearFocus()
        } catch (e: Exception) {
            showError("")
        }
    }


    override fun onItemClick(view: View, position: Int) {

        val searchRecyclerView: RecyclerView = activityDashboardBinding.recyclerViewSearch
        val recyclerView: RecyclerView = activityDashboardBinding.recyclerView
        val thisLayoutManager = LinearLayoutManager(this)
        val searchAdapter: SearchRecyclerViewAdapter? = searchRecyclerView.adapter as SearchRecyclerViewAdapter?
        val adapter: RecyclerViewAdapter = recyclerView.adapter as RecyclerViewAdapter

        try {
            // Diferenciar entre el recycler de busqueda y el normal
            if (searchRecyclerView.layoutManager != thisLayoutManager || searchRecyclerView.getVisibility() == View.GONE) {
                val name = adapter.repositories.get(position).fullName
                val intent = Intent(this, RepositoryActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("tree", "master")
                startActivity(intent)
            } else if (searchAdapter != null) {
                val name = searchAdapter.repositories.get(position).fullName
                val intent = Intent(this, RepositoryActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("tree", "master")
                startActivity(intent)
            }
        } catch (e: Exception) {
            showError("")
        }
    }

    override fun onBackPressed() {
        val searchRecyclerView: RecyclerView = activityDashboardBinding.recyclerViewSearch
        val searchView: SearchView = activityDashboardBinding.searchView
        val recyclerView: RecyclerView = activityDashboardBinding.recyclerView

        try {
            if (searchRecyclerView.getVisibility() == View.VISIBLE) {
                searchRecyclerView.setVisibility(View.GONE)
                recyclerView.visibility = View.VISIBLE
                searchView.setQuery("", false)
                searchView.clearFocus()
            } else {
                super.onBackPressed()
            }
        } catch (e: Exception) {
            super.onBackPressed()
        }
    }

}
