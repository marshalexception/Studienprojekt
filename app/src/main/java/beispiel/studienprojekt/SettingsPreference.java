package beispiel.studienprojekt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.google.zxing.client.android.BeepManager;

import static beispiel.studienprojekt.Data.clicked;
import static beispiel.studienprojekt.MainActivity.DI;
import static beispiel.studienprojekt.MainActivity.DO;
import static beispiel.studienprojekt.MainActivity.E;
import static beispiel.studienprojekt.MainActivity.L;
import static beispiel.studienprojekt.MainActivity.M;
import static beispiel.studienprojekt.MainActivity.S;
import static beispiel.studienprojekt.MainActivity.logouttime;

/**
 * Die Klasse wird implizit durch SettingsActivity aufgerufen, wenn im Hauptmenü Einstellungen
 * gedrückt wird.
 * Die Einstellungen werden als Preference genutzt und nicht als normale Aktivität, so dass in der
 * MainActivity per einfachen Aufrufs setDefaultValues(...) die Vorlieben des Nutzers bei z.B. der
 * automatischen Logoutzeit direkt übernommen werden können. In einer Aktivität würde dies eine
 * NullPointerException werfen, da diese noch nicht gestartet wurde und die Einstellungen also bei
 * jedem Start der App neu gesetzt werden müssten.
 */

public class SettingsPreference extends PreferenceFragment
{
    /**
     * Sie enthält Variablen, die die vorhandenen Preferences in R.xml.settings zugeordnet werden.
     * Außerdem Integer-Variablen die als Index genutzt werden, z.B. bei der Textgröße von 1
     * (klein) bis 3 (groß).
     */

    private Preference sound, help;
    public static ListPreference logout, textsize;
    public static boolean sound_setting = true;
    public static int logout_time = 0;
    public static int logouttimeindex = 0;
    public static int textsizeindex = 2;

