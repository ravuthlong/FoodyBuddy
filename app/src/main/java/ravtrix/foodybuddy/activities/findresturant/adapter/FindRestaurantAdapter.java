package ravtrix.foodybuddy.activities.findresturant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.findresturant.model.RestaurantModel;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/31/17.
 */

public class FindRestaurantAdapter extends RecyclerView.Adapter<FindRestaurantAdapter.ViewHolder> {

    private List<RestaurantModel> restaurantModels;
    private LayoutInflater inflater;
    private Activity activity;

    public FindRestaurantAdapter(Activity activity, List<RestaurantModel> restaurantModels) {
        this.activity = activity;
        this.restaurantModels = restaurantModels;
        this.inflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FindRestaurantAdapter.ViewHolder(inflater.inflate(R.layout.item_restaurant, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RestaurantModel currentItem = restaurantModels.get(position);

        // Set star icons based on the restaurant's rating
        setStarIcons(currentItem, holder);

        if (!currentItem.getImage_url().isEmpty()) {
            Picasso.with(activity)
                    .load(currentItem.getImage_url())
                    .centerCrop()
                    .fit()
                    .into(holder.image);
        }

        holder.restaurantName.setText(currentItem.getName());
        holder.address.setText(currentItem.getAddress());
        holder.details.setText(currentItem.getCategories());
    }

    @Override
    public int getItemCount() {
        return restaurantModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image, star1, star2, star3, star4, star5;
        private TextView restaurantName, address, details;
        private LinearLayout textLayout;
        private RelativeLayout itemRestaurantRelative;

        ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.item_restaurant_image);
            restaurantName = (TextView) itemView.findViewById(R.id.item_restaurant_name);
            address = (TextView) itemView.findViewById(R.id.item_restaurant_adress);
            details = (TextView) itemView.findViewById(R.id.item_restaurant_details);
            textLayout = (LinearLayout) itemView.findViewById(R.id.item_restaurant_textLayout);
            star1 = (ImageView) itemView.findViewById(R.id.item_restaurant_star1);
            star2 = (ImageView) itemView.findViewById(R.id.item_restaurant_star2);
            star3 = (ImageView) itemView.findViewById(R.id.item_restaurant_star3);
            star4 = (ImageView) itemView.findViewById(R.id.item_restaurant_star4);
            star5 = (ImageView) itemView.findViewById(R.id.item_restaurant_star5);
            itemRestaurantRelative = (RelativeLayout) itemView.findViewById(R.id.item_restaurant_relativeMain);

            Helpers.overrideFonts(activity, textLayout);

            itemRestaurantRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    RestaurantModel currentItem = restaurantModels.get(getAdapterPosition());

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("id", currentItem.getId());
                    resultIntent.putExtra("name", currentItem.getName());
                    resultIntent.putExtra("address", currentItem.getAddress());
                    resultIntent.putExtra("longitude", currentItem.getLongitude());
                    resultIntent.putExtra("latitude", currentItem.getLatitude());

                    activity.setResult(Activity.RESULT_OK, resultIntent);
                    activity.finish();
                }
            });
        }
    }

    private void setStarIcons(RestaurantModel currentItem, ViewHolder holder) {

        switch (Double.toString(currentItem.getRating())) {
            case "0.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                break;
            case "1.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.one));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                break;
            case "1.5":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.one));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.onepointfive));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                break;
            case "2.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.two));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.two));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                break;
            case "2.5":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.two));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.two));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.twopointfive));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                break;
            case "3.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.three));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.three));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.three));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                break;
            case "3.5":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.three));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.three));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.three));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.threepointfive));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                break;
            case "4.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.four));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.four));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.four));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.four));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.zero));
                break;
            case "4.5":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.four));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.four));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.four));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.four));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.fourpointfive));
                break;
            case "5.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.five));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.five));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.five));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.five));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.five));
                break;
        }
    }
}
