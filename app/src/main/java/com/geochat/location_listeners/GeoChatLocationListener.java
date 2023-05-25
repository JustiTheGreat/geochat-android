package com.geochat.location_listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.geochat.preference_managers.PreferenceManager;
import com.geochat.tasks.GetServerByCoordinatesTask;
import com.geochat.ui.activities.MainActivity;
import com.geochat.ui.fragments.Chats;

public class GeoChatLocationListener implements LocationListener {
    private final MainActivity mainActivity;

    public GeoChatLocationListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (!PreferenceManager.authTokenIsAvailable(mainActivity) || !(mainActivity.getCurrentFragment() instanceof Chats))
            return;

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        new GetServerByCoordinatesTask(mainActivity.getCurrentFragment(), PreferenceManager.getAuthToken(mainActivity), latitude, longitude).execute();
//        /*------- To get city name from coordinates -------- */
//        String cityName = null;
//        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//        List<Address> addresses;
//        try {
//            addresses = gcd.getFromLocation(loc.getLatitude(),
//                    loc.getLongitude(), 1);
//            if (addresses.size() > 0) {
//                System.out.println(addresses.get(0).getLocality());
//                cityName = addresses.get(0).getLocality();
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                + cityName;
//        editLocation.setText(s);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
