package ravtrix.foodybuddy.fragments.inbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private List<ChatResponse> inboxModels;
    private boolean isViewShown = false;
    private CompositeSubscription mSubscriptions;
    private UserLocalStore userLocalStore;

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
        //Helpers.displayToast(getContext(), "CALLED2");
        mSubscriptions = new CompositeSubscription();
        userLocalStore = new UserLocalStore(getActivity());
        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        recyclerviewInbox.addItemDecoration(dividerDecorator);

        if (isViewShown) {
            loadData();
        }

        return view;
    }

    private void loadData() {
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

                    }

                    @Override
                    public void onNext(List<ChatResponse> chatResponses) {
                        inboxModels = chatResponses;
                        setRecyclerView();
                    }
                }));
    }

    private void setRecyclerView() {
        InboxAdapter inboxAdapter = new InboxAdapter(getActivity(), inboxModels);
        recyclerviewInbox.setAdapter(inboxAdapter);
        recyclerviewInbox.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
