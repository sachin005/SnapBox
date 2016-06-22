package com.indical.snapbox;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.ArrayList;
import java.util.List;

public class CloudImages extends AppCompatActivity {
    private static final String TAG = "CloudImages.java";
    private GridView gridView;
    private AmazonS3Client s3Client;
    private SimpleAdapter simpleAdapter;
    private List<String> s3Keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "cloud images activity started");

        gridView = (GridView) findViewById(R.id.cloud_gridView);
        registerForContextMenu(gridView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();
        initUI();

        GetAWSImageList getImageList = new GetAWSImageList();
        getImageList.execute();
    }

    private void initData() {
        s3Client = AWSUtil.getS3Client(this);
        s3Keys = new ArrayList<>();
    }

    private void initUI() {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.download_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    class GetAWSImageList extends AsyncTask<Void, Void, Void> {

        List<S3ObjectSummary> s3ObjectSummaryList = new ArrayList<>();

        @Override
        protected void onPostExecute(Void voids) {
            // todo: notify the adapter that its data set has changed.
            logKeys();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "getting keys updates");
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "executing async task in cloud images" + s3Client.listBuckets());
            // key is used to identify a file on AWS cloud.

            s3ObjectSummaryList = s3Client.listObjects(Constants.BUCKET_NAME).getObjectSummaries();
            s3Keys.clear();

            for(S3ObjectSummary summary : s3ObjectSummaryList) {
                s3Keys.add(summary.getKey());
            }
            Log.d(TAG, "keys list: " + s3Keys);
            return null;
        }
    }

    private void logKeys() {
        for(String keys : s3Keys) {
            Log.d(TAG, keys);
        }
    }
}
