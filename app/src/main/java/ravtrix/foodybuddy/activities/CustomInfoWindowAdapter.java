package ravtrix.foodybuddy.activities;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.eventmap.EventMapActivity;

/**
 * Created by Emily on 6/10/17.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private EventMapActivity activity;

    public CustomInfoWindowAdapter(EventMapActivity activity){
        this.activity = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View view = activity.getLayoutInflater().inflate(R.layout.custom_info_window, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        final ImageView image = (ImageView) view.findViewById(R.id.iv_image);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.with(activity).load(activity.getRestaurantImages().get(marker.getTitle())).into(image);
            }
        });
        tvTitle.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());

        return view;
    }


    private class InfoWindowRefresher implements com.squareup.picasso.Callback {
        private Marker markerToRefresh;

        private InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            markerToRefresh.showInfoWindow();
        }

        @Override
        public void onError() {}
    }
}


