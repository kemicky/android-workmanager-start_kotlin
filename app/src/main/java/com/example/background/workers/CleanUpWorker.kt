package com.example.background.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import java.io.File


/**
 Author: Kemmy MO Jones
Project: android-workmanager-start_kotlin
Date: 2022/11/25
Email: mjkonceptz@gmail.com
Copyright (c) 2022 MJKonceptz. All rights reserved.
 */
/** MARK: Create this class in workers package & modify it
 * **/
private const val TAG = "CleanupWorker"
class CleanUpWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    //MARK: Add Codes Here: Cleans up temporary files generated during blurring process

        override fun doWork(): Result {
            // Makes a notification when the work starts and slows down the work so that
            // it's easier to see each WorkRequest start, even on emulated devices
            makeStatusNotification("Cleaning up old temporary files", applicationContext)
            sleep()

            return try {
                val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
                if (outputDirectory.exists()) {
                    val entries = outputDirectory.listFiles()
                    if (entries != null) {
                        for (entry in entries) {
                            val name = entry.name
                            if (name.isNotEmpty() && name.endsWith(".png")) {
                                val deleted = entry.delete()
                                Log.i(TAG, "Deleted $name - $deleted")
                            }
                        }
                    }
                }
                Result.success()
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.failure()

            } //end: return try_catch

        } //end: doWork()


} //end: CleanUpWorker class