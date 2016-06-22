package com.indical.snapbox;

import android.widget.ProgressBar;

/**
 * Created by sachin on 6/19/16.
 *
 * Item to be shown in the on going upload listView
 */
public class ProgressListViewItem {


    private String fileName;
    private ProgressBar progressBar;
    private int progress;

    ProgressListViewItem(String fileName, ProgressBar progressBar) {
        setFileName(fileName);
        this.progressBar = progressBar;
        progress = 0;
    }

    public String getFileName() {
        return fileName;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setFileName(String name) {
        fileName = name;
    }

    public void setProgress(int value) {
        progressBar.setProgress(value);
        progress = value;
    }

    public int getProgress() {
        return progress;
    }
}
