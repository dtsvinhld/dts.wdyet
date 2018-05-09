package vn.com.dtsgroup.wdyet.Activities.Profile.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

public class ChangeFNActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private TextView textView;
    private SharedPreferences sP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_fn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Đổi tên");

        editText = (EditText) findViewById(R.id.edt_changeFN);
        button = (Button) findViewById(R.id.btn_changeFN);
        textView = (TextView) findViewById(R.id.txt_cFN_noti);
        textView.setVisibility(View.GONE);

        sP = getSharedPreferences("user", MODE_PRIVATE);
        final String fn = sP.getString("fullname", "Họ tên");
        final String un = sP.getString("username", "username");
        editText.setText(fn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.GONE);
                button.setEnabled(false);
                final String newFN = editText.getText().toString();
                if(newFN.isEmpty()){
                    Toast.makeText(ChangeFNActivity.this, "Tên hiển thị rỗng", Toast.LENGTH_SHORT).show();
                }
                else if(newFN.equals(fn)){
                    Toast.makeText(ChangeFNActivity.this, "Hãy nhập vào một tên mới", Toast.LENGTH_SHORT).show();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Log.e("Hihi", Module.DOMAINNAME + "changefullname/" + un + "/" + Module.EncodeBase64(newFN));
                            new ChangeFN().execute(Module.DOMAINNAME + "changefullname/" + un + "/" + Module.EncodeBase64(newFN));
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(changeFN)    finishForResult();
            else    finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(changeFN)    finishForResult();
            else    finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean finishForResult(){
        Intent intent = new Intent();
        intent.putExtra("changeFN", true);
        setResult(Module.CHANGEFNACTIVITYCODE, intent);
        finish();
        return true;
    }

    private boolean changeFN = false;

    class ChangeFN extends AsyncTask<String, Integer, String>{
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
                        SharedPreferences.Editor editor = sP.edit();
                        editor.putString("fullname", editText.getText().toString());
                        editor.commit();
                        textView.setText("Thay đổi đã được lưu lại");
                        textView.setVisibility(View.VISIBLE);
                        changeFN = true;
                    }
                    else{
                        Toast.makeText(ChangeFNActivity.this,
                                jsonObject.getString("notify") + ": " + jsonObject.getString("info"),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ChangeFNActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {
                button.setEnabled(true);
            }
        }
    }
}
