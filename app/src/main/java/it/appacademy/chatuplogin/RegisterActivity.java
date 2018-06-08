package it.appacademy.chatuplogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etNome;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfermaPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.initUI();

        mAuth = FirebaseAuth.getInstance();

    }

    private void initUI() {
        etNome = findViewById(R.id.etRegName);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPass);
        etConfermaPassword = findViewById(R.id.etRegPassConf);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        Toast.makeText(this, "Utente già loggato", Toast.LENGTH_LONG).show();
    }

    private void createFirebaseUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("ChatUp", "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("ChatUp", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void btnRegistratiClick(View view) {
        Log.d("RegisterActivity", "Button Registrati clicked");

        String name = this.etNome.getText().toString();
        String email = this.etEmail.getText().toString();
        String password = this.etPassword.getText().toString();

        //valido i dati
        if (!this.nomeValido(name)) {
            Toast.makeText(getApplicationContext(), "Nome non valido", Toast.LENGTH_LONG).show();
        } else if (!this.emailValida(email)) {
            Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_LONG).show();
        } else if (!this.passwordValida(password)) {
            Toast.makeText(getApplicationContext(), "Password non valida", Toast.LENGTH_LONG).show();
        } else {
            this.createFirebaseUser(email, password);
        }

    }

    public void tvLoginClick(View view) {
        Log.d("RegisterActivity", "TextView Login clicked");

        Intent intent2 = new Intent(this, LoginActivity.class);
        startActivity(intent2);
    }

    private boolean nomeValido(String nome) {
        return nome.length() > 8;
    }

    private boolean emailValida(String email) {
        return email.contains("@");
    }

    private boolean passwordValida(String password) {

        String confermaPassword = this.etConfermaPassword.getText().toString();

        return password.length() > 8 && password.equals(confermaPassword);

    }
}