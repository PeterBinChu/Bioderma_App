package fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bioderma.MainActivity;
import com.example.bioderma.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



import Objects.Product;

import Objects.User;

import static android.app.Activity.RESULT_OK;

public class DescriptionFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView productImage;
    EditText productName;
    EditText productPrice;
    EditText productCategory;
    EditText productDescription;
    EditText productQuantity;
    Spinner spinner1;
    ConstraintLayout constraintLayout;

    public boolean c1=true, c2=false;

    private boolean checkEdit=true, anotherCheckEdit=true;

    FirebaseDatabase database;
    DatabaseReference reference, reference1;
    Query query, query1;
    String a;
    String c;
    Boolean photoCheck=true;

    Button addProduct;
    Button editProduct;

    private SharedPreferences keySharedPreferences;
    private static final String KEY_SHARED_PREF = "keyshared";
    private static final String KEY_CHECKER = "key";

    private SharedPreferences storeSharedPreferences;
    private static final String STORE_SHARED_PREF = "storeshared";
    private static final String STORE_CHECKER = "store";

    private SharedPreferences btmDisablePreference;
    private static final String DISABLE_SHARED_PREF = "disableshared";
    private static final String DISABLE_CHECKER = "disable";

    private SharedPreferences emailSharedPreferences;
    private static final String EMAIL_SHARED_PREF = "emailshared";
    private static final String EMAIL_CHECKER = "email";

    StoreFragment storeFragment;

    String productName1, productName2;
    String productPriceString1;
    int productPrice1, productPrice2, index1;
    String productCategory1, productCategory2;
    String productDescription1, productDescription2;
    String productImage1;
    int productQuantity1, productQuantity2;
    String productKey1;
    Product product1;
    Boolean imgCheck=false, doneChecker=true;
    ShoppingCardFragment shoppingCardFragment1;
    private static int RESULT_LOAD_IMG = 1;
    private String products;

    String[] countries = { "Purify the skin", "Treat the skin", "Take care of your skin"};

    Boolean checker=true, checker1=true, admin=false;
    Bitmap decodedByte;

    public DescriptionFragment(){
    }

    public DescriptionFragment(String productName1, int productPrice1, String productCategory1, String productDescription1, String productImage1, int productQuantity1, Product product1, ShoppingCardFragment shoppingCardFragment1, String productKey1, int index1, StoreFragment storeFragment){
        this.productName1=productName1;
        this.productPrice1=productPrice1;
        this.productCategory1=productCategory1;
        this.productDescription1=productDescription1;
        this.productImage1=productImage1;
        this.product1=product1;
        this.shoppingCardFragment1=shoppingCardFragment1;
        this.productQuantity1=productQuantity1;
        this.productKey1=productKey1;
        this.index1=index1;
        this.storeFragment=storeFragment;
    }


    public static DescriptionFragment newInstance(String param1, String param2) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance("https://bioderma-d8248-default-rtdb.firebaseio.com/");
        reference = database.getReference().child("Bioderma Products");
        reference1=database.getReference().child("Users");
        keySharedPreferences=this.getActivity().getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
        storeSharedPreferences=this.getActivity().getSharedPreferences(STORE_SHARED_PREF, Context.MODE_PRIVATE);
        btmDisablePreference=this.getActivity().getSharedPreferences(DISABLE_SHARED_PREF, Context.MODE_PRIVATE);
        emailSharedPreferences=this.getActivity().getSharedPreferences(EMAIL_SHARED_PREF, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description,
                container, false);

        editProduct=(Button) view.findViewById(R.id.edit_product);
        editProduct.setOnClickListener(this);

        spinner1=(Spinner) view.findViewById(R.id.spinner1);
        String d=emailSharedPreferences.getString(EMAIL_CHECKER, "");
        if(d.equals("<YOUR_EMAIL_HERE>") || d.equals("<YOUR_EMAIL_HERE>")){
            admin=true;
            editProduct.setVisibility(View.VISIBLE);
        }

        String product2 = product1.getNativeKeyProduct();
        for (Product product : shoppingCardFragment1.getMCardProductList()) {
            String product3 = product.getNativeKeyProduct();
            if (product2.equals(product3)) {
                checker1 = false;
            }
        }
        constraintLayout=(ConstraintLayout) view.findViewById(R.id.mainDescriptionId);

        productName=(EditText) view.findViewById(R.id.product_name);
        productPrice=(EditText) view.findViewById(R.id.product_price);
        productCategory=(EditText) view.findViewById(R.id.product_category);
        productDescription=(EditText) view.findViewById(R.id.product_description);
        productQuantity=(EditText) view.findViewById(R.id.product_quantity);

        productImage=(ImageView) view.findViewById(R.id.product_image);
        productImage.setOnClickListener(this);

        addProduct=(Button) view.findViewById(R.id.add);
        addProduct.setOnClickListener(this);

        productName.setText(productName1);
        productPrice.setText(productPrice1 +"");
        productCategory.setText(productCategory1);
        productDescription.setText(productDescription1);
        productQuantity.setText(productQuantity1 +"");

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<>(getContext(), R.layout.custom_spinner_list_item, countries); // here dataInsideSpinner is a List<>
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                productCategory2=(String)parent.getItemAtPosition(pos);
                productCategory.setText((String)parent.getItemAtPosition(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        String key=keySharedPreferences.getString(KEY_CHECKER, "");
        if(key.matches("")){
            addProduct.setEnabled(false);
            addProduct.setTextColor(Color.parseColor("#FFD3D3D3"));
        }
        if(checker) {
            byte[] decodedString = Base64.decode(productImage1, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            checker=false;
        }

        if(productQuantity.getText().toString().matches("0")){
            addProduct.setText("Out of stock");
            addProduct.setEnabled(false);
        }
        ((MainActivity) getContext()).animation1.setDuration(500);
        constraintLayout.setVisibility(View.VISIBLE);
        ((MainActivity) getContext()).back1.setVisibility(View.VISIBLE);
        ((MainActivity) getContext()).frameLayout.setVisibility(View.VISIBLE);
        SharedPreferences.Editor disableEditor1=btmDisablePreference.edit();
        disableEditor1.putBoolean(DISABLE_CHECKER, true);
        disableEditor1.apply();
        productImage.setImageBitmap(decodedByte);
        SharedPreferences.Editor disableEditor=btmDisablePreference.edit();
        disableEditor.putBoolean(DISABLE_CHECKER, true);
        disableEditor.apply();
    return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) view.getContext()).frameLayout.setVisibility(View.VISIBLE);
        ((MainActivity) view.getContext()).progressBar_cyclic1.setVisibility(View.GONE);
        ((MainActivity) getContext()).back1.setAnimation(((MainActivity) getContext()).animation1);

        SharedPreferences.Editor disableEditor=btmDisablePreference.edit();
        disableEditor.putBoolean(DISABLE_CHECKER, true);
        disableEditor.apply();
    }

    public String getProductName1() {
        return productName1;
    }

    public void setProductName1(String productName1) {
        this.productName1 = productName1;
    }

    public String getProductPriceString1() {
        return productPriceString1;
    }

    public void setProductPriceString1(String productPrice1) {
        this.productPriceString1 = productPrice1;
    }

    public int getProductPrice1() {
        return productPrice1;
    }

    public void setProductPrice1(int productPrice1) {
        this.productPrice1 = productPrice1;
    }

    public String getProductCategory1() {
        return productCategory1;
    }

    public void setProductCategory1(String productCategory1) {
        this.productCategory1 = productCategory1;
    }

    public String getProductDescription1() {
        return productDescription1;
    }

    public void setProductDescription1(String productDescription1) {
        this.productDescription1 = productDescription1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_image:
                if(admin){
                    if(imgCheck) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                    }
                }
                break;
            case R.id.edit_product:
                if(checkEdit) {
                    spinner1.setVisibility(View.VISIBLE);
                    productCategory.setVisibility(View.GONE);
                    addProduct.setEnabled(false);
                    imgCheck=true;
                    productName.setEnabled(true);
                    productPrice.setEnabled(true);
                    productQuantity.setEnabled(true);
                    productDescription.setEnabled(true);
                    editProduct.setText("Confirm");
                    checkEdit=false;
                    anotherCheckEdit=false;
                }
                else{
                    if(productName.getText().toString().matches(productName1) && productPrice.getText().toString().matches(productPrice1 +"") && productQuantity.getText().toString().matches(productQuantity1+"") && productCategory.getText().toString().matches(productCategory1) && productDescription.getText().toString().matches(productDescription1) && photoCheck==true){
                        spinner1.setVisibility(View.GONE);
                        productCategory.setVisibility(View.VISIBLE);
                        addProduct.setEnabled(true);
                        anotherCheckEdit=true;
                        imgCheck=false;
                        productName.setEnabled(false);
                        productPrice.setEnabled(false);
                        productQuantity.setEnabled(false);
                        productCategory.setEnabled(false);
                        productDescription.setEnabled(false);
                        editProduct.setText("Edit Product");
                        checkEdit = true;
                    }
                    else{
                    if(productName.getText().toString().trim().matches("")){
                        productName.setError("Type product name");
                    }
                    else if(productPrice.getText().toString().trim().matches("")){
                        productPrice.setError("Type product price");
                    }
                    else if(productCategory.getText().toString().trim().matches("")){
                        productCategory.setError("Type product category");
                    }
                    else if(productQuantity.getText().toString().trim().matches("")){
                        productQuantity.setError("Type product quantity");
                    }
                    else if(productDescription.getText().toString().trim().matches("")){
                        productDescription.setError("Type product description");
                    }
                    else {
                        SharedPreferences.Editor disableEditor = btmDisablePreference.edit();
                        disableEditor.putBoolean(DISABLE_CHECKER, false);
                        disableEditor.apply();
                        c1 = false;
                        doneChecker = false;
                        productName2 = productName.getText().toString();
                        productPrice2 = Integer.parseInt(productPrice.getText().toString());
                        productQuantity2 = Integer.parseInt(productQuantity.getText().toString());
                        productDescription2 = productDescription.getText().toString();
                        query = reference.orderByChild(productKey1);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot single : snapshot.getChildren()) {
                                    a = single.getValue(Product.class).getNativeKeyProduct();
                                    if (productKey1.equals(a)) {
                                        product1 = single.getValue(Product.class);
                                        single.getRef().child("nameProduct").setValue(productName2);
                                        single.getRef().child("price").setValue(productPrice2);
                                        single.getRef().child("priceString").setValue(productPrice2 + "тг ");
                                        single.getRef().child("category").setValue(productCategory2);
                                        single.getRef().child("quantity").setValue(productQuantity2);
                                        single.getRef().child("description").setValue(productDescription2);
                                        single.getRef().child("image").setValue(productImage1);

                                        product1.setNameProduct(productName2);
                                        product1.setPrice(productPrice2);
                                        product1.setCategory(productCategory2);
                                        product1.setDescription(productDescription2);
                                        product1.setQuantity(productQuantity2);
                                        product1.setImage(productImage1);

                                        ArrayList<Product> arrayList = new ArrayList();
                                        arrayList = storeFragment.getMlist();
                                        arrayList.set(index1, product1);
                                        storeFragment.setMlist(arrayList);
                                        break;
                                    }
                                }
                                Snackbar snackbar = Snackbar.make(getView(), "Changes saved", Snackbar.LENGTH_LONG);
                                snackbar.setAnchorView(((MainActivity) getActivity()).btNavView);
                                snackbar.show();
                                ((MainActivity) getActivity()).setFragment(((MainActivity) getActivity()).storeFragment);
                                ((MainActivity) getActivity()).btNavView.setSelectedItemId(R.id.store);
                                SharedPreferences.Editor editor = storeSharedPreferences.edit();
                                editor.putInt(STORE_CHECKER, 1);
                                editor.apply();
                                SharedPreferences.Editor disableEditor = btmDisablePreference.edit();
                                disableEditor.putBoolean(DISABLE_CHECKER, true);
                                disableEditor.apply();
                                doneChecker = true;
                                addProduct.setEnabled(true);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });


                        spinner1.setVisibility(View.GONE);
                        productCategory.setVisibility(View.VISIBLE);
                        addProduct.setEnabled(false);
                        anotherCheckEdit = true;

                        productName.setEnabled(false);
                        productPrice.setEnabled(false);
                        productQuantity.setEnabled(false);
                        productCategory.setEnabled(false);
                        productDescription.setEnabled(false);
                        editProduct.setText("Edit Product");
                        checkEdit = true;
                    }
                    }
                }
                break;

            case R.id.add:
                ((MainActivity)getActivity()).animation1.setDuration(0);
                query = reference.orderByChild(productKey1);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot single : snapshot.getChildren()) {
                            a = single.getValue(Product.class).getNativeKeyProduct();
                            if (productKey1.equals(a)) {
                                c = single.getValue(Product.class).getNameProduct();
                                if (c.matches(productName2)) {
                                    anotherCheckEdit = true;
                                }
                            }
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                if(anotherCheckEdit) {
                    productName2 = productName.getText().toString();
                    productPrice2 = Integer.parseInt(productPrice.getText().toString());
                    productCategory2 = productCategory.getText().toString();
                    productQuantity2 = Integer.parseInt(productQuantity.getText().toString());
                    productDescription2 = productDescription.getText().toString();
                    if (checker1) {
                        if (doneChecker) {
                            if (productQuantity2 == 0) {
                                ((MainActivity) getContext()).back1.setVisibility(View.INVISIBLE);
                                Snackbar snackbar = Snackbar.make(getView(),"Out of stock, sorry :(",Snackbar.LENGTH_LONG);
                                snackbar.setAnchorView(((MainActivity)getActivity()).btNavView);
                                snackbar.show();
                                ((MainActivity) getActivity()).setFragment(((MainActivity) getActivity()).storeFragment);
                                ((MainActivity) getActivity()).btNavView.setSelectedItemId(R.id.store);
                                SharedPreferences.Editor editor = storeSharedPreferences.edit();
                                editor.putInt(STORE_CHECKER, 1);
                                editor.apply();
                            }
                            else {
                                String key3=keySharedPreferences.getString(KEY_CHECKER, "");
                                query1 = reference1.orderByChild(key3);
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot single : snapshot.getChildren()) {
                                            a = single.getValue(User.class).getKey();
                                            if (key3.equals(a)) {
                                                products=single.getValue(User.class).getProductKeys();
                                                ((MainActivity) getActivity()).shoppingCardFragment.clearMCardList();
                                                ((MainActivity)getActivity()).back1.setVisibility(View.GONE);
                                                if(products==null || products.matches("")){
                                                    products=productKey1 + " ";
                                                    single.getRef().child("productKeys").setValue(products);
                                                }
                                                else {
                                                    products+=productKey1 + " ";
                                                    single.getRef().child("productKeys").setValue(products);
                                                }
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                ArrayList<Product> arrayList = new ArrayList();
                                arrayList = storeFragment.getMlist();
                                arrayList.set(index1, product1);
                                storeFragment.setMlist(arrayList);
                                shoppingCardFragment1.setmCardProductList(product1);
                                ((MainActivity) getActivity()).setFragment(((MainActivity) getActivity()).storeFragment);
                                ((MainActivity) getActivity()).btNavView.setSelectedItemId(R.id.store);
                                SharedPreferences.Editor editor = storeSharedPreferences.edit();
                                editor.putInt(STORE_CHECKER, 1);
                                editor.apply();
                            }
                        }
                        break;
                    }
                    else{
                        Snackbar snackbar = Snackbar.make(getView(),"This product is already added",Snackbar.LENGTH_LONG);
                        snackbar.setAnchorView(((MainActivity)getActivity()).btNavView);
                        snackbar.show();
                    }
                }
                else{
                    Snackbar snackbar = Snackbar.make(getView(),"Changes are being saved, please wait",Snackbar.LENGTH_LONG);
                    snackbar.setAnchorView(((MainActivity)getActivity()).btNavView);
                    snackbar.show();
                }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                photoCheck=false;
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
        productImage1=encodedString;
        System.out.println(encodedString);

        byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


        productImage.setImageBitmap(decodedByte);
    }
}