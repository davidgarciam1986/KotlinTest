package com.example.kotlintest.dashboard

import com.example.kotlintest.common.api.ApiInteractor
import com.example.kotlintest.common.model.Repository
import com.example.kotlintest.common.model.RepositorySearch

class DashboardPresenterImpl(private val dashboardView: DashboardView): DashboardPresenter,
     ApiInteractor.onGetPublicRepositoriesSinceFinishedListener,
     ApiInteractor.onSearchPublicRepositoriesFinishedListener
   {

    private val apiInteractor: ApiInteractor

    override fun getPublicRepositoriesSince(since: Int) {
        dashboardView.showProgressBar()
        apiInteractor.getPublicRepositoriesSince(since,this)

    }

    override fun searchPublicRepositories(query: String) {
        dashboardView.showProgressBar()
        apiInteractor.searchPublicRepositories(query, this)
    }

    override fun onGetPublicRepositoriesSinceFail() {
        dashboardView.hideProgress()
        dashboardView.showError("Error")
    }

    override fun onGetPublicRepositoriesSinceSuccess(list: MutableList<Repository>) {
        dashboardView.updateRepositoryList(list)
        dashboardView.hideProgress()
    }

    override fun onSearchPublicRepositoriesFail() {
        dashboardView.hideProgress()
        dashboardView.showError("Error")
    }

    override fun onSearchPublicRepositoriesSuccess(list: RepositorySearch) {
        dashboardView.showSearchResults(list)
        dashboardView.hideProgress()
    }

    init {
        apiInteractor = ApiInteractor()
    }
}
