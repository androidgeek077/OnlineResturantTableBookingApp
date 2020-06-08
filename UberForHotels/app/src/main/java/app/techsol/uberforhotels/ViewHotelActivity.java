package app.techsol.uberforhotels;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.AddHotelModel;

public class ViewHotelActivity extends AppCompatActivity implements LocationListener {
    DatabaseReference HotelRef;
    RecyclerView mProductRecycVw;
    Double latitudeDouble=32.079380, LongitudeDouble= 72.675057;
    int value;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hotel);
        HotelRef = FirebaseDatabase.getInstance().getReference("Hotels");
        mProductRecycVw = findViewById(R.id.recycler_vw_catagory);
        getSupportActionBar().hide();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mProductRecycVw.setLayoutManager(mLayoutManager);

        loadData();

    }

    void loadData() {


        FirebaseRecyclerOptions<AddHotelModel> options = new FirebaseRecyclerOptions.Builder<AddHotelModel>()
                .setQuery(HotelRef, AddHotelModel.class)
                .build();

        FirebaseRecyclerAdapter<AddHotelModel, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<AddHotelModel, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final AddHotelModel model) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width
                int DistanceDifference= distance(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()), latitudeDouble, LongitudeDouble);

                if (DistanceDifference<5){
                    holder.HotelName.setText(model.getName());
                }
//                Toast.makeText(ViewHotelActivity.this, model.getLongitude(), Toast.LENGTH_SHORT).show();


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hotel_item_layout, viewGroup, false);
                return new ProductViewHolder(view);
            }
        };

        mProductRecycVw.setAdapter(adapter);
        adapter.startListening();

    }

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


        TextView HotelName, mTextField, PasswordVersionsTV, StageStartTime;

        LinearLayout StartTimeLL;
        FrameLayout ChnageStageStatusFL;
        ImageView stageUnloacked, stageLocked;
        LinearLayout getView;


        ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            HotelName = itemView.findViewById(R.id.hotelNameTV);
//            getView = itemView.findViewById(R.id.getView);
//            mTextField = itemView.findViewById(R.id.mTextField);


        }
    }

}
