package com.example.kotlintest.repository

interface RepositoryPresenter {
    fun getRepository(name: String)
    fun getTree(name: String, tree: String)
}
