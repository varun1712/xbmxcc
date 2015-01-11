package com.ratan.vesit;
import com.ratan.vesit.util.Utils;
import com.ratan.vesit.model.Wallpaper;
import com.ratan.vesit.util.AppController;
import com.ratan.vesit.adapter.GridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class Ion_Fragment extends Fragment {
	public static final String TAG = Ion_Fragment.class.getSimpleName();
	private Utils utils;
	private GridViewAdapter adapter;
	private GridView gridView;
	private int columnWidth;
	private static final String bundleAlbumId = "albumId";
	private String selectedAlbumId;
	private List<Wallpaper> photosList;
	private ProgressBar pbLoader;
	private static final String TAG_FEED = "feed", TAG_ENTRY = "entry",
			TAG_MEDIA_GROUP = "media$group",
			TAG_MEDIA_CONTENT = "media$content", TAG_IMG_URL = "url",
			TAG_IMG_WIDTH = "width", TAG_IMG_HEIGHT = "height", TAG_ID = "id",
			TAG_T = "$t";

	public Ion_Fragment() {
	}

	public static Ion_Fragment newInstance() {
		Ion_Fragment f = new Ion_Fragment();
		Bundle args = new Bundle();
		args.putString(bundleAlbumId,"fuckit");
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		photosList = new ArrayList<Wallpaper>();

		// Preparing the request url
		String url = "https://picasaweb.google.com/data/feed/api/user/vesitapp/albumid/6100589243904406865?alt=json";
		Log.d(TAG, "Final request url: " + url);

		View rootView = inflater.inflate(R.layout.gallery, container,
				false);

		gridView = (GridView) rootView.findViewById(R.id.grid_view);
		gridView.setVisibility(View.GONE);
		pbLoader = (ProgressBar) rootView.findViewById(R.id.pbLoader);
		pbLoader.setVisibility(View.VISIBLE);

		utils = new Utils(getActivity());
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG,
								"List of photos json reponse: "
										+ response.toString());
						try {
							// Parsing the json response
							JSONArray entry = response.getJSONObject(TAG_FEED)
									.getJSONArray(TAG_ENTRY);

							// looping through each photo and adding it to list
							// data set
							for (int i = 0; i < entry.length(); i++) {
								JSONObject photoObj = (JSONObject) entry.get(i);
								JSONArray mediacontentArry = photoObj
										.getJSONObject(TAG_MEDIA_GROUP)
										.getJSONArray(TAG_MEDIA_CONTENT);

								if (mediacontentArry.length() > 0) {
									JSONObject mediaObj = (JSONObject) mediacontentArry
											.get(0);

									String url = mediaObj
											.getString(TAG_IMG_URL);

									String photoJson = photoObj.getJSONObject(
											TAG_ID).getString(TAG_T)
											+ "&imgmax=d";									

									int width = mediaObj.getInt(TAG_IMG_WIDTH);
									int height = mediaObj
											.getInt(TAG_IMG_HEIGHT);

									Wallpaper p = new Wallpaper(photoJson, url, width,
											height);

									// Adding the photo to list data set
									photosList.add(p);

									Log.d(TAG, "Photo: " + url + ", w: "
											+ width + ", h: " + height);
								}
							}

							// Notify list adapter about dataset changes. So
							// that it renders grid again
							adapter.notifyDataSetChanged();

							// Hide the loader, make grid visible
							pbLoader.setVisibility(View.GONE);
							gridView.setVisibility(View.VISIBLE);

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getActivity(),
									"getString(R.string.msg_unknown_error)",
									Toast.LENGTH_LONG).show();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Error: " + error.getMessage());
						// unable to fetch wallpapers
						// either google username is wrong or
						// devices doesn't have internet connection
						Toast.makeText(getActivity(),
								"getString(R.string.msg_wall_fetch_error)",
								Toast.LENGTH_LONG).show();

					}
				});

		// Remove the url from cache
		AppController.getInstance().getRequestQueue().getCache().remove(url);

		// Disable the cache for this url, so that it always fetches updated
		// json
		jsonObjReq.setShouldCache(false);

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);

		// Initilizing Grid View
		InitilizeGridLayout();

		// Gridview adapter
		adapter = new GridViewAdapter(getActivity(), photosList, columnWidth);

		// setting grid view adapter
		gridView.setAdapter(adapter);

		// Grid item select listener
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				// On selecting the grid image, we launch fullscreen activity
				Intent i = new Intent(getActivity(),
						FullScreen.class);

				// Passing selected image to fullscreen activity
				Wallpaper photo = photosList.get(position);

				i.putExtra(FullScreen.TAG_SEL_IMAGE, photo);
				startActivity(i);
			}
		});

		return rootView;
	}

	/**
	 * Method to calculate the grid dimensions Calculates number columns and
	 * columns width in grid
	 * */
	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				4, r.getDisplayMetrics());

		// Column width
		columnWidth = (int) ((utils.getScreenWidth() - ((2 + 1) * padding)) / 2);

		// Setting number of grid columns
		gridView.setNumColumns(2);
		gridView.setColumnWidth(columnWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);

		// Setting horizontal and vertical padding
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
	}

}
