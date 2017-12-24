package sustech.unknown.channelx;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import sustech.unknown.channelx.command.JoinChannelOnFailureMessageCommand;
import sustech.unknown.channelx.command.JoinChannelOnSuccessMessageCommand;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.util.ToastUtil;

public class JoinChannelActivity extends AppCompatActivity {

    private EditText editText;
    private String channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_channel);

        initializeToolbar();
        initializeEditText();
    }

    private void initializeEditText() {
        editText = findViewById(R.id.editText);
    }

    private void initializeToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onJoinChannel(View view) {
       checkChannelExists(editText.getText().toString());
    }

    private void checkChannelExists(String channelId) {
        JoinChannelOnSuccessMessageCommand onSuccessMessageCommand =
                new JoinChannelOnSuccessMessageCommand(this);
        JoinChannelOnFailureMessageCommand onFailureMessageCommand =
                new JoinChannelOnFailureMessageCommand(this);
        ChannelDao channelDao =
                new ChannelDao(onSuccessMessageCommand, onFailureMessageCommand);

        if (!channelId.trim().isEmpty()){
            this.channelId = channelId.trim();
            // channelDao.joinChannel(this.channelId);
            channelDao.joinChannel(this.channelId,
                    CurrentUser.getUser().getUid(),
                    CurrentUser.getUser().getDisplayName());
        } else {
            ToastUtil.makeToast(this, "Channel ID shouldn't be empty");
        }
    }

    public void onSuccess(String message) {
        ToastUtil.makeToast(this, message);
        setResult(RESULT_OK);
        finish();
    }

    public void onFailure(String message) {
        ToastUtil.makeToast(this, message);
        setResult(RESULT_CANCELED);
        finish();

    }


//    public void channelNotExists() {
//        ToastUtil.makeToast(this, "Channel doesn't exist!");
//    }
//
//    public void channelExists() {
//        // ToastUtil.makeToast(this, "Channel does exist!");
//        checkIfInChannel();
//    }
//
//    private void checkIfInChannel() {
//        Command onSuccessCommand =
//                new CheckIfInChannelOnSuccessCommand(this);
//        Command onFailureCommand =
//                new CheckIfInChannelOnFailureCommand(this);
//        ChannelDao channelDao = new ChannelDao(onSuccessCommand, onFailureCommand);
//        channelDao.checkInChannel(this.channelId, CurrentUser.getUser().getUid());
//    }
//
//    public void isInChannel() {
//        ToastUtil.makeToast(this, "You are already in the channel!");
//        finish();
//    }
//
//    public void notInChannel() {
//        // ToastUtil.makeToast(this, "You are not in the channel!");
//
//    }

//    private void checkIfAnonymous() {
//        Command onSuccessCommand =
//                new CheckIfInChannelOnSuccessCommand(this);
//        Command onFailureCommand =
//                new CheckIfInChannelOnFailureCommand(this);
//    }
}
