package com.indical.snapbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by sachin on 6/15/16.
 *
 * Adapter for GridView to display images
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mPaths;
    private static final int IMAGE_HEIGHT = 100;
    private static final int IMAGE_WIDTH = 100;

    public ImageAdapter(Context c, List<String> paths) {
        mContext = c;
        mPaths = paths;
    }
    @Override
    public int getCount() {
        return mPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null) {
            // if not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else {
            imageView = (ImageView) convertView;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPaths.get(position), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        options.inSampleSize = getInSampleSize(85, 85, imageWidth, imageHeight);

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(mPaths.get(position), options);
        imageView.setImageBitmap(bitmap);

        return imageView;
    }

    public static int getInSampleSize(int reqWidth, int reqHeight,
                                      int imageWidth, int imageHeight) {
        final int height = imageHeight;
        final int width = imageWidth;
        int sampleSize = 1;

        if(height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while((halfHeight / sampleSize) > reqHeight &&
                    (halfWidth / sampleSize) > reqWidth) {
                sampleSize *=2;
            }
        }
        return sampleSize;
    }
}
