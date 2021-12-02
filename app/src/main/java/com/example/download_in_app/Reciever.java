package com.example.download_in_app;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

public class Reciever extends BroadcastReceiver {
    DownloadManager manager;
    long size;
    IntentFilter filter;

    private final Context context;
    private final Activity activity;

    public Reciever(Activity activity) {
        this.context = activity;
        this.activity = activity;

        filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*try {
            String action = intent.getAction();

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(size);

                Cursor cursor = manager.query(query);

                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                        String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                        File toInstall = new File(Environment.getExternalStorageDirectory() + "apk", "marventaslogo" + ".apk");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri apkUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", toInstall);
                            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            intent.setData(apkUri);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            Uri apkUri = Uri.fromFile(toInstall);
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        activity.startActivity(intent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();

        // Get the download_id of the completed download.
        long download_id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        // Remove the completed download from the DownloadManager
        //manager.remove(download_id);
    }

    public void Download(String url){
        try{
        DownloadManager.Request request;

        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        request = new DownloadManager.Request(Uri.parse(url));

            request.setDescription("Downloading " + "marventaslogo.apk");
            request.setTitle("Toca para actualizar el cliente MAR");
            request.setDestinationUri(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url);
        String fileName = URLUtil.guessFileName(url, null, fileExtension);

        Log.d("File name: ", fileName);
        Log.d("Extension: ",fileExtension);

        File file = new File(Environment.getExternalStorageDirectory(), "apk");
        File apkFile = new File(Environment.getExternalStorageDirectory() + "/apk/"+fileName);
        if(!file.exists()){
            file.mkdirs();
        }
        if(apkFile.exists()){
         boolean deleted = apkFile.delete();
         Log.v("log_tag","deleted: " + deleted);
        }
            request.setDestinationInExternalPublicDir("/apk", fileName);

            //String requestPath = request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + "MAR" + File.separator + fileName).toString();

            String requestPath = request.setDestinationInExternalPublicDir("/apk", fileName).toString();

            Log.d("Path: ",requestPath);

            size = manager.enqueue(request);

            Log.d("Size file", ""+size);

            Toast.makeText(context, "Descargando...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(Reciever reciever){
        context.registerReceiver(reciever, filter);
    }

    public void wipeRegister(Reciever reciever){
        context.unregisterReceiver(reciever);
    }
}
