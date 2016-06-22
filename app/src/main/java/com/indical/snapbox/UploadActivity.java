package com.indical.snapbox;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends AppCompatActivity {

    private ListView listView;
    private String filePath;
    private String fileName;
    private static final String TAG = "upload activity";
    private File file;
    private ProgressAdapter adapter;
    private TransferUtility transferUtility;
    private List<ProgressListViewItem> uploadList;
    private ProgressListViewItem uploadingItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null)
            toolbar.setTitle("Uploads");
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        filePath = extras.getString("filePath");
        Log.d(TAG, "file path: " + filePath);

        listView = (ListView) findViewById(R.id.uploadListView);

        file = new File(filePath);
        fileName = file.getName();
        uploadList = new ArrayList<>();
        uploadList.add(new ProgressListViewItem(fileName, new ProgressBar(this)));
        adapter = new ProgressAdapter(uploadList, this);
        listView.setAdapter(adapter);

        upload();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void finishUploadActivity() {
        this.finish();
    }

    private void upload() {
        if(filePath == null || filePath.length() < 1){
            Log.d(TAG, "file path has errors");
            this.finish();
        }

        transferUtility = AWSUtil.getTransferUtility(this);
        TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, fileName,
                file);
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.FAILED) {
                    Log.d(TAG, "transfer state failed");
                    showToast("transfer failed");
                }
                if (state == TransferState.CANCELED) {
                    Log.d(TAG, "transfer canceled");
                    showToast("transfer canceled");
                }
                if(state == TransferState.COMPLETED) {
                    uploadList.clear();
                    showToast("Upload complete");
                    finishUploadActivity();
                }
                Log.d(TAG, "transfer state: " + state.toString());
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                uploadingItem = adapter.getItem(id);
                Log.d(TAG, "bytes total: " + bytesTotal);
                if(bytesTotal < 1 ) {
                    Log.d(TAG, "on progress changed bytesTotal < 1");
                    return;
                }
                float progress = (float) ((bytesCurrent * 100.0 / bytesTotal));
                updateItemProgressBar((int) progress);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d(TAG, "error while uploading to AWS. id: " + id);
                showToast("error while uploading to AWS");
                finishUploadActivity();
            }
        });
    }

    private void updateItemProgressBar(int progress) {
        uploadingItem.getProgressBar().setProgress(progress);
        uploadingItem.setProgress(progress);
        adapter.notifyDataSetChanged();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
