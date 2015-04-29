package amyphan.watchfinder;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;


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
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] vibrationPattern = {0, 500, 50, 300};
                final int indexInPatternToRepeat = 1;

                if(vibrateOnButton.isChecked())
                {
                    vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
                    Toast.makeText(WatchFinder.this,"VIBRATE ON",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    vibrator.cancel();
                    Toast.makeText(WatchFinder.this,"VIBRATE OFF",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
