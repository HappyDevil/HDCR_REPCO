package com.repandco.repco;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.repandco.repco.adapter.FindsAdapter;
import com.repandco.repco.adapter.ImagesAdapter;
import com.repandco.repco.adapter.TagsAdapter;
import com.repandco.repco.customClasses.LoadPhotoAct;



public class CreateJobPost extends LoadPhotoAct {

    private ManagerActivity manager;
    private EditText search;
    private RecyclerView finds;
    private boolean submit = false;
    private FindsAdapter findsAdapter;
    private RecyclerView tags_list;
    private RecyclerView photos;
    private TagsAdapter tagsAdapter;
    private CardView find_card;
    private CardView tags_card;
    private Button create;
    private EditText text;
    private Toolbar postTolbar;
    private EditText title;
    private ImagesAdapter imagesAdapter;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private Spinner spinner;
    private Spinner spinner1;
    private Spinner spinner2;
    private EditText price;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job_post);

        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        postTolbar.setTitle("Create post");
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        finds = (RecyclerView) findViewById(R.id.finds);
        search = (EditText) findViewById(R.id.search);
        text = (EditText) findViewById(R.id.text);
        title = (EditText) findViewById(R.id.title);
        tags_list = (RecyclerView) findViewById(R.id.tags_list);
        photos = (RecyclerView) findViewById(R.id.photos);
        create = (Button) findViewById(R.id.create);
        find_card = (CardView) findViewById(R.id.find_card);
        tags_card = (CardView) findViewById(R.id.tags_card);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton = (RadioButton) findViewById(R.id.radioButtonCDD);
        radioButton1 = (RadioButton) findViewById(R.id.radioButtonCDI);
        radioButton2 = (RadioButton) findViewById(R.id.radioButtonEXTRA);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner5);
        spinner2 = (Spinner) findViewById(R.id.spinner6);
        price = (EditText) findViewById(R.id.editText);


    }

}
