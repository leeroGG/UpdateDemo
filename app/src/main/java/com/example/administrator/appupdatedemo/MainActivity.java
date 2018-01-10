package com.example.administrator.appupdatedemo;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class MainActivity extends AppCompatActivity {

    // 下载地址
    private String url = "http://a.gdown.baidu.com/data/wisegame/d3150a5a852ebd92/wangyixinwen_766.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPermission();
            }
        });
    }

    /**
     * 检查wifi网络
     */
    private boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    private void showConfirmDialog() {
        SweetAlertDialog dialog = new SweetAlertDialog(this);
        dialog.setTitleText("提示");
        dialog.setContentText("当前网络非WIFI环境，确定下载吗？");
        dialog.setConfirmText("确定");
        dialog.setCancelText("取消");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                DownloadManager manager = new DownloadManager(MainActivity.this, url);
                sweetAlertDialog.dismiss();
                manager.showDownloadDialog();
            }
        });
        dialog.show();
    }

    /**
     * 读写权限获取
     */
    private void getPermission() {
        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "存储", R.drawable.permission_ic_storage));
        HiPermission.create(MainActivity.this)
                .permissions(permissionItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {
                        SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this);
                        dialog.setTitleText("确认下载吗？");
                        dialog.setConfirmText("确认");
                        dialog.setCancelText("取消");
                        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if (isWifi()) {
                                    DownloadManager manager = new DownloadManager(MainActivity.this, url);
                                    sweetAlertDialog.dismiss();
                                    manager.showDownloadDialog();
                                } else {
                                    sweetAlertDialog.dismiss();
                                    showConfirmDialog();
                                }
                            }
                        });
                        dialog.show();
                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });
    }
}
