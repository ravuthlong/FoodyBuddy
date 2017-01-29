package ravtrix.foodybuddy.fragments.maineventfrag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.decorator.DividerDecoration;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.adapter.EventAdapter;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.EventModel;

/**
 * Created by Ravinder on 1/27/17.
 */

public class MainEventFrag extends Fragment {

    @BindView(R.id.frag_eventmain_recyclerView) protected RecyclerView eventRecyclerView;
    private List<EventModel> eventModels;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_eventmain, container, false);
        ButterKnife.bind(this, view);

        setModels();
        setRecyclerView();
        return view;
    }

    /**
     * Fill the models of Event Model array list
     */
    private void setModels() {
        EventModel eventModel1 = new EventModel("", "Lucky Charm", "Jan 02, 2017", "Jan 30, 2017", "Who wants to try this new place",
                "3222 Broadway", "3 miles", 5);

        EventModel eventModel2 = new EventModel("", "Lucky Charm", "Jan 02, 2017", "Jan 30, 2017", "Who wants to try this new place",
                "3222 Broadway", "3 miles", 5);

        EventModel eventModel3 = new EventModel("", "Lucky Charm", "Jan 02, 2017", "Jan 30, 2017", "Who wants to try this new place",
                "3222 Broadway", "3 miles", 5);

        EventModel eventModel4 = new EventModel("", "Lucky Charm", "Jan 02, 2017", "Jan 30, 2017", "Who wants to try this new place",
                "3222 Broadway", "3 miles", 5);

        this.eventModels = new ArrayList<>();
        this.eventModels.add(eventModel1);
        this.eventModels.add(eventModel2);
        this.eventModels.add(eventModel3);
        this.eventModels.add(eventModel4);
    }

    /**
     * Sets up the recycler view with its adapter and decorator
     */
    private void setRecyclerView() {

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        EventAdapter eventAdapter = new EventAdapter(getContext(), eventModels);
        eventRecyclerView.addItemDecoration(dividerDecorator);
        eventRecyclerView.setAdapter(eventAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * Called by the main activity to scroll the current recycler view of this fragment to the first item
     */
    public void scrollToTop() {
        eventRecyclerView.scrollToPosition(0);
    }
}
