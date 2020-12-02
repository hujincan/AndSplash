package org.bubbble.andsplash.ui.preference

import android.os.Bundle
import androidx.preference.PreferenceDialogFragmentCompat

/**
 * @author Andrew
 * @date 2020/10/08 9:31
 */
class DialogPrefCompat : PreferenceDialogFragmentCompat() {

    lateinit var positiveResult: () -> Unit

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            positiveResult()
        }
    }

    companion object {

        fun newInstance(key: String): DialogPrefCompat {
            val fragment = DialogPrefCompat()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle
            return fragment
        }
    }
}