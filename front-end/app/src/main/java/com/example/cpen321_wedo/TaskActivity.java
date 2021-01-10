package com.example.cpen321_wedo;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cpen321_wedo.models.Task;
import com.example.cpen321_wedo.fragments.UserFragment;
import com.example.cpen321_wedo.fragments.TaskFragment;
import com.example.cpen321_wedo.singleton.RequestQueueSingleton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    private TaskFragment taskFragment;
    private Menu taskMenu;
    private String taskListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("taskListName"));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Intent intent = getIntent();
        taskListId = intent.getStringExtra("taskListId");
        taskFragment = new TaskFragment(intent.getStringExtra("taskListId"));

        viewPagerAdapter.addFragment(taskFragment, "Tasks");
        viewPagerAdapter.addFragment(new UserFragment(), "Chat");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_activity_menu, menu);
        taskMenu = menu;
        menu.setGroupVisible(R.id.taskDefaultMenu, true);
        menu.setGroupVisible(R.id.taskDeleteMenu, false);
        taskFragment.setMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(this, AddTaskActivity.class);
                startActivityForResult(intent, 1);
                return true;
            case R.id.delete:
                taskMenu.setGroupVisible(R.id.taskDefaultMenu, false);
                taskMenu.setGroupVisible(R.id.taskDeleteMenu, true);
                taskFragment.toggleItemViewType();
                return true;
            case R.id.trash:
                taskFragment.deleteTasksSelected();
                taskMenu.setGroupVisible(R.id.taskDefaultMenu, true);
                taskMenu.setGroupVisible(R.id.taskDeleteMenu, false);
                taskFragment.toggleItemViewType();
                return true;
            case R.id.cancel_delete:
                taskMenu.setGroupVisible(R.id.taskDefaultMenu, true);
                taskMenu.setGroupVisible(R.id.taskDeleteMenu, false);
                taskFragment.toggleItemViewType();
                return true;
            default:
                break;
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            final String[] reply = data.getStringArrayExtra("task");

            Task postTask = new Task("SomeID" , reply[0], reply[1], reply[2], reply[3]);
            taskFragment.addTask(postTask);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String[] taskUpdateReply = data.getStringArrayExtra("taskUpdate");
            int index = data.getIntExtra("index", 0);
            long dateModified = data.getLongExtra("dateModified", 0);

            taskFragment.updateTask(taskUpdateReply[0], taskUpdateReply[3], taskUpdateReply[2], taskUpdateReply[1], dateModified, index);
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<Fragment>();
            this.titles = new ArrayList<String>();
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}