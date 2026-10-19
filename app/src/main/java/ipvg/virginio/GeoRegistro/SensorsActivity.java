package ipvg.virginio.GeoRegistro;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView tvSensorValues;
    private TextView tvStatus;
    private Button btnManualControl;
    private boolean isManualMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        tvSensorValues = findViewById(R.id.tv_sensor_values);
        tvStatus = findViewById(R.id.tv_status);
        btnManualControl = findViewById(R.id.btn_manual_control);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        if (accelerometer == null) {
            tvSensorValues.setText("Acelerómetro no disponible en este dispositivo.");
        }

        btnManualControl.setOnClickListener(v -> {
            isManualMode = !isManualMode;
            if (isManualMode) {
                tvStatus.setText("Estado: Controlado manualmente (Sin sensor).");
                Toast.makeText(this, "Modo manual activado", Toast.LENGTH_SHORT).show();
            } else {
                tvStatus.setText("Estado: Usando sensor físico.");
                Toast.makeText(this, "Modo sensor activado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (accelerometer != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!isManualMode && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            tvSensorValues.setText(String.format("X: %.2f | Y: %.2f | Z: %.2f", x, y, z));

            if (Math.abs(x) > 4 || Math.abs(y) > 4) {
                tvStatus.setText("Estado: Movimiento detectado por sensor");
            } else {
                tvStatus.setText("Estado: Dispositivo estable.");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}