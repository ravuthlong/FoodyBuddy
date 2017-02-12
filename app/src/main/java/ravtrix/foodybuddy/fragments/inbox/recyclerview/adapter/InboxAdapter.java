package ravtrix.foodybuddy.fragments.inbox.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.fragments.inbox.recyclerview.model.InboxModel;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/28/17.
 */

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    private List<InboxModel> inboxModelList;
    private Context context;
    private LayoutInflater inflater;

    public InboxAdapter(Context context, List<InboxModel> inboxModels) {
        this.context = context;
        this.inboxModelList = inboxModels;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_inbox, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        InboxModel currentItem = inboxModelList.get(position);
        Picasso.with(context)
                .load(currentItem.getImageURL())
                .centerCrop()
                .fit()
                .into(holder.profileImage);

        holder.username.setText(currentItem.getUsername());
        holder.time.setText(currentItem.getTime());
        holder.message.setText(currentItem.getMessage());
    }

    @Override
    public int getItemCount() {
        return inboxModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private TextView username, time, message;
        private LinearLayout inboxLinear;

        ViewHolder(View itemView) {
            super(itemView);

            profileImage = (CircleImageView) itemView.findViewById(R.id.item_inboxFeed_profileImage);
            username = (TextView) itemView.findViewById(R.id.item_inboxFeed_username);
            time = (TextView) itemView.findViewById(R.id.item_inboxFeed_time);
            message = (TextView) itemView.findViewById(R.id.item_inboxFeed_lastMessage);
            inboxLinear = (LinearLayout) itemView.findViewById(R.id.item_inboxFeed_linearLayout);

            Helpers.overrideFonts(context, inboxLinear);

            //profileImage.setOnClickListener(view -> showToast("Clicked to view profile"));
            //inboxLinear.setOnClickListener(view -> showToast("Clicked to view message"));

        }
    }

    private void showToast(String text) {
        Helpers.displayToast(context, text);
    }
}
