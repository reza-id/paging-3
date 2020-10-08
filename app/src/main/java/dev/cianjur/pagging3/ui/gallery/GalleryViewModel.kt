package dev.cianjur.pagging3.ui.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dev.cianjur.pagging3.data.UnsplashRepository

class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository
) : ViewModel()
