package adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class InitPagerAdapter extends FragmentPagerAdapter {
	
	@SuppressWarnings("rawtypes")
	private final List fragments;

	@SuppressWarnings("rawtypes")
	public InitPagerAdapter(FragmentManager fm, List fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return (Fragment) this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

}