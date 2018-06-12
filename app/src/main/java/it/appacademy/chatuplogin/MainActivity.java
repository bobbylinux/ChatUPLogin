package it.appacademy.chatuplogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        this.updateUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            String extra = b.getString("msg");
            Toast.makeText(this,"Utente : "+ extra, Toast.LENGTH_SHORT).show();
        }
        mAuth = FirebaseAuth.getInstance();
        setTitle(mAuth.getCurrentUser().getDisplayName());
        //TODO: prendere il nome utente dalle shared preferences.
        getNomeOnSharedPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout_item) {
            Log.i(TAG, "Logout selezionato");

            mAuth.signOut();
            this.updateUI();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this,"Nessun utente loggato ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }

    }

    public String getNomeOnSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);

        String nomeOnSharedPreferences = sharedPreferences.getString(RegisterActivity.NOME_KEY, null);
        Log.i("getNomeOnSharedPref", "Nome on pref: " + nomeOnSharedPreferences);
        return nomeOnSharedPreferences;
    }
}
