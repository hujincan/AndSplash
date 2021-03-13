package org.bubbble.andsplash.ui.editor

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import org.bubbble.andsplash.ui.signin.SignInViewModelDelegate

/**
 * @author Andrew
 * @date 2020/12/19 0:05
 */
class EditorViewModel  @ViewModelInject constructor(
    signInViewModelDelegate: SignInViewModelDelegate
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {


}