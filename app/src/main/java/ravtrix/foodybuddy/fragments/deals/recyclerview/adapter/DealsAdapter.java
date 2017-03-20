package ravtrix.foodybuddy.fragments.deals.recyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.fragments.deals.recyclerview.model.DealsModel;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Emily on 3/9/17.
 */

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {
    private List<DealsModel> dealsModelList;
    private LayoutInflater inflater;
    private Context context;
    private boolean firstOfList = true;

    public DealsAdapter(Context context, List<DealsModel> dealsModels) {
        this.context = context;
        this.dealsModelList = dealsModels;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_deals, parent, false));
    }

    @Override
    public void onBindViewHolder (DealsAdapter.ViewHolder holder,int position){
        DealsModel currentItem = dealsModelList.get(position);
        holder.restaurantName.setText(currentItem.getRestaurantName());
        holder.address.setText(currentItem.getAddress());
        holder.dealDescription.setText(currentItem.getDealDescription());
        holder.dealTitle.setText(currentItem.getDealTitle());

        Picasso.with(context)
                .load(currentItem.getImageURL())
                .centerCrop()
                .fit()
                .into(holder.imgRestaurant);
        //holder.url.setText(currentItem.getUrl());
    }

    @Override
    public int getItemCount () {
        return dealsModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView restaurantName, address, dealDescription, url, dealTitle;
        private ImageView imgRestaurant;
        private RelativeLayout dealsRelative;

        ViewHolder(View itemView) {
            super(itemView);

            dealTitle = (TextView) itemView.findViewById(R.id.item_deal_tvDealTitle);
            restaurantName = (TextView) itemView.findViewById(R.id.item_deal_tvRestaurantName);
            dealDescription = (TextView) itemView.findViewById(R.id.item_deal_tvDescription);
            address = (TextView) itemView.findViewById(R.id.item_deal_tvAddress);
            imgRestaurant = (ImageView) itemView.findViewById(R.id.item_deals_imgRestaurant);
            dealsRelative = (RelativeLayout) itemView.findViewById(R.id.item_deal_linearAll);
            //url = (TextView) itemView.findViewById(R.id.item_deal_tvUrl);

            dealsRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = getAdapterPosition();
                    DealsModel currentModel = dealsModelList.get(currentPosition);

                    Uri uri = Uri.parse(currentModel.getUrl()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });

            Helpers.overrideFonts(context, dealsRelative);
        }
    }
}