    /**
     * Die Aktivität bekommt ein Layout zugeordnet (addPreferencesFromResource) und die oben
     * deklarierten Variablen die im Layout erstellten Objekte, z.B. die SwitchPreference sound die
     * Preference "pref_sound".
     *
     *
     * @param savedInstanceState Daten der Aktivität (siehe Data)
     */

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        sound = findPreference("pref_sound");
        sound.setDefaultValue(true);
        sound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            /**
             * o wird in einen boolschen-Wert konvertiert, also ob die Switch ein- oder
             * ausgeschaltet ist.
             * Je nachdem wird der Text geändert und die boolsche-Variable sound_settings geändert.
             *
             * @param preference Preference-Objekt, hier SwitchPreference pref_sound
             * @param o Status der Preference
             * @return boolean
             */
            @Override
            public boolean onPreferenceChange(Preference preference, Object o)
            {
                boolean isOn = (boolean) o;

                if (isOn)
                {
                    sound.setSummary(R.string.settings_sound_on);
                    sound_setting= true;
                } else {
                    sound.setSummary(R.string.settings_sound_off);
                    sound_setting = false;
                }

                return true;
            }
        });


        logout = (ListPreference) findPreference("pref_auto_logout");
        logout.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            /**
             * Die Methode prüft welchen Status o hat (1-7, für sieben verschiedene Logout
             * Möglichkeiten). Je nachdem welchen Status o hat wird die methode setLogoutTime mit
             * der dazu passenden Zeit aufgerufen und die Index-Variable logouttimeindex wird auf
             * den zu o passenden Wert gesetzt.
             *
             * @param preference Preference-Objekt, hier ListPreference pref_auto_logout
             * @param o Status der Preference
             * @return boolean
             */

            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                if (o.equals("1"))
                {
                    setLogoutTime(5);
                    logouttimeindex = 1;
                    preference.setSummary("nach 5 Minuten");

                }
                if (o.equals("2"))
                {
                    setLogoutTime(10);
                    logouttimeindex = 2;
                    preference.setSummary("nach 10 Minuten");
                }
                if (o.equals("3"))
                {
                    setLogoutTime(20);
                    logouttimeindex = 3;
                    preference.setSummary("nach 20 Minuten");
                }
                if (o.equals("4"))
                {
                    setLogoutTime(30);
                    logouttimeindex = 4;
                    preference.setSummary("nach 30 Minuten");
                }
                if (o.equals("5"))
                {
                    setLogoutTime(40);
                    logouttimeindex = 5;
                    preference.setSummary("nach 40 Minuten");
                }
                if (o.equals("6"))
                {
                    setLogoutTime(50);
                    logouttimeindex = 6;
                    preference.setSummary("nach 50 Minuten");
                }
                if (o.equals("7"))
                {
                    setLogoutTime(60);
                    logouttimeindex = 7;
                    preference.setSummary("nach 60 Minuten");
                }

                return true;
            }
        });

        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            /**
             * Als Ergänzung zum OnPreferenceChangeListener, hier wird allerdings nur die Methode
             * clicked() aufgerufen um den automatischen Logout zurückzusetzen.
             *
             * @param preference Preference-Objekt, hier ListPreference pref_auto_logout
             * @return boolean
             */

            @Override
            public boolean onPreferenceClick(Preference preference) {
                clicked();
                return true;
            }
        });

        textsize = (ListPreference) findPreference("pref_text_size");
        textsize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            /**
             * Analog zum onPreferenceChange von logout, nur dass es sich hierbei um die
             * Einstellungen zur Textgröße handelt. Zudem wird die Methode clicked() aufgerufen um
             * den automatischen Logout zurückzusetzen.
             *
             * @param preference Preference-Objekt, hier ListPreference pref_text_size
             * @param o Status der Preference
             * @return boolean
             */
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                clicked();
                if (o.equals("1"))
                {
                    setSmallText();
                    textsizeindex = 1;
                    preference.setSummary("klein");
                }
                if (o.equals("2"))
                {
                    setNormalText();
                    textsizeindex = 2;
                    preference.setSummary("normal");
                }
                if (o.equals("3"))
                {
                    setLargeText();
                    textsizeindex = 3;
                    preference.setSummary("groß");
                }

                return true;
            }
        });

        help = findPreference("pref_help");
        help.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            /**
             * Wenn auf die Preference geklickt wird wird die Aktivität HelpActivity gestartet.
             * Außerdem wird die Methode clicked() aufgerufen um den automatischen Logout
             * zurückzusetzen.
             *
             * @param preference Preference-Objekt, hier PreferenceScreen pref_help
             * @return boolean
             */

            @Override
            public boolean onPreferenceClick(Preference preference) {
                clicked();
                Intent helpActivity = new Intent (getActivity(), HelpActivity.class);
                getActivity().startActivity(helpActivity);

                return true;
            }
        });

        SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String tmp_logout = sharedprefs.getString(logout.getKey(), "");
        String tmp_textsize = sharedprefs.getString(textsize.getKey(), "");
        onPreferenceChange(logout, tmp_logout);
        onPreferenceChange(textsize, tmp_textsize);

    }

    public static boolean onPreferenceChange(Preference preference, Object o) {

        if (preference.getKey().equals("pref_auto_logout"))
        {
            if (o.equals("1"))
            {
                preference.setSummary("nach 5 Minuten");

            }
            if (o.equals("2"))
            {
                preference.setSummary("nach 10 Minuten");
            }
            if (o.equals("3"))
            {
                preference.setSummary("nach 20 Minuten");
            }
            if (o.equals("4"))
            {
                preference.setSummary("nach 30 Minuten");
            }
            if (o.equals("5"))
            {
                preference.setSummary("nach 40 Minuten");
            }
            if (o.equals("6"))
            {
                preference.setSummary("nach 50 Minuten");
            }
            if (o.equals("7"))
            {
                preference.setSummary("nach 60 Minuten");
            }
        }
        if (preference.getKey().equals("pref_text_size"))
        {
            if (o.equals("1"))
            {
                preference.setSummary("klein");

            }
            if (o.equals("2"))
            {
                preference.setSummary("normal");
            }
            if (o.equals("3"))
            {
                preference.setSummary("groß");
            }
        }


        return true;
    }

    /**
     * Methode wird bei jedem Scan aufgerufen. Sie überprüft den Status von sound_setting und
     * schaltet den Ton an bzw. ab.
     *
     * @param beepManager BeepManager Ton-Einstellungen für den jeweiligen Scanner einstellen
     */

    public static void setSound (BeepManager beepManager)
    {
        if (sound_setting)
        {
            beepManager.setBeepEnabled(true);
            beepManager.playBeepSound();
        } else {
            beepManager.setBeepEnabled(false);
        }
    }

    /**
     * Wird bei Änderungen der automatischen Logoutzeit aufgerufen. Die Variable logout_time bekommt
     * den Wert des Parameters time und der TextView im Hauptmenü bekommt den geänderten Wert
     * zugeordnet.
     *
     * @param time Int Zeit bis zum Logout
     */

    public static void setLogoutTime(int time)
    {

        logout_time = time;
        logouttime.setText(logout_time + "");
    }

    /**
     * Gibt die Logout-Zeit bis zum automatischen Logout zurück.
     *
     * @return Int
     */

    public static int getLogoutTime ()
    {
        return 17;
    }

    /**
     * Methode überprüft textsizeindex und ändert die Textgröße des Parameters view (TextView) je
     * nach Status von textsizeindex.
     *
     * @param view TextView dessen Textgröße geändert werden soll
     */

    public static void setTextSize (TextView view) {
        if (textsizeindex == 1) {
            view.setTextSize(15);
        }
        if (textsizeindex == 2)
        {
            view.setTextSize(20);
        }
        if (textsizeindex == 3)
        {
            view.setTextSize(25);
        }
    }

    /**
     * Methode überprüft textsizeindex und ändert die Textgröße des Parameters view
     * (CheckedTextView) je nach Status von textsizeindex.
     * @param view CheckedTextView dessen Textgröße geändert werden soll
     */

    public static void setTextSize (CheckedTextView view) {
        if (textsizeindex == 1) {
            view.setTextSize(15);
        }
        if (textsizeindex == 2)
        {
            view.setTextSize(20);
        }
        if (textsizeindex == 3)
        {
            view.setTextSize(25);
        }
    }

    /**
     * Ändert die Textgröße aller TextViews im Hauptmenü in klein.
     */

    public static void setSmallText ()
    {
        M.setTextSize(20);
        DI.setTextSize(20);
        S.setTextSize(20);
        DO.setTextSize(20);
        E.setTextSize(20);
        L.setTextSize(20);

    }

    /**
     * Ändert die Textgröße aller TextViews im Hauptmenü in normal.
     */

    public static void setNormalText ()
    {
        M.setTextSize(25);
        DI.setTextSize(25);
        S.setTextSize(25);
        DO.setTextSize(25);
        E.setTextSize(25);
        L.setTextSize(25);
    }

    /**
     * Ändert die Textgröße aller TextViews im Hauptmenü in groß.
     */

    public static void setLargeText ()
    {
        M.setTextSize(30);
        DI.setTextSize(30);
        S.setTextSize(30);
        DO.setTextSize(30);
        E.setTextSize(30);
        L.setTextSize(30);
    }

    /**
     * Wird beim Start der MainActivity aufgerufen und prüft die Textgröße für das Hauptmenü bzw.
     * ändert diese bei Bedarf.
     */

    public static void SetTextSize(int textsize)
    {
        if (textsize == 1)
        {
            setSmallText();

        }
        if (textsize == 2)
        {
            setNormalText();
        }
        if (textsize == 3)
        {
            setLargeText();
        }
    }

    /**
     * Wird beim Start der MainActivity aufgerufen und prüft die automatische Logoutzeit für das
     * Hauptmenü bzw. ändert diese bei Bedarf.
     *
     * @return Int
     */

    public static int MainMenuLogoutTime (int time) {
        if (time == 1) {
            return 5;
        } else if (time == 2) {
            return 10;
        } else if (time == 3) {
            return 20;
        } else if (time == 4) {
            return 30;
        } else if (time == 5) {
            return 40;
        } else if (time == 6) {
            return 50;
        } else
            return 60;
    }
}
