package com.ogonzalezm.testgepsi.domain.model

data class DataResponse(
    val item: BaseItem?
)

data class BaseItem(
    val props: Props?
)

data class Props(
    val pageProps: PageProps?
)

data class PageProps(
    val initialData: InitialData?
)

data class InitialData(
    val searchResult: SearchResult?
)

data class SearchResult(
    val itemStacks: List<ItemStacks>?,
    val paginationV2: Pagination?,
    val count: Int?
)

data class ItemStacks(
    val items: List<Item>?
)

data class Pagination(
    val maxPage: Int?
)