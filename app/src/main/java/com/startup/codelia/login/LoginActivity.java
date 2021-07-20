package com.startup.codelia.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.startup.codelia.R;
import com.startup.codelia.SignUpActivity;
import com.startup.codelia.UserActivity;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextView registrarse;

    TextInputEditText txtEmail, txtPassword;
    MaterialButton btnSigIn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setupView();
        mAuth = FirebaseAuth.getInstance();

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnSigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });
    }

    private void validar() {
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Correo inválido");
            return;
        }else{
            txtEmail.setError(null);
        }

        if(password.isEmpty() || password.length() < 5){
            txtPassword.setError("Se necesitan al menos 5 caracteres");
            return;
        }else if(!Pattern.compile("[0-9]").matcher(password).find()){
            txtPassword.setError("");
        }
        iniciarSesion(email, password);
    }

    private void iniciarSesion(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "Credenciales inválidas, intente nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setupView() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnSigIn = findViewById(R.id.btnSigIn);

        registrarse = findViewById(R.id.nuevoUsuario);
    }
}