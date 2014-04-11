package com.example.googlemapexample;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements LocationListener,
		SensorEventListener {

	private GoogleMap googleMap;
	private Button Normal, Hybrid, Sattelite, Train;
	int currenttype = 0;
	private ImageView image;

	// record the compass picture angle turned
	private float currentDegree = 0f;

	// device sensor manager
	private SensorManager mSensorManager;
	LatLng sydney = new LatLng(-33.867, 151.206);
	TextView tvHeading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		image = (ImageView) findViewById(R.id.imageViewCompass);

		// TextView that will tell the user what degree is he heading
		tvHeading = (TextView) findViewById(R.id.tvHeading);

		// initialize your android device sensor capabilities
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		Normal = (Button) findViewById(R.id.normal);
		Hybrid = (Button) findViewById(R.id.hybrid);
		Sattelite = (Button) findViewById(R.id.satellite);
		Train = (Button) findViewById(R.id.terrain);

		Normal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					// Loading map
					initilizeMap(GoogleMap.MAP_TYPE_NORMAL);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		Hybrid.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					// Loading map
					initilizeMap(GoogleMap.MAP_TYPE_HYBRID);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		Sattelite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					// Loading map
					initilizeMap(GoogleMap.MAP_TYPE_SATELLITE);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		Train.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					// Loading map
					initilizeMap(GoogleMap.MAP_TYPE_TERRAIN);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		try {
			// Loading map
			initilizeMap(GoogleMap.MAP_TYPE_NORMAL);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap(int type) {

		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			googleMap.setMapType(type);

			CameraPosition currentPlace = new CameraPosition.Builder()
					.target(new LatLng(googleMap.getMyLocation().getLatitude(),
							googleMap.getMyLocation().getLongitude()))
					.bearing(1.0f).tilt(65.5f).zoom(18f).build();
			googleMap.moveCamera(CameraUpdateFactory
					.newCameraPosition(currentPlace));
			currenttype = type;
			// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

			// check if map is created successfully or not
			// googleMap.addMarker(marker);
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			googleMap.setMapType(type);

		}

		googleMap.setMyLocationEnabled(true);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

		googleMap.addMarker(new MarkerOptions()
				.title("Sydney")
				.snippet("The most populous city in Australia.")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
				.position(sydney));
		// latitude and longitude

		// adding marker

	}

	@Override
	protected void onResume() {
		super.onResume();

		// for the system's orientation sensor registered listeners
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// to stop the listener and save battery
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);

		tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

		// create a rotation animation (reverse turn degree degrees)
		RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		CameraPosition currentPlace = new CameraPosition.Builder()
				.target(sydney)
				.bearing(degree).
				tilt(65.5f)
				.zoom(18f).build();
		googleMap.moveCamera(CameraUpdateFactory
				.newCameraPosition(currentPlace));
		// how long the animation will take place
		ra.setDuration(210);

		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		image.startAnimation(ra);

		currentDegree = -degree;

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
	}

}