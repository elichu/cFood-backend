package ca.bcit.ass3.location_test;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import com.google.android.gms.maps.GoogleMap;


/**
 * Created by E on 2017-11-15.
 */

public class PopulateDBActivity extends ListActivity {

    private ZoneHelper openHelper;
    private String[][] zoneArray;
    private String TAG = PopulateDBActivity.class.getSimpleName();
    private ListView lv;
    SQLiteDatabase db;

    String zoneCoords = null;
    String[] zoneLat = null;
    String[] zoneLong = null;

    ArrayList<String> shopsNames = new ArrayList<String>();
    ArrayList<String> shopsX = new ArrayList<String>();
    ArrayList<String> shopsY = new ArrayList<String>();

    ArrayList<String> busStopNames = new ArrayList<String>();
    ArrayList<String> busStopX = new ArrayList<String>();
    ArrayList<String> busStopY = new ArrayList<String>();

    ArrayList<String> recNames = new ArrayList<String>();
    ArrayList<String> recX = new ArrayList<String>();
    ArrayList<String> recY = new ArrayList<String>();

    ArrayList<String> schoolsNames = new ArrayList<String>();
    ArrayList<String> schoolsX = new ArrayList<String>();
    ArrayList<String> schoolsY = new ArrayList<String>();

    ArrayList<String> parksNames = new ArrayList<String>();
    String[] parksLat = null;
    String[] parksLong = null;
    ArrayList<String> parksCoords = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        openHelper = new ZoneHelper(this);

        //init(); //uncomment this to populate the database with JSON data. 

        getZone("Downtown");
        getShops();
        getParks();
        getBusStops();
        getSchools();
        getRecreation();

        Intent intent = new Intent(PopulateDBActivity.this, MapsActivity.class);
        intent.putExtra("coordsLat", zoneLong);
        intent.putExtra("coordsLong", zoneLat);

        intent.putExtra("shopsX", shopsX.toArray(new String[shopsX.size()]));
        intent.putExtra("shopsY", shopsY.toArray(new String[shopsY.size()]));
        intent.putExtra("shopsNames",  shopsNames.toArray(new String[shopsNames.size()]));

        intent.putExtra("parksLat", parksLat);
        intent.putExtra("parksLong", parksLong);
        intent.putExtra("parksNames", parksNames.toArray(new String[parksNames.size()]));

        intent.putExtra("busStopX", busStopX.toArray(new String[busStopX.size()]));
        intent.putExtra("busStopY", busStopY.toArray(new String[busStopY.size()]));
        intent.putExtra("busStopNames", busStopNames.toArray(new String[busStopNames.size()]));

        intent.putExtra("recX", recX.toArray(new String[recX.size()]));
        intent.putExtra("recY", recY.toArray(new String[recY.size()]));
        intent.putExtra("recNames", recNames.toArray(new String[recNames.size()]));

        intent.putExtra("schoolsX", schoolsX.toArray(new String[schoolsX.size()]));
        intent.putExtra("schoolsY", schoolsY.toArray(new String[schoolsY.size()]));
        intent.putExtra("schoolsNames", schoolsNames.toArray(new String[schoolsNames.size()]));

