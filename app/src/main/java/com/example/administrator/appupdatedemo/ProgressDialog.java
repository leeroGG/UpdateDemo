package com.example.administrator.appupdatedemo;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

/**
 * <pre>
 *     author : Leero
 *     e-mail : 925230519@qq.com
 *     time  : 2018-01-12
 *     desc  :
 *     version: 1.0
 * </pre>
 */
public class ProgressDialog extends Dialog {

    private RingProgressBar progressBar;
    private TextView progressSize;

    public ProgressDialog(Context context) {
        super(context, R.style.mydialog);
        setContentView(R.layout.progress_dialog);

        progressBar = findViewById(R.id.progress);
        progressSize = findViewById(R.id.progress_size);
        setCancelable(false);
    }

    public void setProgress(int progress, String total, String current) {
        progressBar.setProgress(progress);
        progressSize.setText(current + "/" + total);
    }

}
