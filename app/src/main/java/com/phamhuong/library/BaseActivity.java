package com.phamhuong.library;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.phamhuong.library.fragment.HomeFragmentNew;
import com.phamhuong.library.model.InfoResponse;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.fragment.ProfileFragment;
import com.phamhuong.library.fragment.HomeFragment;
import com.phamhuong.library.fragment.NotificationFragment;
import com.phamhuong.library.fragment.SearchFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    private View lastSelectedItem;
    TextView txtFullName;
    TextView txtUserEmail;
    APIService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_base), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        drawerLayout = findViewById(R.id.main_base);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setSupportActionBar(toolbar);

        SearchView searchView = findViewById(R.id.search_view);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));

        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtUserEmail = headerView.findViewById(R.id.txtUserEmail);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            loadFragment(new HomeFragmentNew());
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onNavigationItemSelectedCustom(item);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        bottomNavigationView.setItemIconTintList(null);

//        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                onBottomNavigationViewItemSelectedCustom(item);
//                return true;
//            }
//        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                View view = bottomNavigationView.findViewById(item.getItemId());
                int selectedColor = Color.parseColor("#FF5733"); // Màu cam
                int defaultColor = Color.parseColor("#808080");  // Màu xám

                // Duyệt tất cả item và đặt màu mặc định
                for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                    bottomNavigationView.getMenu().getItem(i).getIcon().setTint(defaultColor);
                }

                // Đổi màu icon của item được chọn
                item.getIcon().setTint(selectedColor);
                // Reset animation của item trước
                if (lastSelectedItem != null) {
                    Animation scaleDown = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.scale_down);
                    lastSelectedItem.startAnimation(scaleDown);
                }

                // Phóng to icon được chọn
                Animation scaleUp = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.scale_up);
                view.startAnimation(scaleUp);

                lastSelectedItem = view; // Cập nhật item đã chọn

                onBottomNavigationViewItemSelectedCustom(item);
                return true;
            }
        });
        getInfo();
    }
    public boolean onNavigationItemSelectedCustom(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new HomeFragmentNew();
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (fragment != null) {
            loadFragment(fragment);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean onBottomNavigationViewItemSelectedCustom(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.ic_bottom_navbar_home_off);
        bottomNavigationView.getMenu().findItem(R.id.nav_save).setIcon(R.drawable.ic_bottom_navbar_save_off);
        bottomNavigationView.getMenu().findItem(R.id.nav_notification).setIcon(R.drawable.ic_bottom_navbar_setting_off);
        bottomNavigationView.getMenu().findItem(R.id.nav_profile).setIcon(R.drawable.ic_bottom_navbar_user_off);

        if (id == R.id.nav_home) {
            item.setIcon(R.drawable.ic_bottom_navbar_home_on);
            fragment = new HomeFragmentNew();
        } else if (id == R.id.nav_save) {
            item.setIcon(R.drawable.ic_bottom_navbar_save_on);
            fragment = new NotificationFragment();
        } else if (id == R.id.nav_notification) {
            item.setIcon(R.drawable.ic_bottom_navbar_setting_on);
            fragment = new SearchFragment();
        } else if (id == R.id.nav_profile) {
            item.setIcon(R.drawable.ic_bottom_navbar_user_on);
            fragment = new ProfileFragment();
        }
        if (fragment != null) {
            loadFragment(fragment);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void getInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        String username = sharedPreferences.getString("username", "");

        apiService = RetrofitClient.getRetrofit(token).create(APIService.class);
        Call<InfoResponse> call = apiService.getInfo(username);
        call.enqueue(new Callback<InfoResponse>() {
            @Override
            public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                InfoResponse body = response.body();
                if(body!=null){
                    txtFullName.setText(body.getFull_name());
                    txtUserEmail.setText(body.getEmail());
                }
            }

            @Override
            public void onFailure(Call<InfoResponse> call, Throwable t) {

            }
        });
    }
}
