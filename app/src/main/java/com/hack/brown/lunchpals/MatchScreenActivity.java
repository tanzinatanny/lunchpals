package com.hack.brown.lunchpals;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hack.brown.models.ChatRoomRepository;

public class MatchScreenActivity extends AppCompatActivity {

    private ChatRoomRepository mRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_screen);
        mRoom = new ChatRoomRepository(FirebaseFirestore.getInstance());
    }

    public void openChatScreen(View view){
        mRoom.createRoom("test", "test1",
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        finish();
                        Log.d("PLATYPUS", "yoooooo");

                        Intent intent = new Intent(MatchScreenActivity.this, ChatScreenActivity.class);
                        intent.putExtra("id", documentReference.getId());
                        startActivity(intent);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                MatchScreenActivity.this,
                                getString(R.string.error_creating_room),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

}
