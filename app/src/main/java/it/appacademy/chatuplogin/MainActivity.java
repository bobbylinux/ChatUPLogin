package it.appacademy.chatuplogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO 2: Ricevere i dati dell'intent ed estrarli con il metodo getExtras()
        Bundle b = getIntent().getExtras();
        String extra = b.getString("msg");


        // TODO 3: Presentare dati all'utente attraverso un Toast
        Toast.makeText(this,"Utente : "+ extra, Toast.LENGTH_SHORT).show();

    }
}
