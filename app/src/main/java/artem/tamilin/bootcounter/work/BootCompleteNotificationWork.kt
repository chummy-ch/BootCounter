package artem.tamilin.bootcounter.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import artem.tamilin.bootcounter.db.NotificationDismissRepository
import org.koin.java.KoinJavaComponent.inject
import java.util.concurrent.TimeUnit

class BootCompleteNotificationWork(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val notificationDismissRepository: NotificationDismissRepository by inject(NotificationDismissRepository::class.java)

    override suspend fun doWork(): Result {
        if (!notificationDismissRepository.isNotificationDismissed()) {
            val workRequest = PeriodicWorkRequestBuilder<BindNotificationWork>(
                BindNotificationWork.WORK_DELAY_DURATION_MINUTES,
                TimeUnit.MINUTES
            )
                .addTag(BindNotificationWork.TAG)
                .build()
            WorkManager.getInstance(applicationContext).enqueue(workRequest)
        }
        return Result.success()
    }
}