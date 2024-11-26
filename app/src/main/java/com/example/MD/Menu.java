package com.example.MD;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//import static com.example.MD.MainActivity.bluetoothSerialmain;
//import static com.example.MD.MainActivity.i;


public class Menu extends AppCompatActivity {

    TextView menu_dish1, menu_dish2, menu_dish3, menu_dish4, menu_dish5, menu_dish1_rate, menu_dish2_rate, menu_dish3_rate, menu_dish4_rate, menu_dish5_rate, dish1_quant, dish2_quant, dish3_quant, dish4_quant, dish5_quant, menu_total;
    Button  dish1_addbtn, dish2_addbtn, dish3_addbtn,dish4_addbtn, dish5_addbtn, dish1_miunsbtn, dish2_miunsbtn, dish3_miunsbtn, dish4_miunsbtn, dish5_miunsbtn;
    public static ArrayList<Orderitem> arrayList=new ArrayList();
    TextView home,save;
    Animation blink;
    TextView cow250,cow500,cow1l,buff250,buff500,buff1l,dish3quantity,dish4quantity,dish5quantity,dish3stock,dish4stock,dish5stock;
    ImageView img1,img2,img3,img4,img5;
    String dish1stts,dish2stts,dish3stts,dish4stts,dish5stts;
    LinearLayout menu_linear1, menu_linear2, menu_linear3, menu_linear4, menu_linear5,linear1,linear2,linear3,linear4,linear5;
    int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0;
    double cart;
    double dish1_total, dish2_total, dish3_total, dish4_total, dish5_total;
    String dish1rate,dish2rate,dish3rate,dish4rate,dish5rate,quant3,quant4,quant5;
    String dish_1,dish_2,dish_3,dish_4,dish_5;
    MediaPlayer mMediaPlayer;
    Context context;
    String dishstock1,dishstock2,dishstock3,dishstock4,dishstock5;
    int dishstock1new,dishstock2new,dishstock3new,dishstock4new,dishstock5new;
    String Modelno;
    SharedPreferences sharedPreferencesnew;
    public static HashMap<String, String> hasordecode = new HashMap<>();
    public static HashMap<String, Integer> hasordecodelist = new HashMap<>();
    public static HashMap<String, Integer> hasordecodeliststock = new HashMap<>();
    TextView no,snf,density,protein,fat,quality;
    String mailid, password;
    IdleCountDownTimer idleCountDownTimer;
    int i=0;

//     // Method to manage network connections
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void manageNetworkConnections() {
//        Log.d("hello","I am in wifi generatioin");
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//
//        // Bind to Ethernet if available
//        NetworkRequest ethernetRequest = new NetworkRequest.Builder()
//                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
//                .build();
//
//        connectivityManager.requestNetwork(ethernetRequest, new ConnectivityManager.NetworkCallback() {
//            @Override
//            public void onAvailable(Network network) {
//                Log.d("Network", "Ethernet is available");
//                connectivityManager.
//                        bindProcessToNetwork(network); // Bind Ethernet
//            }
//        });
//        // Bind to Wi-Fi if available
//        NetworkRequest wifiRequest = new NetworkRequest.Builder()
//                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//                .build();
//
//        connectivityManager.requestNetwork(wifiRequest, new ConnectivityManager.NetworkCallback() {
//            @Override
//            public void onAvailable(Network network) {
//                Log.d("Network", "Wi-Fi is available");
//                connectivityManager.bindProcessToNetwork(network); // Bind Wi-Fi
//            }
//        });
//    }
//    -------------------------------------------------------------------
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
private void manageNetworkConnections(Boolean isEthernet) {
    Log.d("Network", "inside the Managing network connections");
    // Get the connectivity manager
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    if (isEthernet){
        // Check if Ethernet is available
        NetworkRequest ethernetRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .build();

        connectivityManager.requestNetwork(ethernetRequest, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Log.d("Network", "Ethernet is available");

                // Bind Ethernet network only if it's not bound already
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Network activeNetwork = connectivityManager.getBoundNetworkForProcess();
                    if (activeNetwork != null) {
                        Log.d("Network", "Releasing currently bound network before binding Ethernet");
                        connectivityManager.bindProcessToNetwork(null); // Unbind the current network
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.bindProcessToNetwork(network); // Bind Ethernet
                    Log.d("network Log:"," Ethernet Succesfully Bind");
                }
                Log.d("Network", "Bound to Ethernet");
            }

            @Override
            public void onUnavailable() {
                Log.d("Network", "Ethernet is unavailable");
            }
        });

    }
    else {
        // Check if Wi-Fi is available
        NetworkRequest wifiRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();

        connectivityManager.requestNetwork(wifiRequest, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Log.d("Network", "Wi-Fi is available");

                // Check if Ethernet is bound and unbind it if necessary
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Network activeNetwork = connectivityManager.getBoundNetworkForProcess();
                    if (activeNetwork != null) {
                        Log.d("Network", "Releasing currently bound network before binding Wi-Fi");
                        connectivityManager.bindProcessToNetwork(null); // Unbind the current network
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.bindProcessToNetwork(network); // Bind Wi-Fi
                    Log.d("network Log:","wifi Succesfully Bind");
                }
                Log.d("Network", "Bound to Wi-Fi");
            }

            @Override
            public void onUnavailable() {
                Log.d("Network", "Wi-Fi is unavailable");
            }
        });

    }
}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
        idleCountDownTimer = new IdleCountDownTimer(30000, 30000);
        idleCountDownTimer.start();
        context=this;
        init();


        hasordecode.clear();
        hasordecodelist.clear();
        hasordecodeliststock.clear();
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_blink);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer = MediaPlayer.create(this, R.raw.selectmenu);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.start();
        }catch (Exception e){}


