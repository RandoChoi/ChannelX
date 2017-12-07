package sustech.unknown.channelx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "sustech.unknown.channelx.chat.EXTRA";
    public static final String CHANNEL_MESSAGE = "sustech.unknown.channelx.chat.CHANNEL";

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




//        Intent intent = new Intent(this, ChatActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            // 选择登陆验证方式
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
            );
            // 创建并启动登陆Intent
            AuthUI authUI = AuthUI.getInstance();
            AuthUI.SignInIntentBuilder signInIntentBuilder = authUI.createSignInIntentBuilder();
            signInIntentBuilder.setAvailableProviders(providers);
            signInIntentBuilder.setIsSmartLockEnabled(false);
            Intent intent = signInIntentBuilder.build();
            startActivityForResult(intent, RC_SIGN_IN);
        }

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(CHANNEL_MESSAGE, "CHANNEL " + message);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Log.w("SIGNIN", "Sign-in failed.");
            }
        }
    }
}
