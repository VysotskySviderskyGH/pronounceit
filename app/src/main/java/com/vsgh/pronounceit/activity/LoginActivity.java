package com.vsgh.pronounceit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.googleplus.GooglePlusSocialNetwork;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Slawa on 2/4/2015.
 */
public class LoginActivity extends BaseVsghActivity {
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
    private ProgressDialog mProgressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }

    @Override
    protected void configureViews() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    protected void showProgress(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    protected void hideProgress() {
        mProgressDialog.dismiss();
    }

    public static class LoginFragment extends Fragment
            implements SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener {
        private AQuery aQuery;
        private SocialNetworkManager mSocialNetworkManager;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.login_fragment_layout, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            aQuery = ((BaseVsghActivity) getActivity()).getAq();

            ArrayList<String> fbScope = new ArrayList<>();
            fbScope.addAll(Arrays.asList("public_profile, email, user_friends"));

            String VK_KEY = getActivity().getString(R.string.vk_app_id);
            String[] vkScope = {"photos", "status"};

            mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
            if (mSocialNetworkManager == null) {
                mSocialNetworkManager = new SocialNetworkManager();

                FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(this, fbScope);
                mSocialNetworkManager.addSocialNetwork(fbNetwork);

                GooglePlusSocialNetwork gpNetwork = new GooglePlusSocialNetwork(this);
                mSocialNetworkManager.addSocialNetwork(gpNetwork);

                VkSocialNetwork vkNetwork = new VkSocialNetwork(this, VK_KEY, vkScope);
                mSocialNetworkManager.addSocialNetwork(vkNetwork);

                getFragmentManager().beginTransaction().add(mSocialNetworkManager, SOCIAL_NETWORK_TAG).commit();
                mSocialNetworkManager.setOnInitializationCompleteListener(this);
            } else {
                if (!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                    List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                    for (SocialNetwork socialNetwork : socialNetworks) {
                        socialNetwork.setOnLoginCompleteListener(this);
                        initSocialNetwork(socialNetwork);
                    }
                }
            }
            aQuery.id(R.id.imgbtn_close).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            aQuery.id(R.id.btn_login_facebook).clicked(loginClick);
            aQuery.id(R.id.btn_login_googleplus).clicked(loginClick);
            aQuery.id(R.id.btn_login_vk).clicked(loginClick);
        }

        private void initSocialNetwork(SocialNetwork socialNetwork) {
            if (socialNetwork.isConnected()) {
                String networkName = "";
                switch (socialNetwork.getID()) {
                    case FacebookSocialNetwork.ID:
                        networkName = "FB";
                        break;
                    case GooglePlusSocialNetwork.ID:
                        networkName = "GP";
                        break;
                    case VkSocialNetwork.ID:
                        networkName = "VK";
                        break;
                }
                Toast.makeText(getActivity(), "You've logged via " + networkName, Toast.LENGTH_LONG).show();
            }
        }

        private View.OnClickListener loginClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int networkId = 0;
                switch (view.getId()) {
                    case R.id.btn_login_facebook:
                        networkId = FacebookSocialNetwork.ID;
                        break;
                    case R.id.btn_login_vk:
                        networkId = VkSocialNetwork.ID;
                        break;
                    case R.id.btn_login_googleplus:
                        networkId = GooglePlusSocialNetwork.ID;
                        break;
                }
                SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
                if (!socialNetwork.isConnected()) {
                    if (networkId != 0) {
                        socialNetwork.requestLogin();
                        ((LoginActivity) getActivity()).showProgress("Loading");
                    } else {
                        Toast.makeText(getActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "You've already logged "+socialNetwork.getID(), Toast.LENGTH_LONG).show();
                }
            }
        };

        @Override
        public void onSocialNetworkManagerInitialized() {
            for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
                socialNetwork.setOnLoginCompleteListener(this);
                initSocialNetwork(socialNetwork);
            }
        }

        @Override
        public void onLoginSuccess(int networkId) {
            ((LoginActivity) getActivity()).hideProgress();
        }

        @Override
        public void onError(int networkId, String requestID, String errorMessage, Object data) {
            ((LoginActivity) getActivity()).hideProgress();
            Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
