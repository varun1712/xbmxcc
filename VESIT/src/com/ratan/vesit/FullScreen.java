package com.ratan.vesit;
import com.ratan.vesit.util.Utils;
import com.ratan.vesit.model.Wallpaper;
import com.ratan.vesit.util.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ratan.uk.co.senab.photoview.PhotoViewAttacher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;

public class FullScreen extends ActionBarActivity implements OnClickListener {
	private static final String TAG = FullScreen.class
			.getSimpleName();
	PhotoViewAttacher mAttacher;
	public static final String TAG_SEL_IMAGE = "selectedImage";
	private Wallpaper selectedPhoto;
	private ImageView fullImageView;
	private Utils utils;
	private ProgressBar pbLoader;
	

	// Picasa JSON response node keys
	private static final String TAG_ENTRY = "entry",
			TAG_MEDIA_GROUP = "media$group",
			TAG_MEDIA_CONTENT = "media$content", TAG_IMG_URL = "url",
			TAG_IMG_WIDTH = "width", TAG_IMG_HEIGHT = "height";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_screen);
		fullImageView = (ImageView) findViewById(R.id.imgFullscreen);
		pbLoader = (ProgressBar) findViewById(R.id.pbLoader);
		getSupportActionBar().hide();
		utils = new Utils(getApplicationContext());
		Intent i = getIntent();
		selectedPhoto = (Wallpaper) i.getSerializableExtra(TAG_SEL_IMAGE);
		if (selectedPhoto != null) {

			// fetch photo full resolution image by making another json request
			fetchFullResolutionImage();

		} else {
			Toast.makeText(getApplicationContext(),
					"getString(R.string.msg_unknown_error)", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Fetching image fullresolution json
	 * */
	private void fetchFullResolutionImage() {
		String url = selectedPhoto.getPhotoJson();
		pbLoader.setVisibility(View.VISIBLE);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG,
								"Image full resolution json: "
										+ response.toString());
						try {
							// Parsing the json response
							JSONObject entry = response
									.getJSONObject(TAG_ENTRY);

							JSONArray mediacontentArry = entry.getJSONObject(
									TAG_MEDIA_GROUP).getJSONArray(
									TAG_MEDIA_CONTENT);

							JSONObject mediaObj = (JSONObject) mediacontentArry
									.get(0);

							String fullResolutionUrl = mediaObj
									.getString(TAG_IMG_URL);

							// image full resolution widht and height
							final int width = mediaObj.getInt(TAG_IMG_WIDTH);
							final int height = mediaObj.getInt(TAG_IMG_HEIGHT);
							
							Log.d(TAG, "Full resolution image. url: "
									+ fullResolutionUrl + ", w: " + width
									+ ", h: " + height);
							
							ImageLoader imageLoader = AppController
									.getInstance().getImageLoader();

							// We download image into ImageView instead of
							// NetworkImageView to have callback methods
							// Currently NetworkImageView doesn't have callback
							// methods

							imageLoader.get(fullResolutionUrl,
									new ImageListener() {

										@Override
										public void onErrorResponse(
												VolleyError arg0) {
											Toast.makeText(
													getApplicationContext(),
													"getString(R.string.msg_wall_fetch_error)",
													Toast.LENGTH_LONG).show();
										}

										@Override
										public void onResponse(
												ImageContainer response,
												boolean arg1) {
											if (response.getBitmap() != null) {
												// load bitmap into imageview
												fullImageView
														.setImageBitmap(response
																.getBitmap());
							fullImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT));
												mAttacher = new PhotoViewAttacher(fullImageView);
												// hide loader and show set &
												// download buttons
												pbLoader.setVisibility(View.GONE);
											}
										}
									});

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
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
						Toast.makeText(getApplicationContext(),
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
	}

	/**
	 * Adjusting the image aspect ration to scroll horizontally, Image height
	 * will be screen height, width will be calculated respected to height
	 * */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")


	/**
	 * View click listener
	 * */
	@Override
	public void onClick(View v) {
		Bitmap bitmap = ((BitmapDrawable) fullImageView.getDrawable())
				.getBitmap();
		switch (v.getId()) {
		// button Download Wallpaper tapped
		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
}