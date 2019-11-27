package com.makcreation.winnerszone.play_secion;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makcreation.winnerszone.R;
import com.makcreation.winnerszone.login_stuff.LogInFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import static com.makcreation.winnerszone.utils.navbrHelper.enableNavigation;
public class MainActivity extends AppCompatActivity {
    private static final int Activity_num = 1;
    private String uid;
    private String updatesite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView upteinfo = findViewById(R.id.updateinfo);
DatabaseReference drupstie = FirebaseDatabase.getInstance().getReference().child("sparkEarn");
drupstie.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        updatesite = dataSnapshot.child("updatesite").getValue(String.class);
    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
        checkVesion();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent login = new Intent(getApplicationContext(), LogInFragment.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);

                } else {
                    uid = firebaseAuth.getCurrentUser().getUid();

                }
            }
        };
        auth.addAuthStateListener(authStateListener);
        setupBottomNavigtionView();
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }

                    }
                });
setUpAds();
    }
    private void setUpAds() {
        MobileAds.initialize(this,
                "ca-app-pub-6959993616164778~8449413775");
        AdView mAdView = findViewById(R.id.google_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
    protected void createNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("pushNotification", "Push Notification", importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setDescription("Important Update");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.enableLights(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setLightColor(Color.RED);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.enableVibration(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setVibrationPattern(    new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }

    }
    @Override
    protected void onStart() {
        super.onStart();

    }

//   bottom navigation bar

    private void setupBottomNavigtionView(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        enableNavigation(this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);

    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    private void checkVesion() {
        DatabaseReference drv = FirebaseDatabase.getInstance().getReference().child("sparkEarn");
        drv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String upVersion =       dataSnapshot.child("version").getValue(String.class);
                String version = null;
                try {
                    PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionName;

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if(!upVersion.equalsIgnoreCase(version)){
                    Toast.makeText(MainActivity.this, "Please update App", Toast.LENGTH_SHORT).show();
                    if(updatesite != null){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updatesite));
                    startActivity(browserIntent);}
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_history) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void gotoErangleDetails(View view) {
        Intent i = new Intent(getApplicationContext(),Match_menu.class);
        i.putExtra("map","erangel");
        i.putExtra("uid",uid);
        startActivity(i);
    }

    public void gotoMiramarDetails(View view) {
        Intent i = new Intent(getApplicationContext(),Match_menu.class);
        i.putExtra("map","miramar");
        i.putExtra("uid",uid);
        startActivity(i);
    }

    public void gotoSanhokDetails(View view) {
        Intent i = new Intent(getApplicationContext(),Match_menu.class);
        i.putExtra("map","sanhok");
        i.putExtra("uid",uid);
        startActivity(i);
    }
}