package vn.com.dtsgroup.wdyet.Activities.Profile.Account;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout layout_sendCode, layout_continue, layout_save;
    private EditText edt_email, edt_code, edt_pw, edt_pw1;
    private Button btn_send, btn_continue, btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Quên mật khẩu");

        init();
    }

    private void init() {
        layout_sendCode = findViewById(R.id.layout_sendCode);
        layout_continue = findViewById(R.id.layout_continue);
        layout_continue.setVisibility(View.GONE);
        layout_save = findViewById(R.id.layout_newPassword);
        layout_save.setVisibility(View.GONE);
        edt_email = findViewById(R.id.edt_email);
        edt_code = findViewById(R.id.edt_code_fg);
        edt_pw = findViewById(R.id.edt_pw_fg);
        edt_pw1 = findViewById(R.id.edt_pw1_fg);
        btn_send = findViewById(R.id.btn_send_fg);
        btn_send.setOnClickListener(this);
        btn_continue = findViewById(R.id.btn_continue_fg);
        btn_continue.setOnClickListener(this);
        btn_save = findViewById(R.id.btn_save_fg);
        btn_save.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_fg:
                final String email = edt_email.getText().toString();
                if(Module.isEmail(email)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_send.setEnabled(false);
                            new SendMail().execute(Module.MAILLINK + "email=" + Module.EncodeBase64(email));
                        }
                    });
                }
                else{
                    Toast.makeText(this, "Vui lòng nhập đúng địa chỉ email", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_continue_fg:
                int code = Integer.parseInt(edt_code.getText().toString());
                if(code == getSharedPreferences("forget", MODE_PRIVATE).getInt("code", 0)){
                    layout_continue.setVisibility(View.GONE);
                    layout_save.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(this, "Mã xác nhận không đúng", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_save_fg:
                final String pw = edt_pw.getText().toString();
                String pw1 = edt_pw1.getText().toString();
                if(!pw.equals(pw1)){
                    Toast.makeText(this, "Nhập lại mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
                else if(pw.length() < 6){
                    Toast.makeText(this, "Mật khẩu phải tối thiểu 6 kí tự", Toast.LENGTH_SHORT).show();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_save.setEnabled(false);
                            SharedPreferences sP = getSharedPreferences("forget", MODE_PRIVATE);
                            int id = sP.getInt("id", 0);
                            new ChangePW().execute(Module.DOMAINNAME + "changepassword/" + id + "/" + pw);
                        }
                    });
                }
                break;
        }
    }

    class SendMail extends AsyncTask<String, Integer, String> {
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
                        JSONObject object = jsonObject.getJSONObject("info");
                        SharedPreferences sharedPreferences = getSharedPreferences("forget", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("id", object.getInt("id"));
                        editor.putInt("code", object.getInt("code"));
                        editor.commit();
                        layout_sendCode.setVisibility(View.GONE);
                        layout_continue.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(ForgetActivity.this, jsonObject.getString("notify")
                                + ": " + jsonObject.getString("info"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ForgetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {
                btn_send.setEnabled(true);
            }
        }
    }

    class ChangePW extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            return Module.ContentURL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                if(jsonArray.length() >0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if(jsonObject.getString("title").equals("Info")){
                        Toast.makeText(ForgetActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(ForgetActivity.this,
                                jsonObject.getString("notify") + ": " + jsonObject.getString("info"),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ForgetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {
                btn_save.setEnabled(true);
            }
        }
    }
}
