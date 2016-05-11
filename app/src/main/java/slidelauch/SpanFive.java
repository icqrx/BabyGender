package slidelauch;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.marknguyen.babygenderpredictor.MainActivity;
import com.marknguyen.babygenderpredictor.R;


public class SpanFive extends Fragment {
	
	private FragmentActivity fa;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fa = super.getActivity();
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		View five = inflater.inflate(R.layout.span_five, container, false);
		Button validerInscription = (Button)five.findViewById(R.id.valider);
		validerInscription.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(fa, MainActivity.class);
				startActivity(intent);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putBoolean("first_time", true);
				editor.commit();		
				getActivity().finish();
			}
		});
		
		return five;
	}

}