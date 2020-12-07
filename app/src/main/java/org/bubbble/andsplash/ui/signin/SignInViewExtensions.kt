package org.bubbble.andsplash.ui.signin

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import org.bubbble.andsplash.R
import org.bubbble.andsplash.shared.data.signin.AuthenticatedUserInfo
import org.bubbble.andsplash.ui.MainActivityViewModel
import org.bubbble.andsplash.util.asGlideTarget

/**
 * @author Andrew
 * @date 2020/10/21 19:56
 */
fun Toolbar.setupProfileMenuItem(
    viewModel: MainActivityViewModel,
    lifecycleOwner: LifecycleOwner
) {
    inflateMenu(R.menu.profile)
    val profileItem = menu.findItem(R.id.action_profile) ?: return
    profileItem.setOnMenuItemClickListener {
        viewModel.onProfileClicked()
        true
    }
//    viewModel.currentUserInfo.observe(lifecycleOwner, {
//        setProfileContentDescription(profileItem, resources, it)
//    })

    val avatarSize = resources.getDimensionPixelSize(R.dimen.nav_account_image_size)
    val target = profileItem.asGlideTarget(avatarSize)
//    viewModel.currentUserImageUri.observe(lifecycleOwner, {
//        setProfileAvatar(context, target, it)
//    })
}

fun setProfileContentDescription(item: MenuItem, res: Resources, user: AuthenticatedUserInfo?) {
    val description = if (user?.isSignedIn() == true) {
        res.getString(R.string.a11y_signed_in_content_description, user.getDisplayName())
    } else {
        res.getString(R.string.a11y_signed_out_content_description)
    }
    MenuItemCompat.setContentDescription(item, description)
}

fun setProfileAvatar(
    context: Context,
    target: Target<Drawable>,
    imageUri: Uri?,
    placeholder: Int = R.drawable.ic_default_profile_avatar
) {
    // Inflate the drawable for proper tinting
    val placeholderDrawable = AppCompatResources.getDrawable(context, placeholder)
    when (imageUri) {
        null -> {
            Glide.with(context)
                .load(placeholderDrawable)
                .apply(RequestOptions.circleCropTransform())
                .into(target)
        }
        else -> {
            Glide.with(context)
                .load(imageUri)
                .apply(RequestOptions.placeholderOf(placeholderDrawable).circleCrop())
                .into(target)
        }
    }
}
