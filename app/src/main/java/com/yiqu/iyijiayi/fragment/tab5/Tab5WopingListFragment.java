package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab5WopingAdapter;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.JsonUtils;

import java.util.ArrayList;

public class Tab5WopingListFragment extends AbsAllFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener {


    private Tab5WopingAdapter tab5WopingAdapter;
    private String tag = "Tab5WopingListFragment";
    private ArrayList<Sound> datas;
    private Context mContext;

    //分页
    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;

    //刷新
    private RefreshList listView;
    private String arr;
    private UserInfo userInfo;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_listview;
    }

    @Override
    protected void initView(View v) {
        mContext = getActivity();
        listView = (RefreshList) v.findViewById(R.id.lv_sound);

        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);

        listView.addFooterView(mLoadMoreView);
        listView.setOnScrollListener(mLoadMoreView);

        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
    }

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }



    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我评列表");
    }

    @Override
    public void onResume() {
        MobclickAgent.onPageStart("我评列表"); //统计页面，"MainScreen"为页面名称，可自定义

        super.onResume();
        NSDictionary nsDictionary = new NSDictionary();
        nsDictionary.isopen = "1";
        nsDictionary.ispay = "1";
        nsDictionary.status = "1";
        nsDictionary.stype = "1";
        userInfo = AppShare.getUserInfo(mContext);
        nsDictionary.touid = userInfo.uid;
        Gson gson = new Gson();
        arr = gson.toJson(nsDictionary);
        count = 0;

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "edited", "desc",userInfo.uid),
                "getSoundList", Tab5WopingListFragment.this,false,true);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        tab5WopingAdapter = new Tab5WopingAdapter(getActivity());
        listView.setAdapter(tab5WopingAdapter);
        listView.setRefreshListListener(this);
        listView.setOnItemClickListener(tab5WopingAdapter);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT | FLAG_BACK;
    }

    @Override
    protected boolean onPageBack() {
        return false;
    }

    @Override
    protected boolean onPageNext() {
        pageNextComplete();
        return true;
    }

    @Override
    protected void initTitle() {
        setTitleText(getString(R.string.dianping));
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("getSoundList")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                try {
                    datas=JsonUtils.parseSoundList(netResponse.data);
                    tab5WopingAdapter.setData(datas);
                    if (datas.size() == rows) {
                        mLoadMoreView.setMoreAble(true);
                    }
                    count += rows;
                    resfreshOk();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ToastManager.getInstance(getActivity()).showText(getString(R.string.no_history));
                resfreshFail();
            }
        } else if ("getSoundList_more".equals(id)) {
            if (TYPE_SUCCESS == type) {

                try {
//                    datas.addAll(JsonUtils.parseXizuoList(netResponse.data));
                    if (datas.size() < rows) {
                        mLoadMoreView.setMoreAble(false);
                    }
                    count += rows;
                    mLoadMoreView.end();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {
                mLoadMoreView.end();
                mLoadMoreView.setMoreAble(false);
            }
        }
        super.onNetEnd(id, type, netResponse);
    }

    @Override
    public void onRefresh() {

        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        count = 0;
        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "edited", "desc",userInfo.uid),
                "getSoundList", Tab5WopingListFragment.this,false,true);

    }

    @Override
    public boolean onMore(AbsListView view) {
        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();

                RestNetCallHelper.callNet(
                        getActivity(),
                        MyNetApiConfig.getSoundList,
                        MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "edited", "desc",userInfo.uid),
                        "getSoundList_more", Tab5WopingListFragment.this,false,true);

            }
        }


        return false;
    }

    public void resfreshOk() {
        listView.refreshOk();
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                listView.stopRefresh();
            }


        }.execute();

    }

    public void resfreshFail() {
        listView.refreshFail();
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                listView.stopRefresh();
            }


        }.execute();
    }


}