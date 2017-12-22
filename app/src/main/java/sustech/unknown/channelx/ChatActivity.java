package sustech.unknown.channelx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;

import co.intentservice.chatui.ChatView;
import sustech.unknown.channelx.command.ChatMessageObjectCommand;
import sustech.unknown.channelx.command.ReadChannelObjectCommand;
import sustech.unknown.channelx.command.ReadChannelOnFailureMessageCommand;
import sustech.unknown.channelx.command.ReadChannelOnSuccessMessageCommand;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.dao.MessagesDao;
import sustech.unknown.channelx.listener.OnSentMessageListenerImpl;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.model.DatabaseRoot;
import sustech.unknown.channelx.util.ToastUtil;

public class ChatActivity extends AppCompatActivity {

    private ChatView chatView;
    private Channel channel;
    private MessagesDao messagesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeChatView();

//        initializeToolbar(intent);
//
//        messagesReference = getMessagesReference(intent);

//
//        // 增加message的监听器，可以在启动时加载ChatView，且在有新聊天消息时更新ChatView
//        messagesReference.addChildEventListener(new MessagesReferenceListener(chatView));
//        // 在发送消息时触发该监听器
//        chatView.setOnSentMessageListener(
//                new OnSentMessageListenerImpl(messagesReference, chatView));

        readChannelFromIntent(getIntent());


    }

    private void initializeChatView() {
        chatView = findViewById(R.id.chat_view);
        chatView.disableInput();
        chatView.setTypingListener(new TypingListenerImpl());
    }

    private void readChannelFromIntent(Intent intent) {
        ReadChannelOnSuccessMessageCommand onSuccessMessageCommand =
                new ReadChannelOnSuccessMessageCommand(this);
        ReadChannelOnFailureMessageCommand onFailureMessageCommand =
                new ReadChannelOnFailureMessageCommand(this);
        ReadChannelObjectCommand objectCommand =
                new ReadChannelObjectCommand(this);
        ChannelDao channelDao =
                new ChannelDao(onSuccessMessageCommand,
                        onFailureMessageCommand, objectCommand);
        if (intent.getStringExtra(Configuration.CHANNEL_KEY_MESSAGE) == null) {
            onReadChannelFailure("CHANNEL ID cannot be empty");
            return;
        }
        channelDao.readChannel(intent.getStringExtra(Configuration.CHANNEL_KEY_MESSAGE));
    }

    public void onReadChannelSuccess(String message) {
        ToastUtil.makeToast(this, message);
    }

    public void onReadChannelFailure(String message) {
        ToastUtil.makeToast(this, message);
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onReadChannelObject(Channel channel) {
        this.channel = channel;
        initializeChannel();
    }

    private void initializeChannel() {
        if (channel == null) {
            onReadChannelFailure("Cannot read the channel!");
            return;
        }
        initializeToolbar(channel.getName() + " (" + channel.readKey() + ")");
        initializeInput();
        initializeMessagesDao();
        initializeMessagesListener();
        initializeOnSentMessageListener();
    }

    private void initializeOnSentMessageListener() {
        chatView.setOnSentMessageListener(
                new OnSentMessageListenerImpl(messagesDao, chatView)
        );
    }

    private void initializeMessagesDao() {
        messagesDao = new MessagesDao(channel, CurrentUser.getUser().getUid());
    }

    private void initializeMessagesListener() {
        Log.d("ChatActivity", "initializeMessagesListener()");
        messagesDao.setChatMessageObjectCommand(new ChatMessageObjectCommand(chatView));
        messagesDao.addListenerForChatMessage();
    }

    private void initializeInput() {
        if (channel.getExpiredTime() < System.currentTimeMillis()) {
            ToastUtil.makeToast(this, "The channel has expired!");
        } else {
            chatView.enableInput();
        }
    }

    private void initializeToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (title != null) {
            toolbarTitle.setText(title);
        }
    }

    private String getChannelKey(Intent intent) {
        return intent.getStringExtra(Configuration.CHANNEL_KEY_MESSAGE);
    }

    private DatabaseReference getMessagesReference(Intent intent) {
        return DatabaseRoot.getRoot()
                .child("channel")
                .child(getChannelKey(intent))
                .child("messages");
    }

    /**
     * Created by dahao on 2017/12/16.
     */

    static class TypingListenerImpl implements ChatView.TypingListener {
        @Override
        public void userStartedTyping() {}
        @Override
        public void userStoppedTyping() {}
    }
}
