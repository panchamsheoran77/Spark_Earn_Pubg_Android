package com.makcreation.winnerszone.wallet_stuff;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.makcreation.winnerszone.R;
import com.makcreation.winnerszone.paytm_integration_stuff.api;
import com.makcreation.winnerszone.paytm_integration_stuff.checkSum;
import com.makcreation.winnerszone.paytm_integration_stuff.constants;
import com.makcreation.winnerszone.paytm_integration_stuff.paytm;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addMoney extends Fragment implements PaytmPaymentTransactionCallback {
    private Button initiate;
    private EditText amt;
    private int money_atm;
    private ProgressDialog progressDialog;
    private int user_money;
    private FirebaseAuth auth ;
    private TextView textView_finalAmt;
    private String uid;
    private AdView mAdView;
    private String order;
    private RecyclerView transContiner;
    private transAdpter adpter;
    private List<transList> transLists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_money,container,false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
         auth = FirebaseAuth.getInstance();
         uid = auth.getCurrentUser().getUid();

        //container Definitons
        containerDef(v);
//getting user current wallet amt
        getCurentWalletAmt();
        initiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateCheckSum();
            }
        });
setUpAds(v);
        return v;
    }

    private void getCurentWalletAmt() {

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user").child(uid);
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_money = dataSnapshot.child("money").getValue(Integer.class);
                textView_finalAmt.setText(""+user_money);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void containerDef(View v) {
    initiate = v.findViewById(R.id.add_money);
    amt = v.findViewById(R.id.add_money_amount);
    textView_finalAmt = v.findViewById(R.id.wallet_add_section_money_text);
    transContiner = v.findViewById(R.id.transContainer);
    transContiner.setLayoutManager(new LinearLayoutManager(getContext()));
    adpter = new transAdpter(getContext(),transLists);
    transContiner.setAdapter(adpter);
    prepareTransList();
    }

    private void prepareTransList() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user").child(uid).child("transaction");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i=1;i<=dataSnapshot.child("total").getValue(Integer.class);++i){
                    Long amt = dataSnapshot.child(""+i).child("2").getValue(Long.class);
                    String id = dataSnapshot.child(""+i).child("1").getValue(String.class);
                    transList list = new transList(""+i,""+amt,""+id);
                    transLists.add(list);
                    Log.e("pancham", "id: "+transLists.get(0).getId()+" amt : "+transLists.get(0).getAmt());
                   adpter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void generateCheckSum() {
        if(isOnline()){
        if(TextUtils.isEmpty(amt.getText().toString().trim())){
            Toast.makeText(getContext(), "Enter Amount", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setMessage("Redirecting to Paytm Please wait...");
            progressDialog.show();
        money_atm = Integer.parseInt(amt.getText().toString().trim());


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        api apiService = retrofit.create(api.class);

        //creating paytm object
        //containing all the values required
        final paytm paytm = new paytm(
                constants.M_ID,
                uid,
                constants.CHANNEL_ID,
                amt.getText().toString().trim(),
                constants.WEBSITE,
                constants.CALLBACK_URL,
                constants.INDUSTRY_TYPE_ID
        );

//creating a call object from the apiService
        Call<checkSum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getIndustryTypeId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl()

        );
        //making the call to generate checksum
        call.enqueue(new Callback<checkSum>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<checkSum> call, Response<checkSum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<checkSum> call, Throwable t) {
                Toast.makeText(getContext(), "call : "+call+",  throw : "+t, Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }

            }
        });
    }}else{
            Toast.makeText(getContext(), "Check your Connection", Toast.LENGTH_SHORT).show();

        }}
    private void setUpAds(View v) {
        MobileAds.initialize(getContext(),
                "ca-app-pub-6959993616164778~8449413775");


        mAdView = v.findViewById(R.id.google_ad_profile);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initializePaytmPayment(String checksumHash, paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getProductionService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", paytm.getmId());
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);

    order = paytm.getOrderId();


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder((HashMap<String, String>) paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(getContext(), true, true, this);

    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {

  if(inResponse.get("RESPMSG").equals("Txn Success")){
     DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user").child(uid);
     user_money = user_money+money_atm;
   database.child("money").setValue(user_money);
database.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        int tt;
        if( dataSnapshot.child("transaction").child("total").getValue(Integer.class) != null){
            tt= dataSnapshot.child("transaction").child("total").getValue(Integer.class);}
        else{
            tt = 0;
        }

        database.child("transaction").child((tt+1)+"").child("1").setValue(order);
        database.child("transaction").child((tt+1)+"").child("2").setValue(money_atm);
        database.child("transaction").child("total").setValue(tt+1);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
  }
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(getContext(), "Network not available", Toast.LENGTH_SHORT).show();
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressedCancelTransaction() {
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }



    protected boolean isOnline() {



        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);



        NetworkInfo netInfo = cm.getActiveNetworkInfo();



        if (netInfo != null && netInfo.isConnectedOrConnecting()) {



            return true;



        } else {



            return false;



        }



    }




}
