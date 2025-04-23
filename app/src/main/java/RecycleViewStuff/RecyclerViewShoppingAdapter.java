package RecycleViewStuff;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bioderma.MainActivity;
import com.example.bioderma.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import GMailSenders.GMailSenderProduct;
import Objects.Product;
import Objects.User;
import fragments.StoreFragment;


public class RecyclerViewShoppingAdapter extends RecyclerView.Adapter<RecyclerViewShoppingAdapter.MyShoppingViewHolder>{
    private Context context;
    private List<Product> productList, productListFull;
    FirebaseDatabase database;
    DatabaseReference reference, reference1;
    Query query;
    String a, b;
    StoreFragment storeFragment;
    public String keys;
    public String productKey1;
    private String products;
    private int price2;
    View view2;
    Product product1;
    private boolean h=false, d=false;
    private SharedPreferences keySharedPreferences;
    private static final String KEY_SHARED_PREF = "keyshared";
    private static final String KEY_CHECKER = "key";





    public RecyclerViewShoppingAdapter(Context context, List<Product> productList, StoreFragment storeFragment1) {
        this.context = context;
        this.productList = productList;
        storeFragment=storeFragment1;
        productListFull=new ArrayList<>();
        if(storeFragment != null) {
            productListFull = storeFragment.getMlist();
        }
    }

