package app.techsol.uberforhotels;

import android.Manifest;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.RoomModel;

public class ViewRoomsActivity extends AppCompatActivity implements LocationListener {
    DatabaseReference HotelRef, UserRef;
    RecyclerView mRoomRV;
    Double latitudeDouble = 32.079380, LongitudeDouble = 72.675057;
    int value;
    private FirebaseAuth mAuth;
    private String UserType;
    PreferenceManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rooms);
        manager=new PreferenceManager(getApplicationContext());
        HotelRef = FirebaseDatabase.getInstance().getReference("Room");
        UserRef = FirebaseDatabase.getInstance().getReference("user");
        mAuth=FirebaseAuth.getInstance();

        mRoomRV = findViewById(R.id.recycler_vw_rooms);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getUserType();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        loadData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRoomRV.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<RoomModel> options = new FirebaseRecyclerOptions.Builder<RoomModel>()
                .setQuery(HotelRef, RoomModel.class)
                .build();

        FirebaseRecyclerAdapter<RoomModel, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<RoomModel, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final RoomModel model) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                Toast.makeText(ViewRoomsActivity.this, model.getRoomno(), Toast.LENGTH_SHORT).show();
                holder.HotelName.setText(model.getRoomno());
                holder.WifiAvlTV.setText(model.getInternet());
                holder.NofBedsTV.setText(model.getNoofbeds());
                holder.RentTv.setText(model.getRent());

                if (model.getRoomstatus().equals("available for stay")){
                    holder.RoomStatusTv.setBackground(getResources().getDrawable(R.color.reserved));
                } else if (model.getRoomstatus().equals("reserved")  ){
                    holder.RoomStatusTv.setBackground(getResources().getDrawable(R.color.checked_in));

                }else if (model.getRoomstatus().equals("Checked-in")  ){
                    holder.RoomStatusTv.setBackground(getResources().getDrawable(R.color.checked_out));

                }else if (model.getRoomstatus().equals("Checked-out")  ){
                    holder.RoomStatusTv.setBackground(getResources().getDrawable(R.color.available));
                }
                holder.RoomStatusTv.setText(model.getRoomstatus());
                holder.RoomStatusTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (model.getRoomstatus().equals("available for stay")){
                            HotelRef.child(model.getPushid()).child("roomstatus").setValue("reserved");
                            holder.RoomStatusTv.setBackground(getResources().getDrawable(R.color.reserved));
                        } else if (model.getRoomstatus().equals("reserved") && manager.getUserType().equals("admin") ){
                            HotelRef.child(model.getPushid()).child("roomstatus").setValue("Checked-in");
                            holder.RoomStatusTv.setBackground(getResources().getDrawable(R.color.checked_in));

                        }else if (model.getRoomstatus().equals("Checked-in") && manager.getUserType().equals("admin") ){
                            HotelRef.child(model.getPushid()).child("roomstatus").setValue("Checked-out");
                            holder.RoomStatusTv.setBackground(getResources().getDrawable(R.color.checked_out));

                        }else if (model.getRoomstatus().equals("Checked-out") && manager.getUserType().equals("admin") ){
                            HotelRef.child(model.getPushid()).child("roomstatus").setValue("available for stay");
                            holder.RoomStatusTv.setBackground(getResources().getDrawable(R.color.available));
                        } else {
                            Toast.makeText(ViewRoomsActivity.this, "Room is already booked", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //if you need three fix imageview in width
//                int DistanceDifference = distance(Double.parseDouble(model.getRoomno()), Double.parseDouble(model.getLongitude()), latitudeDouble, LongitudeDouble);

//                if (DistanceDifference < 5) {
//                    holder.HotelName.setText(model.getName());
//                }
//                Toast.makeText(ViewRoomsActivity.this, model.getLongitude(), Toast.LENGTH_SHORT).show();


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_item_layout, viewGroup, false);
                return new ProductViewHolder(view);
            }
        };

        mRoomRV.setAdapter(adapter);
        adapter.startListening();
    }

//    void loadData() {
//
//
//
//
//    }

    @Override
    public void onLocationChanged(Location location) {
//        distance(location.getLatitude(), location.getLongitude(), )
//        Toast.makeText(this, "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private int distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        value = (int) dist;
//        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        return (value);
    }

    private double deg2rad(double deg) {
        value = (int) (deg * Math.PI / 180.0);
//        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

        return (value);
    }

    private double rad2deg(double rad) {
//        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        value = (int) (rad * 180.0 / Math.PI);
        return (value);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {


        TextView HotelName, WifiAvlTV, NofBedsTV, RentTv, RoomStatusTv;




        ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            HotelName = itemView.findViewById(R.id.RoomNoTV);
            WifiAvlTV = itemView.findViewById(R.id.WifiAvlTV);
            NofBedsTV = itemView.findViewById(R.id.NofBedsTV);
            RentTv = itemView.findViewById(R.id.RentTv);
            RoomStatusTv = itemView.findViewById(R.id.RoomStatusTv);
        }
    }
    void getUserType() {
        UserRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserType=dataSnapshot.child("usertype").getValue().toString();
//                Toast.makeText(ViewRoomsActivity.this, UserType, Toast.LENGTH_SHORT).show();
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
}
