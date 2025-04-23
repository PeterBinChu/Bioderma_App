package helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bioderma.R;

public class Checker extends AppCompatActivity {
    private boolean c;
    private Button reg, sign;
    private TextView notReg, here;
    private ImageView eye, face;
    private EditText pass, email;
    public SharedPreferences mSettings; // сохранялка
    public final String APP_PREFERENCES = "mysettings"; // файл сохранялки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void profile(boolean check, View view){

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        reg = (Button) view.findViewById(R.id.reg);
        sign=(Button) view.findViewById(R.id.sign);
        eye= (ImageView) view.findViewById(R.id.eye);
        pass=(EditText) view.findViewById(R.id.password);
        email=(EditText) view.findViewById(R.id.email);
        notReg=(TextView) view.findViewById(R.id.notReg);
        face=(ImageView) view.findViewById(R.id.face);
        //here=(TextView) view.findViewById(R.id.here);
    }




}

