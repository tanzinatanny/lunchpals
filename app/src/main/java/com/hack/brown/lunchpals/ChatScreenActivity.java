package com.hack.brown.lunchpals;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.hack.brown.models.Chat;
import com.hack.brown.models.ChatRoomRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ChatRoomRepository chatRoomRepository;

    private EditText message;
    private Button send;

    private String roomId;

    private RecyclerView chats;
    private ChatsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        roomId = getIntent().getStringExtra("id");

        chatRoomRepository = new ChatRoomRepository(FirebaseFirestore.getInstance());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        initUI();
        showChatMessages();
    }

    private void initUI() {
        message = findViewById(R.id.edittext_chatbox);
        send = findViewById(R.id.button_chatbox_send);
        chats = findViewById(R.id.reyclerview_message_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        chats.setLayoutManager(manager);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    Toast.makeText(
                            ChatScreenActivity.this,
                            getString(R.string.error_empty_message),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    addMessageToChatRoom();
                }
            }
        });
    }

    private void showChatMessages() {
        chatRoomRepository.getChats(roomId, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("ChatRoomActivity", "Listen failed.", e);
                    return;
                }

                List<Chat> messages = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    messages.add(
                            new Chat(
                                    doc.getId(),
                                    doc.getString("chat_room_id"),
                                    doc.getString("sender_id"),
                                    doc.getString("message"),
                                    doc.getLong("sent")
                            )
                    );
                }
                Log.d("yup", messages.size() + " size");
                if (messages.size() > 0) {
                    Log.d("yup", messages.get(0).getMessage() + " message");
                }

                adapter = new ChatsAdapter(messages, currentUser.getUid());
                chats.setAdapter(adapter);
            }
        });
    }

    private void addMessageToChatRoom() {
        String chatMessage = message.getText().toString();
        message.setText("");
        send.setEnabled(false);
        chatRoomRepository.addMessageToChatRoom(
                roomId,
                currentUser.getUid(),
                chatMessage,
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        send.setEnabled(true);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        send.setEnabled(true);
                        Toast.makeText(
                                ChatScreenActivity.this,
                                getString(R.string.error_message_failed),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}
