package com.example.myfirstapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

//DOWNLOAD


//NEED TO DOWNLOAD FIREBASE DATABASE AND STORAGE
//This is where we take care of business logic
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerGenres;

    //Create connection to Database
    DatabaseReference databaseArtists;

    //Image Viewing and Uploading
    Button buttonChoose, buttonUpload;
    ImageView imageView;
    Uri filePath;

    //Viewing Recording page
    Button buttonAudioRecord;


    private static final int PICK_IMAGE_REQUEST = 234;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Show the xml page
        setContentView(R.layout.activity_main);

        //Setting up database fire
        /*
        databaseArtists = FirebaseDatabase.getInstance().getReference("artist");
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtist();
            }
        });
        */


        //Finding image
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    //ADDING MANDY'S CODE

    public void jumpToRecording(View view){
        try {
            Intent intent = new Intent(this, AudioRecord.class);
            startActivity(intent);
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    //To upload the file
    private void uploadFile(){

        if(filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading ... ");
            progressDialog.show();

            StorageReference imageRef = storageReference.child("images/evidence.jpg");

            imageRef.putFile(filePath).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    //Handle successful uploads
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "File Uploaded.", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progress) + "% uploaded ...");
                }
            }) ;
        }
        else{
            //Display an error toast

        }

    }

  /*
    private void addArtist(){
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenres.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name)){
            //POST Method
            String id= databaseArtists.push().getKey();
            Artist artist = new Artist(id, name, genre);
            databaseArtists.child(id).setValue(artist);
            Toast.makeText(this, "Artist Added.", Toast.LENGTH_LONG).show();

        }
        else{
            //Notification on the bottom
            Toast.makeText(this, "You should enter a name.", Toast.LENGTH_LONG).show();
        }

    }
    */

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image: "), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Load image
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view){
        //Listens to whatever button was clicked
        if(view == buttonChoose){
            //Open file chooser
            showFileChooser();
        }
        else if(view == buttonUpload){
            //upload file to Firebase Storage
            uploadFile();
        }

    }

    //Tiffany's Code
    public void linkBrowser(View view){
        Uri uri = Uri.parse("http://www.thehotline.org/what-is-live-chat/");
        Intent linkIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(linkIntent);
    }

}
