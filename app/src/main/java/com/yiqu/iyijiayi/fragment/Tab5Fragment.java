package com.yiqu.iyijiayi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.CircleImageView;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.MenuDialogPicHelper;
import com.yiqu.iyijiayi.adapter.Tab4Adapter;
import com.yiqu.iyijiayi.fragment.tab5.ApplyTeacherFragment;
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.fragment.tab5.InfoFragment;
import com.yiqu.iyijiayi.fragment.tab5.JiesuanshuomingFragment;
import com.yiqu.iyijiayi.fragment.tab5.PayforYBFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.fragment.tab5.SettingFragment;
import com.yiqu.iyijiayi.fragment.tab5.Tab5AboutFragment;
import com.yiqu.iyijiayi.fragment.tab5.Tab5FollowedFragment;
import com.yiqu.iyijiayi.fragment.tab5.Tab5WoWenListFragment;
import com.yiqu.iyijiayi.fragment.tab5.Tab5WopingListFragment;
import com.yiqu.iyijiayi.fragment.tab5.Tab5WotingFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PageCursorView;
import com.yiqu.iyijiayi.utils.PictureUtils;

public class Tab5Fragment extends TabContentFragment implements View.OnClickListener {


    private Button Btlogin;
    private UserInfo userInfo;
    private CircleImageView head;
    private MenuDialogPicHelper mMenuDialogPicHelper;
    private RelativeLayout llUserInfo;
    private Button logOutBt;
    private TextView menu_item_wodeyijiayizhuye;
    private TextView menu_item_wowen;
    private TextView menu_item_woting;

    private TextView menu_item_wodeyibi;
    private TextView menu_item_shezhi;
    private TextView menu_item_jiesuanshuoming;
    private TextView menu_item_bangzhu;
    private TextView menu_item_guanyu;
    private TextView username;
    private TextView user_school;
    private TextView user_desc;
    private LinearLayout teacherOnly;
    private TextView menu_item_woping;
    private TextView menu_item_wodexizuo;
    private LinearLayout ll_tabs;
    private ImageView sex;
    private TextView content;
    private ImageView background;

    @Override
    protected int getTitleView() {

        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {

        return R.layout.tab5_fragment;
    }

    @Override
    protected void initView(View v) {


        Btlogin = (Button) v.findViewById(R.id.tab5_login);
        logOutBt = (Button) v.findViewById(R.id.logout);

        llUserInfo = (RelativeLayout) v.findViewById(R.id.userinfo);
        background = (ImageView) v.findViewById(R.id.background);
        ll_tabs = (LinearLayout) v.findViewById(R.id.ll_tabs);

        InitHeadView(v);

        menu_item_wodeyijiayizhuye = (TextView) v.findViewById(R.id.menu_item_wodeyijiayizhuye);
        menu_item_wowen = (TextView) v.findViewById(R.id.menu_item_wowen);
        teacherOnly = (LinearLayout) v.findViewById(R.id.mine_teacher);
        menu_item_woping = (TextView) v.findViewById(R.id.menu_item_woping);
        menu_item_wodexizuo = (TextView) v.findViewById(R.id.menu_item_wodexizuo);

        menu_item_woting = (TextView) v.findViewById(R.id.menu_item_woting);

        menu_item_wodeyibi = (TextView) v.findViewById(R.id.menu_item_wodeyibi);
        menu_item_shezhi = (TextView) v.findViewById(R.id.menu_item_shezhi);
        menu_item_jiesuanshuoming = (TextView) v.findViewById(R.id.menu_item_jiesuanshuoming);
//        menu_item_bangzhu = (TextView) v.findViewById(R.id.menu_item_bangzhu);
        menu_item_guanyu = (TextView) v.findViewById(R.id.menu_item_guanyu);

        v.findViewById(R.id.menu_item_guanzhu).setOnClickListener(this);

        menu_item_wodeyijiayizhuye.setOnClickListener(this);
        menu_item_wowen.setOnClickListener(this);
        menu_item_woting.setOnClickListener(this);

        menu_item_wodeyibi.setOnClickListener(this);
        menu_item_shezhi.setOnClickListener(this);
        menu_item_jiesuanshuoming.setOnClickListener(this);
//        menu_item_bangzhu.setOnClickListener(this);
        menu_item_guanyu.setOnClickListener(this);
        logOutBt.setOnClickListener(this);
        menu_item_woping.setOnClickListener(this);
        menu_item_wodexizuo.setOnClickListener(this);
    }

    public void InitHeadView(View v) {

        username = (TextView) v.findViewById(R.id.name);
        head = (CircleImageView) v.findViewById(R.id.head);
        user_desc = (TextView) v.findViewById(R.id.desc);
        content = (TextView) v.findViewById(R.id.content);
        sex = (ImageView) v.findViewById(R.id.sex);


    }

    @Override
    protected boolean isTouchMaskForNetting() {

        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        Btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.startNextAct(getActivity(),
                        SelectLoginFragment.class.getName());
            }
        });
