package com.example.administrator.appupdatedemo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.text.format.Formatter;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <pre>
 *     author : Leero
 *     e-mail : 925230519@qq.com
 *     time  : 2018-01-12
 *     desc  :
 *     version: 1.0
 * </pre>
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private ProgressDialog mProgressDialog;
    private int fileLength; // 文件大小
    private long total; // 当前已下载量

    public DownloadTask(Context context, ProgressDialog dialog) {
        this.context = context;
        this.mProgressDialog = dialog;
    }
    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        FileOutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "链接失败，请稍后重试";
            }

            // 文件大小
            fileLength = connection.getContentLength();

            // 下载文件
            input = connection.getInputStream();

            File file = new File("/sdcard/updateAPK/");
            // 目录不存在时创建目录
            if (!file.exists()) {
                file.mkdir();
            }

            File apkFile = new File("/sdcard/download/test.apk");
            output = new FileOutputStream(apkFile);

            byte buf[] = new byte[1024];
            total = 0;
            int count;
            while ((count = input.read(buf)) != -1) {
                // 返回取消
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // 更新进度
                if (fileLength > 0) {
                    publishProgress((int) (total * 100 / fileLength));
                }
                output.write(buf, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // 当用户在下载过程中按下电源键，锁定CPU
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.acquire();
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        mProgressDialog.setProgress(progress[0], formatSize(String.valueOf(fileLength)), formatSize(String.valueOf(total)));
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (result != null)
            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
    }

    private String formatSize(String target_size) {
        return Formatter.formatFileSize(context, Long.valueOf(target_size));
    }
}
