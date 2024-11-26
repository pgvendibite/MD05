package com.example.MD;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
//import static com.example.MD.MainActivity.bluetoothSerialmain;
import static com.example.MD.Menu.hasordecode;


public class Admin extends AppCompatActivity {
    EditText owner_email,password,mobile_no,targetSSID,targetpassword,merchant_data,cowsnf,cowdensity,cowprotein,cowfat,buffalosnf,buffalodensity,buffaloprotein,buffalofat;
    Button home,free,paid;
    TextView dish1status,dish2status,dish3status,dish4status,dish5status,ID1,ID2,ID3,ID4,ID5,value,dish1,dish2;
    EditText dish3,dish4,dish5,dish3quant,dish4quant,dish5quant;
    public static int Retrytime = 200000;
    EditText cow250_rate,cow500_rate,cow1l_rate,buff250_rate,buff500_rate,buff1l_rate,dish3_rate,dish4_rate,dish5_rate;
    Button save, dish1_enable,dish1_disable,dish2_enable,dish2_disable,dish3_enable,dish3_disable,dish4_enable,dish4_disable,dish5_enable,dish5_disable;
    String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    Matcher m;
    ImageView stock,clean;
    Context context;
    String B="B";
    static int i = 0;
    String Modelno,dish1name,dish2name,dish3name,dish4name,dish5name;
    private boolean crlf = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin);
        context=this;

        findViewById(android.R.id.content).setFocusableInTouchMode(true);
        overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
      init();
//        clean = findViewById(R.id.cleaning);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the main screen
                Intent intent = new Intent(Admin.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // This will finish the current activity and remove it from the back stack
            }
        });



        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockdialog();
            }
        });


