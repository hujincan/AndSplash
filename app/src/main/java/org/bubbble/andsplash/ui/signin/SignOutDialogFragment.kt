package org.bubbble.andsplash.ui.signin

import android.app.Dialog
import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.bubbble.andsplash.databinding.FragmentSignOutDialogBinding
import org.bubbble.andsplash.shared.data.ConnectionURL
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.util.logger
import org.bubbble.andsplash.ui.MainActivityViewModel
import org.bubbble.andsplash.ui.editor.EditUserActivity
import org.bubbble.andsplash.ui.settings.SettingsActivity
import org.bubbble.andsplash.util.CustomTabUtil
import org.bubbble.andsplash.util.executeAfter


/**
 * A simple [Fragment] subclass.
 * Use the [SignOutDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SignOutDialogFragment : AppCompatDialogFragment() {

    private val signInViewModel: SignInDialogViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NotCurrentAccountAdapter {
            binding.userName.text = it.name
            binding.userEmail.text = it.email
        }

        binding.run {
            manageAccount.setOnClickListener {
                startActivity(Intent(context, EditUserActivity::class.java))
            }

            settings.setOnClickListener {
//                startActivity(Intent(context, SettingsActivity::class.java))

                logger("不应该是null了：${signInViewModel.currentUserInfo.value}")
            }

            addAccount.setOnClickListener {
                activity?.let { activity ->
                    CustomTabUtil.open(activity, ConnectionURL.LOGIN_URL)
                }
                dismiss()
            }
            signOut.setOnClickListener {
                activityViewModel.signOut()
            }

            switchAccount.setOnClickListener {
                adapter.currentUserEntity.numeric_id?.let { it1 -> adapter.currentUserEntity.access_token?.let { it2 ->
                    activityViewModel.switchUser(it1,
                        it2
                    )
                } }
            }

            removeAccount.setOnClickListener {
                adapter.currentUserEntity.numeric_id?.let {
                    activityViewModel.removeUser(it)
                }
            }

            signInViewModel.hasOtherAccount.observe(viewLifecycleOwner, {
                if (it) {
                    accountList.visibility = View.VISIBLE
                    accountGroup.visibility = View.VISIBLE
                } else {
                    accountList.visibility = View.GONE
                    accountGroup.visibility = View.GONE
                }
            })

            accountList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            accountList.adapter = adapter
        }

        signInViewModel.notCurrentAccounts.observe(this, {
            adapter.submitList(it)
        })

        signInViewModel.currentUserInfo.observe(this, {
            logger("有了")
            binding.username.text = it.name
            binding.email.text = it.email
        })
        logger("有吗：${signInViewModel.currentUserInfo.value}")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 监听点击退出登录
        signInViewModel.performSignInEvent.value?.getContentIfNotHandled()
        signInViewModel.performSignInEvent.observe(viewLifecycleOwner, { request ->
            if (request.peekContent() == SignInEvent.RequestSignOut) {
                if (request.getContentIfNotHandled() != null) {
                    dismiss()
                }
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
}

//@BindingAdapter("username")
//fun setUsername(textView: TextView, userInfo: UserEntity) {
//    val displayName = userInfo.name
//    textView.text = displayName
//    textView.isGone = displayName.isNullOrEmpty()
//}
//
//@BindingAdapter("userEmail")
//fun setUserEmail(textView: TextView, userInfo: UserEntity) {
//    val email = userInfo.email
//    textView.text = email
//    textView.isGone = email.isNullOrEmpty()
//}