/**
 * Created by Amy on 5/17/2015.
 */
import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Listens for a message telling it to start the Wearable MainActivity.
 */
public class WearableMessageListenerService extends WearableListenerService {

    private static final String start_Vibrate = "/start_Vibrate";

    @Override
    public void onMessageReceived(MessageEvent event) {
        if (event.getPath().equals(start_Vibrate)) {
            Intent startIntent = new Intent(this, wearActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }
    }
}
