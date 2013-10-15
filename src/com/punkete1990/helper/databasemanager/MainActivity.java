package com.punkete1990.helper.databasemanager;

import com.punkete1990.helper.databasemanager.Database.ClientDBHelper;
import com.punkete1990.helper.databasemanager.Model.Client;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "Database Manager";
	private DataBaseManagerApplication mApp;
	private ClientDBHelper clientDBHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//HOW TO USE Database Helper
		mApp = (DataBaseManagerApplication)getApplication();
		//Set Default user for database (testing)
		mApp.setUserId(1);
		//Get database for user
		clientDBHelper = mApp.getCurrentClientDataBase();
		
		//OnClick
		findViewById(R.id.button_insert).setOnClickListener(this);
		findViewById(R.id.button_update).setOnClickListener(this);
		findViewById(R.id.button_delete).setOnClickListener(this);
		findViewById(R.id.button_select).setOnClickListener(this);
		findViewById(R.id.button_select_all).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		boolean result;
		switch (v.getId()) {
		case R.id.button_insert:
			Client my_client = new Client();
			my_client.setName("Lesther Vega");
			my_client.setAddress1("3 calle 5-40 zona 1");
			my_client.setCity("Guatemala");
			my_client.setCountry("Guatemala");
			my_client.setState_province_country("Guatemala");
			my_client.setZip_postcode("01001");
			my_client.setContact_name("Lesther");
			my_client.setPhone("+502 43188888");
			my_client.setEmail_id("punkete1990@gmail.com");
			my_client.setWebsite("http://iconify.co/punkete1990");
			result = clientDBHelper.insert(my_client);
			Log.d(TAG, "Insert into client -> "+result);
			Toast.makeText(this, "Insert into client -> "+result, Toast.LENGTH_SHORT).show();
			break;
		case R.id.button_update:
			if (clientDBHelper.exists(1)){
				Client update_client = clientDBHelper.select(1);
				update_client.setContact_name("Fernando");
				result = clientDBHelper.update(1, update_client);
				Log.d(TAG, "update client -> "+ result);
				Toast.makeText(this, "update client -> "+result, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button_delete:
			if (clientDBHelper.exists(1)){
				result = clientDBHelper.delete(1);
				Log.d(TAG, "delete client -> "+ result);
				Toast.makeText(this, "delete client -> "+result, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button_select:
			if (clientDBHelper.exists(1)){
				Log.d(TAG, "select client -> "+ clientDBHelper.select(1).toString());
				Toast.makeText(this, "select client -> "+clientDBHelper.select(1).toString(), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button_select_all:
			for (Client client:clientDBHelper.selectAll()){
				Log.d(TAG, "client -> "+ client.toString());
			}
			break;
		default:
			break;
		}

	}

}
