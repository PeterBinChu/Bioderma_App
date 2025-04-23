package fragments;

import android.content.Context;

import android.content.SharedPreferences;
import android.media.AudioManager;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;

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


public class NewUser extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button confirm;
    private TextView emailTextView1;
    private boolean c=true, c1=true, code=true, check=false;
    private ImageView newEye, newEye1, newAgain;
    private EditText pass, pass1, email, newPassCode, nickname;
    private String checkPass, checkPass1, checkEmail, newPassCode1, checkNickname;
    private String passwordCheck = "";
    boolean findEmailChecker2=false, findEmailChecker3=true;
    private String emailForDatabase1;
    private String a1, key1;
    private MediaPlayer mp;
    private MediaPlayer mp1;
    AudioManager audioManager;
    Context context;

    FirebaseDatabase database;
    DatabaseReference reference;
    Query query3;


    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String CHECKER = "checker";

    private SharedPreferences sharedPreferences1;
    private static final String SHARED_PREF_NAME1 = "mypref1";
    private static final String CHECKER1 = "checker1";

    private SharedPreferences emailSharedPreferences;
    private static final String EMAIL_SHARED_PREF = "emailshared";
    private static final String EMAIL_CHECKER = "email";

    private SharedPreferences keySharedPreferences;
    private static final String KEY_SHARED_PREF = "keyshared";
    private static final String KEY_CHECKER = "key";


    public NewUser() {

    }




    public static NewUser newInstance(String param1, String param2) {
        NewUser fragment = new NewUser();
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
        sharedPreferences1=this.getActivity().getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
        keySharedPreferences=this.getActivity().getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
        emailSharedPreferences=this.getActivity().getSharedPreferences(EMAIL_SHARED_PREF, Context.MODE_PRIVATE);
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
        View view = inflater.inflate(R.layout.fragment_new,
                container, false);
        confirm=(Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        newEye=(ImageView) view.findViewById(R.id.newEye);
        newEye.setOnClickListener(this);
        newEye1=(ImageView) view.findViewById(R.id.newEye1);
        newEye1.setOnClickListener(this);
        newAgain=(ImageView) view.findViewById(R.id.newAgain);
        newAgain.setOnClickListener(this);

        emailTextView1=(TextView) view.findViewById(R.id.emailTextView1);
        nickname=(EditText) view.findViewById(R.id.nickname);
        pass=(EditText) view.findViewById(R.id.newPassword);
        pass1=(EditText) view.findViewById(R.id.newPassword1);
        email=(EditText) view.findViewById(R.id.newEmail);
        newPassCode=(EditText) view.findViewById(R.id.emailPassCode);

        setHasOptionsMenu(true);
        ((MainActivity) getContext()).frameLayout.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newAgain:
                checkEmail=email.getText().toString();
                Snackbar.make(getView(), "New password sent", Snackbar.LENGTH_LONG ).show();
                passwordCheck = autoPassword();
                autoSendEmail(passwordCheck, checkEmail);
                break;
            case R.id.newEye:
                if(c==true) {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newEye.setImageResource(R.drawable.eye_close);
                    c=false;
                }
                else{
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newEye.setImageResource(R.drawable.eye_open);
                    c=true;
                }
                break;
            case R.id.newEye1:
                if(c1==true) {
                    pass1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newEye1.setImageResource(R.drawable.eye_close);
                    c1=false;
                }
                else{
                    pass1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newEye1.setImageResource(R.drawable.eye_open);
                    c1=true;
                }
                break;
            case R.id.confirm:
                checkNickname=nickname.getText().toString();
                checkEmail=email.getText().toString();
                checkPass=pass.getText().toString();
                checkPass1=pass1.getText().toString();
                newPassCode1=newPassCode.getText().toString();
                if(checkNickname.trim().matches("")){
                nickname.setError("Enter your nickname");
                }
                else if(checkEmail.trim().matches("")){
                    email.setError("Enter your email address");
                }
                else if(checkPass.trim().matches("")){
                    pass.setError("Enter your password");
                }
                else if(checkPass1.trim().matches("")){
                    pass1.setError("Enter your password");
                }
                else if(checkEmail!="" || checkPass!="" || checkPass1!="" || checkNickname!="") {
                        if (checkPass.matches(checkPass1)) {
                            if (!isEmailValid(checkEmail)) {
                                email.setError("Your Email Id is Invalid.");
                            } else {
                                if (findEmailChecker2 == false) {
                                    emailForDatabase1 = checkEmail.replace(".", "");
                                    query3 = reference.orderByChild(emailForDatabase1);
                                    query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot single : snapshot.getChildren()) {
                                                a1 = single.getValue(User.class).getEmail();
                                                if (checkEmail.equals(a1)) {
                                                    key1 = single.getValue(User.class).getKey();
                                                    findEmailChecker3 = false;
                                                    break;
                                                }
                                            }
                                            findEmailChecker2 = true;
                                            v.performClick();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                } else {
                                    if (findEmailChecker3) {
                                        if (code == true) {
                                            check = true;
                                            email.setEnabled(false);
                                            pass.setEnabled(false);
                                            pass1.setEnabled(false);
                                            newAgain.setVisibility(View.VISIBLE);
                                            newPassCode.setVisibility(View.VISIBLE);
                                            emailTextView1.setVisibility(View.VISIBLE);
                                            confirm.setText("confirm e-mail");
                                            passwordCheck = autoPassword();
                                            autoSendEmail(passwordCheck, checkEmail);
                                            code = false;
                                        } else {
                                            newPassCode1 = newPassCode.getText().toString();
                                            if (newPassCode1.contains(passwordCheck)) {
                                                mp1.start();
                                                code = true;
                                                passwordCheck = "";
                                                User user;
                                                if(checkEmail.equals("<YOUR_EMAIL_HERE>") || checkEmail.equals("<YOUR_EMAIL_HERE>")) {
                                                    user = new User(checkEmail, checkPass, checkNickname, "", "", "", "", "", "", "", "", true, "");
                                                }
                                                else{
                                                    user = new User(checkEmail, checkPass, checkNickname, "", "", "", "", "", "", "", "", false, "");
                                                }

                                                DatabaseReference push = reference.push();
                                                user.setKey(push.getKey());
                                                push.setValue(user);

                                                ((MainActivity) getActivity()).shoppingCardFragment.check=true;
                                                SharedPreferences.Editor editor1=emailSharedPreferences.edit();
                                                editor1.putString(EMAIL_CHECKER, checkEmail);
                                                editor1.apply();

                                                SharedPreferences.Editor keyEditor=keySharedPreferences.edit();
                                                keyEditor.putString(KEY_CHECKER, push.getKey());
                                                keyEditor.apply();

                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putInt(CHECKER, 2);
                                                editor.apply();

                                                ((MainActivity) getActivity()).forShop();
                                            } else {
                                                newPassCode.setError("Wrong password");
                                            }
                                        }
                                    } else {
                                        findEmailChecker3 = true;
                                        findEmailChecker2 = false;
                                        email.setError("This e-mail is already exists");
                                    }
                                }

                            }
                        } else {
                            pass1.setError("Password are different");
                        }
                    }
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
    public void onResume() {
        if(check){
            email.setEnabled(false);
            pass.setEnabled(false);
            pass1.setEnabled(false);
            newAgain.setVisibility(View.VISIBLE);
            newPassCode.setVisibility(View.VISIBLE);
            confirm.setText("confirm e-mail");
        }
        super.onResume();
    }

}