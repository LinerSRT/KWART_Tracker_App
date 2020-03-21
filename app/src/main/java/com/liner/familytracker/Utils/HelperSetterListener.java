package com.liner.familytracker.Utils;

import com.google.android.gms.tasks.Task;

public interface HelperSetterListener {
    void onSuccess(Task<Void> task);
    void onFailed();
}
