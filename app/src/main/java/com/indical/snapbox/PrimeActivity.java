package com.indical.snapbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class PrimeActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.actions_listView);
        String[] actions = {"Camera", "Local Images", "Cloud Images"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, actions);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        callActivity(MainActivity.class);
                        break;
                    case 1:
                        callActivity(Snaps.class);
                        break;
                    case 2:
                        callActivity(CloudImages.class);
                        break;
                }
            }
        });
    }

    private void callActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

}
