package swc.sny.activity;

import swc.test.sockettest.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SinNaYeoMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sin_na_yeo_main);
		
		new AlertDialog.Builder(this)
		.setTitle("???????????? ???????????????").setMessage("IP??? ???????????????")
		.setPositiveButton("??????", null).show();
	}
 
}
