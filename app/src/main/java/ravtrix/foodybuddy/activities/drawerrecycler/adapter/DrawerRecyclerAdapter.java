package ravtrix.foodybuddy.activities.drawerrecycler.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.mainpage.model.EventJoined;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/30/17.
 */

public class DrawerRecyclerAdapter extends RecyclerView.Adapter<DrawerRecyclerAdapter.ViewHolder> {

    private List<EventJoined> eventModelList;
    private Context context;
    private LayoutInflater inflater;
    private boolean isEditEventClicked = false;

    public DrawerRecyclerAdapter(Context context, List<EventJoined> eventModels) {
        this.eventModelList = eventModels;
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
        EventJoined currentItem = eventModelList.get(position);

        if (isEditEventClicked) {
            holder.imageRemove.setVisibility(View.VISIBLE);
        } else {
            holder.imageRemove.setVisibility(View.GONE);
        }
        // populate with list data
        holder.tvRestaurantName.setText(currentItem.getRest_name());
        holder.tvTime.setText(getDate(currentItem.getEvent_time() * 1000));
        holder.tvAddress.setText(currentItem.getAddress());
    }

    @Override
    public int getItemCount() {
        return eventModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tvRestaurantName, tvTime, tvAddress;
        private LinearLayout mainLinear, layoutCancelEvent, layoutRestaurant;
        private CircleImageView imageRemove;

        ViewHolder(View itemView) {
            super(itemView);

            tvRestaurantName = (TextView) itemView.findViewById(R.id.item_drawer_tvRestaurant);
            tvTime = (TextView) itemView.findViewById(R.id.item_drawer_tvTime);
            tvAddress = (TextView) itemView.findViewById(R.id.item_drawer_tvAddress);
            mainLinear = (LinearLayout) itemView.findViewById(R.id.item_drawer_mainLinear);
            layoutRestaurant = (LinearLayout) itemView.findViewById(R.id.item_drawer_recycler_layoutRestaurant);
            layoutCancelEvent = (LinearLayout) itemView.findViewById(R.id.item_drawer_recycler_layoutCancelEvent);
            imageRemove = (CircleImageView) itemView.findViewById(R.id.item_drawer_recycler_imgDelete);

            Helpers.overrideFonts(context, mainLinear);

            mainLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventJoined currentItem = eventModelList.get(getAdapterPosition());
                    Helpers.displayToast(context, "Clicked On: " + currentItem.getRest_name());

                }
            });

            layoutCancelEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventJoined currentItem = eventModelList.get(getAdapterPosition());
                    showOptionDialog(currentItem.getRest_name());
                }
            });

        }
    }

    public void setEditEventClicked(boolean isEditEventClicked) {
        this.isEditEventClicked = isEditEventClicked;
        notifyDataSetChanged();
    }

    private void showOptionDialog(String restaurantName) {

        AlertDialog.Builder optionDialog = Helpers.showAlertDialogWithTwoOptions((Activity) context,
                "Cancel " + restaurantName + " event",
                "Are you sure you want to cancel this event?", "No");

        optionDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Helpers.displayToast(context, "You clicked cancelled.... :/");
            }
        });
        optionDialog.show();
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("MM-dd-yyyy hh:mm a", cal).toString();
    }

    public void swap(List<EventJoined> models) {
        this.eventModelList.clear();
        this.eventModelList.addAll(models);
        this.notifyDataSetChanged();
    }
}



