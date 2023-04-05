package artem.tamilin.bootcounter.work

import android.Manifest
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import artem.tamilin.bootcounter.Application
import artem.tamilin.bootcounter.broadcastreceivers.NotificationDismissReceiver
import artem.tamilin.bootcounter.db.dao.BootDao
import artem.tamilin.bootcounter.db.entity.Boot
import org.koin.java.KoinJavaComponent.inject
import artem.tamilin.bootcounter.R


class BindNotificationWork(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_DELAY_DURATION_MINUTES = 15L
        const val TAG = "BootCounterWork"
    }

    private val bootDao: BootDao by inject(BootDao::class.java)

    override suspend fun doWork(): Result {
        val boots = bootDao.getAll()
        bindNotification(boots)
        bindNotificationDismissWork()
        return Result.success()
    }

    private fun bindNotificationDismissWork() {
        val data = workDataOf(NotificationDismissStatusWork.IS_DISMISSED_KEY to false)
        val dismissWork = OneTimeWorkRequestBuilder<NotificationDismissStatusWork>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(appContext).enqueue(dismissWork)
    }

    private fun bindNotification(boots: List<Boot>) {
        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            val bodyText = getNotificationText(boots)

            val notification = NotificationCompat.Builder(appContext, Application.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(appContext.getString(artem.tamilin.bootcounter.R.string.notification_title))
                .setContentText(bodyText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val deleteIntent = Intent(appContext, NotificationDismissReceiver::class.java).apply {
                action = NotificationDismissReceiver.NOTIFICATION_DISMISS_ACTION
            }
            val pendingDeleteIntent = PendingIntent.getBroadcast(appContext, 0, deleteIntent, FLAG_IMMUTABLE)

            notification.setDeleteIntent(pendingDeleteIntent)

            NotificationManagerCompat.from(appContext).notify(0, notification.build())
        }
    }

    private fun getNotificationText(boots: List<Boot>): String {
        return when (boots.size) {
            0 -> appContext.getString(R.string.notification_body_no_boots)
            1 -> appContext.getString(R.string.notification_body_one_boot, boots.first().timestamp)
            else -> {
                val delta = boots.last().timestamp - boots[boots.size - 2].timestamp
                appContext.getString(R.string.notification_more_than_one_boot, delta)
            }
        }
    }
}