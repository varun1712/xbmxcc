package com.ratan.vesit;

import com.ratan.vesit.util.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Login_Fragement extends Fragment {

	public final static String TAG = Login_Fragement.class.getSimpleName();
	public Login_Fragement() {
		// TODO Auto-generated constructor stub
	}

	public static Login_Fragement newInstance() {
		return new Login_Fragement();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login_xml, container, false);
	}
	
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final EditText app_id = (EditText) view.findViewById(R.id.et_login);
		final EditText password = (EditText) view.findViewById(R.id.et_password);
		final ImageView Logo = (ImageView) view.findViewById(R.id.iv_logo);
		final Button login = (Button) view.findViewById(R.id.bt_login);
		final Button gotit = (Button) view.findViewById(R.id.bt_gotit);
		final TextView whatsthis = (TextView) view.findViewById(R.id.tv_whatsthis);
		final TextView whatsthis_help = (TextView) view.findViewById(R.id.tv_whatsthis_help);
		Logo.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.fade_f));
		login.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.fade_f));
		app_id.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.fade_f));
		password.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.fade_f));
		whatsthis.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.fade_f));
		gotit.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.fade_f));
		whatsthis.setPaintFlags(whatsthis.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		whatsthis.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Logo.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.activity_close_translate));
				Logo.setVisibility(View.INVISIBLE);
				login.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.activity_close_translate));
				login.setVisibility(View.GONE);
				whatsthis.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.activity_close_translate));
				whatsthis.setVisibility(View.GONE);
				app_id.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.activity_close_translate));
				app_id.setVisibility(View.GONE);
				password.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.activity_close_translate));
				password.setVisibility(View.GONE);
				whatsthis_help.setVisibility(View.VISIBLE);
				gotit.setVisibility(View.VISIBLE);
				whatsthis_help.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.activity_open_translate));
				gotit.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.activity_open_translate));
				gotit.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						whatsthis_help.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.layout.activity_close_translate));
						getFragmentManager()
						.beginTransaction(
								)
						.replace(R.id.frame_container,
								Login_Fragement.newInstance(),
								Login_Fragement.TAG).commit();
					}
				});
				
			}
		});
		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String str_app_id = app_id.getText().toString() ;
				String str_password = password.getText().toString();
				if(isInternetAvailable(getActivity())==true){
					Toast.makeText(getActivity(), "Oops..http connection error",2000).show();
					makeJsonRequest(str_app_id,str_password);
				}
				else {
					Toast.makeText(getActivity(), "Oops..No Internet Connection",2000).show();
				}
				
			}
		});
		
	}
	
	

	protected void makeJsonRequest(String app_id, String password) {
		 final ProgressDialog pDialog = new ProgressDialog(getActivity());
	       pDialog.setMessage("Please wait...");
	       pDialog.setCancelable(false);
	       pDialog.show();
	       
		 String urlJsonObj = "http://192.168.0.104/vesit_login.php";
		 JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
                 urlJsonObj,null, new Response.Listener<JSONObject>() {
      
                     @Override
                     public void onResponse(JSONObject response) {
                    	 try {
                             // Parsing json object response
                             // response will be a json object
                        	 JSONArray phone = response.getJSONArray("success");
                        	 getActivity().setTitle("success");
                         } catch (JSONException e) {
                             e.printStackTrace();
                             Toast.makeText(getActivity().getApplicationContext(),
                                     "Error: " + e.getMessage(),
                                     Toast.LENGTH_LONG).show();
                         }
                         pDialog.hide();
                     }
                 }, new Response.ErrorListener() {
      
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         VolleyLog.d(TAG, "Error: " + error.getMessage());
                         Toast.makeText(getActivity().getApplicationContext(),
                                 error.getMessage()+"shit", Toast.LENGTH_SHORT).show();
                         // hide the progress dialog
                         pDialog.hide();
                     }
                 });
      
         // Adding request to request queue
         AppController.getInstance().addToRequestQueue(jsonObjReq);         
         
         

		 
		
	}

	public boolean isInternetAvailable(Context context) {
		return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
}
	
}
