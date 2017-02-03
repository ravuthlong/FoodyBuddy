package ravtrix.foodybuddy.activities.drawerrecycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.drawerrecycler.model.DrawerModel;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/30/17.
 */

public class DrawerRecyclerAdapter extends RecyclerView.Adapter<DrawerRecyclerAdapter.ViewHolder> {

    private List<DrawerModel> drawerModelList;
    private Context context;
    private LayoutInflater inflater;

    public DrawerRecyclerAdapter(Context context, List<DrawerModel> drawerModels) {
        this.drawerModelList = drawerModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_drawer_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // get current item of viewholder
        DrawerModel currentItem = drawerModelList.get(position);

        // populate with list data
        holder.tvRestaurantName.setText(currentItem.getRestaurantName());
        holder.tvTime.setText(currentItem.getTime());
        holder.tvAddress.setText(currentItem.getAddress());
    }

    @Override
    public int getItemCount() {
        return drawerModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRestaurantName, tvTime, tvAddress;
        private LinearLayout mainLinear;

        ViewHolder(View itemView) {
            super(itemView);

            tvRestaurantName = (TextView) itemView.findViewById(R.id.item_drawer_tvRestaurant);
            tvTime = (TextView) itemView.findViewById(R.id.item_drawer_tvTime);
            tvAddress = (TextView) itemView.findViewById(R.id.item_drawer_tvAddress);
            mainLinear = (LinearLayout) itemView.findViewById(R.id.item_drawer_mainLinear);

            Helpers.overrideFonts(context, mainLinear);

            mainLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DrawerModel currentItem = drawerModelList.get(getAdapterPosition());
                    Helpers.displayToast(context, "Clicked On: " + currentItem.getRestaurantName());

                }
            });
        }
    }
}
