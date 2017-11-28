package ravtrix.foodybuddy.activities.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    private String eventID, eventName;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView1, messageTextView2;
        TextView timeTextView;
        CircleImageView messengerImageView1, messengerImageView2;
        RelativeLayout layoutMessage1, layoutMessage2;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView1 = (TextView) itemView.findViewById(R.id.item_message_message);
            timeTextView = (TextView) itemView.findViewById(R.id.item_time);
            messengerImageView1 = (CircleImageView) itemView.findViewById(R.id.item_countryFeed_profileImage);
            layoutMessage1 = (RelativeLayout) itemView.findViewById(R.id.layout_message);

            messageTextView2 = (TextView) itemView.findViewById(R.id.item_message_message2);
            messengerImageView2 = (CircleImageView) itemView.findViewById(R.id.item_inboxFeed_profileImage2);
            layoutMessage2 = (RelativeLayout) itemView.findViewById(R.id.layout_message2);

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
        getBundleData();

        if (!TextUtils.isEmpty(eventName)) {
            setTitle(eventName);
        }
        chatProgressbar.setVisibility(View.VISIBLE);

        userLocalStore = new UserLocalStore(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        //progressBar.setVisibility(View.VISIBLE);
        bSendMessage.setOnClickListener(this);

        // New child entries

        new Runnable() {
            @Override
            public void run() {
                mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
            }
        }.run();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<MessageModel,
                MessageViewHolder>(
                MessageModel.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(eventID)) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, MessageModel model, int position) {
                chatProgressbar.setVisibility(View.GONE);
                Helpers.overrideFonts(ChatActivity.this, viewHolder.messageTextView1);
                Helpers.overrideFonts(ChatActivity.this, viewHolder.messageTextView2);

                if (model.getUserID() == userLocalStore.getLoggedInUser().getUserID()) {
                    viewHolder.layoutMessage2.setVisibility(View.VISIBLE);
                    viewHolder.layoutMessage1.setVisibility(View.GONE);
                    TextView messageTextView2 = viewHolder.messageTextView2;
                    messageTextView2.setText(model.getText());

                    if (TextUtils.isEmpty(model.getImageURL())) {
                        Picasso.with(ChatActivity.this)
                                .load("http://ahdzbook.com/data/out/115/hdwp693950189.jpg")
                                .fit()
                                .centerCrop()
                                .into(viewHolder.messengerImageView2);
                    } else {
                        Picasso.with(ChatActivity.this)
                                .load(model.getImageURL())
                                .fit()
                                .centerCrop()
                                .into(viewHolder.messengerImageView2);
                    }

                } else {
                    viewHolder.layoutMessage1.setVisibility(View.VISIBLE);
                    viewHolder.layoutMessage2.setVisibility(View.GONE);
                    TextView messageTextView1 = viewHolder.messageTextView1;
                    messageTextView1.setText(model.getText());

                    if (TextUtils.isEmpty(model.getImageURL())) {
                        Picasso.with(ChatActivity.this)
                                .load("http://ahdzbook.com/data/out/115/hdwp693950189.jpg")
                                .fit()
                                .centerCrop()
                                .into(viewHolder.messengerImageView1);
                    } else {
                        Picasso.with(ChatActivity.this)
                                .load(model.getImageURL())
                                .fit()
                                .centerCrop()
                                .into(viewHolder.messengerImageView1);
                    }
                }
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

        new Runnable() {
            @Override
            public void run() {
                mMessageRecyclerView.setAdapter(mFirebaseAdapter);
            }
        }.run();
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
            this.eventName = bundle.getString("eventName");
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
        MessageModel message = new MessageModel(userLocalStore.getLoggedInUser().getUserID(), userMessage, time,
                userLocalStore.getLoggedInUser().getImageURL(), eventName);
        mFirebaseDatabaseReference.child(chatName).push().setValue(message);
        etMessage.setText("");
    }
}
