package com.tejadillas.armov3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CountrySelectActivity extends Activity {

	public final static String EXTRA_COUNTRY = "com.tusmuertostos.botonesacarino.COUNTRY";
	private Intent intent;
	private String country;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_country_select);

		country = "Norway";
		intent = new Intent(this, DisplayLandformActivity.class);

		final Button boton = (Button) findViewById(R.id.button_1);
		boton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String providers = Settings.Secure.getString(
						getContentResolver(),
						Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
				if (providers.contains("gps")) {
					// GPS Enabled
					intent.putExtra(EXTRA_COUNTRY, country);
					startActivity(intent);
				} else {
					CheckEnableGPS();
				}
			}
		});

		final Spinner spinner = (Spinner) findViewById(R.id.spinner_1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				country = arg0.getItemAtPosition(arg2).toString();

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_country_select, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_about:
			Toast.makeText(this, "Created by: José Carlos Tejada Saracho",
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void CheckEnableGPS() {

		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.prompts, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(promptsView);
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}
}
