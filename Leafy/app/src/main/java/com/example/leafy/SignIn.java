package com.example.leafy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {
    Button loginButton;
    TextView signup;
    EditText email, pass;
    DBhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loginButton = findViewById(R.id.login);
        email = findViewById(R.id.emailadd1); // Make sure this ID exists in your XML
        pass = findViewById(R.id.pass1); // Make sure this ID exists in your XML
        signup = findViewById(R.id.signuptext); // Make sure this ID exists in your XML
        db = new DBhelper(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString().trim();
                String passText = pass.getText().toString().trim();
                if (emailText.isEmpty()) {
                    email.setError("This field is required");
                } else {
                    email.setError(null); // Clear the error when valid
                }
                if (passText.isEmpty()) {
                    pass.setError("This field is required");
                } else {
                    pass.setError(null); // Clear the error when valid
                }
                if (emailText.isEmpty() || passText.isEmpty()) {
                    Toast.makeText(SignIn.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkuser = db.checkusernamepassword(emailText,passText);
                    if(checkuser == true){
                        Toast.makeText(SignIn.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignIn.this, Home.class); // Replace with actual signup activity
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    }else{
                        Toast.makeText(SignIn.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class); // Replace with actual signup activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }
}
