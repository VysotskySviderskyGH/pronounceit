package com.vsgh.pronounceit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.googleplus.GooglePlusSocialNetwork;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.vsgh.pronounceit.Constants;
import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.customviews.CircleProgress;
import com.vsgh.pronounceit.persistence.Sentence;
import com.vsgh.pronounceit.persistence.User;
import com.vsgh.pronounceit.utils.ConnChecker;
import com.vsgh.pronounceit.utils.SharedPrefsHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Slawa on 2/4/2015.
 */
public class StatisticsActivity extends BaseVsghActivity {

    public static final int LOGIN_REQ_CODE = 2;
    public static final String LOGIN_RESULT_KEY = "soc_id";
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
    protected ProgressDialog mProgressDialog;
    public static final int NUM_OF_COLORS_PALETTE = 2;
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity_layout);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new StatisticsFragment())
                    .commit();
        }
    }

    @Override
    protected void configureViews() {
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
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static class StatisticsFragment extends Fragment
            implements SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener,
            OnRequestSocialPersonCompleteListener {
        private AQuery aQuery;
        private SocialNetworkManager mSocialNetworkManager;
        private int currentNetworkId;
        private String[] colors;
        private Runnable r;
        private int currentResult;
        CircleProgress sector;

        public StatisticsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.statistics_fragment_layout, container, false);
        }

        private void initSocialNetworks() {
            ArrayList<String> fbScope = new ArrayList<>();
            fbScope.addAll(Arrays.asList("public_profile, email, user_friends, user_posts"));

            String VK_KEY = getActivity().getString(R.string.vk_app_id);
            String[] vkScope = {"photos", "status", "wall"};

            mSocialNetworkManager = (SocialNetworkManager) getFragmentManager()
                    .findFragmentByTag(SOCIAL_NETWORK_TAG);

            if (mSocialNetworkManager == null) {
                mSocialNetworkManager = new SocialNetworkManager();

                FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(this, fbScope);
                mSocialNetworkManager.addSocialNetwork(fbNetwork);

                GooglePlusSocialNetwork gpNetwork = new GooglePlusSocialNetwork(this);
                mSocialNetworkManager.addSocialNetwork(gpNetwork);

                VkSocialNetwork vkNetwork = new VkSocialNetwork(this, VK_KEY, vkScope);
                mSocialNetworkManager.addSocialNetwork(vkNetwork);

                getFragmentManager().beginTransaction().add(mSocialNetworkManager,
                        SOCIAL_NETWORK_TAG).commit();
                mSocialNetworkManager.setOnInitializationCompleteListener(this);
            } else {
                if (!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                    List<SocialNetwork> socialNetworks = mSocialNetworkManager
                            .getInitializedSocialNetworks();
                    for (SocialNetwork socialNetwork : socialNetworks) {
                        socialNetwork.setOnLoginCompleteListener(this);
                    }
                }
            }
        }

        private void prepareLastInformation() {
            String userName = SharedPrefsHelper
                    .readStringFromSP(getActivity(), Constants.USERNAME_PREFS, "");
            final int color = SharedPrefsHelper
                    .readIntFromSP(getActivity(), Constants.COLOR_PREFS, Constants.DEF_COLOR_BTNS);
            String userpicUrl = SharedPrefsHelper.readStringFromSP(getActivity(), Constants.USERPIC_URL_PREFS, "");
            int colorPalette = SharedPrefsHelper.readIntFromSP(getActivity(),
                    Constants.PALETTE_COLOR_PREFS, Constants.DEF_COLOR_BACKGROUND);
            aQuery.id(R.id.name).text(userName);
            aQuery.id(R.id.connect).backgroundColor(color);
            aQuery.id(R.id.share).backgroundColor(color);
            aQuery.id(R.id.nameLine).backgroundColor(color);
            if (!userpicUrl.equals("")) {
                File userPic = aQuery.getCachedFile(userpicUrl);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap userBmp = BitmapFactory.decodeFile(userPic.getAbsolutePath(), options);
                aQuery.id(R.id.userpic).image(userBmp);
                Palette.PaletteAsyncListener listener = new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        aQuery.id(R.id.content)
                                .backgroundColor(palette.getLightMutedColor(Constants.DEF_COLOR_BACKGROUND));
                    }
                };
                Palette.generateAsync(userBmp, NUM_OF_COLORS_PALETTE, listener);
            }
            aQuery.id(R.id.content).backgroundColor(colorPalette);
            aQuery.id(R.id.share).enabled(true);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            aQuery = ((BaseVsghActivity) getActivity()).getAq();
            boolean loginStatus = SharedPrefsHelper.readBooleanFromSP(getActivity(),
                    Constants.ONLINE_STATUS_PREFS, false);
            if (loginStatus) {
                int socId = SharedPrefsHelper.readIntFromSP(getActivity(), Constants.SOCID_PREFS, 0);
                prepareLastInformation();
                setLoginState(true, socId);
            }
            initSocialNetworks();
            aQuery.id(R.id.connect).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getActivity(), LoginButtonsActivity.class),
                            LOGIN_REQ_CODE);
                }
            });
            aQuery.id(R.id.share).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ConnChecker.isOnline(getActivity())) {
                        if (currentNetworkId != 0) {
                            SocialNetwork socialNetwork = mSocialNetworkManager
                                    .getSocialNetwork(currentNetworkId);
                            String currentUser = SharedPrefsHelper.readStringFromSP(getActivity(),
                                    Constants.CURRENT_USER, "John Smith");
                            List<User> users = User.find(User.class, "username = ?", currentUser);
                            final int result = Math.round(users.get(0).getSuccess() * 100 / 563);
                            String message = getActivity().getString(R.string.firstpart) + result +
                                    getActivity().getString(R.string.secondpart);
                            if (currentNetworkId == GooglePlusSocialNetwork.ID) {
                                Bundle dialogParams = new Bundle();
                                dialogParams.putString(SocialNetwork.BUNDLE_APP_NAME, getString(R.string.app_name));
                                dialogParams.putString(SocialNetwork.BUNDLE_NAME, getString(R.string.app_name));
                                dialogParams.putString(SocialNetwork.BUNDLE_MESSAGE, message);
                                socialNetwork.requestPostDialog(dialogParams, new OnPostingCompleteListener() {
                                    @Override
                                    public void onPostSuccessfully(int i) {
                                        Toast.makeText(getActivity(),
                                                getActivity().getString(R.string.success_msg), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError(int i, String s, String s2, Object o) {
                                        Toast.makeText(getActivity(), getActivity().getString(R.string.ext_error),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                socialNetwork.requestPostMessage(message, new OnPostingCompleteListener() {
                                    @Override
                                    public void onPostSuccessfully(int i) {
                                        Toast.makeText(getActivity(),
                                                getActivity().getString(R.string.success_msg), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError(int i, String s, String s2, Object o) {
                                        Toast.makeText(getActivity(), getActivity().getString(R.string.ext_error),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    } else {
                        Crouton.makeText(getActivity(), R.string.interner_error, Style.INFO).show();
                    }
                }
            });

            String currentUser = SharedPrefsHelper.readStringFromSP(getActivity(),
                    Constants.CURRENT_USER, "John Smith");
            if(currentUser.equals("John Smith")){
            List<User> users = User.find(User.class, "username = ?", currentUser);
            currentResult = Math.round(users.get(0).getSuccess() * 100 / 563);
            int allTasksCount = Sentence.listAll(Sentence.class).size();
            int successCount = users.get(0).getSuccess();
            int allDoneCoun = successCount + users.get(0).getUnsuccessful();
            aQuery.id(R.id.tv_all).text(allTasksCount + "");
            aQuery.id(R.id.tv_correct).text(successCount + "");
            aQuery.id(R.id.tv_done).text(allDoneCoun + "");
            sector = (CircleProgress) getActivity().findViewById(R.id.sector);
            sector.setType(CircleProgress.SECTOR);
            new AsyncTask<Integer, Integer, Integer>() {
                @Override
                protected Integer doInBackground(Integer... params) {
                    for(int i=0;i<=params[0];i++){
                        publishProgress(i);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                    sector.setmSubCurProgress(values[0]);
                }


            }.execute(currentResult);
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == StatisticsActivity.LOGIN_REQ_CODE) {
                if (resultCode == RESULT_OK) {
                    int networkId = data.getIntExtra(StatisticsActivity.LOGIN_RESULT_KEY, 0);
                    SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
                    if (!socialNetwork.isConnected()) {
                        if (networkId != 0) {
                            socialNetwork.requestLogin();
                            ((StatisticsActivity) getActivity())
                                    .showProgress(getActivity().getString(R.string.loading_msg));
                        } else {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.wrong_sid), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),
                                getActivity().getString(R.string.already_logged_error) + socialNetwork.getID(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        @Override
        public void onSocialNetworkManagerInitialized() {
            for (final SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
                socialNetwork.setOnLoginCompleteListener(this);
                if (socialNetwork.isConnected()) {
                    setLoginState(true, socialNetwork.getID());
                    socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
                    socialNetwork.requestCurrentPerson();
                }
            }
        }

        @Override
        public void onLoginSuccess(final int networkId) {
            ((StatisticsActivity) getActivity()).hideProgress();
            SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
            if (networkId == GooglePlusSocialNetwork.ID
                    && !ConnChecker.isOnline(getActivity())) {
                socialNetwork.logout();
                Crouton.makeText(getActivity(),
                        getActivity().getString(R.string.wrong_msg), Style.INFO).show();
                return;
            }
            setLoginState(true, networkId);
            socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
            socialNetwork.requestCurrentPerson();
            aQuery.id(R.id.share).enabled(true);
        }

        private void setLoginState(boolean loginState, final int networkId) {
            currentNetworkId = networkId;
            if (loginState) {
                aQuery.id(R.id.connect).text(getActivity().getString(R.string.logout))
                        .clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSocialNetworkManager.getSocialNetwork(networkId).cancelAll();
                                mSocialNetworkManager.getSocialNetwork(networkId).logout();
                                setLoginState(false, networkId);
                                aQuery.id(R.id.share).enabled(false);
                                currentNetworkId = 0;
                            }
                        });
            } else {
                aQuery.id(R.id.nameLine).backgroundColor(R.color.s_sky);
                aQuery.id(R.id.connect).backgroundColor(Constants.DEF_COLOR_BTNS);
                aQuery.id(R.id.share).backgroundColor(Constants.DEF_COLOR_BTNS);
                aQuery.id(R.id.name).text(getText(R.string.username));
                aQuery.id(R.id.userpic).image(getResources().getDrawable(R.drawable.ic_smith));
                SharedPrefsHelper.writeStringToSP(getActivity(), Constants.USERNAME_PREFS, "");
                SharedPrefsHelper.writeIntToSP(getActivity(), Constants.COLOR_PREFS, 0);
                SharedPrefsHelper.writeBooleanToSP(getActivity(), Constants.ONLINE_STATUS_PREFS, false);
                SharedPrefsHelper.writeIntToSP(getActivity(), Constants.SOCID_PREFS, 0);
                aQuery.id(R.id.connect).text(getActivity().getString(R.string.login))
                        .clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityForResult(new Intent(getActivity(),
                                                LoginButtonsActivity.class),
                                        LOGIN_REQ_CODE);
                            }
                        });
                User user = User.findById(User.class, (long) 1);
                int allTasksCount = Sentence.listAll(Sentence.class).size();
                int successCount = user.getSuccess();
                int allDoneCoun = successCount + user.getUnsuccessful();
                SharedPrefsHelper.writeStringToSP(getActivity(),
                        Constants.CURRENT_USER,"John Smith");
                aQuery.id(R.id.tv_all).text(allTasksCount + "");
                aQuery.id(R.id.tv_correct).text(successCount + "");
                aQuery.id(R.id.tv_done).text(allDoneCoun + "");
                sector = (CircleProgress) getActivity().findViewById(R.id.sector);
                sector.setType(CircleProgress.SECTOR);
                new AsyncTask<Integer, Integer, Integer>() {
                    @Override
                    protected Integer doInBackground(Integer... params) {
                        for(int i=0;i<=params[0];i++){
                            publishProgress(i);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                        sector.setmSubCurProgress(values[0]);
                    }


                }.execute(Math.round(user.getSuccess() * 100 / 563));
            }
            ((StatisticsActivity) getActivity()).hideProgress();
        }

        @Override
        public void onError(int networkId, String requestID, String errorMessage,
                            Object data) {
            ((StatisticsActivity) getActivity()).hideProgress();
            if (ConnChecker.isOnline(getActivity())) {
                Crouton.makeText(getActivity(),
                        getActivity().getString(R.string.wrong_msg) + ": " + errorMessage,
                        Style.INFO).show();
            }
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Crouton.cancelAllCroutons();
        }

        @Override
        public void onRequestSocialPersonSuccess(int networkId, SocialPerson socialPerson) {
            ((StatisticsActivity) getActivity()).hideProgress();
            int defColor = 0;
            Resources resources = getActivity().getResources();
            switch (networkId) {
                case FacebookSocialNetwork.ID:
                    defColor = resources.getColor(R.color.s_fb_color);
                    break;
                case VkSocialNetwork.ID:
                    defColor = resources.getColor(R.color.s_vk_color);
                    break;
                case GooglePlusSocialNetwork.ID:
                    defColor = resources.getColor(R.color.s_gp_color);
                    break;
            }
            SharedPrefsHelper.writeStringToSP(getActivity(), Constants.USERNAME_PREFS, socialPerson.name);
            SharedPrefsHelper.writeIntToSP(getActivity(), Constants.COLOR_PREFS, defColor);
            SharedPrefsHelper.writeIntToSP(getActivity(), Constants.SOCID_PREFS, networkId);
            SharedPrefsHelper.writeBooleanToSP(getActivity(), Constants.ONLINE_STATUS_PREFS, true);
            SharedPrefsHelper.writeStringToSP(getActivity(), Constants.USERPIC_URL_PREFS, socialPerson.avatarURL);
            aQuery.id(R.id.name).text(socialPerson.name);
            aQuery.id(R.id.nameLine).backgroundColor(defColor);
            aQuery.id(R.id.connect).backgroundColor(defColor);
            aQuery.id(R.id.share).backgroundColor(defColor);
            aQuery.id(R.id.userpic).image(socialPerson.avatarURL, true, true, 0, 0, new BitmapAjaxCallback() {
                @Override
                public void callback(String url, ImageView iv, final Bitmap bm, AjaxStatus status) {
                    Palette.generateAsync(bm, new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            aQuery.id(R.id.userpic).image(bm);
                            int colorPalette = palette.getLightMutedColor(Constants.DEF_COLOR_BACKGROUND);
                            aQuery.id(R.id.content)
                                    .backgroundColor(colorPalette);
                            SharedPrefsHelper.writeIntToSP(getActivity(), Constants.PALETTE_COLOR_PREFS, colorPalette);
                            ((StatisticsActivity) getActivity()).hideProgress();
                        }
                    });
                }
            });
            // update Circlecountet
            List<User> users = User.listAll(User.class);
            boolean flag = false;
            for (User temp : users) {
                if (temp.getUsername().toLowerCase().equals(socialPerson.name.toLowerCase())) {
                    flag = true;
                    SharedPrefsHelper.writeStringToSP(getActivity(),
                            Constants.CURRENT_USER, socialPerson.name);
                    int allTasksCount = Sentence.listAll(Sentence.class).size();
                    int successCount = temp.getSuccess();
                    int allDoneCoun = successCount + temp.getUnsuccessful();
                    aQuery.id(R.id.tv_all).text(allTasksCount + "");
                    aQuery.id(R.id.tv_correct).text(successCount + "");
                    aQuery.id(R.id.tv_done).text(allDoneCoun + "");
                    sector = (CircleProgress) getActivity().findViewById(R.id.sector);
                    sector.setType(CircleProgress.SECTOR);
                    new AsyncTask<Integer, Integer, Integer>() {
                        @Override
                        protected Integer doInBackground(Integer... params) {
                            for(int i=0;i<=params[0];i++){
                                publishProgress(i);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            return null;
                        }

                        @Override
                        protected void onProgressUpdate(Integer... values) {
                            super.onProgressUpdate(values);
                            sector.setmSubCurProgress(values[0]);
                        }


                    }.execute(Math.round(temp.getSuccess() * 100 / 563));
                    break;
                }
            }
            if(!flag){
                User user = new User(socialPerson.name, 0, 0);
                user.save();
                SharedPrefsHelper.writeStringToSP(getActivity(),
                        Constants.CURRENT_USER, user.getUsername());
                aQuery.id(R.id.tv_all).text(563 + "");
                aQuery.id(R.id.tv_correct).text(0 + "");
                aQuery.id(R.id.tv_done).text(0 + "");
                sector = (CircleProgress) getActivity().findViewById(R.id.sector);
                sector.setType(CircleProgress.SECTOR);
                new AsyncTask<Integer, Integer, Integer>() {
                    @Override
                    protected Integer doInBackground(Integer... params) {
                        for(int i=0;i<=params[0];i++){
                            publishProgress(i);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                        sector.setmSubCurProgress(values[0]);
                    }


                }.execute(0);
            }
        }
    }
}
