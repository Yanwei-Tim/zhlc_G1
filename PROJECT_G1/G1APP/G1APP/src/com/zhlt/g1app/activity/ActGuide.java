package com.zhlt.g1app.activity;

import java.io.InputStream;
import java.util.ArrayList;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpGuide;
import com.zhlt.g1app.basefunc.BaseFuncView;
import com.zhlt.g1app.basefunc.SharePreferUtil;
import com.zhlt.g1app.data.DataUser;
import com.zhlt.g1app.fragment.FrgActMain;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ActGuide extends Activity implements OnClickListener,
		OnPageChangeListener {
	// 定义ViewPager对象
	private ViewPager viewPager;

	// 定义ViewPager适配器
	private AdpGuide vpAdapter;

	// 定义一个ArrayList来存放View
	private ArrayList<View> views;

//	// 引导图片资源
//	private static final int[] pics = { R.drawable.act_guide_adv_1,
//			R.drawable.act_guide_adv_2, R.drawable.act_guide_adv_3 };

	// 引导图片资源
		private static final int[] pics = { R.drawable.propaganda_01,
				R.drawable.propaganda_02, R.drawable.propaganda_03 };

	// 底部小点的图片
	private ImageView[] points;

	// 记录当前选中位置
	private int currentIndex;

	// 用户数据
	private DataUser mUserData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_guide);
		if(!isFistStart()){
			enterFlash();
		}
		initView();

		initData();
		
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 实例化ArrayList对象
		views = new ArrayList<View>();

		// 实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		// 实例化ViewPager适配器
		vpAdapter = new AdpGuide(views);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Toast.makeText(getApplicationContext(), (arg0 == viewPager.getChildCount() - 1)+" "+arg2, 1).show();
			       if (arg0 == viewPager.getChildCount() - 1) {
					if (arg2 < 0) {
						saveToSharepref();
						goNext();
					}
				}
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 定义一个布局并设置参数
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		// 初始化引导图片列表
		for (int i = 0; i < pics.length - 1; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
//			iv.setImageResource(pics[i]);
			BaseFuncView.setBGResource(iv,pics[i]);
			views.add(iv);
		}
		LayoutInflater lLayInf = LayoutInflater.from(this);
		View lViewLast = lLayInf.inflate(R.layout.act_guide_last, null);
//		lViewLast.setBackgroundResource(0);
//		lViewLast.setBackgroundResource(pics[pics.length - 1]);
		BaseFuncView.setBGResource(lViewLast,pics[pics.length - 1]);
		lViewLast.findViewById(R.id.r_act_guide_start).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						saveToSharepref();
						goNext();

					}
				});
		views.add(lViewLast);

		// 设置数据
		viewPager.setAdapter(vpAdapter);
		// 设置监听
		viewPager.setOnPageChangeListener(this);

		// 初始化底部小点
		initPoint();
	}

	/**
	 * 进入下一个页面
	 */
	private void goNext() {
		if (hasLogin()) {
			enterMain();
		} else {
			enterLogin();
		}
	}

	/**
	 * 进入主界面
	 */
	private void enterFlash() {
		Intent lIntent = new Intent(this, ActFlash.class);
		// Intent lIntent = new Intent(this, FrgActMain.class);
		startActivity(lIntent);
		// overridePendingTransition(R.anim.anim_alpha_in,
		// R.anim.anim_alpha_out);
		finish();
	}

	private void enterMain() {
		Intent lIntent = new Intent(this, FrgActMain.class);
		// Intent lIntent = new Intent(this, OverlayDemo.class);
		startActivity(lIntent);
		overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);
		finish();
	}
	/**
	 * 跳转到登入界面
	 */
	private void enterLogin() {
		Intent lIntent = new Intent(this, ActLogin.class);
		startActivity(lIntent);
		// overridePendingTransition(R.anim.anim_alpha_in,
		// R.anim.anim_alpha_out);
		finish();
	}

	/**
	 * 
	 * @return false = 用户未登入
	 */
	private boolean hasLogin() {
		mUserData = SharePreferUtil.getUserData(getApplicationContext());
		return mUserData == null ? false : true;
	}

	/**
	 * 初始化底部小点
	 */
	private void initPoint() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);

		points = new ImageView[pics.length];

		// 循环取得小点图片
		for (int i = 0; i < pics.length; i++) {
			// 得到一个LinearLayout下面的每一个子元素
			points[i] = (ImageView) linearLayout.getChildAt(i);
			// 默认都设为灰色
			points[i].setEnabled(true);
			// 给每个小点设置监听
			points[i].setOnClickListener(this);
			// 设置位置tag，方便取出与当前位置对应
			points[i].setTag(i);
		}

		// 设置当面默认的位置
		currentIndex = 0;
		// 设置为白色，即选中状态
		points[currentIndex].setEnabled(false);
	}

	/**
	 * 当滑动状态改变时调用
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	/**
	 * 当当前页面被滑动时调用
	 */

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/**
	 * 当新的页面被选中时调用
	 */

	@Override
	public void onPageSelected(int position) {
		// 设置底部小点选中状态
		setCurDot(position);
	}

	/**
	 * 通过点击事件来切换当前的页面
	 */
	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}

	/**
	 * 设置当前页面的位置
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= pics.length) {
			return;
		}
		viewPager.setCurrentItem(position);
	}

	/**
	 * 设置当前的小点的位置
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
			return;
		}
		points[positon].setEnabled(false);
		points[currentIndex].setEnabled(true);

		currentIndex = positon;
	}

	private void saveToSharepref() {
		SharedPreferences sharedPreferences = getSharedPreferences("g1start", 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("isFirstIn", false);
		editor.commit();
	}

	private boolean isFistStart() {
		SharedPreferences sharedPreferences = getSharedPreferences("g1start", 0);
		return sharedPreferences.getBoolean("isFirstIn", true);
	}
}