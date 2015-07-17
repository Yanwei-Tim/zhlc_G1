package com.zhlt.g1app.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpCommunityListView;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.data.DataCommon;
import com.zhlt.g1app.data.DataShare;
import com.zhlt.g1app.func.CommentCallBack;
import com.zhlt.g1app.view.ViewPicHead;
import com.zhlt.g1app.view.ViewXList;
import com.zhlt.g1app.view.ViewXList.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

public class FrgCommunity extends FrgBase implements IXListViewListener {

	private final int AD_SCROLL_DURATION_MILLS = 5000;// 切换间隔时间，单位秒

	private View mRootView;
	private ViewPager mViewPager = null;
	private Runnable mRunnable = null;
	private Logger log4jUtil = Log4jUtil.getLogger("");
	private ArrayList<String> mAdvImageLists = new ArrayList<String>();
	private ArrayList<String> mAdvTextLists = new ArrayList<String>();
	private ViewPager mTextViewPager;
	private ViewPager mImagerPager;

	// private FootPrintHandler mHandler;
	private Handler mHandler;

	private boolean mIsLoadAdvTextSuccess = false;
	private boolean mIsLoadAdvImageSuccess = false;

	private boolean mIsShowAdvTextSuccess = false;
	private boolean mIsShowAdvImageSuccess = false;

	private boolean mIsLoadListSuccess = false;
	private ViewXList mListView;
	private ViewPicHead mPicHeadView;
	private AdpCommunityListView mListViewAdapter;

	private String mHeadPic = "http://112.124.110.16:8080/apk/share_07.png";
	private String mContentPic = "http://112.124.110.16:8080/apk/share_09.png";

	private String[] mPics = { "http://112.124.110.16:8080/apk/01.png",
			"http://112.124.110.16:8080/apk/02.png",
			"http://112.124.110.16:8080/apk/03.png",
			"http://112.124.110.16:8080/apk/04.png",
			"http://112.124.110.16:8080/apk/05.png",
			"http://112.124.110.16:8080/apk/06.png", };
	private List<DataShare> mListShareDatas = new ArrayList<DataShare>();

	/** 底部评论框 */
	private View mCommentView;

	/** 发送评论 */
	private Button mCommentSendBtn;

	/** 评论编辑框 */
	private EditText mCommentEdit;

	private InputMethodManager imm;

	private CommentCallBack mCommentCallBack;