    @NonNull
    @Override
    public RecyclerViewShoppingAdapter.MyShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.shopping_cardview_item,parent,false);
        view2=view;
        return new RecyclerViewShoppingAdapter.MyShoppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerViewShoppingAdapter.MyShoppingViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance("https://bioderma-d8248-default-rtdb.firebaseio.com/");
        reference = database.getReference().child("Users");
        reference1=database.getReference().child("Bioderma Products");
        keySharedPreferences=context.getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
        holder.price.setText(productList.get(position).getPriceString());
        holder.product_title.setText(productList.get(position).getNameProduct());
        holder.delete.setEnabled(true);
        if(productList.get(holder.getAdapterPosition()).getPersonQuantity()==true){
            holder.quantity.setText(productList.get(position).getPersonQuantityInt() +"");
        }
        else if(productList.get(holder.getAdapterPosition()).getPersonQuantity()==false){
            holder.quantity.setText("1");
        }
        byte[] decodedString = Base64.decode(productList.get(position).getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.img_product_category.setImageBitmap(decodedByte);
        holder.possible_amount.setText("Quantity: " + productList.get(position).getQuantity());
        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s2 = s.toString().trim();

                if(s2.startsWith("0") || s2.startsWith("-") || s2.startsWith("+"))
                    s2 = "0";

                if(s2.equals("0") || s2.startsWith("-")){
                    holder.quantity.setText("1");
                    s2="1";
                }
                if(s2.equals("") || s2.equals("-") || s2.equals("+") || s2.equals(".") || s2.equals(","))
                    s2="0";
                if (Integer.parseInt(s2) > productList.get(holder.getAdapterPosition()).getQuantity()) {
                    holder.quantity.setText(String.valueOf(productList.get(holder.getAdapterPosition()).getQuantity()));
                    s2=String.valueOf(productList.get(holder.getAdapterPosition()).getQuantity());
                }
                productList.get(holder.getAdapterPosition()).setPersonQuantity(true);
                productList.get(holder.getAdapterPosition()).setPersonQuantityInt(Integer.parseInt(s2));
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keys=keySharedPreferences.getString(KEY_CHECKER, "");
                query = reference.orderByChild(keys);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot single : snapshot.getChildren()) {
                            a = single.getValue(User.class).getKey();
                            if (keys.equals(a)) {
                                products=single.getValue(User.class).getProductKeys();
                                if(products.contains(productList.get(holder.getAdapterPosition()).getNativeKeyProduct())){
                                    holder.delete.setEnabled(false);
                                    b = products.replace (productList.get(holder.getAdapterPosition()).getNativeKeyProduct() +" ", "");
                                    single.getRef().child("productKeys").setValue(b);
                                    ((MainActivity) context).shoppingCardFragment.clearMCardList();
                                    for(Product product : productList){
                                        if(product.getNativeKeyProduct().equals(productList.get(holder.getAdapterPosition()).getNativeKeyProduct())){
                                            product.setShoppingCardExist(false);
                                        }
                                    }
                                    productList.get(holder.getAdapterPosition()).setPersonQuantity(true);
                                    productList.get(holder.getAdapterPosition()).setPersonQuantityInt(1);
                                    storeFragment.setMlist((ArrayList) productListFull);
                                    productList.remove(holder.getAdapterPosition());

                                    notifyItemRemoved(holder.getAdapterPosition());
                                }

                                break;
                            }
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (holder.quantity.getText().toString().equals("") || holder.quantity.getText().toString().equals("0")) {
                        //holder.quantity.setText("0");
                        //holder.quantity.setSelection(holder.quantity.getText().toString().length());
                        productList.get(holder.getAdapterPosition()).setPersonQuantity(true);
                        productList.get(holder.getAdapterPosition()).setPersonQuantityInt(1);
                        holder.quantity.setText(productList.get(holder.getAdapterPosition()).getPersonQuantityInt() +"");
                        return;
                    }
                    if (Integer.parseInt(holder.quantity.getText().toString()) < productList.get(holder.getAdapterPosition()).getQuantity()) {
                        productList.get(holder.getAdapterPosition()).setPersonQuantity(true);
                        if(productList.get(holder.getAdapterPosition()).getPersonQuantityInt()==0){
                            productList.get(holder.getAdapterPosition()).setPersonQuantityInt(2);
                        }
                        else {
                            productList.get(holder.getAdapterPosition()).setPersonQuantityInt(productList.get(holder.getAdapterPosition()).getPersonQuantityInt() + 1);
                        }
                        holder.quantity.setText(productList.get(holder.getAdapterPosition()).getPersonQuantityInt() +"");
                        //holder.quantity.setText((Integer.parseInt(holder.quantity.getText().toString()) + 1) + "");
                    }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (holder.quantity.getText().toString().equals("") || holder.quantity.getText().toString().equals("0")) {
                        //holder.quantity.setText("2");
                        //holder.quantity.setSelection(holder.quantity.getText().toString().length());
                        productList.get(holder.getAdapterPosition()).setPersonQuantity(true);
                        productList.get(holder.getAdapterPosition()).setPersonQuantityInt(1);
                        holder.quantity.setText(productList.get(holder.getAdapterPosition()).getPersonQuantityInt() +"");
                    }
                    if (Integer.parseInt(holder.quantity.getText().toString()) > 1) {
                        productList.get(holder.getAdapterPosition()).setPersonQuantity(true);
                        productList.get(holder.getAdapterPosition()).setPersonQuantityInt(productList.get(holder.getAdapterPosition()).getPersonQuantityInt()-1);
                        holder.quantity.setText(productList.get(holder.getAdapterPosition()).getPersonQuantityInt() +"");
                        //holder.quantity.setText((Integer.parseInt(holder.quantity.getText().toString()) - 1) + "");
                    }

            }
        });

        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.quantity.getText().toString().equals("")) {
                    holder.quantity.setText("1");
                }
                holder.buy.setEnabled(false);
                holder.delete.setEnabled(false);
                holder.plus.setEnabled(false);
                holder.minus.setEnabled(false);
                holder.quantity.setEnabled(false);
                ViewGroup viewGroup = holder.itemView.findViewById(android.R.id.content);
                TextView textViewCancel, finalQuantity, product_text_id1, price1, possible_amount1, product_category_buy, product_description1;
                CircularImageView image_buy;

                Button buttonBuy;
                EditText firstName, lastName, email, street, county, city, phoneNumber;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.dialog,viewGroup,false);
                builder.setCancelable(false);
                builder.setView(view);

                textViewCancel = view.findViewById(R.id.text_cancel);
                buttonBuy = view.findViewById(R.id.buy);

                email = view.findViewById(R.id.email_buy);
                firstName = view.findViewById(R.id.first_name_buy);
                lastName = view.findViewById(R.id.last_name_buy);
                street = view.findViewById(R.id.street_buy);
                city = view.findViewById(R.id.city_buy);
                county = view.findViewById(R.id.country_buy);
                phoneNumber = view.findViewById(R.id.phone_buy);
                finalQuantity=view.findViewById(R.id.final_quantity);
                finalQuantity.setText(holder.quantity.getText());
                product_text_id1=view.findViewById(R.id.product_text_id1);
                price1=view.findViewById(R.id.price1);
                possible_amount1=view.findViewById(R.id.possible_amount1);
                product_category_buy=view.findViewById(R.id.product_category_buy);
                product_description1=view.findViewById(R.id.product_description1);
                image_buy=view.findViewById(R.id.image_buy);

                productKey1=productList.get(holder.getAdapterPosition()).getNativeKeyProduct();
                query = reference1.orderByChild(productKey1);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot single : snapshot.getChildren()) {
                            a = single.getValue(Product.class).getNativeKeyProduct();
                            if (productKey1.equals(a)) {
                                product_text_id1.setText(single.getValue(Product.class).getNameProduct());
                                price1.setText(single.getValue(Product.class).getPrice() +" тг");
                                price2=single.getValue(Product.class).getPrice();
                                possible_amount1.setText(single.getValue(Product.class).getQuantity() +"");
                                product_category_buy.setText(single.getValue(Product.class).getCategory());
                                product_description1.setText(single.getValue(Product.class).getDescription());
                                byte[] decodedString = Base64.decode(single.getValue(Product.class).getImage(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                image_buy.setImageBitmap(decodedByte);
                                break;
                            }
                        }
                        d=true;
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                keys=keySharedPreferences.getString(KEY_CHECKER, "");
                query = reference.orderByChild(keys);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot single : snapshot.getChildren()) {
                            a = single.getValue(User.class).getKey();
                            if (keys.equals(a)) {
                                email.setText(single.getValue(User.class).getEmail());
                                firstName.setText(single.getValue(User.class).getFirstName());
                                lastName.setText(single.getValue(User.class).getSecondName());
                                phoneNumber.setText(single.getValue(User.class).getPhoneNumber());
                                county.setText(single.getValue(User.class).getCountry());
                                city.setText(single.getValue(User.class).getCity());
                                street.setText(single.getValue(User.class).getStreet());
                                break;
                            }
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                AlertDialog alertDialog = builder.create();

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                buttonBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(email.getText().toString().trim().equals("")) {
                            email.setError("Error");
                        }
                        else if(firstName.getText().toString().trim().equals("")) {
                            firstName.setError("Error");
                        }
                        else if(lastName.getText().toString().trim().equals("")) {
                            lastName.setError("Error");
                        }
                        else if(street.getText().toString().trim().equals("")) {
                            street.setError("Error");
                        }
                        else if(city.getText().toString().trim().equals("")) {
                            city.setError("Error");
                        }
                        else if(county.getText().toString().trim().equals("")) {
                            county.setError("Error");
                        }
                        else if(phoneNumber.getText().toString().trim().equals("")) {
                            phoneNumber.setError("Error");
                        }
                        else{
                            if (phoneNumber.getText().toString().indexOf("+") != 0) {
                                phoneNumber.setError("+");
                            }
                            else {
                                if(d) {
                                    if(possible_amount1.getText().toString().matches("")){

                                    }
                                    else {
                                        if (Integer.parseInt(possible_amount1.getText().toString()) - Integer.parseInt(finalQuantity.getText().toString()) >= 0) {
                                            Snackbar snackbar = Snackbar.make(view, "Success! We will type you soon!", Snackbar.LENGTH_LONG);
                                            snackbar.setAnchorView(((MainActivity) context).btNavView);
                                            snackbar.show();
                                            d=false;
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        GMailSenderProduct sender = new GMailSenderProduct("<YOUR_EMAIL_HERE>",
                                                                "<YOUR_PASSWORD_HERE>");
                                                        sender.sendMail("Product purchased", "",
                                                                "<YOUR_EMAIL_HERE>", email.getText().toString(), product_text_id1.getText().toString(), price2,
                                                                Integer.parseInt(possible_amount1.getText().toString()),
                                                                firstName.getText().toString(), lastName.getText().toString(),
                                                                phoneNumber.getText().toString(),
                                                                county.getText().toString(),
                                                                city.getText().toString(),
                                                                street.getText().toString(),
                                                                finalQuantity.getText().toString());
                                                    } catch (Exception e) {
                                                        Log.e("SendMail", e.getMessage(), e);
                                                    }
                                                }

                                            }).start();
                                            productKey1=productList.get(holder.getAdapterPosition()).getNativeKeyProduct();
                                            query = reference1.orderByChild(productKey1);
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot single : snapshot.getChildren()) {
                                                        a = single.getValue(Product.class).getNativeKeyProduct();
                                                        if (productKey1.equals(a)) {
                                                            product1=single.getValue(Product.class);
                                                            product1.setQuantity(Integer.parseInt(possible_amount1.getText().toString()) - Integer.parseInt(finalQuantity.getText().toString()));
                                                            holder.possible_amount.setText("Quantity: " + product1.getQuantity());
                                                            productList.get(holder.getAdapterPosition()).setQuantity(product1.getQuantity());
                                                            single.getRef().child("quantity").setValue(Integer.parseInt(possible_amount1.getText().toString()) - Integer.parseInt(finalQuantity.getText().toString()));
                                                            break;
                                                        }
                                                    }
                                                    d=true;
                                                    if(Integer.parseInt(possible_amount1.getText().toString()) - Integer.parseInt(finalQuantity.getText().toString())==0){
                                                            keys=keySharedPreferences.getString(KEY_CHECKER, "");
                                                        query = reference.orderByChild(keys);
                                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for (DataSnapshot single : snapshot.getChildren()) {
                                                                    a = single.getValue(User.class).getKey();
                                                                    if (keys.equals(a)) {
                                                                        products=single.getValue(User.class).getProductKeys();
                                                                        if(products.contains(productList.get(holder.getAdapterPosition()).getNativeKeyProduct())){
                                                                            holder.delete.setEnabled(false);
                                                                            b = products.replace (productList.get(holder.getAdapterPosition()).getNativeKeyProduct() +" ", "");
                                                                            single.getRef().child("productKeys").setValue(b);
                                                                            ((MainActivity) context).shoppingCardFragment.clearMCardList();
                                                                            for(Product product : productList){
                                                                                if(product.getNativeKeyProduct().equals(productList.get(holder.getAdapterPosition()).getNativeKeyProduct())){
                                                                                    product.setShoppingCardExist(false);
                                                                                }
                                                                            }
                                                                            productList.get(holder.getAdapterPosition()).setPersonQuantity(true);
                                                                            productList.get(holder.getAdapterPosition()).setPersonQuantityInt(1);
                                                                            storeFragment.setMlist((ArrayList) productListFull);
                                                                            productList.remove(holder.getAdapterPosition());

                                                                            notifyItemRemoved(holder.getAdapterPosition());
                                                                        }

                                                                        break;
                                                                    }
                                                                }

                                                            }


                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }

                                                        });
                                                    }
                                                    else {
                                                        holder.quantity.setText("1");
                                                        holder.buy.setEnabled(true);
                                                        holder.delete.setEnabled(true);
                                                        holder.plus.setEnabled(true);
                                                        holder.minus.setEnabled(true);
                                                        holder.quantity.setEnabled(true);
                                                    }
                                                    alertDialog.dismiss();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }

                                            });
                                        }
                                        else {
                                            query = reference.orderByChild(keys);
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot single : snapshot.getChildren()) {
                                                        a = single.getValue(User.class).getKey();
                                                        if (keys.equals(a)) {
                                                            products=single.getValue(User.class).getProductKeys();
                                                            if(products.contains(productList.get(holder.getAdapterPosition()).getNativeKeyProduct())){
                                                                holder.delete.setEnabled(false);
                                                                b = products.replace (productList.get(holder.getAdapterPosition()).getNativeKeyProduct() +" ", "");
                                                                single.getRef().child("productKeys").setValue(b);
                                                                ((MainActivity) context).shoppingCardFragment.clearMCardList();
                                                                for(Product product : productList){
                                                                    if(product.getNativeKeyProduct().equals(productList.get(holder.getAdapterPosition()).getNativeKeyProduct())){
                                                                        product.setShoppingCardExist(false);
                                                                    }
                                                                }
                                                                productList.get(holder.getAdapterPosition()).setPersonQuantity(true);
                                                                productList.get(holder.getAdapterPosition()).setPersonQuantityInt(1);
                                                                storeFragment.setMlist((ArrayList) productListFull);
                                                                productList.remove(holder.getAdapterPosition());

                                                                notifyItemRemoved(holder.getAdapterPosition());
                                                            }

                                                            break;
                                                        }
                                                    }

                                                }


                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }

                                            });
                                            d=true;
                                            holder.buy.setEnabled(true);
                                            holder.delete.setEnabled(true);
                                            holder.plus.setEnabled(true);
                                            holder.minus.setEnabled(true);
                                            holder.quantity.setEnabled(true);
                                            alertDialog.dismiss();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });



                textViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(d) {
                            holder.buy.setEnabled(true);
                            holder.delete.setEnabled(true);
                            holder.plus.setEnabled(true);
                            holder.minus.setEnabled(true);
                            holder.quantity.setEnabled(true);
                            alertDialog.dismiss();
                        }
                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    public static class MyShoppingViewHolder extends RecyclerView.ViewHolder {

        TextView product_title, possible_amount;
        ImageView img_product_category;
        TextView price;
        ImageView delete, plus, minus;
        EditText quantity;
        AppCompatButton buy;
        public MyShoppingViewHolder(@NonNull View itemView) {
            super(itemView);
            product_title = itemView.findViewById(R.id.product_text_id);
            img_product_category = itemView.findViewById(R.id.img_id);
            price = itemView.findViewById(R.id.price);
            buy=(AppCompatButton) itemView.findViewById(R.id.card_button);
            delete=(ImageView) itemView.findViewById(R.id.delete);
            quantity=(EditText) itemView.findViewById(R.id.quantity_text);
            plus=(ImageView) itemView.findViewById(R.id.plus);
            minus=(ImageView) itemView.findViewById(R.id.minus);
            possible_amount=(TextView) itemView.findViewById(R.id.possible_amount);
        }
    }
}