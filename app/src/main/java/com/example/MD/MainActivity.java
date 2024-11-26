package com.example.MD;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
//import com.macroyau.blue2serial.BluetoothDeviceListDialog;
//import com.macroyau.blue2serial.BluetoothSerial;
//import com.macroyau.blue2serial.BluetoothSerialListener;

import java.util.ArrayList;
import java.util.Set;

import static com.example.MD.Admin.i;
import static com.example.MD.HandleServer.PREFS_NAME;
import static com.example.MD.HandleServer.WIFI_CALLED_KEY;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
//import static com.example.MD.TerminalActivity.bluetoothSerial;

public class MainActivity extends AppCompatActivity  {
    Button freemode,ordernow;
    Animation blink;
    MediaPlayer mMediaPlayer;
    RelativeLayout relativeLayout;

    String progress;
    Dialog dialogpick,dialogerror,dialogover;
    int cnt1=0;

    ImageView Clean;
    ImageView logo;
    LinearLayout lyt_progress;
    Handler handlermain = new Handler();
    Runnable runnablemain;
    String wifipassword,wifiname;
    private boolean crlf = false;
    Dialog dialogstock;
    Context context;
    TextView parameters,cowsnf,cowdensity,cowprotein,cowfat,buffalosnf,buffalodensity,buffaloprotein,buffalofat;
    // A static or instance variable to check if the code has already run
    private boolean isRunFirst = true;

   int cnt2=0;

    public static int t = 0;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private HandleServer handleServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //remove title bar  from window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //used for the full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure you have a corresponding layout
        SharedPreferences sharedPreferences = getSharedPreferences("p006", MODE_PRIVATE);
        wifiname = sharedPreferences.getString("wifin","");
        wifipassword = sharedPreferences.getString("wifip","");
        String val = sharedPreferences.getString("valuee","");

        Log.d("wifi","this is wifi "+ wifiname+wifipassword);
        // Initialize HandleServer with context
        handleServer = new HandleServer(this);
        handleServer.turnonwifi(wifiname,wifipassword);


        context=this;

        //This method overrides the default transition  animation when starting a new ACTIVITY
        overridePendingTransition(0, 0);

        //these line loads an animation  resourse (defijned in animation blink.xml)these applied on UI element
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_blink);
        ordernow = findViewById(R.id.order_now);
        freemode = findViewById(R.id.freemode);
        logo = findViewById(R.id.logomain);
        parameters = findViewById(R.id.quality);



        if (val.equals("free")) {
            freemode.setVisibility(View.VISIBLE);
            freemode.setAnimation(blink);
            ordernow.setVisibility(View.GONE);
        }
        if (val.equals("paid")) {
            freemode.setVisibility(View.GONE);
            freemode.clearAnimation();
            ordernow.setAnimation(blink);
        }

        parameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }

        });


        freemode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Menu.class);
                startActivity(intent);
            }
        });

        ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