/*        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog();


            }
        });*/


        sharedPreferencesnew = getSharedPreferences("jc", MODE_PRIVATE);
        //owner info
        try {
            dishstock1 = sharedPreferencesnew.getString("stock1","");

        }catch (Exception e){

        }

        try {
            dishstock2 = sharedPreferencesnew.getString("stock2","");

        }catch (Exception e){

        }
        try {
            dishstock3 = sharedPreferencesnew.getString("stock3","");
            dish3stock.setText("Stock Servings" + ": "  + dishstock3);
        }catch (Exception e){


        }
        try {
            dishstock4 = sharedPreferencesnew.getString("stock4","");
            dish4stock.setText("Stock Qty" + ": "  + dishstock4);
        }catch (Exception e){}

        try {
            dishstock5 = sharedPreferencesnew.getString("stock5","");
            dish5stock.setText("Stock Qty" + ": "  + dishstock5);
        }catch (Exception e){}



        try {
            dishstock1new= Integer.parseInt(dishstock1);
            dishstock2new= Integer.parseInt(dishstock2);
            dishstock3new= Integer.parseInt(dishstock3);
            dishstock4new= Integer.parseInt(dishstock4);
            dishstock5new= Integer.parseInt(dishstock5);
        }catch (Exception e){}





        if (dishstock1new <= 2){
            img1.setImageResource(R.drawable.outofstock);
            img1.setEnabled(false);
            img1.clearAnimation();
            cow250.setVisibility(View.INVISIBLE);
            cow500.setVisibility(View.INVISIBLE);
            cow1l.setVisibility(View.INVISIBLE);
        }else if (dishstock1new >= 3){
            img1.setImageResource(R.drawable.cow);
            img1.setEnabled(true);
            cow250.setVisibility(View.VISIBLE);
            cow500.setVisibility(View.VISIBLE);
            cow1l.setVisibility(View.VISIBLE);
        }

        if (dishstock2new <= 2){
            //openWhatsApp();
            img2.setImageResource(R.drawable.outofstock);
            img2.setEnabled(false);
            img2.clearAnimation();
            buff250.setVisibility(View.INVISIBLE);
            buff500.setVisibility(View.INVISIBLE);
            buff1l.setVisibility(View.INVISIBLE);
        }else if (dishstock2new >= 3){
            img2.setImageResource(R.drawable.buffalo);
            img2.setEnabled(true);
            buff250.setVisibility(View.VISIBLE);
            buff500.setVisibility(View.VISIBLE);
            buff1l.setVisibility(View.VISIBLE);
        }

        if (dishstock3new <= 2){
            img3.setImageResource(R.drawable.outofstock);
            img3.setEnabled(false);
            img3.clearAnimation();
            linear3.setVisibility(View.INVISIBLE);
        }else if (dishstock3new >= 3){
            img3.setImageResource(R.drawable.buttermilk);
            img3.setEnabled(true);
            linear3.setVisibility(View.VISIBLE);
        }

        if (dishstock4new <= 2){
            img4.setImageResource(R.drawable.outofstock);
            img4.setEnabled(false);
            img4.clearAnimation();
            linear4.setVisibility(View.INVISIBLE);
        }else if (dishstock4new >= 3){
            img4.setImageResource(R.drawable.curd);
            img4.setEnabled(true);
            linear4.setVisibility(View.VISIBLE);
        }


        if (dishstock5new <= 2){
            img5.setImageResource(R.drawable.outofstock);
            img5.setEnabled(false);
            img5.clearAnimation();
            linear5.setVisibility(View.INVISIBLE);
        }else if (dishstock5new >= 3){
            img5.setImageResource(R.drawable.paneer);
            img5.setEnabled(true);
            linear5.setVisibility(View.VISIBLE);
        }


        SharedPreferences sharedPreferences = getSharedPreferences("p006", MODE_PRIVATE);
        mailid = sharedPreferences.getString("email", "");
        password = sharedPreferences.getString("password", "");

        //dish rate
        String cow1rate = sharedPreferences.getString("cow1rate","");
        cow250.setText("250ml Rs " + cow1rate+".00");
        String cow2rate = sharedPreferences.getString("cow2rate", "");
        cow500.setText("500ml Rs " + cow2rate+".00");
        String cow3rate = sharedPreferences.getString("cow3rate", "");
        cow1l.setText("1litre Rs " + cow3rate+".00");
        String buff1rate = sharedPreferences.getString("buff1rate","");
        buff250.setText("250ml Rs " + buff1rate+".00");
        String buff2rate = sharedPreferences.getString("buff2rate", "");
        buff500.setText("500ml Rs " + buff2rate+".00");
        String buff3rate = sharedPreferences.getString("buff3rate", "");
        buff1l.setText("1litre Rs " + buff3rate+".00");
        dish3rate = sharedPreferences.getString("dish3rate", "");
        menu_dish3_rate.setText("Rs " + dish3rate+".00");
        dish4rate = sharedPreferences.getString("dish4rate", "");
        menu_dish4_rate.setText("Rs " + dish4rate+".00");
        dish5rate = sharedPreferences.getString("dish5rate", "");
        menu_dish5_rate.setText("Rs " + dish5rate+".00");

        //Dish Name
        dish_1 = sharedPreferences.getString("dish1", "");
        menu_dish1.setText(dish_1);
        dish_2 = sharedPreferences.getString("dish2", "");
        menu_dish2.setText(dish_2);
        dish_3 = sharedPreferences.getString("dish3", "");
        menu_dish3.setText(dish_3);
        dish_4 = sharedPreferences.getString("dish4", "");
        menu_dish4.setText(dish_4);
        dish_5 = sharedPreferences.getString("dish5", "");
        menu_dish5.setText(dish_5);
        quant3 = sharedPreferences.getString("dish3quant", "");
        dish3quantity.setText(quant3 + "ml");
        quant4 = sharedPreferences.getString("dish4quant", "");
        dish4quantity.setText(quant4 + "g");
        quant5 = sharedPreferences.getString("dish5quant", "");
        dish5quantity.setText(quant5 + "g");

        //dish status
        dish1stts = sharedPreferences.getString("dishstatus1", "");
        dish2stts = sharedPreferences.getString("dishstatus2", "");
        dish3stts = sharedPreferences.getString("dishstatus3", "");
        dish4stts = sharedPreferences.getString("dishstatus4", "");
        dish5stts = sharedPreferences.getString("dishstatus5", "");


        // GetPrice();
        if (dish1stts.contains("disable")) {
            menu_linear1.setVisibility(View.GONE);
        }
        if (dish2stts.contains("disable")) {
            menu_linear2.setVisibility(View.GONE);
        }
        if (dish3stts.contains("disable")) {
            menu_linear3.setVisibility(View.GONE);
        }
        if (dish4stts.contains("disable")) {
            menu_linear4.setVisibility(View.GONE);
        }
        if (dish5stts.contains("disable")) {
            menu_linear5.setVisibility(View.GONE);
        }



       /* dish1_quant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (Double.parseDouble(dish1_quant.getText().toString()) == 0)
                    {
                        img1.clearAnimation();
                    }

                    dish1_total = Double.parseDouble(dish1_quant.getText().toString()) * Double.parseDouble(dish1rate);
                    cart = Double.parseDouble(dish1_quant.getText().toString()) + Double.parseDouble(dish2_quant.getText().toString()) + Double.parseDouble(dish3_quant.getText().toString()) + Double.parseDouble(dish4_quant.getText().toString()) + Double.parseDouble(dish5_quant.getText().toString());
                    if (cart == 5) {
                        Disable();

                    } else {
                        Enable();

                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Please You have to add product Name and Price in admin screen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                double total1 = dish1_total + dish2_total + dish3_total + dish4_total + dish5_total;
                menu_total.setText(Double.toString(total1));


            }
        });
        dish2_quant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (Double.parseDouble(dish2_quant.getText().toString()) == 0)
                    {
                        img2.clearAnimation();
                    }
                    dish2_total = Double.parseDouble(dish2_quant.getText().toString()) * Double.parseDouble(dish2rate);
                    cart = Double.parseDouble(dish1_quant.getText().toString()) + Double.parseDouble(dish2_quant.getText().toString()) + Double.parseDouble(dish3_quant.getText().toString()) + Double.parseDouble(dish4_quant.getText().toString()) + Double.parseDouble(dish5_quant.getText().toString());
                    if (cart == 5) {
                        Disable();

                    } else {
                        Enable();

                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Please You have to add product Name and Price in admin screen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                double total1 = dish1_total + dish2_total + dish3_total + dish4_total + dish5_total;
                menu_total.setText(Double.toString(total1));


            }
        });*/
        dish3_quant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    if (Double.parseDouble(dish3_quant.getText().toString()) == 0)
                    {
                        img3.clearAnimation();
                    }
                    dish3_total = Double.parseDouble(dish3_quant.getText().toString()) * Double.parseDouble(dish3rate);
                    cart = Double.parseDouble(dish3_quant.getText().toString()) + Double.parseDouble(dish4_quant.getText().toString()) + Double.parseDouble(dish5_quant.getText().toString());
                    if (cart == 10) {
                        Disable();

                    } else {
                        Enable();

                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Please You have to add product Name and Price in admin screen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                double total1 = dish3_total + dish4_total + dish5_total;
                menu_total.setText(Double.toString(total1)+"0");


            }
        });
        dish4_quant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    if (Double.parseDouble(dish4_quant.getText().toString()) == 0)
                    {
                        img4.clearAnimation();
                    }
                    dish4_total = Double.parseDouble(dish4_quant.getText().toString()) * Double.parseDouble(dish4rate);
                    cart = Double.parseDouble(dish3_quant.getText().toString()) + Double.parseDouble(dish4_quant.getText().toString()) + Double.parseDouble(dish5_quant.getText().toString());
                    if (cart == 10) {
                        Disable();

                    } else {
                        Enable();

                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Please You have to add product Name and Price in admin screen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                double total1 = dish3_total + dish4_total + dish5_total;
                menu_total.setText(Double.toString(total1)+"0");


            }
        });


        dish5_quant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (Double.parseDouble(dish5_quant.getText().toString()) == 0)
                    {
                        img5.clearAnimation();
                    }

                    dish5_total = Double.parseDouble(dish5_quant.getText().toString()) * Double.parseDouble(dish5rate);
                    cart = Double.parseDouble(dish3_quant.getText().toString()) + Double.parseDouble(dish4_quant.getText().toString()) + Double.parseDouble(dish5_quant.getText().toString());
                    if (cart == 10) {
                        Disable();

                    } else {
                        Enable();

                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Please You have to add product Name and Price in admin screen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                double total1 = dish3_total + dish4_total + dish5_total;
                menu_total.setText(Double.toString(total1) +"0");


            }
        });


      /*  img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.startAnimation(blink);
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                mMediaPlayer.start();
                img2.clearAnimation();
                img3.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                i++;
                if (i == 5) {
                    i = 0;


                } else if (count1 == 5) {
                    dish1_addbtn.setClickable(false);
                    dish1_addbtn.setClickable(true);

                } else {
                    dish1_addbtn.setClickable(true);
                    count1 = count1 + 1;
                    dish1display(count1);
                    hasordecodelist.put("1",count1);
                    hasordecodeliststock.put("1", count1);
                }
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img2.startAnimation(blink);
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                mMediaPlayer.start();
                img1.clearAnimation();
                img3.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                i++;
                if (i == 5) {
                    i = 0;
                } else if (count2 == 5) {
                    dish2_addbtn.setClickable(false);
                    dish2_addbtn.setClickable(true);

                } else {
                    dish2_addbtn.setClickable(true);
                    count2 = count2 + 1;
                    dish2display(count2);
                    hasordecodelist.put("2",count2);
                    hasordecodeliststock.put("2", count2);

                }

            }
        });*/


        cow250.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cow250.setBackgroundResource(R.drawable.edit_text);
                cow500.setBackgroundResource(R.drawable.yellow);
                cow1l.setBackgroundResource(R.drawable.yellow);
                buffalo();
                rate();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish1.getText().toString() + " " + "Rs:" + "  " + cow250.getText().toString() , Toast.LENGTH_LONG).show();
                img1.startAnimation(blink);
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img3.clearAnimation();
                img2.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                menu_total.setText(cow1rate +".00");
                count1 = count1 + 1;
                hasordecodelist.put("1",1);
                hasordecodeliststock.put("1",1);
            }
        });

        cow500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cow250.setBackgroundResource(R.drawable.yellow);
                cow500.setBackgroundResource(R.drawable.edit_text);
                cow1l.setBackgroundResource(R.drawable.yellow);
                buffalo();
                rate();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish1.getText().toString() + " " + "Rs:" + "  " + cow500.getText().toString() , Toast.LENGTH_LONG).show();

                img1.startAnimation(blink);
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img3.clearAnimation();
                img2.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                menu_total.setText(cow2rate +".00");
                count1 = count1 + 2;
                hasordecodelist.put("1",2);
                hasordecodeliststock.put("1",2);
            }
        });

        cow1l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cow250.setBackgroundResource(R.drawable.yellow);
                cow500.setBackgroundResource(R.drawable.yellow);
                cow1l.setBackgroundResource(R.drawable.edit_text);
                buffalo();
                rate();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish1.getText().toString() + " " + "Rs:" + "  " + cow1l.getText().toString() , Toast.LENGTH_LONG).show();
                img1.startAnimation(blink);
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img3.clearAnimation();
                img2.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                menu_total.setText(cow3rate +".00");
                count1 = count1 + 4;
                hasordecodelist.put("1",4);
                hasordecodeliststock.put("1",4);
            }
        });


        buff250.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buff250.setBackgroundResource(R.drawable.edit_text);
                buff500.setBackgroundResource(R.drawable.yellow);
                buff1l.setBackgroundResource(R.drawable.yellow);
                cow();
                rate();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish2.getText().toString() + " " + "Rs:" + "  " + buff250.getText().toString() , Toast.LENGTH_LONG).show();
                img2.startAnimation(blink);
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img3.clearAnimation();
                img1.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                menu_total.setText(buff1rate +".00");
                count2 = count2 + 1;
                hasordecodelist.put("2",1);
                hasordecodeliststock.put("2",1);
            }
        });

        buff500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buff250.setBackgroundResource(R.drawable.yellow);
                buff500.setBackgroundResource(R.drawable.edit_text);
                buff1l.setBackgroundResource(R.drawable.yellow);
                cow();
                rate();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish2.getText().toString() + " " + "Rs:" + "  " + buff500.getText().toString() , Toast.LENGTH_LONG).show();
                img2.startAnimation(blink);
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img3.clearAnimation();
                img1.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                menu_total.setText(buff2rate +".00");
                count2 = count2 + 2;
                hasordecodelist.put("2",2);
                hasordecodeliststock.put("2",2);
            }
        });

        buff1l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buff250.setBackgroundResource(R.drawable.yellow);
                buff500.setBackgroundResource(R.drawable.yellow);
                buff1l.setBackgroundResource(R.drawable.edit_text);
                cow();
                rate();
                img2.startAnimation(blink);
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish2.getText().toString() + " " + "Rs:" + "  " + buff1l.getText().toString() , Toast.LENGTH_LONG).show();
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img3.clearAnimation();
                img1.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                menu_total.setText(buff3rate +".00");
                count2 = count2 + 4;
                hasordecodelist.put("2",4);
                hasordecodeliststock.put("2",4);
            }
        });


        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cow();
                buffalo();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish3.getText().toString() + " " + "Rs:" + "  " + menu_dish3_rate.getText().toString() , Toast.LENGTH_LONG).show();

                img3.startAnimation(blink);
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img1.clearAnimation();
                img2.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                i++;
                if (i == 10) {
                    i = 0;
                } else if (count3 == 10) {
                    dish3_addbtn.setClickable(false);
                    dish3_addbtn.setClickable(true);

                } else {
                    dish3_addbtn.setClickable(true);
                    count3 = count3 + 1;
                    dish3display(count3);
                    hasordecodelist.put("3",count3);
                    hasordecodeliststock.put("3", count3);



                }
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cow();
                buffalo();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish4.getText().toString() + " " + "Rs:" + "  " + menu_dish4_rate.getText().toString() , Toast.LENGTH_LONG).show();
                img4.startAnimation(blink);
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img2.clearAnimation();
                img3.clearAnimation();
                img1.clearAnimation();
                img5.clearAnimation();
                i++;
                if (i == 10) {
                    i = 0;


                } else if (count4 == 10) {
                    dish4_addbtn.setClickable(false);
                    dish4_addbtn.setClickable(true);

                } else {
                    dish4_addbtn.setClickable(true);
                    count4 = count4 + 1;
                    dish4display(count4);
                    hasordecodelist.put("4",count4);
                    hasordecodeliststock.put("4", count4);



                }
            }
        });

        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cow();
                buffalo();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish5.getText().toString() + " " + "Rs:" + "  " + menu_dish5_rate.getText().toString() , Toast.LENGTH_LONG).show();
                img5.startAnimation(blink);
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img2.clearAnimation();
                img3.clearAnimation();
                img1.clearAnimation();
                img4.clearAnimation();
                i++;
                if (i == 10) {
                    i = 0;

                } else if (count5 == 10) {
                    dish5_addbtn.setClickable(false);
                    dish5_addbtn.setClickable(true);

                } else {
                    dish5_addbtn.setClickable(true);
                    count5 = count5 + 1;
                    dish5display(count5);
                    hasordecodelist.put("5",count5);
                    hasordecodeliststock.put("5", count5);



                }
            }
        });


       /* dish1_addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                mMediaPlayer.start();
                img1.startAnimation(blink);
                img2.clearAnimation();
                img3.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                i++;
                if (i == 5) {
                    i = 0;


                } else if (count1 == 5) {
                    dish1_addbtn.setClickable(false);
                    dish1_addbtn.setClickable(true);

                } else {
                    dish1_addbtn.setClickable(true);
                    count1 = count1 + 1;
                    dish1display(count1);
                    hasordecodelist.put("1",count1);
                    hasordecodeliststock.put("1", count1);
                }
            }
        });
        dish1_miunsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                mMediaPlayer.start();
                if (count1 < 0) {
                    count1 = 0;
                    dish1display(count1);
                }
                if (count1 > 0) {
                    count1 = count1 - 1;
                    dish1display(count1);
                    hasordecodelist.put("1",count1);


                }
            }
        });


        dish2_addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                mMediaPlayer.start();
                img2.startAnimation(blink);
                img1.clearAnimation();
                img3.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                i++;
                if (i == 5) {
                    i = 0;
                } else if (count2 == 5) {
                    dish2_addbtn.setClickable(false);
                    dish2_addbtn.setClickable(true);

                } else {
                    dish2_addbtn.setClickable(true);
                    count2 = count2 + 1;
                    dish2display(count2);
                    hasordecodelist.put("2",count2);
                    hasordecodeliststock.put("2", count2);

                }
            }
        });




        dish2_miunsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                mMediaPlayer.start();
                if (count2 < 0) {
                    count2 = 0;
                    dish2display(count2);
                }
                if (count2 > 0) {
                    count2 = count2 - 1;
                    dish2display(count2);
                    hasordecodelist.put("2",count2);


                }
            }
        });*/

        dish3_addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cow();
                buffalo();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish3.getText().toString() + " " + "Rs:" + "  " + menu_dish3_rate.getText().toString() , Toast.LENGTH_LONG).show();
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img3.startAnimation(blink);
                img1.clearAnimation();
                img2.clearAnimation();
                img4.clearAnimation();
                img5.clearAnimation();
                i++;
                if (i == 10) {
                    i = 0;


                } else if (count3 == 10) {
                    dish3_addbtn.setClickable(false);
                    dish3_addbtn.setClickable(true);

                } else {
                    dish3_addbtn.setClickable(true);
                    count3 = count3 + 1;
                    dish3display(count3);
                    hasordecodelist.put("3",count3);
                    hasordecodeliststock.put("3", count3);



                }
            }
        });
        dish3_miunsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Removed" + "  " + menu_dish3.getText().toString() + " " + "From Cart", Toast.LENGTH_LONG).show();
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                if (count3 < 0) {
                    count3 = 0;
                    dish3display(count3);
                }
                if (count3 > 0) {
                    count3 = count3 - 1;
                    dish3display(count3);
                    hasordecodelist.put("3",count3);


                }
            }
        });

        dish4_addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cow();
                buffalo();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish4.getText().toString() + " " + "Rs:" + "  " + menu_dish4_rate.getText().toString() , Toast.LENGTH_LONG).show();
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img4.startAnimation(blink);
                img2.clearAnimation();
                img3.clearAnimation();
                img1.clearAnimation();
                img5.clearAnimation();
                i++;
                if (i == 10) {
                    i = 0;


                } else if (count4 == 10) {
                    dish4_addbtn.setClickable(false);
                    dish4_addbtn.setClickable(true);

                } else {
                    dish4_addbtn.setClickable(true);
                    count4 = count4 + 1;
                    dish4display(count4);
                    hasordecodelist.put("4",count4);
                    hasordecodeliststock.put("4", count4);



                }
            }
        });
        dish4_miunsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Removed" + "  " + menu_dish4.getText().toString() + " " + "From Cart", Toast.LENGTH_LONG).show();
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                if (count4 < 0) {
                    count4 = 0;
                    dish4display(count4);
                }
                if (count4 > 0) {
                    count4 = count4 - 1;
                    dish4display(count4);
                    hasordecodelist.put("4",count4);


                }
            }
        });

        dish5_addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cow();
                buffalo();
                Toast.makeText(getApplicationContext(), "Selected" + "  " + menu_dish5.getText().toString() + " " + "Rs:" + "  " + menu_dish5_rate.getText().toString() , Toast.LENGTH_LONG).show();
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }
                img5.startAnimation(blink);
                img2.clearAnimation();
                img3.clearAnimation();
                img1.clearAnimation();
                img4.clearAnimation();
                i++;
                if (i == 10) {
                    i = 0;


                } else if (count5 == 10) {
                    dish5_addbtn.setClickable(false);
                    dish5_addbtn.setClickable(true);

                } else {
                    dish5_addbtn.setClickable(true);
                    count5 = count5 + 1;
                    dish5display(count5);
                    hasordecodelist.put("5",count5);
                    hasordecodeliststock.put("5", count5);



                }
            }
        });
        dish5_miunsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Removed" + "  " + menu_dish5.getText().toString() + " " + "From Cart", Toast.LENGTH_LONG).show();
                try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    mMediaPlayer.start();
                }catch (Exception e){

                }

                if (count5 < 0) {
                    count5 = 0;
                    dish5display(count5);
                }
                if (count5 > 0) {
                    count5 = count5 - 1;
                    dish5display(count5);
                    hasordecodelist.put("5",count5);

                }
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

        SharedPreferences sharedPreferencess = getSharedPreferences("p006", MODE_PRIVATE);
        String val = sharedPreferencess.getString("valuee","");
