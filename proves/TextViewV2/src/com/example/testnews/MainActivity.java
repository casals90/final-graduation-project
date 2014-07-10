package com.example.testnews;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private ListView list;
    private ListNewsAdapter adapter;
    //public  CustomListViewAndroidExample CustomListView = null;
    private  ArrayList<New> news = new ArrayList<New>();
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		
		news.add(new New("user1",R.drawable.im_buho,"descripcio 1"));
		news.add(new New("user2",R.drawable.im_colibri,"descripcio 2"));
		news.add(new New("user3",R.drawable.im_flamenco,"descripcio 3"));
		news.add(new New("user4",R.drawable.im_kiwi,"descripcio 4"));
		news.add(new New("user5",R.drawable.im_loro,"descripcio 5"));
		news.add(new New("user6",R.drawable.im_pavo,"descripcio 6"));
		news.add(new New("user7",R.drawable.im_pinguino,"descripcio 7"));
		
		list = (ListView) findViewById(R.id.list_news);
		//Create Custom Adapter 
		adapter = new ListNewsAdapter(this,news);
        list.setAdapter(adapter);

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
