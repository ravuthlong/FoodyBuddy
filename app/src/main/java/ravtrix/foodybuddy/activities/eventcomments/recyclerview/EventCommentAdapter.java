package ravtrix.foodybuddy.activities.eventcomments.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 2/9/17.
 */

public class EventCommentAdapter extends RecyclerView.Adapter<EventCommentAdapter.ViewHolder> {

    private List<EventCommentModel> eventCommentModelList;
    private Context context;
    private LayoutInflater inflater;

    public EventCommentAdapter(Context context, List<EventCommentModel> eventCommentModels) {
        this.eventCommentModelList = eventCommentModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventCommentAdapter.ViewHolder(inflater.inflate(R.layout.item_event_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        EventCommentModel currentItem = eventCommentModelList.get(position);

        // load profile image
        Picasso.with(context)
                .load(currentItem.getUserImage())
                .centerCrop()
                .fit()
                .into(holder.profileImage);
        holder.tvUsername.setText(currentItem.getUsername());
        holder.tvTime.setText(currentItem.getTime());
        holder.tvComment.setText(currentItem.getComment());
    }

    @Override
    public int getItemCount() {
        return eventCommentModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private TextView tvUsername, tvTime, tvComment;
        private RelativeLayout relativeUser, relativeComment;

        public ViewHolder(View itemView) {
            super(itemView);

            profileImage = (CircleImageView) itemView.findViewById(R.id.item_event_comment_profileimage);
            tvUsername = (TextView) itemView.findViewById(R.id.item_event_comment_tvUsername);
            tvTime = (TextView) itemView.findViewById(R.id.item_event_comment_tvTime);
            tvComment = (TextView) itemView.findViewById(R.id.item_event_comment_tvComment);
            relativeUser = (RelativeLayout) itemView.findViewById(R.id.item_event_comment_relativeUserInfo);
            relativeComment = (RelativeLayout) itemView.findViewById(R.id.item_event_comment_relative_comment);

            Helpers.overrideFonts(context, relativeUser);
            Helpers.overrideFonts(context, relativeComment);
        }
    }
}
