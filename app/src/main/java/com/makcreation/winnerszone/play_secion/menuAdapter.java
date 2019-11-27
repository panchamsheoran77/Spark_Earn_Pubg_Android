package com.makcreation.winnerszone.play_secion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import com.makcreation.winnerszone.wallet_stuff.wallet;
import java.util.List;

public class menuAdapter extends RecyclerView.Adapter<menuAdapter.MenuContentHolder> {
    private Context mCtx;
    private List<play_stuff> play_stuffList;
    private menu_clickListener menu_clickListener;
    private int usermoney,entryFeeGlobal,remaing;
    private ProgressDialog progressDialog;
    private String matchNoGlobal;
    private String map;
    public menuAdapter(Context mCtx, List<play_stuff> play_stuffList,String map) {
        this.mCtx = mCtx;
        this.play_stuffList = play_stuffList;
        this.map = map;
    }

    @NonNull
    @Override
    public MenuContentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(mCtx).inflate(R.layout.match_menu,viewGroup,false);
        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Please Wait ... ");
        progressDialog.setCancelable(false);
        return new MenuContentHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuContentHolder holder, int position) {
        DatabaseReference uMoney = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        uMoney.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usermoney = dataSnapshot.child("money").getValue(Integer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        play_stuff play_stuff = play_stuffList.get(position);
        holder.position = position;
        String match = "Match : "+play_stuff.getMatchno();
        holder.matchno.setText(match);
        holder.date_txt.setText(play_stuff.getDate_time());
        holder.enrty.setText(play_stuff.getEnteyfee());
        holder.kill.setText(play_stuff.getPerkill());
        holder.win.setText(play_stuff.getWinprice());
        holder.map_text.setText(play_stuff.getMap());
        if (play_stuff.isJoined()){
            holder.join.setText("JOINED");
            holder.join.setEnabled(false);
        }
        holder.remaining.setText(play_stuff.getRemaing()+"");
        remaing = play_stuff.getRemaing();
        if(play_stuff.getRoomid()!=null&&play_stuff.getRoompass()!=null){
            holder.roomdetails.setText("Room id: "+play_stuff.getRoomid()+" Pass : "+play_stuff.getRoompass());
        }
        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchNoGlobal = play_stuffList.get(holder.position).getMatchno();
                entryFeeGlobal = Integer.parseInt(play_stuffList.get(holder.position).getEnteyfee());

                if(remaing >0){
                    if(usermoney >= entryFeeGlobal){
                        progressDialog.show();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        String uid =   firebaseAuth.getCurrentUser().getUid();
                        final int[] i = {0};
                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("pubg_match_deatils").child(map).child("match"+matchNoGlobal).child("participants");
                        dr.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                while (i[0] ==0){
                                    int total;
                                    if( dataSnapshot.child("total").getValue(Integer.class) != null){
                                     total= dataSnapshot.child("total").getValue(Integer.class);}
                                    else{
                                        total = 0;
                                    }
                                    int tol = total+1;
                                    final int[] no = new int[1];
                                    dr.child(uid).setValue(" ");
                                    dr.child("total").setValue(tol);
                                    usermoney = usermoney - entryFeeGlobal;
                                    final int[] l = {0};
                                    DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user").child(uid);
                                    dr1.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            while (l[0] ==0) {
                                                int n = dataSnapshot.child("total").getValue(Integer.class);

                                                no[0] = n + 1;
                                                dr1.child("money").setValue(usermoney);
                                                dr1.child("played").child("match"+ no[0]).setValue(matchNoGlobal);
                                                dr1.child("total").setValue(no[0]);

                                                ++l[0];
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    ++i[0];
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        FirebaseMessaging.getInstance().subscribeToTopic("match"+matchNoGlobal)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg = "Failed";
                                        if (task.isSuccessful()) {
                                            msg = "Successfull";
                                            holder.join.setText("JOINED");
                                            holder.join.setEnabled(false);
                                        }
                                        Toast.makeText(mCtx, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                        DatabaseReference databaseftotal = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("pubg_match_deatils").child(map).child("match"+matchNoGlobal);
                        int finalremaining = remaing-1;
                        databaseftotal.child("remaining").setValue(finalremaining);
                        holder.remaining.setText(finalremaining+"");
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(mCtx, "Please Add Money ", Toast.LENGTH_SHORT).show();
                        mCtx.startActivity(new Intent(mCtx, wallet.class));
                    }
                    play_stuffList.set(holder.position,new play_stuff(true));

                }else{
                    Toast.makeText(mCtx, "No slot remaining", Toast.LENGTH_SHORT).show();
                }
                }
        });
    }
    @Override
    public int getItemCount() {
        return play_stuffList.size();
    }
    public void setOnItemClickListener(menu_clickListener recyclerViewItemClickListener){
        this.menu_clickListener=recyclerViewItemClickListener;
    }

    class MenuContentHolder extends RecyclerView.ViewHolder{
        TextView matchno,date_txt,enrty,win,kill,remaining,map_text,roomdetails;
        int position=0;
        LinearLayout tncll,ll;
        int count=0;
        Button join;
        public MenuContentHolder(@NonNull View itemView) {
            super(itemView);
            matchno = itemView.findViewById(R.id.play_match_no);
            date_txt = itemView.findViewById(R.id.play_date_time);
            enrty = itemView.findViewById(R.id.play_entry_fee_text);
            win = itemView.findViewById(R.id.play_win_price_text);
            kill = itemView.findViewById(R.id.play_per_kill_price_text);
            remaining = itemView.findViewById(R.id.slot_remaining);
            tncll  =  itemView.findViewById(R.id.tnc_layout);
            ll = itemView.findViewById(R.id.ll);
            map_text = itemView.findViewById(R.id.play_map_text);
            join = itemView.findViewById(R.id.match_detail_join);
            roomdetails = itemView.findViewById(R.id.roomid_passs);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menu_clickListener.onItemClick(view,position);
                    if(play_stuffList.get(position).isJoined()){
                        join.setText("JOINED");
                        join.setEnabled(false);
                    }
                    ViewGroup.LayoutParams params = ll.getLayoutParams();
                    if(count == 0){
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    count++;
                    }else{
                        params.height = 950;
                    count =0;
                    }
                    ll.setLayoutParams(params);
                }

            });

        }
    }
}