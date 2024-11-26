package com.example.MD;

import androidx.appcompat.app.AlertDialog;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.macroyau.blue2serial.BluetoothDeviceListDialog;
//import com.macroyau.blue2serial.BluetoothSerial;
//import com.macroyau.blue2serial.BluetoothSerialListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.MD.Menu.hasordecodeliststock;

import pl.droidsonroids.gif.GifImageView;
//import static com.example.MD.MainActivity.bluetoothSerialmain;
import static com.example.MD.Menu.hasordecode;

public class  TerminalActivity extends AppCompatActivity {
    TextView display;
    private ScrollView svTerminal;
    private TextView tvTerminal;
    private EditText etSend;
    private MenuItem actionConnect, actionDisconnect;
    private boolean crlf = false;
    TextView plswait;
    Handler handler = new Handler();
    Handler handlerorder = new Handler();
    Handler handlerordercompleted = new Handler();
    Handler handlerorderprocess = new Handler();
    Runnable runnable, runnableorder, runnableordercompleted;
    int delay = 1000;
    Dialog dialogpick, dialogpleasewait, orderlist, dialog1, dialogbottle, dialogsuccs, dialogsuccs1, dialogsuccs3, dialogsuccs6, dialogsuccs33;
    GifImageView GIFProcess;
    LinearLayout lyt_progress;
    String BTaddress, BTname;
    int delayorder = 13000;
    int delayordercompleted = 2000;
    int delayordercompletedlist = 1000;
    String subtitle;
    int cnt = 0;
    int cnt1 = 0;
    int cnt2 = 0;
    int cnt3 = 0;
    TextView textView;
    MediaPlayer pleasewaitaudio, dishreadyaudio, thankyouaudio, supportaudio;
    String dish_1, dish_2, dish_3, dish_4, dish_5;
    ProgressDialog progressDialog;
    Context context;
    String Modelno, ModelName;
    Animation blink;
    private HandleServer handleServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_terminal);
        context = this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        // Find UI views and set listeners
        plswait = findViewById(R.id.pleasewait);
        svTerminal = (ScrollView) findViewById(R.id.terminal);
        tvTerminal = (TextView) findViewById(R.id.tv_terminal);
        etSend = (EditText) findViewById(R.id.et_send);
        lyt_progress = findViewById(R.id.lyt_progress);
        GIFProcess = findViewById(R.id.gif);
        textView = findViewById(R.id.textviewprogress);
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_blink);
        SharedPreferences sharedPreferences = getSharedPreferences("p006", MODE_PRIVATE);

        updatestock();

        try {
            pleasewaitaudio = new MediaPlayer();
            pleasewaitaudio = MediaPlayer.create(this, R.raw.its_being_prepaired_pls_wait);
            pleasewaitaudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            pleasewaitaudio.setLooping(false);
            pleasewaitaudio.start();
        } catch (Exception e) {
        }
        updatestock();

        String bitdata = hasordecode.get("D");
        if (bitdata != null) {
            Log.d("Code", bitdata);
        } else {
            Log.d("Code", "bitdata is null");
        }

        if (bitdata != null) {
            sendCommandToServer(bitdata.getBytes());
        } else {
            Log.e("Error", "bitdata is null, cannot send command");
        }

        // Start checking
        checkReceivedData();

    }

    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }
    private void showExitConfirmationDialog() {
        // Create an AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Exit Application")
                .setMessage("Are you sure you want to exit the application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User confirmed, exit the application
                        finish(); // Close the activity
                        System.exit(0); // Optional: forcefully exit the application
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


    @Override
    public void onResume() {
        super.onResume();
//        setFilters();  // Start listening notifications from UsbService
//        startService(EthernetService.class, EthernetConnection, null);

// Start UsbService(if it was not started before) and Bind it
    }

    // Method to check received data and handle the loop

    String receivedBitData = "B11";
    int count = 0;
    Handler handlerforcheck = new Handler();
    final String[] temp = {""};

    private void checkReceivedData() {
        getCommandfromServer(new CommandCallback() {

            @Override
            public void onResult(String result) {
                if (result == null) {
                    // Stop the loop if there's an error (null result)
                    Log.d("Error", "Error occurred while getting data from server");
                    return; // Exit the loop
                }
                count++;
                if (count == 50) {
                    Log.d("Stopping", "So many time API hit stopping!");
                    return;
                }
                receivedBitData = result.substring(1, result.length() - 1);
                Log.d("temp", temp[0]);
                Log.d("result",receivedBitData);
                if(temp[0].equals(receivedBitData)){
                    Log.d("same","same result do nothing");
                } else{
                    // Update receivedBitData with the server result
                    Log.d("recieved bitsdata",receivedBitData);

                    if (receivedBitData.equals("B11")) {
                        try {  // showDialogProductYellow();
                            new Handler().postDelayed(new Runnable() {
                                private EthernetService sendCommandToServer;

                                @Override
                                public void run() {
                                    handler.removeCallbacks(runnable);
                                    int cnt4 = 0;
                                    cnt4++;
                                    if (cnt4 == 1) {
                                        String bitdata = hasordecode.get("D");
                                        Log.e("Code", bitdata);
                                        // if UsbService was correctly binded, Send data
                                        //                                        sendCommandToServer.write(bitdata.getBytes());
                                        BreakIterator display = null;
                                        display.setText("");
                                    }
                                }

                            }, 5000);

                        } catch (Exception e) {
                        }

                    }
                    if (receivedBitData.equals("B6")) {
                        try {
                            dialogsuccs.dismiss();
                            dialogsuccs6.dismiss();
                            dialogsuccs3.dismiss();
                            dialogsuccs33.dismiss();

                        } catch (Exception e) {
                            Log.d("error in B6",e.toString());
                        }

                        //showDialogProductYellow2();
                        cnt++;
                        if (cnt == 1) {
    //                        display.setText("");

                            try {
                                dialogsuccs.dismiss();
                                dialogsuccs6.dismiss();
                                dialogsuccs3.dismiss();
                                dialogsuccs33.dismiss();

                            } catch (Exception e) {
                                Log.d("error in B6",e.toString());
                            }

                        }
                    }


                    if (receivedBitData.equals("B3")) {

                        showDialogProductYellow6();
                        try {
    //                        dialogsuccs3.dismiss();
                            Log.d("in the try of B3"," I am in try of B3 line 243");
                        } catch (Exception e) {
                            Log.d("error in B3",e.toString());
                        }
                    }
                    if (receivedBitData.equals("B5")) {
                        try {
    //                        dialogsuccs1.dismiss();
                            Log.d("in the try of B3"," I am in try of B3 line 243");

                        } catch (Exception e) {
                            Log.d("error in B5",e.toString());

                        }
                        // Toast.makeText(mActivity.get(), "processing",Toast.LENGTH_LONG).show();
                        cnt1++;
                        if (cnt1 == 1) {

                            display.setText("");
                            try {
                                dialogsuccs6.dismiss();
                                dialogsuccs3.dismiss();
                                dialogsuccs33.dismiss();
                            } catch (Exception e) {
                                Log.d("error in B5",e.toString());

                            }
                        }
                    } else if (receivedBitData.equals("B4,B,B")) {

                        cnt++;
                        try {
                            if (cnt == 1) {
                                Insertbottle();
                            }
                        } catch (Exception e) {
                            Log.d("error in B4",e.toString());

                        }


                    } else if (receivedBitData.equals("1,1,1B4,B,B")) {

                        cnt++;
                        try {
                            if (cnt == 1) {
                                Insertbottle();
                            }
                        } catch (Exception e) {
                            Log.d("error in B kaypan",e.toString());

                        }

                    } else if (receivedBitData.equals("B11,B11,B1,1,1B4,B,B")) {
                        cnt++;
                        try {
                            if (cnt == 1) {
                                Insertbottle();
                            }
                        } catch (Exception e) {
                            Log.d("error in B kaypan",e.toString());

                        }
                    } else if (receivedBitData.equals("B10")) {

                        Log.d("in B10","we are in B10");
                        try {
                            cnt2++;
                            if (cnt2 == 1) {
                                try {
                                    thankyouaudio();
                                } catch (Exception e) {
                                    Log.d("error in B10",e.toString());
                                }
                            }

                        } catch (Exception e) {
                            Log.d("error in B10",e.toString());

                        }
                        return;
                    }
                    if (receivedBitData.equals("B12")) {
                        try {

                            //dialogsuccs33.dismiss();
                            firstordercomplted();
    //                        dialogsuccs1.dismiss();
    //                        dialogsuccs.dismiss();
                        } catch (Exception e) {
                            Log.d("error in B12",e.toString());

                        }
                    }


                    // Update temp after processing to track the last result received

                }
                handlerforcheck.postDelayed(() -> {
                    Log.d("Continuing", "Continuing to check data from server...");
                    checkReceivedData(); // Recursive call after the delay
                }, 1000);
                temp[0] = receivedBitData;
            }
        private void firstordercomplted() {
            Log.d("in somewhere", "I am in the firstordercompleted function");

            try {
                dishreadyaudio = new MediaPlayer();
                dishreadyaudio = MediaPlayer.create(getApplicationContext(), R.raw.dishisready);
                dishreadyaudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
                dishreadyaudio.setLooping(false);
                dishreadyaudio.start();
                // Pause the current thread for 4 seconds
                Thread.sleep(4000);

                dishreadyaudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (mp != null) {
                                mp.release();  // Release MediaPlayer when audio finishes
                            }
                        }
                    });

//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
                }catch (Exception e){
                    Log.d("error in ", "error in thank you audio");
                }
            }
            private void showDialogProductYellow6() {
                Log.d("dialog product yellow"," I am in showdialogproductyellow line 243");

            }
            private void thankyouaudio() {
                try {
                    // Check if another MediaPlayer instance is playing
                    if (thankyouaudio != null && thankyouaudio.isPlaying()) {
                        // Wait for the current audio to complete before playing the thankyouaudio
                        thankyouaudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                // Play the thankyou audio once the current audio completes
                                try {
                                    thankyouaudio = new MediaPlayer();
                                    thankyouaudio = MediaPlayer.create(getApplicationContext(), R.raw.thankyouvisitagain);
                                    thankyouaudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    thankyouaudio.setLooping(false);
                                    thankyouaudio.start();

                                    thankyouaudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            if (mp != null) {
                                                mp.release();  // Release MediaPlayer when audio finishes
                                            }
                                        }
                                    });
                                    // Pause the current thread for 2 seconds
                                    Thread.sleep(3000);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }catch (Exception e){
                                    Log.d("error in thank you ", e.toString());
                                }
                            }
                        });
                    } else {
                        // No audio is playing, so play thankyou audio immediately
                        try {
                            thankyouaudio = new MediaPlayer();
                            thankyouaudio = MediaPlayer.create(getApplicationContext(), R.raw.thankyouvisitagain);
                            thankyouaudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            thankyouaudio.setLooping(false);
                            thankyouaudio.start();

                            thankyouaudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    if (mp != null) {
                                        mp.release();  // Release MediaPlayer when audio finishes
                                    }
                                }
                            });
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        }catch (Exception e){
                            Log.d("error in thank you ", e.toString());
                        }

                    }

                } catch (Exception e) {
                    Log.d("Error in thank you", e.toString());
                }
            }
