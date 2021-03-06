package swc.sny.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingActivity extends Activity {

	EditText edTemper, edHumid;
	ImageButton btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		init();
	}

	private void init() {
		edTemper = (EditText) findViewById(R.id.setting_set_temper_edit);
		edHumid = (EditText) findViewById(R.id.setting_set_humid_edit);

		btnSave = (ImageButton) findViewById(R.id.setting_save_btn);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SettingActivity.this, "save", 0).show();
				
				Intent i = new Intent();
				i.putExtra("setT", edTemper.getText().toString());
				i.putExtra("setH", edHumid.getText().toString());
				setResult(RESULT_OK, i);
				
				finish();
			}
		});
	}
}
