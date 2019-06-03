package beispiel.studienprojekt;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import static beispiel.studienprojekt.Data.clicked;
import static beispiel.studienprojekt.SettingsPreference.setTextSize;

/**
 * Klasse die aufgerufen wird, wenn im Einstellungsmenü die Hilfe ausgewählt wird.
 *
 */

public class HelpActivity extends AppCompatActivity implements View.OnClickListener
{
    /**
     * Die Aktivität bekommt ein WebView, das auf der Basis eines html-Dokuments definiert wird und
     * Erklärungen bzw. .gif Dateien enthält.
     * Jede Aktivität hat auch seine eigene Toolbar, die in diesem Fall neben dem Titel auch einen
     * Zurück-Pfeil beinhaltet.
     */

    Toolbar helptoolbar;
    WebView webView;

    public static ScrollView sv;

    /**
     * Die Aktivität bekommt ein Layout zugeordnet (setContentView) und die oben deklarierten
     * Variablen die im Layout erstellten Objekte, z.B. die Toolbar R.id.toolbarHelp.
     *
     * @param savedInstanceState Daten der Aktivität (siehe Data)
     */

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helptoolbar = (Toolbar) findViewById(R.id.toolbarHelp);
        helptoolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
        helptoolbar.setTitle("Hilfe");
        setSupportActionBar(helptoolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sv = (ScrollView)findViewById(R.id.help_scrollview);

        webView = (WebView)findViewById(R.id.webview_dispenser);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

    }

    /**
     * Wenn der Zurück-Pfeil in der Toolbar gedrückt wird, wird die Aktivität beendet und man
     * kommt in die Aktivität zuvor (SettingsActivity) zurück.
     * Außerdem wird die Methode clicked() aufgerufen, die den automatischen Logout zurücksetzt.
     *
     * @param item
     * @return boolean
     */

    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            clicked();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Hat die selbe Funktion wie onOptionsItemSelected ist aber für das Verhalten beim Drücken des
     * physischen Zurück-Knopfs zuständig.
     * Außerdem wird die Methode clicked() aufgerufen, die den automatischen Logout zurücksetzt.
     *
     */

    public void onBackPressed ()
    {

        HelpActivity.this.finish();
        clicked();
    }

    /**
     * Wird ein TextView angeklickt, wird das dazu passende html-Dokument in den WebView geladen
     * und angezeigt.
     *
     * @param view Das geklickte Objekt, z.B. der TextView Medikamentengabe
     */

    @Override
    public void onClick(View view) {
        String URL = "";
        if (view.getId() == R.id.help_dispenser)
        {
            URL = "file:///android_asset/dispenser_help.html";
        }
        if (view.getId() == R.id.help_medication)
        {
            URL = "file:///android_asset/medication_help.html";
        }
        if (view.getId() == R.id.help_simplescan)
        {
            URL = "file:///android_asset/simplescan_help.html";
        }
        if (view.getId() == R.id.help_nomedication)
        {
            URL = "file:///android_asset/nomedication_help.html";
        }
        if (view.getId() == R.id.help_tandt)
        {
            URL = "file:///android_asset/recommendation_help.html";
        }

        webView.loadUrl(URL);
    }

}
