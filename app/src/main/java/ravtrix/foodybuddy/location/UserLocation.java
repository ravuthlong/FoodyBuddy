package ravtrix.foodybuddy.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import ravtrix.foodybuddy.utils.Helpers;
import ravtrix.foodybuddy.utils.HelpersPermission;

/**
 * Created by Ravinder on 3/19/17.
 */

public class UserLocation {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private OnLocationReceived onLocationReceived;
    private Activity context;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private static final int LOCATION_REQUEST_CODE = 1;
    private static int ONE_SECOND = 1000;

    public UserLocation(Activity context) {
        this.context = context;
    }


    public void startLocationListener(OnLocationReceived onLocationReceived) {

        this.onLocationReceived = onLocationReceived;

        if (context != null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            setLocationListener();
            checkPermission();
        }
    }


    /**
     * Listener for location, listens for when location is changed
     */
    private void setLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                final double latitude = location.getLatitude();
                final double longitude = location.getLongitude();

                stopListener(); // stop getting location after location received

                // pass back to caller
                onLocationReceived.onLocationReceived(latitude, longitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    /**
     * Check if permission to use GPS is given, if not display option to enable
     */
    private void checkPermission() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Check for user's SDK Version. SDK version >= Marshmallow need permission access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {

                context.requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 0);
            } else {
                // request location
                requestLocation();
            }
        } else {
            // request location
            requestLocation();
        }
    }

    private void requestLocation() {

        try {

            if (!gps_enabled && !network_enabled) {

                AlertDialog.Builder builder = Helpers.showAlertDialogWithTwoOptions(context, "Location Service", "Turn on location service so other users can see your current country", "No");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onLocationReceived.onLocationReceived(0, 0); // permission denied, just use 0,0
                    }
                });
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // requesting permission to access location
                        HelpersPermission.showLocationRequest(context);
                    }
                });
                builder.show();

            } else {
                if (network_enabled){
                    // One_Second passed because on location changed can be called multiple times in a few users
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UserLocation.ONE_SECOND, 0, locationListener);
                }
                if (gps_enabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UserLocation.ONE_SECOND, 0, locationListener);
                }
            }

        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    private void stopListener() {
        try {
            // Turn off location listeners after successful location retrieval
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
                locationManager = null;
                locationListener = null;
                context = null;
            }
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }
}
