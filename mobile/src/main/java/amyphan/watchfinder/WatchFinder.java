package amyphan.watchfinder;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;
import android.media.RingtoneManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.net.URL;
import java.util.concurrent.TimeUnit;


/**
 * Created by Amy on 4/15/2015.
 */
public class WatchFinder extends ActionBarActivity
{
    private ToggleButton vibrateOnButton;
    private Toolbar toolbar;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchfinderactivity);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_action_ic_android);
        toggleClick();
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
                //Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                //long[] vibrationPattern = {0, 10000};
                //final int indexInPatternToRepeat = 1;

                if(vibrateOnButton.isChecked())
                {
                    Notification notification = new NotificationCompat.Builder(getApplication())
                            .setSmallIcon(R.drawable.android_app_icon)
                            .setContentTitle("Hello World")
                            .setContentText("My first Android Wear notification")
                            .setVibrate(new long[]{1, 1000})
                            .extend(
                                    new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                            .build();

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication());

                    int notificationId = 1;
                    notificationManager.notify(notificationId, notification);
                    //vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
                    //Toast.makeText(WatchFinder.this,"VIBRATE ON",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //vibrator.cancel();
                    //Toast.makeText(WatchFinder.this,"VIBRATE OFF",Toast.LENGTH_SHORT).show();
                }
            }

        }
        );
    }
}
