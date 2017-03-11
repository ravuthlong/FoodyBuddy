package ravtrix.foodybuddy.fragments.deals;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.decorator.DividerDecoration;
import ravtrix.foodybuddy.fragments.deals.recyclerview.adapter.DealsAdapter;
import ravtrix.foodybuddy.fragments.deals.recyclerview.model.DealsModel;
import ravtrix.foodybuddy.utils.Helpers;
import retrofit2.Call;

;
;

/**
 * Created by Ravinder on 1/28/17.
 */

public class DealsFragment extends Fragment {

    @BindView(R.id.frag_display_deals_recyclerView) protected RecyclerView recyclerviewDeals;
    private List<DealsModel> dealsModels;
    boolean isFragLoaded = false;

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



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deals, container, false);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this, view);
        Helpers.displayToast(getContext(), "CALLED2");

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        recyclerviewDeals.addItemDecoration(dividerDecorator);

        YelpAPIFactory apiFactory = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
        YelpAPI yelpAPI = apiFactory.createAPI();

        addParams();

        call = yelpAPI.search("San Francisco", params);

        new getBusinesses().execute();


        return view;
    }

   //private void loadViewWithData() {
      //  loadData();
   //     setRecyclerView();
    //}

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
            dealsModels = new ArrayList<>();


            for (int i = 0; i < businesses.size(); i++) {
                businessNameList.add(businesses.get(i).name());
                String addr = businesses.get(i).location().displayAddress().toString();
                addr = addr.replaceAll("[\\[\\](){}]","");
                businessAddressList.add(addr);
                urlList.add(businesses.get(i).mobileUrl());
                for (int j = 0; j < businesses.get(i).deals().size(); j++) {
                    String deal = businesses.get(i).deals().get(j).whatYouGet().toString();
                    String[] newStr = deal.split("Print");
                    deals.add(newStr[0]);
                }
            }

            for (int k = 0; k < businessNameList.size(); k++){

                DealsModel dealsModel = new DealsModel(businessNameList.get(k), deals.get(k), businessAddressList.get(k), urlList.get(k));

                dealsModels.add(dealsModel);
                /*restaurantName.setText(businessNameList.get(k));
                restaurantAddr.setText(businessAddressList.get(k));
                url.setText(urlList.get(k));
                dealDescription.setText(deals.get(k));
*/
            }
            setRecyclerView();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

    }
/*
    private void loadData() {
        dealsModels = new ArrayList<>();

        for ()
        InboxModel inboxModel1 = new InboxModel("Ortemis", "http://images6.fanpop.com/image/photos/39200000/taylor-swift-icons-demmah-39210598-250-250.png", "01/11/2017", "Hey, do you remember this person?");
        InboxModel inboxModel2 = new InboxModel("Jojo191", "http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png", "01/01/2017", "This has got to be the biggest joke in the world...");
        InboxModel inboxModel3 = new InboxModel("Mika", "http://a1.mzstatic.com/us/r30/Purple4/v4/eb/36/69/eb366995-c26d-85be-16e3-44cbb1adff9a/icon350x350.png", "12/25/2016", "When are you free? I have something very important to tell you.");
        InboxModel inboxModel4 = new InboxModel("Joseph100", "http://purrfectcatbreeds.com/wp-content/uploads/2014/06/small-cat-breeds.jpg", "11/12/2016", "What is a world without peace and wisdom? That's right. Nothing. Not a world at all...");
        dealsModels.add(inboxModel1);
        dealsModels.add(inboxModel2);
        dealsModels.add(inboxModel3);
        dealsModels.add(inboxModel4);
    }
*/
    private void setRecyclerView() {
        DealsAdapter dealsAdapter = new DealsAdapter(getContext(), dealsModels);
        recyclerviewDeals.setAdapter(dealsAdapter);
        recyclerviewDeals.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
