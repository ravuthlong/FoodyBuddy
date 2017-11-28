package ravtrix.foodybuddy.fragments.maineventfrag;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.createevent.CreateEventActivity;
import ravtrix.foodybuddy.activities.eventmap.EventMapActivity;
import ravtrix.foodybuddy.activities.mainpage.MainActivity;
import ravtrix.foodybuddy.activitymonitorDB.DatabaseOperations;
import ravtrix.foodybuddy.decorator.DividerDecoration;
import ravtrix.foodybuddy.fragactivityinterfaces.OnEventJoined;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.adapter.EventAdapter;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.Event;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.network.networkmodel.NearbyEventsParam;
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

public class MainEventFrag extends Fragment implements IOnDistanceSettingSelected, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.frag_eventmain_recyclerView) protected RecyclerView eventRecyclerView;
    @BindView(R.id.activity_event_main_floatingButtonMap) protected FloatingActionButton bLiveEventMap;
    @BindView(R.id.frag_event_main_progressbar) protected ProgressBar progressBar;
    @BindView(R.id.activity_event_main_refreshLayout) protected SwipeRefreshLayout refreshLayout;

    private static final String CLASS_NAME = MainEventFrag.class.getSimpleName();
    private List<Event> eventModels;
    private CompositeSubscription mSubscriptions;
    private UserLocalStore userLocalStore;
    private EventAdapter eventAdapter;
    private OnEventJoined onEventJoinedInterface;

    private static final String TAG = "MainEventFrag";

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
        refreshLayout.setOnRefreshListener(this);

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

    @Override
    public void onRefresh() {
        fetchEventRefresh();
    }

    /**
     * Fetch nearby main events
     */
    private void fetchNearbyEvents(String radius) {

        if (!refreshLayout.isRefreshing()) {
            showProgressbar();
        }
        mSubscriptions.add(RetrofitEventSingleton.getInstance()
                .getNearbyEvents()
                .getNearbyEvents(new NearbyEventsParam(Float.toString(userLocalStore.getLatitude()),
                        Float.toString(userLocalStore.getLongitude()), radius))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        hideProgressbar();
                    }

                    @Override
                    public void onNext(List<Event> events) {
                        eventModels = events;
                        // refresh with new data
                        eventAdapter.swap(eventModels);
                        hideProgressbar();
                    }
                }));
    }

    /**
     * Fetch main events
     */
    private void fetchEvents() {

        showProgressbar();
        mSubscriptions.add(RetrofitEventSingleton.getInstance()
                .getEvents()
                .getEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        hideProgressbar();
                    }

                    @Override
                    public void onNext(List<Event> events) {
                        eventModels = events;
                        setRecyclerView();
                        hideProgressbar();
                    }
                }));
    }

    /**
     * Refresh current list of events
     */
    private void fetchEventRefresh() {
        showProgressbar();
        mSubscriptions.add(RetrofitEventSingleton.getInstance()
                .getEvents()
                .getEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        hideProgressbar();
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<Event> events) {
                        eventModels = events;
                        // refresh with new data
                        if (eventAdapter != null) {
                            eventAdapter.swap(eventModels);
                        }
                        hideProgressbar();
                        refreshLayout.setRefreshing(false);
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
                .joinEvent(new EventParam(userLocalStore.getLoggedInUser().getUserID(), eventID, restID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressbar();
                    }

                    @Override
                    public void onNext(Response response) {

                        Helpers.displayToast(getActivity(), response.getMessage());
                        onEventJoinedInterface.onEventJoined(); // refresh drawer in main

                        if (!response.getMessage().equals("Event already joined")) {
                            joinEventChatRetrofit(eventID);
                        }
                        hideProgressbar();
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
                        // ask user to set event on their calender
                        //showAddToCalendarDialog();
                        DatabaseOperations databaseOperations = new DatabaseOperations(getActivity());
                        databaseOperations.insertActivity(databaseOperations, Long.toString(System.currentTimeMillis()), "You joined a new event.");
                    }
                }));
    }

    /**
     * Sets up the recycler view with its adapter and decorator
     */
    private void setRecyclerView() {

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        eventAdapter = new EventAdapter(this, eventModels, userLocalStore);
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

    public void showProgressbar() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressbar() {
        if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.NEW_EVENT_REQUEST_CODE) {
            // Callback after event inserted. Time to refresh this fragment
            fetchEventRefresh();
            ((MainActivity) getActivity()).fetchDrawerModelsRetrofitRefresh();
        }
    }

    @Override
    public void onDistanceSelected(String distance) {

        switch (distance) {
            case "Best Match":
                fetchEventRefresh();
                break;
            case "Within 5 miles":
                fetchNearbyEvents("5");
                break;
            case "Within 10 miles":
                fetchNearbyEvents("10");
                break;
            case "Within 15 miles":
                fetchNearbyEvents("15");
                break;
            default:
                fetchEventRefresh();
        }
    }

    private void showAddToCalendarDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("title", "Some title");
                intent.putExtra("description", "Some description");
                intent.putExtra("beginTime", 0);
                intent.putExtra("endTime", 0);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setTitle("Would you like to add this event to your calendar? This will set a reminder. ");

        // Set other dialog properties


        // Create the AlertDialog
        builder.create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
