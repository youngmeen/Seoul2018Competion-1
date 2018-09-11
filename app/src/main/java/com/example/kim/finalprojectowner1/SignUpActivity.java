package com.example.kim.finalprojectowner1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kim.finalprojectowner1.fragment.ChatFragment;
import com.example.kim.finalprojectowner1.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText id;
    EditText name;
    EditText password;
    EditText place;
    Button signup;

    String stEmail;
    String stPassWord;
    String stPlace;
    String stName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        id = (EditText) findViewById(R.id.signupActivity_edittext_id);
        password = (EditText) findViewById(R.id.signupActivity_edittext_password);
        name = (EditText) findViewById(R.id.signupActivity_edittext_name);
        place = (EditText) findViewById(R.id.signupActivity_edittext_place);

        signup = (Button) findViewById(R.id.signupActivity_button_signup);

        signup.setOnClickListener(view -> {
            stEmail = id.getText().toString();
            stPassWord = password.getText().toString();
            stPlace = place.getText().toString();
            stName = name.getText().toString();

            if (stEmail.isEmpty() || stEmail.equals(" ") || stPassWord.isEmpty() || stPassWord.equals(" ") || stName.isEmpty() || stName.equals(" ") || stPlace.isEmpty() || stPlace.equals("") || stPassWord.length() < 6)  {
                Toast.makeText(SignUpActivity.this, "다시 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            } else {
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(stEmail, stPassWord)
                        .addOnCompleteListener(SignUpActivity.this, task -> {
                            final String uid = task.getResult().getUser().getUid();
                            UserModel userModel = new UserModel();
                            userModel.userName = name.getText().toString();
                            userModel.place = stPlace;
                            userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(aVoid -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
                            finish();
                        });


            }
        });
    }
}
