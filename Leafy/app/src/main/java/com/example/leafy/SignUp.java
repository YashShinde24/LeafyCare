package com.example.leafy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    EditText email,pass,repass;
    TextView move;
    Button cont;
    DBhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.emailadd);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        cont = findViewById(R.id.con);
        move = findViewById(R.id.move);
        db = new DBhelper(this);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString().trim();
                String passText = pass.getText().toString().trim();
                String repasstext = repass.getText().toString().trim();
                if (emailText.isEmpty()) {
                    email.setError("This field is required");
                } else {
                    email.setError(null);
                }
                if (passText.isEmpty()) {
                    pass.setError("This field is required");
                } else {
                    pass.setError(null);
                }
                if (repasstext.isEmpty()) {
                    repass.setError("This field is required");
                } else {
                    repass.setError(null);
                }

                if (emailText.equals("") || passText.equals("") || repasstext.equals("")) {
                    Toast.makeText(SignUp.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else{
                    if(passText.equals(repasstext)){
                        Boolean checkuser = db.checkusername(String.valueOf(email));
                        if(checkuser == false){
                            Boolean insert = db.insertData(emailText,passText);
                            if(insert == true){
                                Toast.makeText(SignUp.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, Home.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                            }else{
                                Toast.makeText(SignUp.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SignUp.this, "User already exist! Please sign in", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignUp.this, "Password not matching", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });


    }
}