package ravtrix.foodybuddy.fragments.maineventfrag;

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
import ravtrix.foodybuddy.network.NetworkUtil;
import ravtrix.foodybuddy.utils.Helpers;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_event_main, container, false);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this, view);

        //setModels();

        mSubscriptions = new CompositeSubscription();
        mSubscriptions.add(NetworkUtil.getRawRetrofit().getEvents()
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

        return view;
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

    /**
     * Sets up the recycler view with its adapter and decorator
     */
    private void setRecyclerView() {

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        EventAdapter eventAdapter = new EventAdapter(getContext(), eventModels);
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
    public void onDistanceSelected(String distance) {
        Helpers.displayToast(getContext(), "FRAGMENT RECEIVED VALUE: " + distance);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
