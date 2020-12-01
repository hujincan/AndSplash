package org.bubbble.andsplash.ui.pictures

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ItemPhotoBinding
import org.bubbble.andsplash.databinding.ItemPhotoGridBinding
import org.bubbble.andsplash.util.load

/**
 * @author Andrew
 * @date 2020/11/30 16:08
 */
internal class PictureAdapter(private val onClick: ((Int) -> Unit)?) : ListAdapter<PictureItem, ViewHolder>(PictureDiffCallback) {

    private val viewLayout = ViewType.LIST

    enum class ViewType {
        GRID,
        LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_photo -> {
                ViewHolder.ListViewHolder(ItemPhotoBinding.inflate(layoutInflater, parent, false))
            }

            R.layout.item_photo_grid -> {
                ViewHolder.GridViewHolder(ItemPhotoGridBinding.inflate(layoutInflater, parent, false))
            }

            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ViewHolder.ListViewHolder) {
            holder.onBind(getItem(position), onClick)
        }

        if (holder is ViewHolder.GridViewHolder) {
            holder.onBind(getItem(position), onClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (viewLayout) {
            ViewType.GRID -> R.layout.item_photo_grid
            ViewType.LIST -> R.layout.item_photo
        }
    }

    override fun submitList(list: List<PictureItem>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}

internal sealed class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    class ListViewHolder(private val binding: ItemPhotoBinding) : ViewHolder(binding.root) {
        fun onBind(data: PictureItem, onClick: ((Int) -> Unit)?) {
            binding.photosImage.load(data.data)
            binding.photosImage.setOnClickListener {
                onClick?.invoke(data.data)
            }
        }
    }

    class GridViewHolder(private val binding: ItemPhotoGridBinding) : ViewHolder(binding.root)  {
        fun onBind(data: PictureItem, onClick: ((Int) -> Unit)?) {
            binding.photosImage.load(data.data)
            binding.photosImage.setOnClickListener {
                onClick?.invoke(data.data)
            }
        }
    }
}

object PictureDiffCallback : DiffUtil.ItemCallback<PictureItem>() {
    override fun areItemsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
        return oldItem.data == newItem.data
    }

    override fun areContentsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
        return oldItem.data == newItem.data
    }
}