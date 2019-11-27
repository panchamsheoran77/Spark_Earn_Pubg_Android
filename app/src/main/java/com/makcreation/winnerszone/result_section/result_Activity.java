package com.makcreation.winnerszone.result_section;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makcreation.winnerszone.R;
import java.util.ArrayList;
import java.util.List;

import static com.makcreation.winnerszone.utils.navbrHelper.enableNavigation;

public class result_Activity extends AppCompatActivity {
    private RecyclerView result_recycler;
    private List<result_recycler_stuff> result_recycler_stuffs = new ArrayList<>();
    private result_adapter result_adapter;
    private ProgressDialog progressDialog;
    private String[] upComMatchNum = new String[20];
    private AdView mAdView;

    private static final int Activity_num = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result);
        progressDialog_setUp();
        setupBottomNavigtionView();
        setUpResultRecyclerView();
            setUpMachDetails();
setUpAds();
    }
    private void setupBottomNavigtionView(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        enableNavigation(this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }
    private void setUpResultRecyclerView(){
        result_recycler = findViewById(R.id.result_container);
        result_adapter = new result_adapter(result_recycler_stuffs,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        result_recycler.setLayoutManager(layoutManager);
        result_recycler.setItemAnimator(new DefaultItemAnimator());
        result_recycler.setAdapter(result_adapter);
    }
    private void setUpMachDetails() {
        DatabaseReference databaseReference ;
            String map="";
            int iii=0;
            for(int z =1;z<=3;z++){
                switch(z){
                    case 1:
                        map="erangel";
                        break;
                    case 2:
                        map="miramar";
                        break;
                    case 3:
                        map = "sanhok";
                        break;
                }
            databaseReference = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("pubg_match_deatils").child(map);
            DatabaseReference data = databaseReference.child("played");
                String finalMap = map;
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long UpComMacthCount = dataSnapshot.getChildrenCount();

                     int ii = 1;
                    while(ii <=UpComMacthCount) {
                        upComMatchNum[ii] = dataSnapshot.child("match"+ ii).getValue(String.class);

                        ii++;
                    }
                    fullHistory(UpComMacthCount, finalMap,iii);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        if((progressDialog != null)&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        }
    private void setUpAds() {
        MobileAds.initialize(this,
                "ca-app-pub-6959993616164778~8449413775");
        mAdView = findViewById(R.id.google_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
private void fullHistory(long count,String map,int i){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("pubg_match_deatils").child(map);
            while ( i <=count){
    DatabaseReference database = databaseReference.child("match"+upComMatchNum[i]);
                database.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String matchNo = (String) dataSnapshot.child("matchNo").getValue(String.class);
            String per_Kill = (String) dataSnapshot.child("perKill").getValue(String.class);
            String win_prize = (String) dataSnapshot.child("winPrize").getValue(String.class);
            String entryFee = (String) dataSnapshot.child("entryFee").getValue(String.class);
            String date_time = (String) dataSnapshot.child("dateTime").getValue(String.class);
            String type = (String) dataSnapshot.child("type").getValue(String.class);
            String version = (String) dataSnapshot.child("version").getValue(String.class);
            String winner1 = dataSnapshot.child("winner1").getValue(String.class);
            String winner2 = dataSnapshot.child("winner2").getValue(String.class);
            String winner3 = dataSnapshot.child("winner3").getValue(String.class);
            if(matchNo != null && per_Kill != null && win_prize != null && entryFee != null && date_time != null && type != null && version != null &&winner1 != null && winner2 != null && winner3 != null )
            {
                prepareRecycler(win_prize, per_Kill, entryFee, type, version, map, matchNo, date_time,winner1,winner2,winner3);
            }else{
                if(progressDialog.isShowing())
                progressDialog.dismiss();
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
                i++;
           }
}

    @Override
    protected void onPause() {
        super.onPause();
        if((progressDialog != null)&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        finish();
    }

    private void prepareRecycler(String win_prize, String per_kill, String entryFee, String type, String version, String map, String matchNo, String date_time,String winner1,String winner2,String winner3) {
        result_recycler_stuff result_stuff = new result_recycler_stuff(""+win_prize,""+per_kill,""+entryFee,""+type,""+version,""+map,"Match No : "+matchNo,""+date_time,winner1,winner2,winner3);
        result_recycler_stuffs.add(result_stuff);
        result_adapter.notifyDataSetChanged();
        goToResultDetails();
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
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
    private void goToResultDetails() {
    result_adapter.setOnItemClickListener(new RecyclerViewClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (result_recycler_stuffs.get(position).getWinner1() != null && result_recycler_stuffs.get(position).getWinner2() != null && result_recycler_stuffs.get(position).getWinner3()!=null) {
                Intent i = new Intent(getApplicationContext(), result_details.class);
                i.putExtra("matchNo", "" + result_recycler_stuffs.get(position).getMatchno());
                i.putExtra("dateTime", "" + result_recycler_stuffs.get(position).getDate_time());
                i.putExtra("entryFee", "" + result_recycler_stuffs.get(position).getEnteyfee());
                i.putExtra("Map", "" + result_recycler_stuffs.get(position).getMap());
                i.putExtra("perKill", "" + result_recycler_stuffs.get(position).getPerkill());
                i.putExtra("winPrice", "" + result_recycler_stuffs.get(position).getWinprice());
                i.putExtra("type", "" + result_recycler_stuffs.get(position).getType_txt());
                i.putExtra("version", "" + result_recycler_stuffs.get(position).getVersion_txt());
                i.putExtra("winner1", "" + result_recycler_stuffs.get(position).getWinner1());
                i.putExtra("winner2", "" + result_recycler_stuffs.get(position).getWinner2());
                i.putExtra("winner3", "" + result_recycler_stuffs.get(position).getWinner3());
                startActivity(i);
            }
        }
        @Override
        public void onItemLongClick(View view, int position) {

        }
    });
    }
    private void progressDialog_setUp() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

    }

}
