package artem.tamilin.bootcounter.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import artem.tamilin.bootcounter.work.NotificationDismissStatusWork

class NotificationDismissReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_DISMISS_ACTION = "notification_dismiss"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            if (intent?.action == NOTIFICATION_DISMISS_ACTION) {
                handleDismissEvent(context)
            }
        }
    }

    private fun handleDismissEvent(context: Context) {
        val data = workDataOf(NotificationDismissStatusWork.IS_DISMISSED_KEY to true)
        val dismissWork = OneTimeWorkRequestBuilder<NotificationDismissStatusWork>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(context).enqueue(dismissWork)
    }
}