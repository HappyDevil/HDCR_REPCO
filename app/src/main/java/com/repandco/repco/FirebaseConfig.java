package com.repandco.repco;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseConfig{

    public static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static final FirebaseDatabase  mDatabase = FirebaseDatabase.getInstance();

}
