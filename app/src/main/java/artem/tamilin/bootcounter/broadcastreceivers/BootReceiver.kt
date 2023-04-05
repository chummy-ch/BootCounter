package artem.tamilin.bootcounter.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import artem.tamilin.bootcounter.work.BootCompleteNotificationWork
import artem.tamilin.bootcounter.work.IncreaseBootWork

class BootReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
                handleBootEven(context)
            }
        }
    }

    private fun handleBootEven(context: Context) {
        val data = workDataOf(IncreaseBootWork.TIMESTAMP_DATA_KEY to System.currentTimeMillis())
        val bootRequest = OneTimeWorkRequestBuilder<IncreaseBootWork>()
            .setInputData(data)
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWork()
        workManager.enqueue(bootRequest)

        val bootCompletedNotificationRequest = OneTimeWorkRequestBuilder<BootCompleteNotificationWork>().build()
        workManager.enqueue(bootCompletedNotificationRequest)
    }
}