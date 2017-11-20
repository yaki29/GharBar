package com.example.user.gharbar.Utilities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 29/10/17.
 */

public class GeocodingLocation {

    private static final String TAG = "GeocodingLocation";


    public static String getAddressFromLocation(final String locationAddress,


                                                final Context context) {




        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        String result = null;

        try {

            List addressList = geocoder.getFromLocationName(locationAddress, 1);

            if (addressList != null && addressList.size() > 0)

            {
                Address address = (Address) addressList.get(0);

                StringBuilder sb = new StringBuilder();

                sb.append(address.getLatitude()).append(" ");


                sb.append(address.getLongitude());

                result = sb.toString();

            }

        } catch (IOException e)

        {


            Log.e(TAG, "Unable to connect to Geocoder", e);

        } finally

        {



            if (result != null)

            {

                Log.d("LogH", "result" + result);


            } else

            {



            }


        }



        return result;
    }
}
