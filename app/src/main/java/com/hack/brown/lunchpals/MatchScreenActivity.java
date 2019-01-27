package com.hack.brown.lunchpals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MatchScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_screen);
    }

    public void openChatScreen( View view){
        Intent intent = new Intent(this, ChatScreenActivity.class );
        startActivity(intent);
        Log.d("intent", "method called for chat screen");
    }

}
