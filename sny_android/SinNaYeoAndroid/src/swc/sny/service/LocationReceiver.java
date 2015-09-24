package swc.sny.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

public class LocationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean enterFlag = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, true);
		Toast.makeText(context, enterFlag ? "산들바람 관리가 시작됩니다." : "외출모드 입니다.", 1).show();
	}

}
