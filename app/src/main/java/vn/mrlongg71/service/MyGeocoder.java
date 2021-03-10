package vn.mrlongg71.service;

import android.location.Address;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyGeocoder {
    public static final String TAG = MyGeocoder.class.getSimpleName();

    static OkHttpClient client = new OkHttpClient();

    public static List<Address> getFromLocation(double lat, double lng, int maxResult) {

        String address = String.format(Locale.getDefault(),
                "https://maps.googleapis.com/maps/api/geocode/json?latlng=&sensor=false&language="
                        + Locale.getDefault().getCountry(), lat, lng);
        Log.d(TAG, "address = " + address);
        Log.d(TAG, "Locale.getDefault().getCountry() = " + Locale.getDefault().getCountry());

        return getAddress(address, maxResult);

    }

    public static List<Address> getFromLocationName(String locationName, int maxResults) {

        String address = null;
        try {
            address = "https://maps.google.com/maps/api/geocode/json?address=" + URLEncoder.encode(locationName,
                    "UTF-8") + "&ka&sensor=false";
            return getAddress(address, maxResults);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Address> getAddress(String url, int maxResult) {
        List<Address> retList = null;

        Request request = new Request.Builder().url(url)
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json; q=0.5")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseStr = response.body().string();
            JSONObject jsonObject = new JSONObject(responseStr);

            retList = new ArrayList<Address>();

            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                JSONArray results = jsonObject.getJSONArray("results");
                if (results.length() > 0) {
                    for (int i = 0; i < results.length() && i < maxResult; i++) {
                        JSONObject result = results.getJSONObject(i);
                        Address addr = new Address(Locale.getDefault());

                        JSONArray components = result.getJSONArray("address_components");
                        String streetNumber = "";
                        String route = "";
                        for (int a = 0; a < components.length(); a++) {
                            JSONObject component = components.getJSONObject(a);
                            JSONArray types = component.getJSONArray("types");
                            for (int j = 0; j < types.length(); j++) {
                                String type = types.getString(j);
                                if (type.equals("locality")) {
                                    addr.setLocality(component.getString("long_name"));
                                } else if (type.equals("street_number")) {
                                    streetNumber = component.getString("long_name");
                                } else if (type.equals("route")) {
                                    route = component.getString("long_name");
                                }
                            }
                        }
                        addr.setAddressLine(0, route + " " + streetNumber);

                        addr.setLatitude(
                                result.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                        addr.setLongitude(
                                result.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                        retList.add(addr);
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error calling Google geocode webservice.", e);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing Google geocode webservice response.", e);
        }

        return retList;
    }
}
