package swc.sny.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SinNaYeoMainActivity extends Activity {
	private Socket socket;
	BufferedReader in;
	PrintWriter out;
	String request, result, ipAddress;
	TextView tvOuting, tvNowTemper, tvNowHumid, tvSetTemper, tvSetHumid, tvIp;
	ImageButton btnOuting, btnWindow, btnHumidifier, btnSetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sin_na_yeo_main);

		init();

		// 1번은온도받기 2번습도 3번은 창문열기 4번은 창문닫기
		// 그리고 목표온습도설정은 온도-습도 로
		// try {
		// if (ipAddress != null) {
		// socket = new Socket(ipAddress, 5555);
		// out = new PrintWriter(socket.getOutputStream(), true);
		// in = new BufferedReader(new InputStreamReader(
		// socket.getInputStream()));
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		Thread worker = new Thread() {
			public void run() {
				try {

					try {
						Log.i("****ipAddress", ipAddress);
						if (ipAddress != null) {
							Log.i("****ipAddress is not nul1", ipAddress);
							socket = new Socket(ipAddress, 5555);
							Log.i("****ipAddress is not nul2",
									socket.getLocalPort() + "");
							out = new PrintWriter(socket.getOutputStream(),
									true);
							Log.i("****ipAddress is not nul3", "outout");
							in = new BufferedReader(new InputStreamReader(
									socket.getInputStream()));
							Log.i("****ipAddress is not nul4", "inin");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					while (true) {
						result = in.readLine();
						Log.i("****result", result);
						getTempHumid(result);
						if (request.equals("1")) {
							// tvNowTemper.post(new Runnable() {
							// public void run() {
							// tvNowTemper.setText(result);
							// }
							// });
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

		worker.start();

		// GetInfoThread infoWorker = new GetInfoThread();
		// infoWorker.start();
	}

	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.main_outing_btn:
			request = "0";
			out.println(request);
			new AlertDialog.Builder(this).setTitle("귀가시간?")
					.setMessage("custom dialog 써야할듯")
					.setPositiveButton("확인", null).show();
			break;
		case R.id.main_renew_btn:
			// request = "1";
			sendRequest("1");
			// getTempHumid(result);
			break;
		case R.id.main_window_btn:
			sendRequest("3");
			break;
		case R.id.main_humidifier_btn:
			sendRequest("2");
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
		btnSetting = (ImageButton) findViewById(R.id.main_setting_btn);

		btnSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SinNaYeoMainActivity.this,
						SettingActivity.class);
				startActivityForResult(intent, 100);
			}
		});

		request = "0";

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 100:
			if (resultCode == RESULT_OK) {
				String req = data.getStringExtra("setT");
				req += "-";
				req += data.getStringExtra("setH");

				Log.i("****setTH", req);

				out.println(req);
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// getTemper();
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

	private void sendRequest(String req) {
		request = "req";
		if (out != null) {
			out.println(req);
		}
	}

	private void getTempHumid(String res) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		// sendRequest("1");
		if (in != null) {
			String[] data = res.split(" ");
			Log.i("****data", "" + data[0] + ", " + data[1]);
//
//			Toast.makeText(SinNaYeoMainActivity.this,
//					"현재온도 : " + data[0] + ", 현재 습도 : " + data[1], 1).show();
			
			tvNowTemper.setText(data[0] + " 도");
			tvNowHumid.setText(data[1] + " %");
		}
	}

	class GetInfoThread extends Thread {
		public GetInfoThread() {

		}

		public void run() {
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}