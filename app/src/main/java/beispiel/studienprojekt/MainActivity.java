package beispiel.studienprojekt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import static beispiel.studienprojekt.DispenserScanResult.counter;
import static beispiel.studienprojekt.LoginActivity.isautologout;
import static beispiel.studienprojekt.LoginActivity.loggedin;
import static beispiel.studienprojekt.SettingsPreference.MainMenuLogoutTime;
import static beispiel.studienprojekt.SettingsPreference.SetTextSize;
import static beispiel.studienprojekt.SettingsPreference.logouttimeindex;

/**
 * Klasse die nach erfolgreichem Login auftritt und das Hauptmenü beinhaltet.
 * Erweitert die Klasse Data, da z.B. die Variable currentEmployee gesetzt werden muss.
 * Sie implementiert außerdem den View.OnClickListener, der eine zentrale Methode onClick
 * bereitstellt.
 */

public class MainActivity extends Data implements View.OnClickListener
{

    /**
     * Enthält diverse Text- und ImageViews die für das Hauptmenü benötigt werden, z.B. der Name
     * des im Moment angemeldeten Mitarbeiters, der in der Toolbar angezeigt wird. Zudem eine
     * Integer Variable index, die dafür gebraucht wird beim Dispenser Scan zwischen Medikamentengabe
     * und Dispenserbestückung zu unterscheiden.
     */


    Toolbar toolbar;
    public static TextView M, DI, S, E, DO, L, username;
    public static ImageView m, di, s, e, dok, l;
    public static TextView logouttime;
    private Context mContext;

    public static int index = 0;

    public static MainActivity MA;

    /**
     * Die Aktivität bekommt ein Layout zugeordnet (setContentView) und die oben deklarierten
     * Variablen die im Layout erstellten Objekte, z.B. die verbleibende Zeit bis zum automatischen
     * Logout R.id.toolbar_logouttime.
     * Außerdem wird die Textgröße und der Zähler, der für die Dispenserbestückung benötigt wird,
     * initialisiert.
     *
     * @param savedInstanceState Daten der Aktivität
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MA = MainActivity.this;

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
        int time = Integer.parseInt(sharedprefs.getString("pref_auto_logout", "2"));
        logouttimeindex = time;
        currentTime = MainMenuLogoutTime(time);

        mContext = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbarMainActivity);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hauptmenü");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));


        username = (TextView)findViewById(R.id.toolbar_username);
        username.setText("Nutzer:\n" + currentEmployee.getFirstname()+" "+currentEmployee.getLastname());
        username.setTextColor(getResources().getColor(R.color.ToolbarTitle));


        logouttime = (TextView)findViewById(R.id.toolbar_logouttime);
        logouttime.setText(currentTime + "");
        logouttime.setTextColor(getResources().getColor(R.color.ToolbarTitle));
        logouttime.setTextSize(20);


        M = (TextView)findViewById(R.id.TextViewMedication);
        DI = (TextView)findViewById(R.id.TextViewDispenser);
        S = (TextView)findViewById(R.id.TextViewSimpleScan);
        E = (TextView)findViewById(R.id.TextViewSettings);
        DO = (TextView)findViewById(R.id.TextViewDocumentation);
        L = (TextView)findViewById(R.id.TextViewLogout);

        m = (ImageView)findViewById(R.id.imageViewMedication);
        di = (ImageView)findViewById(R.id.imageViewDispenser);
        s = (ImageView)findViewById(R.id.imageViewSimpleScan);
        dok = (ImageView)findViewById(R.id.imageViewDocumentation);
        e = (ImageView)findViewById(R.id.imageViewSettings);
        l = (ImageView)findViewById(R.id.imageViewLogout);

        E = (TextView) findViewById(R.id.TextViewSettings);
        E.setOnClickListener(this);

        L = (TextView) findViewById(R.id.TextViewLogout);
        L.setOnClickListener(this);


        int textsize = Integer.parseInt(sharedprefs.getString("pref_text_size", "2"));
        SetTextSize(textsize);

        counter = 0;

    }

    /**
     * Je nachdem, welches Bild oder welcher Text angeklickt wird, wird die dazu gehörige Aktivität
     * gestartet, also z.B. beim Einzelscan SimpleScan.class. Beim Klick auf Logout wird die
     * Methode onBackPressed() aufgerufen.
     *
     * @param view Das geklickte Objekt, z.B. der Text Logout
     */

    @Override
    public void onClick(View view)
    {
        clicked();
        if (view.getId() == R.id.TextViewDispenser || view.getId() == R.id.imageViewDispenser)
        {
            index = 2;
            Intent DispScan = new Intent(this, DispenserScan.class);
            startActivity(DispScan);
        }
        if (view.getId() == R.id.TextViewMedication || view.getId() == R.id.imageViewMedication)
        {
            index = 1;
            Intent DispScan = new Intent(this, DispenserScan.class);
            startActivity(DispScan);
        }
        if (view.getId() == R.id.TextViewSimpleScan || view.getId() == R.id.imageViewSimpleScan)
        {
            index = 3;
            Intent simplescan = new Intent(this, SimpleScan.class);
            startActivity(simplescan);
        }

        if (view.getId() == R.id.TextViewSettings || view.getId() == R.id.imageViewSettings)
        {
            Intent settings = new Intent(mContext, SettingsActivity.class);
            startActivity(settings);
        }

        if(view.getId() == R.id.TextViewDocumentation || view.getId() == R.id.imageViewDocumentation)
        {
            Intent documentation = new Intent (this, Documentation.class);
            startActivity(documentation);
        }

        if (view.getId() == R.id.TextViewLogout || view.getId() == R.id.imageViewLogout)
        {
            onBackPressed();
        }
    }

    /**
     * Hat die selbe Funktion wie onOptionsItemSelected ist aber für das Verhalten beim Drücken des
     * physischen Zurück-Knopfs zuständig.
     * Der Nutzer muss den Logout bestätigen und gelangt dann in die LoginActivity. Außerdem wird
     * die Dokumentationsdatenbank gelöscht, die Logout-Zeit zurückgesetzt und loggedin auf true
     * gesetzt (Beim nächsten Login wird dann restart() statt start() ausgeführt, siehe
     * LoginActivity).
     *
     */

    public void onBackPressed ()
    {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout").setMessage("Sind Sie sich sicher, dass Sie sich ausloggen möchten?")
                .setCancelable(false)
                .setNegativeButton("Nein", null)
                .setNeutralButton("Ja", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        isautologout = false;
                        Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(logout);
                        MainActivity.this.finish();
                        documentationHandler.delete();
                        reset();
                        loggedin = true;
                    }
                });
        MakeDialog(ad);
    }

    /**
     * Wird beim Start des Timers und bei jeder vergangenen Minute in Data aufgerufen, damit die
     * aktuelle verbleibende Zeit im TextView logouttime angezeigt wird.
     *
     * @param time Zeit die vom Nutzer festgelegt wurde bei der nach Ablauf dieser automatisch
     *             ausgeloggt wird.
     */

    public static void displayLogoutTime(int time)
    {
        logouttime.setText(time + "");
    }
}
