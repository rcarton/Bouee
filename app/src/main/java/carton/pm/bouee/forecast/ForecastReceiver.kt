package carton.pm.bouee.forecast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ForecastReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    Log.d(ForecastReceiver::class.java.toString(), "ForecastReceiver received intent")
    intent.setClass(context, ForecastIntentService::class.java)
    enqueueWork(context, intent)
  }
}