/*        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    hasordecode.size();
                    hasordecode.put("D","(0,0,0,0,0,1)");
                    Intent intent = new Intent(getApplicationContext(),TerminalActivity.class);
                    startActivity(intent);
                }catch (Exception e){}

            }
        });*/

        SharedPreferences sharedPreferences = getSharedPreferences("p006", MODE_PRIVATE);

        String email = sharedPreferences.getString("email","");
        owner_email.setText(email);
        String phone1 = sharedPreferences.getString("phone","");
        mobile_no.setText(phone1);
        String wname = sharedPreferences.getString("wifin","");
        targetSSID.setText(wname);
        String wpass = sharedPreferences.getString("wifip", "");
        targetpassword.setText(wpass);
        String pass = sharedPreferences.getString("password","");
        password.setText(pass);
        String mid = sharedPreferences.getString("merchant", "");
        merchant_data.setText(mid);
        String buffalosnf1 = sharedPreferences.getString("buffaloSNF", "");
        buffalosnf.setText(buffalosnf1);
        String buffalodensity1 = sharedPreferences.getString("buffaloDensity", "");
        buffalodensity.setText(buffalodensity1);
        String buffaloprotein1 = sharedPreferences.getString("buffaloProtein", "");
        buffaloprotein.setText(buffaloprotein1);
        String buffalofat1 = sharedPreferences.getString("buffaloFat", "");
        buffalofat.setText(buffalofat1);
        String cowsnf1 = sharedPreferences.getString("cowSNF", "");
        cowsnf.setText(cowsnf1);
        String cowdensity1 = sharedPreferences.getString("cowDensity", "");
        cowdensity.setText(cowdensity1);
        String cowprotein1 = sharedPreferences.getString("cowProtein", "");
        cowprotein.setText(cowprotein1);
        String cowfat1 = sharedPreferences.getString("cowFat", "");
        cowfat.setText(cowfat1);
        //dish rate
        String cow1rate = sharedPreferences.getString("cow1rate","");
        cow250_rate.setText(cow1rate);
        String cow2rate = sharedPreferences.getString("cow2rate", "");
        cow500_rate.setText(cow2rate);
        String cow3rate = sharedPreferences.getString("cow3rate", "");
        cow1l_rate.setText(cow3rate);
        String buff1rate = sharedPreferences.getString("buff1rate","");
        buff250_rate.setText(buff1rate);
        String buff2rate = sharedPreferences.getString("buff2rate", "");
        buff500_rate.setText(buff2rate);
        String buff3rate = sharedPreferences.getString("buff3rate", "");
        buff1l_rate.setText(buff3rate);
        String dish3rate = sharedPreferences.getString("dish3rate", "");
        dish3_rate.setText(dish3rate);
        String dish4rate = sharedPreferences.getString("dish4rate", "");
        dish4_rate.setText(dish4rate);
        String dish5rate = sharedPreferences.getString("dish5rate", "");
        dish5_rate.setText(dish5rate);

        dish1name = sharedPreferences.getString("dish1","");
        dish1.setText(dish1name);
        dish2name = sharedPreferences.getString("dish2", "");
        dish2.setText(dish2name);
        dish3name = sharedPreferences.getString("dish3", "");
        dish3.setText(dish3name);
        dish4name = sharedPreferences.getString("dish4", "");
        dish4.setText(dish4name);
        dish5name = sharedPreferences.getString("dish5", "");
        dish5.setText(dish5name);
        String val = sharedPreferences.getString("valuee","");
        value.setText(val);

        String dish3q = sharedPreferences.getString("dish3quant", "");
        dish3quant.setText(dish3q);
        String dish4q = sharedPreferences.getString("dish4quant", "");
        dish4quant.setText(dish4q);
        String dish5q = sharedPreferences.getString("dish5quant", "");
        dish5quant.setText(dish5q);


        if (val.equals("free")){
            free.setVisibility(View.GONE);
            paid.setVisibility(View.VISIBLE);
        }

        if (val.equals("paid")){
            paid.setVisibility(View.GONE);
            free.setVisibility(View.VISIBLE);
        }



        String dish1stts = sharedPreferences.getString("dishstatus1","");
        dish1status.setText(dish1stts);

        String dish2stts = sharedPreferences.getString("dishstatus2","");
        dish2status.setText(dish2stts);

        String dish3stts = sharedPreferences.getString("dishstatus3","");
        dish3status.setText(dish3stts);

        String dish4stts = sharedPreferences.getString("dishstatus4","");
        dish4status.setText(dish4stts);

        String dish5stts = sharedPreferences.getString("dishstatus5","");
        dish5status.setText(dish5stts);

        if (dish1stts.equals("enable"))
        {
            dish1_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish1_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish1stts.equals("disable"))
        {
            dish1_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish1_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }

        if (dish2stts.equals("enable"))
        {
            dish2_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish2_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish2stts.equals("disable"))
        {
            dish2_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish2_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }
        if (dish3stts.equals("enable"))
        {
            dish3_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish3_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish3stts.equals("disable"))
        {
            dish3_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish3_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }
        if (dish4stts.equals("enable"))
        {
            dish4_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish4_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish4stts.equals("disable"))
        {
            dish4_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish4_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }
        if (dish5stts.equals("enable"))
        {
            dish5_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish5_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish5stts.equals("disable"))
        {
            dish5_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish5_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }

        dish1_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish1status.setText("enable");
                dish1_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish1_disable.setBackgroundColor(Color.parseColor("#708090"));

            }
        });

        dish1_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish1status.setText("disable");
                dish1_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish1_enable.setBackgroundColor(Color.parseColor("#708090"));



            }
        });



        dish2_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish2status.setText("enable");
                dish2_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish2_disable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish2_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish2status.setText("disable");
                dish2_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish2_enable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish3_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish3status.setText("enable");
                dish3_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish3_disable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish3_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish3status.setText("disable");
                dish3_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish3_enable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });


        dish4_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish4status.setText("enable");
                dish4_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish4_disable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish4_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish4status.setText("disable");
                dish4_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish4_enable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish5_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish5status.setText("enable");
                dish5_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish5_disable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish5_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish5status.setText("disable");
                dish5_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish5_enable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });


        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value.setText("free");
                free.setVisibility(View.GONE);
                paid.setVisibility(View.VISIBLE);

            }
        });

        paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value.setText("paid");
                paid.setVisibility(View.GONE);
                free.setVisibility(View.VISIBLE);

            }
        });






        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("p006", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("email", owner_email.getText().toString().trim());
                editor.putString("phone", mobile_no.getText().toString().trim());
                editor.putString("merchant",merchant_data.getText().toString().trim());
                editor.putString("wifin",targetSSID.getText().toString().trim());
                editor.putString("wifip",targetpassword.getText().toString().trim());
                editor.putString("password",password.getText().toString().trim());
                editor.putString("cowSNF",cowsnf.getText().toString().trim());
                editor.putString("cowDensity",cowdensity.getText().toString().trim());
                editor.putString("cowProtein",cowprotein.getText().toString().trim());
                editor.putString("cowFat",cowfat.getText().toString().trim());
                editor.putString("buffaloSNF",buffalosnf.getText().toString().trim());
                editor.putString("buffaloDensity",buffalodensity.getText().toString().trim());
                editor.putString("buffaloProtein",buffaloprotein.getText().toString().trim());
                editor.putString("buffaloFat",buffalofat.getText().toString().trim());

                //Dish rate
                editor.putString("cow1rate",cow250_rate.getText().toString().trim());
                editor.putString("cow2rate",cow500_rate.getText().toString().trim());
                editor.putString("cow3rate",cow1l_rate.getText().toString().trim());
                editor.putString("buff1rate",buff250_rate.getText().toString().trim());
                editor.putString("buff2rate",buff500_rate.getText().toString().trim());
                editor.putString("buff3rate",buff1l_rate.getText().toString().trim());
                editor.putString("dish3rate",dish3_rate.getText().toString().trim());
                editor.putString("dish4rate",dish4_rate.getText().toString().trim());
                editor.putString("dish5rate",dish5_rate.getText().toString().trim());

                //Dish name
                editor.putString("dish1",dish1.getText().toString().trim());
                editor.putString("dish2",dish2.getText().toString().trim());
                editor.putString("dish3",dish3.getText().toString().trim());
                editor.putString("dish4",dish4.getText().toString().trim());
                editor.putString("dish5",dish5.getText().toString().trim());

                //Dish quantity
                editor.putString("dish3quant",dish3quant.getText().toString().trim());
                editor.putString("dish4quant",dish3quant.getText().toString().trim());
                editor.putString("dish5quant",dish3quant.getText().toString().trim());





                //Dish status
                editor.putString("dishstatus1",dish1status.getText().toString().trim());
                editor.putString("dishstatus2",dish2status.getText().toString().trim());
                editor.putString("dishstatus3",dish3status.getText().toString().trim());
                editor.putString("dishstatus4",dish4status.getText().toString().trim());
                editor.putString("dishstatus5",dish5status.getText().toString().trim());
                editor.putString("valuee",value.getText().toString().trim());




/*                String ownername = owner_name.getText().toString();
                String machinenumber = model.getText().toString();
                String modelname =Modelname .getText().toString();
                String btaddress = other_info.getText().toString();
                String btname = sn.getText().toString();
                String city =City.getText().toString();
                String mobileno = mobile_no .getText().toString();
                String id1 =ID1 .getText().toString();
                String id2 = ID2.getText().toString();
                String id3 = ID3.getText().toString();

                String dish1name =dish1 .getText().toString();
                String dish2name = dish2.getText().toString();
                String dish3name = dish3.getText().toString();

                String dish1price =dish1_rate .getText().toString();
                String dish2price = dish2_rate.getText().toString();
                String dish3price = dish3_rate.getText().toString();*/

              //  Updaterofile(ownername,  machinenumber,  modelname,  btaddress,  btname,  city, mobileno, dish1name, dish2name, dish3name,id1,id2,id3,dish1price,dish2price,dish3price);

                    editor.apply();
                Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int[] sourceCoordinates = new int[2];
            v.getLocationOnScreen(sourceCoordinates);
            float x = ev.getRawX() + v.getLeft() - sourceCoordinates[0];
            float y = ev.getRawY() + v.getTop() - sourceCoordinates[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                hideKeyboard(Admin.this);
            }

        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard(Activity activity) {
        findViewById(android.R.id.content).clearFocus();
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().getDecorView();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }

//    public void onBackPressed() {
//        super.onBackPressed();
//        bluetoothSerialmain.stop();
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(intent);
//        finish();
//    }




    private void stockdialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.stockr);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button button = dialog.findViewById(R.id.savestock);
        EditText stock1 = dialog.findViewById(R.id.dish1stock);
        EditText stock2 = dialog.findViewById(R.id.dish2stock);
        EditText stock3 = dialog.findViewById(R.id.dish3stock);
        EditText stock4 = dialog.findViewById(R.id.dish4stock);
        EditText stock5 = dialog.findViewById(R.id.dish5stock);
        TextView stock1name = dialog.findViewById(R.id.dish1stockname);
        TextView stock2name = dialog.findViewById(R.id.dish2stockname);
        TextView stock3name = dialog.findViewById(R.id.dish3stockname);
        TextView stock4name = dialog.findViewById(R.id.dish4stockname);
        TextView stock5name = dialog.findViewById(R.id.dish5stockname);



        SharedPreferences sharedPreferences = getSharedPreferences("jc", MODE_PRIVATE);
        //owner info
        String dishstock1 = sharedPreferences.getString("stock1", "");
        stock1.setText(dishstock1);
        String dishstock2 = sharedPreferences.getString("stock2", "");
        stock2.setText(dishstock2);
        String dishstock3 = sharedPreferences.getString("stock3", "");
        stock3.setText(dishstock3);
        String dishstock4 = sharedPreferences.getString("stock4", "");
        stock4.setText(dishstock4);
        String dishstock5 = sharedPreferences.getString("stock5", "");
        stock5.setText(dishstock5);


        stock1name.setText(dish1name);
        stock2name.setText(dish2name);
        stock3name.setText(dish3name);
        stock4name.setText(dish4name);
        stock5name.setText(dish5name);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("jc", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //owner info.
                editor.putString("stock1", stock1.getText().toString().trim());
                editor.putString("stock2", stock2.getText().toString().trim());
                editor.putString("stock3",stock3.getText().toString().trim());
                editor.putString("stock4",stock4.getText().toString().trim());
                editor.putString("stock5",stock5.getText().toString().trim());
                Toast.makeText(getApplicationContext(),"Stock Saved Successfully",Toast.LENGTH_SHORT).show();
                editor.apply();
                dialog.dismiss();



            }
        });
        if(!((Activity) context).isFinishing())
        {
            //show dialog
            dialog.show();
        }

    }

