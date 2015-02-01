package com.vsgh.pronounceit.activity.base;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

/**
 * Created by Slawa on 2/1/2015.
 */
public abstract class BaseSocialActivity extends BaseVsghActivity {
    protected UiLifecycleHelper facebookUiHelper;
    protected Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onFacebookCalled(session, state, exception);
        }
    };

    protected void onFacebookCalled(Session session, SessionState state, Exception exception) {
    }

    protected UiLifecycleHelper getFacebookUiHelper() {
        return facebookUiHelper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookUiHelper = new UiLifecycleHelper(this, callback);
        facebookUiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        facebookUiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        facebookUiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        facebookUiHelper.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookUiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        facebookUiHelper.onSaveInstanceState(outState);
    }

}
