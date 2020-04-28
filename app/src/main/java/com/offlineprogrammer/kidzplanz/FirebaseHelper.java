package com.offlineprogrammer.kidzplanz;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.offlineprogrammer.kidzplanz.user.User;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class FirebaseHelper {
    private User m_User;
    FirebaseFirestore m_db;
    private static final String TAG = "FirebaseHelper";

    public FirebaseHelper (){
        m_db = FirebaseFirestore.getInstance();
    }

    Observable<String> getDeviceToken() {
        return Observable.create((ObservableEmitter<String> emitter) -> {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String deviceToken = instanceIdResult.getToken();
                    emitter.onNext(deviceToken);
                }
            });

        });



    }

     Observable<User> getUserData(String deviceToken) {

        return Observable.create((ObservableEmitter<User> emitter) -> {

            m_User = new User(deviceToken);
            m_db.collection("users")
                    .whereEqualTo("deviceToken", deviceToken)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {
                                        m_User=document.toObject(User.class);
                                    }

                                }
                                emitter.onNext(m_User);

                            } else {
                                emitter.onError(task.getException());
                                Log.d("Got Date", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        });

    }
}
