package com.makcreation.winnerszone.play_secion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makcreation.winnerszone.R;
import java.util.ArrayList;
import java.util.List;

public class Match_menu extends AppCompatActivity {
    private menuAdapter adapter;
    private List<play_stuff> play_stuffs = new ArrayList<>();
    public String map;
    private String uid;
    private ProgressDialog progressDialog;
    private String[] upComMatchNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_menu);
        Intent i = getIntent();
        map = i.getStringExtra("map");
        uid = i.getStringExtra("uid");
        progressDialog_setUp();
        setUpMenuContainer();
        setUpMachDetails();
    }
   private void progressDialog_setUp(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

    }
    private void setUpMenuContainer() {
        RecyclerView menuContainer = findViewById(R.id.menu_container);
        menuContainer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new menuAdapter(this,play_stuffs,map);
        menuContainer.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
    private void setUpMachDetails() {
        upComMatchNum =  new String[20];
        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("pubg_match_deatils").child(map).child("tobeplayed");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long UpComMacthCount = dataSnapshot.getChildrenCount();
                play_stuffs.clear();
                if (UpComMacthCount >0){
                    int i =1;
                    while(i<=UpComMacthCount) {
                        upComMatchNum[i] = dataSnapshot.child("match"+i).getValue(String.class);
                        i++;
                    }
                    numCount(UpComMacthCount);
                }else{
                    if((progressDialog != null)&&progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void numCount(long count) {

        int i =1;

        while ( i <=count){
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("pubg_match_deatils").child(map).child("match"+upComMatchNum[i]);
            mData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String matchNo = (String) dataSnapshot.child("matchNo").getValue(String.class);
                    String per_Kill = (String) dataSnapshot.child("perKill").getValue(String.class);
                    String win_prize = (String) dataSnapshot.child("winPrize").getValue(String.class);
                    String entryFee = (String) dataSnapshot.child("entryFee").getValue(String.class);
                    String date_time = (String) dataSnapshot.child("dateTime").getValue(String.class);
                    String type = (String) dataSnapshot.child("type").getValue(String.class);
                    String map = (String) dataSnapshot.child("map").getValue(String.class);
                    String version = (String) dataSnapshot.child("version").getValue(String.class);
                    int remaing = dataSnapshot.child("remaining").getValue(Integer.class);
                    boolean isjoined = false;
                    String roomid=null,roompass=null;
                  if(dataSnapshot.child("participants").hasChild(uid)){
                      isjoined = true;
                      if(dataSnapshot.child("roomid").getValue(String.class)!=null&&dataSnapshot.child("roompass").getValue(String.class)!=null){
                         roomid = dataSnapshot.child("roomid").getValue(String.class);
                         roompass = dataSnapshot.child("roompass").getValue(String.class);
                      }

                  }
                    if (matchNo != null && per_Kill != null && win_prize != null && entryFee != null && date_time != null && type != null&& version != null ) {
                        prepareRecycler(win_prize, per_Kill, entryFee, type, version, map, matchNo, date_time,remaing,isjoined,roomid,roompass);
                    }else{
                        Toast.makeText(getApplicationContext(), "Data not available", Toast.LENGTH_SHORT).show();
                        if((progressDialog != null)&&progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("pancham", databaseError.getMessage());
                }
            });
            ++i;
        }
    }
    private void prepareRecycler(String winPrize,String perKills,String entryFees , String types, String versions , String maps, String matchNos, String dateTimes,int remaing,boolean isJoined,String roomid,String roompass) {
       play_stuff play_stuff = new play_stuff(""+winPrize,""+perKills,""+entryFees,""+types,""+versions,""+maps,""+matchNos,""+dateTimes,remaing,isJoined,roomid,roompass);
       play_stuffs.add(play_stuff);
       adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new menu_clickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        if (progressDialog!=null)
        {
            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
        }
    }

}