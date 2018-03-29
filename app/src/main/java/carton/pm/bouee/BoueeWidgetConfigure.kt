package carton.pm.bouee

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class BoueeWidgetConfigure : Activity() {

  private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Set result to canceled if the user backs out of the configuration
    setResult(RESULT_CANCELED)

    // Set the layout for the configuration menu
    setContentView(R.layout.widget_configuration)

    // Find the widget id
    appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

    // If the widget id is invalid, just bail as the android docs say
    if (AppWidgetManager.INVALID_APPWIDGET_ID == appWidgetId) {
      finish()
    }

    // Ok
    findViewById<Button>(R.id.button_ok).setOnClickListener {
      val context = this

      // Get the spot id and the spot name from the configuration
      val spotId = (findViewById<EditText>(R.id.spot_id)).text.toString()
      val spotName = (findViewById<EditText>(R.id.spot_name)).text.toString()

      savePreferences(context, appWidgetId, PreferenceKeys.SPOT_ID, spotId)
      savePreferences(context, appWidgetId, PreferenceKeys.SPOT_NAME, spotName)

      // Update the widget
      val appWidgetManager = AppWidgetManager.getInstance(context)
      updateAppWidget(context, appWidgetManager, appWidgetId, spotId.toInt(), spotName)

      val resultValue = Intent()
      resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
      setResult(RESULT_OK, resultValue)

      finish()
    }

    // Cancel
    findViewById<Button>(R.id.button_cancel).setOnClickListener {
      val context = this

      val resultValue = Intent()
      resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
      setResult(RESULT_CANCELED, resultValue)
      finish()
    }

  }
}