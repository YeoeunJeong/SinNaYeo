package swc.sny.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConnectActivity extends Activity {
	EditText editIp; 
	Button connectBtn;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
    
		// start
		editIp = (EditText) findViewById(R.id.connect_edit_ip); 
		connectBtn = (Button) findViewById(R.id.connect_btn); 
		connectBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String strIp = editIp.getText().toString();
				Log.w("NETWORK", " " + strIp);
				if (strIp != null) {
					SharedPreferences pref = getSharedPreferences("pref",
							MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					editor.putString("ip_address", strIp);
					editor.commit();

					Intent intent = new Intent(ConnectActivity.this,
							SinNaYeoMainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}
}
