package ipvg.virginio.georegistro;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. MUST be the very first line before super.onCreate()
        // This handles the splash screen and automatically applies your postSplashScreenTheme
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // 2. Now safe to load your layout
        setContentView(R.layout.activity_main); // Replace with your actual layout name
    }
}
