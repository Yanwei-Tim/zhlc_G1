package com.zhlt.g1app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActShowPhoto;
import com.zhlt.g1app.basefunc.FileUtil;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.data.CarModelData;
import com.zhlt.g1app.data.DataShare;
import com.zhlt.g1app.fragment.FrgCommunity;
import com.zhlt.g1app.func.BitmapCache;
import com.zhlt.g1app.func.BitmapCache.IBitmapCacheCallback;
import com.zhlt.g1app.func.CommentCallBack;
import com.zhlt.g1app.func.FunAsyncBitmapLoader;
import com.zhlt.g1app.func.FunAsyncBitmapLoader.ImageCallBack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdpCommunityListView extends BaseAdapter {

	private List<DataShare> mList;
	private Context mContext;

	private FunAsyncBitmapLoader asyncLoader = null;

	private CommentCallBack mCommentCallBack;

	private ShareHolder mCommentHolder;

	private Animation mGoodAnimation;

	private int mNumColumns;
	private int mHorizontalSpacing = 0;
	private int mColumnsWidth = 0;

	private Logger mLogger = Log4jUtil.getLogger("");
	private BitmapCache mBitmapCache;

	public AdpCommunityListView(Context context, CommentCallBack callBack) {
		mContext = context;
		asyncLoader = new FunAsyncBitmapLoader();
		mCommentCallBack = callBack;
		mBitmapCache = BitmapCache.getSingleTon(mContext);
		(mBitmapCache).setLoadingBitmap(R.drawable.frg_community_load);
	}

	public void setData(List<DataShare> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	public void setHorizontalSpacing(int horizontalSpacing) {
		this.mHorizontalSpacing = horizontalSpacing;
		mLogger.info("setHorizontalSpacing:" + horizontalSpacing);
	}

	public void setColumnsWidth(int width) {
		mColumnsWidth = width;
		mBitmapCache.configBitmapSize(width, width);
		mLogger.info("setColumnsWidth:" + mColumnsWidth);
	}

	public void setColumnsNum(int num) {
		mNumColumns = num;
		mLogger.info("setColumnsNum:" + num);

	}

	@Override
	public int getCount() {
		return mList == null || mNumColumns == 0 ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ShareHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listitem_community, null);
			mHolder = new ShareHolder(convertView);
		} else {
			mHolder = (ShareHolder) convertView.getTag();
		}

		// Bitmap bitmap = asyncLoader.loadBitmap(mHolder.mContentIv,
		// mList.get(position).getContentPic(), new ImageCallBack() {
		// @Override
		// public void imageLoad(ImageView imageView, Bitmap bitmap) {
		// // TODO Auto-generated method stub
		// imageView.setImageBitmap(bitmap);
		// }
		// });
		Bitmap bitmap2 = asyncLoader.loadBitmap(mHolder.mPicIv,
				mList.get(position).getHeadPic(), new ImageCallBack() {
					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						// TODO Auto-generated method stub
						imageView.setImageBitmap(bitmap);
					}
				});

		if (bitmap2 != null) {
			// mHolder.mContentIv.setImageBitmap(bitmap);
			mHolder.mPicIv.setImageBitmap(bitmap2);
		} else {
			// mHolder.mContentIv.setImageResource(R.drawable.ic_launcher);
			mHolder.mPicIv.setImageResource(R.drawable.ic_launcher);
		}

		mHolder.mTextTv.setText("--" + position + "--FLING");
		addView(mHolder.mContentIv, position);
		mHolder.mNameTv.setText(mList.get(position).getName());
		mHolder.mTextTv.setText(mList.get(position).getText());
		setCommentLayout(mHolder, position);
		mHolder.mCommentIBtn.setOnClickListener(mOnClickListener);
		mHolder.mCommentIBtn.setTag(mHolder);
		mHolder.mCollectIBtn.setOnClickListener(mOnClickListener);
		// mHolder.mCollectIBtn.setTag(mHolder);
		mHolder.mGoodIBtn.setOnClickListener(mOnClickListener);
		mHolder.mGoodIBtn.setTag(mHolder);
		mHolder.setPosition(position);
		return convertView;
	}

	private IBitmapCacheCallback cacheCallback = new IBitmapCacheCallback() {

		@Override
		public Bitmap processBitmap(Bitmap bitmap) {
			return bitmap;
		}

		@Override
		public String getLocalPathnameByDummy(String URL) {
			return FileUtil.getImageLocalPath(URL);
		}
	};

	private void addView(LinearLayout layout, int position) {
		List<String> mPicList = mList.get(position).getContentPic();
		if (layout.getChildCount() > 0) {

			if (mPicList.size() == 0) {
				layout.removeAllViews();
				return;
			}

			if (layout.getChildCount() <= getChildCount(mPicList)) {
				getItem(layout, position);
				if (layout.getChildCount() < getChildCount(mPicList)) {
					addItemView(layout, position, layout.getChildCount());
				}
			} else {
				getItem(layout, position);
				if (layout.getChildCount() - getChildCount(mPicList) > 0) {
					layout.removeViews(getChildCount(mPicList),
							layout.getChildCount() - getChildCount(mPicList));
				}
			}
			return;
		}
		addItemView(layout, position, 0);
	}

	public void getItem(LinearLayout layout, int position) {
		int start1 = 0;
		List<String> mPicList = mList.get(position).getContentPic();
		LinearLayout view = null;
		// 当数据为一张图片时
		if (mPicList.size() == 1) {
			// 如果只有一行
			layout.removeAllViews();
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.FIT_XY);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			imageView.setTag(R.id.tag_community_image, position + "-" + 0);
			imageView.setOnClickListener(mOnClickListener);
			mBitmapCache.configBitmapSize(0, 0);
			loadImage(imageView, mPicList.get(0));
			layout.addView(imageView, layoutParams);

		} else {
			mBitmapCache.configBitmapSize(mColumnsWidth, mColumnsWidth);
			for (int i = 0; i < layout.getChildCount(); i++) {
				start1 = i * mNumColumns;
				if (layout.getChildAt(i) instanceof LinearLayout) {
					view = (LinearLayout) layout.getChildAt(i);
					view.removeAllViews();
				} else {
					layout.removeAllViews();
					view = new LinearLayout(mContext);
				}
				for (int j = 0; j < mNumColumns
						&& (start1 + j) < mPicList.size(); j++) {
					ImageView imageView = new ImageView(mContext);
					imageView.setScaleType(ScaleType.FIT_XY);
					imageView.setTag(R.id.tag_community_image, position + "-"
							+ j);
					LayoutParams layoutParams = new LayoutParams(mColumnsWidth,
							mColumnsWidth);
					layoutParams.rightMargin = mHorizontalSpacing;
					imageView.setOnClickListener(mOnClickListener);
					loadImage(imageView, mPicList.get(start1 + j));
					view.addView(imageView, layoutParams);
				}
				if (view.getParent() == null) {
					layout.addView(view);
				}
			}
		}

	}

	/**
	 * 
	 * @param mPicList
	 * @return
	 */
	private int getChildCount(List<String> mPicList) {
		return mPicList.size() % mNumColumns == 0 ? mPicList.size()
				/ mNumColumns : mPicList.size() / mNumColumns + 1;
	}

	private void addItemView(LinearLayout layout, int position, int start) {
		List<String> mPicList = mList.get(position).getContentPic();
		int count = 0;
		int start1 = 0;
		int end = 0;
		if (mPicList.size() != 0) {
			count = getChildCount(mPicList);
		}
		if (mPicList.size() < mNumColumns) {
			if (mPicList.size() == 1) {
				mBitmapCache.configBitmapSize(0, 0);
				ImageView imageView = new ImageView(mContext);
				imageView.setScaleType(ScaleType.FIT_XY);
				LayoutParams layoutParams = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				imageView.setTag(R.id.tag_community_image, position + "-" + 0);
				imageView.setOnClickListener(mOnClickListener);
				loadImage(imageView, mPicList.get(0));
				layout.addView(imageView, layoutParams);
			} else {
				mBitmapCache.configBitmapSize(mColumnsWidth, mColumnsWidth);
				LinearLayout linearLayout = new LinearLayout(mContext);
				LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				lp.topMargin = mHorizontalSpacing;
				for (int j = 0; j < mPicList.size(); j++) {
					ImageView imageView = new ImageView(mContext);
					imageView.setScaleType(ScaleType.FIT_XY);
					imageView.setTag(R.id.tag_community_image, position + "-"
							+ j);
					imageView.setScaleType(ScaleType.FIT_XY);
					LayoutParams layoutParams = new LayoutParams(mColumnsWidth,
							mColumnsWidth);
					layoutParams.rightMargin = mHorizontalSpacing;
					imageView.setOnClickListener(mOnClickListener);
					loadImage(imageView, mPicList.get(j));
					linearLayout.addView(imageView, layoutParams);
				}
				layout.addView(linearLayout, lp);
			}
		} else {
			mBitmapCache.configBitmapSize(mColumnsWidth, mColumnsWidth);
			for (int i = start; i < count; i++) {
				start1 = i * mNumColumns;
				LinearLayout linearLayout = new LinearLayout(mContext);
				LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				lp.topMargin = mHorizontalSpacing;
				for (int j = 0; j < mNumColumns
						&& (start1 + j) < mPicList.size(); j++) {
					ImageView imageView = new ImageView(mContext);
					imageView.setScaleType(ScaleType.FIT_XY);
					imageView.setTag(R.id.tag_community_image, position + "-"
							+ (start1 + j));
					LayoutParams layoutParams = new LayoutParams(mColumnsWidth,
							mColumnsWidth);
					layoutParams.rightMargin = mHorizontalSpacing;
					imageView.setOnClickListener(mOnClickListener);
					loadImage(imageView, mPicList.get(start1 + j));
					linearLayout.addView(imageView, layoutParams);
				}
				layout.addView(linearLayout, lp);
			}
		}

	}

	private void loadImage(ImageView imageView, String url) {
		mBitmapCache.loadBitmap(imageView, url, cacheCallback);
	}

	/**
	 * 评论成功，并在列表里显示
	 */
	public void onCommentSuccess(String value) {
		if (mCommentHolder != null) {
			int position = mCommentHolder.position;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "无敌大魔王");
			map.put("comment", value);
			mList.get(position).getList().add(map);
			notifyDataSetChanged();
		}
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String tag = (String) v.getTag(R.id.tag_community_image);
			if (!TextUtils.isEmpty(tag)) {
				viewImage(tag);
			}
			mCommentHolder = (ShareHolder) v.getTag();
			if (mCommentHolder != null) {
				switch (v.getId()) {
				case R.id.ibtn_xlistview_comment:
					if (mCommentCallBack != null) {
						mCommentCallBack.showComment();
					}
					break;
				case R.id.ibtn_xlistview_good:
					startGoodFlow();
					break;

				default:
					break;
				}
			}

		}
	};

	/**
	 * 浏览图片
	 */
	private void viewImage(String tag) {
		if (TextUtils.isEmpty(tag) || !tag.contains("-")) {
			return;
		}
		int center = tag.indexOf("-");
		int position = Integer.valueOf(tag.substring(0, center));
		int index = Integer.valueOf(tag.substring(center+1));
		ArrayList<String> mPicList = mList.get(position).getContentPic();
		Intent mIntent = new Intent(mContext, ActShowPhoto.class);
		mIntent.putStringArrayListExtra("list", mPicList);
		mIntent.putExtra("index", index);
		mContext.startActivity(mIntent);
	}

	/**
	 * 开始点赞动画
	 */
	private void startGoodFlow() {

		if (mCommentHolder == null) {
			return;
		}
		mCommentHolder.mGoodTv.setText((Integer.valueOf(mCommentHolder.mGoodTv
				.getText().toString()) + 1) + "");
		mCommentHolder.mGoodFlowIBtn.setVisibility(View.VISIBLE);
		if (mGoodAnimation == null) {
			mGoodAnimation = AnimationUtils.loadAnimation(mContext,
					R.anim.good_flow);
			mGoodAnimation.setAnimationListener(mAnimationListener);
		}
		mCommentHolder.mGoodFlowIBtn.startAnimation(mGoodAnimation);
	}

	private AnimationListener mAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mCommentHolder.mGoodFlowIBtn.setVisibility(View.GONE);

		}
	};

	private void setCommentLayout(ShareHolder mHolder, int position) {
		List<HashMap<String, String>> temp;
		temp = mList.get(position).getList();
		mHolder.mLinearLayout.removeAllViews();
		if (temp != null) {
			for (int i = 0; i < temp.size(); i++) {

				View view = LayoutInflater.from(mContext).inflate(
						R.layout.item_comment_content, null);
				TextView comment = (TextView) view
						.findViewById(R.id.tv_comment_value);
				TextView name = (TextView) view
						.findViewById(R.id.tv_comment_name);
				name.setText(temp.get(i).get("name") + "：");
				comment.setText(temp.get(i).get("comment"));
				mHolder.mLinearLayout.addView(view);
			}
		}
	}

	private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(
			android.R.color.transparent);

	private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
		final TransitionDrawable transitionDrawable = new TransitionDrawable(
				new Drawable[] { TRANSPARENT_DRAWABLE,
						new BitmapDrawable(imageView.getResources(), bitmap) });
		imageView.setImageDrawable(transitionDrawable);
		transitionDrawable.startTransition(500);
	}

	private class ShareHolder {
		public TextView mNameTv;
		public TextView mTextTv;
		public LinearLayout mLinearLayout;
		public ImageView mPicIv;
		public LinearLayout mContentIv;
		public ImageButton mCommentIBtn;
		public ImageButton mGoodIBtn;
		public ImageButton mGoodFlowIBtn;
		public CheckBox mCollectIBtn;
		public TextView mCommentValueTv;
		public TextView mGoodTv;
		public int position;

		public ShareHolder(View view) {
			mNameTv = (TextView) view.findViewById(R.id.tv_xlistview_name);
			mTextTv = (TextView) view.findViewById(R.id.tv_xlistview_text);
			mCommentValueTv = (TextView) view
					.findViewById(R.id.tv_xlistview_comment_value);
			mGoodTv = (TextView) view
					.findViewById(R.id.tv_xlistview_good_value);
			mLinearLayout = (LinearLayout) view
					.findViewById(R.id.llyt_xlistview_comment);
			mContentIv = (LinearLayout) view
					.findViewById(R.id.llyt_xlistview_content);
			mPicIv = (ImageView) view.findViewById(R.id.ibtn_xlistitem_pic);

			mCommentIBtn = (ImageButton) view
					.findViewById(R.id.ibtn_xlistview_comment);
			mGoodIBtn = (ImageButton) view
					.findViewById(R.id.ibtn_xlistview_good);
			mGoodFlowIBtn = (ImageButton) view
					.findViewById(R.id.ibtn_xlistview_goodflow);
			mCollectIBtn = (CheckBox) view
					.findViewById(R.id.ibtn_xlistview_collect);
			view.setTag(this);
		}

		public void setPosition(int i) {
			position = i;
		}

	}

}
