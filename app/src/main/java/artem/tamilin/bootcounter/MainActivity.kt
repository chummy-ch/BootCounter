package artem.tamilin.bootcounter

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import artem.tamilin.bootcounter.work.BindNotificationWork
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            requestNotificationPermission()
        }

        initBootWork()

        bindUI()
    }

    private fun bindUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.boots.collect { boots ->
                    val text = if (boots.isNotEmpty()) {
                        boots.joinToString(separator = "\n") { boot ->
                            "\u25CF ${boot.id} - ${boot.timestamp}"
                        }
                    } else {
                        getString(R.string.notification_body_no_boots)
                    }
                    findViewById<TextView>(R.id.boot_text_view).text = text
                }
            }
        }
    }

    private fun initBootWork() {
        val workRequest =
            PeriodicWorkRequestBuilder<BindNotificationWork>(BindNotificationWork.WORK_DELAY_DURATION_MINUTES, TimeUnit.MINUTES)
                .addTag(BindNotificationWork.TAG)
                .build()
        val workManager = WorkManager.getInstance(this)
        workManager.cancelAllWork()
        workManager.enqueue(workRequest)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }
        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }
}