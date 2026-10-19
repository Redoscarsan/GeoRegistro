package ipvg.virginio.GeoRegistro;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Localization extends AppCompatActivity implements OnMapReadyCallback {
    TextView tv_location;
    private GoogleMap mMap;

    private double userLatitude = -36.6066;
    private double userLongitude = -72.1034;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        tv_location = findViewById(R.id.tv_location);

        if (getIntent() != null) {
            String stringLat = getIntent().getStringExtra("EXTRA_LATITUDE");
            String stringLng = getIntent().getStringExtra("EXTRA_LONGITUDE");

            try {
                if (stringLat != null && !stringLat.isEmpty()) {
                    userLatitude = Double.parseDouble(stringLat);
                }
                if (stringLng != null && !stringLng.isEmpty()) {
                    userLongitude = Double.parseDouble(stringLng);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (tv_location != null) {
            tv_location.setText("Lat: " + userLatitude + ", Lng: " + userLongitude);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateMapLocation(userLatitude, userLongitude);
    }

    public void updateMapLocation(double lat, double lng) {
        LatLng userLocation = new LatLng(lat, lng);

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f));
    }
}