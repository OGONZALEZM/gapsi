package com.ogonzalezm.testgepsi.domain.model

data class Item(
    val id: String?,
    val name: String?,
    val price: String?,
    val image: String?,
    val imageInfo: ImageInfo?
)

data class ImageInfo(
    val thumbnailUrl: String?
)