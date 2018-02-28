package spinoffpyme.com.talentmobile;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import spinoffpyme.com.talentmobile.Animations.ProgressBarAnimation;
import spinoffpyme.com.talentmobile.Model.City;
import spinoffpyme.com.talentmobile.Model.StationWeather;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private EditText edtCiudad;
    private Button btnBuscar;
    private GoogleMap mMap;
    private ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        edtCiudad = findViewById(R.id.edtCiudad);
        btnBuscar = findViewById(R.id.btnBuscar);
        pbar=findViewById(R.id.pbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetGeonames().execute(edtCiudad.getText().toString());
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    public class GetGeonames extends AsyncTask<String, Integer, City> {
        @Override
        protected void onPostExecute(City city) {
            if (city != null) {
                mMap.clear();
                // Add a marker in Sydney and move the camera
                LatLng ciudad = new LatLng(city.getLatitude(), city.getLongitude());
                mMap.addMarker(new MarkerOptions().position(ciudad).title(city.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudad, 8));
                Log.i("Datos", city.getCityBox().toString());
            } else
                Toast.makeText(getApplication(), "Ciudad no encontrada", Toast.LENGTH_LONG).show();
            new GetWeather().execute(city);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected City doInBackground(String... strings) {
            City city = null;
            try {
                URL url = new URL("http://api.geonames.org/searchJSON?q=" + strings[0] + "&maxRows=20&startRow=0&lang=en&isNameRequired=true&style=FULL&username=ilgeonamessample");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    StringBuilder result = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    JSONObject responseJSON = new JSONObject(result.toString());
                    JSONArray geonames = responseJSON.getJSONArray("geonames");
                    JSONObject cityJSON = geonames.getJSONObject(0);
                    city = new City();
                    city.setName(cityJSON.getString("name"));
                    city.setRegion(cityJSON.getString("continentCode"));

                    city.setLatitude(cityJSON.getDouble("lat"));
                    city.setLongitude(cityJSON.getDouble("lng"));

                    city.getCityBox().setEast(cityJSON.getJSONObject("bbox").getString("east"));
                    city.getCityBox().setSouth(cityJSON.getJSONObject("bbox").getString("south"));
                    city.getCityBox().setNorth(cityJSON.getJSONObject("bbox").getString("north"));
                    city.getCityBox().setWest(cityJSON.getJSONObject("bbox").getString("west"));

                }
                return city;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return city;
        }
    }

    public class GetWeather extends AsyncTask<City, Void, City> {
        Double temperature=0.0;
        @Override
        protected void onPostExecute(City city) {
            for (int i = 0; i < city.getListStations().size(); i++) {
                temperature=temperature+city.getListStations().get(i).getTemperature();
                LatLng mark = new LatLng(city.getListStations().get(i).getLatitude(), city.getListStations().get(i).getLongitude());
                mMap.addMarker(new MarkerOptions().position(mark).title(city.getListStations().get(i).getName()));
            }
            temperature=temperature/city.getListStations().size();
            ProgressBarAnimation progressBarAnimation=new ProgressBarAnimation(pbar,0,temperature.floatValue());
            progressBarAnimation.setDuration(1000);
            pbar.startAnimation(progressBarAnimation);


        }


    @Override
    protected City doInBackground(City... cities) {
        City city = cities[0];
        try {
            URL url = new URL("http://api.geonames.org/weatherJSON?north=" + city.getCityBox().getNorth() + "&south=" + city.getCityBox().getSouth() + "&east=" + city.getCityBox().getEast() + "&west=" + city.getCityBox().getWest() + "&username=ilgeonamessample");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                JSONObject responseJSON = new JSONObject(result.toString());
                JSONArray weatherObservations = responseJSON.getJSONArray("weatherObservations");
                for (int i = 0; i < weatherObservations.length(); i++) {
                    JSONObject wstation = weatherObservations.getJSONObject(i);
                    StationWeather stationWeather = new StationWeather();
                    stationWeather.setLatitude(wstation.getDouble("lat"));
                    stationWeather.setLongitude(wstation.getDouble("lng"));
                    stationWeather.setTemperature(wstation.getDouble("temperature"));
                    stationWeather.setName(wstation.getString("stationName"));
                    city.getListStations().add(stationWeather);
                }
            }
            return city;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }
}
}
