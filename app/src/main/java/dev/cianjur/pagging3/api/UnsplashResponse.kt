package dev.cianjur.pagging3.api

import dev.cianjur.pagging3.data.UnsplashPhoto

data class UnsplashResponse(
    val results: List<UnsplashPhoto>,
)
