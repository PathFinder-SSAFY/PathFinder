package com.dijkstra.pathfinder.util

import android.content.Context
import android.content.SharedPreferences
import com.dijkstra.pathfinder.R

class SharedPreferencesUtil(context: Context) {
    private var preference: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.shared_preferences_util_name), Context.MODE_PRIVATE
    )

    val userUuidText = context.getString(R.string.user_uuid)

    fun getUserUUID(): String {
        return preference.getString(userUuidText, "") ?: ""
    }

    fun setUserUUID(userUUID: String) {
        val editor = preference.edit()
        editor.putString(userUuidText, userUUID)
        editor.apply()
    }

} // End of SharedPreferencesUtil
