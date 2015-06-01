package amyphan.watchfinder;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;



/**
 * Created by Amy on 4/15/2015.
 */
public class WatchFinder extends ActionBarActivity implements MessageApi.MessageListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "watchFinder";
    private static final String start_Activity_Path = "/start-activity";
    private static final String start_Vibrate = "/start_Vibrate";
    private static final String cancel_Vibrate = "/cancel_Vibrate";
    private GoogleApiClient mGoogleApiClient;
    private ToggleButton vibrateOnButton;
    private Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchfinderactivity);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_action_ic_android);

        toggleClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.connect();
        }
    }

    protected void onDestroy()
    {
        if(mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    public void onConnected(Bundle bundle)
    {
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    public void onConnectionSuspended(int i)
    {
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (messageEvent.getPath().equals(start_Vibrate)) {
                    Toast.makeText(getApplicationContext(), "Vibration on",
                            Toast.LENGTH_SHORT).show();
                } else if (messageEvent.getPath().equals(cancel_Vibrate)) {
                    Toast.makeText(getApplicationContext(), "Vibration off",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Failed to connect to Google Api Client with error code "
                + connectionResult.getErrorCode());
    }

    /**
     * Sends a message to Wearable MainActivity when button is pressed.
     */
    public void onStartWearableActivityClick(View view) {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(
                new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        for (final Node node : getConnectedNodesResult.getNodes()) {
                            Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                                    start_Activity_Path, new byte[0]).setResultCallback(
                                    getSendMessageResultCallback());
                        }
                    }
                });
    }

    private ResultCallback<MessageApi.SendMessageResult> getSendMessageResultCallback() {
        return new ResultCallback<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                if (!sendMessageResult.getStatus().isSuccess()) {
                    Log.e(TAG, "Failed to connect to Google Api Client with status "
                            + sendMessageResult.getStatus());
                }
            }
        };
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            return true;
        }
        if(id == R.id.action_about)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toggleClick()
    {
        vibrateOnButton = (ToggleButton) findViewById(R.id.toggleButton);
        vibrateOnButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vibrateOnButton.isChecked())
                {
                    onStartWearableActivityClick(v);
                    /*
                    Notification notification = new NotificationCompat.Builder(getApplication())
                            .setSmallIcon(R.drawable.android_app_icon)
                            .setContentTitle("WatchFinder")
                            .setContentText("Vibrating!!!!!")
                            .setVibrate(new long[]{1, 1000})
                            .extend(
                                    new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                            .build();

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication());

                    int notificationId = 1;
                    notificationManager.notify(notificationId, notification);
                    */
                    Toast.makeText(WatchFinder.this,"VIBRATE ON",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(WatchFinder.this,"VIBRATE OFF",Toast.LENGTH_SHORT).show();
                }
            }

        }
        );
    }
}
