package ravtrix.foodybuddy.activities.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 5/9/17.
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.chat_recyclerView) protected RecyclerView mMessageRecyclerView;
    @BindView(R.id.activity_chat_spinner) protected ProgressBar chatProgressbar;
    @BindView(R.id.activity_chat_loading_spinner) protected ProgressBar sendProgressbar;
    @BindView(R.id.activity_chat_sendMessageButton) protected Button bSendMessage;
    @BindView(R.id.activity_chat_textMessage) protected EditText etMessage;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<MessageModel, MessageViewHolder> mFirebaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private UserLocalStore userLocalStore;
    private String eventID;

    private static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
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
        Helpers.overrideFonts(this, bSendMessage);
        setTitle("Event Chat");
        chatProgressbar.setVisibility(View.VISIBLE);
        getBundleData();

        userLocalStore = new UserLocalStore(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        //progressBar.setVisibility(View.VISIBLE);
        bSendMessage.setOnClickListener(this);

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<MessageModel,
                MessageViewHolder>(
                MessageModel.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(eventID)) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, MessageModel model, int position) {
                chatProgressbar.setVisibility(View.GONE);
                Helpers.overrideFonts(ChatActivity.this, viewHolder.messageTextView);

                if (model.getUserID() == userLocalStore.getLoggedInUser().getUserID()) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
                    params.weight = 1.0f;
                    params.gravity = Gravity.END;

                    viewHolder.messengerImageView.setLayoutParams(params);
                    viewHolder.messageTextView.setLayoutParams(params);
                    viewHolder.messageTextView.setBackground(ContextCompat.getDrawable(ChatActivity.this, R.drawable.bubright));

                } else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
                    params.weight = 1.0f;
                    params.gravity = Gravity.START;

                    viewHolder.messengerImageView.setLayoutParams(params);
                    viewHolder.messageTextView.setGravity(Gravity.START);
                    viewHolder.messageTextView.setBackground(ContextCompat.getDrawable(ChatActivity.this, R.drawable.bubleft));
                }

                if (model.getText() != null) {
                    viewHolder.messageTextView.setText(model.getText());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                }
                //Picasso.with(ChatActivity.this).load(model.getImageUrl()).into(viewHolder.messengerImageView);
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
                sendProgressbar.setVisibility(View.VISIBLE);
                sendMessage();
                break;
            default:
                break;
        }
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            this.eventID = Integer.toString(bundle.getInt("eventID"));
        }
    }

    /**
     * This is the case where the user is trying to chat through another user's profile page
     * Send/Save new message to Firebase cloud. Save in the chat room name if room exists.
     * Otherwise, if room doesn't exist between the two users yet, make a new room
     */
    private void sendMessage() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userMessage =  etMessage.getText().toString().trim();
                Long time = System.currentTimeMillis();

                if (dataSnapshot.child(eventID).exists()) {
                    sendMessageFirebase(eventID, userMessage, time);
                }
                sendProgressbar.setVisibility(View.GONE);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void sendMessageFirebase(String chatName, String userMessage, Long time) {
        MessageModel message = new MessageModel(userLocalStore.getLoggedInUser().getUserID(), userMessage, time);
        mFirebaseDatabaseReference.child(chatName).push().setValue(message);
        etMessage.setText("");
    }
}
