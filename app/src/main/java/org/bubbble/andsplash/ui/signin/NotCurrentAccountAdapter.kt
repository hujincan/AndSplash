package org.bubbble.andsplash.ui.signin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ItemUserAccountBinding
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.util.dp
import org.bubbble.andsplash.util.load

/**
 * @author Andrew
 * @date 2020/12/11 11:14
 */
class NotCurrentAccountAdapter(
    private val currentItem: (UserEntity) -> Unit
) : ListAdapter<UserEntity, NotCurrentAccountAdapter.ViewHolder>(NotCurrentDiffCallback()) {

    private var currentPosition = 0

    class ViewHolder(private val binding: ItemUserAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: UserEntity, currentPosition: Int, onClick: (UserEntity, Int) -> Unit) {
            binding.authorIcon.setStrokeWidth(if (adapterPosition == currentPosition) {
                2F.dp
            } else {
                0F
            })

            data.profile_image?.let {
                binding.authorIcon.load(it)
            } ?: run {
                binding.authorIcon.load(R.drawable.ic_default_profile_avatar)
            }

            binding.authorIcon.setOnClickListener {
                onClick(data, adapterPosition)
            }
        }
    }

    class NotCurrentDiffCallback : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUserAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), currentPosition, ::onClick)
    }

    private fun onClick(userEntity: UserEntity, position: Int) {
        currentItem(userEntity)
        currentPosition = position
        notifyDataSetChanged()
    }

    override fun submitList(list: List<UserEntity>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}