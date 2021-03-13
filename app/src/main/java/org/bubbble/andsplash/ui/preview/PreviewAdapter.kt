package org.bubbble.andsplash.ui.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ItemPhotoViewBinding
import org.bubbble.andsplash.databinding.ItemThumbnailBinding
import org.bubbble.andsplash.ui.pictures.PictureItem
import org.bubbble.andsplash.util.load

/**
 * @author Andrew
 * @date 2021/01/18 12:24
 */
class PreviewAdapter(val viewType: PreviewViewType) : ListAdapter<PictureItem, PreviewAdapter.ViewHolder>(PreviewDiffCallback) {

    enum class PreviewViewType {
        PHOTO,
        THUMBNAIL
    }

    sealed class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        class PhotoViewHolder(private val binding: ItemPhotoViewBinding) : ViewHolder(binding.root) {
            fun bind(data: PictureItem) {
                binding.photoView.load(data.data)
            }
        }


        class ThumbnailViewHolder(private val binding: ItemThumbnailBinding) : ViewHolder(binding.root) {
            fun bind(data: PictureItem) {
                binding.imageView.load(data.data)
            }
        }
    }

    object PreviewDiffCallback : DiffUtil.ItemCallback<PictureItem>() {
        override fun areItemsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            R.layout.item_photo_view -> ViewHolder.PhotoViewHolder(ItemPhotoViewBinding.inflate(layoutInflater, parent, false))

            R.layout.item_thumbnail ->  ViewHolder.ThumbnailViewHolder(ItemThumbnailBinding.inflate(layoutInflater, parent, false))

            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (holder is ViewHolder.PhotoViewHolder) {
            holder.bind(getItem(position))
        }

        if (holder is ViewHolder.ThumbnailViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            PreviewViewType.PHOTO -> R.layout.item_photo_view
            PreviewViewType.THUMBNAIL -> R.layout.item_thumbnail
        }
    }
}