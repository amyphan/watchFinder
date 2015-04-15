package amyphan.watchfinder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by Amy on 4/15/2015.
 */
public class WatchFinder extends Activity
{
    Button vibrateOnButton;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchfinderactivity);
        addListenerOnButton();
    }

    public void addListenerOnButton()
    {
        vibrateOnButton = (Button) findViewById(R.id.vibrateOff);
        vibrateOnButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(WatchFinder.this,
                        "Vibrating Watch!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
