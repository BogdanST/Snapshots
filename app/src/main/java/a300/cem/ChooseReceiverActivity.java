package a300.cem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import a300.cem.RecyclerViewReceiver.ReceiverAdapter;
import a300.cem.RecyclerViewReceiver.ReceiverObject;

public class ChooseReceiverActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String Uid;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_receiver);

        try {
            bitmap = BitmapFactory.decodeStream(getApplication().openFileInput("imageToSend"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            finish();
            return;
        }

        Uid = FirebaseAuth.getInstance().getUid();


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);

        //The recyclerView should not have a fixed size since the list can go up. A user can follow multiple 'friends'
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ReceiverAdapter(getDataSet(), getApplication());
        mRecyclerView.setAdapter(mAdapter);

        //Button
        FloatingActionButton mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToStories();
            }
        });


    }
    //Get the results
    private ArrayList<ReceiverObject> results = new ArrayList<>();
    public ArrayList<ReceiverObject> getDataSet(){
        listenForData();
        return results;
    }

    private void listenForData() {
        for (int i = 0; i < UserInformation.listFollowing.size(); i++) {
            DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users").child(UserInformation.listFollowing.get(i));
            userDb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String email = "";
                    String uid = dataSnapshot.getRef().getKey();
                    if (dataSnapshot.child("email") != null) {
                        email = dataSnapshot.child("email").getValue().toString();

                    }

                    ReceiverObject obj = new ReceiverObject(email, uid, false);
                    if (!results.contains(obj)) {
                        results.add(obj);
                        //After the data has changed we need to notify the adapter.
                        mAdapter.notifyDataSetChanged();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    //Save Captures to Firebase - Storage
    public void saveToStories(){
        final DatabaseReference userStoryDb = FirebaseDatabase.getInstance().getReference().child("users").child(Uid).child("story");
        final String key = userStoryDb.push().getKey();

        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("captures").child(key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos );

        byte[] dataToUpload = baos.toByteArray();
        UploadTask uploadTask = filePath.putBytes(dataToUpload);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
                return;
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri imageURL = taskSnapshot.getDownloadUrl();


                Long currentTimeStamp = System.currentTimeMillis();
                Long endTimeStamp = currentTimeStamp + (24*60*60*1000); //url valid for 24 hours


                CheckBox mStory  = findViewById(R.id.story);
                if(mStory.isChecked()){
                    Map<String, Object> mapToUpload = new HashMap<>();
                    mapToUpload.put("imageUrl", imageURL.toString());
                    mapToUpload.put("timestampBeg", currentTimeStamp);
                    mapToUpload.put("timestampEnd", endTimeStamp);

                    userStoryDb.child(key).setValue(mapToUpload);
                }

                for(int i = 0; i < results.size(); i++){
                    if(results.get(i).getReceive()){
                        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users").child(results.get(i).getUid()).child("received").child(Uid);
                        Map<String, Object> mapToUpload = new HashMap<>();
                        mapToUpload.put("imageUrl", imageURL.toString());
                        mapToUpload.put("timestampBeg", currentTimeStamp);
                        mapToUpload.put("timestampEnd", endTimeStamp);

                        userDb.child(key).setValue(mapToUpload);
                    }
                }

                //clear the activity and go back to the MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        });

    }

}
