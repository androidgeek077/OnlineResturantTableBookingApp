package app.techsol.uberforhotels;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;

import Models.TableModel;
import Models.UserModel;


public class AddTableActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 1;
    KProgressHUD progressDialog;
    Button mSelectedImgBtn;
    ImageView profileImageView;
    String downloadUri;
    LinearLayout view;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    StorageReference profilePicRef;
    TableModel model;
    DatabaseReference registerStudent;
    FirebaseAuth mAuth;
    EditText TableNoET, NoOfChairs, TableRentET;
    RadioGroup InternetRdoGrp;
    RadioButton mSelectedRB;
    ImageView mProfilePic;
    Double StdLatDouble = 0.0;
    Double StdLongDouble = 0.0;
    String RoomStr, NoOfChairsStr, RoomRentStr, Phone, ImgUrl;
    UserModel Model;
    private StorageReference mProfilePicStorageReference;
    private Uri selectedProfileImageUri;
    private String InternetStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);
        databaseReference = FirebaseDatabase.getInstance().getReference("Table");
        mProfilePicStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        TableNoET = findViewById(R.id.TableNoET);
        view = findViewById(R.id.ll);
        NoOfChairs = findViewById(R.id.NoOfChairs);
        TableRentET = findViewById(R.id.TableRentET);
        InternetRdoGrp = findViewById(R.id.InternetRdoGrp);


        Button btnSignUp = findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                RoomStr = TableNoET.getText().toString();
                NoOfChairsStr = NoOfChairs.getText().toString();
                RoomRentStr = TableRentET.getText().toString();
                int selectedOptionStr = InternetRdoGrp.getCheckedRadioButtonId();
                mSelectedRB = findViewById(selectedOptionStr);
                InternetStr = mSelectedRB.getText().toString();


                if (RoomStr.isEmpty()) {
                    TableNoET.setError("Please Enter RoomStr First");
                } else if (NoOfChairsStr.isEmpty()) {
                    NoOfChairs.setError("please fill NoOfChairs");
                } else if (RoomRentStr.isEmpty()) {
                    TableRentET.setError("Please enter RoomRentStr");
                } else {

                    progressDialog = KProgressHUD.create(AddTableActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setAnimationSpeed(2)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setLabel("Uploading")
                            .setDetailsLabel("Please Wait...")
                            .setDimAmount(0.3f)
                            .show();

                    uploadProduct();
                }


            }


        });


    }


    public void uploadProduct() {
        String pushid = databaseReference.push().getKey();

        model = new TableModel(RoomStr, NoOfChairsStr, InternetStr, RoomRentStr, pushid, "available for book", "Not Booked Yet");
        databaseReference.child(pushid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                final Snackbar snackbar = Snackbar.make(view, "Table Added Successfully", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AddTableActivity.this, DashboardActivity.class));
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
