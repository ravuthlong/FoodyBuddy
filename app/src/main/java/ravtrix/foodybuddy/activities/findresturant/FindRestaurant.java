package ravtrix.foodybuddy.activities.findresturant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.findresturant.adapter.FindRestaurantAdapter;
import ravtrix.foodybuddy.activities.findresturant.model.RestaurantModel;
import ravtrix.foodybuddy.activities.restaurantfilter.RestaurantFilterActivity;
import ravtrix.foodybuddy.decorator.DividerDecoration;
import ravtrix.foodybuddy.utils.Helpers;
import ravtrix.foodybuddy.utils.RetrofitYelpSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindRestaurant extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.activity_find_restaurant_recyclerview) protected RecyclerView recyclerView;
    @BindView(R.id.activity_find_restaurant_noResult) protected TextView tvNoResult;
    @BindView(R.id.activity_find_restaurant_search) protected EditText etSearch;
    @BindView(R.id.activity_find_restaurant_imageDelete) protected ImageView imageDelete;
    @BindView(R.id.activity_find_restaurant_filter) protected ImageView imageFilter;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private List<RestaurantModel> restaurantModels;
    private FindRestaurantAdapter findRestaurantAdapter;
    public static final int FIND_RESTAURANT_REQUEST_CODE = 1;
    private int distanceSpinnerSelected = 0;
    private int priceSpinnerSelected = 0;
    private int sortSpinnerSelected = 0;
    private String receivedDistance = "";
    private String receivedPrice = "";
    private String receivedSort = "";
    private String receivedKeyword = "";
    private String currentSearchTerm = "restaurants";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_restaurant);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Restaurant Selection");

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    currentSearchTerm = etSearch.getText().toString().trim();
                    Helpers.hideKeyboard(FindRestaurant.this);
                    etSearch.clearFocus();

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("location", "San Jose");

                    hashMap.put("term", etSearch.getText().toString().trim());
                    fetchRestaurantWithFilter(hashMap);

                    System.out.println("PATH - " + "restaurants+" + etSearch.getText().toString().trim());
                    //fetchRestaurants("San Jose", etSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_drawer);
        recyclerView.addItemDecoration(dividerDecorator);


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("location", "San Jose");
        hashMap.put("term", "restaurants");
        fetchRestaurantWithFilter(hashMap);

        imageDelete.setOnClickListener(this);
        imageFilter.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.activity_find_restaurant_imageDelete:
                // delete existing text
                etSearch.setText("");
                break;
            case R.id.activity_find_restaurant_filter:
                Intent intent = new Intent(this, RestaurantFilterActivity.class);
                intent.putExtra("distanceSelected", this.distanceSpinnerSelected);
                intent.putExtra("priceSelected", this.priceSpinnerSelected);
                intent.putExtra("sortSelected", this.sortSpinnerSelected);
                intent.putExtra("termSelected", this.receivedKeyword);

                startActivityForResult(intent, FIND_RESTAURANT_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: // filter request
                if (resultCode == Activity.RESULT_OK) {

                    // Data passed from Restaurant Filter Activity
                    this.receivedDistance = data.getStringExtra("distance");
                    this.receivedPrice = data.getStringExtra("price");
                    this.receivedSort = data.getStringExtra("sort");
                    this.receivedKeyword = data.getStringExtra("term"); Helpers.displayToast(this, receivedKeyword);
                    this.distanceSpinnerSelected = data.getIntExtra("distanceSelected", 0);
                    this.priceSpinnerSelected = data.getIntExtra("priceSelected", 0);
                    this.sortSpinnerSelected = data.getIntExtra("sortSelected", 0);

                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("location", "San Jose");

                    if (!receivedKeyword.equals("all")) {
                        hashMap.put("term", currentSearchTerm + " " + receivedKeyword);

                    } else {
                        // All is
                        hashMap.put("term", currentSearchTerm);
                    }

                    // Filter was submitted, need to refresh this page
                    if (!receivedDistance.isEmpty()) {
                        hashMap.put("radius", this.receivedDistance);
                    }
                    if (!receivedPrice.isEmpty()) {
                        hashMap.put("price", this.receivedPrice);
                    }
                    hashMap.put("sort_by", this.receivedSort);

                    fetchRestaurantWithFilter(hashMap);

                }
                break;
            default:
                break;
        }
    }

    private void fetchRestaurantWithFilter(HashMap<String, String> values) {

        Call<JsonObject> retrofit = RetrofitYelpSingleton.getRetrofitYelp()
                .fetchPlacesWithFilter()
                .fetchPlacesWithFilter(values);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                setData(response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(FindRestaurant.this, "ERROR FETCH");
            }
        });
    }

    private void setData(Response<JsonObject> response) {
        restaurantModels = new ArrayList<>();

        JsonArray array = response.body().get("businesses").getAsJsonArray();
        if (array.size() == 0) {
            tvNoResult.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        } else {
            tvNoResult.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        for (int x = 0; x < array.size(); x++) {
            RestaurantModel restaurantModel = new RestaurantModel();
            JsonObject jsonObject = array.get(x).getAsJsonObject();

            System.out.println(jsonObject.toString());

            restaurantModel.setId(jsonObject.get("id").getAsString());
            restaurantModel.setName(jsonObject.get("name").getAsString());
            restaurantModel.setImage_url(jsonObject.get("image_url").getAsString());
            restaurantModel.setRating(jsonObject.get("rating").getAsDouble());
            restaurantModel.setLongitude(jsonObject.get("coordinates").getAsJsonObject().get("longitude").getAsDouble());
            restaurantModel.setLatitude(jsonObject.get("coordinates").getAsJsonObject().get("latitude").getAsDouble());

            String categories = "";
            JsonArray categoriesArray = jsonObject.get("categories").getAsJsonArray();
            for (int i = 0; i < categoriesArray.size(); i++) {
                if (categoriesArray.size() == 1) {
                    categories += categoriesArray.get(i).getAsJsonObject().get("title").getAsString();
                } else if (i == 0) {
                    categories += categoriesArray.get(i).getAsJsonObject().get("title").getAsString() + ",";
                } else if (i == categoriesArray.size() - 1 && categoriesArray.size() != 1) {
                    categories += " " + categoriesArray.get(i).getAsJsonObject().get("title").getAsString();
                } else {
                    categories += " " + categoriesArray.get(i).getAsJsonObject().get("title").getAsString() + ",";
                }
            }
            restaurantModel.setCategories(categories);

            if (!jsonObject.get("location").getAsJsonObject().get("address1").isJsonNull()) {
                restaurantModel.setAddress(jsonObject.get("location").getAsJsonObject().get("address1").getAsString());
            }
            restaurantModels.add(restaurantModel);
        }
        setAdapter();
    }

    private void setAdapter() {
        findRestaurantAdapter = new FindRestaurantAdapter(FindRestaurant.this, restaurantModels);
        recyclerView.setAdapter(findRestaurantAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FindRestaurant.this));
    }
}
