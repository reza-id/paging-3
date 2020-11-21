package dev.cianjur.pagging3.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "photos")
data class PhotoDb(
    @PrimaryKey(autoGenerate = false) val id: String,
    val description: String?,
    val fullImageUrl: String,
    val regularImageUrl: String,
    val name: String,
    val username: String,
    val searchQuery: String? = null,
) : Parcelable {
    val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageSearchApp&utm_medium=referral"
}
