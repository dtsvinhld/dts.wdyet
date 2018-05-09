package vn.com.dtsgroup.wdyet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vn.com.dtsgroup.wdyet.Activities.Main.NavigationDrawerActivity;
import vn.com.dtsgroup.wdyet.Class.Module;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//startActivity(new Intent(this, MaterialofDayActivity.class));
//        Log.e("HIHI", FirebaseInstanceId.getInstance().getToken().toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new SubmitLink().execute(Module.DOMAINNAME + "adddevice/" + FirebaseInstanceId.getInstance().getToken().toString());
            }
        });
        startActivity(new Intent(this, NavigationDrawerActivity.class));

        finish();

//        Log.e("TEST", Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + "");
//        Log.e("TEST", Base64.encodeToString("Lại Đức Vinh".getBytes(), 1));
//        Log.e("TEST", (new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")).format(new Date()).toString());
    }

    public static class SubmitLink extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            return Module.ContentURL(strings[0]);
        }
    }
}
