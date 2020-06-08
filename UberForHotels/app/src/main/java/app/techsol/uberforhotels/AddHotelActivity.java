package app.techsol.uberforhotels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.AddHotelModel;

public class AddHotelActivity extends AppCompatActivity {
    DatabaseReference HotelRef;
    EditText HotelNameET, HotelLat, HotelLong;
    String HotelNameStr, HotelLatStr, HotelLongStr;
    Button mAddBtn;
    LinearLayout mgetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);
        HotelRef= FirebaseDatabase.getInstance().getReference("Hotels");



        mgetView=findViewById(R.id.getView);
        HotelNameET=findViewById(R.id.HotelNameET);
        HotelLong=findViewById(R.id.HotelLong);
        HotelLat=findViewById(R.id.HotelLat);
        mAddBtn=findViewById(R.id.addHotelBtn);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStrings ();
                AddHotelModel model=new AddHotelModel(HotelNameStr, HotelLatStr, HotelLongStr);
                HotelRef.push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Snackbar.make(mgetView, "Hotel added Successfully", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddHotelActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    void getStrings (){
        HotelNameStr=HotelNameET.getText().toString();
        HotelLatStr=HotelLat.getText().toString();
        HotelLongStr=HotelLong.getText().toString();
    }



}