        startActivity(intent);

    }

    private void getSchools() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllSchools(db);
        String name = null;
        String xValue = null;
        String yValue = null;

        while (cursor.moveToNext() ) {
            name = cursor.getString(1);
            xValue = cursor.getString(2);
            yValue = cursor.getString(3);
            schoolsNames.add(name);
            schoolsX.add(xValue);
            schoolsY.add(yValue);
        }
        cursor.close();
    }

    private void getRecreation() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllRec(db);
        String name = null;
        String xValue = null;
        String yValue = null;

        while (cursor.moveToNext() ) {
            name = cursor.getString(1);
            xValue = cursor.getString(2);
            yValue = cursor.getString(3);
            recNames.add(name);
            recX.add(xValue);
            recY.add(yValue);
        }
        cursor.close();
    }

    private void getBusStops() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllBusStops(db);
        String name = null;
        String xValue = null;
        String yValue = null;

        while (cursor.moveToNext() ) {
            name = cursor.getString(1);
            xValue = cursor.getString(2);
            yValue = cursor.getString(3);
            busStopNames.add(name);
            busStopX.add(xValue);
            busStopY.add(yValue);
        }
        cursor.close();
    }

    private void getParks() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllParks(db);
        String name = null;
        String xValue = null;
        String yValue = null;
        int count = 0;

        while (cursor.moveToNext() ) {
            parksCoords.add(cursor.getString(2));
            if(cursor.getString(1) == null || cursor.getString(1) == "") {
                parksNames.add("Park");
            } else {
                parksNames.add(cursor.getString(1));
            }
            count++;
        }
        cursor.close();

        parksLat = new String[parksCoords.size()];
        parksLong = new String[parksCoords.size()];
        for (int j = 0; j < parksCoords.size(); j++) {
            try {
                JSONArray array = new JSONArray(parksCoords.get(j));
                JSONArray array2 = new JSONArray(array.getJSONArray(0).toString());
                int length = array2.length();
                    JSONArray array3 = new JSONArray(array2.getJSONArray(0).toString());
                    parksLat[j] = array3.get(1).toString();
                    parksLong[j] = array3.get(0).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getShops() {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getAllShops(db);
        String name = null;
        String xValue = null;
        String yValue = null;

        while (cursor.moveToNext() ) {
            name = cursor.getString(1);
            xValue = cursor.getString(2);
            yValue = cursor.getString(3);
            shopsNames.add(name);
            shopsX.add(xValue);
            shopsY.add(yValue);
        }
        cursor.close();
    }

    private void getZone(String zone) {
        db = openHelper.getReadableDatabase();
        Cursor cursor = openHelper.getZoneCoords(db, zone);

        if (cursor.moveToFirst() ) {
            zoneCoords = cursor.getString(2);
        }
        cursor.close();

        try {
            JSONArray array = new JSONArray(zoneCoords);
            JSONArray array2 = new JSONArray(array.getJSONArray(0).toString());
            zoneLat = new String[array2.length()];
            zoneLong = new String[array2.length()];
            // zoneCoords = new String[array2.length()][2];
            int length = array2.length();
            for(int i = 0; i < length; i++) {
                JSONArray array3 = new JSONArray(array2.getJSONArray(i).toString());
                zoneLong[i] = array3.get(1).toString();
                zoneLat[i] = array3.get(0).toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void retrieveAll() {

        SQLiteDatabase db;
        Cursor cursor;

        db = openHelper.getWritableDatabase();
        cursor = openHelper.getAllZones(db);
        int i = 0;
        while(cursor.moveToNext())
        {
            String zoneType;
            String zoneCoord;

            zoneType = cursor.getString(0);
            zoneCoord = cursor.getString(1);

            zoneArray[i][0] = zoneType;
            zoneArray[i][1] = zoneCoord;
            i++;
            //Log.d(TAG, zoneType);
        }

        cursor.close();

    }

    private void init() {

        SQLiteDatabase db;

        db = openHelper.getWritableDatabase();
        ArrayList<String[][]> jsonResult = getJSONString();
        String[][] zones = jsonResult.get(0);
        String[][] bus = jsonResult.get(1);
        String[][] shops = jsonResult.get(2);
        String[][] parks = jsonResult.get(3);
        String[][] rec = jsonResult.get(4);
        String[][] schools = jsonResult.get(5);

        for(int i = 0; i < zones.length; i++) {
            db.beginTransaction();
            try {
                openHelper.insertZone(db, zones[i][0], zones[i][1]);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        for(int i = 0; i < parks.length; i++) {
            db.beginTransaction();
            try {
                openHelper.insertPark(db, parks[i][0], parks[i][1]);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        for(int i = 0; i < shops.length; i++) {
            db.beginTransaction();
            try {
                openHelper.insertShop(db, shops[i][0], shops[i][1], shops[i][2]);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        for(int i = 0; i < bus.length; i++) {
            db.beginTransaction();
            try {
                openHelper.insertBusStop(db, bus[i][0], bus[i][1], bus[i][2]);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        for(int i = 0; i < rec.length; i++) {
            db.beginTransaction();
            try {
                openHelper.insertRec(db, rec[i][0], rec[i][1], rec[i][2]);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        for(int i = 0; i < schools.length; i++) {
            db.beginTransaction();
            try {
                openHelper.insertSchool(db, schools[i][0], schools[i][1], schools[i][2]);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    public ArrayList<String> importJSON() {
        ArrayList<String> jsonList = new ArrayList();
        try {
            AssetManager assets = getAssets();
            String [] allFiles = assets.list("");

            for(int i = 0; i < allFiles.length; i++) {
                if(allFiles[i].contains(".json")) {
                    InputStream is = getAssets().open(allFiles[i]);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    jsonList.add(new String(buffer, "UTF-8"));
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonList;
    }

    public ArrayList<String[][]> getJSONString() {

        ArrayList<String[][]> allJson = new ArrayList<String[][]>();
        ArrayList <String> jsonImport = importJSON();
        Log.e(TAG, "Response from url: " + jsonImport);
        String[][] zoneArray = null;
        String[][] parksArray = null;
        String[][] busArray = null;
        String[][] recreationArray = null;
        String[][] schoolsArray = null;
        String[][] shoppingArray = null;

        try {
            //for(int j = 0; j < jsonImport.size(); j++) {

            JSONArray zoneJSONArray = new JSONArray(jsonImport.get(5));
            zoneArray = new String[zoneJSONArray.length()][2];
            for (int i = 0; i < zoneJSONArray.length(); i++) {
                JSONObject obj = zoneJSONArray.getJSONObject(i);

                String category = obj.getString("NEIGH_NAME");
                String coords = obj.getJSONObject("json_geometry")
                        .getJSONArray("coordinates").toString();

                zoneArray[i][0] = category;
                zoneArray[i][1] = coords;
            }

            JSONArray busJSONArray = new JSONArray(jsonImport.get(0));
            busArray = new String[busJSONArray.length()][3];
            for (int i = 0; i < busJSONArray.length(); i++) {
                JSONObject obj = busJSONArray.getJSONObject(i);

                String category = obj.getString("BUSSTOPNUM");
//                String coords = obj.getJSONObject("json_geometry")
//                        .getJSONArray("coordinates").toString();
                String longitude = obj.getString("X");
                String latitude = obj.getString("Y");

                busArray[i][0] = category;
                busArray[i][1] = latitude;
                busArray[i][2] = longitude;
            }

            JSONArray shopJSONArray = new JSONArray(jsonImport.get(1));
            shoppingArray = new String[shopJSONArray.length()][3];
            for (int i = 0; i < shopJSONArray.length(); i++) {
                JSONObject obj = shopJSONArray.getJSONObject(i);

                String category = obj.getString("BLDGNAM");
//                String coords = obj.getJSONObject("json_geometry")
//                        .getJSONArray("coordinates").toString();
                String longitude = obj.getString("X");
                String latitude = obj.getString("Y");

                shoppingArray[i][0] = category;
                shoppingArray[i][1] = latitude;
                shoppingArray[i][2] = longitude;
            }

            JSONArray parksJSONArray = new JSONArray(jsonImport.get(2));
            parksArray = new String[parksJSONArray.length()][2];
            for (int i = 0; i < parksJSONArray.length(); i++) {
                JSONObject obj = parksJSONArray.getJSONObject(i);

                String category = obj.getString("Name");
                String coords = obj.getJSONObject("json_geometry")
                        .getJSONArray("coordinates").toString();

                parksArray[i][0] = category;
                parksArray[i][1] = coords;
            }

            JSONArray recJSONArray = new JSONArray(jsonImport.get(3));
            recreationArray = new String[recJSONArray.length()][3];
            for (int i = 0; i < recJSONArray.length(); i++) {
                JSONObject obj = recJSONArray.getJSONObject(i);

                String category = obj.getString("Name");
//                String coords = obj.getJSONObject("json_geometry")
//                        .getJSONArray("coordinates").toString();
                String longitude = obj.getString("X");
                String latitude = obj.getString("Y");

                recreationArray[i][0] = category;
                recreationArray[i][1] = latitude;
                recreationArray[i][2] = longitude;
            }

            JSONArray schoolsJSONArray = new JSONArray(jsonImport.get(4));
            schoolsArray = new String[schoolsJSONArray.length()][3];
            for (int i = 0; i < schoolsJSONArray.length(); i++) {
                JSONObject obj = schoolsJSONArray.getJSONObject(i);

                String category = obj.getString("BLDGNAM");
//                String coords = obj.getJSONObject("json_geometry")
//                        .getJSONArray("coordinates").toString();
                String longitude = obj.getString("X");
                String latitude = obj.getString("Y");

                schoolsArray[i][0] = category;
                schoolsArray[i][1] = latitude;
                schoolsArray[i][2] = longitude;
            }

            allJson.add(zoneArray);
            allJson.add(busArray);
            allJson.add(shoppingArray);
            allJson.add(parksArray);
            allJson.add(recreationArray);
            allJson.add(schoolsArray);

            //  }
        }catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
        return allJson;
    }
}


