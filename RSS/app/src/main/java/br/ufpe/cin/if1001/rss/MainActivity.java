package br.ufpe.cin.if1001.rss;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    //private final String RSS_FEED_DEFAULT = "http://leopoldomt.com/if1001/g1brasil.xml";

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    //use ListView ao invés de TextView - deixe o atributo com o mesmo nome
    private ListView conteudoRSS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //use ListView ao invés de TextView - deixe o ID no layout XML com o mesmo nome conteudoRSS
        //isso vai exigir o processamento do XML baixado da internet usando o ParserRSS
        conteudoRSS = (ListView) findViewById(R.id.conteudoRSS);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // instance preferences
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        // get preferences in "rssfeed" if not exist use "http://leopoldomt.com/if1001/g1brasil.xml"
        String rssFeed = sharedPref.getString(getString(R.string.rss_feed_key),
                getString(R.string.rss_feed_default));
        new CarregaRSStask().execute(rssFeed);
    }

    private class CarregaRSStask extends AsyncTask<String, Void, ArrayAdapter<ItemRSS>> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ArrayAdapter<ItemRSS> doInBackground(String... params) {

            // default list has a item error
            List<ItemRSS> conteudo = Arrays.asList(
                new ItemRSS("Error", "", "", "Error getting feed"));

            try {
                conteudo = getRssFeed(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // gerar o novo adapter com o conteudo
            return new ItemRSSAdapter(getApplicationContext(), conteudo);
        }

        @Override
        protected void onPostExecute(ArrayAdapter<ItemRSS> s) {
            Toast.makeText(getApplicationContext(), "terminando...", Toast.LENGTH_SHORT).show();

            //ajuste para usar uma ListView
            //o layout XML a ser utilizado esta em res/layout/itemlista.xml
            // setar o adapter do ListView conteudo
            conteudoRSS.setAdapter(s);
        }
    }

    //Opcional - pesquise outros meios de obter arquivos da internet
    // return List<ItemRSS> to set an adapter to show all
    private List<ItemRSS> getRssFeed(String feed) throws IOException {
        InputStream in = null;
        List<ItemRSS> rssFeed = new ArrayList<>();
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            String feedStr = new String(response, "UTF-8");
            // parse string feed to List<ItemRSS>
            rssFeed = ParserRSS.parse(feedStr);

        } catch (Exception e) { // Adding catcher because ParserRSS can throw Exception
            Log.e("error", e.getMessage());
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }
}
