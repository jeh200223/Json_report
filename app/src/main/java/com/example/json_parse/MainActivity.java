package com.example.json_parse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    int type = 1;
    String json = "";
    String page = "http://192.168.56.1:8887/person.json";
    String team = "[\n" +
            "{\"팀명\":\"KIA\",\"감독\":\"선동렬\",\"연고지\":\"광주\"}," +
            "\n {\"팀명\":\"SAMSUNG\",\"감독\":\"류중일\",\"연고지\":\"대구\"}," +
            "\n {\"팀명\":\"LG\",\"감독\":\"김기태\",\"연고지\":\"서울\"}"+
            "\n]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Parse_Report");

        TextView textView = findViewById(R.id.textview);
        Button button = findViewById(R.id.source);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                InputStream inputStream = null;
                if (type == 1) {
                    json = new String(team);
                    textView.setText(json);
                } else if (type == 2) {
                    inputStream = getResources().openRawResource(R.raw.users);
                    try {
                        byte[] txt = new byte[inputStream.available()];
                        inputStream.read(txt);
                        json = new String(txt);
                        textView.setText(json);
                        inputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (type == 3) {
                    try {
                        inputStream = getAssets().open("employee.json");
                        byte[] txt = new byte[inputStream.available()];
                        inputStream.read(txt);
                        json = new String(txt);
                        textView.setText(json);
                        inputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    JSONThread thread = new JSONThread(MainActivity.this, page);
                    thread.start();
                    try {
                        thread.join();
                        json = thread.getJson();
                        textView.setText(json);
                    } catch (InterruptedException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        TextView textView1 = findViewById(R.id.textview2);
        Button button1 = findViewById(R.id.parser);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("");
                if (json.equals("")) {
                    Toast.makeText(MainActivity.this, "먼저 JSON 문서를 받으세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (type == 1) {
                        try {
                            JSONArray jsonArray = new JSONArray(team);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("팀명");
                                String direct = jsonObject.getString("감독");
                                String home = jsonObject.getString("연고지");
                                textView1.append("팀명 : " + name + "\n");
                                textView1.append("감독 : " + direct + "\n");
                                textView1.append("연고지 : " + home + "\n\n");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (type == 2) {
                        try {
                            JSONObject root = new JSONObject(json);
                            JSONArray array = root.getJSONArray((String) root.names().get(0));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int id = object.getInt((String) object.names().get(0));
                                String name = object.getString((String) object.names().get(1));
                                String email = object.getString((String) object.names().get(2));
                                String gender = object.getString((String) object.names().get(3));
                                String mobile = array.getJSONObject(i).getJSONObject("contact").getString("mobile");
                                String home = array.getJSONObject(i).getJSONObject("contact").getString("home");
                                String office = array.getJSONObject(i).getJSONObject("contact").getString("office");
                                textView1.append("ID : " + id + "\n");
                                textView1.append("이름 : " + name + "\n");
                                textView1.append("메일 : " + email + "\n");
                                textView1.append("성별 : " + gender + "\n");
                                textView1.append("연락처 \n");
                                textView1.append("핸드폰 : " + mobile + "\n");
                                textView1.append("집전화 : " + home + "\n");
                                textView1.append("사무실 : " + office + "\n\n");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (type == 3) {
                        try {
                            JSONObject root = new JSONObject(json);
                            JSONArray array = root.getJSONArray((String) root.names().get(0));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String name = object.getString((String) object.names().get(0));
                                String gender = object.getString((String) object.names().get(1));
                                int age = object.getInt((String) object.names().get(2));
                                JSONArray hobby = object.getJSONArray("hobby");
                                String temp = "";
                                for (int j = 0; j < hobby.length(); j++) {
                                    temp += hobby.getString(j) + ", ";
                                }
                                temp = temp.substring(0, temp.length() - 2);
                                textView1.append("이름 : " + name + "\n");
                                textView1.append("성별 : " + gender + "\n");
                                textView1.append("나이 : " + age + "\n");
                                textView1.append("취미 : " + temp + "\n\n");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();;
                        }
                    } else {
                        try {
                            JSONObject root = new JSONObject(json);
                            JSONArray array = root.getJSONArray((String) root.names().get(0));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String name = object.getString((String) object.names().get(0));
                                int age = object.getInt((String) object.names().get(1));
                                String address = object.getString((String) object.names().get(2));
                                textView1.append("이름 : " + name + "\n");
                                textView1.append("나이 : " + age + "\n");
                                textView1.append("주소 : " + address + "\n\n");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.program:
                type = 1;
                break;
            case R.id.raw:
                type = 2;
                break;
            case R.id.assets:
                type = 3;
                break;
            case R.id.website:
                type = 4;
        }
        item.setChecked(true);
        return true;
    }
}