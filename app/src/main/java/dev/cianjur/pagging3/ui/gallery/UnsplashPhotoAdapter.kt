package dev.cianjur.pagging3.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.cianjur.pagging3.R
import dev.cianjur.pagging3.data.PhotoDb
import dev.cianjur.pagging3.data.UnsplashPhoto
import dev.cianjur.pagging3.databinding.ItemUnsplashPhotoBinding

class UnsplashPhotoAdapter(private val clickListener: (photo: PhotoDb) -> Unit) :
    PagingDataAdapter<PhotoDb, UnsplashPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PhotoViewHolder(private val binding: ItemUnsplashPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { clickListener(it) }
                }
            }
        }

        fun bind(photo: PhotoDb) {
            binding.apply {
                Glide.with(itemView)
                    .load(photo.regularImageUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                tvUsername.text = photo.username
            }
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<PhotoDb>() {
            override fun areItemsTheSame(oldItem: PhotoDb, newItem: PhotoDb) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: PhotoDb, newItem: PhotoDb) = oldItem == newItem
        }
    }
}