//---------------------------------------------------------------------------


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (val.equals("free"))
                {
                    if (menu_total.getText().equals("00")) {
                        showCustomDialog();
                    } else {
                        Intent i  = new Intent(getApplicationContext(),TerminalActivity.class);
                        hasordecode.size();
                        hasordecode.put("D","("+count1+","+count2+","+count3+","+count4+","+count5+",0)");
                        startActivity(i);
                        finish();
                    }
                }else if (val.equals("paid")){

                    if (menu_total.getText().equals("00")) {
                        showCustomDialog();
                    } else {
                        HandleServer handleServer= new HandleServer(Menu.this);
//                        handleServer.turnoffethernet();
                        // Pause the current thread for 4 seconds
//                        try {
//                            Thread.sleep(5000);
//                            Log.d("thread","Thread stoped for 5 second");
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
                        Intent intent = new Intent(getApplicationContext(), Payment.class);
                        String t1 = menu_total.getText().toString().trim();
                        intent.putExtra("total" , t1);
                        hasordecode.size();
                        hasordecode.put("D","("+count1+","+count2+","+count3+","+count4+","+count5+",0)");
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
//       super.onBackPressed();
        try {
            Log.d("messgae", "we stopped");
        }catch (Exception e){}

//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
//
//        finish();
}


    private void dish1display(int number) {
        dish1_quant.setText("" + number);

    }

    private void dish2display(int number) {
        dish2_quant.setText("" + number);
    }

    private void dish3display(int number) {
        dish3_quant.setText("" + number);
    }

    private void dish4display(int number) {
        dish4_quant.setText("" + number);
    }

    private void dish5display(int number) {
        dish5_quant.setText("" + number);
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void Enable() {


        dish3_addbtn.setEnabled(true);
        dish4_addbtn.setEnabled(true);
        dish5_addbtn.setEnabled(true);
        img1.setEnabled(true);
        img2.setEnabled(true);
        img3.setEnabled(true);
        img4.setEnabled(true);
        img5.setEnabled(true);


    }
    public void Disable() {


        dish3_addbtn.setEnabled(false);
        dish4_addbtn.setEnabled(false);
        dish5_addbtn.setEnabled(false);
        img1.setEnabled(false);
        img2.setEnabled(false);
        img3.setEnabled(false);
        img4.setEnabled(false);
        img5.setEnabled(false);

    }

   /* private void dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.snf);
        snf = dialog.findViewById(R.id.snf);
        density = dialog.findViewById(R.id.density);
        protein = dialog.findViewById(R.id.protein);
        fat = dialog.findViewById(R.id.fat);

        SharedPreferences sharedPreferences = getSharedPreferences("p006", MODE_PRIVATE);
        String snf1 = sharedPreferences.getString("SNF", "");
        snf.setText("SNF : " + snf1);;
        String density1 = sharedPreferences.getString("Density", "");
        density.setText("DENSITY : " + density1);
        String protein1 = sharedPreferences.getString("Protein", "");
        protein.setText( "PROTEIN : " + protein1);
        String fat1 = sharedPreferences.getString("Fat", "");
        fat.setText("FAT : " + fat1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.show();
    }*/




    private void cow(){
        cow250.setBackgroundResource(R.drawable.yellow);
        cow500.setBackgroundResource(R.drawable.yellow);
        cow1l.setBackgroundResource(R.drawable.yellow);
    }


    private void buffalo(){
        buff250.setBackgroundResource(R.drawable.yellow);
        buff500.setBackgroundResource(R.drawable.yellow);
        buff1l.setBackgroundResource(R.drawable.yellow);
    }

    private void rate(){
        dish3_quant.setText("0");
        dish4_quant.setText("0");
        dish5_quant.setText("0");
        count3 = 0;
        count4 = 0;
        count5 = 0;
    }

    private void init(){
        menu_dish1 = findViewById(R.id.menu1_name);
        menu_dish2 = findViewById(R.id.menu2_name);
        menu_dish3 = findViewById(R.id.menu3_name);
        menu_dish4 = findViewById(R.id.menu4_name);
        menu_dish5 = findViewById(R.id.menu5_name);
        cow250 = findViewById(R.id.cow250_rate);
        cow500 = findViewById(R.id.cow500_rate);
        cow1l = findViewById(R.id.cow1l_rate);
        buff250 = findViewById(R.id.buff250_rate);
        buff500 = findViewById(R.id.buff500_rate);
        buff1l = findViewById(R.id.buff1l_rate);
        menu_dish3_rate = findViewById(R.id.menu3_rate);
        menu_dish4_rate = findViewById(R.id.menu4_rate);
        menu_dish5_rate = findViewById(R.id.menu5_rate);
        dish3_quant = findViewById(R.id.dish3_quant);
        dish4_quant = findViewById(R.id.dish4_quant);
        dish5_quant = findViewById(R.id.dish5_quant);
        menu_total = findViewById(R.id.menutotal);
        home = findViewById(R.id.menu_home);
        save = findViewById(R.id.confirm);
        img1 = findViewById(R.id.dish1_img);
        img2 = findViewById(R.id.dish2_img);
        img3 = findViewById(R.id.dish3_img);
        img4 = findViewById(R.id.dish4_img);
        img5 = findViewById(R.id.dish5_img);
        menu_linear1 = findViewById(R.id.linear1);
        menu_linear2 = findViewById(R.id.linear2);
        menu_linear3 = findViewById(R.id.linear3);
        menu_linear4 = findViewById(R.id.linear4);
        menu_linear5 = findViewById(R.id.linear5);
        linear3 = findViewById(R.id.hori_linear3);
        linear4 = findViewById(R.id.hori_linear4);
        linear5 = findViewById(R.id.hori_linear5);
        dish3_addbtn = findViewById(R.id.dish3_addbtn);
        dish3_miunsbtn = findViewById(R.id.dish3_miunsbtn);
        dish4_addbtn = findViewById(R.id.dish4_addbtn);
        dish4_miunsbtn = findViewById(R.id.dish4_miunsbtn);
        dish5_addbtn = findViewById(R.id.dish5_addbtn);
        dish5_miunsbtn = findViewById(R.id.dish5_miunsbtn);
        dish3quantity = findViewById(R.id.menu3quantity);
        dish4quantity = findViewById(R.id.menu4quantity);
        dish5quantity = findViewById(R.id.menu5quantity);
        dish3stock = findViewById(R.id.menu3stock);
        dish4stock = findViewById(R.id.menu4stock);
        dish5stock = findViewById(R.id.menu5stock);
    }
    public class IdleCountDownTimer extends CountDownTimer {
        public IdleCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            //Time lapsed
//            bluetoothSerialmain.stop();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);

        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }



    @Override
    public void onUserInteraction(){
        super.onUserInteraction();

        //Reset the count down
        idleCountDownTimer.cancel();

    }
//    @Override
//   public void onBackPressed() {
//        super.onBackPressed();  // This enables the default back button behavior
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Reset the count down
        idleCountDownTimer.cancel();


        // Let the event flow
        return false;
    }
}