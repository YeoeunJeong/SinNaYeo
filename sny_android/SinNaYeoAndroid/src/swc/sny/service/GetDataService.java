package swc.sny.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GetDataService extends Service {
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		String ipAddress = intent.getStringExtra("ipAddr");
		
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
