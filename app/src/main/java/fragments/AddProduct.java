package fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import android.widget.Toast;

import com.example.bioderma.MainActivity;
import com.example.bioderma.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Objects.Product;

import static android.app.Activity.RESULT_OK;

public class AddProduct extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView adminImage;
    private EditText adminName;
    private EditText adminPrice;
    private EditText adminQuantity;
    private EditText adminDescription;
    private AppCompatButton adminAddButton;
    private static int RESULT_LOAD_IMG = 1;
    private boolean againChecker=false;
    StoreFragment storeFragment;

    private SharedPreferences btmDisablePreference;
    private static final String DISABLE_SHARED_PREF = "disableshared";
    private static final String DISABLE_CHECKER = "disable";

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String CHECKER = "checker";

    String adminName1, adminCategory1, adminDescription1, adminImage1;
    int adminPrice1, adminQuantity1;
    FirebaseDatabase database;
    DatabaseReference reference;

    Query query;

    String[] countries = { "Purify the skin", "Treat the skin", "Take care of your skin"};

    public AddProduct() {
        // Required empty public constructor
    }

    public AddProduct(StoreFragment storeFragment){
        this.storeFragment=storeFragment;
    }
    public static AddProduct newInstance(String param1, String param2) {
        AddProduct fragment = new AddProduct();
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
        sharedPreferences=this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        btmDisablePreference=this.getActivity().getSharedPreferences(DISABLE_SHARED_PREF, Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance("https://bioderma-d8248-default-rtdb.firebaseio.com/");
        reference = database.getReference().child("Bioderma Products");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product,
                container, false);
        adminImage=(ImageView) view.findViewById(R.id.admin_product_image);
        adminImage.setOnClickListener(this);

        adminName=(EditText) view.findViewById(R.id.admin_product_name);
        adminPrice=(EditText) view.findViewById(R.id.admin_product_price);
        adminQuantity=(EditText) view.findViewById(R.id.admin_product_quantity);
        adminDescription=(EditText) view.findViewById(R.id.admin_product_description);
        adminAddButton=(AppCompatButton) view.findViewById(R.id.admin_add_button);
        adminAddButton.setOnClickListener(this);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<>(getContext(), R.layout.custom_spinner_list_item, countries); // here dataInsideSpinner is a List<>
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                adminCategory1=(String)parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        ((MainActivity)getActivity()).frameLayout.setVisibility(View.VISIBLE);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                Base64Encoder(imageStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }


    public void Base64Encoder(InputStream inputStream1){
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            while ((bytesRead = inputStream1.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
        adminImage1=encodedString;
        System.out.println(encodedString);

        byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


        adminImage.setImageBitmap(decodedByte);
        againChecker=true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_product_image:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                break;

            case R.id.admin_add_button:
                adminName1=adminName.getText().toString();
                if(adminPrice.length()>=1) {
                    adminPrice1 = Integer.parseInt(adminPrice.getText().toString());
                }
                else{
                    adminPrice.setError("Type product price");
                }
                if(adminQuantity.length()>=1) {
                    adminQuantity1=Integer.parseInt(adminQuantity.getText().toString());
                }
                else{
                    adminQuantity.setError("Type product quantity");
                }
                adminDescription1=adminDescription.getText().toString();
                if(adminName1.trim().matches("")){
                    adminName.setError("Type product name");
                }
                else if(adminPrice1<=0 || String.valueOf(adminPrice1)==""){
                    adminPrice.setError("Type product price");
                }
                else if(adminQuantity1<=0 || String.valueOf(adminQuantity1)==""){
                    adminQuantity.setError("Type product quantity");
                }
                else if(adminDescription1.trim().matches("")){
                    adminDescription.setError("Type product description");
                }
                else if(againChecker==false){
                    Snackbar.make(v, "Choose product image", BaseTransientBottomBar.LENGTH_LONG).show();
                }
                else {
                    Product product;

                    product = new Product(adminName1, adminCategory1, adminDescription1, "", adminPrice1, adminImage1, adminQuantity1, false, 1);
                    DatabaseReference push = reference.push();
                    product.setNativeKeyProduct(push.getKey());
                    push.setValue(product);

                    ArrayList<Product> arrayList = new ArrayList();
                    arrayList = storeFragment.getMlist();
                    arrayList.add(product);

                    storeFragment.setMlist(arrayList);
                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.putInt(CHECKER, 2);
                    editor1.apply();

                    ((MainActivity)getActivity()).back1.setVisibility(View.GONE);
                    ((MainActivity)getActivity()).back.setVisibility(View.GONE);
                    ((MainActivity)getActivity()).btNavView.setSelectedItemId(R.id.store);
                    ((MainActivity)getActivity()).reload.setVisibility(View.INVISIBLE);
                    ((MainActivity)getActivity()).setFragment(((MainActivity) getActivity()).storeFragment);
                }

                break;

        }
    }
}