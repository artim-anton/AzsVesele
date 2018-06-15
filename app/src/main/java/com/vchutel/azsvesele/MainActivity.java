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
    HashMap<Integer, ArrayList<String>> hashMapOne = new HashMap<Integer, ArrayList<String>>();
    HashMap<Integer, ArrayList<String>> hashMapTwo = new HashMap<Integer, ArrayList<String>>();
    String urlAzsOne = "https://www.azski.com.ua/networks/avias/07c52181-7ee6-4a4b-8322-4615f414041d";
    String urlAzsTwo = "https://www.azski.com.ua/networks/zog/32";
    String AzsNameOne, AzsNameTwo;
    String RelevanceOne, RelevanceTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MyTask().execute();
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        TableLayout tableRowOne = (TableLayout) findViewById(R.id.tableLayoutOne);
        TableLayout tableRowTwo = (TableLayout) findViewById(R.id.tableLayoutTwo);

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

            int countOne = 0;
            int countTwo = 0;
            try {
                Document docOne = Jsoup.connect(urlAzsOne).get();
                Document docTwo = Jsoup.connect(urlAzsTwo).get();
                if (docOne != null || docTwo != null) {
                    Elements tableRowsOne = docOne.getElementsByClass("fuel-gas-station-price-table").select("tr");
                    Elements tableRowsTwo = docTwo.getElementsByClass("fuel-gas-station-price-table").select("tr");

                    String subAzsNameOne = docOne.getElementsByClass("gas-stations-content-description").first().toString();
                    AzsNameOne = subAzsNameOne.substring(48, subAzsNameOne.length()-8);
                    String subRelevanceOne = docOne.getElementsByClass("gas-stations-content-description").last().toString();
                    RelevanceOne = subRelevanceOne.substring(48, subRelevanceOne.length()-7);

                    String subAzsNameTwo = docTwo.getElementsByClass("gas-stations-content-description").first().toString();
                    AzsNameTwo = subAzsNameTwo.substring(48, subAzsNameTwo.length()-7);
                    String subRelevanceTwo = docTwo.getElementsByClass("gas-stations-content-description").last().toString();
                    RelevanceTwo = subRelevanceTwo.substring(48, subRelevanceTwo.length()-7);

                    for (int i = 0; i < tableRowsOne.size(); i++) {
                        Element rowOne = tableRowsOne.get(i);

                        ArrayList<String> arrayListOne = new ArrayList<String>();
                        Elements rowItemsOne = rowOne.select("td");  //select
                        for (int j = 0; j < rowItemsOne.size(); j++) {
                            arrayListOne.add(rowItemsOne.get(j).text());
                        }
                        hashMapOne.put(countOne, arrayListOne);
                        countOne++;
                        Log.d("Output", hashMapOne.toString());
                        for (Element link : rowItemsOne) {
                            //Log.d("Return: ", "" + link.text());
                            //receivedStr = link.text();
                        }



                    }

                    for (int i = 0; i < tableRowsTwo.size(); i++) {
                        Element rowTwo = tableRowsTwo.get(i);

                        ArrayList<String> arrayListTwo = new ArrayList<String>();
                        Elements rowItemsTwo = rowTwo.select("td");  //select
                        for (int j = 0; j < rowItemsTwo.size(); j++) {
                            arrayListTwo.add(rowItemsTwo.get(j).text());
                        }
                        hashMapTwo.put(countTwo, arrayListTwo);
                        countTwo++;
                        Log.d("Output", hashMapTwo.toString());
                        for (Element link : rowItemsTwo) {
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
            TextView tvAzsNameOne = findViewById(R.id.AzsNameOne);
            TextView tvAzsNameTwo = findViewById(R.id.AzsNameTwo);
            TextView tvRelevanceOne = findViewById(R.id.RelevanceOne);
            TextView tvRelevanceTwo = findViewById(R.id.RelevanceTwo);

            tvAzsNameOne.setText(AzsNameOne);
            tvAzsNameTwo.setText(AzsNameTwo);
            tvRelevanceOne.setText(RelevanceOne);
            tvRelevanceTwo.setText(RelevanceTwo);


            TableLayout.LayoutParams viewParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT);

            for (int i=0; i<hashMapOne.size(); i++) {
                TextView textUser = new TextView(MainActivity.this);
                textUser.setGravity(Gravity.LEFT);
                //textUser.setLayoutParams(viewParams);
                textUser.setTextColor(0xff000000);

                textUser.setText(hashMapOne.get(i).toString());


                for (int j = 0; j < 1; j++) {
                    // создаём строку для таблицы
                    TableRow row = new TableRow(MainActivity.this);
                    row.addView(textUser);// добавляем в строку столбец с именем пользователя
                    tableRowOne.addView(row); // добавляем в таблицу новую строку

                }



            }


           for (int i=0; i<hashMapTwo.size(); i++) {
                TextView textUserTwo = new TextView(MainActivity.this);
                textUserTwo.setGravity(Gravity.LEFT);
                //textUser.setLayoutParams(viewParams);
                textUserTwo.setTextColor(0xff000000);

                textUserTwo.setText(hashMapTwo.get(i).toString());

                // создаём строку для таблицы
                TableRow rowTwo = new TableRow(MainActivity.this);
                rowTwo.addView(textUserTwo);// добавляем в строку столбец с именем пользователя
                tableRowTwo.addView(rowTwo); // добавляем в таблицу новую строку
            }

            mProgressDialog.dismiss();
        }
    }
}
