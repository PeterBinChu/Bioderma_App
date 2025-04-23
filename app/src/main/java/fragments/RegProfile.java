package fragments;

import android.app.AlertDialog;

import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import androidx.fragment.app.Fragment;


import android.os.CountDownTimer;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.bioderma.MainActivity;
import com.example.bioderma.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GMailSenders.GMailSender;
import Objects.Product;
import Objects.User;


public class RegProfile extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String emailForDatabase;
    private String passwordCheck = "";
    private String newPassCode1="";
    private boolean f1=true, f2=true;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String CHECKER = "checker";

    private SharedPreferences keySharedPreferences;
    private static final String KEY_SHARED_PREF = "keyshared";
    private static final String KEY_CHECKER = "key";

    private SharedPreferences emailSharedPreferences;
    private static final String EMAIL_SHARED_PREF = "emailshared";
    private static final String EMAIL_CHECKER = "email";

    private SharedPreferences btmDisablePreference;
    private static final String DISABLE_SHARED_PREF = "disableshared";
    private static final String DISABLE_CHECKER = "disable";

    private static ShoppingCardFragment shoppingCardFragment;
    private static StoreFragment storeFragment;

    ImageView editDesc, question;
    TextView profile_nickname;
    Button changePassword;


    LinearLayout informationLinearLayout, mainRegisterLayout;

    EditText firstName, lastName, phoneNumber, country, city, street;
    ImageView profile_edit_save;
    CircularImageView profile_icon;

    FirebaseDatabase database;
    DatabaseReference reference;
    Query query;

    private boolean forEditDesc=false, forEditSave=false;
    public EditText description;

    private AlphaAnimation alphaAnimation;
    Button exit;
    private String a;
    private String email;
    AppCompatButton profile_settings, administrator_add;

    public RegProfile() {

    }
    public RegProfile(ShoppingCardFragment shoppingCardFragment1, StoreFragment storeFragment1){
        shoppingCardFragment=shoppingCardFragment1;
        storeFragment=storeFragment1;
    }

    public static RegProfile newInstance(String param1, String param2) {
        RegProfile fragment = new RegProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        database = FirebaseDatabase.getInstance("https://bioderma-d8248-default-rtdb.firebaseio.com/");
        reference = database.getReference().child("Users");
        sharedPreferences=this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        keySharedPreferences=this.getActivity().getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
        btmDisablePreference=this.getActivity().getSharedPreferences(DISABLE_SHARED_PREF, Context.MODE_PRIVATE);
        emailSharedPreferences=this.getActivity().getSharedPreferences(EMAIL_SHARED_PREF, Context.MODE_PRIVATE);


        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setStartOffset(0);
        alphaAnimation.setFillAfter(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg,
                container, false);


        exit=view.findViewById(R.id.exit);
        exit.setOnClickListener(this);

        mainRegisterLayout=view.findViewById(R.id.mainRegisterLayout);
        mainRegisterLayout.setVisibility(View.INVISIBLE);

        profile_settings=view.findViewById(R.id.profile_settings);
        profile_settings.setOnClickListener(this);
        administrator_add=view.findViewById(R.id.administrator_add);
        administrator_add.setOnClickListener(this);
        changePassword=view.findViewById(R.id.change_password);
        changePassword.setOnClickListener(this);

        editDesc=view.findViewById(R.id.profile_edit_desc);
        editDesc.setOnClickListener(this);
        question=view.findViewById(R.id.question) ;
        question.setOnClickListener(this);


        profile_edit_save=view.findViewById(R.id.profile_information_edit);
        profile_edit_save.setOnClickListener(this);

        profile_icon=view.findViewById(R.id.profile_icon);

        profile_nickname=view.findViewById(R.id.profile_nickname);
        profile_nickname.setOnClickListener(this);

        description=view.findViewById(R.id.profile_desc);


        firstName=view.findViewById(R.id.edit_first_name);

        lastName=view.findViewById(R.id.edit_last_name);

        phoneNumber=view.findViewById(R.id.edit_phone_number);

        country=view.findViewById(R.id.edit_country);

        city=view.findViewById(R.id.edit_city);

        street=view.findViewById(R.id.edit_street);


        informationLinearLayout=view.findViewById(R.id.profileInformationLayout);

        enableSet(false);

        String key2=keySharedPreferences.getString(KEY_CHECKER, "");
        query = reference.orderByChild(key2);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot single : snapshot.getChildren()) {
                    a = single.getValue(User.class).getKey();
                    if (key2.equals(a)) {
                        Boolean adminCheck=single.getValue(User.class).getAdministrator();
                        if(adminCheck){
                            profile_icon.setImageResource(R.drawable.round_logo_admin);
                            administrator_add.setVisibility(View.VISIBLE);
                        }
                        profile_nickname.setText(single.getValue(User.class).getNickname());
                        description.setText(single.getValue(User.class).getDesc());
                        firstName.setText(single.getValue(User.class).getFirstName());
                        lastName.setText(single.getValue(User.class).getSecondName());
                        phoneNumber.setText(single.getValue(User.class).getPhoneNumber());
                        country.setText(single.getValue(User.class).getCountry());
                        city.setText(single.getValue(User.class).getCity());
                        street.setText(single.getValue(User.class).getStreet());
                        email=single.getValue(User.class).getEmail();
                        mainRegisterLayout.startAnimation(alphaAnimation);
                        mainRegisterLayout.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                ((MainActivity) getContext()).progressBar_cyclic1.setVisibility(View.GONE);
                ((MainActivity) getContext()).frameLayout.setVisibility(View.VISIBLE);
                new CountDownTimer(500, 1000) {
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_password:
                changePassword.setEnabled(false);
                AppCompatButton appCompatButtonChange, appCompatButtonCancelPass, appCompatButtonSend, appCompatButtonConfirm;
                EditText passEmail, passEmailCode, newPass, newPassConfirm;
                ImageView again, eye, eye1;
                ViewGroup viewGroup3 = getView().findViewById(R.id.mainRegisterLayout);
                AlertDialog.Builder builder3 = new AlertDialog.Builder(getContext());
                View view3 = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.change_password_layout,viewGroup3,false);
                builder3.setCancelable(false);
                builder3.setView(view3);

                appCompatButtonChange=view3.findViewById(R.id.save);
                again=view3.findViewById(R.id.again);
                again.setOnClickListener(this);
                appCompatButtonCancelPass=view3.findViewById(R.id.cancel);
                appCompatButtonSend=view3.findViewById(R.id.send);
                appCompatButtonConfirm=view3.findViewById(R.id.confirm_email);
                eye=view3.findViewById(R.id.eye);
                eye.setOnClickListener(this);
                eye1=view3.findViewById(R.id.eye1);
                eye1.setOnClickListener(this);
                passEmail=view3.findViewById(R.id.email_change);
                passEmail.setText(email);
                passEmail.setEnabled(false);
                passEmail.setTextColor(Color.parseColor("#8BFFFFFF"));
                passEmailCode=view3.findViewById(R.id.email_code_change);
                newPass=view3.findViewById(R.id.password_change);
                newPassConfirm=view3.findViewById(R.id.password_change_confirm);

                AlertDialog alertDialog3 = builder3.create();

                alertDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                appCompatButtonSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        again.setVisibility(View.VISIBLE);
                        passEmailCode.setVisibility(View.VISIBLE);
                        passwordCheck = autoPassword();
                        autoSendEmail(passwordCheck, email);
                        appCompatButtonConfirm.setVisibility(View.VISIBLE);
                        appCompatButtonSend.setVisibility(View.GONE);
                    }
                });

                eye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(f1) {
                            newPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            eye.setImageResource(R.drawable.eye_close);
                            f1=false;
                        }
                        else{
                            newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            eye.setImageResource(R.drawable.eye_open);
                            f1=true;
                        }
                    }
                });

                eye1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(f2) {
                            newPassConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            eye1.setImageResource(R.drawable.eye_close);
                            f2=false;
                        }
                        else{
                            newPassConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            eye1.setImageResource(R.drawable.eye_open);
                            f2=true;
                        }
                    }
                });
                again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar = Snackbar.make(getView(), "New password sent", Snackbar.LENGTH_LONG);
                        snackbar.setAnchorView(((MainActivity) getActivity()).btNavView);
                        snackbar.show();
                        passwordCheck = autoPassword();
                        autoSendEmail(passwordCheck, email);
                    }
                });
                appCompatButtonConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPassCode1 = passEmailCode.getText().toString();
                        if (newPassCode1.contains(passwordCheck)) {
                            again.setEnabled(false);
                            appCompatButtonConfirm.setVisibility(View.GONE);
                            passEmailCode.setEnabled(false);
                            passEmailCode.setTextColor(Color.parseColor("#8BFFFFFF"));
                            appCompatButtonChange.setEnabled(true);
                            appCompatButtonChange.setTextColor(Color.parseColor("#FFFFFF"));
                            newPass.setVisibility(View.VISIBLE);
                            newPassConfirm.setVisibility(View.VISIBLE);
                            eye.setVisibility(View.VISIBLE);
                            eye1.setVisibility(View.VISIBLE);
                        }
                        else {
                            passEmailCode.setError("Wrong password");
                        }
                    }
                });
                appCompatButtonChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(newPass.getText().toString().trim().matches("")){
                            newPass.setError("Enter your password");
                        }
                        else if(newPassConfirm.getText().toString().trim().matches("")){
                            newPassConfirm.setError("Enter your password");
                        }
                        else{
                            if(newPass.getText().toString().matches(newPassConfirm.getText().toString())){
                                changePassword.setEnabled(true);
                                String key3=keySharedPreferences.getString(KEY_CHECKER, "");
                                query = reference.orderByChild(key3);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot single : snapshot.getChildren()) {
                                            a = single.getValue(User.class).getKey();
                                            if (key3.equals(a)) {
                                                Snackbar snackbar = Snackbar.make(getView(), "Password Changed", Snackbar.LENGTH_LONG);
                                                snackbar.setAnchorView(((MainActivity) getActivity()).btNavView);
                                                snackbar.show();
                                                single.getRef().child("password").setValue(newPass.getText().toString());
                                                alertDialog3.dismiss();
                                                break;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                            else{
                                newPass.setError("Password Difference");
                                newPassConfirm.setError("Password Difference");
                            }
                        }
                    }
                });
                appCompatButtonCancelPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePassword.setEnabled(true);
                        alertDialog3.dismiss();
                    }
                });

                alertDialog3.show();
                break;

            case R.id.question:
                question.setEnabled(false);
                AppCompatButton appCompatButtonOk;
                ViewGroup viewGroup2 = getView().findViewById(R.id.mainRegisterLayout);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                View view2 = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.profile_question,viewGroup2,false);
                builder2.setCancelable(false);
                builder2.setView(view2);
                appCompatButtonOk=view2.findViewById(R.id.ok);

                AlertDialog alertDialog2 = builder2.create();

                alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                appCompatButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        question.setEnabled(true);
                        alertDialog2.dismiss();
                    }
                });
                alertDialog2.show();
                break;

            case R.id.administrator_add:
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putInt(CHECKER, 4);
                editor1.apply();
                ((MainActivity)getActivity()).back.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).back.startAnimation(((MainActivity) getActivity()).animation1);
                ((MainActivity)getActivity()).setFragment(((MainActivity) getActivity()).addProduct=new AddProduct(storeFragment));
                break;
            case R.id.profile_nickname:
                profile_nickname.setEnabled(false);
                EditText nicknameChange;
                AppCompatButton appCompatButtonSave, appCompatButtonCancel;
                ViewGroup viewGroup1 = getView().findViewById(R.id.mainRegisterLayout);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                View view1 = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.nickname_alert,viewGroup1,false);
                builder1.setCancelable(false);
                builder1.setView(view1);
                appCompatButtonSave=view1.findViewById(R.id.save);
                appCompatButtonCancel=view1.findViewById(R.id.cancel);
                nicknameChange=view1.findViewById(R.id.nickname_change);
                nicknameChange.setText(profile_nickname.getText().toString());
                AlertDialog alertDialog1 = builder1.create();

                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                appCompatButtonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key3=keySharedPreferences.getString(KEY_CHECKER, "");
                        String editTextInput = nicknameChange.getText().toString();
                        if(editTextInput.matches(profile_nickname.getText().toString())){

                        }
                        else {
                            if (editTextInput.trim().matches("")) {
                                Snackbar snackbar = Snackbar.make(view1, "The nickname must have at least 1 character", Snackbar.LENGTH_LONG);
                                snackbar.setAnchorView(((MainActivity) getActivity()).btNavView);
                                snackbar.show();
                            } else {
                                query = reference.orderByChild(key3);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot single : snapshot.getChildren()) {
                                            a = single.getValue(User.class).getKey();
                                            if (key3.equals(a)) {
                                                profile_nickname.setText(editTextInput);
                                                single.getRef().child("nickname").setValue(editTextInput);
                                                Snackbar snackbar = Snackbar.make(getView(), "Nickname changed", Snackbar.LENGTH_LONG);
                                                snackbar.setAnchorView(((MainActivity) getActivity()).btNavView);
                                                snackbar.show();
                                                profile_nickname.setEnabled(true);
                                                alertDialog1.dismiss();
                                                break;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }
                    }
                });
                appCompatButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profile_nickname.setEnabled(true);
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1.show();
                break;

            case R.id.exit:
                exit.setEnabled(false);
                AppCompatButton appCompatButtonYes, appCompatButtonNo;
                ViewGroup viewGroup = getView().findViewById(R.id.mainRegisterLayout);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.reg_alert,viewGroup,false);
                builder.setCancelable(false);
                builder.setView(view);
                appCompatButtonYes=view.findViewById(R.id.yes);
                appCompatButtonNo=view.findViewById(R.id.no);

                AlertDialog alertDialog = builder.create();

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                appCompatButtonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Product> arrayList = new ArrayList();
                        arrayList = storeFragment.getMlist();
                        for(Product product:arrayList){
                            product.setShoppingCardExist(false);
                        }
                        storeFragment.setMlist(arrayList);

                        shoppingCardFragment.clearMCardList();
                        ((MainActivity)getActivity()).setFragment(((MainActivity) getActivity()).storeFragment);
                        ((MainActivity)getActivity()).btNavView.setSelectedItemId(R.id.store);
                        shoppingCardFragment.check=true;
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putInt(CHECKER, 1);
                        editor.apply();
                        SharedPreferences.Editor editor1=emailSharedPreferences.edit();
                        editor1.putString(EMAIL_CHECKER, "");
                        editor1.apply();
                        SharedPreferences.Editor editor2=keySharedPreferences.edit();
                        editor2.putString(KEY_CHECKER, "");
                        editor2.apply();
                        exit.setEnabled(true);
                        alertDialog.dismiss();
                    }
                });
                appCompatButtonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exit.setEnabled(true);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                break;

            case R.id.profile_information_edit:
                String key3=keySharedPreferences.getString(KEY_CHECKER, "");

                String firstName1=firstName.getText().toString();
                String lastName1=lastName.getText().toString();
                String phoneNumber1=phoneNumber.getText().toString();
                String country1=country.getText().toString();
                String city1=city.getText().toString();
                String street1=street.getText().toString();

                query = reference.orderByChild(key3);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot single : snapshot.getChildren()) {
                                a = single.getValue(User.class).getKey();
                                if (key3.equals(a)) {
                                    single.getRef().child("firstName").setValue(firstName1);
                                    single.getRef().child("secondName").setValue(lastName1);
                                    single.getRef().child("phoneNumber").setValue(phoneNumber1);
                                    single.getRef().child("country").setValue(country1);
                                    single.getRef().child("city").setValue(city1);
                                    single.getRef().child("street").setValue(street1);
                                    break;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                enableSet(false);
                profile_edit_save.setVisibility(View.INVISIBLE);
                break;

            case R.id.profile_edit_desc:
                String description1=description.getText().toString();
                String key2=keySharedPreferences.getString(KEY_CHECKER, "");
                query = reference.orderByChild(key2);
                if(forEditDesc){
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot single : snapshot.getChildren()) {
                                a = single.getValue(User.class).getKey();
                                if (key2.equals(a)) {
                                    single.getRef().child("desc").setValue(description1);
                                    break;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    forEditDesc=false;
                    description.setEnabled(false);
                    description.setHint("Description");
                    editDesc.setImageResource(R.drawable.edit);
                }
                else{
                    forEditDesc=true;
                    description.setEnabled(true);
                    description.setHint("Type your description");
                    editDesc.setImageResource(R.mipmap.ic_confirm);
                }
                break;

            case R.id.profile_settings:

                enableSet(true);

                profile_edit_save.setVisibility(View.VISIBLE);

                break;
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void enableSet(boolean c){
        firstName.setEnabled(c);
        firstName.setFocusable(c);
        firstName.setFocusableInTouchMode(c);

        lastName.setEnabled(c);
        lastName.setFocusable(c);
        lastName.setFocusableInTouchMode(c);

        phoneNumber.setEnabled(c);
        phoneNumber.setFocusable(c);
        phoneNumber.setFocusableInTouchMode(c);

        country.setEnabled(c);
        country.setFocusable(c);
        country.setFocusableInTouchMode(c);

        city.setEnabled(c);
        city.setFocusable(c);
        city.setFocusableInTouchMode(c);

        street.setEnabled(c);
        street.setFocusable(c);
        street.setFocusableInTouchMode(c);
    }

    public String autoPassword(){
        Random rand = new Random();
        String ch1 = "";
        int x=6;
        char ch = 0;
        for(int i=0; i<x; i++){
            int c=48+rand.nextInt(123);
            if(c>=48 && c<58 || c>=65 && c<91 || c>=97 && c<=122){
                ch= (char) c;
                System.out.print(ch);
                ch1+=String.valueOf(ch);
            }
            else{
                i--;
            }
        }
        return ch1;
    }
    public void autoSendEmail(String newPass, String email2){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("<YOUR_EMAIL_HERE>",
                            "<YOUR_PASSWORD_HERE>");
                    sender.sendMail("confirm your mail", "",
                            "<YOUR_EMAIL_HERE>", email2, newPass);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();
    }
}