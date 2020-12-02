package org.bubbble.andsplash.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.bubbble.andsplash.R
import org.bubbble.andsplash.ui.preference.DialogClearRecentSearches
import org.bubbble.andsplash.ui.preference.DialogPrefCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onDisplayPreferenceDialog(preference: Preference?) {
            val clearRecentDialog = preference as? DialogClearRecentSearches
            if (clearRecentDialog != null) {
                val dialogFragment = DialogPrefCompat.newInstance(clearRecentDialog.key)
                dialogFragment.setTargetFragment(this, 0)
                dialogFragment.positiveResult = {
                    Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show()
                }
                dialogFragment.show(parentFragmentManager, null)
            } else {
                super.onDisplayPreferenceDialog(preference)
            }
        }
    }
}