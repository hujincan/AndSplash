package org.bubbble.andsplash.ui.signin

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.bubbble.andsplash.databinding.SignInDialogFragmentBinding
import org.bubbble.andsplash.shared.data.ConnectionURL
import org.bubbble.andsplash.util.signin.SignInHandler
import org.bubbble.andsplash.shared.result.EventObserver
import org.bubbble.andsplash.util.CustomTabUtil
import org.bubbble.andsplash.util.executeAfter
import javax.inject.Inject

@AndroidEntryPoint
class SignInDialogFragment : AppCompatDialogFragment() {

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

        // 只负责启动浏览器登录，然后dismiss()
        signInViewModel.performSignInEvent.observe(viewLifecycleOwner, EventObserver { request ->
            if (request == SignInEvent.RequestSignIn) {
                activity?.let { activity ->
                    CustomTabUtil.open(activity, ConnectionURL.LOGIN_URL)
                }
                dismiss()
            }
        })

        binding.executeAfter {
            viewModel = signInViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }
    }

    companion object {
        const val DIALOG_SIGN_IN = "dialog_sign_in"
        const val REQUEST_CODE_SIGN_IN = 42
    }
}