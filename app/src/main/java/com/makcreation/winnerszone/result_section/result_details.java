package com.makcreation.winnerszone.result_section;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makcreation.winnerszone.R;

public class result_details extends AppCompatActivity {
    private TextView match,entry,kill,win,ver,type,map,date,winner1,winner2,winner3;
    private AdView mAdView;
    private String win1,win2,win3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);
        //def of design components
        conttainerDef();
        //setting things in the  components
        setUpActivity();
setUpAds();
    }
    private void conttainerDef() {
        match = findViewById(R.id.result_detail_matchNo);//result_detail_matchNo
        date = findViewById(R.id.result_detail_dateTime);
        entry = findViewById(R.id.result_detail_entry);
        kill = findViewById(R.id.result_detail_kill);
        win = findViewById(R.id.result_detail_win);
        ver = findViewById(R.id.result_detail_version);
        type = findViewById(R.id.result_detail_type);
        map = findViewById(R.id.result_detail_map);
        winner1 = findViewById(R.id.winner1);
        winner2 = findViewById(R.id.winner2);
        winner3 = findViewById(R.id.winner3);
    }

    private void setUpActivity() {
        Intent i = getIntent();
        String entryFee = i.getStringExtra("entryFee");
        match.setText(i.getStringExtra("matchNo"));
        date.setText(i.getStringExtra("dateTime"));
        entry.setText(entryFee);
        win.setText(i.getStringExtra("winPrice"));
        type.setText(i.getStringExtra("type"));
        map.setText(i.getStringExtra("map"));
        ver.setText(i.getStringExtra("version"));
        kill.setText(i.getStringExtra("perKill"));

                DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user").child(i.getStringExtra("winner1"));
                dr1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        win1 = dataSnapshot.child("pubg_userName").getValue(String.class);
                        Log.e("pancham", "onDataChange: "+win1);
                        winner1.setText(win1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference dr2 = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user").child(i.getStringExtra("winner2"));
                dr2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        win2 = dataSnapshot.child("pubg_userName").getValue(String.class);
                        winner2.setText(win2);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference dr3 = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user").child(i.getStringExtra("winner3"));
                dr3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        win3 = dataSnapshot.child("pubg_userName").getValue(String.class);
                        winner3.setText(win3);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void setUpAds() {
        MobileAds.initialize(this,
                "ca-app-pub-6959993616164778~8449413775");
        mAdView = findViewById(R.id.google_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,result_Activity.class));
    }
}
