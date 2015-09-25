import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;



public class GCMServerSide {
		public static void sendMessage(String msg) throws IOException {
			Sender sender = new Sender("AIzaSyAsXnwskB8Fc9hWOC5mjJD5BpeCWJG1UQM");

			String regId = "APA91bEXfKHR40OyzF7a3m_rSKwKQNOTEP7ZPzAKluWQTTKvkO2zwgQQxWvc26P_tutMOrmd-sr0fzyR9ruGjlB14USzjOInd6AOvr5oubpFNukkQlK3vSg0CtV7tqRDjJKTAeMWJFTu";

			Message message = new Message.Builder().addData("msg", msg)
					.build();

			List<String> list = new ArrayList<String>();
			list.add(regId);

			Result result = sender.send(message, regId, 5);
			String tempResult = result.getMessageId();
			
			 if (tempResult != null) {
		          // ������ ������ ������
				 System.out.println("success");
		      } else {
		          String error = result.getErrorCodeName();
		      }

//			if (multiResult != null) {
//				List<Result> resultList = multiResult.getResults();
	//
//				for (Result result : resultList) {
//					System.out.println(result.getMessageId());
//				}
//			}
		}
}