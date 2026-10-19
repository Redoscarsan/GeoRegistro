package ipvg.virginio.GeoRegistro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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


                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        }
}
