package com.example.hobbiz.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hobbiz.Model.Interfaces.DeleteHobbyListener;
import com.example.hobbiz.Model.Interfaces.EditHobbyListener;
import com.example.hobbiz.Model.Interfaces.GetUserById;
import com.example.hobbiz.Model.Interfaces.UploadHobbyListener;
import com.example.hobbiz.Model.Interfaces.UploadImageListener;
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

    public void uploadHobby(Hobbiz hobbiz, Bitmap bitmap, UploadHobbyListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Map<String, Object> dataModelHobby = new HashMap<>();
        DocumentReference hobbyDocRef = db.collection("hobbiz").document();

        hobbyDocRef.set(dataModelHobby).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DocumentReference userRef = db.collection("users").document(user.getUid());
                userRef.update("hobbiz", FieldValue.arrayUnion(hobbyDocRef)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        uploadImage(bitmap, hobbyDocRef.getId(), new UploadImageListener() {
                            @Override
                            public void onComplete(String url) {
                                if (url != null) {
                                    dataModelHobby.put("hobby_name", hobbiz.getHobby_Name());
                                    dataModelHobby.put("age", hobbiz.getAge());
                                    dataModelHobby.put("city", hobbiz.getCity());
                                    dataModelHobby.put("contact", hobbiz.getContact());
                                    dataModelHobby.put("description", hobbiz.getDescription());
                                    dataModelHobby.put("timestamp", FieldValue.serverTimestamp());
                                    dataModelHobby.put("userId", hobbiz.getUserId());
                                    dataModelHobby.put("image", url);
                                    dataModelHobby.put("isDeleted", false);

                                    hobbyDocRef.set(dataModelHobby).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            hobbiz.setImage(url);
                                            listener.onComplete(task,hobbiz);
                                        }
                                    });
                                } else {
                                    listener.onComplete(task, new Hobbiz());

                                }
                            }
                        });
                    }
                });
            }
        });
    }
    public void deleteHobby(Hobbiz hobbiz, DeleteHobbyListener listener){
        DocumentReference documentReference= db.collection("hobbiz").document(hobbiz.getId());
        documentReference.update("isDeleted", true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete();
            }
        });


    }
    public void editHobby(Hobbiz hobbiz, Bitmap bitmap, EditHobbyListener listener) {
        DocumentReference docRef = db.collection("hobbiz").document(hobbiz.getId());
        if(bitmap == null) {
            docRef.set(hobbiz.toJson()).addOnSuccessListener(unused -> listener.onComplete(hobbiz));
        } else {
            uploadImage(bitmap, hobbiz.getId(), url -> {
                hobbiz.setImage(url);
                docRef.set(hobbiz.toJson()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onComplete(hobbiz);
                    }
                });
            });
        }

    }

    public interface GetHobbyListener{
        void onComplete(Hobbiz hobbiz);
    }

    public void getHobby(String hobbyId ,GetHobbyListener listener ){
        db.collection("hobbiz").document(hobbyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    Hobbiz h = Hobbiz.HobbizFromJson(document.getData());
                    h.setId(document.getId());
                }
            }
        });
    }

    public interface GetAllHobbizListener{
        void onComplete(List<Hobbiz> data);
    }
    public void getUserById(String uid, GetUserById listener) {

        DocumentReference docRef = db.collection(Constants.MODEL_FIRE_BASE_USER_COLLECTION).document(uid);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    User u = User.fromJson(document.getData());
                    listener.onComplete(u);
                } else {
                    listener.onComplete(null);
                }
            } else {
                listener.onComplete(null);
            }
        });
    }

    public void getAllHobbiz(Long since, GetAllHobbizListener listener) {
        db.collection("hobbiz")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinkedList<Hobbiz> hobbizList = new LinkedList<Hobbiz>();
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Hobbiz h = Hobbiz.HobbizFromJson(doc.getData());
                        h.setId(doc.getId());
                        if (h != null) {
                            hobbizList.add(h);
                        }
                    }
                }else {
                    Log.d("Hobby", "Not successfull - didn't get all hobbiz");
                }
                listener.onComplete(hobbizList);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR!", "Not successfull - didn't get all hobbiz");
                listener.onComplete(null);
            }
        });
    }

    public void uploadImage(Bitmap bitmap, String id_key, final UploadImageListener listener)  {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imageRef;

        imageRef = storage.getReference().child(Constants.MODEL_FIRE_BASE_IMAGE_COLLECTION).child(id_key);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask=imageRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        listener.onComplete(uri.toString());
                    }
                });
            }
        });
    }


}
