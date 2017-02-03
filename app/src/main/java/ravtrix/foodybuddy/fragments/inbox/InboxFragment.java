package ravtrix.foodybuddy.fragments.inbox;

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
import ravtrix.foodybuddy.fragments.inbox.recyclerview.adapter.InboxAdapter;
import ravtrix.foodybuddy.fragments.inbox.recyclerview.model.InboxModel;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/28/17.
 */

public class InboxFragment extends Fragment {

    @BindView(R.id.frag_userinbox_recyclerView) protected RecyclerView recyclerviewInbox;
    private List<InboxModel> inboxModels;
    boolean isFragLoaded = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && !isFragLoaded) {
            loadViewWithData();
            isFragLoaded = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_userinbox, container, false);
        ButterKnife.bind(this, view);
        Helpers.displayToast(getContext(), "CALLED2");

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(getActivity(), R.drawable.line_divider_main);
        recyclerviewInbox.addItemDecoration(dividerDecorator);

        return view;
    }

    private void loadViewWithData() {
        loadData();
        setRecyclerView();
    }

    private void loadData() {
        inboxModels = new ArrayList<>();
        InboxModel inboxModel1 = new InboxModel("Ortemis", "http://images6.fanpop.com/image/photos/39200000/taylor-swift-icons-demmah-39210598-250-250.png", "01/11/2017", "Hey, do you remember this person?");
        InboxModel inboxModel2 = new InboxModel("Jojo191", "http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png", "01/01/2017", "This has got to be the biggest joke in the world...");
        InboxModel inboxModel3 = new InboxModel("Mika", "http://a1.mzstatic.com/us/r30/Purple4/v4/eb/36/69/eb366995-c26d-85be-16e3-44cbb1adff9a/icon350x350.png", "12/25/2016", "When are you free? I have something very important to tell you.");
        InboxModel inboxModel4 = new InboxModel("Joseph100", "http://purrfectcatbreeds.com/wp-content/uploads/2014/06/small-cat-breeds.jpg", "11/12/2016", "What is a world without peace and wisdom? That's right. Nothing. Not a world at all...");
        inboxModels.add(inboxModel1);
        inboxModels.add(inboxModel2);
        inboxModels.add(inboxModel3);
        inboxModels.add(inboxModel4);
    }

    private void setRecyclerView() {
        InboxAdapter inboxAdapter = new InboxAdapter(getContext(), inboxModels);
        recyclerviewInbox.setAdapter(inboxAdapter);
        recyclerviewInbox.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
