package com.tencent.open.agent;

import android.app.Activity;
import android.os.Bundle;
import com.tencent.mobileqq.R;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import java.util.regex.Pattern;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Intent;
import android.widget.Toast;
import android.content.Context;
import android.content.pm.ActivityInfo;
import java.util.regex.Matcher;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import java.util.Base64;
import android.view.Window;
import android.view.WindowManager;

// by川秋 电报@chuanqiunb 开源频道@zaizaishuju

/*

删除版权信息 全家必死

// by川秋 电报@chuanqiunb 开源频道@zaizaishuju
by 川秋版权所有 此版权不会在上号器界面显示 所以请不要删除 谢谢

*/

public class AgentActivity extends Activity {
    
    private Button myButton;
    private EditText myEditText;
    private static final String KEY_RAW_TEXT = "raw_text";
    private static final String PREF_NAME = "MyPrefs";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
      
        setContentView(R.layout.activity_agent);
                
        myButton = findViewById(R.id.my_button);
        myEditText = findViewById(R.id.editTextTextPersonName);
        loadRawDataFromSharedPreferences(myEditText);
    
    myButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                    String uin = myEditText.getText().toString();

            // String inputText = uin.replace("&expires_in=7776000", "");
            String inputText = uin.replaceAll("&expires_in=(7776000|5184000)", "");
            Pattern pattern = Pattern.compile("access_token=(.*?)&openid=(.*?)&pay_token=(.*?)&");
            Matcher matcher = pattern.matcher(inputText);
            if (matcher.find()) {
                String access_token = matcher.group(1);
                String openid = matcher.group(2);
                String pay_token = matcher.group(3);
                try {
                    JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("access_token", access_token);
                    jsonObject3.put("openid", openid);
                    jsonObject3.put("pay_token", pay_token);
                    jsonObject3.put("expires_in", "7776000");
                    jsonObject3.put("ret", "0");
                    jsonObject3.put("pf", "desktop_m_qq-10000144-android-2002-");
                    jsonObject3.put("page_type", "1");
                    saveRawDataToSharedPreferences(uin);
                    Toast.makeText(AgentActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(); // 创建 Intent 对象
                    intent.putExtra("key_response", jsonObject3.toString()); // 添加额外数据到 Intent
                    setResult(-1, intent); // 设置 Activity 结果
                    finish(); // 结束当前 Activity
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AgentActivity.this, "格式错误", Toast.LENGTH_SHORT).show();
            }



        }
    });
    
    }

// by川秋 电报@chuanqiunb 开源频道@zaizaishuju

    public void saveRawDataToSharedPreferences(String rawText) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RAW_TEXT, rawText);
        editor.apply();
    }

    private void loadRawDataFromSharedPreferences(EditText plainText) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, 0);
        String rawText = sharedPreferences.getString(KEY_RAW_TEXT, "");
        plainText.setText(rawText);
    }
    
}
