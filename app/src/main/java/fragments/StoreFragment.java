package fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bioderma.MainActivity;
import com.example.bioderma.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import Objects.Product;
import RecycleViewStuff.RecycleViewAdapter;


public class StoreFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private SharedPreferences keySharedPreferences;
    private static final String KEY_SHARED_PREF = "keyshared";
    private static final String KEY_CHECKER = "key";

    private String mParam1;
    private String mParam2;
    private static boolean checker=true, c=true, d=true;
    private int SET_PAGE_COUNT = 3;
    private List<Product> mList, mCoolList;
    private StoreFragment storeFragment;
    Button  All, Purify, Treat, Care;
    private int c1;
    RecycleViewAdapter recycleViewAdapter;
    EditText searchView;
    Button cardButton;
    ShoppingCardFragment shoppingCardFragment;




    public StoreFragment() {
        mList = new ArrayList<>();
        this.database = FirebaseDatabase.getInstance("https://bioderma-d8248-default-rtdb.firebaseio.com/");
        this.reference = database.getReference().child("Bioderma Products");
        mCoolList = mList;
    }

    public StoreFragment(ArrayList arrayList){
        this.mList=arrayList;
    }

    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(checker) {
            storeFragment = new StoreFragment((ArrayList) mList);
            shoppingCardFragment = new ShoppingCardFragment();
            checker = false;
        }

    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        View view = inflater.inflate(R.layout.fragment_store, container, false);
        cardButton = view.findViewById(R.id.card_button);

        All = view.findViewById(R.id.all_button);
        Care = view.findViewById(R.id.care_button);
        Purify = view.findViewById(R.id.pury_button);
        Treat = view.findViewById(R.id.treat_button);

        RecyclerView recyclerView = view.findViewById(R.id.recycleView);
        recycleViewAdapter = new RecycleViewAdapter(getContext(), mList, shoppingCardFragment, storeFragment);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(recycleViewAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        CarouselView carouselView = view.findViewById(R.id.carouselView);
        carouselView.setPageCount(SET_PAGE_COUNT);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                int[] images = new int[]{
                        R.drawable.about_us_new2,R.drawable.instagram_new, R.drawable.news_new

                };
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(images[position]);

            }
        });

        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                if(position==0) {
                    goToUrl("http://hbluxe.kz/o-nas");
                }
                else if(position==1){
                    goToUrl("https://instagram.com/bioderma_almaty?utm_medium=copy_link");
                }
                else if(position==2){
                    goToUrl("http://hbluxe.kz/blog");
                }
            }
        });
        carouselView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });


        All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleViewAdapter.getButtonFilter().filter("All");
            }
        });

        Purify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleViewAdapter.getButtonFilter().filter("Purify the skin");
            }
        });
        Treat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleViewAdapter.getButtonFilter().filter("Treat the skin");
            }
        });
        Care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleViewAdapter.getButtonFilter().filter("Take care of your skin");
            }

        });
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusableInTouchMode(true);
                v.setFocusable(true);
                return false;
            }
        });


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recycleViewAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(((MainActivity)getActivity()).mainBoolean) {
            ((MainActivity)getActivity()).frameLayout.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public ArrayList<Product> getMlist(){
        return (ArrayList<Product>) mList;
    }

    public void setMlist(ArrayList mListSec){
        mList=new ArrayList<>();
        mList = mListSec;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}