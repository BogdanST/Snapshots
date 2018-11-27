package a300.cem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.BitSet;

public class ShowCaptureActivity extends AppCompatActivity {

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
            Bitmap rotateBitmap = rotate(decodeBitmap);

            //Show Picture
            image.setImageBitmap(rotateBitmap);


        }

    }

    public Bitmap rotate(Bitmap decodeBitmap) {
        int w = decodeBitmap.getWidth();
        int h = decodeBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(decodeBitmap, 0, 0, w,h,matrix,true );

    };
}
