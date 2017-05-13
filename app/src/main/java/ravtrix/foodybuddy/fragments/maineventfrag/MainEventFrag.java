package ravtrix.foodybuddy.fragments.maineventfrag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import ravtrix.foodybuddy.activities.EventMapActivity;
import ravtrix.foodybuddy.decorator.DividerDecoration;
import ravtrix.foodybuddy.fragactivityinterfaces.OnEventJoined;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.adapter.EventAdapter;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.Event;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.network.networkmodel.NewChatParam;
import ravtrix.foodybuddy.network.networkresponse.Response;
import ravtrix.foodybuddy.network.networkmodel.EventParam;
import ravtrix.foodybuddy.utils.Constants;
import ravtrix.foodybuddy.utils.Helpers;
import ravtrix.foodybuddy.utils.HelpersAPI;
import ravtrix.foodybuddy.utils.RetrofitChatSingleton;
import ravtrix.foodybuddy.utils.RetrofitEventSingleton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ravinder on 1/27/17.
 */

public class MainEventFrag extends Fragment implements IOnDistanceSettingSelected, View.OnClickListener {

    @BindView(R.id.frag_eventmain_recyclerView) protected RecyclerView eventRecyclerView;
    @BindView(R.id.activity_event_main_floatingButtonMap) protected FloatingActionButton bLiveEventMap;

    private static final String CLASS_NAME = MainEventFrag.class.getSimpleName();
    private List<Event> eventModels;
    private CompositeSubscription mSubscriptions;
    private UserLocalStore userLocalStore;
    private EventAdapter eventAdapter;
    private OnEventJoined onEventJoinedInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.onEventJoinedInterface = (OnEventJoined) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_event_main, container, false);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this, view);

        mSubscriptions = new CompositeSubscription();
        userLocalStore = new UserLocalStore(getActivity());

        bLiveEventMap.setOnClickListener(this);

        fetchEvents();
        HelpersAPI.updateUserLocation(getActivity(), userLocalStore);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_event_main_floatingButtonMap:
                startActivity(new Intent(getActivity(), EventMapActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * Fetch main events
     */
    private void fetchEvents() {
        mSubscriptions.add(RetrofitEventSingleton.getInstance()
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
                        setRecyclerView();
                    }
                }));
    }

    /**
     * Refresh current list of events
     */
    private void fetchEventRefresh() {
        mSubscriptions.add(RetrofitEventSingleton.getInstance()
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
                        // refresh with new data
                        eventAdapter.swap(eventModels);

                    }
                }));
    }

    /**
     * Join an event
     * @param eventID               - the eventID
     * @param restID                - the restaurantID
     */
    public void joinEventRetrofit(final int eventID, final String restID) {

        mSubscriptions.add(RetrofitEventSingleton.getInstance()
                .joinEvent()
                .joinEvenet(new EventParam(userLocalStore.getLoggedInUser().getUserID(), eventID, restID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(Response response) {
                        joinEventChatRetrofit(eventID);
                    }
                }));

    }

    /**
     * Join an event chat
     * @param eventID               - the eventID
     */
    public void joinEventChatRetrofit(int eventID) {

        mSubscriptions.add(RetrofitChatSingleton.getInstance()
                .insertChat()
                .insertChat(new NewChatParam(userLocalStore.getLoggedInUser().getUserID(), eventID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(Response response) {
                        Helpers.displayToast(getActivity(), response.getMessage());
                        onEventJoinedInterface.onEventJoined(); // refresh drawer in main
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

        if (requestCode == Constants.NEW_EVENT_REQUEST_CODE) {
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
