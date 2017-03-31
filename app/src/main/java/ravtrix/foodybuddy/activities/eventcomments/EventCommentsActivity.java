package ravtrix.foodybuddy.activities.eventcomments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.eventcomments.recyclerview.EventCommentAdapter;
import ravtrix.foodybuddy.activities.eventcomments.recyclerview.EventCommentModel;
import ravtrix.foodybuddy.decorator.DividerDecoration;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.model.Response;
import ravtrix.foodybuddy.networkmodel.CommentParam;
import ravtrix.foodybuddy.utils.Helpers;
import ravtrix.foodybuddy.utils.RetrofitCommentSingleton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class EventCommentsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.activity_event_comment_recyclerView) protected RecyclerView recyclerView;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_event_comment_submitButton) protected Button bSubmit;
    @BindView(R.id.activity_event_comment_etComment) protected EditText etComment;
    @BindView(R.id.activity_event_comment_relativeComment) protected RelativeLayout relativeComment;
    private List<EventCommentModel> eventCommentModelList;
    private EventCommentAdapter eventCommentAdapter;
    private int event_id;
    private CompositeSubscription mSubscriptions;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_comments);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, relativeComment);
        setTitle("Comments");

        getBundle();
        setListeners();
        mSubscriptions = new CompositeSubscription();
        userLocalStore = new UserLocalStore(this);

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_main);
        recyclerView.addItemDecoration(dividerDecorator);

        setModels();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_event_comment_submitButton:
                // post comment
                postComment();
                break;
        }
    }

    private void setModels() {

        mSubscriptions.add(RetrofitCommentSingleton.getRetrofitComment()
                .getEventComments()
                .getEventComments(event_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<EventCommentModel>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.d(EventCommentsActivity.class.getSimpleName(), "Error fetching event comments");
                    }

                    @Override
                    public void onNext(List<EventCommentModel> eventCommentModels) {
                        Log.d(EventCommentsActivity.class.getSimpleName(), "ON NEXT COMMENT");

                        eventCommentModelList = eventCommentModels;
                        setAdapter();
                    }
                }));

        /*
        eventCommentModelList.add(new EventCommentModel("Ortemis", "Can you make it 25 minutes later? I have work, and I really want to join.", "2 hours ago", "https://s.aolcdn.com/hss/storage/midas/4e58b258b6e54aa8d650dafd6360cf18/203771936/TopGun_INTRO.jpg"));
        eventCommentModelList.add(new EventCommentModel("Randy", "Same here. It is a bit too early?", "1 hour ago", "https://www.biography.com/.image/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTE4MDAzNDEwMDU4NTc3NDIy/hillary-clinton-9251306-2-402.jpg"));
        eventCommentModelList.add(new EventCommentModel("Taylor", "Okay guys, I will change the time.", "1 minute ago", "http://cdn.playbuzz.com/cdn/ff9dd0b6-7e75-45fe-8f6b-ec608e7337ab/268c931f-49c8-47b3-8185-70cc566b2538.jpg"));*/
    }

    private void setAdapter() {
        this.eventCommentAdapter = new EventCommentAdapter(this, eventCommentModelList);
        this.recyclerView.setAdapter(eventCommentAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            this.event_id = bundle.getInt("event_id"); // event id received from on click event item
        }
    }

    private void setListeners() {
        bSubmit.setOnClickListener(this);
    }

    private void postComment() {

        mSubscriptions.add(RetrofitCommentSingleton.getRetrofitComment()
                .postComment()
                .postComment(getCommentParam())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(Response response) {
                        Helpers.displayToast(EventCommentsActivity.this, response.getMessage());
                        etComment.getText().clear();
                    }
                }));

    }

    // Set up comment object based on user input
    private CommentParam getCommentParam() {
        CommentParam commentParam = new CommentParam();
        commentParam.setComment(etComment.getText().toString());
        commentParam.setUser_id(userLocalStore.getLoggedInUser().getUserID());
        commentParam.setEvent_id(event_id);
        commentParam.setCreate_time(System.currentTimeMillis() / 1000L);

        return commentParam;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
