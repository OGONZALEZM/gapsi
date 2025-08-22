package com.ogonzalezm.testgepsi.domain.model

data class SearchParams(
    val keyword: String,
    val page: Int,
    val pageSize: Int,
    val sortBy: String
)