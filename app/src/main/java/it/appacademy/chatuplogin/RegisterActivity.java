package it.appacademy.chatuplogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText etConfermaPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.etConfermaPassword = findViewById(R.id.etRegPassConf);
    }

    public void btnRegistratiClick(View view) {

        Log.d("RegisterActivity", "Button Registrati clicked");
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