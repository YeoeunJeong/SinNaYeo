package swc.sny.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import swc.test.sockettest.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

public class SinNaYeoMainActivity extends Activity {
	private Socket socket;
	BufferedReader in;
	PrintWriter out;
	String data, ipAddress;
	TextView tvOuting, tvNowTemper, tvNowHumid, tvSetTemper, tvSetHumid, tvIp;
	ImageButton btnOuting, btnWindow, btnHumidifier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sin_na_yeo_main);

		init();

		// new AlertDialog.Builder(this)
		// .setTitle("title").setMessage("message")
		// .setPositiveButton("??????", null).show();

		// 1번은온도받기2번이 습도 3번은 창문열기 4번은창문닫기야
		// 그리고 목표온습도설정은 온도-습도 로 쏴줘

		Thread worker = new Thread() {
			public void run() {
				try {
					socket = new Socket(ipAddress, 5555);
					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));

				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					while (true) {
						data = in.readLine();
						Log.i("****data", data);

						tvNowTemper.post(new Runnable() {
							public void run() {
								tvNowTemper.setText(data);
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		worker.start(); // onResume()?????? ??????.
	}

	private void init() {
		tvOuting = (TextView) findViewById(R.id.main_outing_tv);
		tvNowTemper = (TextView) findViewById(R.id.main_now_temper_tv);
		tvNowHumid = (TextView) findViewById(R.id.main_now_humid_tv);
		tvSetTemper = (TextView) findViewById(R.id.main_set_temper_tv);
		tvSetHumid = (TextView) findViewById(R.id.main_set_humid_tv);

		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		ipAddress = pref.getString("ip_address", "");
		tvIp = (TextView) findViewById(R.id.main_ip_tv);
		tvIp.setText(ipAddress);

		btnOuting = (ImageButton) findViewById(R.id.main_outing_btn);
		btnWindow = (ImageButton) findViewById(R.id.main_window_btn);
		btnHumidifier = (ImageButton) findViewById(R.id.main_humidifier_btn);
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
