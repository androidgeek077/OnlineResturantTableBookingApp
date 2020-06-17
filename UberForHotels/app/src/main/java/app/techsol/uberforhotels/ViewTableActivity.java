package app.techsol.uberforhotels;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;

import Models.TableModel;

public class ViewTableActivity extends AppCompatActivity  {
    DatabaseReference TableRef, UserRef;
    RecyclerView mTableRV;
    int value;
    private FirebaseAuth mAuth;
    private String UserType;
    PreferenceManager manager;
    private Dialog dialog;
    private EditText AddTimeET;
    private String TimeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_table);
        manager=new PreferenceManager(getApplicationContext());
        TableRef = FirebaseDatabase.getInstance().getReference("Table");
        UserRef = FirebaseDatabase.getInstance().getReference("user");

        mAuth=FirebaseAuth.getInstance();

        mTableRV = findViewById(R.id.recycler_vw_tables);

        getUserType();
       
        
//        loadData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mTableRV.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<TableModel> options = new FirebaseRecyclerOptions.Builder<TableModel>()
                .setQuery(TableRef, TableModel.class)
                .build();

        FirebaseRecyclerAdapter<TableModel, TableViewHolder> adapter = new FirebaseRecyclerAdapter<TableModel, TableViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final TableViewHolder holder, int position, @NonNull final TableModel model) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                holder.HotelName.setText(model.getTableno());
                holder.WifiAvlTV.setText(model.getInternetstatus());
                holder.NofBedsTV.setText(model.getNoofchairs());
                holder.RentTv.setText(model.getRent());
                holder.bookingTimingTV.setText(model.getReservationtime());


                if (model.getTablestatus().equals("available for book")){
                    holder.tablestatusTv.setBackground(getResources().getDrawable(R.color.reserved));
                } else if (model.getTablestatus().equals("reserved")  ){
                    holder.tablestatusTv.setBackground(getResources().getDrawable(R.color.checked_in));

                }else if (model.getTablestatus().equals("Checked-in")  ){
                    holder.tablestatusTv.setBackground(getResources().getDrawable(R.color.checked_out));

                }else if (model.getTablestatus().equals("Checked-out")  ){
                    holder.tablestatusTv.setBackground(getResources().getDrawable(R.color.available));
                }
                holder.tablestatusTv.setText(model.getTablestatus());
                holder.tablestatusTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (model.getTablestatus().equals("available for book")){
                            openDialog(model, holder);

                        } else if (model.getTablestatus().equals("reserved") && manager.getUserType().equals("admin") ){
                            TableRef.child(model.getPushid()).child("tablestatus").setValue("Checked-in");
                            holder.tablestatusTv.setBackground(getResources().getDrawable(R.color.checked_in));

                        }else if (model.getTablestatus().equals("Checked-in") && manager.getUserType().equals("admin") ){
                            TableRef.child(model.getPushid()).child("tablestatus").setValue("Checked-out");
                            holder.tablestatusTv.setBackground(getResources().getDrawable(R.color.checked_out));

                        }else if (model.getTablestatus().equals("Checked-out") && manager.getUserType().equals("admin") ){
                            TableRef.child(model.getPushid()).child("tablestatus").setValue("available for book");
                            holder.tablestatusTv.setBackground(getResources().getDrawable(R.color.available));
                            TableRef.child(model.getPushid()).child("reservationtime").setValue("Not Booked Yet");
                        } else {
                            Toast.makeText(ViewTableActivity.this, "Room is already booked", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }

            @NonNull
            @Override
            public TableViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_item_layout, viewGroup, false);
                return new TableViewHolder(view);
            }
        };

        mTableRV.setAdapter(adapter);
        adapter.startListening();
    }

//    void loadData() {
//
//
//
//
//    }

    
    

    static class TableViewHolder extends RecyclerView.ViewHolder {


        TextView HotelName, WifiAvlTV, NofBedsTV, RentTv, tablestatusTv, bookingTimingTV;




        TableViewHolder(@NonNull View itemView) {
            super(itemView);

            HotelName = itemView.findViewById(R.id.RoomNoTV);
            WifiAvlTV = itemView.findViewById(R.id.WifiAvlTV);
            NofBedsTV = itemView.findViewById(R.id.NofBedsTV);
            RentTv = itemView.findViewById(R.id.RentTv);
            tablestatusTv = itemView.findViewById(R.id.tablestatusTv);
            bookingTimingTV = itemView.findViewById(R.id.bookingTimingTV);
        }
    }
    void getUserType() {
        UserRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserType=dataSnapshot.child("usertype").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog(final TableModel model, final TableViewHolder holder) {
        dialog = new Dialog(ViewTableActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_box);
        dialog.setTitle("Add Time");
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_AppCompat_DayNight_Dialog_Alert;
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.setCancelable(true);
        AddTimeET = dialog.findViewById(R.id.AddTimeET);
        Button cancelBtn = dialog.findViewById(R.id.CancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button addPlantBtn = dialog.findViewById(R.id.addPlantBtn);
        addPlantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeStr=AddTimeET.getText().toString();
                String TableId=model.getPushid();
//                TableModel tableModel=new TableModel(null,null, null, null, null,null, TimeStr);
                TableRef.child(TableId).child("reservationtime").setValue(TimeStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            TableRef.child(model.getPushid()).child("tablestatus").setValue("reserved");
                            holder.tablestatusTv.setBackground(getResources().getDrawable(R.color.reserved));
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), "Table Reserved Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }

}
