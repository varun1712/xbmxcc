package com.ratan.vesit;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ratan.vesit.R;

public class Map_Fragment extends Fragment {
	
	public final static String TAG = Map_Fragment.class.getSimpleName();
	
	
	public Map_Fragment(){
		
		
	}
	
	public static Map_Fragment newInstance(){
		return new Map_Fragment();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.map,container,false);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
}

}
