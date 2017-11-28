package ravtrix.foodybuddy.activities.eventmap;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.CustomInfoWindowAdapter;
import ravtrix.foodybuddy.activitymonitorDB.DatabaseOperations;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.Event;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.network.networkmodel.EventParam;
import ravtrix.foodybuddy.network.networkmodel.NewChatParam;
import ravtrix.foodybuddy.network.networkresponse.Response;
import ravtrix.foodybuddy.utils.HelperEvent;
import ravtrix.foodybuddy.utils.Helpers;
import ravtrix.foodybuddy.utils.RetrofitChatSingleton;
import ravtrix.foodybuddy.utils.RetrofitEventSingleton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.widget.Toast.makeText;

/**
 * Created by Ravinder on 5/12/17.
 */

public class EventMapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    @BindView(R.id.toolbar) protected Toolbar toolbar;

    private ImageView imageRefresh;

    private GoogleMap googleMap;
    private List<Event> eventList;
    private CompositeSubscription mSubscriptions;
    private HashMap<Marker, Event> eventMarkerMap;
    private static final int ZOOM_LEVEL = 11;
    private HashMap<String, String> restaurantImages;

    // marker display range
    double range = 0.5;

    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);

        ButterKnife.bind(this);
        restaurantImages = new HashMap<>();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Event Map");

        imageRefresh = (ImageView) toolbar.findViewById(R.id.activity_event_map_imageRefresh);
        imageRefresh.setOnClickListener(this);

        mSubscriptions = new CompositeSubscription();
        userLocalStore = new UserLocalStore(this);
        //setMap();

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: Refresh without setting the zoom to because user might be looking around
                new Timer().scheduleAtFixedRate(new MapRefreshTask(), 0, 30000); // time in milliseconds 30000 = 30 seconds
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_event_map_imageRefresh:
                setMap(); // Call set map again to refresh the map

                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // set style for map
        final MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.google_map_json);
        googleMap.setMapStyle(style);
        googleMap.setOnInfoWindowClickListener(this);
        fetchEvents();
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        int eventId = eventMarkerMap.get(marker).getEvent_id();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(eventMarkerMap.get(marker).getRest_name());
        dialog.setMessage(eventMarkerMap.get(marker).getAddress() + "\n" + HelperEvent.getDate(eventMarkerMap.get(marker).getEvent_time() * 1000));

        dialog.setPositiveButton("JOIN",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mSubscriptions != null) {
                    // Join an event

                    mSubscriptions.add(RetrofitEventSingleton.getInstance()
                            .joinEvent().joinEvent(new EventParam(userLocalStore.getLoggedInUser().getUserID(),
                                    eventMarkerMap.get(marker).getEvent_id(), eventMarkerMap.get(marker).getRest_id()))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Observer<Response>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(Response response) {

                                    Helpers.displayToast(EventMapActivity.this, response.getMessage());
                                    //onEventJoinedInterface.onEventJoined(); // refresh drawer in main

                                    if (!response.getMessage().equals("Event already joined")) {
                                        mSubscriptions.add(RetrofitChatSingleton.getInstance()
                                                .insertChat()
                                                .insertChat(new NewChatParam(userLocalStore.getLoggedInUser().getUserID(), eventMarkerMap.get(marker).getEvent_id()))
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
                                                        DatabaseOperations databaseOperations = new DatabaseOperations(EventMapActivity.this);
                                                        databaseOperations.insertActivity(databaseOperations, Long.toString(System.currentTimeMillis()), "You joined a new event.");
                                                    }
                                                }));
                                    }
                                }
                            }));
                }


            }
        });
        dialog.setNeutralButton("NAVIGATE",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String addressURL = "http://maps.google.com/maps?daddr=" +  eventMarkerMap.get(marker).getLat() + "," + eventMarkerMap.get(marker).getLng();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(addressURL));
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        dialog.show();
    }



    private void setMap() {
        // Set map fragment on the layout
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_user_map_mapFrag);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapFragment.getMapAsync(EventMapActivity.this);
            }
        });
      //  googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    /**
     * Fetch a list of events created
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
                        eventMarkerMap = new HashMap<>();
                        eventList = events;
                        addMarker(events);
                    }
                }));
    }

    /**
     * Draw events on the map based on latitude and longitude
     * @param eventList                 - list of events
     * TODO: ONLY FETCH NEARBY EVENTS
     */
    private void addMarker(List<Event> eventList) {
        if (eventList.get(0).getEvent_id() != 0) {

            for (Event event : eventList) {
                MarkerOptions markerOpt = new MarkerOptions();
                //System.out.println("name = " + event.getRest_name() + ", url = " + event.getRest_image());

                markerOpt.position(new LatLng(event.getLat(), event.getLng()))
                        .title(event.getRest_name())
                        .snippet(event.getAddress() + "\n" + HelperEvent.getDate(event.getEvent_time() * 1000))
                        .alpha(0.7f);

                //Set Custom InfoWindow Adapter
                Log.e(EventMapActivity.class.getSimpleName(), "URL: " + event.getRestaurantImage());
                eventMarkerMap.put(googleMap.addMarker(markerOpt), event);

                restaurantImages.put(event.getRest_name(), event.getRestaurantImage()); // name associate with image
            }

            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(this);
            googleMap.setInfoWindowAdapter(adapter);

            // Make the map zoom focus to the first location. This will show the surrounding events also
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocalStore.getLatitude(),
                    userLocalStore.getLongitude()), ZOOM_LEVEL));
        }
    }

    public HashMap<String, String> getRestaurantImages() {
        return this.restaurantImages;
    }

    public class MapRefreshTask extends TimerTask {
        @Override
        public void run() {
            setMap();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

}

