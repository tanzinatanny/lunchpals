package com.hack.brown.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthenticator {
    private FirebaseAuth auth;
    public FirebaseAuthenticator() {
        auth = FirebaseAuth.getInstance();
    }

    public void login(@NonNull String username, @NonNull String password) {
        Task<AuthResult> result = auth.signInWithEmailAndPassword(username, password);
        result.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete() && task.isCanceled()) {
                    // Login!
                    // Intent myintent = new Intent(this, MainActivity.class);


                } else {
                    // Throw error message!
                }
            }
        });
    }
}
