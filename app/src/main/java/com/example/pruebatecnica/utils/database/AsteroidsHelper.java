package com.example.pruebatecnica.utils.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.pruebatecnica.utils.models.Asteroid;
import com.example.pruebatecnica.utils.models.CloseApproachData;
import com.example.pruebatecnica.utils.models.EstimatedDiameter;
import com.example.pruebatecnica.utils.shared.IdAsteroidUtility;
import com.example.pruebatecnica.utils.shared.IdUtility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsteroidsHelper extends SQLiteOpenHelper {
    public static final String DatabaseName = "Signup3.db";

    public AsteroidsHelper(@Nullable Context context) {
        super(context, "Signup3.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int i, int i1) {
        MyDatabase.execSQL("DROP TABLE IF EXISTS asteroids");
    }

    public long insertData(
            Context context,
            String link,
            String neo_reference_id,
            String name,
            String nasa_jpl_url,
            Long absolute_magnitude_h,
            String estimated_diameter,
            int is_potentially_hazardous_asteroid,
            String close_approach_data,
            int is_sentry_object ) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id_user", IdUtility.getId(context));
        contentValues.put("link", link);
        contentValues.put("neo_reference_id", neo_reference_id);
        contentValues.put("name", name);
        contentValues.put("nasa_jpl_url", nasa_jpl_url);
        contentValues.put("absolute_magnitude_h", absolute_magnitude_h);
        contentValues.put("estimated_diameter", estimated_diameter);
        contentValues.put("is_potentially_hazardous_asteroid", is_potentially_hazardous_asteroid);
        contentValues.put("close_approach_data", close_approach_data);
        contentValues.put("is_sentry_object", is_sentry_object);

        long inserted = MyDatabase.insert("asteroids", null, contentValues);

        return inserted;
    }

    @SuppressLint("Range")
    public List<Asteroid> getAsteroids(Context context){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from asteroids where id_user = ?", new String[]{ IdUtility.getId(context)});

        List<Asteroid> asteroids = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Asteroid asteroid = new Asteroid();
                asteroid.setLink(cursor.getString(cursor.getColumnIndex("link")));
                asteroid.setNeoReferenceId(cursor.getString(cursor.getColumnIndex("neo_reference_id")));
                asteroid.setName(cursor.getString(cursor.getColumnIndex("name")));
                asteroid.setNasaJplUrl(cursor.getString(cursor.getColumnIndex("nasa_jpl_url")));
                asteroid.setAbsoluteMagnitudeH(cursor.getLong(cursor.getColumnIndex("absolute_magnitude_h")));

                EstimatedDiameter estimatedDiameter = new EstimatedDiameter();
                try {
                    JSONObject estimatedDiameterJson = new JSONObject(cursor.getString(cursor.getColumnIndex("estimated_diameter")));
                    JSONObject kilometersJson = estimatedDiameterJson.getJSONObject("kilometers");
                    estimatedDiameter.setKilometers(new HashMap<String, Long>() {
                        {
                            put("estimated_diameter_min", kilometersJson.getLong("estimated_diameter_min"));
                            put("estimated_diameter_max", kilometersJson.getLong("estimated_diameter_max"));
                        }
                    });
                    JSONObject metersJson = estimatedDiameterJson.getJSONObject("meters");
                    estimatedDiameter.setMeters(new HashMap<String, Long>() {
                        {
                            put("estimated_diameter_min", metersJson.getLong("estimated_diameter_min"));
                            put("estimated_diameter_max", metersJson.getLong("estimated_diameter_max"));
                        }
                    });
                    JSONObject milesJson = estimatedDiameterJson.getJSONObject("miles");
                    estimatedDiameter.setMiles(new HashMap<String, Long>() {
                        {
                            put("estimated_diameter_min", milesJson.getLong("estimated_diameter_min"));
                            put("estimated_diameter_max", milesJson.getLong("estimated_diameter_max"));
                        }
                    });
                    JSONObject feetJson = estimatedDiameterJson.getJSONObject("feet");
                    estimatedDiameter.setFeet(new HashMap<String, Long>() {
                        {
                            put("estimated_diameter_min", feetJson.getLong("estimated_diameter_min"));
                            put("estimated_diameter_max", feetJson.getLong("estimated_diameter_max"));
                        }
                    });
                } catch (Exception e) {
                    Log.w("estimatedDiameter", e);
                }

                asteroid.setEstimatedDiameter(estimatedDiameter);
                asteroid.setIs_potentially_hazardous_asteroid(cursor.getInt(cursor.getColumnIndex("is_potentially_hazardous_asteroid")));

                List<CloseApproachData> closeApproachDataArrayList = new ArrayList<>();

                try {
                    JSONArray closeApproachDataJsonArray = new JSONArray(cursor.getString(cursor.getColumnIndex("close_approach_data")));

                    for (int i = 0; i < closeApproachDataJsonArray.length(); i++) {
                        CloseApproachData closeApproachData = new CloseApproachData();

                        JSONObject closeApproachDataJson = closeApproachDataJsonArray.getJSONObject(i);

                        String close_approach_date = closeApproachDataJson.getString("close_approach_date");
                        closeApproachData.setClose_approach_date(close_approach_date);

                        String close_approach_date_full = closeApproachDataJson.getString("close_approach_date_full");
                        closeApproachData.setClose_approach_date_full(close_approach_date_full);

                        Long epoch_date_close_approach = closeApproachDataJson.getLong("epoch_date_close_approach");
                        closeApproachData.setEpoch_date_close_approach(epoch_date_close_approach);

                        JSONObject relative_velocityJson = closeApproachDataJson.getJSONObject("relative_velocity");
                        closeApproachData.setRelative_velocity(new HashMap<String, String>() {
                            {
                                put("kilometers_per_second", relative_velocityJson.getString("kilometers_per_second"));
                                put("kilometers_per_hour", relative_velocityJson.getString("kilometers_per_hour"));
                                put("miles_per_hour", relative_velocityJson.getString("miles_per_hour"));
                            }
                        });

                        JSONObject miss_distanceJson = closeApproachDataJson.getJSONObject("miss_distance");
                        closeApproachData.setMiss_distance(new HashMap<String, String>() {
                            {
                                put("astronomical", miss_distanceJson.getString("astronomical"));
                                put("lunar", miss_distanceJson.getString("lunar"));
                                put("kilometers", miss_distanceJson.getString("kilometers"));
                                put("miles", miss_distanceJson.getString("miles"));
                            }
                        });

                        String orbiting_body = closeApproachDataJson.getString("orbiting_body");
                        closeApproachData.setOrbiting_body(orbiting_body);

                        closeApproachDataArrayList.add(closeApproachData);
                    }
                } catch (Exception e) {
                    Log.w("estimatedDiameter", e);
                }

                asteroid.setCloseApproachData(closeApproachDataArrayList);

                int is_sentry_object = cursor.getInt(cursor.getColumnIndex("is_sentry_object"));
                asteroid.setIsSentryObjec(is_sentry_object);

                asteroids.add(asteroid);
            };
        }
        return asteroids;
    }

    @SuppressLint("Range")
    public Asteroid getAsteroid(Context context){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from asteroids where id = ?", new String[]{ IdAsteroidUtility.getId(context)});

        Asteroid asteroid = new Asteroid();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                asteroid.setLink(cursor.getString(cursor.getColumnIndex("link")));
                asteroid.setNeoReferenceId(cursor.getString(cursor.getColumnIndex("neo_reference_id")));
                asteroid.setName(cursor.getString(cursor.getColumnIndex("name")));
                asteroid.setNasaJplUrl(cursor.getString(cursor.getColumnIndex("nasa_jpl_url")));
                asteroid.setAbsoluteMagnitudeH(cursor.getLong(cursor.getColumnIndex("absolute_magnitude_h")));

                EstimatedDiameter estimatedDiameter = new EstimatedDiameter();
                try {
                    JSONObject estimatedDiameterJson = new JSONObject(cursor.getString(cursor.getColumnIndex("estimated_diameter")));
                    JSONObject kilometersJson = estimatedDiameterJson.getJSONObject("kilometers");
                    estimatedDiameter.setKilometers(new HashMap<String, Long>() {
                        {
                            put("estimated_diameter_min", kilometersJson.getLong("estimated_diameter_min"));
                            put("estimated_diameter_max", kilometersJson.getLong("estimated_diameter_max"));
                        }
                    });
                    JSONObject metersJson = estimatedDiameterJson.getJSONObject("meters");
                    estimatedDiameter.setMeters(new HashMap<String, Long>() {
                        {
                            put("estimated_diameter_min", metersJson.getLong("estimated_diameter_min"));
                            put("estimated_diameter_max", metersJson.getLong("estimated_diameter_max"));
                        }
                    });
                    JSONObject milesJson = estimatedDiameterJson.getJSONObject("miles");
                    estimatedDiameter.setMiles(new HashMap<String, Long>() {
                        {
                            put("estimated_diameter_min", milesJson.getLong("estimated_diameter_min"));
                            put("estimated_diameter_max", milesJson.getLong("estimated_diameter_max"));
                        }
                    });
                    JSONObject feetJson = estimatedDiameterJson.getJSONObject("feet");
                    estimatedDiameter.setFeet(new HashMap<String, Long>() {
                        {
                            put("estimated_diameter_min", feetJson.getLong("estimated_diameter_min"));
                            put("estimated_diameter_max", feetJson.getLong("estimated_diameter_max"));
                        }
                    });
                } catch (Exception e) {
                    Log.w("estimatedDiameter", e);
                }

                asteroid.setEstimatedDiameter(estimatedDiameter);
                asteroid.setIs_potentially_hazardous_asteroid(cursor.getInt(cursor.getColumnIndex("is_potentially_hazardous_asteroid")));

                List<CloseApproachData> closeApproachDataArrayList = new ArrayList<>();

                try {
                    JSONArray closeApproachDataJsonArray = new JSONArray(cursor.getString(cursor.getColumnIndex("close_approach_data")));

                    for (int i = 0; i < closeApproachDataJsonArray.length(); i++) {
                        CloseApproachData closeApproachData = new CloseApproachData();

                        JSONObject closeApproachDataJson = closeApproachDataJsonArray.getJSONObject(i);

                        String close_approach_date = closeApproachDataJson.getString("close_approach_date");
                        closeApproachData.setClose_approach_date(close_approach_date);

                        String close_approach_date_full = closeApproachDataJson.getString("close_approach_date_full");
                        closeApproachData.setClose_approach_date_full(close_approach_date_full);

                        Long epoch_date_close_approach = closeApproachDataJson.getLong("epoch_date_close_approach");
                        closeApproachData.setEpoch_date_close_approach(epoch_date_close_approach);

                        JSONObject relative_velocityJson = closeApproachDataJson.getJSONObject("relative_velocity");
                        closeApproachData.setRelative_velocity(new HashMap<String, String>() {
                            {
                                put("kilometers_per_second", relative_velocityJson.getString("kilometers_per_second"));
                                put("kilometers_per_hour", relative_velocityJson.getString("kilometers_per_hour"));
                                put("miles_per_hour", relative_velocityJson.getString("miles_per_hour"));
                            }
                        });

                        JSONObject miss_distanceJson = closeApproachDataJson.getJSONObject("miss_distance");
                        closeApproachData.setMiss_distance(new HashMap<String, String>() {
                            {
                                put("astronomical", miss_distanceJson.getString("astronomical"));
                                put("lunar", miss_distanceJson.getString("lunar"));
                                put("kilometers", miss_distanceJson.getString("kilometers"));
                                put("miles", miss_distanceJson.getString("miles"));
                            }
                        });

                        String orbiting_body = closeApproachDataJson.getString("orbiting_body");
                        closeApproachData.setOrbiting_body(orbiting_body);

                        closeApproachDataArrayList.add(closeApproachData);
                    }
                } catch (Exception e) {
                    Log.w("estimatedDiameter", e);
                }

                asteroid.setCloseApproachData(closeApproachDataArrayList);

                int is_sentry_object = cursor.getInt(cursor.getColumnIndex("is_sentry_object"));
                asteroid.setIsSentryObjec(is_sentry_object);

                return asteroid;
            };
        }
        return null;
    }

}
