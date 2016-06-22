package com.indical.snapbox;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Snaps extends AppCompatActivity {

    private final int READ_EXTERNAL_STORAGE_PERMISSION = 1;
    private final String TAG = "Snaps.java";
    private List<String> imagesPaths = new ArrayList<>();
    private static String selectedFilePath = "";
    private boolean isShortClick = false;
    GridView gridView;

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.local_images:
//                showLocalImages();
//                return true;
//            case R.id.cloud_images:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snaps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(this, "retrieving local images", Toast.LENGTH_SHORT).show();
        gridView = (GridView) findViewById(R.id.gridview);
        // IMPORTANT: register view to get context menu
        registerForContextMenu(gridView);

        // get storage access permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION);
        }
        else {
            Toast.makeText(this, "permission obtained", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "already has permission");
            startFetchingImages();
//            setupGridView();

//            imagesPaths = getImagePaths();
//            if(imagesPaths.size() > 0)
//                gridView.setAdapter(new ImageAdapter(this, imagesPaths));

//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(Snaps.this, "" + position, Toast.LENGTH_SHORT).show();
//                }
//            });
        }

//        GridView gridView = (GridView) findViewById(R.id.gridview);
//        gridView.setAdapter(new ImageAdapter(this, getImagePaths()));
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Snaps.this, "" + position, Toast.LENGTH_SHORT).show();
//            }
//        });

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "on request permission result invoked");
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION: {
                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    setupGridView();
                    startFetchingImages();
                }
                else {
                    Toast.makeText(this, "App needs access to SD card", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
            }
        }
    }

    private void startFetchingImages() {
        FetchImagesAsyncTask fetchImages = new FetchImagesAsyncTask();
        Cursor cursor = getMediaCursor();
        fetchImages.execute(cursor);

    }

    private void setupGridView() {

        // TODO: 6/21/16 remove after adding async task
//        imagesPaths = getImagePaths();
        Log.d(TAG, "images Paths size: " + imagesPaths.size());
        Log.d(TAG, "setup grid view called");

        gridView.setAdapter(new ImageAdapter(this, imagesPaths));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Snaps.this, imagesPaths.get(position) + " " + position,
                        Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "on request permission images Paths size: " + imagesPaths.size());
                isShortClick = true;
                selectedFilePath = imagesPaths.get(position);
            }
        });

//        gridView.setLongClickable(true);
//
//        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
////                if(isShortClick) {
////                    isShortClick = false;
////                    return false;
////                }
//                selectedFilePath = imagesPaths.get(position);
//                Log.d(TAG, "file path: " + selectedFilePath + " pos: " + position);
//                openContextMenu(view);
//                return true;
//            }
//        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.upload_context_options, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.upload_image) {
            Intent intent = new Intent(this, UploadActivity.class);
            Log.d(TAG, "path in intent: " + selectedFilePath);
            intent.putExtra("filePath", selectedFilePath);
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }

    private Cursor getMediaCursor() {
        Uri uri;
        Cursor cursor;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projections = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String order = MediaStore.Images.Media._ID;
        cursor = getContentResolver().query(uri, projections, null, null, order);

        return cursor;
    }

    private List<String> getImagePaths() {
        Uri uri;
        List<String> imageList = new ArrayList<>();
        Cursor cursor;
        int indexData, indexFolder;
        String path = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projections = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String order = MediaStore.Images.Media._ID;
        cursor = getContentResolver().query(uri, projections, null, null, order);

        indexData = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
        indexFolder = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        boolean flag = true;

        while(cursor.moveToNext()) {
            path = cursor.getString(indexData);
            if(flag) {
                Log.d(TAG, "path: " + path);
                Toast.makeText(this, "first path: " + path, Toast.LENGTH_SHORT).show();
                flag = false;
            }
            imageList.add(path);
        }
        cursor.close();
        Log.d(TAG, "images Paths size: " + imageList.size());
        return imageList;
    }

    class FetchImagesAsyncTask extends AsyncTask<Cursor, Void, List<String>> {
//        Cursor cursor;
        int indexData;
        String path;
        List<String> imageList = new ArrayList<>();

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            imagesPaths = list;
            setupGridView();
        }

        @Override
        protected List<String> doInBackground(Cursor... cursors) {
            Cursor cursor = cursors[0];
            while(cursor.moveToNext()) {
                path = cursor.getString(indexData);
                imageList.add(path);
            }
            return imageList;
        }
    }

}
