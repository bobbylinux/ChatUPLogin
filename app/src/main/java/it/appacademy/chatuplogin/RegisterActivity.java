package it.appacademy.chatuplogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etNome;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfermaPassword;
    private SharedPreferences sharedPreferences;
    static final String CHAT_PREFS = "ChatPrefs";
    static final String NOME_KEY = "username";

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

    private void createFirebaseUser(String email, String password, final String nome) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("ChatUp", "createUserWithEmail:success");
                            // Toast.makeText(RegisterActivity.this, "Authentication success.",Toast.LENGTH_SHORT).show();
                            //showDialog("Registrazione avvenuta con successo", "Successo", android.R.drawable.ic_dialog_info);

                            salvaNomeInSharedPreferences();

                            setNome(nome);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("ChatUp", "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(RegisterActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            showDialog("Errore nella registrazione", "Errore", android.R.drawable.ic_dialog_alert);
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void showDialog(String message, String title, int icon) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(icon)
                .show();
    }

    private void salvaNomeInSharedPreferences() {
        this.sharedPreferences = this.getSharedPreferences(CHAT_PREFS, 0);
        this.sharedPreferences.edit().putString(NOME_KEY, this.etNome.getText().toString()).apply();
    }

    private void setNome(String nome) {
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
        user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("RegisterActivity", "Nome caricato con successo");
                } else {
                    Log.i("RegisterActivity", "Errore nel caricamento del nome");
                }
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
            this.createFirebaseUser(email, password, name);
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