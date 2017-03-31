package ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.createevent.CreateEventActivity;
import ravtrix.foodybuddy.activities.eventcomments.EventCommentsActivity;
import ravtrix.foodybuddy.activities.otheruserprofile.OtherUserProfileActivity;
import ravtrix.foodybuddy.fragments.maineventfrag.MainEventFrag;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.Event;
import ravtrix.foodybuddy.utils.Constants;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/28/17.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Event> eventModelList;
    private LayoutInflater inflater;
    private Fragment fragment;
    private boolean firstOfList = true;

    public EventAdapter(Fragment fragment, List<Event> eventModels) {
        this.fragment = fragment;
        this.eventModelList = eventModels;
        inflater = LayoutInflater.from(fragment.getActivity());
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
                Picasso.with(fragment.getActivity())
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
                Event currentItem;
                if (firstOfList) {
                    currentItem = eventModelList.get(0);
                    firstOfList = false;
                } else {
                    currentItem = eventModelList.get(position - 1);
                }
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.restaurantName.setText(currentItem.getRest_name());

                // Converting timestamp into x ago format
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                        currentItem.getCreate_time() * 1000,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                viewHolder2.postDate.setText(timeAgo);
                viewHolder2.eventDate.setText(getDate(currentItem.getEvent_time() * 1000));
                viewHolder2.description.setText(currentItem.getEvent_des());
                viewHolder2.address.setText(currentItem.getAddress());
                viewHolder2.numComment.setText("2");

                if (!currentItem.getOwnerImage().isEmpty()) {
                    Picasso.with(fragment.getActivity())
                            .load(currentItem.getOwnerImage())
                            .centerCrop()
                            .fit()
                            .into(viewHolder2.profileImage);
                } else {
                    viewHolder2.profileImage.setVisibility(View.GONE);
                }/*
                if (!currentItem.getUserImage1().isEmpty()) {
                    Picasso.with(context)
                            .load(currentItem.getUserImage1())
                            .centerCrop()
                            .fit()
                            .into(viewHolder2.otherUser1);
                } else {
                    viewHolder2.otherUser1.setVisibility(View.GONE);
                }
                if (!currentItem.getUserImage2().isEmpty()) {

                    Picasso.with(context)
                            .load(currentItem.getUserImage2())
                            .centerCrop()
                            .fit()
                            .into(viewHolder2.otherUser2);
                } else {
                    viewHolder2.otherUser2.setVisibility(View.GONE);
                }
                if (!currentItem.getUserImage3().isEmpty()) {

                    Picasso.with(context)
                            .load(currentItem.getUserImage3())
                            .centerCrop()
                            .fit()
                            .into(viewHolder2.otherUser3);
                } else {
                    viewHolder2.otherUser3.setVisibility(View.GONE);
                }
                if (!currentItem.getUserImage4().isEmpty()) {

                    Picasso.with(context)
                            .load(currentItem.getUserImage4())
                            .centerCrop()
                            .fit()
                            .into(viewHolder2.otherUser4);
                } else {
                    viewHolder2.otherUser4.setVisibility(View.GONE);
                }*/
                Picasso.with(fragment.getActivity())
                        .load("https://viralplots.com/wp-content/uploads/2015/12/angelina.jpeg")
                        .centerCrop()
                        .fit()
                        .into(viewHolder2.otherUser1);
                Picasso.with(fragment.getActivity())
                        .load("http://cdn.hanoitimes.com.vn/mfiles/data/2014/04/81E07AB1/yeojingoo-photo-3-400x600.jpg")
                        .centerCrop()
                        .fit()
                        .into(viewHolder2.otherUser2);
                Picasso.with(fragment.getActivity())
                        .load("https://www.cdc.gov/features/dog-bite-prevention/dog-bite-prevention_456px.jpg")
                        .centerCrop()
                        .fit()
                        .into(viewHolder2.otherUser3);
                Picasso.with(fragment.getActivity())
                        .load("https://s-media-cache-ak0.pinimg.com/236x/75/bd/b9/75bdb9bf23175ec74f86cec6554791b5.jpg")
                        .centerCrop()
                        .fit()
                        .into(viewHolder2.otherUser4);
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
        private TextView etDescription;
        private RelativeLayout relativeLayoutNewPost;

        ViewHolder1(View itemView) {
            super(itemView);

            profileImage = (CircleImageView) itemView.findViewById(R.id.item_eventfirst_profileImage);
            etDescription = (TextView) itemView.findViewById(R.id.item_eventfirst_etDescription);
            relativeLayoutNewPost = (RelativeLayout) itemView.findViewById(R.id.item_eventfirst_relativePost);

            Helpers.overrideFonts(fragment.getActivity(), relativeLayoutNewPost);

            relativeLayoutNewPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // start new event post
                    fragment.startActivityForResult(new Intent(fragment.getActivity(), CreateEventActivity.class), Constants.NEW_EVENT_REQUEST_CODE);
                }
            });

        }
    }

    /**
     * The rest of the items of recycler view are the same. They display the nearby events
     */
    private class ViewHolder2 extends RecyclerView.ViewHolder {

        private TextView restaurantName, postDate, eventDate, description, address, numComment;
        private LinearLayout layoutComment, layoutJoin, layoutMore, layoutDrive;
        private RelativeLayout relativeAll;
        private CircleImageView otherUser1, otherUser2, otherUser3, otherUser4;
        private ImageView profileImage;

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
            layoutDrive = (LinearLayout) itemView.findViewById(R.id.item_event_layoutDrive);
            layoutMore = (LinearLayout) itemView.findViewById(R.id.item_event_layoutMore);
            profileImage = (ImageView) itemView.findViewById(R.id.item_event_profileImage);
            relativeAll = (RelativeLayout) itemView.findViewById(R.id.item_event_relativeAll);
            otherUser1 = (CircleImageView) itemView.findViewById(R.id.item_event_otherUser1);
            otherUser2 = (CircleImageView) itemView.findViewById(R.id.item_event_otherUser2);
            otherUser3 = (CircleImageView) itemView.findViewById(R.id.item_event_otherUser3);
            otherUser4 = (CircleImageView) itemView.findViewById(R.id.item_event_otherUser4);

            Helpers.overrideFonts(fragment.getActivity(), relativeAll);

            layoutJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    Event clickedItem = eventModelList.get(position - 1);

                    if (fragment instanceof MainEventFrag) {
                        // Invoke main fragment to invoke event insert
                        ((MainEventFrag) fragment).joinEventRetrofit(clickedItem.getEvent_id(), clickedItem.getRest_id());
                    }

                    /*
                    Snackbar snackbar = Snackbar
                            .make(relativeAll, "Joined. Owner has been notified.", Snackbar.LENGTH_SHORT);
                    View mView = snackbar.getView();
                    TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    } else {
                        mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                    snackbar.show();*/

                }
            });

            relativeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helpers.displayToast(fragment.getActivity(), "Clicked on background to see restaurant info");
                }
            });


            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.startActivity(new Intent(fragment.getActivity(), OtherUserProfileActivity.class));
                }
            });

            layoutComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Event clickedItem = eventModelList.get(position - 1);

                    Intent intent = new Intent(fragment.getActivity(), EventCommentsActivity.class);
                    intent.putExtra("event_id", clickedItem.getEvent_id());
                    fragment.startActivity(intent);
                }
            });

            layoutDrive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helpers.displayToast(fragment.getActivity(), "Clicked on drive");
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=37.416936,-121.890564"));
                    fragment.startActivity(intent);
                }
            });

            layoutMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helpers.displayToast(fragment.getActivity(), "Clicked on more options");
                }
            });


        }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("MM-dd-yyyy hh:mm a", cal).toString();
    }

    public void swap(List<Event> models) {
        this.eventModelList.clear();
        this.eventModelList.addAll(models);
        this.notifyDataSetChanged();
    }
}
