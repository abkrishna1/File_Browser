package com.wordpress.abkrishna.ftpoc;

import android.app.Activity;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
//import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by balakrishna on 14-Dec-16.
 */

public class PermissionsUtility {

    private static final String TAG = "PermissionsUtility";
    public static final int REQUEST_EXTERNAL_STORAGE = 2;

    public static void requestExternalStoragePermission(final Activity activity, View mLayout) {
        Log.i(TAG, "Storage permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying storage permission rationale to provide additional context.");
            Snackbar.make(mLayout, R.string.permission_externalStorage_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {

            // Storage permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        }
        // END_INCLUDE(camera_permission_request)
    }
}
