package com.ratan.vesit;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

public class WIki_Fragment extends Fragment {

	public final static String TAG = WIki_Fragment.class.getSimpleName();
	
	public WIki_Fragment() {
		// TODO Auto-generated constructor stub
	}

	public static WIki_Fragment newInstance() {
		return new WIki_Fragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (isInternetAvailable(getActivity())) {
		return inflater.inflate(R.layout.wiki, container, false);
		} else {
			Toast.makeText(getActivity().getApplicationContext(),"Oops..No Internet Connection !",2000).show();
			return null;
		}
		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		WebView webView = (WebView) view.findViewById(R.id.webPage);
		webView.loadUrl("http://en.m.wikipedia.org/wiki/VESIT");
	}
	
	public boolean isInternetAvailable(Context context) {
		return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
}
	
}