//            private void thankyouaudio() {
//                try {
//                    thankyouaudio = new MediaPlayer();
//                    thankyouaudio = MediaPlayer.create(getApplicationContext(), R.raw.thankyouvisitagain);
//                    thankyouaudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    thankyouaudio.setLooping(false);
//                    thankyouaudio.start();
//
//                    thankyouaudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            if (mp != null) {
//                                mp.release();  // Release MediaPlayer when audio finishes
//                            }
//                        }
//                    });
//                    // Pause the current thread for 2 seconds
//                    Thread.sleep(3000);
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
//                            finish();
//
//                }catch (Exception e){
//                    Log.d("error in thank you ", e.toString());
//                }
//
//            }
//

        });
    }

//    private void getCommandfromServer(CommandCallback commandCallback) {
//    }

    private void setFilters() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(EthernetService.ACTION_Ethernet_PERMISSION_GRANTED);
            filter.addAction(EthernetService.ACTION_NO_ETHERNET);
            filter.addAction(EthernetService.ACTION_ETHERNET_DISCONNECTED);
            filter.addAction(EthernetService.ACTION_ETHERNET_NOT_SUPPORTED);
            filter.addAction(EthernetService.ACTION_Ethernet_PERMISSION_GRANTED);

        } catch (Exception e) {
            Log.d("error in kuthetari",e.toString());

        }
        ;

    }

    //-----------------------------
    public class MyHandler extends Handler {
        private final WeakReference<TerminalActivity> mActivity;

        public MyHandler(TerminalActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            Log.d("msg", "hello how are you");

            switch (msg.what) {
                case EthernetService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    mActivity.get().display.append(data);
                    //   mActivity.get().display.setText(data);
                    String var = mActivity.get().display.getText().toString();
                    //  Toast.makeText(context, var,Toast.LENGTH_LONG).show();


                    if (var.contains("B11")) {
                        try {  // showDialogProductYellow();
                            new Handler().postDelayed(new Runnable() {
                                private EthernetService sendCommandToServer;

                                @Override
                                public void run() {
                                    handler.removeCallbacks(runnable);
                                    int cnt4 = 0;
                                    cnt4++;
                                    if (cnt4 == 1) {
                                        String bitdata = hasordecode.get("D");
                                        Log.e("Code", bitdata);
                                        // if UsbService was correctly binded, Send data
//                                        sendCommandToServer.write(bitdata.getBytes());
                                        display.setText("");
                                    }
                                }

                            }, 5000);

                        } catch (Exception e) {
                        }

                    }
                    if (var.contains("B6")) {

//                        try {
////
////                            dialogsuccs.dismiss();
////                            dialogsuccs6.dismiss();
////                            dialogsuccs3.dismiss();
////                            dialogsuccs33.dismiss();
//
//                        } catch (Exception e) {
//                        }

                        cnt++;
                        if (cnt == 1) {
                            display.setText("");

                            try {
                                dialogsuccs.dismiss();
                                dialogsuccs6.dismiss();
                                dialogsuccs3.dismiss();
                                dialogsuccs33.dismiss();

                            } catch (Exception e) {
                            }

                        }
                    }


                    if (var.contains("B3")) {
//                        showDialogProductYellow6();
                        try {
                            dialogsuccs3.dismiss();
                        } catch (Exception e) {
                        }

                    }
                    if (var.contains("B5")) {
                        try {
                            dialogsuccs1.dismiss();

                        } catch (Exception e) {
                        }

                        cnt1++;
                        if (cnt1 == 1) {

                            display.setText("");
                            try {
                                dialogsuccs6.dismiss();
                                dialogsuccs3.dismiss();
                                dialogsuccs33.dismiss();
                            } catch (Exception e) {
                            }
                        }
                    } else if (var.contains("B4,B,B")) {

                        cnt++;
                        try {
                            if (cnt == 1) {
                                Insertbottle();
                            }
                        } catch (Exception e) {
                        }


                    } else if (var.contains("1,1,1B4,B,B")) {

                        cnt++;
                        try {
                            if (cnt == 1) {
                                Insertbottle();
                            }
                        } catch (Exception e) {
                        }

                    } else if (var.contains("B11,B11,B1,1,1B4,B,B")) {

                        cnt++;
                        try {
                            if (cnt == 1) {
                                Insertbottle();
                            }
                        } catch (Exception e) {
                        }
                    } else if (var.contains("B10")) {
                        try {

                            cnt2++;
                            if (cnt2 == 1) {
                                try {
//                                    thankyouaudio();
                                } catch (Exception e) {
                                }

                            }

                        } catch (Exception e) {
                        }

                    }

                    if (var.contains("B12")) {
                        //  Toast.makeText(mActivity.get(), "Completed",Toast.LENGTH_LONG).show();
                        try {
//                            firstordercomplted();
                            //dialogsuccs33.dismiss();
                            dialogsuccs1.dismiss();
                            dialogsuccs.dismiss();
                        } catch (Exception e) {
                        }
                    }


                    break;

                case EthernetService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case EthernetService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }

//        private void thankyouaudio() {
//        }
//

    }

    //------------------------------
    @Override
    public void onPause() {
        super.onPause();
    }

    private void updatestock() {
        try {

            int total1 = 0, total2 = 0, total3 = 0, total4 = 0, total5 = 0, total6 = 0;
            SharedPreferences sharedPreferences = getSharedPreferences("jc", MODE_PRIVATE);

            int dishstock1 = Integer.parseInt(sharedPreferences.getString("stock1", ""));
            int dishstock2 = Integer.parseInt(sharedPreferences.getString("stock2", ""));
            int dishstock3 = Integer.parseInt(sharedPreferences.getString("stock3", ""));
            int dishstock4 = Integer.parseInt(sharedPreferences.getString("stock4", ""));
            int dishstock5 = Integer.parseInt(sharedPreferences.getString("stock5", ""));


            SharedPreferences.Editor editor = sharedPreferences.edit();

            Integer val1 = 0;
            Integer val2 = 0;
            Integer val3 = 0;
            Integer val4 = 0;
            Integer val5 = 0;

            val1 = hasordecodeliststock.get("1");
            val2 = hasordecodeliststock.get("2");
            val3 = hasordecodeliststock.get("3");
            val4 = hasordecodeliststock.get("4");
            val5 = hasordecodeliststock.get("5");

            if (val1 != null) {
                if (val1 > 0) {
                    total1 = dishstock1 - val1;
                    String st1 = String.valueOf(total1);
                    editor.putString("stock1", st1);
                }
            }
            if (val2 != null) {
                if (val2 > 0) {
                    total2 = dishstock2 - val2;
                    String st2 = String.valueOf(total2);
                    editor.putString("stock2", st2);
                }
            }
            if (val3 != null) {
                if (val3 > 0) {
                    total3 = dishstock3 - val3;
                    String st3 = String.valueOf(total3);
                    editor.putString("stock3", st3);
                }
            }
            if (val4 != null) {
                if (val4 > 0) {
                    total4 = dishstock4 - val4;
                    String st4 = String.valueOf(total4);
                    editor.putString("stock4", st4);
                }
            }
            if (val5 != null) {
                if (val5 > 0) {
                    total5 = dishstock5 - val5;
                    String st5 = String.valueOf(total5);
                    editor.putString("stock5", st5);
                }
            }

            editor.apply();

        } catch (Exception e) {

        }
    }

    private void Errorfound() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.errorfound);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button button = dialog.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialog.dismiss();

                // bluetoothSerial.write("(0,0,0,A,0,0)", crlf);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        dialog.show();
    }

    private void stockrefiled() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.stockrefailed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button button = dialog.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void Pleasewait() {
        dialogpleasewait = new Dialog(this);
        dialogpleasewait.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogpleasewait.setContentView(R.layout.pleasewait);
        dialogpleasewait.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogpleasewait.setCancelable(true);
        dialogpleasewait.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogpleasewait.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialogpleasewait.show();
    }

    private void orderlist() {
        orderlist = new Dialog(this);
        orderlist.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        orderlist.setContentView(R.layout.ordernamelist);
        orderlist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        orderlist.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(orderlist.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView textView = orderlist.findViewById(R.id.orderlist);
        textView.setText("Dish " + cnt + " Completed ");

        orderlist.show();
    }

    private void stockover() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.stockover);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button button = dialog.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void ordercompleted() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.ordercompleted);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        handlerordercompleted.postDelayed(runnableordercompleted = new Runnable() {
            public void run() {
                handlerordercompleted.postDelayed(runnableordercompleted, delayordercompleted);
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                handlerordercompleted.removeCallbacks(runnableordercompleted);

            }
        }, delayordercompleted);
        super.onResume();

        Button button = dialog.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void pickdish() {
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog1.setContentView(R.layout.pickdish);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        handlerordercompleted.postDelayed(runnableordercompleted = new Runnable() {
            public void run() {
                handlerordercompleted.postDelayed(runnableordercompleted, delayordercompleted);
                // dialog1.dismiss();

                handlerordercompleted.removeCallbacks(runnableordercompleted);

            }
        }, delayordercompleted);
        super.onResume();

        Button button = dialog1.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    public void audio() {
        thankyouaudio = new MediaPlayer();
        thankyouaudio = MediaPlayer.create(this, R.raw.thankyouvisitagain);
        thankyouaudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
        thankyouaudio.setLooping(false);
        thankyouaudio.start();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    private void Insertbottle() {
        dialogbottle = new Dialog(this);
        dialogbottle.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogbottle.setContentView(R.layout.insertbottle);
        dialogbottle.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbottle.setCancelable(true);
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Function to send the command to the Raspberry Pi server via HTTP GET request
    private void sendCommandToServer(final byte[] command) {
        executorService.execute(() -> {
            String result;
            try {
                // Initialize HandleServer with context
                handleServer = new HandleServer(this);
                String serverip = handleServer.getmyip();
                Log.d("bits",command.toString());
                Log.d("ipseverlsdj","this is server ip:"+serverip);
                // Construct the URL for sending the command
                String urlString = "http://" + "192.168.0.100" + ":" + 5000 + "/sendbits?bits=" + new String(command);
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

                // Assign the response to result
                result = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error sending command";
            }

            // Handle server response on the UI thread
            final String finalResult = result;
            runOnUiThread(() -> {
                // Update UI or display a toast here
                Log.d("Server response: ", finalResult);
            });
        });
    }
//    -------------------

    public interface CommandCallback {
        void onResult(String result);
    }

    private void getCommandfromServer(CommandCallback callback) {
        try {
            executorService.execute(() -> {
                String result;
                try {
                    // Initialize HandleServer with context
                    // Pass context to HandleServer
                    handleServer = new HandleServer(this);
                    String serverip = handleServer.getmyip();
                    Log.d("Server IP", "Server IP is: " + serverip);
                    // Construct the URL for sending the command
                    String urlString = "http://" + "192.168.0.100" + ":" + 5000 + "/getbits";
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

                    // Assign the response to result
                    result = response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "Error sending command";
                }

                // Handle serv er response on the UI thread
                final String finalResult = result;
                runOnUiThread(() -> {
                    // Update UI or display a toast here
                    Log.d("Server response: ", finalResult);
                });

                // Call the callback with the result
                if (callback != null) {
                    callback.onResult(finalResult);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error in getcommand",e.toString());
            String result = "Error sending command";
        }
    }
}


