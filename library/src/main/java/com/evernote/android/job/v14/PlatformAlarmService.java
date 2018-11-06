/*
 * Copyright (C) 2018 Evernote Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evernote.android.job.v14;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.JobProxy;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.JobCat;

/**
 * @author rwondratschek
 */
public final class PlatformAlarmService extends IntentService {

    private static final JobCat CAT = new JobCat("PlatformAlarmService");

    public PlatformAlarmService() {
        super("PlatformAlarmService");
    }

    public static void start(Context context, int jobId, @Nullable Bundle transientExtras) {
        Intent intent = new Intent(context, PlatformAlarmService.class);
        intent.putExtra(PlatformAlarmReceiver.EXTRA_JOB_ID, jobId);
        if (transientExtras != null) {
            intent.putExtra(PlatformAlarmReceiver.EXTRA_TRANSIENT_EXTRAS, transientExtras);
        }

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        runJob(intent, this, CAT);
    }

    /*package*/ static void runJob(@Nullable Intent intent, @NonNull Service service, @NonNull JobCat cat) {
        if (intent == null) {
            cat.i("Delivered intent is null");
            return;
        }

        int jobId = intent.getIntExtra(PlatformAlarmReceiver.EXTRA_JOB_ID, -1);
        Bundle transientExtras = intent.getBundleExtra(PlatformAlarmReceiver.EXTRA_TRANSIENT_EXTRAS);
        final JobProxy.Common common = new JobProxy.Common(service, cat, jobId);

        // create the JobManager. Seeing sometimes exceptions, that it wasn't created, yet.
        final JobRequest request = common.getPendingRequest(true, true);
        if (request != null) {
            common.executeJobRequest(request, transientExtras);
        }
    }
}
