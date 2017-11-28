package ravtrix.foodybuddy.fragments.eventprofilefrag.recycylerviewtimeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import ravtrix.foodybuddy.R;

/**
 * Created by rlong on 11/8/17.
 */

public class EventActivityAdapter extends RecyclerView.Adapter<EventActivityAdapter.TimeLineViewHolder> {

    private List<ActivityModel> activityModelList;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public EventActivityAdapter(Context context, List<ActivityModel> activityModels) {
        this.context = context;
        this.activityModelList = activityModels;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        ActivityModel currentItem = activityModelList.get(position);

        holder.item_timeline_detail.setText(currentItem.getActivity_detail());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(currentItem.getActivity_time()), // * 1000?
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.item_timeline_time.setText(timeAgo);

    }

    @Override
    public int getItemCount() {
        return activityModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        public TimelineView mTimelineView;
        public TextView item_timeline_time, item_timeline_detail;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
            mTimelineView.initLine(viewType);
            item_timeline_time = (TextView) itemView.findViewById(R.id.item_timeline_time);
            item_timeline_detail = (TextView) itemView.findViewById(R.id.item_timeline_detail);
        }
    }
}
