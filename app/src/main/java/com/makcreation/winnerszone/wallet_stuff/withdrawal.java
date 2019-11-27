package com.makcreation.winnerszone.wallet_stuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makcreation.winnerszone.R;
public class withdrawal extends Fragment {
    private TextView text;
    private int user_money;
    String uid;
    FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_widthraw_money, container, false);
        setUpAds(v);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        text = v.findViewById(R.id.wallet_add_section_money_text);
        getCurentWalletAmt();
        setUpAds(v);
        return v;
    } private void setUpAds(View v) {
        MobileAds.initialize(getContext(),
                "ca-app-pub-6959993616164778~8449413775");
        AdView mAdView = v.findViewById(R.id.google_ad_profile);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void getCurentWalletAmt() {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user").child(uid);
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_money = dataSnapshot.child("earning").getValue(Integer.class);
                text.setText(""+user_money);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }}