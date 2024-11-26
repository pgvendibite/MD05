package com.example.MD;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandleServer {

    private WifiManager wifiManager;
    private ExecutorService executorService;
    static final String PREFS_NAME = "AppPrefs";
    static final String WIFI_CALLED_KEY = "WifiCalled";
    private String ipaddress;
    private static final String TAG = "HandleServer";
    private Context context;

    public HandleServer(Context context) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void turnonwifi(String wifissid, String wifipassword) {
            final String[] finalResult = {""};
            executorService.execute(() -> {
                String result;
                try {
                    // Construct the URL for sending the command
                    String urlString = "http://" + "192.168.0.100" + ":" + 5000;
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    // Read the response from the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    result = response.toString();
                } catch (Exception e) {
                    result = "Error sending command";
                }
                // Handle server response on the UI thread
                finalResult[0] = result;
                Log.d("Server response: ", finalResult[0]);
                if (Objects.equals(finalResult[0], "ready")) {
                    // Proceed with Wi-Fi setup
//                    if (wifiManager.isWifiEnabled()) {
//                        wifiManager.setWifiEnabled(false);  // Turn off Wi-Fi if it's enabled
//                    }

                    final String[] finalResultdowneth = {""};
                    executorService.execute(() -> {
                        String resultdownth;
                        try {
                            // Construct the URL for sending the command
                            String urlString = "http://" + "192.168.0.100" + ":" + 5000 + "/turndowneth?wifidata=123,12345678";
                            URL url = new URL(urlString);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");

                            // Read the response from the server
                            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();

                            resultdownth = response.toString();
                        } catch (Exception e) {
                            resultdownth = "Error sending command";
                        }

                        // Handle server response on the UI thread
                        finalResultdowneth[0] = resultdownth;
                        Log.d("Server responseof eth: ", finalResultdowneth[0]);
                        connectToSpecificWifi(wifissid, wifipassword);

                    });
                }else
                {
                    Log.d("ethernet ","ethernet not found skiping the step");
                }
            });
    }


    private void connectToSpecificWifi(String ssid, String password) {
        try {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            Log.d("TAG", "Attempting to connect to " + ssid);

            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = String.format("\"%s\"", ssid);
            wifiConfig.preSharedKey = String.format("\"%s\"", password);

            int netId = wifiManager.addNetwork(wifiConfig);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();

            Thread.sleep(5000);

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null && wifiInfo.getSSID().equals("\"" + ssid + "\"")) {
                Log.d("TAG", "Successfully connected to " + ssid);
            } else {
                Log.e("TAG", "Failed to connect to " + ssid);
            }

        } catch (Exception e) {
            Log.e("TAG", "Error connecting to Wi-Fi: " + e.getMessage());
        }
    }

    public String getmyip() {
        return "192.168.0.101";
    }
}
