package ravtrix.foodybuddy.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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

import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.Event;
import ravtrix.foodybuddy.utils.RetrofitEventSingleton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ravinder on 5/12/17.
 */

public class EventMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap googleMap;
    private List<Event> eventList;
    private CompositeSubscription mSubscriptions;
    private HashMap<Marker, Event> eventMarkerMap;

    // lat and lng of san jose
    double lat = 37.279518;
    double lng = -121.867905;

    // marker display range
    double range = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscriptions = new CompositeSubscription();
        Log.e("testLog", "testtest");
        setContentView(R.layout.activity_event_map);
        setMap();
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
    public void onInfoWindowClick(Marker marker) {
        int eventId = eventMarkerMap.get(marker).getEvent_id();
        String dateString = new SimpleDateFormat("mm/dd/yyyy").format(new Date(eventMarkerMap.get(marker).getEvent_time()));
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(eventMarkerMap.get(marker).getRest_name());
        dialog.setMessage(eventMarkerMap.get(marker).getAddress() + "\n" + dateString);

        dialog.setPositiveButton("JOIN",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked JOIN button

            }
        });
        dialog.setNeutralButton("NAVIGATE",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked NAVIGATE button

            }
        });
        dialog.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked CANCEL button

            }
        });

        dialog.show();

        Toast.makeText(this, "event id: "+ eventId,
                Toast.LENGTH_SHORT).show();
    }



    private void setMap() {
        // Set map fragment on the layout
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_user_map_mapFrag);
        mapFragment.getMapAsync(this);
       // googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return true;
    }

    /**
     * Ftech a list of events created
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
                if(event.getLng() <= lng + range && event.getLng() >= lng - range && event.getLat() <= lat + range && event.getLat() >= lat - range) {
                    eventMarkerMap.put(googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(event.getLat(), event.getLng()))
                                    .title(event.getRest_name())
                                    .snippet(event.getAddress())
                                    .alpha(0.7f)), event);
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
