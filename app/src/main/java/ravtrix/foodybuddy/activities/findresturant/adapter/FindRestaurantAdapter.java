package ravtrix.foodybuddy.activities.findresturant.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private Context context;
    private LayoutInflater inflater;

    public FindRestaurantAdapter(Context context, List<RestaurantModel> restaurantModels) {
        this.context = context;
        this.restaurantModels = restaurantModels;
        this.inflater = LayoutInflater.from(context);
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
            Picasso.with(context)
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

            Helpers.overrideFonts(context, textLayout);
        }
    }

    private void setStarIcons(RestaurantModel currentItem, ViewHolder holder) {

        switch (Double.toString(currentItem.getRating())) {
            case "0.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                break;
            case "1.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.one));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                break;
            case "1.5":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.one));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.onepointfive));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                break;
            case "2.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.two));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.two));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                break;
            case "2.5":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.two));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.two));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.twopointfive));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                break;
            case "3.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.three));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.three));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.three));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                break;
            case "3.5":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.three));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.three));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.three));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.threepointfive));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                break;
            case "4.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.four));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.four));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.four));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.four));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zero));
                break;
            case "4.5":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.four));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.four));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.four));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.four));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fourpointfive));
                break;
            case "5.0":
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.five));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.five));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.five));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.five));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.five));
                break;
        }
    }
}
