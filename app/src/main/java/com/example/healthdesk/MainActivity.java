package com.example.healthdesk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private TextInputEditText emailTextInputEditText,passwordTextInputEditText;
    private Button loginButton,registerButton;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailTextInputEditText=(TextInputEditText) findViewById(R.id.email_login_textInputEditText);
        passwordTextInputEditText=(TextInputEditText) findViewById(R.id.password_login_textInputEditText);
        loginButton=(Button)findViewById(R.id.login_login_button);
        registerButton=(Button)findViewById(R.id.register_login_button);
        loginProgressBar=(ProgressBar)findViewById(R.id.login_progressBar);

        mAuth=FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail() | !validatePassword()){
                    return;
                }
                validateUser();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private boolean validateEmail(){
        String emailInput=emailTextInputEditText.getText().toString().trim();
        if(emailInput.isEmpty()){
            emailTextInputEditText.setError("Field can't be empty");
            return false;
        }else{
            emailTextInputEditText.setError(null);
            return true;
        }
    }
    private boolean validatePassword(){
        String passwordInput=passwordTextInputEditText.getText().toString().trim();
        if(passwordInput.isEmpty()){
            passwordTextInputEditText.setError("Field can't be empty");
            return false;
        }else{
            passwordTextInputEditText.setError(null);
            return true;
        }
    }
    private void validateUser(){
        String emailInput=emailTextInputEditText.getText().toString().trim();
        String passwordInput=passwordTextInputEditText.getText().toString().trim();
        loginProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currenUser=mAuth.getCurrentUser();
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    updateUI(currenUser);
                }else{
                    Toast.makeText(MainActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUI(FirebaseUser currentUser){
        if(currentUser==null)return;
        else{
            Intent intent=new Intent(MainActivity.this,AfterLogin.class);
            startActivity(intent);
        }
    }
}
