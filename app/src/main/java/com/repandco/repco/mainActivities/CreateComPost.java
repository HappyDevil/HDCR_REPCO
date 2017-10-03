package com.repandco.repco.mainActivities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.adapter.FindsAdapter;
import com.repandco.repco.adapter.ImagesAdapter;
import com.repandco.repco.adapter.TagsAdapter;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.Post;

import java.util.ArrayList;
import java.util.TreeSet;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;


public class CreateComPost extends Fragment {
    private ManagerActivity manager;
    private EditText search;
    private RecyclerView finds;
    private boolean submit = false;
    private FindsAdapter findsAdapter;
    private RecyclerView tags_list;
    private RecyclerView photos;
    private TagsAdapter adapter;
    private Button create;
    private EditText text;
    private EditText title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_dashboard).setChecked(true);

        View content = inflater.inflate(R.layout.fragment_create_com_post, container, false);

        finds = (RecyclerView) content.findViewById(R.id.finds);
        search = (EditText) content.findViewById(R.id.search);
        text = (EditText) content.findViewById(R.id.text);
        title = (EditText) content.findViewById(R.id.title);
        tags_list = (RecyclerView) content.findViewById(R.id.tags_list);
        photos = (RecyclerView) content.findViewById(R.id.photos);
        create = (Button) content.findViewById(R.id.create);

        tags_list.setHasFixedSize(false);
        RecyclerView.LayoutManager  tagsLayoutManager = new LinearLayoutManager(content.getContext(),LinearLayoutManager.HORIZONTAL,false);
        tags_list.setLayoutManager(tagsLayoutManager);

        adapter = new TagsAdapter(manager);
        adapter.setCreate(true);
        tags_list.setAdapter(adapter);

        photos.setHasFixedSize(false);
        RecyclerView.LayoutManager  photosLayoutManager = new LinearLayoutManager(content.getContext(),LinearLayoutManager.HORIZONTAL,false);
        photos.setLayoutManager(photosLayoutManager);

        ImagesAdapter imagesAdapter = new ImagesAdapter(new ArrayList<String>(),manager);
        imagesAdapter.addPlus();


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String searchText = charSequence.toString();
                if(searchText.length()>0){
                    mDatabase.getReference().child(URLS.TAGS).orderByKey().startAt(searchText).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            TreeSet<String> keys = new TreeSet<String>();
                            for (DataSnapshot data: dataSnapshot.getChildren()) {
                                String key = data.getKey();
                                if(!key.startsWith(searchText)) break;
                                keys.add(key);
                            }
                            findsAdapter.setFinds(keys);
                            if(!submit) finds.setVisibility(View.VISIBLE);
                            else submit = false;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            submit = true;
                            finds.setVisibility(View.GONE);
                        }
                    });
                }
                else finds.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        findsAdapter = new FindsAdapter(manager);
        findsAdapter.setCreate(true);
        findsAdapter.setTagsAdapter(adapter);
        finds.setLayoutManager(new LinearLayoutManager(content.getContext(),LinearLayoutManager.VERTICAL,false));
        finds.setAdapter(findsAdapter);

        finds.setVisibility(View.GONE);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post();
                post.setText(text.getText().toString());
                post.setTitle(title.getText().toString());
                post.setType(Values.POSTS.STANDARD_POST);
                post.setUserid(mAuth.getCurrentUser().getUid());
                post.setTags(adapter.getTags());
                mDatabase.getReference().child(URLS.POSTS).push();
            }
        });
        return content;
    }

    public ManagerActivity getManager() {
        return manager;
    }

    public void setManager(ManagerActivity manager) {
        this.manager = manager;
    }
}
