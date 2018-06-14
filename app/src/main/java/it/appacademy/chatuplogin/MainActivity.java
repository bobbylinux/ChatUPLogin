package it.appacademy.chatuplogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.appacademy.chatuplogin.adapter.ChatListAdapter;
import it.appacademy.chatuplogin.model.Messaggio;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    private EditText mEditText;
    private Button mButton;

    private ChatListAdapter chatListAdapter;
    private RecyclerView recyclerViewChatMsg;

    public MainActivity() {
    }

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

        if (mAuth.getCurrentUser() == null) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        setTitle(mAuth.getCurrentUser().getDisplayName());

        this.initUI();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewChatMsg.setLayoutManager(linearLayoutManager);

        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        chatListAdapter = new ChatListAdapter(this, mDatabaseReference, mAuth.getCurrentUser().getDisplayName());
        recyclerViewChatMsg.setAdapter(chatListAdapter);

        getNomeOnSharedPreferences();
    }

    private void initUI() {
        mEditText = findViewById(R.id.et_messaggio);
        mButton = findViewById(R.id.btn_invia);

        recyclerViewChatMsg = findViewById(R.id.rv_chat);


        //si invia il messaggio sia quando si fa click sul button, sia quando si preme il soft button enter della tastiera android.
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                inviaMessaggio();
                return true;
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviaMessaggio();
            }
        });


    }

    private void inviaMessaggio() {
        Log.i("ChatUp","Invio messaggio");
        String inputMsg = mEditText.getText().toString();
        if (!inputMsg.equals("")) {
            Messaggio chat = new Messaggio(inputMsg, mAuth.getCurrentUser().getDisplayName());
            //Salvare il messaggio sul Cloud
            mDatabaseReference.child("messaggi").push().setValue(chat);
            mEditText.setText("");
        }
    }

    public String getNomeOnSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);

        String nomeOnSharedPreferences = sharedPreferences.getString(RegisterActivity.NOME_KEY, null);
        Log.i("getNomeOnSharedPref", "Nome on pref: " + nomeOnSharedPreferences);
        return nomeOnSharedPreferences;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.layout_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.logout_item){

            Log.i(TAG, "Logout selezionato");
            // TODO: Logout
            mAuth.signOut();
            updateUI();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            Intent intToLogin = new Intent(this, LoginActivity.class);
            finish();

            startActivity(intToLogin);
        }

    }
}
