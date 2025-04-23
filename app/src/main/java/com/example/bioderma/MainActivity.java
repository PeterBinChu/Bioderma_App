package com.example.bioderma;


import android.app.AlertDialog;
import android.content.Context;

import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragments.AddProduct;
import fragments.DescriptionFragment;
import fragments.NewUser;
import fragments.NotRegProfile;
import fragments.RegProfile;
import fragments.ShoppingCardFragment;
import fragments.StoreFragment;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView btNavView;
    public FrameLayout frameLayout, frameLayout1;
    public ProgressBar progressBar_cyclic1;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String CHECKER = "checker";

    private SharedPreferences sharedPreferences1;
    private static final String SHARED_PREF_NAME1 = "mypref1";
    private static final String CHECKER1 = "checker1";

    private SharedPreferences keySharedPreferences;
    private static final String KEY_SHARED_PREF = "keyshared";
    private static final String KEY_CHECKER = "key";

    private SharedPreferences storeSharedPreferences;
    private static final String STORE_SHARED_PREF = "storeshared";
    private static final String STORE_CHECKER = "store";

    private SharedPreferences btmDisablePreference;
    private static final String DISABLE_SHARED_PREF = "disableshared";
    private static final String DISABLE_CHECKER = "disable";

    NavController navController;
    AppBarConfiguration appBarConfiguration;

    public AddProduct addProduct;
    public RegProfile regProfile;
    public NotRegProfile notRegProfile;
    public ShoppingCardFragment shoppingCardFragment;
    public StoreFragment storeFragment;
    public NewUser newUser;
    public DescriptionFragment descriptionFragment;
    public ImageView back, reload, back1;
    public Toolbar toolbar;
    public AlphaAnimation animation1, animation2;
    private DownloadingScreen downloadingScreen;
    private String fail;
    public Boolean mainBoolean=false;
    ConstraintLayout constraintLayout;

    private boolean backCheck=true, reloadMenu1=true;
    public boolean reloadMenu=false, anotherChecker=true, anotherChecker1=true, mainWaiter1=true, mainWaiter2=true;
    Boolean check;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        downloadingScreen=new DownloadingScreen();

        back=(ImageView) findViewById(R.id.back);
        back1=(ImageView) findViewById(R.id.back1);
        reload=(ImageView) findViewById(R.id.reload);
        btNavView = findViewById(R.id.bottomNavViewId);
        frameLayout = findViewById(R.id.frameLayoutId);
        frameLayout1=findViewById(R.id.frameLayoutId1);
        progressBar_cyclic1=findViewById(R.id.progressBar_cyclic1);

        constraintLayout=findViewById(R.id.mainDescriptionId);

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences1=getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
        keySharedPreferences=getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
        storeSharedPreferences=getSharedPreferences(STORE_SHARED_PREF, Context.MODE_PRIVATE);
        btmDisablePreference=getSharedPreferences(DISABLE_SHARED_PREF, Context.MODE_PRIVATE);

        fail=keySharedPreferences.getString(KEY_CHECKER, "");
        if(fail.matches("")){

        }

        SharedPreferences.Editor disableEditor=btmDisablePreference.edit();
        disableEditor.putBoolean(DISABLE_CHECKER, true);
        disableEditor.apply();

        SharedPreferences.Editor store=storeSharedPreferences.edit();
        store.putInt(STORE_CHECKER, 1);
        store.apply();

        toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);

        notRegProfile=new NotRegProfile();
        shoppingCardFragment=new ShoppingCardFragment();
        storeFragment=downloadingScreen.putStoreFragment();
        newUser=new NewUser();
        descriptionFragment=downloadingScreen.putDescriptionFragment();
        regProfile=new RegProfile(shoppingCardFragment, storeFragment);
        addProduct=new AddProduct(storeFragment);

        animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(500);
        animation1.setStartOffset(0);
        animation1.setFillAfter(false);

        animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation2.setDuration(500);
        animation2.setStartOffset(0);
        animation2.setFillAfter(false);

        btNavView.setSelectedItemId(R.id.store);

        SharedPreferences.Editor disableEditor2=btmDisablePreference.edit();
        disableEditor2.putBoolean(DISABLE_CHECKER, false);
        disableEditor2.apply();
        frameLayout.setVisibility(View.INVISIBLE);
        progressBar_cyclic1.setVisibility(View.VISIBLE);
        setFragment(storeFragment);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
                setFragment(shoppingCardFragment);
            }

            public void onFinish() {
                setFragment(storeFragment);
                disableEditor2.putBoolean(DISABLE_CHECKER, true);
                disableEditor2.apply();
                mainWaiter1=false;
                progressBar_cyclic1.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
                mainBoolean=true;
            }

        }.start();
        btNavView.setBackgroundResource(R.drawable.cute_tone_pink);

        btNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.store:
                        anotherChecker1=true;
                        check=btmDisablePreference.getBoolean(DISABLE_CHECKER, true);
                        if(check) {
                            mainWaiter2=true;
                            if(mainWaiter1){
                                frameLayout.setVisibility(View.INVISIBLE);
                                toolbar.setBackgroundResource(R.drawable.cute_tone_pink);
                                btNavView.setBackgroundResource(R.drawable.cute_tone_pink);
                                waiter();
                                mainWaiter1=false;
                            }
                            //progressBar_cyclic1.setVisibility(View.GONE);
                            reloadMenu1 = true;
                            animation1.setDuration(0);
                            reload.setVisibility(View.INVISIBLE);
                            back.setVisibility(View.INVISIBLE);
                            back1.setVisibility(View.INVISIBLE);
                            anotherChecker=true;
                            int d = storeSharedPreferences.getInt(STORE_CHECKER, 1);
                            if (d == 1) {
                                back1.setVisibility(View.INVISIBLE);
                                animation1.setDuration(0);
                                setFragment(storeFragment);
                            } else if (d == 2) {

                                if(mainWaiter1) {
                                    SharedPreferences.Editor disableEditor = btmDisablePreference.edit();
                                    disableEditor.putBoolean(DISABLE_CHECKER, false);
                                    disableEditor.apply();
                                    back1.startAnimation(animation1);
                                    mainWaiter1=false;
                                }
                                back1.setVisibility(View.VISIBLE);
                                setFragment(descriptionFragment);
                            }

                            return true;
                        }

                    case R.id.profile:
                        String keyCheck=keySharedPreferences.getString(KEY_CHECKER, "");
                        anotherChecker1=true;
                        check=btmDisablePreference.getBoolean(DISABLE_CHECKER, false);
                        int c2 = sharedPreferences.getInt(CHECKER, 1);
                        if(keyCheck.matches("") && c2==2){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            shoppingCardFragment.check=true;
                            editor.putInt(CHECKER, 1);
                            editor.apply();
                            btNavView.setEnabled(true);
                            waiter();
                            back.setVisibility(View.INVISIBLE);
                            setFragment(notRegProfile);
                            return true;
                        }
                        else {
                            if (check) {
                                mainWaiter2=true;
                                mainWaiter1=true;
                                btNavView.setEnabled(true);
                                //progressBar_cyclic1.setVisibility(View.GONE);
                                animation1.setDuration(500);
                                reload.setVisibility(View.VISIBLE);
                                back1.setVisibility(View.GONE);
                                if (reloadMenu1 == true) {
                                    frameLayout.setVisibility(View.INVISIBLE);
                                    toolbar.setBackgroundResource(R.drawable.cute_tone_orange);
                                    btNavView.setBackgroundResource(R.drawable.cute_tone_orange);
                                    reload.startAnimation(animation1);
                                    reloadMenu1 = false;
                                }
                                int c = sharedPreferences.getInt(CHECKER, 1);
                                if (c == 1) {
                                    waiter();
                                    back.setVisibility(View.INVISIBLE);
                                    setFragment(notRegProfile);
                                } else if (c == 2) {

                                    SharedPreferences.Editor disableEditor = btmDisablePreference.edit();
                                    disableEditor.putBoolean(DISABLE_CHECKER, false);
                                    disableEditor.apply();
                                    if (anotherChecker) {
                                        progressBar_cyclic1.setVisibility(View.VISIBLE);
                                        anotherChecker = false;
                                    } else {
                                        SharedPreferences.Editor disableEditor1 = btmDisablePreference.edit();
                                        disableEditor1.putBoolean(DISABLE_CHECKER, true);
                                        disableEditor1.apply();
                                    }
                                    back.setVisibility(View.INVISIBLE);
                                    setFragment(regProfile);
                                } else if (c == 3) {
                                    waiter();
                                    back.setVisibility(View.VISIBLE);
                                    back.startAnimation(animation1);
                                    setFragment(newUser);
                                } else if (c == 4) {
                                    waiter();
                                    back.setVisibility(View.VISIBLE);
                                    back.startAnimation(animation1);
                                    setFragment(addProduct);
                                }
                                return true;
                            }
                        }

                    case R.id.shopping_card:
                        check=btmDisablePreference.getBoolean(DISABLE_CHECKER, false);
                        if(check) {
                            mainWaiter1=true;
                            if(mainWaiter2){
                                frameLayout.setVisibility(View.INVISIBLE);
                                toolbar.setBackgroundResource(R.drawable.cute_tone_green);
                                btNavView.setBackgroundResource(R.drawable.cute_tone_green);
                                waiter();
                                mainWaiter2=false;
                            }
                            reloadMenu1 = true;
                            anotherChecker=true;
                            animation1.setDuration(0);
                            reload.setVisibility(View.INVISIBLE);
                            back.setVisibility(View.INVISIBLE);
                            back1.setVisibility(View.GONE);
                            setFragment(shoppingCardFragment);
                            return true;
                        }

                    default:
                        return false;
                }
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration );
    }

    public void logoClicked(View v){
        check=btmDisablePreference.getBoolean(DISABLE_CHECKER, true);
        if(check) {
            btNavView.setSelectedItemId(R.id.store);
            back1.setVisibility(View.GONE);

            SharedPreferences.Editor disableEditor=btmDisablePreference.edit();
            disableEditor.putBoolean(DISABLE_CHECKER, true);
            disableEditor.apply();

            SharedPreferences.Editor editor = storeSharedPreferences.edit();
            editor.putInt(STORE_CHECKER, 1);
            editor.apply();

            animation1.setDuration(0);

            setFragment(storeFragment);
        }
    }


    public void backClicked(View v){
        int c = sharedPreferences.getInt(CHECKER, 1);
        if(c==3) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(CHECKER, 1);
            editor.apply();
            newUser = new NewUser();
            back.setVisibility(View.INVISIBLE);
            setFragment(notRegProfile);
        }
        else if(c==4){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(CHECKER, 2);
            editor.apply();
            progressBar_cyclic1.setVisibility(View.VISIBLE);
            SharedPreferences.Editor disableEditor=btmDisablePreference.edit();
            disableEditor.putBoolean(DISABLE_CHECKER, false);
            disableEditor.apply();
            addProduct=new AddProduct();
            back.setVisibility(View.INVISIBLE);
            setFragment(regProfile);
        }
    }

    public void back1Clicked(View v){
        if(descriptionFragment.c1) {
            SharedPreferences.Editor editor = storeSharedPreferences.edit();
            editor.putInt(STORE_CHECKER, 1);
            editor.apply();
            back1.setVisibility(View.GONE);
            SharedPreferences.Editor disableEditor = btmDisablePreference.edit();
            disableEditor.putBoolean(DISABLE_CHECKER, true);
            disableEditor.apply();
            setFragment(storeFragment);
        }
    }

    public void reloadClicked(View v){
        int c = sharedPreferences.getInt(CHECKER, 1);
        check=btmDisablePreference.getBoolean(DISABLE_CHECKER, false);
        if(c==1){
            notRegProfile=new NotRegProfile();
            setFragment(notRegProfile);
        }
        else if(c==2){
            if(check) {
                SharedPreferences.Editor disableEditor = btmDisablePreference.edit();
                disableEditor.putBoolean(DISABLE_CHECKER, false);
                disableEditor.apply();
                regProfile = new RegProfile();
                setFragment(regProfile);
            }
        }
        else if(c==3){
            newUser=new NewUser();
            setFragment(newUser);
        }
        else if(c==4){
            addProduct=new AddProduct(storeFragment);
            setFragment(addProduct);
        }
    }


    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutId,fragment);
        fragmentTransaction.commit();
    }

    /*@Override
    public void onBackPressed() {
        int c = sharedPreferences.getInt(CHECKER, 1);
        if(c==1){
            Toast.makeText(this, "c=1", Toast.LENGTH_LONG).show();
        }
        else if(c==2){
            Toast.makeText(this, "c=2", Toast.LENGTH_LONG).show();
        }
        else if(c==3){
            Toast.makeText(this, "c=3", Toast.LENGTH_LONG).show();
        }
        super.onBackPressed();
    }

     */


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*int c = sharedPreferences.getInt(CHECKER, 1);
        if(keyCode==KeyEvent.KEYCODE_BACK && c==3) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(CHECKER, 1);
            editor.apply();
            back.setVisibility(View.INVISIBLE);
            setFragment(notRegProfile);
            return false;
        }
        else if(keyCode==KeyEvent.KEYCODE_BACK && c==2){}
         */
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            LinearLayout linearLayout;
            AppCompatButton appCompatButtonYes, appCompatButtonNo;
            ViewGroup viewGroup = findViewById(R.id.main);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.exit_layout, viewGroup, false);
            builder.setCancelable(false);
            builder.setView(view);
            linearLayout = view.findViewById(R.id.exit_id);
            appCompatButtonYes = view.findViewById(R.id.yes1);
            appCompatButtonNo = view.findViewById(R.id.no1);

            if (btNavView.getSelectedItemId() == R.id.store) {
                linearLayout.setBackgroundResource(R.drawable.border_with_shadow2);
                appCompatButtonYes.setBackgroundResource(R.drawable.edit_border2);
                appCompatButtonNo.setBackgroundResource(R.drawable.edit_border2);
            } else if (btNavView.getSelectedItemId() == R.id.shopping_card) {
                linearLayout.setBackgroundResource(R.drawable.border_with_shadow);
                appCompatButtonYes.setBackgroundResource(R.drawable.edit_border1);
                appCompatButtonNo.setBackgroundResource(R.drawable.edit_border1);
            } else if (btNavView.getSelectedItemId() == R.id.profile) {
                linearLayout.setBackgroundResource(R.drawable.border_with_shadow3);
                appCompatButtonYes.setBackgroundResource(R.drawable.edit_border3);
                appCompatButtonNo.setBackgroundResource(R.drawable.edit_border3);
            }
            AlertDialog alertDialog = builder.create();

            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            appCompatButtonYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                }
            });
            appCompatButtonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
        return false;
    }

    public void waiter(){
        SharedPreferences.Editor disableEditor=btmDisablePreference.edit();
        disableEditor.putBoolean(DISABLE_CHECKER, false);
        disableEditor.apply();

        new CountDownTimer(250, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                SharedPreferences.Editor disableEditor=btmDisablePreference.edit();
                disableEditor.putBoolean(DISABLE_CHECKER, true);
                disableEditor.apply();
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = storeSharedPreferences.edit();
        editor.putInt(STORE_CHECKER, 1);
        editor.apply();
        super.onDestroy();
    }
    public void forShop(){
        mainBoolean=false;
        shoppingCardFragment.clearMCardList();
        setFragment(shoppingCardFragment);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {shoppingCardFragment.clearMCardList(); }

            public void onFinish() {

                SharedPreferences.Editor disableEditor2=btmDisablePreference.edit();
                disableEditor2.putBoolean(DISABLE_CHECKER, true);
                disableEditor2.apply();
                setFragment(regProfile);
                frameLayout.setVisibility(View.VISIBLE);
                progressBar_cyclic1.setVisibility(View.GONE);
                mainBoolean=true;
            }
        }.start();
    }
}