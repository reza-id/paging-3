package dev.cianjur.pagging3.api

import dev.cianjur.pagging3.data.UnsplashPhoto

data class UnsplashResponse(
    val total_pages: Int,
    val results: List<UnsplashPhoto>,
)
