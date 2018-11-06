package com.evernote.android.job.demo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;

import java.util.Random;

/**
 * @author rwondratschek
 */
public class DemoSyncJob extends Job {

    public static final String TAG = "job_demo_tag";

    @Override
    @NonNull
    protected Result onRunJob(@NonNull final Params params) {
        boolean success = new DemoSyncEngine(getContext()).sync();

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(getContext())
                .setContentTitle("ID " + params.getId())
                .setContentText("Job ran, exact " + params.isExact() + " , periodic " + params.isPeriodic() + ", transient " + params.isTransient())
                .setAutoCancel(true)
                .setSound(null)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification)
                .setShowWhen(true)
                .setColor(Color.GREEN)
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(), notification);

        return success ? Result.SUCCESS : Result.FAILURE;
    }
}
