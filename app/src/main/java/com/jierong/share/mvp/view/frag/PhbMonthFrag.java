package com.jierong.share.mvp.view.frag;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jierong.share.BaseFrag;
import com.jierong.share.Constants;
import com.jierong.share.R;
import com.jierong.share.mvp.model.info.MonthBuyInfo;
import com.jierong.share.mvp.model.info.MonthClickInfo;
import com.jierong.share.mvp.model.info.MonthFanInfo;
import com.jierong.share.mvp.presenter.PhbMonthPresenter;
import com.jierong.share.mvp.view.IPhbMonthView;
import com.jierong.share.mvp.view.ada.MonthBuyAdapter;
import com.jierong.share.mvp.view.ada.MonthClickAdapter;
import com.jierong.share.mvp.view.ada.MonthFanAdapter;
import com.jierong.share.refresh.MaterialHeader;
import com.jierong.share.util.ToastUtils;
import com.jierong.share.widget.LoadingDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

public class PhbMonthFrag extends BaseFrag implements IPhbMonthView, OnRefreshListener {
    private boolean beginToLoad = false;    // 是否加载数据
    private Dialog mLoadingDialog;
    private SmartRefreshLayout refreshView;
    private RecyclerView rv_list_click, rv_list_buy, rv_list_fan;
    private PhbMonthPresenter mPhbMonthPresenter;
    private MonthClickAdapter mMonthClickAdapter;
    private List<MonthClickInfo> clickData;
    private MonthBuyAdapter mMonthBuyAdapter;
    private List<MonthBuyInfo> buyData;
    private MonthFanAdapter mMonthFanAdapter;
    private List<MonthFanInfo> fanData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_phb_month, null);
        initView(view);
        return view;
    }

    @Override
    public void onNetNo() { }

    @Override
    public void onNetOk() { }

    private void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.createLoadingDialog(getActivity(), "正在加载中...");
            mLoadingDialog.show();
        }
    }

    private void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    private void initAdapter() {
        mMonthClickAdapter = new MonthClickAdapter(clickData);
        mMonthClickAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rv_list_click.setAdapter(mMonthClickAdapter);

        mMonthBuyAdapter = new MonthBuyAdapter(buyData);
        mMonthBuyAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rv_list_buy.setAdapter(mMonthBuyAdapter);

        mMonthFanAdapter = new MonthFanAdapter(fanData);
        mMonthFanAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rv_list_fan.setAdapter(mMonthFanAdapter);
    }

    private void initView(View view) {
        mPhbMonthPresenter = new PhbMonthPresenter(this);
        rv_list_click = (RecyclerView) view.findViewById(R.id.rv_list_click);
        rv_list_buy = (RecyclerView) view.findViewById(R.id.rv_list_buy);
        rv_list_fan = (RecyclerView) view.findViewById(R.id.rv_list_fan);
        refreshView = (SmartRefreshLayout) view.findViewById(R.id.refreshView);
        refreshView.setRefreshHeader(new MaterialHeader(getMContext())
                .setColorSchemeColors(getActivity().getResources().getColor(R.color.bottom_text_click)));
        refreshView.setEnableHeaderTranslationContent(false);
        refreshView.setDisableContentWhenRefresh(true);
        refreshView.setOnRefreshListener(this);

        rv_list_click.setLayoutManager(new LinearLayoutManager(getMContext()));
        rv_list_click.setHasFixedSize(true);
        rv_list_buy.setLayoutManager(new LinearLayoutManager(getMContext()));
        rv_list_buy.setHasFixedSize(true);
        rv_list_fan.setLayoutManager(new LinearLayoutManager(getMContext()));
        rv_list_fan.setHasFixedSize(true);
        initAdapter();
        beginToLoad = true;
        if(getUserVisibleHint()) {
            showLoading();
            mPhbMonthPresenter.loadData();
            beginToLoad = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!beginToLoad) {

        } else {
            showLoading();
            mPhbMonthPresenter.loadData();
            beginToLoad = false;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPhbMonthPresenter.loadData();
    }

    @Override
    public Context getMContext() {
        return getActivity();
    }

    @Override
    public void getDataSuccess(String time, List<MonthClickInfo> click, List<MonthBuyInfo> buy, List<MonthFanInfo> fan) {
        Intent timeIntent = new Intent();
        timeIntent.setAction(Constants.Refresh_Phb_time);
        timeIntent.putExtra("updateType", "monthData");
        timeIntent.putExtra("monthTime", time);
        getActivity().sendBroadcast(timeIntent);
        hideLoading();
        if(refreshView.isRefreshing()) refreshView.finishRefresh();

        this.clickData = click;
        mMonthClickAdapter.getData().clear();
        mMonthClickAdapter.setNewData(clickData);

        this.buyData = buy;
        mMonthBuyAdapter.getData().clear();
        mMonthBuyAdapter.setNewData(buyData);

        this.fanData = fan;
        mMonthFanAdapter.getData().clear();
        mMonthFanAdapter.setNewData(fanData);
    }

    @Override
    public void showError(String msg, boolean flag) {
        hideLoading();
        ToastUtils.show(getMContext(), msg);
        if (flag) getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideLoading();
        mPhbMonthPresenter.closeHttp();
    }

}
