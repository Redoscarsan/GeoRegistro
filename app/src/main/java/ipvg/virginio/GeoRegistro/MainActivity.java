package ipvg.virginio.GeoRegistro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Terreno> listaterrenos = new ArrayList<>();
    String current_latitude = "";
    String current_longitude = "";
    TextView warning;
    EditText title;
    EditText description;
    EditText category;
    EditText date;
    EditText latitude;
    EditText longitude;
    Button save;
    Button register;
    Button map;
    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("MissingPermission")
    private void fetchCurrentLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Assign to global strings instead of making new local variables
                    current_latitude = String.valueOf(location.getLatitude());
                    current_longitude = String.valueOf(location.getLongitude());

                    // Also auto-populate your EditText fields if they are empty
                    if (latitude.getText().toString().isEmpty()) {
                        latitude.setText(current_latitude);
                    }
                    if (longitude.getText().toString().isEmpty()) {
                        longitude.setText(current_longitude);
                    }
                } else {
                    Log.w("LocationWarning", "Last location was null");
                }
            }
        });
    }

    public void openMap(String lat, String lon){
        String locationString = "Latitude: " + lat + "\nLongitude: " + lon;

        Intent intent = new Intent(MainActivity.this, Map.class);
        intent.putExtra("LOCATION_EXTRA", locationString);

        startActivity(intent);
    }

    private final ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(
                        Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(
                        Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if ((fineLocationGranted != null && fineLocationGranted) ||
                        (coarseLocationGranted != null && coarseLocationGranted)) {

                    if (fineLocationGranted == null || !fineLocationGranted) {
                        new AlertDialog.Builder(MainActivity.this).setTitle("Acceso a la ubicacion")
                                .setMessage("Ubicacion parcial activada.")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
                    }

                    // Fetch location now that permissions are granted, then open map
                    fetchCurrentLocation();
                    openMap(current_latitude, current_longitude);

                } else {
                    new AlertDialog.Builder(MainActivity.this).setTitle("Acceso a la ubicacion")
                            .setMessage("Se requiere de permisos de localizacion para continuar.")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        title = findViewById(R.id.txt_title);
        description = findViewById(R.id.txt_description);
        category = findViewById(R.id.txt_category);
        date = findViewById(R.id.txt_date);
        latitude = findViewById(R.id.txt_latitude);
        longitude = findViewById(R.id.txt_longitude);
        save = findViewById(R.id.btn_save);
        register = findViewById(R.id.btn_register);
        map = findViewById(R.id.btn_map);
        warning = findViewById(R.id.warning);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation();
        }

        save.setOnClickListener(v -> {
            Log.d("DebugButton", "Save button was clicked!");

            if (warning != null) {
                warning.setText("");
            } else {
                Log.e("DebugButton", "warning TextView is null!");
                return;
            }

            String input_title = title != null ? title.getText().toString().trim() : "";
            String input_description = description != null ? description.getText().toString().trim() : "";
            String input_category = category != null ? category.getText().toString().trim() : "";
            String input_date = date != null ? date.getText().toString().trim() : "";
            String input_latitude = latitude != null ? latitude.getText().toString().trim() : "";
            String input_longitude = longitude != null ? longitude.getText().toString().trim() : "";

            if (input_title.isEmpty()) {
                warning.setText("Error: el terreno debe de tener un titulo.");
                return;
            }
            if (input_description.isEmpty()) {
                warning.setText("Error: el terreno debe de tener una descripción");
                return;
            }
            if (input_category.isEmpty()) {
                warning.setText("Error: el terreno debe de tener una categoría");
                return;
            }
            if (input_date.isEmpty()) {
                warning.setText("Error: el terreno debe de tener una fecha");
                return;
            }
            if (input_latitude.isEmpty()) {
                warning.setText("Error: el terreno debe de tener una latitud");
                return;
            }
            if (input_longitude.isEmpty()) {
                warning.setText("Error: el terreno debe de tener una longitud");
                return;
            } else {
                Terreno newterreno = new Terreno(input_title, input_description, input_category, input_date, input_latitude, input_longitude);
                listaterrenos.add(newterreno);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("my_objects_collection").add(newterreno)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(MainActivity.this, "Saved to Firebase!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("FirestoreError", "Error writing document", e);
                        });
            }
        });

        map.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation();
                openMap(current_latitude, current_longitude);
            } else {
                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}