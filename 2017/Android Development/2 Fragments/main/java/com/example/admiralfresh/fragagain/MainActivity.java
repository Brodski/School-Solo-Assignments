package com.example.admiralfresh.fragagain;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(msg, "The onCreate() event");
        Configuration config = getResources().getConfiguration();

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

     //   if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
   //         lm_Frag myLM_frag = new lm_Frag();
    //        fragmentTransaction.replace(android.R.id.content, myLM_frag);
//
  //      }
  //      else {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
          setContentView(R.layout.frag_layout);
          /*   lm_Frag myLM_frag = new lm_Frag();
            fragmentTransaction.replace(android.R.id.content, myLM_frag);
            Bundle argz = new Bundle();
            String xD = "This is Frag 2";
            argz.putString("sendFromPM", xD);
            myLM_frag.setArguments(argz);
        */
        }

        else{
            pm_Frag myPM_frag = new pm_Frag();
            fragmentTransaction.replace(android.R.id.content, myPM_frag);
            Bundle argz = new Bundle();
            String xD = "This is Frag 1!";
            argz.putString("sendFromLM", xD);
            myPM_frag.setArguments(argz);
        }

        fragmentTransaction.commit();

        // setContentView(R.layout.activity_main);
    }

    String msg ="hi Android :";

    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
    }


    public void startService(View view) {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }
}