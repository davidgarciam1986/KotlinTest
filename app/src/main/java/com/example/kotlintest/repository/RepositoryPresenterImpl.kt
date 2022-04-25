package com.example.kotlintest.repository

import com.example.kotlintest.common.api.ApiInteractor
import com.example.kotlintest.common.model.RepositoryDetails
import com.example.kotlintest.common.model.RepositoryTree

class RepositoryPresenterImpl(private val repositoryView: RepositoryView) :
    RepositoryPresenter,
    ApiInteractor.onGetRepositoryFinishedListener,
    ApiInteractor.onGetTreeFinishedListener {

    private val repositoryInteractor: ApiInteractor

    override fun getRepository(name: String) {
        repositoryView.showProgressBar()
        repositoryInteractor.getRepository(name, this)
    }

    override fun getTree(name: String, tree: String) {
        repositoryView.showProgressBar()
        repositoryInteractor.getTree(name, tree, this)
    }

    override fun onGetRepositoryFail() {
            repositoryView.hideProgress()
            repositoryView.showError("Error")

    }

    override fun onGetRepositorySuccess(repository: RepositoryDetails) {
        repositoryView.showRepository(repository)
        repositoryView.hideProgress()

    }

    override fun onGetTreeSuccess(tree: RepositoryTree) {
        repositoryView.showTree(tree)
        repositoryView.hideProgress()

    }

    override fun onGetTreeFail() {
        repositoryView.hideProgress()
        repositoryView.showError("Error")
    }

    init {
        repositoryInteractor = ApiInteractor()
    }
}