package com.example.kotlintest.dashboard

interface DashboardPresenter {
    fun getPublicRepositoriesSince(since: Int)
    fun searchPublicRepositories(query: String)
}