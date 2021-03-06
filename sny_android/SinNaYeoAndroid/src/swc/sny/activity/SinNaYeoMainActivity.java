package swc.sny.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

import swc.sny.service.GetDataService;
import swc.sny.service.LocationReceiver;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class SinNaYeoMainActivity extends Activity {
	private Socket socket;
	BufferedReader in;
	PrintWriter out;
	LocationManager locMgr;
	PendingIntent pIntent;

	private String request, result, ipAddress;
	private TextView tvNowTemper, tvNowHumid, tvSetTemper, tvSetHumid, tvIp,
			tvTime;
	private ImageButton btnAuto, btnOuting, btnWindow, btnHumidifier,
			btnSetting, btnRenew;
	private Calendar calendar;
	private Date crtDate, setDate;
	long setTime, leftTime;
	int flagAutoMode, flagOutingMode, flagWindow, flagHumid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sin_na_yeo_main);

		init();

		// 1번은온도받기 2번습도 3번은 창문열기 4번은 창문닫기
		// 그리고 목표온습도설정은 온도-습도 로

		Thread worker = new Thread() {
			public void run() {
				try {
					try {
						if (ipAddress != null) {
							socket = new Socket(ipAddress, 5555);
							// out = new PrintWriter(socket.getOutputStream(),
							// true);
							// in = new BufferedReader(new InputStreamReader(
							// socket.getInputStream()));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					// while (true) {
					// result = in.readLine();
					// Log.i("****result", result);
					// getTempHumid(result);
					// if (request.equals("1")) {
					// // tvNowTemper.post(new Runnable() {
					// // public void run() {
					// // tvNowTemper.setText(result);
					// // }
					// // });
					// } else if (request.equals("3")) {
					// // btnWindow.setImageResource(R.drawable.xx);
					// } else if (request.equals("5")) {
					// // btnWindow.setImageResource(R.drawable.xx);
					// } else {
					//
					// }
					// request = "0";
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		worker.start();

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				tvNowTemper.setText(msg.arg1 + " 도");
				tvNowHumid.setText(msg.arg2 + " %");
			}
		};

		if (socket != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					try {
						sendRequest("1");
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						in = new BufferedReader(new InputStreamReader(
								socket.getInputStream()));
						String res = in.readLine();
						String[] data = res.split(" ");
						
						Message msg = new Message();
						msg.arg1 = Integer.parseInt(data[0]);
						msg.arg2 = Integer.parseInt(data[1]);
						handler.sendMessage(msg);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}
		// GetInfoThread infoWorker = new GetInfoThread();
		// infoWorker.start();

		Intent startIntent = new Intent(GetDataService.class.getName());
		startIntent.putExtra("ipAddr", ipAddress);
		// startService(startIntent);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	Dialog dialog = null;

	public void mOnClick(View v) throws IOException {
		switch (v.getId()) {
		case R.id.main_automode_btn:
			// sendRequest("");
			if (flagAutoMode == 0) { // auto on
				flagAutoMode = 1;
				btnAuto.setImageResource(R.drawable.btn_auto_on);
				flagOutingMode = 0;
				btnOuting.setClickable(true);
				btnOuting.setImageResource(R.drawable.btn_outing_off);
				sendRequest("m");
			} else if (flagAutoMode == 1) {
				flagAutoMode = 0;
				btnAuto.setImageResource(R.drawable.btn_auto_off);
				flagOutingMode = -1;
				btnOuting.setClickable(false);
				btnOuting.setImageResource(R.drawable.btn_outing_nonclickable);
				sendRequest("n");
			}
			break;

		case R.id.main_outing_btn:
			// request = "o"; // on : o, off : f
			// out.println(request);
			if (flagOutingMode == -1) {
				Toast.makeText(SinNaYeoMainActivity.this, "자동모드를 켜주세요", 1)
						.show();
				flagOutingMode = -1;
			} else if (flagOutingMode == 0) {
				Rect displayRectangle = new Rect();
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				dialog = new Dialog(SinNaYeoMainActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				Window window = dialog.getWindow();
				window.getDecorView().getWindowVisibleDisplayFrame(
						displayRectangle);
				WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
				wlp.copyFrom(window.getAttributes());
				wlp.width = (int) (displayRectangle.width() * 0.69f);
				wlp.height = (int) (displayRectangle.height() * 0.65f);

				dialog.getWindow().setAttributes(wlp);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View dialogView = (LinearLayout) vi.inflate(
						R.layout.dialog_outing_setting, null);

				final TimePicker time = (TimePicker) dialogView
						.findViewById(R.id.dialog_timepicker);

				ImageButton btnOk = (ImageButton) dialogView
						.findViewById(R.id.dialog_ok_btn);
				ImageButton btnCancel = (ImageButton) dialogView
						.findViewById(R.id.dialog_cancel_btn);

				btnOk.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						crtDate = new Date();
						setDate = new Date();
						setDate.setHours(time.getCurrentHour());
						setDate.setMinutes(time.getCurrentMinute());

						if ((time.getCurrentHour() == 0)
								&& (time.getCurrentMinute() < 30)) {
							Toast.makeText(SinNaYeoMainActivity.this,
									"최소 30분 이후의 시간을 선택해주세요", 1).show();
						} else {
							flagOutingMode = 1;
							btnOuting.setClickable(true);
							btnOuting
									.setImageResource(R.drawable.btn_outing_on);

							setTime = setDate.getTime() - 30 * 60 * 1000;
							String sTime = "b" + (setTime / 1000 / 60 / 60)
									+ ":" + (setTime / 1000 % 60) + ":00";

							try {
								sendRequest("b" + sTime);
							} catch (IOException e) {
								e.printStackTrace();
							}

							leftTime = setDate.getTime() - crtDate.getTime();

							Toast.makeText(
									SinNaYeoMainActivity.this,
									"[ " + (leftTime / 1000 / 60 / 60) + "시간 "
											+ (leftTime / 1000 / 60 % 60)
											+ "분 ] 후로 설정되었습니다.",
									Toast.LENGTH_LONG).show();
							// InputMethodManager imm = (InputMethodManager)
							// getSystemService(Context.INPUT_METHOD_SERVICE);
							// imm.hideSoftInputFromWindow(edit_time.getWindowToken(),
							// 0);

							tvTime.setText("[ " + (leftTime / 1000 / 60 / 60)
									+ "시간 " + (leftTime / 1000 / 60 % 60)
									+ "분 ] 후로 설정되었습니다.");
							tvTime.setVisibility(View.VISIBLE);

							dialog.cancel();
						}
					}
				});

				btnCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.cancel();
					}
				});

				dialog.setCancelable(true);
				dialog.setContentView(dialogView);
				dialog.show();
			} else if (flagOutingMode == 1) {
				flagOutingMode = 0;
				btnOuting.setClickable(true);
				btnOuting.setImageResource(R.drawable.btn_outing_off);
				tvTime.setVisibility(View.INVISIBLE);
			} else {
			}

			break;
		case R.id.main_renew_btn:
			// request = "1";
			sendRequest("1");

			try {
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				getTempHumid(in.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case R.id.main_window_btn:
			if (flagWindow == 0) {
				flagWindow = 1;
				btnWindow.setImageResource(R.drawable.btn_window_open);
				sendRequest("4");
			} else {
				flagWindow = 0;
				btnWindow.setImageResource(R.drawable.btn_window_closed);
				sendRequest("5");
			}
			break;
		case R.id.main_humidifier_btn:
			if (flagHumid == 0) {
				flagHumid = 1;
				btnHumidifier.setImageResource(R.drawable.btn_humid_on);
				sendRequest("2");
			} else {
				flagHumid = 0;
				btnHumidifier.setImageResource(R.drawable.btn_humid_off);
				sendRequest("3");
			}
			break;
		default:
			break;
		}
	}

	public void registerGcm() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			Log.d("Tag", regId);
			GCMRegistrar.register(this, "798195391733");
		} else {
			Log.d("Tag", regId);
		}
	}

	private void getTempHumid(String res) {
		if (in != null) {
			final String[] data = res.split(" ");
			Log.i("****data", "" + data[0] + ", " + data[1]);

			tvNowTemper.setText(data[0] + " 도");
			tvNowHumid.setText(data[1] + " %");
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 100:
			if (resultCode == RESULT_OK) {
				String setT = data.getStringExtra("setT");
				String setH = data.getStringExtra("setH");

				Log.i("****setTH", setT + "-" + setH);

				tvSetTemper.setText("설정 온도 : " + setT);
				tvSetHumid.setText("설정 습도 : " + setH);

				try {
					sendRequest("a" + setT + "-" + setH);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					in = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
	}

	private void init() {
		tvNowTemper = (TextView) findViewById(R.id.main_now_temper_tv);
		tvNowHumid = (TextView) findViewById(R.id.main_now_humid_tv);
		tvSetTemper = (TextView) findViewById(R.id.main_set_temper_tv);
		tvSetHumid = (TextView) findViewById(R.id.main_set_humid_tv);
		tvTime = (TextView) findViewById(R.id.main_outing_tv);

		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		ipAddress = pref.getString("ip_address", "");
		tvIp = (TextView) findViewById(R.id.main_ip_tv);
		tvIp.setText(ipAddress);

		btnAuto = (ImageButton) findViewById(R.id.main_automode_btn);
		btnOuting = (ImageButton) findViewById(R.id.main_outing_btn);
		btnRenew = (ImageButton) findViewById(R.id.main_renew_btn);
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
		flagAutoMode = 0;
		flagOutingMode = -1;
		flagWindow = 0;
		flagHumid = 0;

		locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Intent intent = new Intent(this, LocationReceiver.class);
		pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

		registerGcm();
	}

	private void sendRequest(String req) throws IOException {
		out = new PrintWriter(socket.getOutputStream(), true);

		request = "req";
		if (out != null) {
			out.println(req);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		locMgr.addProximityAlert(37.6065142, 127.0417786, 1000, -1, pIntent);
	}

	@Override
	protected void onPause() {
		super.onPause();

		locMgr.removeProximityAlert(pIntent);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	class InfoHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			try {
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				getTempHumid(in.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
			super.handleMessage(msg);
		}
	}
}