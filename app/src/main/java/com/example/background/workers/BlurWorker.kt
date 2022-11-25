package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import timber.log.Timber


/**
Author: Kemmy MO Jones
Project: android-work-manager-start_kotlin
Date: 2022/11/25
Email: mjkonceptz@gmail.com
Copyright (c) 2022 MJKonceptz. All rights reserved.
 */

//MARK: Create Class BlurWorker  And Change The Parameters.
class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {

        //SETTINGS: Add Some Override Settings To Your doWork function and implement the result
        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", appContext)

        //Note: Add this:
        sleep()

        return try {

            /**
             MARK: Clean up the codes here & add Timber dependency in
              build.gradle file to clear timber error
             */

            if (TextUtils.isEmpty(resourceUri)) {
                Timber.e("Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))

            val output = blurBitmap(picture, appContext)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)

            //MARK: Modify This :
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(outputData)

            Result.success(outputData)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.failure()
        }//end: return try_catch

    }//end: doWork


} //end: BlurWorker