//-------------passcode----------
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i == 5) {
                    i = 0;
                    Intent i = new Intent(getApplicationContext(), Passcode.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
//-------------------------------------------------------------------
private void checkAndRequestPermissions() {
    // Check if ACCESS_FINE_LOCATION is granted
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

        // Permissions are not granted, request them
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                LOCATION_PERMISSION_REQUEST_CODE);
    } else {
        // Permissions are already granted, proceed with Wi-Fi operations
//        handleServer.scanNetworks();
        Log.d("permission","permission granted");
    }
}

    // Handle the permission request response
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with Wi-Fi operations
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
                Log.d("permission","permission granted");
            } else {
                // Permission denied, disable functionality that depends on this permission
                Toast.makeText(this, "Location Permission Denied. Unable to scan Wi-Fi networks.", Toast.LENGTH_LONG).show();
                // Optionally, you can guide the user to enable permissions in settings
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }
//    -----------------------------------+--------

    private void showExitConfirmationDialog() {
        // Create an AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Exit Application")
                .setMessage("Are you sure you want to exit the application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User confirmed, exit the application
                        finishAffinity(); // Closes all activities in the app
                        System.exit(0);  // Optional: forcefully exit the application
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled, just dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .show();
    }

//    private void showDeviceListDialog() {
//        ArrayList<BluetoothDevice> listItems = new ArrayList<>();
//        // Display dialog for selecting a remote Bluetooth device
//        bluetoothSerialmain.getPairedDevices();
//
//        Set<BluetoothDevice> val = bluetoothSerialmain.getPairedDevices();
//        if (subtitle != null) {
//            if (!subtitle.equalsIgnoreCase("Connected to "+BTname ))
//
//            {
//                if (val.size() >= 1) {
//                    bluetoothSerialmain.connect(BTaddress);
//
//                }
//            } else if (subtitle.equalsIgnoreCase("Connected to "+BTname)) {
//                handlermain.removeCallbacks(runnablemain);
//
//            }
//        }
//
//    }





//    @Override
//    public void onBluetoothDeviceSelected(BluetoothDevice device) {
//        bluetoothSerialmain.connect(device);
//    }
//
//    @Override
//    public void onBluetoothNotSupported() {
//
//    }
//
//    @Override
//    public void onBluetoothDisabled() {
//
//    }
//
//    @Override
//    public void onBluetoothDeviceDisconnected() {
//        updateBluetoothState();
//    }
//
//    @Override
//    public void onConnectingBluetoothDevice() {
//        updateBluetoothState();
//    }
//
//    @Override
//    public void onBluetoothDeviceConnected(String name, String address) {
//        updateBluetoothState();
//    }

//    @Override
//    public void onBluetoothSerialRead(String message) {
//        // Print the incoming message on the terminal screen
//
//        Log.d("Namess", " R" + message);
//
//        if (message.equalsIgnoreCase("B11,B,B")) {
//            try {
//                //Updatestatusnotrefiled();
//               // Updatestatusactive();
//                dialogpick.dismiss();
//                dialogerror.dismiss();
//                dialogover.dismiss();
//                dialogstock.dismiss();
//                relativeLayout.setVisibility(View.VISIBLE);
//
//
//            } catch (Exception e) {}
//
//        }
//        else if (message.equalsIgnoreCase("B5,B,B")) {
//            //Updatestatusactive();
//
//            parameters.setVisibility(View.INVISIBLE);
//            relativeLayout.setVisibility(View.GONE);
//            lyt_progress.setVisibility(View.VISIBLE);
//        }
//     /*   else  if (message.equalsIgnoreCase("B6,B,B"))
//        {
//            pickdish();
//        }*/
//        else if (message.equalsIgnoreCase("B,B,B3"))
//        {
//            cnt1++;
//            if (cnt1 == 1) {
//
//                Errorfound();
//            }
//        }
//        else if (message.equalsIgnoreCase("B,B22,B"))
//        {
//            stockrefiled();
//            //Updatesta tusnotrefiled();
//
//
//        }
//      /*  else if (message.equalsIgnoreCase("B1,B,B"))
//        {
//            Pleasewait();
//        }*/
//        else if (message.equalsIgnoreCase("B,B2,B"))
//        {
//            cnt2++;
//            if(cnt2==1) {
//                stockover();
//                //Updatestatusrefiled();
//            }
//
//        }
//    }

//    @Override
//    public void onBluetoothSerialWrite(String message) {
//        Log.d("Names","W Main"+message);
//    }
//
//    private void updateBluetoothState() {
//        // Get the current Bluetooth state
//        final int state;
//        if (bluetoothSerialmain != null)
//            state = bluetoothSerialmain.getState();
//        else
//            state = BluetoothSerial.STATE_DISCONNECTED;
//        ordernow.setVisibility(View.INVISIBLE);
//        // Display the current state on the app bar as the subtitle
//        switch (state) {
//            case BluetoothSerial.STATE_CONNECTING:
//                subtitle = getString(R.string.status_connecting);
//                break;
//            case BluetoothSerial.STATE_CONNECTED:
//                subtitle = getString(R.string.status_connected, bluetoothSerialmain.getConnectedDeviceName());
//                ordernow.setVisibility(View.VISIBLE);
//                ordernow.startAnimation(blink);
//
//
//                break;
//            default:
//                subtitle = getString(R.string.status_disconnected);
//                ordernow.setVisibility(View.GONE);
//
//                break;
//        }
//
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setSubtitle(subtitle);
//        }
//    }

//    private void pairpopup() {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//        dialog.setContentView(R.layout.pairpopup);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(false);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        Button button = dialog.findViewById(R.id.pair);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                dialog.dismiss();
//                startActivityForResult(new Intent(Settings.ACTION_SETTINGS),0);
//            }
//        });
//        if(!((Activity) context).isFinishing())
//        {
//            //show dialog
//            dialog.show();
//        }
//
//    }

    private void Errorfound() {
       dialogerror = new Dialog(this);
        dialogerror.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogerror.setContentView(R.layout.errorfound);
        dialogerror.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogerror.setCancelable(true);
        dialogerror.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogerror.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button button = dialogerror.findViewById(R.id.okbtn);
//        button.setOnClickListener(new View.OnClickListener() {
////            @Override
//            public void onClick(View view) {
//
//                dialogerror.dismiss();
//                bluetoothSerialmain.write("(0,0,0,A,0,0)", crlf);
//               /* Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);*/
//                try {
//                    bluetoothSerial.stop();
//                }catch (Exception e){}
//
//            }
//        });

        if(!((Activity) context).isFinishing())
        {
            //show dialog
            dialogerror.show();
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private void stockrefiled() {
     dialogstock = new Dialog(this);
        dialogstock.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogstock.setContentView(R.layout.stockrefailed);
        dialogstock.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogstock.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogstock.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button button = dialogstock.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogstock.dismiss();

            }
        });
        if(!((Activity) context).isFinishing())
        {
            //show dialog
            dialogstock.show();
        }
    }



    private void stockover() {
         dialogover = new Dialog(this);
        dialogover.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogover.setContentView(R.layout.stockover);
        dialogover.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogover.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogover.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Button button = dialogover.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogover.dismiss();

            }
        });
        if(!((Activity) context).isFinishing())
        {
            //show dialog
            dialogover.show();
        }
    }

    private void dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.snf);
        cowsnf = dialog.findViewById(R.id.cowsnf);
        cowdensity = dialog.findViewById(R.id.cowdensity);
        cowprotein = dialog.findViewById(R.id.cowprotein);
        cowfat = dialog.findViewById(R.id.cowfat);
        buffalosnf = dialog.findViewById(R.id.buffalosnf);
        buffalodensity = dialog.findViewById(R.id.buffalodensity);
        buffaloprotein = dialog.findViewById(R.id.buffaloprotein);
        buffalofat = dialog.findViewById(R.id.buffalofat);

        SharedPreferences sharedPreferences = getSharedPreferences("p006", MODE_PRIVATE);
        String snf1 = sharedPreferences.getString("cowSNF", "");
        cowsnf.setText("COW SNF : "     + snf1);
        String density1 = sharedPreferences.getString("cowDensity", "");
        cowdensity.setText("COW DENSITY : " + density1);
        String protein1 = sharedPreferences.getString("cowProtein", "");
        cowprotein.setText( "COW PROTEIN : " + protein1);
        String fat1 = sharedPreferences.getString("cowFat", "");
        cowfat.setText("COW FAT : " + fat1);
        String snf2 = sharedPreferences.getString("buffaloSNF", "");
        buffalosnf.setText("BUFFALO SNF : " + snf2);
        String density2 = sharedPreferences.getString("buffaloDensity", "");
        buffalodensity.setText("BUFFALO DENSITY : " + density2);
        String protein2 = sharedPreferences.getString("buffaloProtein", "");
        buffaloprotein.setText( "BUFFALO PROTEIN : " + protein2);
        String fat2 = sharedPreferences.getString("buffaloFat", "");
        buffalofat.setText("BUFFALO FAT : " + fat2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;


        dialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Reset the Wi-Fi called flag
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(WIFI_CALLED_KEY, false);
        editor.apply();
    }


}