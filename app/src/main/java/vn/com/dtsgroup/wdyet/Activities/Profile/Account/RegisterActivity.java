package vn.com.dtsgroup.wdyet.Activities.Profile.Account;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edt_fn, edt_un, edt_pw, edt_pw1;
    private Button btn_rgr, btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Đăng ký");

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_rgr = (Button) findViewById(R.id.btn_rgr);
        btn_rgr.setOnClickListener(this);

        edt_fn = (EditText) findViewById(R.id.edt_fn);
        edt_un = (EditText) findViewById(R.id.edt_un);
        edt_pw = (EditText) findViewById(R.id.edt_pw);
        edt_pw1 = (EditText) findViewById(R.id.edt_pw1);
    }

    private String un, fn, pw;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_rgr:
                fn = edt_fn.getText().toString(); un = edt_un.getText().toString();
                pw = edt_pw.getText().toString();
                final String pw1 = edt_pw1.getText().toString();
                if(fn.isEmpty() || un.isEmpty() || pw.isEmpty() || pw1.isEmpty()){
                    Toast.makeText(this, "Bạn phải nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                }
                else if(!pw.equals(pw1)){
                    Toast.makeText(this, "Nhập lại mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
                else if(pw.length() < 6){
                    Toast.makeText(this, "Mật khẩu phải có độ dài tối thiểu là 6 kí tự", Toast.LENGTH_SHORT).show();
                }
                else{
                    btn_rgr.setEnabled(false);
//                    keys = new ArrayList<>();
//                    keys.add("username");
//                    keys.add("password");
//                    keys.add("fullname");
//                    values = new ArrayList<>();
//                    values.add(un);
//                    values.add(pw);
//                    values.add(fn);
//                    register(Module.DOMAINNAME + "reg");
//
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Register().execute(Module.DOMAINNAME + "register/" + un + "/" + pw
                                    + "/" + Base64.encodeToString(fn.getBytes(), 1));
//                            new Register().execute(Module.DOMAINNAME + "reg");
                        }
                    });
                }
                break;

            case R.id.btn_back:
                finish();
                break;
        }
    }

//    private ArrayList<String> keys, values;
//    private void register(String url){
//        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            if(jsonArray.length() > 0){
//                                JSONObject jsonObject = jsonArray.getJSONObject(0);
//                                if(jsonObject.getString("title").equals("Info")){
//                                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent();
//                                    intent.putExtra("un", edt_un.getText().toString());
//                                    intent.putExtra("pw", edt_pw.getText().toString());
//                                    setResult(Module.REGISTERACTIVITYCODE, intent);
//                                    finish();
//                                }
//                                else{
//                                    Toast.makeText(RegisterActivity.this,
//                                            jsonObject.getString("notify") + ": " + jsonObject.getString("info"),
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                        finally {
//                            btn_rgr.setEnabled(true);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        btn_rgr.setEnabled(true);
//                    }
//                }
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("username", un);
//                params.put("password", pw);
//                params.put("fullname", fn);
////                for(int i=0; i<keys.size(); i++){
////                    params.put(keys.get(i), values.get(i));
////                }
//                return params;
//            }
//        };
//
//        requestQueue.add(stringRequest);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class Register extends AsyncTask<String, Integer, String>{
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
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("un", edt_un.getText().toString());
                        intent.putExtra("pw", edt_pw.getText().toString());
                        setResult(Module.REGISTERACTIVITYCODE, intent);
                        finish();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,
                                jsonObject.getString("notify") + ": " + jsonObject.getString("info"),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {
                btn_rgr.setEnabled(true);
            }
        }
    }
}
