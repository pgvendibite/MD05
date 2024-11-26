package com.example.MD;
import android.app.Service;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class  EthernetService  extends Service {
    public static final String ACTION_NO_ETHERNET ="com.example.MD.ACTION_NO_ETHERNET";

    public static final String ACTION_ETHERNET_NOT_SUPPORTED = "com.example.MD.ACTION_ETHERNET_NOT_SUPPORTED";
    public static final int MESSAGE_FROM_SERIAL_PORT =100 ;
    public static final int CTS_CHANGE = 1;
    public static final int DSR_CHANGE = 2;
    //  public static final String ACTION_NO_ETHERNET = ;
    //public static final String ACTION_ETHERNET_NOT_SUPPORTED = ;
    // public static final String ACTION_ETHERNET_DISCONNECTED = ;
    private static final String TAG = "EthernetService";
    private static final String ACTION_ETHERNET_CONNECTED = "com.md_usb_pantro.ETHERNET_CONNECTED";
    public static final String ACTION_ETHERNET_DISCONNECTED = "com.md_usb_pantro.ETHERNET_DISCONNECTED";
    private static final int SERVER_PORT = 5000; // Set your server port
    public static String ACTION_Ethernet_PERMISSION_GRANTED;
    private Socket socket;
    private OutputStream outputStream;
    private BufferedReader inputStream;
    private Handler writeHandler;
    private final IBinder binder = new EthernetBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        connectToServer(); // Replace with your server's IP
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("192.168.0.100", SERVER_PORT);
                outputStream = socket.getOutputStream();
                inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.d(TAG, "Connected to server");

                Intent intent = new Intent(ACTION_ETHERNET_CONNECTED);
                sendBroadcast(intent);

                // Start reading data from the server
                readDataFromServer();
            } catch (IOException e) {
                Log.e(TAG, "Error connecting to server", e);
                Toast.makeText(this, "Error connecting to server", Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    private void readDataFromServer() {
        new Thread(() -> {
            String line;
            try {
                while ((line = inputStream.readLine()) != null) {
                    // Process incoming data
                    Log.d(TAG, "Received: " + line);
                    // Handle the data received
                }
            } catch (IOException e) {
                Log.e(TAG, "Error reading from server", e);
                Toast.makeText(this, "Disconnected from server", Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    public void sendData(String data) {
        new Thread(() -> {
            try {
                if (outputStream != null) {
                    outputStream.write((data + "\n").getBytes());
                    outputStream.flush();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error sending data", e);
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error closing socket", e);
        }
    }

    public class EthernetBinder extends Binder {
        public EthernetService getService() {
            return EthernetService.this;
        }
    }



}
