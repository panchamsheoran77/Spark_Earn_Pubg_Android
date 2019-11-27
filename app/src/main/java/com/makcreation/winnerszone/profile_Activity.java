package com.makcreation.winnerszone;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makcreation.winnerszone.login_stuff.LogInFragment;
import com.makcreation.winnerszone.wallet_stuff.wallet;
import static com.makcreation.winnerszone.utils.navbrHelper.enableNavigation;

public class profile_Activity extends AppCompatActivity {
    private static final int Activity_num = 2;
    private int money;
    private FirebaseAuth auth;
    private TextView userMoney,pubguser,pubgid,mp,mw;
    private String pubgusers,pubgids,numbers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        setupBottomNavigtionView();
        userMoney = findViewById(R.id.user_money);
        pubguser = findViewById(R.id.pubg_userName);
        pubgid = findViewById(R.id.pubg_id);
        mp = findViewById(R.id.match_played_number);
        mw = findViewById(R.id.match_win);
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user").child(uid);
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                money = dataSnapshot.child("money").getValue(Integer.class);
                userMoney.setText(""+money);
                pubgusers = dataSnapshot.child("pubg_userName").getValue(String.class);
                pubguser.setText(""+pubgusers);
                pubgids = dataSnapshot.child("pubg_userId").getValue(String.class);
                pubgid.setText(""+pubgids);
                mp.setText(""+dataSnapshot.child("total").getValue(Integer.class));
                mw.setText(""+dataSnapshot.child("totalWin").getValue(Integer.class));
               numbers = dataSnapshot.child("paytm_number").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

setUpAds();

    }

    private void setUpAds() {
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");
        AdView mAdView = findViewById(R.id.google_ad_profile);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    private void setupBottomNavigtionView(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        enableNavigation(this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }

    public void login_activity(View view) {
        auth.signOut();
startActivity(new Intent(getApplicationContext(),LogInFragment.class));
    }

    public void toWallet(View view) {
        startActivity(new Intent(this,wallet.class));
    }

    public void shareIntent(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "hey Buddy!!I just found an app,that earns you money for playing PUBG.it worth a shot." +
                "" +
                "http://www.bookmania.in/sparkStudios/";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void toedit(View view) {
        Intent i = new Intent(this,edit_Profile.class);
         i.putExtra("pubgId",pubgids);
        i.putExtra("pubgUser",pubgusers);
        i.putExtra("number",numbers);
        startActivity(i);
    }

    public void mailus(View view) {
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+"makcreation77@gmail.com"));
            startActivity(intent);

        }catch (ActivityNotFoundException e){

        }
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
    public void toInsta(View view) {
        Uri uri = Uri.parse("http://instagram.com/_u/_spark_studios_");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/_spark_studios_")));
        }
    }

    public void youtube(View view) {
        try
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.youtube.com/channel/UCKi4msakw6xJMJ__mKvMlOQ"));
            startActivity(intent);
        }
        catch (Exception e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/channel/UCKi4msakw6xJMJ__mKvMlOQ")));
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}