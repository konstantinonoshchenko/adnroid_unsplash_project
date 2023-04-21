package com.example.unsplash.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.*
import com.example.unsplash.utils.Constants.KEY_WORK_INPUT
import com.example.unsplash.utils.Constants.KEY_WORK_RESULT
import com.example.unsplash.utils.Constants.WORK_TAG
import java.util.concurrent.TimeUnit


class MyWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        val result = work()
        return Result.success(result)
    }

    private fun work(): Data {
        val url = inputData.getString(KEY_WORK_INPUT)
        return workDataOf(KEY_WORK_RESULT to url)
    }

    companion object {
        fun createWorkRequest(urlRaw: String): OneTimeWorkRequest {
            val networkConstraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
            return OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(workDataOf(KEY_WORK_INPUT to urlRaw))
                .setInitialDelay(1, TimeUnit.SECONDS)
                .setConstraints(networkConstraint)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.SECONDS)
                .addTag(WORK_TAG)
                .build()
        }

    }
}