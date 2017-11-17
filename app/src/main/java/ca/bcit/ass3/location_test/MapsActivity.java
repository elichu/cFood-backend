package ca.bcit.ass3.location_test;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    SQLiteDatabase db;
    private ZoneHelper dbHelper;

    private GoogleMap mMap;
    String [] coordsLong;
    String [] coordsLat;

    String [] allShopsNames;
    Parcelable[] allShopsLatLng;

    String [] allShopsX;
    String [] allShopsY;

    String [] parksLat;
    String [] parksLong;
    String [] parksNames;

    String [] busStopX;
    String [] busStopY;
    String [] busStopNames;

    String [] recX;
    String [] recY;
    String [] recNames;

    String [] schoolsX;
    String [] schoolsY;
    String [] schoolsNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dbHelper = new ZoneHelper(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String[] neighbourhoods = {
                "Connaught Heights", "Downtown", "North Arm South", "Brunette Creek", "Sapperton", "Glenbrooke South", "Victory Heights", "Queens Park", "Glenbrooke North", "Uptown", "Brow of the Hill", "Kelvin", "Westend", "Queensborough", "North Arm North",
        };

        Intent intent = getIntent();
        coordsLong = intent.getStringArrayExtra("coordsLong");
        coordsLat = intent.getStringArrayExtra("coordsLat");

        allShopsNames = intent.getStringArrayExtra("shopsNames");
        allShopsX = intent.getStringArrayExtra("shopsX");
        allShopsY = intent.getStringArrayExtra("shopsY");

        parksNames = intent.getStringArrayExtra("parksNames");
        parksLat = intent.getStringArrayExtra("parksLat");
        parksLong = intent.getStringArrayExtra("parksLong");

        busStopNames = intent.getStringArrayExtra("busStopNames");
        busStopX = intent.getStringArrayExtra("busStopX");
        busStopY = intent.getStringArrayExtra("busStopY");

        recNames = intent.getStringArrayExtra("recNames");
        recX = intent.getStringArrayExtra("recX");
        recY = intent.getStringArrayExtra("recY");

        schoolsNames = intent.getStringArrayExtra("schoolsNames");
        schoolsX = intent.getStringArrayExtra("schoolsX");
        schoolsY = intent.getStringArrayExtra("schoolsY");
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

        //Contains all the places
        LatLng[] latLngs = new LatLng[coordsLat.length];
        LatLng[] latLngsShops = new LatLng[allShopsX.length];
        LatLng[] latLngsParks = new LatLng[parksLat.length];
        LatLng[] latLngsBusStops = new LatLng[busStopX.length];
        LatLng[] latLngsRecreation = new LatLng[recX.length];
        LatLng[] latLngsSchools = new LatLng[schoolsX.length];

        //Contains only those places/landmarks that are within
        //the specified neighbourhood (polygon)
        LatLng[] latLngsShopsInPolygon = new LatLng[allShopsX.length];
        LatLng[] latLngsParksInPolygon = new LatLng[parksLat.length];
        LatLng[] latLngsBusStopsInPolygon = new LatLng[busStopX.length];
        LatLng[] latLngsRecreationInPolygon = new LatLng[recX.length];
        LatLng[] latLngsSchoolsInPolygon = new LatLng[schoolsX.length];


        for(int i = 0; i < coordsLat.length; i++) {
            latLngs[i] = new LatLng(Float.parseFloat(coordsLat[i]), Float.parseFloat(coordsLong[i]));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs[0], 14.0f));
        PolygonOptions rectOptions = new PolygonOptions()
                .add(latLngs)
                .fillColor(Color.argb(50, 50, 0, 255))
                .strokeWidth(2.0f);
        Polygon polygon = mMap.addPolygon(rectOptions);

        for(int j = 0; j < allShopsX.length; j++) {
            latLngsShops[j] = new LatLng(Float.parseFloat(allShopsX[j]), Float.parseFloat(allShopsY[j]));
            if(PolyUtil.containsLocation(
                    Float.parseFloat(allShopsX[j]),
                    Float.parseFloat(allShopsY[j]), polygon.getPoints(), false)) {
                latLngsShopsInPolygon[j] = new LatLng(Float.parseFloat(allShopsX[j]), Float.parseFloat(allShopsY[j]));

                mMap.addMarker(new MarkerOptions()
                        .position((LatLng)latLngsShopsInPolygon[j])
                        .title(allShopsNames[j])
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                );
            }
        }

        for(int j = 0; j < parksLat.length; j++) {
            latLngsParks[j] = new LatLng(Float.parseFloat(parksLat[j]), Float.parseFloat(parksLong[j]));
            if(PolyUtil.containsLocation(
                    Float.parseFloat(parksLat[j]),
                    Float.parseFloat(parksLong[j]), polygon.getPoints(), false)) {
                latLngsParksInPolygon[j] = new LatLng(Float.parseFloat(parksLat[j]), Float.parseFloat(parksLong[j]));

                mMap.addMarker(new MarkerOptions()
                        .position((LatLng)latLngsParksInPolygon[j])
                        .title(parksNames[j])
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );
            }
        }

        for(int j = 0; j < busStopX.length; j++) {
            latLngsBusStops[j] = new LatLng(Float.parseFloat(busStopX[j]), Float.parseFloat(busStopY[j]));

            if(PolyUtil.containsLocation(
                    Float.parseFloat(busStopX[j]),
                    Float.parseFloat(busStopY[j]), polygon.getPoints(), false)) {
                latLngsBusStopsInPolygon[j] = new LatLng(Float.parseFloat(busStopX[j]), Float.parseFloat(busStopY[j]));

                mMap.addMarker(new MarkerOptions()
                        .position((LatLng)latLngsBusStopsInPolygon[j])
                        .title(busStopNames[j])
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.reticle))
                );
            }
        }

        for(int j = 0; j < recX.length; j++) {
            latLngsRecreation[j] = new LatLng(Float.parseFloat(recX[j]), Float.parseFloat(recY[j]));

            if(PolyUtil.containsLocation(
                    Float.parseFloat(recX[j]),
                    Float.parseFloat(recY[j]), polygon.getPoints(), false)) {
                latLngsRecreationInPolygon[j] = new LatLng(Float.parseFloat(recX[j]), Float.parseFloat(recY[j]));

                mMap.addMarker(new MarkerOptions()
                        .position((LatLng)latLngsRecreationInPolygon[j])
                        .title(recNames[j])
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );
            }
        }

        for(int j = 0; j < schoolsX.length; j++) {
            latLngsSchools[j] = new LatLng(Float.parseFloat(schoolsX[j]), Float.parseFloat(schoolsY[j]));

            if(PolyUtil.containsLocation(
                    Float.parseFloat(schoolsX[j]),
                    Float.parseFloat(schoolsY[j]), polygon.getPoints(), false)) {
                latLngsSchoolsInPolygon[j] = new LatLng(Float.parseFloat(schoolsX[j]), Float.parseFloat(schoolsY[j]));

                mMap.addMarker(new MarkerOptions()
                        .position((LatLng)latLngsSchoolsInPolygon[j])
                        .title(schoolsNames[j])
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                );
            }
        }
    }
}
