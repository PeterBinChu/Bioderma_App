package RecycleViewStuff;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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


import java.util.ArrayList;
import java.util.List;


import Objects.Product;
import Objects.User;
import fragments.DescriptionFragment;
import fragments.ShoppingCardFragment;
import fragments.StoreFragment;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> implements Filterable{

    private Context context;
    private List<Product> productList,productListFull;
    private static int checkerCounter = 0;
    private ShoppingCardFragment shoppingCardFragment;
    private StoreFragment storeFragment;
    private boolean check=false, check1=true;

    private SharedPreferences keySharedPreferences;
    private static final String KEY_SHARED_PREF = "keyshared";
    private static final String KEY_CHECKER = "key";

    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference reference1;
    Query query;
    private String a;
    String c;
    private ArrayList<Product> list2;
    private String keys;
    private String products;
    public MyViewHolder myViewHolder;
    boolean checker = true;
    String  productName;
    String  productPriceString;
    int productPrice;
    String  productCategory;
    String  productDescription;
    String  productImage;
    int  productQuantity;
    String  productKey;

    private SharedPreferences storeSharedPreferences;
    private static final String STORE_SHARED_PREF = "storeshared";
    private static final String STORE_CHECKER = "store";

    private SharedPreferences btmDisablePreference;
    private static final String DISABLE_SHARED_PREF = "disableshared";
    private static final String DISABLE_CHECKER = "disable";

    public RecycleViewAdapter(Context context, List<Product> productList,ShoppingCardFragment shoppingCardFragment, StoreFragment storeFragment) {
        this.context = context;
        this.productList = productList;
        this.productListFull = productList;
        this.shoppingCardFragment = shoppingCardFragment;
        this.storeFragment = storeFragment;
        this.list2=new ArrayList<>();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_item,parent,false);
        myViewHolder=new MyViewHolder(view, (ArrayList<Product>) productList, shoppingCardFragment, storeFragment);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        database = FirebaseDatabase.getInstance("https://bioderma-d8248-default-rtdb.firebaseio.com/");
        reference = database.getReference().child("Users");
        reference1=database.getReference().child("Bioderma Products");

        keySharedPreferences = context.getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
        storeSharedPreferences = context.getSharedPreferences(STORE_SHARED_PREF, Context.MODE_PRIVATE);
        btmDisablePreference = context.getSharedPreferences(DISABLE_SHARED_PREF, Context.MODE_PRIVATE);

        holder.price.setText(productList.get(position).getPriceString());
        holder.product_title.setText(productList.get(position).getNameProduct());

        shoppingCardFragment.setStoreFragment(storeFragment);
        shoppingCardFragment.setCardProductList((ArrayList<Product>) productListFull);

        byte[] decodedString = Base64.decode(productList.get(position).getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.img_product_category.setImageBitmap(decodedByte);
        checker = true;
        if (productList.get(position).getShoppingCardExist()) {
            holder.cardButton.setTextSize(25);
            holder.cardButton.setText("Add");
            holder.cardButton.setEnabled(false);
            holder.cardButton.setTextColor(Color.parseColor("#FFD3D3D3"));
            holder.cardButton.setClickable(false);
            check1 = false;

        } else {
            holder.cardButton.setTextSize(25);
            holder.cardButton.setText("Add");
            holder.cardButton.setEnabled(true);
            holder.cardButton.setClickable(true);
            holder.cardButton.setTextColor(Color.parseColor("#FFFFFFFF"));
            check = true;
            check1 = true;
        }
        if (productList.get(position).getQuantity() == 0) {
            holder.cardButton.setTextSize(12);
            holder.cardButton.setText("out of stock");
            holder.cardButton.setTextColor(Color.parseColor("#FFD3D3D3"));
            holder.cardButton.setEnabled(false);
        } else {
            keys = keySharedPreferences.getString(KEY_CHECKER, "");
            if (keys.matches("")) {
            } else {
                query = reference.orderByChild(keys);
                holder.cardButton.setClickable(false);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot single : snapshot.getChildren()) {
                            a = single.getValue(User.class).getKey();
                            if (keys.equals(a)) {
                                products = single.getValue(User.class).getProductKeys();
                                if (products.contains(productList.get(position).getNativeKeyProduct())) {
                                    productList.get(position).setShoppingCardExist(true);
                                    holder.cardButton.setTextSize(25);
                                    holder.cardButton.setText("Add");
                                    holder.cardButton.setTextColor(Color.parseColor("#FFD3D3D3"));
                                    holder.cardButton.setClickable(false);
                                    check1 = false;
                                } else {
                                    check = true;
                                    check1 = true;
                                }
                                break;
                            }
                        }
                        checker = true;

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        }
        holder.cardButton.setClickable(true);

        holder.cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String g = keySharedPreferences.getString(KEY_CHECKER, "");
                if (g.matches("")) {
                    Snackbar snackbar = Snackbar.make(v, "You are not registered", Snackbar.LENGTH_LONG);
                    snackbar.setAnchorView(((MainActivity) context).btNavView);
                    snackbar.show();
                } else {
                    String product2 = productList.get(position).getNativeKeyProduct();
                    for (Product product : shoppingCardFragment.getMCardProductList()) {
                        String product3 = product.getNativeKeyProduct();
                        if (product2.equals(product3)) {
                            checker = false;
                        }
                    }

                    if (holder.cardButton.getCurrentTextColor() == Color.parseColor("#FFD3D3D3")) {

                    } else {
                        if (checker) {
                            String key3 = keySharedPreferences.getString(KEY_CHECKER, "");
                            query = reference.orderByChild(key3);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot single : snapshot.getChildren()) {
                                        a = single.getValue(User.class).getKey();
                                        if (key3.equals(a)) {
                                            products = single.getValue(User.class).getProductKeys();
                                            ((MainActivity) context).shoppingCardFragment.clearMCardList();
                                            if (products == null || products.trim().matches("")) {
                                                products = productList.get(position).getNativeKeyProduct() + " ";
                                                single.getRef().child("productKeys").setValue(products);
                                            } else {
                                                products += productList.get(position).getNativeKeyProduct() + " ";
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
                            holder.cardButton.setText("Add");
                            holder.cardButton.setTextColor(Color.parseColor("#FFD3D3D3"));
                            holder.cardButton.setEnabled(false);
                            holder.cardButton.setClickable(false);
                            productList.get(position).setShoppingCardExist(true);
                            check = true;
                            check1 = true;
                        }
                        else {
                            Toast.makeText(context, "Product is already added", Toast.LENGTH_LONG).show();
                            holder.cardButton.setText("Add");
                            holder.cardButton.setTextColor(Color.parseColor("#FFD3D3D3"));
                            holder.cardButton.setClickable(false);
                            productList.get(position).setShoppingCardExist(true);
                        }
                    }
                }
            }
        });

        holder.img_product_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor disableEditor=btmDisablePreference.edit();
                disableEditor.putBoolean(DISABLE_CHECKER, false);
                disableEditor.apply();

                ((MainActivity) v.getContext()).frameLayout.setVisibility(View.INVISIBLE);
                String key=productList.get(holder.getAdapterPosition()).getNativeKeyProduct();
                query = reference1.orderByChild(key);
                ((MainActivity) v.getContext()).progressBar_cyclic1.setVisibility(View.VISIBLE);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot single : snapshot.getChildren()) {
                            a = single.getValue(Product.class).getNativeKeyProduct();
                            if (key.equals(a)) {

                                int d=0;
                                SharedPreferences.Editor editor = storeSharedPreferences.edit();
                                editor.putInt(STORE_CHECKER, 2);
                                editor.apply();

                                Product product=single.getValue(Product.class);

                                productName=single.getValue(Product.class).getNameProduct();
                                productPrice=single.getValue(Product.class).getPrice();
                                productCategory=single.getValue(Product.class).getCategory();
                                productDescription=single.getValue(Product.class).getDescription();
                                productImage=single.getValue(Product.class).getImage();
                                productQuantity=single.getValue(Product.class).getQuantity();
                                productKey=single.getValue(Product.class).getNativeKeyProduct();

                                for(Product product1 : productListFull){
                                    if(product1.getNativeKeyProduct().matches(productKey)){
                                        ((MainActivity) v.getContext()).setFragment(((MainActivity) v.getContext()).descriptionFragment=new DescriptionFragment(productName, productPrice, productCategory, productDescription, productImage, productQuantity, product, shoppingCardFragment, productKey, d, storeFragment));
                                    }
                                    else{
                                        ++d;
                                    }
                                }
                                productList=productListFull;
                                storeFragment.setMlist((ArrayList) productListFull);
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
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FirebaseDatabase database;
        DatabaseReference reference;
        private Context mContext;

        ConstraintLayout constraintLayout;
        TextView product_title;
        ImageView img_product_category;
        TextView price;
        Button cardButton;
        List<Product> mProductList;
        ShoppingCardFragment shoppingCardFragment1;
        StoreFragment storeFragment1;

        private SharedPreferences storeSharedPreferences;
        private static final String STORE_SHARED_PREF = "storeshared";
        private static final String STORE_CHECKER = "store";

        private SharedPreferences btmDisablePreference;
        private static final String DISABLE_SHARED_PREF = "disableshared";
        private static final String DISABLE_CHECKER = "disable";

        public MyViewHolder(@NonNull View itemView, ArrayList<Product> mlist, ShoppingCardFragment shoppingCardFragment, StoreFragment storeFragment) {
            super(itemView);
            this.mContext = itemView.getContext();

            storeSharedPreferences = mContext.getSharedPreferences(STORE_SHARED_PREF, Context.MODE_PRIVATE);
            btmDisablePreference = mContext.getSharedPreferences(DISABLE_SHARED_PREF, Context.MODE_PRIVATE);

            product_title = itemView.findViewById(R.id.product_text_id);
            img_product_category = itemView.findViewById(R.id.img_id);
            img_product_category.setOnClickListener(this);
            price = itemView.findViewById(R.id.price);
            cardButton = itemView.findViewById(R.id.card_button);
            cardButton.setOnClickListener(this);
            mProductList = mlist;
            shoppingCardFragment1 = shoppingCardFragment;
            storeFragment1 = storeFragment;
            database = FirebaseDatabase.getInstance("https://bioderma-d8248-default-rtdb.firebaseio.com/");
            reference = database.getReference().child("Bioderma Products");
        }

        @Override
        public void onClick(View v) {
        }


    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString().trim().replaceAll(" ", ""),previousKey;
                int letterCounter = key.length();
                if(checkerCounter < letterCounter)
                    checkerCounter = letterCounter;
                if(checkerCounter > letterCounter) {
                    productList = list2;
                }
                try {
                    previousKey = key.substring(0,key.length()-1);
                } catch (Exception e) {
                    e.printStackTrace();
                    previousKey = key;
                }

                if(key.isEmpty() || key.length() == 0){
                    productList = productListFull;
                }
                else{
                    List<Product> listFilter = new ArrayList<>();
                    if(productList.size() == 0)
                        productList = list2;


                    for(Product product : productList){
                        if(product.getNameProduct().toLowerCase().replaceAll(" ", "").contains(key.toLowerCase()))
                            listFilter.add(product);

                    }
                    productList = listFilter;
                }
                FilterResults results = new FilterResults();
                results.values = productList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public Filter getButtonFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if(constraint.toString().equals("All")){
                    list2.clear();
                    for (Product product : productListFull) {
                        list2.add(product);
                    }
                    productList = productListFull;
                    FilterResults results = new FilterResults();
                    results.values = productList;
                    return results;
                }

                list2.clear();
                for (Product product : productListFull) {
                    if(product.getCategory().contains(constraint.toString())) {
                        list2.add(product);
                    }
                }


                productList = list2;
                FilterResults results = new FilterResults();
                results.values = productList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
