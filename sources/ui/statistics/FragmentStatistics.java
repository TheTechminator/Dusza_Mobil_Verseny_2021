package com.terminato.moneymanager.ui.statistics;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.terminato.moneymanager.MainActivity;
import com.terminato.moneymanager.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

//kandobest

/**
 * A statisztikák grafikonos megjelenítését lehetővé tevő oldal.
 */
public class FragmentStatistics extends Fragment {

    /* Oldalon levő elemek */
    private WebView webView;
    private Button btKiadasok;
    private Button btBevetelek;

    private View root; //gyökér

    public FragmentStatistics() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_statistics, container, false);

        webView = root.findViewById(R.id.webView);
        btKiadasok = root.findViewById( R.id.btKiadasok );
        btBevetelek = root.findViewById( R.id.btBevetelek );

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.requestFocusFromTouch();
        webView.loadDataWithBaseURL( "file:///android_asset/", getContent(MainActivity.users.get(MainActivity.currentUserId).getFilteredExpenditurePrice()), "text/html", "utf-8", null );


        btKiadasok.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btKiadasok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btKiadasok.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                btBevetelek.setPaintFlags(0);

                webView.loadDataWithBaseURL( "file:///android_asset/", getContent(MainActivity.users.get(MainActivity.currentUserId).getFilteredExpenditurePrice()), "text/html", "utf-8", null );
            }
        });

        btBevetelek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btBevetelek.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                btKiadasok.setPaintFlags(0);

                webView.loadDataWithBaseURL( "file:///android_asset/", getContent(MainActivity.users.get(MainActivity.currentUserId).getFilteredIncomePrice()), "text/html", "utf-8", null );
            }
        });

        return root;
    }

    /**
     * A megkapott adatok alapján létrehoz egy szöveget amit be tudunk szúrni a html kódba
     */
    String data = "";
    public String generateData (HashMap<String, Float> hashMap) {
        data = "";

        hashMap.forEach( (k, v) -> {
            data += "['" + k + "', " + v + "],";
        });

        return data;
    }

    /**
     * Létrehoz egy html kódot amit majd a webview segítségével meg tudunk jeleníteni.
     * @param hashMap - a megjelenítendő adatokat tartalmazza
     * @return
     */
    private String getContent (HashMap<String, Float> hashMap) {
        String content = "<html>"
                + "  <head>"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
                + "      google.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "        var data = google.visualization.arrayToDataTable(["
                + "          ['Kategória', 'Összeg'],"
                +            generateData(hashMap)
                + "        ]);"
                + "        var options = {"
                + "          title: 'Kiadások kategóriánként',"
                + "          pieHole: 0.4,"
                + "          legend: { position: 'top', alignment: 'start', maxLines: 100 },"
                + "          chartArea:{left:'5%',top:100,width:'90%',height:'90%'}"
                + "        };"
                + "        var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "    <div id=\"piechart_3d\" style=\"width: 99vw; height: 100vh;\"></div>"
                + "  </body>" + "</html>";

        return content;
    }
}