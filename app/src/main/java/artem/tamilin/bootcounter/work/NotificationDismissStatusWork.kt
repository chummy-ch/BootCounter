package artem.tamilin.bootcounter.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import artem.tamilin.bootcounter.db.NotificationDismissRepository
import org.koin.java.KoinJavaComponent.inject

class NotificationDismissStatusWork(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    companion object {
        const val IS_DISMISSED_KEY = "is_dismissed"
    }

    private val notificationDismissRepository: NotificationDismissRepository by inject(NotificationDismissRepository::class.java)

    override suspend fun doWork(): Result {
        val isDismissed = inputData.getBoolean(IS_DISMISSED_KEY, false)
        notificationDismissRepository.setNotificationDismissStatus(isDismissed)
        return Result.success()
    }
}