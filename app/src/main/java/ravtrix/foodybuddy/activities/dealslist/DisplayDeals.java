package ravtrix.foodybuddy.activities.dealslist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ravtrix.foodybuddy.R;
import retrofit2.Call;

import static ravtrix.foodybuddy.R.id.item_deal_tvAddress;
import static ravtrix.foodybuddy.R.id.item_deal_tvDescription;
import static ravtrix.foodybuddy.R.id.item_deal_tvRestaurantName;
import static ravtrix.foodybuddy.R.id.item_deal_tvUrl;


public class DisplayDeals extends AppCompatActivity {

    private Map<String, String> params = new HashMap<>();
    private String CONSUMER_KEY = "9HmtA8JSGhJyhkzYC_07Eg";
    private String CONSUMER_SECRET = "NcDOmqldwulUxzNj1vcsZzErPQk";
    private String TOKEN = "Ck_9Np3gLjbpEy1ks3vtHGJXYJDul3Oh";
    private String TOKEN_SECRET = "uvVIsIpGYO7JbuciZhkoENmyO8A";
    private TextView restaurantName;
    private TextView restaurantAddr;
    private TextView url;
    private TextView dealDescription;
    private Call<SearchResponse> call;
    private SearchResponse searchResponse;
    private ArrayList<String> businessNameList = new ArrayList<>();
    private ArrayList<String> businessAddressList = new ArrayList<>();
    private ArrayList<String> deals = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();



    private void addParams() {
        params.put("category_filter", "restaurants");
        params.put("deals_filter", "true");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_deals);

        YelpAPIFactory apiFactory = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
        YelpAPI yelpAPI = apiFactory.createAPI();

        addParams();

        call = yelpAPI.search("San Francisco", params);

        new getBusinesses().execute();


    }


    private class getBusinesses extends AsyncTask<Void, Void, ArrayList<Business>> {
        @Override
        protected ArrayList<Business> doInBackground(Void... params) {
            try {
                searchResponse = call.execute().body();
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
            return searchResponse.businesses();
        }

        @Override
        protected void onPostExecute(ArrayList<Business> businesses) {
            restaurantName = (TextView) findViewById(item_deal_tvRestaurantName);
            restaurantAddr = (TextView) findViewById(item_deal_tvAddress);
            url = (TextView) findViewById(item_deal_tvUrl);
            dealDescription = (TextView) findViewById(item_deal_tvDescription);


            for (int i = 0; i < businesses.size(); i++) {
                businessNameList.add(businesses.get(i).name());
                String addr = businesses.get(i).location().displayAddress().toString();
                addr = addr.replaceAll("[\\[\\](){}]","");
                businessAddressList.add(addr);
                urlList.add(businesses.get(i).mobileUrl());
                for (int j = 0; j < businesses.get(i).deals().size(); j++) {
                    deals.add(businesses.get(i).deals().get(j).whatYouGet());
                }
            }

            for (int k = 0; k < businessNameList.size(); k++){
                restaurantName.setText(businessNameList.get(k));
                restaurantAddr.setText(businessAddressList.get(k));
                url.setText(urlList.get(k));
                dealDescription.setText(deals.get(k));

            }

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

    }
}
