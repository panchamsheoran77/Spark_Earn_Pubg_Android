package com.makcreation.winnerszone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class edit_Profile extends AppCompatActivity {
    private EditText pubgUser,pubgId,number;
    private Button textView;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
           Intent i = getIntent();
        pubgUser = findViewById(R.id.pubg_user_name_input_edit);
        pubgId = findViewById(R.id.pubg_user_id_input_edit);
        number = findViewById(R.id.confirm_password_edit);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user");
        progressDialog = new ProgressDialog(this);
        textView = findViewById(R.id.verticalTextView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        pubgId.setText(i.getStringExtra("pubgId"));
        pubgUser.setText(i.getStringExtra("pubgUser"));
        number.setText(i.getStringExtra("number"));
    }
    private void update() {
        String pubg_userName = pubgUser.getText().toString().trim();
        String pubg_Id = pubgId.getText().toString().trim();
        String pytm_number = number.getText().toString().trim();

        if(TextUtils.isEmpty(pubg_userName)){
            Toast.makeText(getApplicationContext(),"Please enter Pubg User Name ",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pubg_Id)){
            Toast.makeText(getApplicationContext(),"Please enter Pubg Id",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pytm_number)){
            Toast.makeText(getApplicationContext(),"Please enter Paytm Number",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage(" Updating Please Wait...");
        progressDialog.show();
        String user_id = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db =   mDatabase.child(user_id);
        current_user_db.child("pubg_userName").setValue(pubg_userName);
        current_user_db.child("pubg_userId").setValue(pubg_Id);
        current_user_db.child("paytm_number").setValue(pytm_number);
        if((progressDialog != null)&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        startActivity(new Intent(this,profile_Activity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,profile_Activity.class));
    }
}