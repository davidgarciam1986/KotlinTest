package com.example.kotlintest.dashboard

import com.example.kotlintest.common.model.Repository
import com.example.kotlintest.common.model.RepositorySearch

interface DashboardView {
    fun showProgressBar()
    fun hideProgress()
    fun showError(error: String)
    fun updateRepositoryList(list: MutableList<Repository>)
    fun showSearchResults(list: RepositorySearch)
}
