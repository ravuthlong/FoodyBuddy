package ravtrix.foodybuddy.activities.findresturant;

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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.findresturant.adapter.FindRestaurantAdapter;
import ravtrix.foodybuddy.activities.findresturant.model.RestaurantModel;
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
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private List<RestaurantModel> restaurantModels;
    private FindRestaurantAdapter findRestaurantAdapter;

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
                    etSearch.clearFocus();
                    Helpers.hideKeyboard(FindRestaurant.this);
                    fetchRestaurants("San Jose", "restaurants+" + etSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_drawer);
        recyclerView.addItemDecoration(dividerDecorator);

        fetchRestaurants("San Jose", "restaurants");

        imageDelete.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.activity_find_restaurant_imageDelete:
                // delete existing text
                etSearch.setText("");
                break;
            default:
                break;
        }
    }

    private void fetchRestaurants(String keyword, String city) {
        Call<JsonObject> jsonObjectCall = RetrofitYelpSingleton.getRetrofitYelp()
                .fetchAPlace()
                .fetchAPlace(keyword, city);

        //Helpers.displayToast(this, "FETCHING:");
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

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

                    System.out.println("NAME: " + jsonObject.get("name").getAsString()); restaurantModel.setName(jsonObject.get("name").getAsString());
                    System.out.println("IMAGE: " + jsonObject.get("image_url").getAsString()); restaurantModel.setImage_url(jsonObject.get("image_url").getAsString());
                    System.out.println("RATING: " + jsonObject.get("rating").getAsString()); restaurantModel.setRating(jsonObject.get("rating").getAsDouble());
                    System.out.println("REVIEWS: " + jsonObject.get("review_count").getAsString());

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
                    System.out.println("CATEGORIES: " + categories); restaurantModel.setCategories(categories);

                    if (!jsonObject.get("location").getAsJsonObject().get("address1").isJsonNull()) {
                        System.out.println("ADDRESS: " + jsonObject.get("location").getAsJsonObject().get("address1").getAsString()); restaurantModel.setAddress(jsonObject.get("location").getAsJsonObject().get("address1").getAsString());
                    }
                    System.out.println("ID: " + jsonObject.get("id").getAsString());
                    restaurantModels.add(restaurantModel);

                }

                findRestaurantAdapter = new FindRestaurantAdapter(FindRestaurant.this, restaurantModels);
                recyclerView.setAdapter(findRestaurantAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(FindRestaurant.this));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(FindRestaurant.this, "ERROR FETCH");

            }
        });
    }
}
