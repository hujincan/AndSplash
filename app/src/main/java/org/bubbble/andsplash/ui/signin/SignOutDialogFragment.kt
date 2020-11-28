package org.bubbble.andsplash.ui.signin

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.bubbble.andsplash.databinding.FragmentSignOutDialogBinding
import org.bubbble.andsplash.shared.data.signin.AuthenticatedUserInfo
import org.bubbble.andsplash.ui.MainActivityViewModel
import org.bubbble.andsplash.util.signin.SignInHandler
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SignOutDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SignOutDialogFragment : AppCompatDialogFragment() {

    @Inject
    lateinit var signInHandler: SignInHandler

    private val signInViewModel: SignInDialogViewModel by viewModels()
    private val viewModel: MainActivityViewModel by activityViewModels()

    private lateinit var binding: FragmentSignOutDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // We want to create a dialog, but we also want to use DataBinding for the content view.
        // We can do that by making an empty dialog and adding the content later.
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignOutDialogBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        signInViewModel.performSignInEvent.observe(viewLifecycleOwner, { request ->
            if (request.peekContent() == SignInEvent.RequestSignOut) {
                request.getContentIfNotHandled()

                dismiss()
            }
        })

        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }
    }
}

@BindingAdapter("username")
fun setUsername(textView: TextView, userInfo: AuthenticatedUserInfo) {
    val displayName = userInfo.getDisplayName()
    textView.text = displayName
    textView.isGone = displayName.isNullOrEmpty()
}

@BindingAdapter("userEmail")
fun setUserEmail(textView: TextView, userInfo: AuthenticatedUserInfo) {
    val email = userInfo.getEmail()
    textView.text = email
    textView.isGone = email.isNullOrEmpty()
}