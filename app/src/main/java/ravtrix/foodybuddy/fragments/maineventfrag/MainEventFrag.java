package ravtrix.foodybuddy.fragments.maineventfrag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.decorator.DividerDecoration;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.adapter.EventAdapter;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.Event;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.EventModel;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.model.Response;
import ravtrix.foodybuddy.networkmodel.EventParam;
import ravtrix.foodybuddy.utils.Constants;
import ravtrix.foodybuddy.utils.Helpers;
import ravtrix.foodybuddy.utils.RetrofitEventSingleton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ravinder on 1/27/17.
 */

public class MainEventFrag extends Fragment implements IOnDistanceSettingSelected {

    @BindView(R.id.frag_eventmain_recyclerView) protected RecyclerView eventRecyclerView;
    private List<Event> eventModels;
    private CompositeSubscription mSubscriptions;
    private UserLocalStore userLocalStore;
    private EventAdapter eventAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_event_main, container, false);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this, view);


        mSubscriptions = new CompositeSubscription();
        fetchEvents();

        userLocalStore = new UserLocalStore(getActivity());

        return view;
    }


    private void fetchEvents() {
        mSubscriptions.add(RetrofitEventSingleton.getRetrofitEvent()
                .getEvents()
                .getEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(List<Event> events) {
                        eventModels = events;
                        for (int i = 0; i < eventModels.size(); i++) {
                            eventModels.get(i).setOwnerImage("http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png");
                        }
                        setRecyclerView();
                    }
                }));
    }

    private void fetchEventRefresh() {
        mSubscriptions.add(RetrofitEventSingleton.getRetrofitEvent()
                .getEvents()
                .getEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(List<Event> events) {
                        eventModels = events;
                        for (int i = 0; i < eventModels.size(); i++) {
                            eventModels.get(i).setOwnerImage("http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png");
                        }
                        // refresh with new data
                        eventAdapter.swap(eventModels);

                    }
                }));
    }

    /**
     * Fill the models of Event Model array list
     */
    private void setModels() {
        EventModel eventModel1 = new EventModel("http://images6.fanpop.com/image/photos/39200000/taylor-swift-icons-demmah-39210598-250-250.png", "Lucky Charm Restaurant", "Jan 02, 2017", "Jan 30, 2017", "Who wants to try this new place",
                "3222 Broadway", "1 miles", 0, "http://purrfectcatbreeds.com/wp-content/uploads/2014/06/small-cat-breeds.jpg", "http://purrfectcatbreeds.com/wp-content/uploads/2014/06/small-cat-breeds.jpg", "http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png",
                "http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png");

        EventModel eventModel2 = new EventModel("http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png", "Red Lobster Curry Style", "Jan 02, 2017", "Jan 30, 2017", "I heard they serve really good Indian authentic food. I suddenly feel like being a curry.",
                "3242 Lucky", "3 miles", 5, "", "", "", "");

        EventModel eventModel3 = new EventModel("http://purrfectcatbreeds.com/wp-content/uploads/2014/06/small-cat-breeds.jpg", "Pho Me", "Jan 02, 2017", "Jan 30, 2017", "Pho me Pho you. Who cares? I need Pho right now. ",
                "213 Moron", "13 miles", 1, "http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png", "", "", "");

        EventModel eventModel4 = new EventModel("http://a1.mzstatic.com/us/r30/Purple4/v4/eb/36/69/eb366995-c26d-85be-16e3-44cbb1adff9a/icon350x350.png", "All You Can Eat Foo Foo", "Jan 02, 2017", "Jan 30, 2017", "I'm trying to gain 10 pounds before my wedding. The baby bump is coming soon too!",
                "11112 Hell street", "31 miles", 2, "http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png", "http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png", "http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png", "");
    }

    public void joinEventRetrofit(int eventID, final String restID) {

        System.out.println("USER ID: " + userLocalStore.getLoggedInUser().getUserID());

        System.out.println("IM GONNA TO JOIN EVENT");


        mSubscriptions.add(RetrofitEventSingleton.getRetrofitEvent()
                .joinEvent()
                .joinEvenet(new EventParam(userLocalStore.getLoggedInUser().getUserID(), eventID, restID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("JOIN ERROR");

                    }

                    @Override
                    public void onNext(Response response) {
                        System.out.println("JOIN DONE");

                        Helpers.displayToast(getActivity(), response.getMessage());
                    }
                }));

    }
    /**
     * Sets up the recycler view with its adapter and decorator
     */
    private void setRecyclerView() {

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        eventAdapter = new EventAdapter(this, eventModels);
        eventRecyclerView.addItemDecoration(dividerDecorator);
        eventRecyclerView.setAdapter(eventAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * Called by the main activity to scroll the current recycler view of this fragment to the first item
     */
    public void scrollToTop() {
        eventRecyclerView.scrollToPosition(0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.NEW_EVENT_REQUEST_CODE &&
                resultCode == Constants.EVENT_INSERTED_RESULT_CODE) {
            // Callback after event inserted. Time to refresh this fragment
            fetchEventRefresh();
        }
    }

    @Override
    public void onDistanceSelected(String distance) {
        Helpers.displayToast(getContext(), "FRAGMENT RECEIVED VALUE: " + distance);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
