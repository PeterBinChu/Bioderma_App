package fragments;


import android.app.AlertDialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bioderma.MainActivity;
import com.example.bioderma.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GMailSenders.GMailSender;
import Objects.User;

public class NotRegProfile extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private MediaPlayer mp;
    private MediaPlayer mp1;
    private boolean f1=true, f2=true;
    private boolean check=false, c=true, code=true;
    boolean findEmailChecker=false, findPasswordChecker=false, findEmailChecker1=false, findPasswordChecker1=false;
    private Button reg, sign, forget;
    private TextView notReg;
    private ImageView eye, face, again;
    private EditText pass, email, newPassCode;
    private String pass1, email1, newPassCode1;
    private String passwordCheck = "";
    private String emailForDatabase;
    private String nickname;
    private String a, key;
    private TextView emailTextView;
    AudioManager audioManager;
    Context context;
    String getEmailId;



    private RegProfile regProfile;

    FirebaseDatabase database;
    DatabaseReference reference;
    Query query, query1;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String CHECKER = "checker";

    private SharedPreferences sharedPreferences1;
    private static final String SHARED_PREF_NAME1 = "mypref1";
    private static final String CHECKER1 = "checker1";

    private SharedPreferences sharedPreferences2;
    private static final String SHARED_PREF_NAME2 = "mypref2";
    private static final String CHECKER2 = "checker2";

    private SharedPreferences keySharedPreferences;
    private static final String KEY_SHARED_PREF = "keyshared";
    private static final String KEY_CHECKER = "key";

    private SharedPreferences emailSharedPreferences;
    private static final String EMAIL_SHARED_PREF = "emailshared";
    private static final String EMAIL_CHECKER = "email";

    private SharedPreferences btmDisablePreference;
    private static final String DISABLE_SHARED_PREF = "disableshared";
    private static final String DISABLE_CHECKER = "disable";



    public NotRegProfile() {

    }



    public static NotRegProfile newInstance(String param1, String param2) {
        NotRegProfile fragment = new NotRegProfile();
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

        regProfile=new RegProfile();
        sharedPreferences=this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences1=this.getActivity().getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
        sharedPreferences2=this.getActivity().getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        keySharedPreferences=this.getActivity().getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
        emailSharedPreferences=this.getActivity().getSharedPreferences(EMAIL_SHARED_PREF, Context.MODE_PRIVATE);
        btmDisablePreference=this.getActivity().getSharedPreferences(DISABLE_SHARED_PREF, Context.MODE_PRIVATE);
        context=getActivity();
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 12, 0);
        mp = MediaPlayer.create(getContext(), R.raw.sms_tone);
        mp1 = MediaPlayer.create(getContext(), R.raw.sms_pretty);
        mp.setVolume(100f , 100f);
        mp1.setVolume(100f , 100f);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_reg,
                container, false);
        reg =view.findViewById(R.id.reg);
        reg.setOnClickListener(this);
        sign=view.findViewById(R.id.sign);
        sign.setOnClickListener(this);
        eye= view.findViewById(R.id.eye);
        eye.setOnClickListener(this);
        again=view.findViewById(R.id.again);
        again.setOnClickListener(this);
        forget=view.findViewById(R.id.forgotten);
        forget.setOnClickListener(this);

        emailTextView= view.findViewById(R.id.emailTextView);
        newPassCode= view.findViewById(R.id.newPassCode);
        pass= view.findViewById(R.id.password);
        email= view.findViewById(R.id.email);
        notReg= view.findViewById(R.id.notReg);
        face= view.findViewById(R.id.face);
        ((MainActivity) getContext()).frameLayout.setVisibility(View.VISIBLE);
        setHasOptionsMenu(true);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgotten:
                forget.setEnabled(false);
                AppCompatButton appCompatButtonChange, appCompatButtonCancelPass, appCompatButtonSend, appCompatButtonConfirm;
                EditText passEmail, passEmailCode, newPass, newPassConfirm;
                ImageView again1, eye3, eye1;
                ViewGroup viewGroup3 = getView().findViewById(R.id.mainRegisterLayout);
                AlertDialog.Builder builder3 = new AlertDialog.Builder(getContext());
                View view3 = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.change_password_layout,viewGroup3,false);
                builder3.setCancelable(false);
                builder3.setView(view3);

                appCompatButtonChange=view3.findViewById(R.id.save);
                again1=view3.findViewById(R.id.again);
                again1.setOnClickListener(this);
                appCompatButtonCancelPass=view3.findViewById(R.id.cancel);
                appCompatButtonSend=view3.findViewById(R.id.send);
                appCompatButtonConfirm=view3.findViewById(R.id.confirm_email);
                eye3=view3.findViewById(R.id.eye);
                eye3.setOnClickListener(this);
                eye1=view3.findViewById(R.id.eye1);
                eye1.setOnClickListener(this);
                passEmail=view3.findViewById(R.id.email_change);
                passEmailCode=view3.findViewById(R.id.email_code_change);
                newPass=view3.findViewById(R.id.password_change);
                newPassConfirm=view3.findViewById(R.id.password_change_confirm);

                AlertDialog alertDialog3 = builder3.create();

                alertDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                appCompatButtonSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(passEmail.getText().toString().trim().matches("")){
                            passEmail.setError("Enter e-mail");
                        }
                        else {
                            emailForDatabase = passEmail.getText().toString().replace(".", "");
                            query = reference.orderByChild(emailForDatabase);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot single : snapshot.getChildren()) {
                                        a = single.getValue(User.class).getEmail();
                                        if (passEmail.getText().toString().equals(a)) {
                                            passEmail.setEnabled(false);
                                            again1.setVisibility(View.VISIBLE);
                                            passEmailCode.setVisibility(View.VISIBLE);
                                            passwordCheck = autoPassword();
                                            autoSendEmail(passwordCheck, a);
                                            appCompatButtonConfirm.setVisibility(View.VISIBLE);
                                            appCompatButtonSend.setVisibility(View.GONE);
                                            key = single.getValue(User.class).getKey();
                                            findEmailChecker = true;
                                            break;
                                        }
                                    }
                                    if(!findEmailChecker){
                                        passEmail.setError("No e-mail founded");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });

                eye3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(f1) {
                            newPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            eye3.setImageResource(R.drawable.eye_close);
                            f1=false;
                        }
                        else{
                            newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            eye3.setImageResource(R.drawable.eye_open);
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
                again1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar = Snackbar.make(getView(), "New password sent", Snackbar.LENGTH_LONG);
                        snackbar.setAnchorView(((MainActivity) getActivity()).btNavView);
                        snackbar.show();
                        passwordCheck = autoPassword();
                        autoSendEmail(passwordCheck, passEmail.getText().toString());
                    }
                });
                appCompatButtonConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPassCode1 = passEmailCode.getText().toString();
                        if (newPassCode1.contains(passwordCheck)) {
                            again1.setEnabled(false);
                            appCompatButtonConfirm.setVisibility(View.GONE);
                            passEmailCode.setEnabled(false);
                            passEmailCode.setTextColor(Color.parseColor("#8BFFFFFF"));
                            appCompatButtonChange.setEnabled(true);
                            appCompatButtonChange.setTextColor(Color.parseColor("#FFFFFF"));
                            newPass.setVisibility(View.VISIBLE);
                            newPassConfirm.setVisibility(View.VISIBLE);
                            eye3.setVisibility(View.VISIBLE);
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
                                emailForDatabase = passEmail.getText().toString().replace(".", "");
                                query = reference.orderByChild(emailForDatabase);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot single : snapshot.getChildren()) {
                                            a = single.getValue(User.class).getEmail();
                                            if (passEmail.getText().toString().equals(a)) {
                                                single.getRef().child("password").setValue(newPass.getText().toString());
                                                forget.setEnabled(true);
                                                alertDialog3.dismiss();
                                                Snackbar snackbar = Snackbar.make(getView(), "Password Changed", Snackbar.LENGTH_LONG);
                                                snackbar.setAnchorView(((MainActivity) getActivity()).btNavView);
                                                snackbar.show();
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
                        forget.setEnabled(true);
                        alertDialog3.dismiss();
                    }
                });

                alertDialog3.show();
                break;
            case R.id.sign:
                email1=email.getText().toString();
                pass1=pass.getText().toString();
                newPassCode1=newPassCode.getText().toString();
                if(email1.trim().matches("")){
                    email.setError("Enter your email address");
                }
                else if(pass1.trim().matches("")){
                    pass.setError("Enter your password");
                }
                else if(email1!="" || pass1!=""){
                    if (!isEmailValid(email1)){
                        email.setError("Your Email Id is Invalid.");
                    }
                    else {
                        if(!findEmailChecker1) {
                            emailForDatabase = email1.replace(".", "");
                            query = reference.orderByChild(emailForDatabase);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot single : snapshot.getChildren()) {
                                        a = single.getValue(User.class).getEmail();
                                        if (email1.equals(a)) {
                                            key=single.getValue(User.class).getKey();
                                            findEmailChecker = true;
                                            break;
                                        }
                                    }
                                    findEmailChecker1=true;
                                    v.performClick();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else {
                            if (!findEmailChecker) {
                                email.setError("No email founded");
                                findEmailChecker1=false;
                                findEmailChecker=false;
                                findPasswordChecker=false;
                                findPasswordChecker1=false;
                            }
                            else {
                                if(!findPasswordChecker1) {
                                        query1 = reference.orderByChild(key);
                                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot single : snapshot.getChildren()) {
                                                a=single.getValue(User.class).getKey();
                                                String b = single.getValue(User.class).getPassword();
                                                if (pass1.equals(b) && a.equals(key)) {
                                                    key=a;
                                                    nickname=single.getValue(User.class).getNickname();
                                                    findPasswordChecker = true;
                                                    break;
                                                }
                                            }
                                            findPasswordChecker1=true;
                                            v.performClick();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                else {
                                    if (!findPasswordChecker) {
                                        pass.setError("Invalid password");
                                        findPasswordChecker1=false;
                                        findEmailChecker=false;
                                        findPasswordChecker=false;
                                        findEmailChecker1=false;
                                    } else if(findPasswordChecker && findEmailChecker){
                                        if (code) {
                                            check=true;
                                            email.setEnabled(false);
                                            pass.setEnabled(false);
                                            again.setVisibility(View.VISIBLE);
                                            newPassCode.setVisibility(View.VISIBLE);
                                            emailTextView.setVisibility(View.VISIBLE);
                                            sign.setText("confirm");
                                            passwordCheck = autoPassword();
                                            autoSendEmail(passwordCheck, email1);
                                            code = false;
                                        } else {
                                            newPassCode1 = newPassCode.getText().toString();
                                            if (newPassCode1.contains(passwordCheck)) {
                                                ((MainActivity)getActivity()).shoppingCardFragment.clearMCardList();
                                                mp1.start();
                                                code = true;
                                                passwordCheck = "";
                                                DatabaseReference push = reference.push();
                                                SharedPreferences.Editor keyEditor=keySharedPreferences.edit();
                                                keyEditor.putString(KEY_CHECKER, key);
                                                keyEditor.apply();

                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putInt(CHECKER, 2);
                                                editor.apply();

                                                SharedPreferences.Editor editor1 = emailSharedPreferences.edit();
                                                editor1.putString(EMAIL_CHECKER, email1);
                                                editor1.apply();
                                                SharedPreferences.Editor disableEditor2=btmDisablePreference.edit();
                                                disableEditor2.putBoolean(DISABLE_CHECKER, false);
                                                disableEditor2.apply();
                                                ((MainActivity) getActivity()).shoppingCardFragment.check=true;
                                                ((MainActivity)getActivity()).notRegProfile=new NotRegProfile();
                                                ((MainActivity) getActivity()).frameLayout.setVisibility(View.INVISIBLE);
                                                ((MainActivity) getActivity()).progressBar_cyclic1.setVisibility(View.VISIBLE);
                                                ((MainActivity)getActivity()).forShop();

                                                pass.setText("");
                                                newPassCode.setText("");
                                            } else {
                                                newPassCode.setError("Wrong password");
                                            }
                                        }
                                    }
                                }


                            }
                        }
                    }

                }
                break;
            case R.id.reg:
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt(CHECKER, 3);
                editor.apply();
                ((MainActivity)getActivity()).back.startAnimation(((MainActivity)getActivity()).animation1);
                ((MainActivity)getActivity()).back.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).setFragment(((MainActivity) getActivity()).newUser);
                break;
            case R.id.eye:
                if(c) {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.eye_close);
                    c=false;
                }
                else{
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.eye_open);
                    c=true;
                }
                break;
            case R.id.again:
                email1=email.getText().toString();
                Snackbar.make(getView(), "New password sent", Snackbar.LENGTH_LONG ).show();
                passwordCheck = autoPassword();
                autoSendEmail(passwordCheck, email1);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        if(check){
            email.setEnabled(false);
            pass.setEnabled(false);
            again.setVisibility(View.VISIBLE);
            newPassCode.setVisibility(View.VISIBLE);
            emailTextView.setVisibility(View.VISIBLE);
            sign.setText("confirm");
        }
        super.onResume();
    }
}