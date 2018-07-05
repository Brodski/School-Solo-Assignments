package com.example.admiralfresh.flashandstuff;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.os.Vibrator;

public class MainActivity extends AppCompatActivity {

    private  String mCameraId;
    private boolean isTorchOn;
    Camera camera;
    Camera.Parameters parameters;
    boolean runThis;
    int countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   //     checkIfLight();
        isTorchOn = false;

         camera = Camera.open();
        parameters = camera.getParameters();

        Button flashButton = (Button) findViewById(R.id.FlashButton);

    /*
        flashButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!isTorchOn) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                    isTorchOn = true;
                }
                else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                    isTorchOn = false;

                }
                return;
            }
        });

    */
    }
    public  void onFlashClick(View view) {


        if (!isTorchOn) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            isTorchOn = true;
        }
        else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            isTorchOn = false;

        }
        return;
    }

    public void onVibrateButton(View view) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);

    }

    public void onBeepButton(View view){
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000);

    }

    public void doAllButton(View view){

        Runnable r = new Runnable() {
            @Override
            public void run() {

                Button flashButton = (Button) findViewById(R.id.FlashButton);
                flashButton.performClick();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);

                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000);

                countdown = countdown - 1;
                if (countdown == 0) {
                    runThis = false;
                }
            }
        };
        Handler h = new Handler();
        //while (runThis = true) {
        h.postDelayed(r, 0);
        h.postDelayed(r, 1500);
        h.postDelayed(r, 3000);
        h.postDelayed(r, 4500);
       // }

    }




    public void checkIfLight() {
        CameraManager mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Context mCont = getApplicationContext();
        CameraManager mCamMang = (CameraManager) mCont.getSystemService(Context.CAMERA_SERVICE);

        boolean existanceOfFlash = getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!existanceOfFlash) {
            Toast.makeText(this, "There is no flash feature on this camera!", Toast.LENGTH_SHORT).show();
            //isLightOn = true;
            return;
        } else {
            Toast.makeText(this, "There IS, yes, flash feature on this camera!", Toast.LENGTH_SHORT).show();
            //isLightOn = false;
            return;
        }
    }

}
