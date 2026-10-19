package ipvg.virginio.GeoRegistro;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Terreno> listaterrenos = new ArrayList<>();
    TextView warning;
    EditText title;
    EditText description;
    EditText category;
    Button save;
    Button register;
    Button map;
    public void openMap(){
        String currentLatitude = "36.6062° S";
        String currentLongitude = "72.1030° W";
        String locationString = "Latitude: " + currentLatitude + "\nLongitude: " + currentLongitude;

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

                if (fineLocationGranted != null && fineLocationGranted) {
                    openMap();

                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    new AlertDialog.Builder(MainActivity.this).setTitle("Acceso a la ubicacion")
                            .setMessage("Ubicacion parcial activada.")
                            .setPositiveButton("OK", (dialog, which) -> {dialog.dismiss();}).show();
                    openMap();
                } else {
                    new AlertDialog.Builder(MainActivity.this).setTitle("Acceso a la ubicacion")
                            .setMessage("Se requiere de permisos de localizacion para continuar.")
                            .setPositiveButton("OK", (dialog, which) -> {dialog.dismiss();}).show();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        title = (EditText) findViewById(R.id.txt_title);
        description = (EditText) findViewById(R.id.txt_description);
        category = (EditText) findViewById(R.id.txt_category);
        save = (Button) findViewById(R.id.btn_save);
        register = (Button) findViewById(R.id.btn_register);
        map = (Button) findViewById(R.id.btn_map);
        warning = (TextView) findViewById(R.id.warning);

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
            } else {
                Terreno newterreno = new Terreno(input_title, input_description, input_category);
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
                openMap();
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
