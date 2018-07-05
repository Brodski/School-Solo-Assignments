package com.example.admiralfresh.fragagain;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Admiral Fresh on 2/9/2017.
 */

public class lm_Frag extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.lmfrag_xml,container,false);
        TextView lmTextView = (TextView) v.findViewById(R.id.lmOutput);

        String msg = getArguments().getString("sendFromPM");
       lmTextView.setText(msg);

        Button lmButton = (Button) v.findViewById(R.id.lmButtonID);
        lmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                //String t = "Toasyt"
                //  Toast.makeText(getActivity(), t, Toast.LENGTH_LONG).show();

                EditText lmEditText = (EditText) getView().findViewById(R.id.lmEditTextID);
                String lmUserIn = lmEditText.getText().toString();
                Bundle argz2 = new Bundle();
                argz2.putString("sendFromLM", lmUserIn);
                pm_Frag myPM_frag = new pm_Frag();
                myPM_frag.setArguments(argz2);

                android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(android.R.id.content, myPM_frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //TextView lmTextView = (TextView) getView().findViewById(R.id.pmOutput);
                //String msg = getArguments().getString(pmUserIn);
                //lmTextView.setText(msg);
            }
        });


    return v;

    }

}
