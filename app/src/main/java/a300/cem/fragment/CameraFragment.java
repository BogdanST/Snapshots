package a300.cem.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import a300.cem.FindUsersActivity;
import a300.cem.R;
import a300.cem.ShowCaptureActivity;
import a300.cem.SplashScreenActivity;

import static android.content.Context.LOCATION_SERVICE;
import static android.hardware.Camera.open;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {


    /*
    //Location
    private LocationManager locationManager;
    private LocationListener locationListener; */


    //camera
    Camera camera;
    Camera.PictureCallback jpegCallback;

    //Views
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    //PERMISSIONS
    final int CAMERA_REQUEST_CODE = 1;
    //final int LOCATION_REQUEST_CODE = 2;

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        mSurfaceView = view.findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        /*
        //GET THE LOCATION
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("My location", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        /*
            if (Build.VERSION.SDK_INT < 23) {
                Toast.makeText(getActivity(), "The android version is bellow marshmallow!", Toast.LENGTH_SHORT).show();

            } else {
                //Check if the permission was granted
                //get permission to access the location
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }*/


            //Get Permission to access camera
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            } else {
                //Bridge gap between SurfaceHolder and SurfaceView
                mSurfaceHolder.addCallback(this);
                mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }


        //BUTTONS
        Button mLogout = view.findViewById(R.id.logout);
        Button mCapture = view.findViewById(R.id.capture);
        Button mFindUser = view.findViewById(R.id.findUsers);


        //Click -> LogOut the user
        mLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LogOut();
            }
        });

        //captureImage
        mCapture.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                captureImage();
            }
        });

        mFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindUsers();
            }
        });

        jpegCallback = new Camera.PictureCallback(){
            public void onPictureTaken(byte[] bytes, Camera camera){

                //Decode the image
                Bitmap decodeBitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);


                //A function is require to rotate the image, after has been captured
                //Even if the Image was taken in a vertical view, the app will automatically reverse it to a landscape view
                //So in order to keep the orientation, I will implement a function that preserves the view
                Bitmap rotateBitmap = rotate(decodeBitmap);

                String fileLocation = SaveImageToStorage(rotateBitmap);
                //before starting the ShopCaptureActivity -> verify if we get the fileLocation
                if(fileLocation != null){
                    Intent intent = new Intent(getActivity(), ShowCaptureActivity.class );
                    startActivity(intent);
                    return;
                }

            }
        };

        return view;

    }

    //Save the file in a file system as a JPEG
    public String SaveImageToStorage(Bitmap bitmap){
        String filename = "imageToSend";
        try{
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = getContext().openFileOutput(filename,Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        }catch (Exception e){
            e.printStackTrace();
            filename = null;
        }
        return filename;
    }

    public Bitmap rotate(Bitmap decodeBitmap) {
        int w = decodeBitmap.getWidth();
        int h = decodeBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(decodeBitmap, 0, 0, w,h,matrix,true );

    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Check if the camera is opened
        camera = open();

        //Since I use the camera API instead of Camera2API, it is required to set some parameters
        Camera.Parameters parameters;
        parameters = camera.getParameters();

        //Set Display Orientation to 90 degrees
        camera.setDisplayOrientation(90);
        //Frames per sec - Set to 30 {by default the camera is set to 30 FPS}
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        //Automatically camera stretches the image, so in order to preserve the image a few parameters are required.
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
        bestSize = sizeList.get(0);
        for(int i = 1; i< sizeList.size(); i++){
            if((sizeList.get(i).width* sizeList.get(i).height) > (bestSize.width*bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }
        parameters.setPreviewSize(bestSize.width, bestSize.height);


        //camera.setParameters(parameters); -- is not required to set this manually,
        //                                     natively camera gets the parameters automatically

        try {
            camera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.startPreview();
    }


    //Check if the surface has changed
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    //Check if the surface has been destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }


    //Getting permissions -> request Result [CAMERA]
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        switch (requestCode){
            case CAMERA_REQUEST_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mSurfaceHolder.addCallback(this);
                    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }else{
                    Toast.makeText(getContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }

           /* case LOCATION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    }

                }
                break;
            }*/
        }
    }


    public void LogOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent  = new Intent(getContext(), SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return;
    }

    public void captureImage(){
        camera.takePicture(null, null, jpegCallback);
    }


    public void FindUsers(){
        Intent intent  = new Intent(getContext(), FindUsersActivity.class);
        startActivity(intent);
        return;

    }
}