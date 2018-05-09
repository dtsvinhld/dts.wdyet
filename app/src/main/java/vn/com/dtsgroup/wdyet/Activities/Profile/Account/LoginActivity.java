package vn.com.dtsgroup.wdyet.Activities.Profile.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edt_username, edt_password;
    private Button btn_checklogin, btn_register, btn_forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Đăng nhập");

        btn_checklogin = (Button) findViewById(R.id.btn_checklogin);
        btn_checklogin.setOnClickListener(this);
        btn_forget = (Button) findViewById(R.id.btn_forget);
        btn_forget.setOnClickListener(this);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
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
            case R.id.btn_checklogin:
                final String username = edt_username.getText().toString(), password = edt_password.getText().toString();
                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu rỗng", Toast.LENGTH_SHORT).show();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new CheckLogin().execute(Module.DOMAINNAME + "login/"+username+"/" +password);
                        }
                    });
                }
                break;

            case R.id.btn_forget:
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                break;

            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, Module.REGISTERACTIVITYCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Module.REGISTERACTIVITYCODE){
            if(data != null){
                edt_username.setText(data.getStringExtra("un"));
                edt_password.setText(data.getStringExtra("pw"));
            }
        }
    }

    class CheckLogin extends AsyncTask<String, Integer, String>{
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
                        SharedPreferences sP = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sP.edit();
                        JSONObject object = jsonObject.getJSONObject("info");
                        editor.putInt("id", object.getInt("id"));
                        editor.putString("username", object.getString("username"));
                        editor.putString("password", object.getString("password"));
                        editor.putString("fullname", object.getString("fullname"));
                        editor.putString("sdt", object.getString("sdt"));
                        editor.putString("email", object.getString("email"));
                        editor.putBoolean("semail", object.getString("semail").equals("1"));
                        editor.putInt("days", object.getInt("days"));
                        editor.putInt("meals", object.getInt("meals"));
                        editor.putString("createDate", object.getString("createDate"));
                        editor.putBoolean("state", object.getString("state").equals("1"));
                        editor.commit();

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("res", true);
                        setResult(Module.LOGINACTIVITYCODE, intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,
                                jsonObject.getString("notify") + ": " + jsonObject.getString("info"),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
