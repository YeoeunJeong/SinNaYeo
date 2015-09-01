package swc.sny.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SinNaYeoMainActivity extends Activity {
	private Socket socket;
	BufferedReader in;
	PrintWriter out;
	String request, result, ipAddress;
	TextView tvOuting, tvNowTemper, tvNowHumid, tvSetTemper, tvSetHumid, tvIp;
	ImageButton btnOuting, btnWindow, btnHumidifier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sin_na_yeo_main);

		init();

		// 1번은온도받기 2번습도 3번은 창문열기 4번은 창문닫기
		// 그리고 목표온습도설정은 온도-습도 로
		for (int i = 1; i < 3; i++) {
			request = "" + i;
		}

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
						result = in.readLine();
						Log.i("****data", result);
						if (request.equals("1")) {
							tvNowTemper.post(new Runnable() {
								public void run() {
									tvNowTemper.setText(result);
								}
							});
						} else if (request.equals("2")) {
							tvNowHumid.post(new Runnable() {
								public void run() {
									tvNowTemper.setText(result);
								}
							});
						} else if (request.equals("3")) {
							// btnWindow.setImageResource(R.drawable.xx);
						} else if (request.equals("5")) {
							// btnWindow.setImageResource(R.drawable.xx);
						} else {
							
						}
						request = "0";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		worker.start(); // onResume()?????? ??????.
	}

	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.main_outing_btn:
			request = "0";
			 new AlertDialog.Builder(this)
			 .setTitle("귀가시간?").setMessage("custom dialog 써야할듯")
			 .setPositiveButton("확인", null).show();
			break;
		case R.id.main_window_btn:
			request = "3";
			break;
		case R.id.main_humidifier_btn:
			request = "5";
			break;
		default:
			break;
		}
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
