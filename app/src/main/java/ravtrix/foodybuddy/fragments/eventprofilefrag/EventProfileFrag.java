package ravtrix.foodybuddy.fragments.eventprofilefrag;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activitymonitorDB.ActivityData;
import ravtrix.foodybuddy.activitymonitorDB.DatabaseOperations;
import ravtrix.foodybuddy.fragments.eventprofilefrag.recycylerviewtimeline.ActivityModel;
import ravtrix.foodybuddy.fragments.eventprofilefrag.recycylerviewtimeline.EventActivityAdapter;

public class EventProfileFrag extends Fragment {

    private static final String TAG = "EventProfileFrag";
    private EventActivityAdapter eventActivityAdapter;
    private List<ActivityModel> activityModelList;

    @BindView(R.id.frag_event_profile_recyclerview) protected RecyclerView activityTimelineRecycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_event_profile, container, false);
        ButterKnife.bind(this, view);
        activityModelList = new ArrayList<>();

        fetchData();
        return view;
    }



    private void fetchData() {
        DatabaseOperations databaseOperations = new DatabaseOperations(getActivity());
        Cursor cursor = databaseOperations.getActivities(databaseOperations);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Log.d(TAG, "TIME: " + cursor.getString(0) + ", ACTIVITY: " + cursor.getString(1));
                activityModelList.add(new ActivityModel(cursor.getString(0), cursor.getString(1))); // 0 time, 1 detail columns
            } while (cursor.moveToNext());
        }

        eventActivityAdapter = new EventActivityAdapter(getActivity(), activityModelList);
        activityTimelineRecycler.setAdapter(eventActivityAdapter);
        activityTimelineRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
