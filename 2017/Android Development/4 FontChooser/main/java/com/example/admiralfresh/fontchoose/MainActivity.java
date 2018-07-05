package com.example.admiralfresh.fontchoose;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DisplayContext;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView theetext = (TextView) findViewById(R.id.TheeText);
        final EditText eText = (EditText) findViewById(R.id.editText);

        Spinner typeSpinner = (Spinner) findViewById(R.id.typeSpinnerID);
        final Spinner styleSpinner = (Spinner) findViewById(R.id.styleSpinnerID);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.stylez, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.typez, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        styleSpinner.setAdapter(adapter);
        typeSpinner.setAdapter(adapter2);

        styleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                int selection = pos;
                if (pos == 0) {
                    theetext.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                }
                if (pos == 1) {
                    theetext.setTypeface(theetext.getTypeface(), Typeface.BOLD);
                }
                if (pos == 2) {
                    theetext.setTypeface(theetext.getTypeface(), Typeface.ITALIC);
                }
                if (pos == 3) {
                    theetext.setTypeface(theetext.getTypeface(), Typeface.BOLD_ITALIC);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos == 0) {
                    theetext.setTypeface(Typeface.DEFAULT, theetext.getTypeface().getStyle());
                }
                if (pos == 1) {
                    theetext.setTypeface(Typeface.DEFAULT_BOLD, theetext.getTypeface().getStyle() );
                }
                if (pos == 2) {
                    theetext.setTypeface(Typeface.MONOSPACE, theetext.getTypeface().getStyle());
                }
                if (pos == 3) {
                    theetext.setTypeface(Typeface.SANS_SERIF, theetext.getTypeface().getStyle());
                }
                if (pos == 4) {
                    theetext.setTypeface(Typeface.SERIF, theetext.getTypeface().getStyle());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        eText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            theetext.setText(eText.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        theetext.setTextColor(Color.rgb(0,0,0));

        seekbarz();
        colorbarz();
    }

    public void seekbarz(){

        final SeekBar sizeBar = (SeekBar) findViewById(R.id.seekFontSize);
        final TextView theetext = (TextView) findViewById(R.id.TheeText);
        final EditText eText = (EditText) findViewById(R.id.editText);

        sizeBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //    theetext.setText(eText.getText().toString() + " : " + progress + " / " + sizeBar.getMax());
            theetext.setText(theetext.getText().toString() );
            theetext.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress+10);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    public void colorbarz(){
        final SeekBar redBar = (SeekBar) findViewById(R.id.RedBarID);
        final SeekBar GreenBar = (SeekBar) findViewById(R.id.GreenBarID);
        final SeekBar BlueBar = (SeekBar) findViewById(R.id.BlueBarID);
        final TextView theetext = (TextView) findViewById(R.id.TheeText);

        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            theetext.setTextColor(Color.rgb(progress, GreenBar.getProgress(), BlueBar.getProgress()));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        GreenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                theetext.setTextColor(Color.rgb(redBar.getProgress(), progress, BlueBar.getProgress()));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        BlueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                theetext.setTextColor(Color.rgb(redBar.getProgress(), GreenBar.getProgress(), progress) );
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    public void returnData(View view) {
        Intent myintent = getIntent();

        final TextView theetext = (TextView) findViewById(R.id.TheeText);
        String typeFstring = getTF_as_String(); // It turns out that returning a TypeFace object through a bundle is really hard. So I did a trick of returning the typeface as a string to identify it.
        float tSize = theetext.getTextSize();
        int tStyle = theetext.getTypeface().getStyle();
        int tColor = theetext.getCurrentTextColor();

       // Ideally, if the API for intents' Typeface existed, we could have something like;
        // myIntnet.putExtas(myStringIntent.INTENT_ACTION_FONT_SIZE);
        // Where myStringIntent.INTENT_ACTION_FONT_SIZE might equal something like "android.text.action.FONT_SIZE"
        myintent.putExtra("FONT_SIZE", tSize);
        myintent.putExtra("FONT_STYLE", tStyle);
        myintent.putExtra("FONT_COLOR", tColor);
        myintent.putExtra("FONT_TYPEFACE", typeFstring);

        setResult(RESULT_OK, myintent);
        finish();
    }

    public String getTF_as_String(){
        Spinner typeSpinner = (Spinner) findViewById(R.id.typeSpinnerID);
        int pos = typeSpinner.getSelectedItemPosition();
        if (pos == 0) {
            return "DEFAULT";
        }
        if (pos == 1) {
            return "DEFAULT_BOLD";
        }
        if (pos == 2) {
            return "MONOSPACE";
        }
        if (pos == 3) {
            return "SAN_SERIF";
        }
        if (pos == 4) {
            return "SERIF";
        }
        else
        //TO DO - exception
            return "error";
    }
}

