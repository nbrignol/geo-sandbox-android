package fr.nbrignol.geosandbox;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("POSITION", "Start");
        handleAccessPermissions(true);

        //LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    public void handleAccessPermissions(boolean requestPermissionsIfNeeded) {
        Log.d("POSITION", "Will check permissions...");

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        int fineLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        ArrayList<String> permissionsToRequest = new ArrayList<String>();

        //FINE LOCATION
        if (fineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            Log.d("POSITION", "FINE location ok.");

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
        } else {
            Log.d("POSITION", "FINE location nok.");

            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        //COARSE LOCATION
        if (coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            Log.d("POSITION", "COARSE location ok.");

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        } else {
            Log.d("POSITION", "COARSE location nok.");

            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionsToRequest.size() == 0) {
            return;
        }

        if (! requestPermissionsIfNeeded) {
            return;
        }

        Log.d("POSITION", "Need to ask some permissions.");
        String[] permissionsToRequestArray = permissionsToRequest.toArray(new String[0]);
        ActivityCompat.requestPermissions(this, permissionsToRequestArray, 0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("POSITION", "Received permissions results...");
        handleAccessPermissions(false);
    }


        @Override
    public void onLocationChanged(Location location) {
        String message = String.format("lat: %f, long: %f", location.getLatitude(), location.getLongitude());
        Log.d("POSITION", message);

        TextView label = (TextView) findViewById(R.id.label);
        label.setText(message);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("POSITION", "Status Changed " + provider + " " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("POSITION", "Provider enabled " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("POSITION", "Provider disabled " + provider);
    }

}
