package courseTracker.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.courseTracker.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyReceiver extends BroadcastReceiver {

static int notificationID;
    String channel_id="test";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,intent.getStringExtra("key"),Toast.LENGTH_LONG).show();
        createNotificationChannel(context,channel_id);

        Notification notification= new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.mipmap.ic_launcher1_background)
                .setContentText(intent.getStringExtra("key"))
                .setContentTitle("Course Tracker Reminder:").build();


        NotificationManager notificationManager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++,notification);

        //Put a notification her aka Vogella Tutorial

    }
    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nameChannel = context.getResources().getString(R.string.channel_name);
            String desc = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;


            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID, nameChannel, importance);
            channel1.setDescription(desc);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);


        }
    }
}


