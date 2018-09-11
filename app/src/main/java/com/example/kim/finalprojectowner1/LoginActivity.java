package com.example.kim.finalprojectowner1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kim.finalprojectowner1.chat.MessageActivity;
import com.example.kim.finalprojectowner1.fragment.ChatFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class LoginActivity extends AppCompatActivity {

    private EditText id;
    private EditText password;

    Button registerbtn;
    Button loginbtn;

    FirebaseRemoteConfig firebaseRemoteConfig;
    private FirebaseAuth firebaseAuth; //로그인관리
    private FirebaseAuth.AuthStateListener authStateListener;//로그인 되었는지 안되었는 채크를 해주는 역활


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText) findViewById(R.id.loginActivity_edittext_id);
        password = (EditText) findViewById(R.id.loginActivity_edittext_password);
        registerbtn = (Button) findViewById(R.id.registerbtn);
        loginbtn = (Button) findViewById(R.id.loginbtn);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        loginbtn.setOnClickListener((View v) -> {
            if (id.getText().toString().equals(" ") || password.getText().toString().equals(" ") || id.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            loginEvent();
        });

        registerbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
        //로그인 인터페이스 리스너



        authStateListener = firebaseAuth -> {
            //로그인이 되었거나 로그아웃이 되었을 때
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                //로그인
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();//자기를 닫아주는 역활
            } else {
                //로그아웃
            }
        };

    }

    private void loginEvent() {
        firebaseAuth.signInWithEmailAndPassword(id.getText().toString(), password.getText().toString()).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                //로그인이 실패한 부분
                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                //로그인이 정상적으로 처리가 되었는지 안되었는지 확인을 해주는 부분
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed() {

    }
}

