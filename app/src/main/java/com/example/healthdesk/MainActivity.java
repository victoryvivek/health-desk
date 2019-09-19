package com.example.healthdesk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout emailTextInputLayout,passwordTextInputLayout;
    private Button loginButton,registerButton;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailTextInputLayout=(TextInputLayout)findViewById(R.id.email_login_textinputlayout);
        passwordTextInputLayout=(TextInputLayout)findViewById(R.id.password_login_textinputlayout);
        loginButton=(Button)findViewById(R.id.login_login_button);
        registerButton=(Button)findViewById(R.id.register_login_button);

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
        String emailInput=emailTextInputLayout.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            emailTextInputLayout.setError("Field can't be empty");
            return false;
        }else{
            emailTextInputLayout.setError(null);
            return true;
        }
    }
    private boolean validatePassword(){
        String passwordInput=passwordTextInputLayout.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty()){
            passwordTextInputLayout.setError("Field can't be empty");
            return false;
        }else{
            passwordTextInputLayout.setError(null);
            return true;
        }
    }
    private void validateUser(){
        String emailInput=emailTextInputLayout.getEditText().getText().toString().trim();
        String passwordInput=passwordTextInputLayout.getEditText().getText().toString().trim();

        mAuth.signInWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currenUser=mAuth.getCurrentUser();
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
