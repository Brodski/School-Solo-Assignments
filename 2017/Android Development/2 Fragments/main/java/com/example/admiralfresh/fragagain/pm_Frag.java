package com.example.admiralfresh.fragagain;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Admiral Fresh on 2/9/2017.
 */

public class pm_Frag extends Fragment {

    // @Override
    //  public void OnCreate(Bundle savedInstaceStage){
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //    View myView = inflater(R.layout.pmfrag_xml, container, false);
        String msg = getArguments().getString("sendFromLM");
        View v = inflater.inflate(R.layout.pmfrag_xml, container, false);

        TextView pmTextView = (TextView) v.findViewById(R.id.pmOutput);
        if ( msg != "This is Frag 1!") {
             msg = getArguments().getString("sendFromLM");
            pmTextView.setText(msg);
        }
        Button pmButton = (Button) v.findViewById(R.id.pmButtonID);
        pmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                //String t = "Toasyt"
                //  Toast.makeText(getActivity(), t, Toast.LENGTH_LONG).show();

                EditText pmEditText = (EditText) getView().findViewById(R.id.pmEditTextID);
                String pmUserIn = pmEditText.getText().toString();
                Bundle argz = new Bundle();
                argz.putString("sendFromPM", pmUserIn);
                lm_Frag myLM_frag = new lm_Frag();
                myLM_frag.setArguments(argz);

                android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(android.R.id.content, myLM_frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //TextView lmTextView = (TextView) getView().findViewById(R.id.lmOutput);
                //String msg = getArguments().getString(pmUserIn);
                //lmTextView.setText(msg);
                }
            });
        return v;
    }

}

