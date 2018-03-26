package carton.pm.bouee.forecast

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log


class ForecastIntentService: IntentService("ForecastIntentService") {
  override fun onHandleIntent(intent: Intent) {
    val bundle = intent.extras
    if (bundle != null) {
      val messenger = bundle.get("messenger") as Messenger
      val msg = Message.obtain()
      val bundle = Bundle()
      bundle.putString("forecast", "hello")
      msg.data = bundle

      try {
        messenger.send(msg)
      } catch (e: RemoteException) {
        Log.e(ForecastIntentService::class.toString(), "error")
      }

    }
    Log.d(ForecastIntentService()::class.toString(), "IntentService onHandleIntent")
  }
}