//
    }

    private void initUI() {
        boolean isLogin = AppShare.getIsLogin(getActivity());

        if (isLogin) {
            userInfo = AppShare.getUserInfo(getActivity());
            Btlogin.setVisibility(View.GONE);
            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getUserByPhoneUid, MyNetRequestConfig.getUserByPhoneUid(
                            getActivity(), userInfo.uid), "getUserByPhoneUid", Tab5Fragment.this, false, true);


            llUserInfo.setVisibility(View.VISIBLE);
            logOutBt.setVisibility(View.VISIBLE);
        } else {
            logOutBt.setVisibility(View.GONE);
            ll_tabs.setVisibility(View.GONE);
            llUserInfo.setVisibility(View.GONE);
            Btlogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        MobclickAgent.onPageStart("我"); //统计页面，"MainScreen"为页面名称，可自定义

        initUI();
        super.onResume();
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {

        return FLAG_TXT;
    }

    @Override
    protected boolean onPageBack() {

        if (mOnFragmentListener != null) {
            mOnFragmentListener.onFragmentBack(this);
        }
        return true;
    }

    @Override
    protected boolean onPageNext() {
        return false;
    }

    @Override
    protected void initTitle() {
        setTitleText(getString(R.string.label_tab5));
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);

        if (type == NetCallBack.TYPE_SUCCESS) {
            Gson gson = new Gson();
            userInfo = gson.fromJson(netResponse.data.toString(), UserInfo.class);
            AppShare.setIsLogin(getActivity(), true);
            AppShare.setUserInfo(getActivity(), userInfo);
        }
        String descStr = "";
        if (userInfo.type.equals("2")) {
            if (TextUtils.isEmpty(userInfo.title)) {
                descStr = String.format("%s | 粉丝:%s | 收听:%s", "未填写", userInfo.followcount, userInfo.myfollowcount);
            } else {
                descStr = String.format("%s | 粉丝:%s | 收听:%s", userInfo.title, userInfo.followcount, userInfo.myfollowcount);
            }

        } else {
            if (TextUtils.isEmpty(userInfo.school)) {
                descStr = String.format("%s | 粉丝:%s | 收听:%s", "未填写", userInfo.followcount, userInfo.myfollowcount);
            } else {
                descStr = String.format("%s | 粉丝:%s | 收听:%s", userInfo.school, userInfo.followcount, userInfo.myfollowcount);
            }
        }
        username.setText(userInfo.username);
        content.setText(descStr);
        if (!TextUtils.isEmpty(userInfo.desc)) {
            user_desc.setText(userInfo.desc);
        } else {
            user_desc.setText("未填写");
        }
        head.setOnClickListener(this);
        //  LogUtils.LOGE("---",userInfo.userimage);
        PictureUtils.showPicture(getActivity(), userInfo.userimage, head);
        PictureUtils.showBackgroudPicture(getActivity(), userInfo.backgroundimage, background);

        if (userInfo.sex.equals("0")) {
            sex.setBackgroundResource(R.mipmap.sex_female);
        } else {
            sex.setBackgroundResource(R.mipmap.sex_male);
        }

        if (userInfo.type.equals("2")) {
            menu_item_guanyu.setVisibility(View.GONE);
        }
        ll_tabs.setVisibility(View.VISIBLE);
        if (userInfo.type.equals("1")) {  //1是学生

            teacherOnly.setVisibility(View.GONE);
        } else {

            teacherOnly.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head:
//                mMenuDialogPicHelper.show(v, head);

                if (AppShare.getIsLogin(getActivity())) {
                    Model.startNextAct(getActivity(),
                            InfoFragment.class.getName());
                } else {
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                }

                break;
            case R.id.logout:
                AppShare.setIsLogin(getActivity(), false);
                AppShare.clearShare(getActivity());
                head.setImageResource(R.mipmap.menu_head);
                background.setImageResource(R.mipmap.home_bg);
                initUI();

                break;
            case R.id.menu_item_wodeyijiayizhuye:

                Model.startNextAct(getActivity(),
                        HomePageFragment.class.getName());
                break;

            case R.id.menu_item_wodeyibi:
                Model.startNextAct(getActivity(),
                        PayforYBFragment.class.getName());
                break;
            case R.id.menu_item_woping:
                Model.startNextAct(getActivity(),
                        Tab5WopingListFragment.class.getName());
                break;
            case R.id.menu_item_woting:
                Model.startNextAct(getActivity(),
                        Tab5WotingFragment.class.getName());
                break;
            case R.id.menu_item_guanzhu:
                Model.startNextAct(getActivity(),
                        Tab5FollowedFragment.class.getName());
                break;
            case R.id.menu_item_jiesuanshuoming:

                Model.startNextAct(getActivity(),
                        JiesuanshuomingFragment.class.getName());
                break;
            case R.id.menu_item_wowen:

                Model.startNextAct(getActivity(),
                        Tab5WoWenListFragment.class.getName());
                break;
            case R.id.menu_item_guanyu:
                Intent in = new Intent(getActivity(), StubActivity.class);
                in.putExtra("fragment", ApplyTeacherFragment.class.getName());
                getActivity().startActivity(in);
                break;
            case R.id.menu_item_shezhi:
                Model.startNextAct(getActivity(),
                        SettingFragment.class.getName());
                break;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我");
    }
}
