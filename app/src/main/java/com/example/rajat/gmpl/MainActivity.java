package com.example.rajat.gmpl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText em,pass;
    FirebaseAuth mAuth;
    ProgressBar p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b2= (Button) findViewById(R.id.button2);
        b2.setOnClickListener(this);
        Button b1= (Button) findViewById(R.id.button);
        b1.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();

        em= (EditText) findViewById(R.id.editText);
        pass= (EditText) findViewById(R.id.editText2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),Profile.class));
        }
    }

    @Override
    public void onClick(View v) {
    switch (v.getId())
    {
        case R.id.button2:
            finish();
            startActivity(new Intent(this,Register.class));
            break;
        case R.id.button:
            userLogin();
            break;
    }
    }
    private  void userLogin()
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

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    finish();
                Intent intent=new Intent(MainActivity.this,Profile.class);
                    startActivity(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
