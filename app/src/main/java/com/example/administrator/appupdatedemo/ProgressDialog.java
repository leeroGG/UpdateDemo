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

    private CircleProgressBar progressBar;
    private TextView progressText;

    protected ProgressDialog(Context context) {
        super(context);
        setContentView(R.layout.progress_dialog);

        progressBar = findViewById(R.id.progress);
        progressText = findViewById(R.id.progress_num);
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
        progressText.setText(progress + "%");
    }

}
