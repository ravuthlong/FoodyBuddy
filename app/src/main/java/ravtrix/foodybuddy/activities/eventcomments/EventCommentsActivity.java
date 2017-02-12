package ravtrix.foodybuddy.activities.eventcomments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.eventcomments.recyclerview.EventCommentAdapter;
import ravtrix.foodybuddy.activities.eventcomments.recyclerview.EventCommentModel;
import ravtrix.foodybuddy.decorator.DividerDecoration;
import ravtrix.foodybuddy.utils.Helpers;

public class EventCommentsActivity extends AppCompatActivity {

    @BindView(R.id.activity_event_comment_recyclerView) protected RecyclerView recyclerView;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private List<EventCommentModel> eventCommentModelList;
    private EventCommentAdapter eventCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_comments);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Comments");

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_main);
        recyclerView.addItemDecoration(dividerDecorator);

        setModels();
        setAdapter();
    }

    private void setModels() {
        eventCommentModelList = new ArrayList<>();
        eventCommentModelList.add(new EventCommentModel("Ortemis", "Can you make it 25 minutes later? I have work, and I really want to join.", "2 hours ago", "https://s.aolcdn.com/hss/storage/midas/4e58b258b6e54aa8d650dafd6360cf18/203771936/TopGun_INTRO.jpg"));
        eventCommentModelList.add(new EventCommentModel("Randy", "Same here. It is a bit too early?", "1 hour ago", "https://www.biography.com/.image/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTE4MDAzNDEwMDU4NTc3NDIy/hillary-clinton-9251306-2-402.jpg"));
        eventCommentModelList.add(new EventCommentModel("Taylor", "Okay guys, I will change the time.", "1 minute ago", "http://cdn.playbuzz.com/cdn/ff9dd0b6-7e75-45fe-8f6b-ec608e7337ab/268c931f-49c8-47b3-8185-70cc566b2538.jpg"));
    }

    private void setAdapter() {
        this.eventCommentAdapter = new EventCommentAdapter(this, eventCommentModelList);
        this.recyclerView.setAdapter(eventCommentAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
