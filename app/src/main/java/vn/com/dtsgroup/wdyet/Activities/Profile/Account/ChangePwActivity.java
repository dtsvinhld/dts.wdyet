package vn.com.dtsgroup.wdyet.Activities.Profile.Account;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class ChangePwActivity extends AppCompatActivity {

    private EditText edt_pw, edt_nPw, edt_nPw1;
    private TextView txt_noti;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Đổi mật khẩu");

        init();
    }

    private void init() {
        edt_pw = (EditText) findViewById(R.id.edt_cPw_pw);
        edt_nPw = (EditText) findViewById(R.id.edt_cPw_nPw);
        edt_nPw1 = (EditText) findViewById(R.id.edt_cPw_nPw1);

        txt_noti = (TextView) findViewById(R.id.txt_cPw_noti);
        txt_noti.setVisibility(View.GONE);

        button = (Button) findViewById(R.id.btn_changePw);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_noti.setVisibility(View.GONE);
                SharedPreferences sP = getSharedPreferences("user", MODE_PRIVATE);
                String oPw = sP.getString("password", "");
                String pw = edt_pw.getText().toString();
                final String nPw = edt_nPw.getText().toString();
                String nPw1 = edt_nPw1.getText().toString();
                if(pw.isEmpty() || nPw.isEmpty() || nPw1.isEmpty()){
                    Toast.makeText(ChangePwActivity.this, "Bạn phải nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                }
                else if(!oPw.equals(Module.PasswordEncodeMD5(pw))){
                    Toast.makeText(ChangePwActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                }
                else if(!nPw.equals(nPw1)){
                    Toast.makeText(ChangePwActivity.this, "Nhập lại mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
                else if(nPw.length() < 6){
                    Toast.makeText(ChangePwActivity.this, "Mật khẩu phải tối thiểu 6 kí tự", Toast.LENGTH_SHORT).show();
                }
                else{
                    button.setEnabled(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sP = getSharedPreferences("user", MODE_PRIVATE);
                            int id = sP.getInt("id", 0);
                            new ChangePW().execute(Module.DOMAINNAME + "changepassword/" + id + "/" + nPw);
                        }
                    });
                }
            }
        });

        ((Button) findViewById(R.id.btn_cPw_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                        SharedPreferences sP = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sP.edit();
                        editor.putString("password", edt_nPw.getText().toString());
                        editor.commit();
                        txt_noti.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(ChangePwActivity.this,
                                jsonObject.getString("notify") + ": " + jsonObject.getString("info"),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ChangePwActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {
                button.setEnabled(true);
            }
        }
    }
}
