package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

import Interface.FirebaseAdapter;
import Objects.Product;
import RecycleViewStuff.RecyclerViewShoppingAdapter;
import Objects.User;

public class ShoppingCardFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static List<Product> mCardProductList;
    private static List<Product> fullCardProductList;

    private String mParam1;
    private String mParam2;

    private static StoreFragment storeFragment;

    private SharedPreferences keySharedPreferences;
    private static final String KEY_SHARED_PREF = "keyshared";
    private static final String KEY_CHECKER = "key";
    private SharedPreferences btmDisablePreference;
    private static final String DISABLE_SHARED_PREF = "disableshared";
    private static final String DISABLE_CHECKER = "disable";

    private FirebaseAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    Query query;
    RecyclerView recyclerView;
    RecyclerViewShoppingAdapter recyclerViewShoppingAdapter;
    LinearLayout frameLayout;

    private String key;
    private String a;
    public boolean check=true, b=true;

    public ShoppingCardFragment() {
        storeFragment=null;
        mCardProductList = new ArrayList<>();
        fullCardProductList=new ArrayList<>();
        this.database = FirebaseDatabase.getInstance("https://bioderma-d8248-default-rtdb.firebaseio.com/");
        this.reference = database.getReference().child("Users");
    }

    public void setmCardProductList(Product product) {
        mCardProductList.add(product);
    }

    public static ShoppingCardFragment newInstance(String param1, String param2) {
        ShoppingCardFragment fragment = new ShoppingCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btmDisablePreference=this.getActivity().getSharedPreferences(DISABLE_SHARED_PREF, Context.MODE_PRIVATE);
        keySharedPreferences=this.getActivity().getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
    }



    public ArrayList<Product> getMCardProductList(){
        return (ArrayList<Product>) mCardProductList;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_card, container, false);
        frameLayout=view.findViewById(R.id.shoppingBackground);
        recyclerView = view.findViewById(R.id.shopping_card_recyclerview);
        recyclerViewShoppingAdapter = new RecyclerViewShoppingAdapter(getContext(),mCardProductList, storeFragment);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(recyclerViewShoppingAdapter);
        key = keySharedPreferences.getString(KEY_CHECKER, "");
        if(check) {
            adapter = new FirebaseAdapter() {
                @Override
                public void readInfo() {
                    key = keySharedPreferences.getString(KEY_CHECKER, "");

                    if(key.matches("")){

                    }
                    else {
                        query = reference.orderByChild(key);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot single : snapshot.getChildren()) {
                                    a = single.getValue(User.class).getKey();
                                    if (key.equals(a)) {
                                        User user = single.getValue(User.class);
                                        for (Product product : fullCardProductList) {
                                            if (user.getProductKeys().contains(product.getNativeKeyProduct())) {
                                                if(mCardProductList.contains(product)){

                                                }
                                                else {
                                                    mCardProductList.add(product);
                                                }
                                            }
                                        }
                                    }
                                    recyclerView = view.findViewById(R.id.shopping_card_recyclerview);
                                    recyclerViewShoppingAdapter = new RecyclerViewShoppingAdapter(getContext(), mCardProductList, storeFragment);
                                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                                    recyclerView.setAdapter(recyclerViewShoppingAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            };
            adapter.readInfo();
            check=false;
        }


        if(((MainActivity)getActivity()).mainBoolean) {
            ((MainActivity)getActivity()).frameLayout.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).progressBar_cyclic1.setVisibility(View.GONE);
        }

        return view;
    }

    public void setCardProductList(ArrayList<Product> mList){
        fullCardProductList=new ArrayList<>();
        fullCardProductList=mList;
    }

    public void clearMCardList(){
        mCardProductList=new ArrayList<>();
    }

    public void setStoreFragment(StoreFragment storeFragment1){
        storeFragment=storeFragment1;
    }

}