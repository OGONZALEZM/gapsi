package com.ogonzalezm.testgepsi.ui.home

sealed interface HomeIntent {
    data class EnterKeyword(val keyword: String) : HomeIntent
    data class ChangeSort(val sortBy: String) : HomeIntent
    data object Search : HomeIntent
}