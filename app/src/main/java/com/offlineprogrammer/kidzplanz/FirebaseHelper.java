package com.offlineprogrammer.kidzplanz;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.offlineprogrammer.kidzplanz.kid.Kid;
import com.offlineprogrammer.kidzplanz.plan.KidPlan;
import com.offlineprogrammer.kidzplanz.planitem.PlanItem;
import com.offlineprogrammer.kidzplanz.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class FirebaseHelper {
    private User m_User;
    FirebaseFirestore m_db;
    FirebaseAuth firebaseAuth;
    private static final String TAG = "FirebaseHelper";

    public FirebaseHelper (){
        m_db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
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
                                        m_User.setFirebaseId(document.getId());
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

    Observable<User> saveUser() {
        return Observable.create((ObservableEmitter<User> emitter) -> {
            Map<String, Object> user = new HashMap<>();
            user.put("deviceToken", m_User.getDeviceToken());
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

            if (currentUser != null) {
                user.put("userId", currentUser.getEmail());

            } else {

                user.put("userId", "guest");
            }
            m_db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            m_User.setFirebaseId(documentReference.getId());
                            emitter.onNext(m_User);
                            Log.d("SavingData", "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("SavingData", "Error adding document", e);
                            emitter.onError(e);
                        }
                    });

        });
    }


    public Observable<Kid> saveKid(Kid newKid) {
        return Observable.create((ObservableEmitter<Kid> emitter) -> {
            DocumentReference newKidRef = m_db.collection("users").document(m_User.getFirebaseId()).collection("kidz").document();
            newKid.setFirestoreId(newKidRef.getId());
            newKid.setUserFirestoreId(m_User.getFirebaseId());
            Map<String, Object> kidValues = newKid.toMap();
            newKidRef.set(kidValues, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Add Kid", "DocumentSnapshot successfully written!");
                            emitter.onNext(newKid);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Add Kid", "Error writing document", e);
                            emitter.onError(e);
                        }
                    });

        });

    }

    public Observable<ArrayList<Kid>> getKidz(String userFireStoreId) {
         ArrayList<Kid> kidzList = new ArrayList<>();
        return Observable.create((ObservableEmitter<ArrayList<Kid>> emitter) -> {

            m_db.collection("users").document(userFireStoreId).collection("kidz")
                    .orderBy("createdDate", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {
                                        Log.d("Got Data", document.getId() + " => " + document.getData());
                                        Kid myKid = document.toObject(Kid.class);
                                        kidzList.add(myKid);
                                    }
                                }
                                emitter.onNext(kidzList);
                            } else {
                                Log.d("Got Date", "Error getting documents: ", task.getException());
                                emitter.onError(task.getException());
                            }
                        }
                    });


        });

    }

    public Observable<KidPlan>  saveKidPlan(KidPlan newPlan, Kid selectedKid) {

        return Observable.create((ObservableEmitter<KidPlan> emitter) -> {
            DocumentReference newPlanRef = m_db.collection("users").document(selectedKid.getUserFirestoreId()).collection("kidz").document(selectedKid.getFirestoreId()).collection("planz").document();
            newPlan.setFirestoreId(newPlanRef.getId());
            newPlan.setKidFirestoreId(selectedKid.getFirestoreId());
            Map<String, Object> planValues = newPlan.toMap();

            newPlanRef.set(planValues, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Add Kid", "DocumentSnapshot successfully written!");
                            emitter.onNext(newPlan);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Add Kid", "Error writing document", e);
                            emitter.onError(e);
                        }
                    });

        });

    }

    public Observable<ArrayList<KidPlan>> getKidPlanz(Kid selectedKid) {

        ArrayList<KidPlan> kidPlanzList = new ArrayList<>();
        return Observable.create((ObservableEmitter<ArrayList<KidPlan>> emitter) -> {

            DocumentReference selectedKidRef = m_db.collection("users").document(selectedKid.getUserFirestoreId()).collection("kidz").document(selectedKid.getFirestoreId());
            selectedKidRef.collection("planz").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {
                                        Log.d("Got Task Data", document.getId() + " => " + document.getData());
                                        KidPlan kidPlan = document.toObject(KidPlan.class);
                                        kidPlanzList.add(kidPlan);
                                    }
                                }
                                emitter.onNext(kidPlanzList);
                            } else {
                                Log.d("Got Date", "Error getting documents: ", task.getException());
                                emitter.onError(task.getException());
                            }
                        }
                    });
        });

    }

    public Observable<PlanItem> savePlanItem(PlanItem newItem, KidPlan selectedPlan, Kid selectedKid) {

        return Observable.create((ObservableEmitter<PlanItem> emitter) -> {
            DocumentReference newPlanItemRef = m_db.collection("users").document(selectedKid.getUserFirestoreId())
                    .collection("kidz").document(selectedKid.getFirestoreId())
                    .collection("planz").document(selectedPlan.getFirestoreId())
                    .collection("itemz").document();
            newItem.setFirestoreId(newPlanItemRef.getId());
            newItem.setPlanFirestoreId(selectedPlan.getFirestoreId());
            Map<String, Object> planItemValues = newItem.toMap();

            newPlanItemRef.set(planItemValues, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Add Kid", "DocumentSnapshot successfully written!");
                            emitter.onNext(newItem);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Add Kid", "Error writing document", e);
                            emitter.onError(e);
                        }
                    });

        });

    }

    public Observable<ArrayList<PlanItem>> getPlanItemz(KidPlan selectedPlan, Kid selectedKid) {
        ArrayList<PlanItem> planItemzList = new ArrayList<>();
        return Observable.create((ObservableEmitter<ArrayList<PlanItem>> emitter) -> {


            DocumentReference selectedPlanRef = m_db.collection("users").document(selectedKid.getUserFirestoreId())
                    .collection("kidz").document(selectedKid.getFirestoreId())
                    .collection("planz").document(selectedPlan.getFirestoreId());
            selectedPlanRef.collection("itemz").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {
                                        Log.d("Got Task Data", document.getId() + " => " + document.getData());
                                        PlanItem planItem = document.toObject(PlanItem.class);
                                        planItemzList.add(planItem);
                                    }
                                }
                                emitter.onNext(planItemzList);
                            } else {
                                Log.d("Got Date", "Error getting documents: ", task.getException());
                                emitter.onError(task.getException());
                            }
                        }
                    });
        });
    }
}
