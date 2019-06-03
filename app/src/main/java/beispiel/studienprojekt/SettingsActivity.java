package beispiel.studienprojekt;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import static beispiel.studienprojekt.Data.clicked;
import static beispiel.studienprojekt.MainActivity.logouttime;
import static beispiel.studienprojekt.SettingsPreference.logout_time;
import static beispiel.studienprojekt.SettingsPreference.setLogoutTime;

/**
 * Die Klasse wird aufgerufen, wenn im Hauptmenü Einstellungen gedrückt wird.
 */

public class SettingsActivity extends AppCompatActivity
{
    /**
     * Die Aktivität hat nur eine Toolbar, die einen Titel und Zurück-Button enthält.
     */

    private Toolbar settings_toolbar;

    /**
     * Die Aktivität bekommt ein Layout zugeordnet (setContentView) und die Toolbar settings_toolbar
     * wird initialisiert.
     * Da sich die Einstellungen in einem PreferenceScreen befinden wird das FrameLayout in
     * R.layout.activity_settings (content_frame) durch die Aktivität SettingsPreference ersetzt.
     *
     * @param savedInstanceState Daten der Aktivität (siehe Data)
     */

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings_toolbar = (Toolbar) findViewById(R.id.toolbarsettings);
        settings_toolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
        settings_toolbar.setTitle("Einstellungen");
        setSupportActionBar(settings_toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new SettingsPreference()).commit();


    }

    /**
     * Wenn der Zurück-Pfeil in der Toolbar gedrückt wird, wird die Aktivität beendet und man
     * kommt in die Aktivität zuvor (MainActivity) zurück. Zudem wird die Methode clicked()
     * aufgerufen, die den automatischen Logout zurücksetzt.
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
     * Die Einstellungsaktivität wird beendet und die Methode clicked() aufgerufen, die den
     * automatischen Logout zurücksetzt.
     *
     */

    @Override
    public void onBackPressed ()
    {
        SettingsActivity.this.finish();
        clicked();
    }
}