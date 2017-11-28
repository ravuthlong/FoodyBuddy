package ravtrix.foodybuddy.fragments.inbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.decorator.DividerDecoration;
import ravtrix.foodybuddy.fragments.inbox.recyclerview.adapter.InboxAdapter;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.network.networkresponse.ChatResponse;
import ravtrix.foodybuddy.utils.RetrofitChatSingleton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ravinder on 1/28/17.
 */

public class InboxFragment extends Fragment {

    @BindView(R.id.frag_userinbox_recyclerView) protected RecyclerView recyclerviewInbox;
    @BindView(R.id.frag_userinbox_progressbar) protected ProgressBar progressBar;
    @BindView(R.id.frag_userinbox_tvNoInbox) protected TextView tvNoInbox;

    private List<ChatResponse> inboxModels;
    private boolean isViewShown = false;
    private CompositeSubscription mSubscriptions;
    private UserLocalStore userLocalStore;
    private static final String TAG = InboxFragment.class.getSimpleName();
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getView() != null) {
            isViewShown = true;
            loadData();
        } else {
            isViewShown = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_userinbox, container, false);

        ButterKnife.bind(this, view);
        mSubscriptions = new CompositeSubscription();
        userLocalStore = new UserLocalStore(getActivity());
        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_inbox);
        recyclerviewInbox.addItemDecoration(dividerDecorator);

        if (isViewShown) {
            loadData();
        }

        return view;
    }

    private void loadData() {
        showProgressbar();
        mSubscriptions.add(RetrofitChatSingleton.getInstance()
                .getUserChats()
                .getUserChats(userLocalStore.getLoggedInUser().getUserID())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<ChatResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressbar();
                        Log.e(TAG, "Error fetching user inbox");
                    }

                    @Override
                    public void onNext(List<ChatResponse> chatResponses) {
                        if (chatResponses.size() > 0) {
                            inboxModels = chatResponses;
                            loadLatestMessage(inboxModels);
                        } else {
                            recyclerviewInbox.setVisibility(View.GONE);
                            tvNoInbox.setVisibility(View.VISIBLE);
                            hideProgressbar();
                        }
                    }
                }));
    }


    private void loadLatestMessage(final List<ChatResponse> chatResponses) {
        final PassValue counter = new PassValue(0);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        for (int i = 0; i < chatResponses.size(); i++) {

            final PassValue passValue = new PassValue(i);
            String chatName = "" + chatResponses.get(i).getEvent_id();

            // Iterate to the last child of the chatroom to get latest snapshot for latest message
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference().child(chatName);
            mFirebaseDatabaseReference.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    counter.increment();

                    if (dataSnapshot.exists()) {
                        DataSnapshot snapshot;
                        // access last message
                        snapshot = dataSnapshot.getChildren().iterator().next();

                        /*
                         * Set the components of each inbox message by getting its child from the database through key vale pair
                         */
                        if (snapshot != null) {
                            String time = snapshot.child("time").getValue().toString();
                            String latestMessage = snapshot.child("text").getValue().toString();

                            String eventName = "";
                            if (snapshot.hasChild("eventName")) {
                                eventName = snapshot.child("eventName").getValue().toString();
                            }

                            /*if (countLines(latestMessage) > 4) {
                                latestMessage = getFirstFourLines(latestMessage);
                            }*/

                            chatResponses.get(passValue.getI()).setLatest_message(latestMessage);
                            chatResponses.get(passValue.getI()).setEventName(eventName);
                            chatResponses.get(passValue.getI()).setLatestDate(time);
                        }
                        chatResponses.get(passValue.getI()).setSnapshot(snapshot);
                    }

                    // completion
                    if (counter.getI() >= inboxModels.size()) {
                        hideProgressbar();
                        recyclerviewInbox.setVisibility(View.VISIBLE);
                        tvNoInbox.setVisibility(View.GONE);
                        setRecyclerView();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void setRecyclerView() {

        for (ChatResponse chatResponse: inboxModels) {
            if (TextUtils.isEmpty(chatResponse.getLatestDate())) {
                chatResponse.setLatestDate("1510174798217");
            }
        }
        Collections.sort(inboxModels);

        InboxAdapter inboxAdapter = new InboxAdapter(getActivity(), inboxModels);
        recyclerviewInbox.setAdapter(inboxAdapter);
        recyclerviewInbox.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showProgressbar() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
    }

    private class PassValue {
        int i;

        PassValue(int i) {
            this.i = i;
        }
        void setI(int i) {
            this.i = i;
        }
        int getI() {
            return i;
        }
        void increment() { i++; }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
