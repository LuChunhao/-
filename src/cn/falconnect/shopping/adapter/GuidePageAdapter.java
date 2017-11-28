package cn.falconnect.shopping.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.falconnect.shopping.cat.R;

public class GuidePageAdapter extends PagerAdapter {
	private List<View> mViews;
	private onClickSplashButtonListener mButtonListener;
	public void setButtonListener(onClickSplashButtonListener buttonListener) {
		this.mButtonListener = buttonListener;
	}
	public GuidePageAdapter(){
		mViews = new ArrayList<View>();
	}
	@Override
	public int getCount() {
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	// 销毁position位置的界面  
    @Override  
    public void destroyItem(View view, int position, Object arg2) {  
        ((ViewPager) view).removeView(mViews.get(position));  
    }  
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(mViews.get(position), 0);
		if (position == mViews.size() - 1) {
			Button btn_go = (Button)container.findViewById(R.id.btn_go);
			btn_go.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 设置已经引导  
					mButtonListener.onClick();  
				}
			});
		}
		return mViews.get(position);
	}
	
	public void setData(List<View> list){
		this.mViews = list;
		notifyDataSetChanged();
	}
	
	public interface onClickSplashButtonListener{
		public void onClick();
	}
}
