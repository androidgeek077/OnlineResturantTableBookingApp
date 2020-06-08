package app.techsol.uberforhotels;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import Models.RoomModel;
import Models.UserModel;


public class AddRoomActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 1;
    KProgressHUD progressDialog;
    Button mSelectedImgBtn;
    ImageView profileImageView;
    String downloadUri;
    LinearLayout view;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    StorageReference profilePicRef;
    RoomModel model;
    DatabaseReference registerStudent;
    FirebaseAuth mAuth;
    EditText RoomNoET, NoOfBeds, RoomRentET;
    RadioGroup InternetRdoGrp;
    RadioButton mSelectedRB;
    ImageView mProfilePic;
    Double StdLatDouble = 0.0;
    Double StdLongDouble = 0.0;
    String RoomStr, NoofBeds, RoomRentStr, Phone, ImgUrl;
    UserModel Model;
    private StorageReference mProfilePicStorageReference;
    private Uri selectedProfileImageUri;
    private Button btnSignUp;
    private Button mSelectImgBtn;
    private String InternetStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        databaseReference = FirebaseDatabase.getInstance().getReference("Room");
        registerStudent = FirebaseDatabase.getInstance().getReference("Room");
        mProfilePicStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        RoomNoET = findViewById(R.id.RoomNoET);
        view = findViewById(R.id.ll);
        NoOfBeds = findViewById(R.id.NoOfBeds);
        RoomRentET = findViewById(R.id.RoomRentET);
        InternetRdoGrp = findViewById(R.id.InternetRdoGrp);

        mProfilePic = findViewById(R.id.selectedImg);
        mSelectImgBtn = findViewById(R.id.btn_selectimg);
        mSelectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfilePicture();
            }
        });


        btnSignUp = findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                RoomStr = RoomNoET.getText().toString();
                NoofBeds = NoOfBeds.getText().toString();
                RoomRentStr = RoomRentET.getText().toString();
                int selectedOptionStr = InternetRdoGrp.getCheckedRadioButtonId();
                mSelectedRB=findViewById(selectedOptionStr);
                InternetStr=mSelectedRB.getText().toString();


                if (RoomStr.isEmpty()) {
                    RoomNoET.setError("Please Enter RoomStr First");
                } else if (NoofBeds.isEmpty()) {
                    NoOfBeds.setError("please fill NoofBeds");
                } else if (RoomRentStr.isEmpty()) {
                    RoomRentET.setError("Please enter RoomRentStr");
                } else {

                    progressDialog = KProgressHUD.create(AddRoomActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setAnimationSpeed(2)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setLabel("Authenticating")
                            .setDetailsLabel("Please Wait...")
                            .setDimAmount(0.3f)
                            .show();

                    profilePicRef = mProfilePicStorageReference.child(selectedProfileImageUri.getLastPathSegment());
                    profilePicRef.putFile(selectedProfileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri = uri.toString();
                                    uploadProduct(downloadUri);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddRoomActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }


        });


    }

    public void getProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            selectedProfileImageUri = selectedImageUri;
            mProfilePic.setImageURI(selectedImageUri);
            mProfilePic.setVisibility(View.VISIBLE);
        }

    }

    public void uploadProduct(String ImageUrl) {
        String pushid=databaseReference.push().getKey();

        model = new RoomModel(RoomStr, NoofBeds,InternetStr, RoomRentStr, Phone, ImageUrl,pushid, "available for stay");
        databaseReference.child(pushid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                mProfilePic.setVisibility(View.GONE);
                final Snackbar snackbar = Snackbar.make(view, "Room Added Successfully", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AddRoomActivity.this, MainActivity.class));
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }


}
