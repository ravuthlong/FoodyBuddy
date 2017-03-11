package ravtrix.foodybuddy.fragments.deals.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        holder.url.setText(currentItem.getUrl());
    }

    @Override
    public int getItemCount () {
        return dealsModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView restaurantName, address, dealDescription, url;
        private LinearLayout dealsLinear;

        ViewHolder(View itemView) {
            super(itemView);

            restaurantName = (TextView) itemView.findViewById(R.id.item_deal_tvRestaurantName);
            dealDescription = (TextView) itemView.findViewById(R.id.item_deal_tvDescription);
            address = (TextView) itemView.findViewById(R.id.item_deal_tvAddress);
            url = (TextView) itemView.findViewById(R.id.item_deal_tvUrl);
            dealsLinear = (LinearLayout) itemView.findViewById(R.id.item_deal_linearAll);

            Helpers.overrideFonts(context, dealsLinear);

            //profileImage.setOnClickListener(view -> showToast("Clicked to view profile"));
            //inboxLinear.setOnClickListener(view -> showToast("Clicked to view message"));

        }
    }
    private void showToast(String text) {
        Helpers.displayToast(context, text);
    }


}