package com.indical.snapbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachin on 6/18/16.
 *
 * Custom adapter for ListView
 */
public class ProgressAdapter extends BaseAdapter {

    private List<ProgressListViewItem> uploads = new ArrayList<>();
    private final Context context;
    private int pos = 0;
//    private final String fileName;

    ProgressAdapter(Context context, String path) {
        this.context = context;
//        this.fileName = path;
    }

    ProgressAdapter(List<ProgressListViewItem> list, Context context) {
        uploads = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return uploads.size();
    }

    @Override
    public ProgressListViewItem getItem(int position) {
        return uploads.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ProgressBar progressBar;
        TextView fName;

        ViewHolder(View view) {
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            fName = (TextView) view.findViewById(R.id.uploadingFilename);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.upload_row_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        // TODO: 6/20/16 Find when exactly getView() is called

        ProgressListViewItem item = getItem(position);
        viewHolder.progressBar.setProgress(item.getProgress());
        item.setProgressBar(viewHolder.progressBar);
        viewHolder.fName.setText(item.getFileName());

        return convertView;
    }
}
