package com.example.admiralfresh.intentassignment;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
//static final String ACTION_TIMETRAVEL = "com.example.action.TIMETRAVEL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText eTxt = (EditText) findViewById(R.id.editText);
        final TextView myTextView =  (TextView) findViewById(R.id.textView);

        eTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myTextView.setText(eTxt.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle extraz = data.getExtras();
        float tSize = extraz.getFloat("FONT_SIZE");
            tSize = tSize / getResources().getDisplayMetrics().scaledDensity; // Must convert pixels (tSize) to sp
        int tStyle = extraz.getInt("FONT_STYLE");
        int tColor = extraz.getInt("FONT_COLOR");
        String tTF = extraz.getString("FONT_TYPEFACE");

        TextView myTextView = (TextView) findViewById(R.id.textView);
        Typeface myTF = returnTypeFace(tTF);

        myTextView.setTypeface(myTF, tStyle);
        myTextView.setTextColor(tColor);
        myTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, tSize);

    }

        public Typeface returnTypeFace(String typeFString) {

            if (typeFString.equals("DEFAULT")) {
                Typeface myTypeface = Typeface.create(Typeface.DEFAULT, 0);
                return myTypeface;
            }
            if (typeFString.equals("DEFAULT_BOLD") ){
                Typeface myTypeface = Typeface.create(Typeface.DEFAULT_BOLD, 0);
                return myTypeface;
            }
            if (typeFString.equals("MONOSPACE")) {
                Typeface myTypeface = Typeface.create(Typeface.MONOSPACE, 0);
                return myTypeface;
            }
            if (typeFString.equals("SAN_SERIF") ){
                Typeface myTypeface = Typeface.create(Typeface.SANS_SERIF, 0);
                return myTypeface;
            }
            if (typeFString.equals("SERIF") ){
                Typeface myTypeface = Typeface.create(Typeface.SERIF, 0);
                return myTypeface;
            }
            else {
                //TO DO exception
                Typeface myTypeface = Typeface.create(Typeface.DEFAULT, 0);
                return myTypeface;
            }
        }


        public void goToAct2(View view){

        Intent myIntent = new Intent("com.example.admiralfresh.fontchoose.ACTION_RETRIEVE_FONT");

        startActivityForResult(myIntent, 0);
        }
}
