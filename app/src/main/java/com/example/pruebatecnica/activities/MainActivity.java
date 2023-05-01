package com.example.pruebatecnica.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pruebatecnica.R;
import com.example.pruebatecnica.utils.ListElements.ListAdapter;
import com.example.pruebatecnica.utils.ListElements.ListElement;
import com.example.pruebatecnica.utils.database.AsteroidsHelper;
import com.example.pruebatecnica.utils.database.DatabaseHelper;
import com.example.pruebatecnica.utils.models.Asteroid;
import com.example.pruebatecnica.utils.shared.IdUtility;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    AsteroidsHelper asteroidsHelper;
    List<ListElement> elements;
    TextView textDate;
    String startDate;
    String endDate;
    Button selectDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textDate = findViewById(R.id.textRange);
        selectDate = findViewById(R.id.buttonRange);

        asteroidsHelper = new AsteroidsHelper(this);


        MaterialDatePicker datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .build();
        Toast.makeText(getApplicationContext(), IdUtility.getId(getApplicationContext()), Toast.LENGTH_LONG).show();

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "Material Range");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        Pair<Long,Long> selected = (Pair<Long, Long>) selection;

                        long diffInMillies = Math.abs(selected.second - selected.first);
                        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                        if (diff >= 7) {
                            Toast.makeText(MainActivity.this,"Days limit is 7!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Get the offset from our timezone and UTC.
                        TimeZone timeZoneUTC = TimeZone.getDefault();
                        // It will be negative, so that's the -1
                        int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        startDate = dateFormat.format(new Date(selected.first + offsetFromUTC));
                        endDate = dateFormat.format(new Date(selected.second + offsetFromUTC));

                        textDate.setText(startDate + " / " + endDate );

                        leerWs();
                    }
                });
            }
        });


    }

    private void leerWs() {
        String url = "https://api.nasa.gov/neo/rest/v1/feed?start_date=" + startDate +"&end_date=" + endDate +"&api_key=prp0LisVDgj4Jf92I6dktNRqhPOAQNhHaueEKcoi";

        StringRequest getAsteroids = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject nearEarthObjects = jsonObject.getJSONObject("near_earth_objects");

                    JSONArray firstDateObject = nearEarthObjects.getJSONArray(startDate);

                    JSONArray endDateObject = nearEarthObjects.getJSONArray(endDate);

                    JSONArray newJson = firstDateObject;
                    for (int i = 0; i < endDateObject.length(); i++) {
                        newJson.put(endDateObject.getJSONObject(i));
                    }

                    elements = new ArrayList<>();

                    for (int i = 0 ; i < newJson.length(); i++) {
                        String estimatedDiameter = newJson.getJSONObject(i).getString("estimated_diameter");
                        String closeApproachData = newJson.getJSONObject(i).getString("close_approach_data");
                        Asteroid asteroid =  Asteroid.castJsonObjectToAsteroid(newJson.getJSONObject(i));
                        long id =  asteroidsHelper.insertData(
                                getApplicationContext(),
                                asteroid.getLink(),
                                asteroid.getNeoReferenceId(),
                                asteroid.getName(),
                                asteroid.getNasaJplUrl(),
                                asteroid.getAbsoluteMagnitudeH(),
                                estimatedDiameter,
                                asteroid.getIs_potentially_hazardous_asteroid(),
                                closeApproachData,
                                asteroid.getIs_potentially_hazardous_asteroid());
                        if (id != -1) {
                            elements.add(getListElement(asteroid, id));
                        }
                    }

                    init();

                } catch (JSONException e) {
                    Log.w("ERROR_PARSING_ASTEROIDS", e);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("ERROR_FETCHING_ASTEROIDS", error);
            }
        });

        Volley.newRequestQueue(this).add(getAsteroids).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.w("ERROR_RETRYING_FETCH", error);
            }
        });;
    }

    public ListElement getListElement(Asteroid asteroid, long id) throws JSONException {
        String neoReferenceId, name, photo;
        Long magnitude;
        Boolean isPotentiallyHazardous;

        neoReferenceId = asteroid.getNeoReferenceId();
        name = asteroid.getName();
        photo = asteroid.getNasaJplUrl();
        magnitude = asteroid.getAbsoluteMagnitudeH();
        isPotentiallyHazardous = asteroid.getIs_potentially_hazardous_asteroid() == 1;

        return new ListElement(id,neoReferenceId,photo,name,magnitude,isPotentiallyHazardous);
    }

    public void init() {
        ListAdapter listAdapter = new ListAdapter(elements, this);
        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }
}