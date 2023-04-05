package artem.tamilin.bootcounter.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import artem.tamilin.bootcounter.db.dao.BootDao
import artem.tamilin.bootcounter.db.entity.Boot
import org.koin.java.KoinJavaComponent.inject

class IncreaseBootWork(
    applicationContext: Context,
    params: WorkerParameters
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val TIMESTAMP_DATA_KEY = "timestamp"
    }

    private val bootDao: BootDao by inject(BootDao::class.java)

    override suspend fun doWork(): Result {
        val time = inputData.getLong(TIMESTAMP_DATA_KEY, 0L)
        val boot = Boot(timestamp = time)
        bootDao.insertBoot(boot)
        return Result.success()
    }
}