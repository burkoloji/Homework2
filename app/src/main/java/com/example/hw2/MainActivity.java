package com.example.hw2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends AppCompatActivity {

    private ListView LV; //listview
    private ListView LV_ann;
    private ListView LV_News;
    public ArrayList ArrayL = new ArrayList(); //ArrayList
    public ArrayList ArrayAnn=new ArrayList();
    public ArrayList ArrayNews=new ArrayList();

    public ArrayList Links_ann = new ArrayList();
    public ArrayList Links_news=new ArrayList();
    public ArrayList Data_ann=new ArrayList();
    public ArrayList Data_news=new ArrayList();
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterAnn;
    private ArrayAdapter<String> adapterNews;
    private static String URL1="https://aybu.edu.tr/sks/";
    private static String URL2="https://aybu.edu.tr/muhendislik/bilgisayar/";
    private static String URL3="https://aybu.edu.tr/muhendislik/bilgisayar/";

    private ProgressDialog progressD;
    private ProgressDialog progressD2;
    private ProgressDialog progressD3;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LV=(ListView)findViewById(R.id.ListVi);
        LV_ann=(ListView)findViewById(R.id.ListAnn);
        LV_News=(ListView)findViewById(R.id.ListNews);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ArrayL);
        adapterAnn   = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ArrayAnn);
        adapterNews= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ArrayNews);
        Button buttonFood =(Button)findViewById(R.id.BtnFood);

        Button buttonAnn =(Button)findViewById(R.id.BtnAnn);

        Button buttonNews=(Button)findViewById(R.id.BtnNews);







        buttonFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetFood().execute();
            }
        });

        buttonAnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetAnn().execute();
            }
        });

        buttonNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetNews().execute();
            }
        });


        LV_ann.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                //elements




                AlertDialog.Builder diyalogOlusturucu = new AlertDialog.Builder(MainActivity.this);

                diyalogOlusturucu.setMessage("->"+ Data_ann.get(position)).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
   }
                });
                diyalogOlusturucu.create().show();

            }
        });

        LV_News.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                AlertDialog.Builder diyalogOlusturucu = new AlertDialog.Builder(MainActivity.this);

                diyalogOlusturucu.setMessage("->  "+ Data_news.get(position)).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                diyalogOlusturucu.create().show();

            }
        });





    }



    private class GetFood extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressD=new ProgressDialog(MainActivity.this);
            progressD.setTitle("Food Menü");
            progressD.setMessage("Loading ...");
            progressD.setIndeterminate(false);
            progressD.show();




        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayAnn.clear();
            ArrayNews.clear();
            LV.setAdapter(adapter);
            LV_News.setAdapter(adapterNews);
            LV_ann.setAdapter(adapterAnn);

            progressD.dismiss();

        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                Document docFood=Jsoup.connect(URL1).timeout(30*1000).get();

                Elements FoodName= docFood.select("td");
                ArrayL.clear();
                for(int i=2;i<FoodName.size();i++){
                    ArrayL.add(FoodName.get(i).text());
                }



            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;

        }
    }




    private class GetAnn extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressD2=new ProgressDialog(MainActivity.this);
            progressD2.setTitle("Announcements");
            progressD2.setMessage("Loading ...");
            progressD2.setIndeterminate(false);
            progressD2.show();




        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayL.clear();
            ArrayNews.clear();

            LV.setAdapter(adapter);
            LV_News.setAdapter(adapterNews);
            LV_ann.setAdapter(adapterAnn);
            progressD2.dismiss();

        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                Document docFood=Jsoup.connect(URL2).timeout(30*1000).get();
                Elements FoodName= docFood.select("div[class=caContent]>div[class=cncItem]");
                Elements links=FoodName.select("a[href]");


                ArrayAnn.clear();
                Links_ann.clear();
                Data_ann.clear();
                for(int i=0;i<FoodName.size();i++){
                    ArrayAnn.add(FoodName.get(i).text());
                    Links_ann.add(links.get(i).attr("href"));


                }
                for(int j=0;j<Links_ann.size();j++) {
                    String URLDyno = "https://www.aybu.edu.tr/muhendislik/bilgisayar/" + Links_ann.get(j);
                    Document docItem = Jsoup.connect(URLDyno).timeout(30 * 1000).get();
                    Elements dataname = docItem.select("div[id=content_left]");
                    Data_ann.add(dataname.get(0).text());

                }







            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;

        }
    }
    private class GetNews extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressD3=new ProgressDialog(MainActivity.this);
            progressD3.setTitle("News");
            progressD3.setMessage("Loading ...");
            progressD3.setIndeterminate(false);
            progressD3.show();




        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayL.clear();
            ArrayAnn.clear();

            LV.setAdapter(adapter);
            LV_News.setAdapter(adapterNews);
            LV_ann.setAdapter(adapterAnn);
            progressD3.dismiss();

        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                Document docFood=Jsoup.connect(URL3).timeout(30*1000).get();

                Elements FoodName= docFood.select("div[class=cnContent]>div[class=cncItem]");
                Elements linksx=FoodName.select("a[href]");
                ArrayNews.clear();
                for(int i=0;i<FoodName.size();i++){
                    ArrayNews.add(FoodName.get(i).text());
                    Links_news.add(linksx.get(i).attr("href"));
                }
                for(int j=0;j<Links_news.size();j++) {
                    String URLDyno = "https://www.aybu.edu.tr/muhendislik/bilgisayar/" + Links_news.get(j);
                    Document docItem = Jsoup.connect(URLDyno).timeout(30 * 1000).get();
                    Elements dataname = docItem.select("div[id=content_left]");
                    Data_news.add(dataname.get(0).text());

                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;

        }
    }

}
