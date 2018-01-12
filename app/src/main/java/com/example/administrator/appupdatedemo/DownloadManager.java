package com.example.administrator.appupdatedemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * <pre>
 *     author : Leero
 *     e-mail : 925230519@qq.com
 *     time  : 2018-01-10
 *     desc  :
 *     version: 1.0
 * </pre>
 */
public class DownloadManager {
    private Context mContext; //上下文

    private String apkUrl; //apk下载地址
    private static final String savePath = "/sdcard/download/"; //apk保存到SD卡的路径
    private static final String saveFileName = savePath + "test.apk"; //完整路径名

    private static final int DOWNLOADING = 1; // 下载中
    private static final int DOWNLOADED = 2; //下载完毕
    private static final int DOWNLOAD_FAILED = 3; //下载失败
    private boolean cancelFlag = false; //取消下载标志位

    private SweetAlertDialog dialog;

    /**
     * 构造函数
     */
    public DownloadManager(Context context, String url) {
        this.mContext = context;
        this.apkUrl = url;
    }

    /** 显示进度条对话框 */
    public void showDownloadDialog() {
        dialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("下载中，请稍后");
        dialog.setCancelable(false);
        dialog.show();

        //下载apk
        downloadAPK();
    }

    /**
     * 下载apk的线程
     */
    public void downloadAPK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    InputStream is = conn.getInputStream();

                    File file = new File(savePath);
                    // 目录不存在时创建目录
                    if (!file.exists()) {
                        file.mkdir();
                    } else {
                        Log.d("111", "目录存在！");
                    }

                    File apkFile = new File(saveFileName);
                    FileOutputStream fos = new FileOutputStream(apkFile);

                    // 每次写入1k
                    byte buf[] = new byte[1024];

                    do {
                        int numread = is.read(buf);
                        if (numread <= 0) {
                            //下载完成通知安装
                            mHandler.sendEmptyMessage(DOWNLOADED);
                            break;
                        }
                        fos.write(buf, 0, numread);
                        mHandler.sendEmptyMessage(DOWNLOADING);//下载中，更新进度条
                    } while (!cancelFlag); //点击取消就停止下载.

                    fos.close();
                    is.close();
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 更新UI的handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOADED:
                    dialog.dismiss();
                    Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
//                    installAPK();
                    break;
                case DOWNLOAD_FAILED:
                    dialog.dismiss();
                    Toast.makeText(mContext, "网络断开，请稍候再试", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 下载完成后自动安装apk
     */
    public void installAPK() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.example.administrator.appupdatedemo.fileprovider", apkFile);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }

        mContext.startActivity(intent);

    }
}
