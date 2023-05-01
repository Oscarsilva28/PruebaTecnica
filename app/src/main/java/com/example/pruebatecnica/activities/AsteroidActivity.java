package com.example.pruebatecnica.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebatecnica.R;
import com.example.pruebatecnica.utils.database.AsteroidsHelper;
import com.example.pruebatecnica.utils.models.Asteroid;
import com.example.pruebatecnica.utils.shared.IdAsteroidUtility;

public class AsteroidActivity extends AppCompatActivity {
    AsteroidsHelper asteroidsHelper;

    private TextView neoReferenceId_asteroid,
            link_asteroid,
            name_asteroid,
            nasaJplUrl_asteroid,
            absoluteMagnitudeH_asteroid,
            estimatedDiameter_asteroid,
            closeApproachData_asteroid,
            is_potentially_hazardous_asteroid_asteroid,
            isSentryObjec_hazardous_asteroid_asteroid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asteroid);

        asteroidsHelper = new AsteroidsHelper(this);

        neoReferenceId_asteroid = findViewById(R.id.neoReferenceId_asteroid);
        link_asteroid = findViewById(R.id.link_asteroid);
        name_asteroid = findViewById(R.id.name_asteroid);
        nasaJplUrl_asteroid = findViewById(R.id.nasaJplUrl_asteroid);
        absoluteMagnitudeH_asteroid = findViewById(R.id.absoluteMagnitudeH_asteroid);
        estimatedDiameter_asteroid = findViewById(R.id.estimatedDiameter_asteroid);
        closeApproachData_asteroid = findViewById(R.id.closeApproachData_asteroid);
        is_potentially_hazardous_asteroid_asteroid = findViewById(R.id.is_potentially_hazardous_asteroid_asteroid);
        isSentryObjec_hazardous_asteroid_asteroid = findViewById(R.id.isSentryObjec_hazardous_asteroid_asteroid);

        init();
    }

    public void init() {

        Toast.makeText(getApplicationContext(), IdAsteroidUtility.getId(getApplicationContext()), Toast.LENGTH_SHORT).show();
        Asteroid asteroid = asteroidsHelper.getAsteroid(getApplicationContext());

        if (asteroid == null) {
            Toast.makeText(getApplicationContext(),"Not found", Toast.LENGTH_SHORT).show();
        }
        neoReferenceId_asteroid.setText("ID: " + asteroid.getNeoReferenceId());
        link_asteroid.setText("Link: " + asteroid.getNeoReferenceId());
        name_asteroid.setText("Name: " + asteroid.getName());
        nasaJplUrl_asteroid.setText("ImageURL: " + asteroid.getNasaJplUrl());
        absoluteMagnitudeH_asteroid.setText("Magnitude: " + asteroid.getAbsoluteMagnitudeH().toString());
        estimatedDiameter_asteroid.setText("Diameter: " + asteroid.getEstimatedDiameter().toString());
        closeApproachData_asteroid.setText("Close Approach: " + HtmlCompat.fromHtml(asteroid.getCloseApproachData().toString(), 0));
        is_potentially_hazardous_asteroid_asteroid.setText("Hazardous: " + (asteroid.getIs_potentially_hazardous_asteroid() == 1 ? "⚠️⚠" : "✅"));
        isSentryObjec_hazardous_asteroid_asteroid.setText("Sentry Object: " + ( asteroid.getIsSentryObjec() == 1 ? "✅" : "❌"));

    }
}