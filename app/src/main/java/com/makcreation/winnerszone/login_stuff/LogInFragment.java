package com.makcreation.winnerszone.login_stuff;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.makcreation.winnerszone.R;
import com.makcreation.winnerszone.play_secion.MainActivity;

public class LogInFragment extends AppCompatActivity{
    private EditText emialId;
    private EditText password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);

// conatiner definations
    emialId = findViewById(R.id.email_input_edit);
    password = findViewById(R.id.password_input_edit);
    progressDialog = new ProgressDialog(this);
    firebaseAuth =  FirebaseAuth.getInstance();
    Button textView = findViewById(R.id.login_button);
    textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            user_login();
        }
    });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void user_login() {
        String email  = emialId.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Verifying Please Wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LogInFragment.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void tologin(View view) {
        startActivity(new Intent(this,SignUpFragment.class));
    }
    public void resrtPassword(View view) {
        String email  = emialId.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.sendPasswordResetEmail(email);
        Toast.makeText(this,"Check Your Inbox For Password Reset Link",Toast.LENGTH_LONG).show();
    }
}
