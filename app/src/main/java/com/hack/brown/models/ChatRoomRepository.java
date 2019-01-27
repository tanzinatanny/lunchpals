package com.hack.brown.models;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class ChatRoomRepository {
    private static final String TAG = "ChatRoomRepo";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public ChatRoomRepository(FirebaseFirestore db) {
        this.db = db;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public void createRoom(String participant1,
                           String participant2,
                           final OnSuccessListener<DocumentReference> successCallback,
                           final OnFailureListener failureCallback) {
        Map<String, Object> room = new HashMap<>();
        room.put("participant1", participant1);
        room.put("participant2", participant2);

        db.collection("chatrooms")
                .add(room)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        successCallback.onSuccess(documentReference);

                        Map<String, Object> roomRef = new HashMap<>();
                        roomRef.put("room", documentReference);

                        db.collection("users")
                                .document(currentUser.getUid())
                                .collection("rooms")
                                .add(roomRef)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        //nothing
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //nothing
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureCallback.onFailure(e);
                    }
                });
    }

    public void addMessageToChatRoom(String roomId,
                                     String senderId,
                                     String message,
                                     final OnSuccessListener<DocumentReference> successCallback,
                                     final OnFailureListener failureCallback) {
        Map<String, Object> chat = new HashMap<>();
        chat.put("chat_room_id", roomId);
        chat.put("sender_id", senderId);
        chat.put("message", message);
        chat.put("sent", System.currentTimeMillis());

        db.collection("chatrooms")
                .document(roomId)
                .collection("messages")
                .add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        successCallback.onSuccess(documentReference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureCallback.onFailure(e);
                    }
                });
    }

    public void getChats(String roomId, EventListener<QuerySnapshot> listener) {
        db.collection("chatrooms")
                .document(roomId)
                .collection("messages")
                .orderBy("sent", Query.Direction.DESCENDING)
                .addSnapshotListener(listener);
    }
}
