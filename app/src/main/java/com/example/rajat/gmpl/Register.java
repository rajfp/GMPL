package com.example.rajat.gmpl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.iid.FirebaseInstanceId;

public class Register extends AppCompatActivity implements View.OnClickListener {
EditText em,pass,name;
    Button b3;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        em= (EditText) findViewById(R.id.editText3);
        pass= (EditText) findViewById(R.id.editText4);
        mAuth=FirebaseAuth.getInstance();
        b3= (Button) findViewById(R.id.button3);
        b3.setOnClickListener(this);
        name= (EditText) findViewById(R.id.editText5);
        progressBar= (ProgressBar) findViewById(R.id.pb);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button3:
                Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_SHORT).show();
                registerUser();
                break;
        }
    }
    private void registerUser()
    {
String email=em.getText().toString();
       String password=pass.getText().toString();
        if(email.isEmpty())
        {
         em.setError("Email is required");
            em.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
    {
        em.setError("Please enter valid email");
        em.requestFocus();
        return;
    }
        if(password.isEmpty())
        {
            pass.setError("Password is required");
            pass.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            pass.setError("Password should be at least 6 characters long");
            pass.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    finish();
                    progressBar.setVisibility(View.GONE);
                    userProfile();
                    Toast.makeText(getApplicationContext
                            (), "User registration successful", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                else if(task.getException() instanceof FirebaseAuthUserCollisionException)
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void userProfile()
    {
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null)
        {
            UserProfileChangeRequest profileupdate=new UserProfileChangeRequest.Builder().setDisplayName(name.getText().toString().trim()).build();
            user.updateProfile(profileupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Log.d("Testing","user profile updated");
                    }
                }
            });

        }
    }
}
