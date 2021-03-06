package jp.project_p.d.prop_d;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;

import java.util.Random;


public class MapsActivity extends FragmentActivity  {

    private DatabaseHelper sqlHelper;
    private SQLiteDatabase db;

    private double university_mLatitude = 35.62519765707683;
    private double university_mLongitude = 139.34302747249603;
    private double ball_mLatitude = 35.6246621929409;
    private double ball_mLongitude = 139.34279680252075;
    private double castle_mLatitude = 35.625917997814696;
    private double castle_mLongitude = 139.3448030948639;
    private GoogleMap mMap = null; // Might be null if Google Play services APK is not available.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("aaaaaaaaa", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        Log.d("aaaaaaaaa", "onResume");
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        Log.d("aaaaaaaaa", "setUpMapIfNeeded");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            Log.d("aaaaaaaaa", "setUpMapIfNeeded NULLの場合１");
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                Log.d("aaaaaaaaa", "setUpMapIfNeeded NULLの場合２");
                setUpMap();
            }
        }
        Log.d("aaaaaaaaa", "setUpMapIfNeeded NULLのじゃない場合");
    }
    private void image_overray(double mLatitude, double mLongitude) {
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    private void setUpMap() {
        Log.d("aaaaaaaaa", "setUpMap");

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getReadableDatabase();

        LatLng university_location = new LatLng(university_mLatitude, university_mLongitude);
        LatLng ball_location = new LatLng(ball_mLatitude, ball_mLongitude);
        LatLng castle_location = new LatLng(castle_mLatitude, castle_mLongitude);
        CameraPosition cameraPos = new CameraPosition.Builder()
                .target(university_location).zoom(12.0f)
                .bearing(0).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
        Random r = new Random();
        MarkerOptions options = new MarkerOptions();
        String[] columns = new String[]{"latitude","longitude"};
                /*Cursor cursor = db.query("ball", columns, null,
                        null, null, null, null, "1");*/
        String sqlstr = "select latitude, longitude from ball";
        Cursor cursor = db.rawQuery(sqlstr, null);
        while(cursor.moveToNext()) {
            options.position(new LatLng((cursor.getDouble(cursor.getColumnIndex("latitude"))), (cursor.getDouble(cursor.getColumnIndex("longitude")))));
            options.title("ボール");
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ball));
            options.anchor(0.5f, 0.5f);
            mMap.addMarker(options);
        }
        options.position(castle_location);
        options.title("城");
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.castle));
        options.anchor(0.5f, 0.5f);
        mMap.addMarker(options);

        options.position(ball_location);
        options.title("ボール");
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ball));
        options.anchor(0.5f, 0.5f);
        mMap.addMarker(options);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("aaaaaaaaa", "onMarkerClick");
                // TODO Auto-generated method stub
                String id = marker.getId();
                Log.d("aaaaaaaaa", id);
                if (id.equals("m0")) {
                    Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                    startActivityForResult(intent, 123);
                } else if (id.equals("m1")) {
                    Toast.makeText(getApplicationContext(), "城をタップしました。", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "（＾ｐ＾）", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(university_mLatitude, university_mLongitude))
                .title("講義実験棟")
                .draggable(true));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private class SQLHelper {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("aaaaaaaaa", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();

            switch (requestCode) {
                case 123:
                    if (resultCode == RESULT_OK) {
                        /*mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(bundle.getDouble("key.latitude"), bundle.getDouble("key.longitude")))
                                .title("ボール")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ball))
                                .anchor(0.5f, 0.5f));*/
                        ball_mLatitude = bundle.getDouble("key.latitude");
                        ball_mLongitude = bundle.getDouble("key.longitude");
                        mMap.clear();
                        mMap = null;
                    }
                    break;
            }
        }
    }

}
