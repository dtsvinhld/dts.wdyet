package vn.com.dtsgroup.wdyet.Activities.Profile.Account;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class SafeActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout layout_email, layout_aEmail, layout_activate;
    private EditText edt_email, edt_newEmail, edt_code;
    private Button btn_add_email, btn_save, btn_cancel;
    private TextView txt_email, btn_cEmail, btn_activate, txt_noti, btn_activated;
    private String email;
    private int id;
    private Dialog dialog;
    private boolean changeE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Bảo vệ tài khoản");
        init();
        initEmail();
    }

    private void init(){
        layout_email = findViewById(R.id.layout_email);
        layout_aEmail = findViewById(R.id.layout_add_email);
        layout_activate = findViewById(R.id.layout_activate);
        layout_activate.setVisibility(View.GONE);
        edt_email = findViewById(R.id.edt_add_email);
        btn_add_email = findViewById(R.id.btn_add_email);
        btn_add_email.setOnClickListener(this);
        txt_email = findViewById(R.id.txt_email);
        btn_activate = findViewById(R.id.btn_activate);
        btn_activate.setOnClickListener(this);
        btn_cEmail = findViewById(R.id.btn_changeEmail);
        btn_cEmail.setOnClickListener(this);
        txt_noti = findViewById(R.id.txt_noti_email);
        txt_noti.setVisibility(View.GONE);
        id = getSharedPreferences("user", MODE_PRIVATE).getInt("id", 0);
        dialog = new Dialog(this);
        dialog.setTitle("Đổi email");
        dialog.setContentView(R.layout.dialog_change_email);
        edt_newEmail = dialog.findViewById(R.id.edt_newEmail);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_save = (Button) dialog.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        edt_code = findViewById(R.id.edt_code);
        btn_activated = findViewById(R.id.btn_activated);
        btn_activated.setOnClickListener(this);
    }

    private void initEmail(){
        email = getSharedPreferences("user", MODE_PRIVATE).getString("email", "");
        if(!email.isEmpty()){
            txt_email.setText(email);
            boolean s = getSharedPreferences("user", MODE_PRIVATE).getBoolean("semail", false);
            if(s){
                btn_activate.setText("Đã xác minh");
                btn_activate.setEnabled(false);
            }
            setContent(View.VISIBLE, View.GONE);
        }
        else {
            setContent(View.GONE, View.VISIBLE);
        }
    }

    private void setContent(int view, int view1){
        layout_email.setVisibility(view);
        layout_aEmail.setVisibility(view1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_email:
                final String em = edt_email.getText().toString();
                if(Module.isEmail(em)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_add_email.setEnabled(false);
                            new MEmail().execute(Module.DOMAINNAME + "addemail/" + id + "/" + em);
                        }
                    });
                }
                else{
                    Toast.makeText(this, "Vui lòng nhập đúng địa chỉ email", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_activate:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_activate.setEnabled(false);
                        new SendMail().execute(Module.MAILLINK + "id=" + id);
                    }
                });
                break;

            case R.id.btn_changeEmail:
                changeE = true;
                dialog.show();
                break;

            case R.id.btn_cancel:
                dialog.hide();
                break;

            case R.id.btn_save:
                changeE = true;
                final String newEmail = edt_newEmail.getText().toString();
                if(Module.isEmail(newEmail)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_cancel.setEnabled(false);
                            btn_save.setEnabled(false);
                            new MEmail().execute(Module.DOMAINNAME + "addemail/" + id + "/" + newEmail);
                        }
                    });
                }
                else{
                    Toast.makeText(this, "Vui lòng nhập đúng địa chỉ email", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_activated:
                int code = Integer.parseInt(edt_code.getText().toString());
                if(code == getSharedPreferences("safe", MODE_PRIVATE).getInt("code",0)){
                    SharedPreferences.Editor editor = getSharedPreferences("safe", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.commit();
//                    Toast.makeText(this, "hihi", Toast.LENGTH_LONG).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_activated.setEnabled(false);
                            txt_noti.setVisibility(View.GONE);
                            new Authenticate().execute(Module.DOMAINNAME + "aemail/" + id);
                        }
                    });
                }
                else{
                    Toast.makeText(this, "Mã xác nhận không đúng", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    class MEmail extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            return Module.ContentURL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                if(jsonArray.length()>0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if(jsonObject.getString("title").equals("Info")){
                        String noti = "";
                        if(jsonObject.getString("info").equals("successful")){
                            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                            if(changeE){
                                email = edt_newEmail.getText().toString();
                                editor.putString("email",email);
                                editor.commit();
                                edt_newEmail.setText("");
                                dialog.hide();
                                changeE = false;
                            }
                            else{
                                email = edt_email.getText().toString();
                                editor.putString("email",email);
                                editor.commit();
                                edt_email.setText("");
                            }
                            noti = "Thay đổi đã được lưu lại";
                            txt_email.setText(email);
                            txt_noti.setText(noti);
                            txt_noti.setVisibility(View.VISIBLE);
                            setContent(View.VISIBLE, View.GONE);
                        }
                        else{
                            noti = jsonObject.getString("info").toString();
                            if(!changeE){
                                txt_noti.setText(noti);
                                txt_noti.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SafeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {
                btn_add_email.setEnabled(true);
                btn_cEmail.setEnabled(true);
                btn_cancel.setEnabled(true);
                btn_save.setEnabled(true);
            }
        }
    }

//    private void countDown(){
//
//        final SharedPreferences.Editor editor = getSharedPreferences("safe", MODE_PRIVATE).edit();
//
//
//        CountDownTimer countDownTimer = new CountDownTimer(60000,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                editor.clear();
//                editor.commit();
//            }
//        };
//    }

    class SendMail extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            return Module.ContentURL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                btn_activate.setEnabled(true);
                JSONArray jsonArray = new JSONArray(s);
                if(jsonArray.length()>0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if(jsonObject.getString("title").equals("Info")){
                        SharedPreferences sharedPreferences = getSharedPreferences("safe", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("code", jsonObject.getInt("info"));
                        editor.commit();
                        btn_activate.setEnabled(false);
                        txt_noti.setText("Một mã xác nhận đã được gửi đến email của bạn");
                        txt_noti.setVisibility(View.VISIBLE);
                        layout_activate.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(SafeActivity.this, jsonObject.getString("notify")
                                + ": " + jsonObject.getString("info"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SafeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class Authenticate extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            return Module.ContentURL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                if(jsonArray.length() > 0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if(jsonObject.getString("title").equals("Info")){
                        btn_activate.setText("Đã xác minh");
                        btn_activate.setEnabled(false);
                        txt_noti.setText("Email đã được xác minh");
                        txt_noti.setVisibility(View.VISIBLE);
                        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                        editor.putBoolean("semail", true);
                        editor.commit();
                        edt_code.setText("");
                        layout_activate.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(SafeActivity.this, jsonObject.getString("notify")
                                + ": " + jsonObject.getString("info"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SafeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
