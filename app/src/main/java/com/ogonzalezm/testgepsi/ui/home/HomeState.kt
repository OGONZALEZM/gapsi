package com.ogonzalezm.testgepsi.ui.home

data class HomeState(
    val keyword: String = "",
    val sortBy: String = "best_match",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)