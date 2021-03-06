package sustech.unknown.channelx.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by dahao on 2017/12/16.
 */

public class CurrentUser {

    public static FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean isLogin() {
        return !(getUser() == null || getUser().isAnonymous());
    }

}
