package com.setiaki.githubusersubmission2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import java.util.*


class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var KEY_REPEATING_ALARM: String
    private lateinit var KEY_CHANGE_LANGUAGE: String

    private lateinit var repeatingAlarmPreference: SwitchPreferenceCompat
    private lateinit var changeLanguagePreference: Preference

    private lateinit var alarmReceiver: AlarmReceiver


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        alarmReceiver = AlarmReceiver()
        init()
    }

    private fun init() {
        KEY_REPEATING_ALARM = resources.getString(R.string.key_repeating_alarm)
        KEY_CHANGE_LANGUAGE = resources.getString(R.string.key_change_language)

        repeatingAlarmPreference =
            findPreference<SwitchPreferenceCompat>(KEY_REPEATING_ALARM) as SwitchPreferenceCompat
        changeLanguagePreference = findPreference<Preference>(KEY_CHANGE_LANGUAGE) as Preference

        changeLanguagePreference.setOnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            true
        }
        changeLanguagePreference.summary = getSupportedLanguageName()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == KEY_REPEATING_ALARM) {
            val state = sharedPreferences?.getBoolean(KEY_REPEATING_ALARM, true) as Boolean
            toggleRepeatingAlarm(state)
            repeatingAlarmPreference.isChecked = state
        }
    }

    private fun toggleRepeatingAlarm(state: Boolean) {
        if (state) {
            val title = context?.resources?.getString(R.string.notification_title)
            val message = context?.resources?.getString(R.string.notification_message)
            alarmReceiver.set9AMRepeatingAlarm(context, title, message)
        } else {
            alarmReceiver.cancel9AMRepeatingAlarm(context)
        }
    }

    private fun getSupportedLanguageName(): String {
        val localeDisplayLanguage = Locale.getDefault().displayLanguage
        val displayLanguageIndonesia = Locale("id", "ID").displayLanguage
        val displayLanguageEnglish = Locale("en", "US").displayLanguage

        if (localeDisplayLanguage != displayLanguageEnglish && localeDisplayLanguage != displayLanguageIndonesia) {
            return displayLanguageEnglish
        }
        return localeDisplayLanguage
    }
}