private void init(){
    owner_email = findViewById(R.id.editemail);
    mobile_no = findViewById(R.id.editno);
    password = findViewById(R.id.editpass);
//    bt_name = findViewById(R.id.btname);
//    bt_add = findViewById(R.id.btadd);
    targetSSID= findViewById(R.id.wifiname);
    targetpassword=findViewById(R.id.wifipassword);
    merchant_data = findViewById(R.id.editmid);
    cowsnf = findViewById(R.id.cowsnf);
    cowdensity = findViewById(R.id.cowdensity);
    cowprotein = findViewById(R.id.cowprotein);
    cowfat = findViewById(R.id.cowfat);
    buffalosnf = findViewById(R.id.buffalosnf);
    buffalodensity = findViewById(R.id.buffalodensity);
    buffaloprotein = findViewById(R.id.buffaloprotein);
    buffalofat = findViewById(R.id.buffalofat);
    home = findViewById(R.id.home);
    stock=findViewById(R.id.stock);
    dish1 = findViewById(R.id.admin_optn1_name);
    dish2 = findViewById(R.id.admin_optn2_name);
    dish3 = findViewById(R.id.admin_optn3_name);
    dish4 = findViewById(R.id.admin_optn4_name);
    dish5 = findViewById(R.id.admin_optn5_name);
    cow250_rate = findViewById(R.id.cow250_rate);
    cow500_rate = findViewById(R.id.cow500_rate);
    cow1l_rate = findViewById(R.id.cow1l_rate);
    buff250_rate = findViewById(R.id.buff250_rate);
    buff500_rate = findViewById(R.id.buff500_rate);
    buff1l_rate = findViewById(R.id.buff1l_rate);
    dish3_rate = findViewById(R.id.admin_optn3_rate);
    dish4_rate = findViewById(R.id.admin_optn4_rate);
    dish5_rate = findViewById(R.id.admin_optn5_rate);
    dish1status = findViewById(R.id.optn1_status);
    dish2status = findViewById(R.id.optn2_status);
    dish3status = findViewById(R.id.optn3_status);
    dish4status = findViewById(R.id.optn4_status);
    dish5status = findViewById(R.id.optn5_status);
    dish1_enable = findViewById(R.id.dish1_enable);
    dish1_disable = findViewById(R.id.dish1_disable);
    dish2_enable = findViewById(R.id.dish2_enable);
    dish2_disable = findViewById(R.id.dish2_disable);
    dish3_enable = findViewById(R.id.dish3_enable);
    dish3_disable = findViewById(R.id.dish3_disable);
    dish4_enable = findViewById(R.id.dish4_enable);
    dish4_disable = findViewById(R.id.dish4_disable);
    dish5_enable = findViewById(R.id.dish5_enable);
    dish5_disable = findViewById(R.id.dish5_disable);
    dish3quant = findViewById(R.id.admin_optn3quant);
    dish4quant = findViewById(R.id.admin_optn4quant);
    dish5quant = findViewById(R.id.admin_optn5quant);
    save = findViewById(R.id.save);
    ID1=findViewById(R.id.sr1);
    ID2=findViewById(R.id.sr2);
    ID3=findViewById(R.id.sr3);
    ID4=findViewById(R.id.sr4);
    ID5=findViewById(R.id.sr5);
    free = findViewById(R.id.free);
    paid = findViewById(R.id.paid);
    value = findViewById(R.id.value);
}
}