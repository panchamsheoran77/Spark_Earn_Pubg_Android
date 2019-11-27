package com.makcreation.winnerszone.login_stuff;
import android.content.Intent;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makcreation.winnerszone.play_secion.MainActivity;
import com.makcreation.winnerszone.R;

public class SignUpFragment extends AppCompatActivity{
private EditText pubgUser,pubgId,emailid,pass,number;
private DatabaseReference mDatabase;
private ProgressDialog progressDialog; 
private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_fragment);
        firebaseAuth = FirebaseAuth.getInstance();
        pubgUser = findViewById(R.id.pubg_user_name_input_edit);
        pubgId = findViewById(R.id.pubg_user_id_input_edit);
        emailid = findViewById(R.id.email_input_edit);
        pass = findViewById(R.id.password_input_edit);
        number = findViewById(R.id.confirm_password_edit);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("sparkEarn").child("user");
        progressDialog = new ProgressDialog(this);
        Button textView = findViewById(R.id.verticalTextView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_singup();
            }
        });

    }
    private void user_singup() {
        String email = emailid.getText().toString().trim();
        String password  = pass.getText().toString().trim();
        String pubg_userName = pubgUser.getText().toString().trim();
        String pubg_Id = pubgId.getText().toString().trim();
        String pytm_number = number.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
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
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            String user_id = firebaseAuth.getCurrentUser().getUid();
                          DatabaseReference current_user_db =   mDatabase.child(user_id);
//                          current_user_db.child("name").setValue(user_Name);
                          current_user_db.child("pubg_userName").setValue(pubg_userName);
                          current_user_db.child("pubg_userId").setValue(pubg_Id);
                          current_user_db.child("paytm_number").setValue(pytm_number);
                          current_user_db.child("money").setValue(0);
                          current_user_db.child("total").setValue(0);
                          current_user_db.child("totalWin").setValue(0);
                          current_user_db.child("earning").setValue(0);
                          finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            //display some message here
                            Toast.makeText(getApplicationContext(),"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpFragment.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void tosinup(View view) {
        startActivity(new Intent(this,LogInFragment.class));
    }
}
