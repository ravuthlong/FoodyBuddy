package ravtrix.foodybuddy.activities.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 5/9/17.
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.chat_recyclerView) protected RecyclerView mMessageRecyclerView;
    @BindView(R.id.activity_chat_loading_spinner) protected Spinner spinner;
    @BindView(R.id.activity_chat_sendMessageButton) protected Button bSendMessage;
    @BindView(R.id.activity_chat_textMessage) protected EditText etMessage;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<MessageModel, MessageViewHolder> mFirebaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, etMessage);
        setTitle("Event Chat");

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        spinner.setVisibility(View.VISIBLE);
        bSendMessage.setOnClickListener(this);

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<MessageModel,
                MessageViewHolder>(
                MessageModel.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child("1")) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, MessageModel model, int position) {
                spinner.setVisibility(View.INVISIBLE);

                if (model.getText() != null) {
                    viewHolder.messageTextView.setText(model.getText());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                }
                viewHolder.messengerTextView.setText(model.getName());
                Picasso.with(ChatActivity.this).load(model.getImageUrl()).into(viewHolder.messengerImageView);
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_chat_sendMessageButton:
                break;
            default:
                break;
        }
    }
}
