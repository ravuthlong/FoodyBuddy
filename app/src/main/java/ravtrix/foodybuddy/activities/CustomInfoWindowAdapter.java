package ravtrix.foodybuddy.activities;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ravtrix.foodybuddy.R;

/**
 * Created by Emily on 6/10/17.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity context;

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


        tvTitle.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());
     //   image.set

        return view;
    }
}
