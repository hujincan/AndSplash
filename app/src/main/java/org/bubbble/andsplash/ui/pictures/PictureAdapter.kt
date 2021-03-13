package org.bubbble.andsplash.ui.pictures

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.*
import org.bubbble.andsplash.util.load

/**
 * @author Andrew
 * @date 2020/11/30 16:08
 */
internal class PictureAdapter(private val onClick: ((personal: String?, picture: Int?) -> Unit)?) : ListAdapter<PictureItem, ViewHolder>(PictureDiffCallback) {

    private var viewLayout = ViewType.PHOTO

    fun setViewType(viewType: ViewType) {
        viewLayout = viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_photo -> {
                ViewHolder.PhotoViewHolder(ItemPhotoBinding.inflate(layoutInflater, parent, false))
            }

            R.layout.item_photo_list -> {
                ViewHolder.ListViewHolder(ItemPhotoListBinding.inflate(layoutInflater, parent, false))
            }

            R.layout.item_photo_flow -> {
                ViewHolder.FlowViewHolder(ItemPhotoFlowBinding.inflate(layoutInflater, parent, false))
            }

            R.layout.item_photo_flow2 -> {
                ViewHolder.Flow2ViewHolder(ItemPhotoFlow2Binding.inflate(layoutInflater, parent, false))
            }

            R.layout.item_photo_grid -> {
                ViewHolder.GridViewHolder(ItemPhotoGridBinding.inflate(layoutInflater, parent, false))
            }

            R.layout.item_photo_gallery -> {
                ViewHolder.GalleryViewHolder(ItemPhotoGalleryBinding.inflate(layoutInflater, parent, false))
            }

            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (holder is ViewHolder.PhotoViewHolder) {
            holder.onBind(getItem(position), onClick)
        }

        if (holder is ViewHolder.ListViewHolder) {
            holder.onBind(getItem(position), onClick)
        }

        if (holder is ViewHolder.GridViewHolder) {
            holder.onBind(getItem(position), onClick)
        }

        if (holder is ViewHolder.FlowViewHolder) {
            holder.onBind(getItem(position), onClick)
        }

        if (holder is ViewHolder.Flow2ViewHolder) {
            holder.onBind(getItem(position), onClick)
        }


        if (holder is ViewHolder.GalleryViewHolder) {
            holder.onBind(getItem(position), onClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewLayout) {
            ViewType.GRID -> R.layout.item_photo_grid
            ViewType.FLOW -> R.layout.item_photo_flow
            ViewType.FLOW2 -> R.layout.item_photo_flow2
            ViewType.LIST -> R.layout.item_photo_list
            ViewType.PHOTO -> R.layout.item_photo
            ViewType.GALLERY -> R.layout.item_photo_gallery
        }
    }

    override fun submitList(list: List<PictureItem>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}

internal sealed class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    class PhotoViewHolder(private val binding: ItemPhotoBinding) : ViewHolder(binding.root) {
        fun onBind(data: PictureItem, onClick: ((String?, Int?) -> Unit)?) {
            binding.photosImage.load(data.data)
            binding.photosImage.setOnClickListener {
                onClick?.invoke(null, data.data)
            }

            binding.authorIcon.load(R.drawable.bush)
            binding.authorIcon.setOnClickListener {
                onClick?.invoke("Ok", null)
            }

            binding.authorName.text = "Andrew"
        }
    }

    class ListViewHolder(private val binding: ItemPhotoListBinding) : ViewHolder(binding.root) {
        fun onBind(data: PictureItem, onClick: ((String?, Int?) -> Unit)?) {
            binding.photosImage.load(data.data)
            binding.photosImage.setOnClickListener {
                onClick?.invoke(null, data.data)
            }
            binding.authorIcon.setOnClickListener {
                onClick?.invoke("Ok", null)
            }
        }
    }

    class FlowViewHolder(private val binding: ItemPhotoFlowBinding) : ViewHolder(binding.root)  {
        fun onBind(data: PictureItem, onClick: ((String?, Int?) -> Unit)?) {
            binding.photosImage.load(data.data)
            binding.photosImage.setOnClickListener {
                onClick?.invoke(null, data.data)
            }
        }
    }

    class Flow2ViewHolder(private val binding: ItemPhotoFlow2Binding) : ViewHolder(binding.root) {
        fun onBind(data: PictureItem, onClick: ((String?, Int?) -> Unit)?) {
            binding.photosImage.load(data.data)
            binding.photosImage.setOnClickListener {
                onClick?.invoke(null, data.data)
            }

            binding.authorIcon.load(R.drawable.bush)
            binding.authorIcon.setOnClickListener {
                onClick?.invoke("Ok", null)
            }

            binding.authorName.text = "Andrew"
        }
    }

    class GridViewHolder(private val binding: ItemPhotoGridBinding) : ViewHolder(binding.root)  {
        fun onBind(data: PictureItem, onClick: ((String?, Int?) -> Unit)?) {
            binding.photosImage.load(data.data)
            binding.photosImage.setOnClickListener {
                onClick?.invoke(null, data.data)
            }
        }
    }

    class GalleryViewHolder(private val binding: ItemPhotoGalleryBinding) : ViewHolder(binding.root)  {
        fun onBind(data: PictureItem, onClick: ((String?, Int?) -> Unit)?) {
            binding.photosImage.load(data.data)
            binding.photosImage.setOnClickListener {
                onClick?.invoke(null, data.data)
            }
        }
    }
}

object PictureDiffCallback : DiffUtil.ItemCallback<PictureItem>() {
    override fun areItemsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
        return oldItem.data != newItem.data
    }

    override fun areContentsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
        return oldItem.data != newItem.data
    }
}