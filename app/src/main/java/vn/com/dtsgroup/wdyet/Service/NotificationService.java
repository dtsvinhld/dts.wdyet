package vn.com.dtsgroup.wdyet.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import vn.com.dtsgroup.wdyet.Activities.Eatings.EatingActivity;
import vn.com.dtsgroup.wdyet.Activities.Result.ResultActivity;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.MainActivity;
import vn.com.dtsgroup.wdyet.R;

/**
 * Created by Verdant on 4/9/2018.
 */

public class NotificationService extends FirebaseMessagingService {

    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size() > 0){
            try {
                JSONObject data = new JSONObject(remoteMessage.getData().toString());
                String message = data.getString("message");
                data = data.getJSONObject("data");
                int idN = data.getInt("idN");
                String day = data.getString("day");

                int id = 0;
                switch (message){
                    case "Breakfast":
                        id = 1;
                        break;

                    case "Lunch":
                        id = 2;
                        break;

                    case "Dinner":
                        id = 3;
                        break;
                }

                Intent resultIntent = new Intent(getApplicationContext(), EatingActivity.class);
                resultIntent.putExtra("message",true);
                resultIntent.putExtra("meal",id);
                resultIntent.putExtra("day",daybynumber(day));
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(EatingActivity.class);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle(data.getString("title"))
                        .setContentText(data.getString("text"))
                        .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                        .setContentIntent(resultPendingIntent)
                        .build();

                NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
                if(id != 0){
                    new MainActivity.SubmitLink()
                            .execute(Module.DOMAINNAME + "addnotifyforuser/" + idN + "/"
                                    + getSharedPreferences("user", MODE_PRIVATE).getInt("id", 0) +"/"
                                    + getSharedPreferences("menu", MODE_PRIVATE).getInt("id", 0)
                                    + "/" + daybynumber(day) + "/" + id);
                    manager.notify(id, notification);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    int daybynumber(String day){
        switch (day){
            case "Mon":
                return 1;

            case "Tue":
                return 2;

            case "Wed":
                return 3;

            case "Thu":
                return 4;

            case "Fri":
                return 5;

            case "Sat":
                return 6;

            default:
                return 7;
        }
    }
}
