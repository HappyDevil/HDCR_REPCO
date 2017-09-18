package com.repandco.repco;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;


public class FirebaseConfig{

    public static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static final FirebaseDatabase  mDatabase = FirebaseDatabase.getInstance();
    public static final FirebaseStorage  mStorage = FirebaseStorage.getInstance();

}
