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
import ravtrix.foodybuddy.localstore.UserLocalStore;
import retrofit2.Call;

/**
 * Created by Emily on 1/28/17.
 */

public class DealsFragment extends Fragment {

    @BindView(R.id.frag_display_deals_recyclerView) protected RecyclerView recyclerviewDeals;
    private List<DealsModel> dealsModels;
    private Map<String, String> params = new HashMap<>();
    private final String CONSUMER_KEY = "9HmtA8JSGhJyhkzYC_07Eg";
    private final String CONSUMER_SECRET = "NcDOmqldwulUxzNj1vcsZzErPQk";
    private final String TOKEN = "Ck_9Np3gLjbpEy1ks3vtHGJXYJDul3Oh";
    private final String TOKEN_SECRET = "uvVIsIpGYO7JbuciZhkoENmyO8A";
    private Call<SearchResponse> call;
    private SearchResponse searchResponse;
    private ArrayList<String> businessNameList = new ArrayList<>();
    private ArrayList<String> businessImageList = new ArrayList<>();
    private ArrayList<String> businessAddressList = new ArrayList<>();
    private ArrayList<String> dealsTitle = new ArrayList<>();
    private ArrayList<String> deals = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();
    private boolean isViewShown = false;
    private UserLocalStore userLocalStore;

    private void addParams() {
        params.put("category_filter", "restaurants");
        params.put("deals_filter", "true");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getView() != null) {
            isViewShown = true;
            loadViewWithData();
        } else {
            isViewShown = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deals, container, false);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this, view);
        userLocalStore = new UserLocalStore(getActivity());

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        recyclerviewDeals.addItemDecoration(dividerDecorator);

        if (!isViewShown) {
            loadViewWithData();
        }

        return view;
    }

    private void loadViewWithData() {

        YelpAPIFactory apiFactory = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
        YelpAPI yelpAPI = apiFactory.createAPI();
        addParams();
        call = yelpAPI.search(userLocalStore.getLatitude() + "," + userLocalStore.getLongitude(), params);
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

            dealsModels = new ArrayList<>();

            for (int i = 0; i < businesses.size(); i++) {

                businessNameList.add(businesses.get(i).name());
                String addr = businesses.get(i).location().displayAddress().toString();
                businessImageList.add(businesses.get(i).imageUrl());
                addr = addr.replaceAll("[\\[\\](){}]","");
                businessAddressList.add(addr);
                urlList.add(businesses.get(i).mobileUrl());

                if (businesses.get(i).deals() != null) {
                    for (int j = 0; j < businesses.get(i).deals().size(); j++) {

                        dealsTitle.add(businesses.get(i).deals().get(j).title());
                        String deal = businesses.get(i).deals().get(j).whatYouGet();
                        String[] newStr = deal.split("Print");
                        deals.add(newStr[0]);
                    }
                }
            }

            for (int k = 0; k < deals.size(); k++){
                DealsModel dealsModel = new DealsModel(businessNameList.get(k), deals.get(k),
                        businessAddressList.get(k), urlList.get(k), businessImageList.get(k), dealsTitle.get(k));
                dealsModels.add(dealsModel);

            }
            setRecyclerView();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

    }

    private void setRecyclerView() {
        DealsAdapter dealsAdapter = new DealsAdapter(getContext(), dealsModels);
        recyclerviewDeals.setAdapter(dealsAdapter);
        recyclerviewDeals.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}