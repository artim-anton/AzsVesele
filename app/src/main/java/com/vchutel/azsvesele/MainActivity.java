package com.vchutel.azsvesele;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ProgressDialog mProgressDialog;
    HashMap<Integer, ArrayList<String>> hashMap = new HashMap<Integer, ArrayList<String>>();
    String url = "https://www.azski.com.ua/networks/avias/07c52181-7ee6-4a4b-8322-4615f414041d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MyTask().execute();
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        TableLayout tableRow = (TableLayout) findViewById(R.id.tablelayout);
        ArrayList<HashMap<String, String>> userTable = new ArrayList<HashMap<String, String>>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Application");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            int count = 0;
            try {
                Document doc = Jsoup.connect(url).get();
                if (doc != null) {
                    Elements tableRows = doc.getElementsByClass("fuel-gas-station-price-table").select("tr");
                    for (int i = 0; i < tableRows.size(); i++) {
                        Element row = tableRows.get(i);

                        ArrayList<String> arrayList = new ArrayList<String>();
                        Elements rowItems = row.select("td");  //select
                        for (int j = 0; j < rowItems.size(); j++) {
                            arrayList.add(rowItems.get(j).text());
                        }
                        hashMap.put(count, arrayList);
                        count++;
                        Log.d("Output", hashMap.toString());
                        for (Element link : rowItems) {
                            //Log.d("Return: ", "" + link.text());
                            //receivedStr = link.text();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            TableLayout.LayoutParams viewParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT);

            for (int i=0; i<hashMap.size(); i++) {
                TextView textUser = new TextView(MainActivity.this);
                textUser.setGravity(Gravity.LEFT);
                //textUser.setLayoutParams(viewParams);
                textUser.setTextColor(0xff000000);

                textUser.setText(hashMap.get(i).toString());

                // создаём строку для таблицы
                TableRow row = new TableRow(MainActivity.this);
                row.addView(textUser);// добавляем в строку столбец с именем пользователя
                tableRow.addView(row); // добавляем в таблицу новую строку
            }

            mProgressDialog.dismiss();
        }
    }
}
