package com.example.hobbiz.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataModel {
    public static final DataModel data_instence = new DataModel();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface LoginUserListener{
        void onComplete(FirebaseUser user, Task<AuthResult> task);
    }

    public void loginUser(String email, String password, LoginUserListener listener ) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    listener.onComplete(user, task);
                });
    }

    public interface SignupUserListener{
        void onComplete(FirebaseUser user, Task task);
    }

    public void registerUser(User user, String password, SignupUserListener listener) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userId);
                        Map<String, Object> dbUser = new HashMap<>();
                        dbUser.put("e_mail", user.getEmail());
                        dbUser.put("full_name", user.getFullName());

                        documentReference.set(dbUser).addOnCompleteListener(task1 -> {
                            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                            listener.onComplete(fbUser, task1);
                        });
                    }  else {
                        Log.d("ERR", "Error creating account");
                    }
                });
    }

    public interface UploadHobbyListener {
        void onComplete(String id, Task task);
    }

    public void uploadHobby(Hobbiz hobbiz, Bitmap bitmap, UploadHobbyListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, Object> data_hobby = new HashMap<>();
        data_hobby.put("hobby_name",hobbiz.getHobby_Name());
        data_hobby.put("city", hobbiz.getCity());
        data_hobby.put("age", hobbiz.getAge());
        data_hobby.put("contact", hobbiz.getContact());
        data_hobby.put("description", hobbiz.getDescription());

        data_hobby.put("timestamp", FieldValue.serverTimestamp());

        DocumentReference hobby_doc = db.collection("hobbiz").document();

        hobby_doc.set(data_hobby).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DocumentReference userRef = db.collection("users").document(user.getUid());
                userRef.update("hobbiz", FieldValue.arrayUnion(hobby_doc)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();

                        StorageReference storageRef = storage.getReference();
                        StorageReference imageRef = storageRef.child("images/" + hobby_doc.getId() + ".jpg");

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = imageRef.putBytes(data);
                        uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    Uri downloadUrl = uri;
                                    listener.onComplete( hobby_doc.getId(), task);
                                }));
                    }
                });
            }
        });
    }

    public interface GetHobbyListener{
        void onComplete(Hobbiz hobbiz);
    }

    public void getHobby(String productId, GetHobbyListener listener ) {
        DocumentReference productDocRef = db.collection("products").document(productId);
        productDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                DocumentSnapshot document = task.getResult();
                storageRef.child("images/" + productId + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(document.exists()) {
                            Hobbiz hobbiz = Hobbiz.HobbizFromJson(document.getData());
                            hobbiz.setImage(task.getResult());
                            listener.onComplete(hobbiz);
                        }else {
                            listener.onComplete(null);
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);
            }
        });
    }

    public interface GetAllProductsListener{
        void onComplete(List<Hobbiz> productsList);
    }
    public interface GetUserByIdListener{
        void onComplete(Task task, User user);
    }
    public void getUserById(String userId, GetUserByIdListener listener) {
        DocumentReference userDocRef = db.collection("users").document(userId);
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snap = task.getResult();
                User user = new User();;
                user.setEmail(snap.get("email").toString());
                user.setFullName(snap.get("fname").toString());
                listener.onComplete(task, user);
            }
        });
    }

    public void getAllProducts(Long since,GetAllProductsListener listener) {
        db.collection("hobbiz").whereGreaterThanOrEqualTo(Hobbiz.TIME, new Timestamp(since,0))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    LinkedList<Hobbiz> hobbizList = new LinkedList<Hobbiz>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Hobbiz h = Hobbiz.HobbizFromJson(document.getData());

                        if(h != null) {
                            hobbizList.add(h);
                        }
                        listener.onComplete(hobbizList);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);
            }
        });
    }
}
