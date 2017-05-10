package ravtrix.foodybuddy.utils;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ravtrix.foodybuddy.callbacks.OnCityReceived;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.location.OnLocationReceived;
import ravtrix.foodybuddy.location.UserLocation;

/**
 * Created by Ravinder on 4/6/17.
 */

public class HelpersAPI {

    private HelpersAPI() {};

    public static void updateUserLocation(Activity activity, final UserLocalStore userLocalStore) {

        new UserLocation(activity).startLocationListener(new OnLocationReceived() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                //userLocalStore.changeLatitude(latitude);
                //userLocalStore.changeLongitude(longitude);
                System.out.println("LOCATION LATITUDE: " + latitude);
                System.out.println("LOCATION LONGITUDE: " + longitude);

                new GetCityAsync(Double.toString(longitude), Double.toString(latitude), new OnCityReceived() {
                    @Override
                    public void onCityReceived(String city) {
                        System.out.println("LOCATION CITY: " + city);
                        //if (city != null && !city.isEmpty()) userLocalStore.changeCity(city);
                    }
                }).execute();

            }
        });
    }

    /**
     * Asynchtask class that uses Google Map API to fetch for user's city based on given
     * longitude and latitude
     */
    private static class GetCityAsync extends AsyncTask<Void, Void, String> {

        private String longitude;
        private String latitude;
        private OnCityReceived onCityReceived; // callback

        GetCityAsync(String longitude, String latitude, OnCityReceived onCityReceived) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.onCityReceived = onCityReceived;
        }

        @Override
        protected String doInBackground(Void... voids) {

            URL url;
            HttpURLConnection urlConnection = null;
            JSONObject jsonObject;
            String city = "";
            String urlAddress = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                    latitude + "," + longitude + "&key=" + Constants.GOOGLE_MAP_API_KEY + "&language=en-US";

            try {
                url = new URL(urlAddress);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {
                    try {
                        jsonObject = new JSONObject(readStream(urlConnection.getInputStream()));

                        JSONArray address_components = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                        for (int i = 0; i < address_components.length(); i++) {
                            JSONObject component = address_components.getJSONObject(i);

                            String short_name = component.getString("short_name");
                            JSONArray typeArray = component.getJSONArray("types");
                            String addressType = typeArray.getString(0);

                            if (null != short_name && short_name.length() > 0) {
                                switch (addressType) {
                                    case "locality":
                                        city += short_name;
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (city.length() > 0) break; // already found a city, break out of addresses
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return city;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            onCityReceived.onCityReceived(s); // s is the returned city by google map api
        }
    }

    /**
     * Read the input stream into a String object
     * @param in        the input stream
     * @return          the string of information
     */
    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }



}
