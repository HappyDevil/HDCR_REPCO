package com.repandco.repco.mainActivities;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.FirebaseConfig;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.Values;

import static com.repandco.repco.constants.Toasts.ERR_INTERNET;
import static com.repandco.repco.constants.URLS.USERS;

public class ProfileFragment extends Fragment {

    private ManagerActivity manager;
    private TextView usename;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_profile).setChecked(true);

        String uid = this.getArguments().getString(Keys.UID);
        final View context = inflater.inflate(R.layout.activity_scrolling, container,false);
        if(uid!=null) {
            if(context!=null) {
                View content = context.findViewById(R.id.content);
                if (content != null) usename = (TextView) content.findViewById(R.id.username);
                if (usename != null)
                {
                    DatabaseReference reference = FirebaseConfig.mDatabase.getReference();
                    reference.child(USERS+uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long type = (long) dataSnapshot.child("type").getValue();
                            if(type== Values.TYPES.PROFESSIONAL_TYPE){
                                String name = dataSnapshot.child("name").getValue(String.class)+dataSnapshot.child("firstname").getValue(String.class);
                                usename.setText(name);
                            }
                            else {
                                if(type == Values.TYPES.ENTERPRISE_TYPE){
                                    String name = dataSnapshot.child("name").getValue(String.class);
                                    usename.setText(name);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(context.getContext(),ERR_INTERNET,Toast.LENGTH_SHORT).show();
                        }
                    });
                    usename.setText(uid);
                }
            }
        }

        return context;
    }

    public ManagerActivity getManager() {
        return manager;
    }

    public void setManager(ManagerActivity manager) {
        this.manager = manager;
    }

}
