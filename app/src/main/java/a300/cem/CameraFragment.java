package a300.cem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {

    Camera camera;
    Camera.PictureCallback jpegCallback;

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    final int CAMERA_REQUEST_CODE = 1;

    public static CameraFragment newInstance(){
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment , container, false);

        mSurfaceView = view.findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        //Set Permission to access camera
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }else{
            //Bridge gap between SurfaceHolder and SurfaceView
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        //BUTTONS
        Button mLogout = view.findViewById(R.id.logout);
        Button mCapture = view.findViewById(R.id.capture);


        //Click -> LogOut the user
        mLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LogOut();
            }
        });

        mCapture.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                captureImage();
            }
        });

        jpegCallback = new Camera.PictureCallback(){
            public void onPictureTaken(byte[] bytes, Camera camera){
                Intent intent = new Intent(getActivity(), ShowCaptureActivity.class );
                intent.putExtra("capture", bytes);
                startActivity(intent);
                return;
            }
        };

        return view;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Check if the camera is opened
        camera = Camera.open();

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


    //Getting permissions -> request Result
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

}