	private Button mPositionBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log4jUtil.info("zzw footprint onCreateView");

		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.frg_community, null);
		}

		// if (!mIsLoadAdvSuccess) {
		// getAdvData();
		// }
		if (!mIsPrepare) {
			initView();
		}
		// 如果加载完成并且没有设置适配
		// if (mIsLoadAdvImageSuccess && !mIsShowAdvImageSuccess) {
		// refreshAdvImageView();
		// }
		// if (mIsLoadAdvTextSuccess && !mIsShowAdvTextSuccess) {
		// refreshAdvTextView();
		// }
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);// 先移除
		}
		mIsPrepare = true;
		return mRootView;
	}

	public void setCommentCallBack(CommentCallBack callBack) {
		mCommentCallBack = callBack;
	}
	
	public void backToTop(){
		mListView.setSelection(0);
	}

	// private IXListViewListener mListViewListener = new IXListViewListener() {
	//
	// @Override
	// public void onRefresh() {
	// getListData();
	//
	// }
	//
	// @Override
	// public void onLoadMore() {
	// getListData();
	// }
	// };

	// private void getListData() {
	// log4jUtil.info("zzw FootPrint getListData");
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// for (int i = 0; i < 10; i++) {
	// List<HashMap<String, String>> mList = new ArrayList<HashMap<String,
	// String>>();
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("name", "超级大坏蛋");
	// map.put("comment", "哇，好给力啊！");
	// DataShare shareData = new DataShare("超级大好人",
	// "今天又遇到了碰瓷的，请大家注意这个人！！/喷怒！", mHeadPic, mContentPic,
	// mList);
	// mListShareDatas.add(shareData);
	// mHandler.sendEmptyMessageDelayed(
	// DataCommon.Message.MSG_SHARE_lOADFINISHED, 1000);
	// }
	// mListView.stopRefresh();
	// mListView.stopLoadMore();
	// mListView.setRefreshTime("刚刚");
	// }
	// }).start();
	//
	// }

	// private static class FootPrintHandler extends Handler {
	//
	// private WeakReference<FrgCommunity> reference;
	//
	// public FootPrintHandler(FrgCommunity fragment) {
	// reference = new WeakReference<FrgCommunity>(fragment);
	// }
	//
	// @Override
	// public void handleMessage(Message msg) {
	// FrgCommunity fragment = reference.get();
	// if (fragment == null) {
	// return;
	// }
	// switch (msg.what) {
	// case DataCommon.Message.MSG_ADV_TEXT_lOADFINISHED:
	// fragment.refreshAdvTextView();
	// break;
	// case DataCommon.Message.MSG_ADV_IMAGE_lOADFINISHED:
	// fragment.refreshAdvImageView();
	// break;
	//
	// case DataCommon.Message.MSG_SHARE_lOADFINISHED:
	// fragment.refershListView();
	// break;
	//
	// default:
	// break;
	// }
	//
	// }
	//
	// }

	// private void refershListView() {
	// mListView.stopRefresh();
	// mListViewAdapter.setData(mListShareDatas);
	// }

	// public void refreshAdvImageView() {
	// mIsShowAdvImageSuccess = true;
	// mImagerPager = new ViewPager(getActivity());
	// if (mIsShowAdvTextSuccess) {
	// mListView.addHeaderView(mImagerPager);
	// }
	// }

	public void setAdvTextData(ArrayList<String> list) {
		mIsLoadAdvTextSuccess = true;
		log4jUtil.info("setAdvTextData");
		mAdvTextLists = list;
		// 如果已经初始化显示过了,
		if (mIsPrepare == true) {
			mHandler.sendEmptyMessageDelayed(
					DataCommon.Message.MSG_ADV_TEXT_lOADFINISHED, 1000);
		}

	}

	// public void refreshAdvTextView() {
	// mIsShowAdvTextSuccess = true;
	// mTextViewPager = new ViewPager(getActivity());
	// mListView.addHeaderView(mTextViewPager);
	// if (mIsShowAdvImageSuccess) {
	// mListView.addHeaderView(mImagerPager);
	// }
	// }

	private void initView() {
		geneItems();
		mListView = (ViewXList) mRootView.findViewById(R.id.xListView);
		mPicHeadView = new ViewPicHead(getActivity());
		mListView.addHeaderView(mPicHeadView);
		mListView.setPullLoadEnable(true);
		mListViewAdapter = new AdpCommunityListView(getActivity(),
				mCommentCallBack);
		mListViewAdapter.setData(mListShareDatas);
		mListView.setAdapter(mListViewAdapter);
		mListView.setXListViewListener(this);
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mCommentCallBack != null) {
					mCommentCallBack.hideComment();
				}
				return false;
			}
		});
		mPositionBtn = (Button) mRootView.findViewById(R.id.btn_title_right);
		// mHandler = new FootPrintHandler(this);
		mHandler = new Handler();
	}

	@Override
	protected void lazyLoad() {
		if (!mIsPrepare || !mIsVisiable) {
			return;
		}
		// getListData();
	}

	private void geneItems() {
		for (int i = 0; i < 10; i++) {
			List<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = new HashMap<String, String>();
			ArrayList<String> mContentPic = new ArrayList<String>();
			if (i % 2 == 0) {
				map.put("name", "无敌大魔王");
				map.put("comment", "不要太好看了哦！");
				mContentPic.add(mPics[0]);

				mList.add(map);
				DataShare shareData = new DataShare("超级大好人",
						"今天又遇到了碰瓷的，请大家注意这个人！！/喷怒！", mHeadPic, mContentPic,
						mList);
				mListShareDatas.add(shareData);
			} else if (i % 3 == 0) {
				mContentPic.add(mPics[3]);
				mContentPic.add(mPics[4]);
				mContentPic.add(mPics[5]);
				map.put("name", "超级大坏蛋");
				map.put("comment", "哇，好给力啊！");
				mList.add(map);
				DataShare shareData = new DataShare("董小姐", "今天好高兴啊，又可以看美女了！！",
						mHeadPic, mContentPic, mList);
				mListShareDatas.add(shareData);
			} else {
				mContentPic.add(mPics[0]);
				mContentPic.add(mPics[1]);
				mContentPic.add(mPics[2]);
				mContentPic.add(mPics[3]);
				mContentPic.add(mPics[4]);
				mContentPic.add(mPics[5]);
				map.put("name", "超级大坏蛋");
				map.put("comment", "哇，好给力啊！");
				mList.add(map);
				DataShare shareData = new DataShare("董小姐", "今天好高兴啊，又可以看美女了！！",
						mHeadPic, mContentPic, mList);
				mListShareDatas.add(shareData);
			}

			// mHandler.sendEmptyMessageDelayed(
			// DataCommon.Message.MSG_SHARE_lOADFINISHED, 1000);
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mListShareDatas.clear();
				geneItems();
				// mAdapter.notifyDataSetChanged();
				mListViewAdapter = new AdpCommunityListView(getActivity(),
						mCommentCallBack);
				mListViewAdapter.setData(mListShareDatas);
				mListView.setAdapter(mListViewAdapter);
				onLoad();
			}
		}, 500);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems();
				mListViewAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 500);
	}

	public void onCommentSuccess(String comment) {
		mListViewAdapter.onCommentSuccess(comment);
	}

}
