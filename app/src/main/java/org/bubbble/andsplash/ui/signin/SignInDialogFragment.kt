package org.bubbble.andsplash.ui.signin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.bubbble.andsplash.databinding.SignInDialogFragmentBinding
import org.bubbble.andsplash.util.signin.SignInHandler
import org.bubbble.andsplash.shared.result.EventObserver
import javax.inject.Inject

@AndroidEntryPoint
class SignInDialogFragment : AppCompatDialogFragment() {

    @Inject
    lateinit var signInHandler: SignInHandler

    private val signInViewModel: SignInDialogViewModel by viewModels()

    private lateinit var binding: SignInDialogFragmentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 我们想创建一个对话框，但是我们也想对内容视图使用DataBinding。
        // 我们可以通过创建一个空对话框并稍后添加内容来做到这一点。
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SignInDialogFragmentBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 监听了一个performSignInEvent，它的值是一个Event<?>，? 可能是RequestSignIn或RequestSignOut
        // 这取决于按钮点击 viewModel.onSignIn() 或者 其他的什么的。
        // 监听了点击，然后观察了一个凭空创造出来的 observer？
        signInViewModel.performSignInEvent.observe(viewLifecycleOwner, EventObserver { request ->
            if (request == SignInEvent.RequestSignIn) {
                activity?.let { activity ->
                    val signInIntent = signInHandler.makeSignInIntent(activity)

                    signInIntent.observeForever(object : Observer<Intent?> {
                        override fun onChanged(it: Intent?) {
                            activity.startActivityForResult(it, REQUEST_CODE_SIGN_IN)
                            signInIntent.removeObserver(this)
                        }
                    })
                }
                dismiss()
            }
        })

        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }
    }

    companion object {
        const val DIALOG_SIGN_IN = "dialog_sign_in"
        const val REQUEST_CODE_SIGN_IN = 42
    }
}