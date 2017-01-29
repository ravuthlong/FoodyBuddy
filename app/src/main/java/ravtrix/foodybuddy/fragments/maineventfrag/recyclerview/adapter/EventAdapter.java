package ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.EventModel;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/28/17.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EventModel> eventModelList;
    private LayoutInflater inflater;
    private Context context;
    private boolean firstOfList = true;

    public EventAdapter(Context context, List<EventModel> eventModels) {
        this.context = context;
        this.eventModelList = eventModels;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder1(inflater.inflate(R.layout.item_event_first_item, parent, false));
            case 1:
                return new ViewHolder2(inflater.inflate(R.layout.item_event, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                Picasso.with(context)
                        .load("http://orig11.deviantart.net/7bbd/f/2013/331/2/c/team_crafted_style_profile_picture__shadowvenom718_by_shadowvenom718-d6vuqot.png")
                        .centerCrop()
                        .fit()
                        .into(viewHolder1.profileImage);
                break;
            case 1:

                /**
                 * Because the first position 0 is "current user's event post" (unique), we need to start reading
                 * from array list only when position is 1. This is why care 1 reads from position 0.
                 */
                EventModel currentItem;
                if (firstOfList) {
                    currentItem = eventModelList.get(0);
                    firstOfList = false;
                } else {
                    currentItem = eventModelList.get(position - 1);
                }
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.restaurantName.setText(currentItem.getRestaurantName());
                viewHolder2.postDate.setText(currentItem.getPostTime());
                viewHolder2.eventDate.setText(currentItem.getEventTime());
                viewHolder2.description.setText(currentItem.getEventDescription());
                viewHolder2.address.setText(currentItem.getAddress());
                viewHolder2.numComment.setText(Integer.toString(currentItem.getNumComment()));

                Picasso.with(context)
                        .load("http://gurucul.com/wp-content/uploads/2015/01/default-user-icon-profile.png")
                        .centerCrop()
                        .fit()
                        .into(viewHolder2.profileImage);
                break;
            default:
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0; // first item unique
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        // size is + 1 because first item is not using our array list.
        return eventModelList.size() + 1;
    }


    /**
     * The first item of the recycler view is unique. It is responsible for taking user input for event page
     */
    private class ViewHolder1 extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private EditText etDescription;
        private RelativeLayout relativeLayout;

        ViewHolder1(View itemView) {
            super(itemView);

            profileImage = (CircleImageView) itemView.findViewById(R.id.item_eventfirst_profileImage);
            etDescription = (EditText) itemView.findViewById(R.id.item_eventfirst_etDescription);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_eventfirst_relativePost);

            Helpers.overrideFonts(context, relativeLayout);
        }
    }

    /**
     * The rest of the items of recycler view are the same. They display the nearby events
     */
    private class ViewHolder2 extends RecyclerView.ViewHolder {

        private TextView restaurantName, postDate, eventDate, description, address, numComment;
        private LinearLayout layoutComment, layoutJoin, layoutMore;
        private RelativeLayout relativeAll;
        private CircleImageView profileImage;

        ViewHolder2(View itemView) {
            super(itemView);

            restaurantName = (TextView) itemView.findViewById(R.id.item_event_tvRestaurantName);
            postDate = (TextView) itemView.findViewById(R.id.item_event_tvPostTime);
            eventDate = (TextView) itemView.findViewById(R.id.item_event_eventTime);
            description = (TextView) itemView.findViewById(R.id.item_event_tvDescription);
            address = (TextView) itemView.findViewById(R.id.item_event_tvAddress);
            numComment = (TextView) itemView.findViewById(R.id.item_event_tvCommentNum);
            layoutComment = (LinearLayout) itemView.findViewById(R.id.item_event_layoutComment);
            layoutJoin = (LinearLayout) itemView.findViewById(R.id.item_event_layoutJoin);
            layoutMore = (LinearLayout) itemView.findViewById(R.id.item_event_layoutMore);
            profileImage = (CircleImageView) itemView.findViewById(R.id.item_event_profileImage);
            relativeAll = (RelativeLayout) itemView.findViewById(R.id.item_event_relativeAll);

            Helpers.overrideFonts(context, relativeAll);
        }
    }
}
