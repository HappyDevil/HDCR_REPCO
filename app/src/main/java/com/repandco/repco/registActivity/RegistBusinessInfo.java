package com.repandco.repco.registActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.repandco.repco.FirstActivity;
import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;

public class RegistBusinessInfo extends AppCompatActivity {

    private Intent intent;
    private EditText bact;
    private EditText bname;
    private EditText siret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_business_info);

        intent = getIntent();

        bact = (EditText) findViewById(R.id.bact);
        bname = (EditText) findViewById(R.id.bname);
        siret = (EditText) findViewById(R.id.siret);
    }

    public void next(View view) {
        if(intent!=null)
        {
            intent.putExtra(Keys.BACT,bact.getText().toString());
            intent.putExtra(Keys.NAME,bname.getText().toString());
            intent.putExtra(Keys.SIRET,siret.getText().toString());
            intent.setClass(this,RegistBusinessContacts.class);
        }
        else intent = new Intent(this, FirstActivity.class);
        startActivity(intent);

    }
}
