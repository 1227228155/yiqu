package com.yiqu.iyijiayi.fragment.tab3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.base.utils.ToastManager;
import com.ui.abs.AbsFragment;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.SoundsTab1Adapter;
import com.db.DownloadMusicInfoDBHelper;
import com.model.Music;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SoundsTab2fragment extends AbsFragment implements View.OnClickListener,RefreshList.IRefreshListViewListener {

    private String tag = "SoundsTab2fragment";
    private RefreshList listView;
    private SoundsTab1Adapter soundsTab1Adapter;
    private ArrayList<Music> musics;
    private DownloadMusicInfoDBHelper downloadMusicInfoDBHelper;


    @Override
    public void onClick(View v) {

    }

    @Override
    protected int getContentView() {
        return R.layout.record_tab2_fragment;
    }

    @Override
    protected void initView(View v) {
        listView = (RefreshList) v.findViewById(R.id.tab1_list);
        soundsTab1Adapter = new SoundsTab1Adapter(getActivity());
        listView.setAdapter(soundsTab1Adapter);
        listView.setOnItemClickListener(soundsTab1Adapter);
        listView.setRefreshListListener(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadMusicInfoDBHelper = new DownloadMusicInfoDBHelper(getActivity());
        musics = downloadMusicInfoDBHelper.getAll();
        if (musics!=null&&musics.size()>0){
            soundsTab1Adapter.setData(musics);
        }
        MobclickAgent.onPageStart("声乐已下载");
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("声乐已下载");

    }

    @Override
    public void onRefresh() {
        musics = downloadMusicInfoDBHelper.getAll();
        if (musics!=null&&musics.size()>0){
            soundsTab1Adapter.setData(musics);
            resfreshOk();
        }else {
            resfreshFail();
            ToastManager.getInstance(getActivity()).showText("没有数据");
        }

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
