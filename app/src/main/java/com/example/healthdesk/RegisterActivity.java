package com.example.healthdesk;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout emailTextInputLayout,passwordTextInputLayout,phoneTextInputLayout,nameTextInputLayout;
    private Button loginButton,registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;
    private ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailTextInputLayout=(TextInputLayout)findViewById(R.id.email_register_textinputlayout);
        passwordTextInputLayout=(TextInputLayout)findViewById(R.id.password_register_textinputlayout);
        phoneTextInputLayout=(TextInputLayout)findViewById(R.id.phone_register_textinputlayout);
        nameTextInputLayout=(TextInputLayout)findViewById(R.id.name_textinputlayout);
        loginButton=(Button)findViewById(R.id.login_register_button);
        registerButton=(Button)findViewById(R.id.register_register_button);
        registerProgressBar=(ProgressBar)findViewById(R.id.register_progressBar);

        mAuth=FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail() | !validatePassword() | !validateName() | !validatePhone()){
                    Toast.makeText(RegisterActivity.this, "Error in Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser();
            }
        });

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
    private boolean validateName(){
        String nameInput=nameTextInputLayout.getEditText().getText().toString().trim();
        if(nameInput.isEmpty()){
            nameTextInputLayout.setError("Field can't be empty");
            return false;
        }else{
            nameTextInputLayout.setError(null);
            return true;
        }
    }
    private boolean validatePhone(){
        String phoneInput=phoneTextInputLayout.getEditText().getText().toString().trim();
        if(phoneInput.isEmpty()){
            phoneTextInputLayout.setError("Field can't be empty");
            return false;
        }else{
            phoneTextInputLayout.setError(null);
            return true;
        }
    }
    private void registerUser(){
        final String emailInput=emailTextInputLayout.getEditText().getText().toString().trim();
        final String phoneInput=phoneTextInputLayout.getEditText().getText().toString().trim();
        final String nameInput=nameTextInputLayout.getEditText().getText().toString().trim();
        final String passwordInput=passwordTextInputLayout.getEditText().getText().toString().trim();

        registerProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();

                    userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                    String imageUrl="https://firebasestorage.googleapis.com/v0/b/chatapplication-da329.appspot.com/o/profile_images%2Fdefault%20avatar.png?alt=media&token=3f91b549-9664-4dcd-acbf-47e2fbf8e462";

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", nameInput);
                    userMap.put("email", emailInput);
                    userMap.put("image", imageUrl);
                    userMap.put("phone", phoneInput);
                    userMap.put("password",passwordInput);
                    Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                    userDatabaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                registerProgressBar.setVisibility(View.INVISIBLE);
                                Intent afterloginIntent = new Intent(RegisterActivity.this, AfterLogin.class);
                                startActivity(afterloginIntent);
                                finish();
                            }else{
                                registerProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this, "Error in registering data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
