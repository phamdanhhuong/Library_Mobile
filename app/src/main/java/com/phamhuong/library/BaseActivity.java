package com.phamhuong.library;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.phamhuong.library.fragment.profile.FragmentProfile;
import com.phamhuong.library.fragment.home.HomeFragmentNew;
import com.phamhuong.library.fragment.reservation.BasketFragment;
import com.phamhuong.library.fragment.search.FragmentSeachedBooks;
import com.phamhuong.library.fragment.store.FragmentStore;
import com.phamhuong.library.model.InfoResponse;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.model.UserLoginInfo;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.fragment.notification.NotificationFragment;
import com.phamhuong.library.service.DatabaseHelper;
import com.phamhuong.library.utils.NotificationHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    private View lastSelectedItem;
    SearchView searchView;
    TextView txtFullName;
    TextView txtUserEmail;
    ImageView imgAvatarHeader;
    TextView toolbarTitle;
    ImageView toolbarEndIcon;
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

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    FragmentSeachedBooks fragment = FragmentSeachedBooks.newInstance(query);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit();
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtUserEmail = headerView.findViewById(R.id.txtUserEmail);
        imgAvatarHeader = headerView.findViewById(R.id.imgUserAvatar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarEndIcon = findViewById(R.id.toolbar_end_icon);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            loadFragment(new HomeFragmentNew());
            updateUiForFragment(R.id.nav_home);
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

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onBottomNavigationViewItemSelectedCustom(item);
                updateUiForFragment(item.getItemId());
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
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", false);
            editor.apply();
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.clearAllLoginData();
            RetrofitClient.currentToken = "";
            RetrofitClient.retrofit = null;

            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_basket) {
            fragment = new BasketFragment();
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

        searchView.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.GONE);
        toolbarEndIcon.setVisibility(View.GONE);


        if (id == R.id.nav_home) {
            item.setIcon(R.drawable.ic_bottom_navbar_home_on);
            fragment = new HomeFragmentNew();
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarEndIcon.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_save) {
            item.setIcon(R.drawable.ic_bottom_navbar_save_on);
            fragment = new FragmentStore();
            searchView.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_notification) {
            item.setIcon(R.drawable.ic_bottom_navbar_setting_on);
            fragment = new NotificationFragment();
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarEndIcon.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_profile) {
            item.setIcon(R.drawable.ic_bottom_navbar_user_on);
            fragment = new FragmentProfile();
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarEndIcon.setVisibility(View.VISIBLE);
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

        transaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right);

        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null); // Thường thêm vào back stack để có thể quay lại
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
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();
        if (userLoginInfo != null && !userLoginInfo.getUsername().equals("guest")) {
            Log.d("BaseActivity", "Token before getInfo: " + RetrofitClient.currentToken);
            apiService = RetrofitClient.getRetrofit().create(APIService.class);
            Call<InfoResponse> call = apiService.getInfo(userLoginInfo.username);
            call.enqueue(new Callback<InfoResponse>() {
                @Override
                public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                    InfoResponse body = response.body();
                    if (body != null) {
                        txtFullName.setText(body.getFull_name());
                        txtUserEmail.setText(body.getEmail());
                        String avatarUrl = body.getAvatar();
                        dbHelper.updateLoginInfoSQLite(body.getId(), body.getFull_name(), body.getEmail(), userLoginInfo.username, avatarUrl);
                        updateAvatarHeader(avatarUrl);

                        int userId = body.getId();
                        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                        notificationHelper.checkAndSendInitialNotifications(String.valueOf(userId));
                    }
                }

                @Override
                public void onFailure(Call<InfoResponse> call, Throwable t) {
                    Toast.makeText(BaseActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            // User is guest, no need to fetch info
            txtFullName.setText("Guest User");
            txtUserEmail.setText("");
        }
    }
    private void updateAvatarHeader(String avatarUrl) {
        if (imgAvatarHeader != null && avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.avatar1)
                    .error(R.drawable.avatar1)
                    .into(imgAvatarHeader);
        } else if (imgAvatarHeader != null) {
            imgAvatarHeader.setImageResource(R.drawable.avatar1);
        }
    }
    private void updateUiForFragment(int itemId) {
        toolbar.setBackgroundColor(getResources().getColor(R.color.my_yellow));
        searchView.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.GONE);
        toolbarEndIcon.setVisibility(View.GONE);

        if (itemId == R.id.nav_home) {
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarTitle.setText("Home");
            toolbarEndIcon.setVisibility(View.VISIBLE);
            toolbarEndIcon.setImageResource(R.drawable.ic_home_end);
        } else if (itemId == R.id.nav_notification) {
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarTitle.setText("Notification");
            toolbarEndIcon.setVisibility(View.VISIBLE);
            toolbarEndIcon.setImageResource(R.drawable.ic_notification_end);
        } else if (itemId == R.id.nav_profile) {
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarTitle.setText("Profile");
            toolbarEndIcon.setVisibility(View.VISIBLE);
            toolbarEndIcon.setImageResource(R.drawable.ic_profile_end);
        } else if (itemId == R.id.nav_save) {
//            toolbar.setBackgroundColor(getResources().getColor(R.color.onPrimary));
            searchView.setVisibility(View.VISIBLE);
        }
    }
}
