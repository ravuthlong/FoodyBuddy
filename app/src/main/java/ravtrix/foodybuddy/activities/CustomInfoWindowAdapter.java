package ravtrix.foodybuddy.activities;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import ravtrix.foodybuddy.R;

/**
 * Created by Emily on 6/10/17.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    private String image_url = "http://basera-dfw.com/wp-content/uploads/2016/03/restaurant.jpeg";
    boolean not_first_time_showing_info_window;


    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        ImageView image = (ImageView) view.findViewById(R.id.iv_image);
        if (not_first_time_showing_info_window) {
            Picasso.with(context)
                    .load(image_url)
                    .into(image);
        } else {
            not_first_time_showing_info_window = true;
            Picasso.with(context).load(image_url).into(image, new InfoWindowRefresher(marker));
         //   Picasso.with(MainActivity.this).load("image_URL.png").into(image, new InfoWindowRefresher(marker));
        }
        Picasso.with(context)
                .load(image_url)
                .into(image);

        tvTitle.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());
     //   image.set


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


