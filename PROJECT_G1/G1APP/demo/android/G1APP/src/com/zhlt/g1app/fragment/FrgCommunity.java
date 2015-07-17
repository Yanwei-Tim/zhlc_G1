package com.zhlt.g1app.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpCommunityListView;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.data.CommonData;
import com.zhlt.g1app.data.ShareData;
import com.zhlt.g1app.view.ViewPicHead;
import com.zhlt.g1app.view.ViewXList;
import com.zhlt.g1app.view.ViewXList.IXListViewListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class FrgCommunity extends FrgBase {

	private final int AD_SCROLL_DURATION_MILLS = 5000;// 切换间隔时间，单位秒

	private View mRootView;
	private ViewPager mViewPager = null;
	private Runnable mRunnable = null;
	private Logger log4jUtil = Log4jUtil.getLogger("");
	private ArrayList<String> mAdvImageLists = new ArrayList<String>();
	private ArrayList<String> mAdvTextLists = new ArrayList<String>();
	private ViewPager mTextViewPager;
	private ViewPager mImagerPager;

	private FootPrintHandler mHandler;

	private boolean mIsLoadAdvTextSuccess = false;
	private boolean mIsLoadAdvImageSuccess = false;

	private boolean mIsShowAdvTextSuccess = false;
	private boolean mIsShowAdvImageSuccess = false;

	private boolean mIsLoadListSuccess = false;
	private ViewXList mListView;
	private ViewPicHead mPicHeadView;
	private AdpCommunityListView mListViewAdapter;

	private String mHeadPic = "http://image.haosou.com/v?q=%E5%B8%85%E6%B0%94%E7%94%B7%E7%94%9F%E5%A4%B4%E5%83%8F&src=relation_qqimage&fromurl=http%3A%2F%2Fwww.2cto.com%2Fqq%2F201301%2F181941.html#q=%E5%B8%85%E6%B0%94%E7%94%B7%E7%94%9F%E5%A4%B4%E5%83%8F&src=relation_qqimage&fromurl=http%3A%2F%2Fwww.2cto.com%2Fqq%2F201301%2F181941.html&lightboxindex=1&id=f9fd316213ac39dfb3af5a10e215eb0b&multiple=0&itemindex=0&dataindex=1";
	private String mContentPic = "http://image.haosou.com/zv?ch=go&t1=396&t2=491#ch=go&t1=396&t2=491&lightboxindex=0&groupid=4c56fda8f09f693f4bbd9e7621bff404&itemindex=0&dataindex=120";
	private List<ShareData> mListShareDatas = new ArrayList<ShareData>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log4jUtil.info("zzw footprint onCreateView");

		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.frg_community, null);
		}
		mHandler = new FootPrintHandler(this);
		// if (!mIsLoadAdvSuccess) {
		// getAdvData();
		// }
		if (!mIsPrepare) {
			initView();
		}
		// 如果加载完成并且没有设置适配
		if (mIsLoadAdvImageSuccess && !mIsShowAdvImageSuccess) {
			refreshAdvImageView();
		}
		if (mIsLoadAdvTextSuccess && !mIsShowAdvTextSuccess) {
			refreshAdvTextView();
		}
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);// 先移除
		}
		mIsPrepare = true;
		return mRootView;
	}

	private IXListViewListener mListViewListener = new IXListViewListener() {

		@Override
		public void onRefresh() {
			getListData();

		}

		@Override
		public void onLoadMore() {
			getListData();
		}
	};

	private void getListData() {
		log4jUtil.info("zzw FootPrint getListData");
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					List<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("name", "超级大坏蛋");
					map.put("comment", "哇，好给力啊！");
					ShareData shareData = new ShareData("超级大好人",
							"今天又遇到了碰瓷的，请大家注意这个人！！/喷怒！", mHeadPic, mContentPic,
							mList);
					mListShareDatas.add(shareData);
					mHandler.sendEmptyMessageDelayed(
							CommonData.Message.MSG_SHARE_lOADFINISHED, 1000);
				}

			}
		}).start();

	}

	private static class FootPrintHandler extends Handler {

		private WeakReference<FrgCommunity> reference;

		public FootPrintHandler(FrgCommunity fragment) {
			reference = new WeakReference<FrgCommunity>(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			FrgCommunity fragment = reference.get();
			if (fragment == null) {
				return;
			}
			switch (msg.what) {
			case CommonData.Message.MSG_ADV_TEXT_lOADFINISHED:
				fragment.refreshAdvTextView();
				break;
			case CommonData.Message.MSG_ADV_IMAGE_lOADFINISHED:
				fragment.refreshAdvImageView();
				break;

			case CommonData.Message.MSG_SHARE_lOADFINISHED:
				fragment.refershListView();
				break;

			default:
				break;
			}

		}

	}

	private void refershListView() {
		mListView.stopRefresh();
		mListViewAdapter.setData(mListShareDatas);
	}

	public void refreshAdvImageView() {
		mIsShowAdvImageSuccess = true;
		mImagerPager = new ViewPager(getActivity());
		if (mIsShowAdvTextSuccess) {
			mListView.addHeaderView(mImagerPager);
		}
	}

	public void setAdvTextData(ArrayList<String> list) {
		mIsLoadAdvTextSuccess = true;
		log4jUtil.info("setAdvTextData");
		mAdvTextLists = list;
		// 如果已经初始化显示过了,
		if (mIsPrepare == true) {
			mHandler.sendEmptyMessageDelayed(
					CommonData.Message.MSG_ADV_TEXT_lOADFINISHED, 1000);
		}

	}

	private void setAdvImageData(ArrayList<String> list) {
		mIsLoadAdvImageSuccess = true;
		log4jUtil.info("setAdvImageData");
		mAdvImageLists = list;
		// 如果已经初始化显示过了,
		if (mIsPrepare == true) {
			mHandler.sendEmptyMessageDelayed(
					CommonData.Message.MSG_ADV_IMAGE_lOADFINISHED, 1000);
		}
	}

	public void refreshAdvTextView() {
		mIsShowAdvTextSuccess = true;
		mTextViewPager = new ViewPager(getActivity());
		mListView.addHeaderView(mTextViewPager);
		if (mIsShowAdvImageSuccess) {
			mListView.addHeaderView(mImagerPager);
		}
	}

	private void initView() {
		mListView = (ViewXList) mRootView.findViewById(R.id.xListView);
		mPicHeadView = new ViewPicHead(getActivity());
		mListView.addHeaderView(mPicHeadView);

		mListView.setPullLoadEnable(true);
		mListViewAdapter = new AdpCommunityListView(getActivity());
		mListViewAdapter.setData(mListShareDatas);
		mListView.setAdapter(mListViewAdapter);
		mListView.setXListViewListener(mListViewListener);
	}

	@Override
	protected void lazyLoad() {
		if (!mIsPrepare || !mIsVisiable) {
			return;
		}
		getListData();
	}

}
