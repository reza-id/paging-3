package dev.cianjur.pagging3.data

import dev.cianjur.pagging3.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {
}
