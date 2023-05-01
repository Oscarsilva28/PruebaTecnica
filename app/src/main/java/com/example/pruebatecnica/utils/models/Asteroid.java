package com.example.pruebatecnica.utils.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Asteroid {
    private String link,
                    neoReferenceId,
                    name,
                    nasaJplUrl;
    private Long absoluteMagnitudeH;
    private EstimatedDiameter estimatedDiameter;
    private List<CloseApproachData> closeApproachData;
    private int is_potentially_hazardous_asteroid, isSentryObjec;

    public Asteroid(String link, String neoReferenceId, String name, String nasaJplUrl, Long absoluteMagnitudeH, EstimatedDiameter estimatedDiameter, List<CloseApproachData> closeApproachData, int is_potentially_hazardous_asteroid, int isSentryObjec) {
        this.link = link;
        this.neoReferenceId = neoReferenceId;
        this.name = name;
        this.nasaJplUrl = nasaJplUrl;
        this.absoluteMagnitudeH = absoluteMagnitudeH;
        this.estimatedDiameter = estimatedDiameter;
        this.closeApproachData = closeApproachData;
        this.is_potentially_hazardous_asteroid = is_potentially_hazardous_asteroid;
        this.isSentryObjec = isSentryObjec;
    }

    public Asteroid(JSONObject jsonObject) throws JSONException {
        JSONObject links = jsonObject.getJSONObject("links");
        this.link = links.getString("self");
        this.neoReferenceId = jsonObject.getString("neo_reference_id");
        this.name = jsonObject.getString("name");
        this.nasaJplUrl = jsonObject.getString("nasa_jpl_url");
        this.absoluteMagnitudeH = jsonObject.getLong("absolute_magnitude_h");
    }

    public Asteroid() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNeoReferenceId() {
        return neoReferenceId;
    }

    public void setNeoReferenceId(String neoReferenceId) {
        this.neoReferenceId = neoReferenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNasaJplUrl() {
        return nasaJplUrl;
    }

    public void setNasaJplUrl(String nasaJplUrl) {
        this.nasaJplUrl = nasaJplUrl;
    }

    public Long getAbsoluteMagnitudeH() {
        return absoluteMagnitudeH;
    }

    public void setAbsoluteMagnitudeH(Long absoluteMagnitudeH) {
        this.absoluteMagnitudeH = absoluteMagnitudeH;
    }

    public EstimatedDiameter getEstimatedDiameter() {
        return estimatedDiameter;
    }

    public void setEstimatedDiameter(EstimatedDiameter estimatedDiameter) {
        this.estimatedDiameter = estimatedDiameter;
    }

    public List<CloseApproachData> getCloseApproachData() {
        return closeApproachData;
    }

    public void setCloseApproachData(List<CloseApproachData> closeApproachData) {
        this.closeApproachData = closeApproachData;
    }

    public int getIs_potentially_hazardous_asteroid() {
        return is_potentially_hazardous_asteroid;
    }

    public void setIs_potentially_hazardous_asteroid(int is_potentially_hazardous_asteroid) {
        this.is_potentially_hazardous_asteroid = is_potentially_hazardous_asteroid;
    }

    public int getIsSentryObjec() {
        return isSentryObjec;
    }

    public void setIsSentryObjec(int isSentryObjec) {
        this.isSentryObjec = isSentryObjec;
    }


    @Override
    public String toString() {
        return "Asteroid{" +
                "link='" + link + '\'' +
                ", neoReferenceId='" + neoReferenceId + '\'' +
                ", name='" + name + '\'' +
                ", nasaJplUrl='" + nasaJplUrl + '\'' +
                ", absoluteMagnitudeH=" + absoluteMagnitudeH +
                ", estimatedDiameter=" + estimatedDiameter +
                ", closeApproachData=" + closeApproachData +
                ", is_potentially_hazardous_asteroid=" + is_potentially_hazardous_asteroid +
                ", isSentryObjec=" + isSentryObjec +
                '}';
    }

    public static Asteroid castJsonObjectToAsteroid(JSONObject cursor) throws JSONException {
        Asteroid asteroid = new Asteroid();
        JSONObject links = cursor.getJSONObject("links");
        asteroid.setLink(links.getString("self"));
        asteroid.setNeoReferenceId(cursor.getString("neo_reference_id"));
        asteroid.setName(cursor.getString("name"));
        asteroid.setNasaJplUrl(cursor.getString("nasa_jpl_url"));
        asteroid.setAbsoluteMagnitudeH(cursor.getLong("absolute_magnitude_h"));

        EstimatedDiameter estimatedDiameter = new EstimatedDiameter();
        try {
            JSONObject estimatedDiameterJson = new JSONObject(cursor.getString("estimated_diameter"));
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
        asteroid.setIs_potentially_hazardous_asteroid(cursor.getBoolean("is_potentially_hazardous_asteroid") ? 1 : 0);

        List<CloseApproachData> closeApproachDataArrayList = new ArrayList<>();

        try {
            JSONArray closeApproachDataJsonArray = new JSONArray(cursor.getString("close_approach_data"));

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

        int is_sentry_object = cursor.getBoolean("is_sentry_object") ? 1 : 0;
        asteroid.setIsSentryObjec(is_sentry_object);
        return asteroid;
    }

}
