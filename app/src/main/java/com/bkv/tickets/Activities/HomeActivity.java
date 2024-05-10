package com.bkv.tickets.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bkv.tickets.Adapters.HomePagerAdapter;
import com.bkv.tickets.Models.User;
import com.bkv.tickets.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    private static final String LOG_TAG = HomeActivity.class.getName();

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private User currentUser;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Log.d(LOG_TAG, "User not logged in");
            finish();
        }

        Intent intent = getIntent();
        currentUser = new User(
                intent.getStringExtra("USER_ID"),
                intent.getStringExtra("USER_AUTH_ID"),
                intent.getStringExtra("USER_EMAIL"),
                intent.getStringExtra("USER_NAME")
        );

        if (currentUser.getId() == null || currentUser.getAuthId() == null
                || currentUser.getEmail() == null || currentUser.getName() == null) {
            Log.e(LOG_TAG, "Failed to get current user from intent");
            Toast.makeText(HomeActivity.this, getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
            return;
        }


        db = FirebaseFirestore.getInstance();

        tabLayout = findViewById(R.id.tabBar);
        viewPager = findViewById(R.id.viewPager);

        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(Math.min(2, Math.max(0, tab.getPosition())));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(Math.min(2, Math.max(0, position))));
                super.onPageSelected(position);
            }
        });
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}