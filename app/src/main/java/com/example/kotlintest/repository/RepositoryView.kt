package com.example.kotlintest.repository

import com.example.kotlintest.common.model.RepositoryDetails
import com.example.kotlintest.common.model.RepositoryTree

interface RepositoryView {
    fun showProgressBar()
    fun hideProgress()
    fun showError(error: String)
    fun showRepository(repository: RepositoryDetails)
    fun showTree(tree: RepositoryTree)
}
