package com.zhlt.g1app.view;

import com.zhlt.g1app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ViewPicHead extends LinearLayout {

	private View view;

	public ViewPicHead(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ViewPicHead(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ViewPicHead(Context context) {
		super(context);
		init();
	}

	private void init() {

		view = inflate(getContext(), R.layout.layout_headview, null);
		addView(view);
	}

}
