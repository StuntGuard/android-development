package id.project.stuntguard.data.repository

import id.project.stuntguard.data.remote.api.ApiService

class DetailHistoryPagingSource(private val apiService: ApiService, private val authToken: String) {
    // TODO Implement Paging 3 -> Cause Detail History of specific child may have so much of their Analyze Result Data.
}