package ravtrix.foodybuddy.fragments.inbox.recyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.chat.ChatActivity;
import ravtrix.foodybuddy.model.Image;
import ravtrix.foodybuddy.network.networkresponse.ChatResponse;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/28/17.
 */

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    private List<ChatResponse> inboxModelList;
    private Context context;
    private LayoutInflater inflater;

    public InboxAdapter(Context context, List<ChatResponse> inboxModels) {
        this.context = context;
        this.inboxModelList = inboxModels;
        if (null != context) inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_inbox, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ChatResponse currentItem = inboxModelList.get(position);

        Picasso.with(context)
                .load(currentItem.getCreator_pic())
                .centerCrop()
                .fit()
                .into(holder.profileImage);

        if (!currentItem.getRest_image().equals("https://imgur.com/6Jm6IwC") && !TextUtils.isEmpty(currentItem.getRest_image())) {
            Picasso.with(context)
                    .load(currentItem.getRest_image())
                    .centerCrop()
                    .fit()
                    .into(holder.restImage);
        } else {
            Picasso.with(context)
                    .load("https://atmedia.imgix.net/025374fc4e6d728bc6d9f7861c9311f34a6d0f08?auto=format&q=45&w=600.0&h=800.0&fit=max&cs=strip")
                    .centerCrop()
                    .fit()
                    .into(holder.restImage);
        }

        if (!TextUtils.isEmpty(currentItem.getEventName())) {
            holder.username.setText(currentItem.getEventName());
        } else {
            holder.username.setText("Empty event name");
        }

        if (!TextUtils.isEmpty(currentItem.getLatestDate())) {
            // Converting timestamp into x ago format
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(currentItem.getLatestDate()),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

            holder.time.setText(timeAgo);
        } else {
            holder.time.setText("2 hours ago");
        }
        holder.message.setText(currentItem.getLatest_message());
    }

    @Override
    public int getItemCount() {
        return inboxModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileImage;
        private CircleImageView restImage;
        private TextView username, time, message;
        private LinearLayout inboxLinear;

        ViewHolder(View itemView) {
            super(itemView);

            profileImage = (ImageView) itemView.findViewById(R.id.item_inboxFeed_profileImage);
            restImage = (CircleImageView) itemView.findViewById(R.id.item_inboxFeed_restImage);
            username = (TextView) itemView.findViewById(R.id.item_inboxFeed_username);
            time = (TextView) itemView.findViewById(R.id.item_inboxFeed_time);
            message = (TextView) itemView.findViewById(R.id.item_inboxFeed_lastMessage);
            inboxLinear = (LinearLayout) itemView.findViewById(R.id.item_inboxFeed_linearLayout);

            Helpers.overrideFonts(context, inboxLinear);

            inboxLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("eventID", inboxModelList.get(getAdapterPosition()).getEvent_id()); // pass event id, chat name
                    intent.putExtra("eventName", inboxModelList.get(getAdapterPosition()).getEventName()); // pass event id, chat name
                    context.startActivity(intent);
                }
            });

        }
    }

    private void showToast(String text) {
        Helpers.displayToast(context, text);
    }
}
