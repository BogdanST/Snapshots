package a300.cem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class ShowCaptureActivity extends AppCompatActivity {

    String Uid;
    Bitmap rotateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_capture);


        //Getting image from Bundle
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        byte[] b = extras.getByteArray("capture");


        //Check if the byte is not NULL
        if(b!=null){
            //get the ImageView
            ImageView image = findViewById(R.id.image_capture);
            //Decode the image
            Bitmap decodeBitmap = BitmapFactory.decodeByteArray(b, 0 , b.length);


            //A function is require to rotate the image, after has been captured
            //Even if the Image was taken in a vertical view, the app will automatically reverse it to a landscape view
            //So in order to keep the orientation, I will implement a function that preserves the view
            rotateBitmap = rotate(decodeBitmap);

            //Show Picture
            image.setImageBitmap(rotateBitmap);

        }

        Uid = FirebaseAuth.getInstance().getUid();

        //Buttons:
        Button mStory = findViewById(R.id.story);

        mStory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveToStories();
            }
        });

    }

    //Save Captures to Firebase - Storage
    public void saveToStories(){
        final DatabaseReference userStoryDb = FirebaseDatabase.getInstance().getReference().child("users").child(Uid).child("story");
        final String key = userStoryDb.push().getKey();

        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("captures").child(key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos );

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

                Map<String, Object> mapToUpload = new HashMap<>();
                mapToUpload.put("imageUrl", imageURL.toString());
                mapToUpload.put("timestampBeg", currentTimeStamp);
                mapToUpload.put("timestampEnd", endTimeStamp);

                userStoryDb.child(key).setValue(mapToUpload);

                finish();
                return;
            }
        });

    }

    public Bitmap rotate(Bitmap decodeBitmap) {
        int w = decodeBitmap.getWidth();
        int h = decodeBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(decodeBitmap, 0, 0, w,h,matrix,true );

    };
}
