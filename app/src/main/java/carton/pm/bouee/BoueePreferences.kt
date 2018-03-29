package carton.pm.bouee

import android.content.Context
import android.util.Log

const val TAG = "BoueePreferences"

const val PREFS_NAME = "carton.pm.bouee.BoueeWidgetProvider"
const val PREF_PREFIX_KEY = "prefix"

enum class PreferenceKeys {
  SPOT_ID, SPOT_NAME
}

fun savePreferences(context: Context, appWidgetId: Int, key: PreferenceKeys, value: String) {
  val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
  prefs.putString(getKeyWithPrefix(appWidgetId, key), value)
  prefs.apply()
}

fun getPreferences(context: Context, appWidgetId: Int, key: PreferenceKeys): String {
  val prefs = context.getSharedPreferences(PREFS_NAME, 0)
  return prefs.getString(getKeyWithPrefix(appWidgetId, key), getDefault(context, key))

}

private fun getKeyWithPrefix(appWidgetId: Int, key: PreferenceKeys): String {
  return "${PREF_PREFIX_KEY}_${appWidgetId}_$key"
}

private fun getDefault(context: Context, key: PreferenceKeys): String {
  val resId = context.resources.getIdentifier("DEFAULT_$key", "string", context.packageName)

  if (0 == resId) {
    Log.e(TAG, "Default value not found for key=$key")
    return "NOT_FOUND"
  }
  return context.getString(resId)
}