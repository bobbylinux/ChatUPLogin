package it.appacademy.chatuplogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    // TODO 1: Definire a livello globale le varibili e le costanti
    EditText mNomeUtente;
    EditText mPassword;

    // Costanti
    final String mUtente = "appAdmin";
    final String mPass = "12345678";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void btnLoginClick(View view) {
        Log.d("LoginActivity", "Login Button Click");

        // TODO 2: Collegare le variabili ai Widgets
        mNomeUtente = (EditText)findViewById(R.id.etRegName);
        mPassword = (EditText)findViewById(R.id.etRegPass);

        String nomeUtente = mNomeUtente.getText().toString();
        String password = mPassword.getText().toString();

        Log.d("LoginActivity",nomeUtente );
        Log.d("LoginActivity",password );

        // TODO 3: Se Nome Utente e Password sono corretti passare a MainActivity altrimenti presentare Toast con errore
        if(nomeUtente.equals(mUtente) && password.equals(mPass) ){

            Intent intent3 = new Intent(this, MainActivity.class);
            // TODO 4: Passare il nome utente a MainActivity con putExtra
            intent3.putExtra("msg", nomeUtente);

            startActivity(intent3);
        }else{
            Toast.makeText(this, "Nome utente o Password non corretti", Toast.LENGTH_SHORT).show();
        }




    }

    public void tvRegistratiClick(View view) {

        Log.d("LoginActivity", "Registrati Click");

        Intent intent1 = new Intent(this, RegisterActivity.class);
        startActivity(intent1);

    }
}
