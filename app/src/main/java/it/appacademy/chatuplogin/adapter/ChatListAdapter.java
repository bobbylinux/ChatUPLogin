package it.appacademy.chatuplogin.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import it.appacademy.chatuplogin.R;
import it.appacademy.chatuplogin.model.Messaggio;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>{

    private Activity mActivity;
    private DatabaseReference mDataBaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mDataSnapshot;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            mDataSnapshot.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference ref, String name){

        mActivity = activity;
        mDataBaseReference  = ref.child("messaggi");
        mDisplayName = name;
        mDataSnapshot = new ArrayList<>();

        mDataBaseReference.addChildEventListener(mListener);

    }


    public class ChatViewHolder extends RecyclerView.ViewHolder{

        TextView autore;
        TextView messaggio;
        LinearLayout.LayoutParams params;

        public ChatViewHolder(View itemView) {
            super(itemView);

            autore = (TextView)itemView.findViewById(R.id.tv_autore);
            messaggio = (TextView)itemView.findViewById(R.id.tv_messaggio);
            params = (LinearLayout.LayoutParams) autore.getLayoutParams();

        }
    }


    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.chat_msg_row, parent, false);
        ChatViewHolder vh = new ChatViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {

        DataSnapshot snapshot = mDataSnapshot.get(position);

        Messaggio msg = snapshot.getValue(Messaggio.class);


        holder.autore.setText(msg.getAutore());
        holder.messaggio.setText(msg.getMessaggio());

        boolean sonoIo = msg.getAutore().equals(mDisplayName);
        setChatItemStyle(sonoIo, holder);


    }

    private void setChatItemStyle(boolean sonoIo, ChatViewHolder holder){
        if(sonoIo){
            holder.params.gravity = Gravity.END;
            holder.autore.setTextColor(Color.BLUE);
            holder.messaggio.setBackgroundResource(R.drawable.in_msg_bg);


        }else{
            holder.params.gravity = Gravity.START;
            holder.autore.setTextColor(Color.MAGENTA);
            holder.messaggio.setBackgroundResource(R.drawable.out_msg_bg);
        }

        holder.autore.setLayoutParams(holder.params);
        holder.messaggio.setLayoutParams(holder.params);

    }

    @Override
    public int getItemCount() {
        return mDataSnapshot.size();
    }


    public void clean(){
        mDataBaseReference.removeEventListener(mListener);
    }
}