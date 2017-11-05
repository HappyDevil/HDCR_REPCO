package com.repandco.repco.customClasses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.repandco.repco.adapter.ImagesAdapter;

import static com.repandco.repco.FirebaseConfig.mStorage;
import static com.repandco.repco.constants.URLS.IMAGES;
import static com.repandco.repco.constants.Values.REQUEST.LOAD_POST_PHOTO;

public class LoadPhotoAct extends AppCompatActivity {
    private ImagesAdapter imagesAdapter;
    private Context context;

    public void loadPhoto(final ImagesAdapter imagesAdapter){
        this.imagesAdapter = imagesAdapter;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, LOAD_POST_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            context = this;//
            if(imagesAdapter.create!=null) imagesAdapter.create.setActivated(false);
            final ImageViewLoader imageViewLoader = imagesAdapter.plus;
            if (data != null) {
                final Uri uri = data.getData();

                if (requestCode == LOAD_POST_PHOTO){
                    if(imageViewLoader.getImageView() != null) imageViewLoader.getImageView().setImageURI(uri);
                }
                imagesAdapter.plus.getProgressBar().setVisibility(View.VISIBLE);

                mStorage.getReference(IMAGES).child(uri.getLastPathSegment()).putFile(uri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
//                                    imageViewLoader.setLoadPhotoUrl();
                                    imagesAdapter.plus.getProgressBar().setVisibility(View.GONE);
                                    imagesAdapter.convertPlus(task.getResult().getMetadata().getDownloadUrl().toString());
                                    imagesAdapter.plus.getImageView().setTag(task.getResult().getMetadata().getDownloadUrl().toString());
                                    imagesAdapter.addPlus();
                                    Toast.makeText(context,"SUCSESS",Toast.LENGTH_SHORT).show();
                                    if(imageViewLoader.getProgressBar()!=null) imageViewLoader.getProgressBar().setVisibility(View.INVISIBLE);
                                    if(imagesAdapter.create!=null) imagesAdapter.create.setActivated(true);
                                }
                                else {
                                    Toast.makeText(context,"Load error",Toast.LENGTH_SHORT).show();
                                    if(imageViewLoader.getProgressBar()!=null)  imageViewLoader.getProgressBar().setVisibility(View.INVISIBLE);
                                    imageViewLoader.setImageURI(null);
                                    if(imagesAdapter.create!=null) imagesAdapter.create.setActivated(true);
                                }
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                if(imageViewLoader.getProgressBar()!=null) imageViewLoader.getProgressBar().setProgress((int) progress);
                            }
                        });
            }
        }
    }
}
