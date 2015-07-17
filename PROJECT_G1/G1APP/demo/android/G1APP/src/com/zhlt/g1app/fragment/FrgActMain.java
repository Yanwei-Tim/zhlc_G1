package com.zhlt.g1app.fragment;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActHistoryTrack;
import com.zhlt.g1app.adapter.AdpFrgActMain;
import com.zhlt.g1app.basefunc.UpdateManager;
import com.zhlt.g1app.data.CommonData;
import com.zhlt.g1app.view.ViewXList;

public class FrgActMain extends FragmentActivity {
	private ViewPager mPager;
	private ArrayList<Fragment> mFragments;
	private AdpFrgActMain mAdapter;
	private ArrayList<RadioButton> btnTitles;
	private ArrayList<String> mAdvImageLists = new ArrayList<String>();

	private FrgCarState mFrgCarState;
	private FrgCamera mFrgCamera;
	private FrgCommunity mFrgCommunity;
	private FrgSettings mFrgSettings;
	private TextView mTopTitle;
	private Button mTitleLeft;
	private Button mTitleRight;

	

 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.frgact_main);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.initView();
//		getAdvImageData();
//		getAdvTextData();
		this.setListener();
		this.setCurrentPage(0);

		DisplayMetrics outMetrics = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		Log.d("zzw", outMetrics.widthPixels + " " + outMetrics.heightPixels);
		
		// 动画效果启动
//		ImageView animationIV;
//		AnimationDrawable animationDrawable;
//		animationIV = (ImageView) findViewById(R.id.iv_guide_ad);
//		animationIV.setImageResource(R.drawable.anim_g1);
//		animationDrawable = (AnimationDrawable) animationIV.getDrawable();
//		animationDrawable.start();
		isupdate();
	}

	

	


	private void getAdvImageData() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				mAdvImageLists
						.add("http://image.haosou.com/zv?ch=go&t1=396&t2=491#ch=go&t1=396&t2=491&lightboxindex=0&groupid=4c56fda8f09f693f4bbd9e7621bff404&itemindex=0&dataindex=120");
			}
		}).start();
	}

	private void getAdvTextData() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				mAdvImageLists.add("13名密切接触者失联");
				mFrgCarState.setAdvTextData(mAdvImageLists);
			}
		}).start();
	}

	private void initView() {
		mPager = (ViewPager) findViewById(R.id.r_vps_frg_act_main);
		mPager.setOffscreenPageLimit(0);
		
		mFragments = new ArrayList<Fragment>();
		mFrgCarState = new FrgCarState();
		mFrgCamera = new FrgCamera();
		mFrgCommunity = new FrgCommunity();
		mFrgSettings = new FrgSettings();
		mFragments.add(mFrgCarState);
		mFragments.add(mFrgCamera);
		mFragments.add(mFrgCommunity);
		mFragments.add(mFrgSettings);
		this.setViewPager();

		btnTitles = new ArrayList<RadioButton>();
		mTopTitle = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleLeft = (Button) findViewById(R.id.r_ib_title_left);
		mTitleRight = (Button) findViewById(R.id.r_ib_title_right);
		mTitleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startHistoryTrack();
			}
		});
		initTitle();
	}
	
	private void startHistoryTrack() {
		Intent lIntent = new Intent(this, ActHistoryTrack.class);
		startActivity(lIntent);
//		overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);
//		finish();
	}

	private void setViewPager() {
		mAdapter = new AdpFrgActMain(getSupportFragmentManager(), mFragments);
		mPager.setAdapter(mAdapter);
		mPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
//				if(arg0.getId()==) {
//                        return true;        
//                }
				return false;
			}
		});
	}

	private void setListener() {
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int id) {
				setHeadandFoot(id);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * 初始化几个用来显示title的RadioButton
	 */
	private void initTitle() {
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_car_state));
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_camera));
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_community));
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_settings));
		for (int i = 0; i < btnTitles.size(); i++) {
			btnTitles.get(i).setOnClickListener(
					new FooterOnClickListener(i, false));
		}
	}

	private int[] mTitles = { R.string.car_state, R.string.camera,
			R.string.community, R.string.settings };

	/**
	 * 根据id设置切换页面和底部菜单
	 * 
	 * @param currentId
	 */
	public void setCurrentPage(int pCurrentId) {
		mPager.setCurrentItem(pCurrentId);
		setHeadandFoot(pCurrentId);
	}

	private void setHeadandFoot(int pCurrentId) {
		btnTitles.get(pCurrentId).setChecked(true);
		mTopTitle.setText(getString(mTitles[pCurrentId]));
		if(pCurrentId == 0){
			mTitleRight.setVisibility(View.VISIBLE);
		}else{
			mTitleRight.setVisibility(View.GONE);
		}
	}
	private long mExitTime;
	@Override
	public boolean onKeyDown(int key, KeyEvent event) {
		if (key == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();

            } else {
                    finish();
            }
            return false;
	    }else{
		    return super.onKeyDown(key, event);    	
	    }
	}

	/**
	 * 重写OnClickListener的响应函数，主要目的就是实现点击title时，pager会跟着响应切换
	 * 
	 * @author tdx
	 */
	private class FooterOnClickListener implements OnClickListener {
		private int index;

		public FooterOnClickListener(int index, boolean isMenubtn) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			FrgActMain.this.setCurrentPage(index);
		}
	}
	private void isupdate() {
		// TODO Auto-generated method stub
		UpdateManager  um=new UpdateManager(FrgActMain.this);
		um.checkUpdate();
	}

	
}
