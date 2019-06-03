package beispiel.studienprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static beispiel.studienprojekt.SettingsPreference.setTextSize;

/**
 * Diese Klasse enthält die Informationen und Methoden für die Dokumentation und wird aufgerufen,
 * wenn im Hauptmenü Dokumentation gedrückt wird.
 * Erweitert die Klasse Data die Daten wie z.B. die Dispenser enthält die für diese Aktivität
 * benötigt werden.
 *
 */

public class Documentation extends Data
{
    /**
     * Integer Variablen employee und patient, die die IDs für den Angestellten und Patienten
     * enthalten.
     * String Array atc enthält die ATC-Nummern der relevanten Medikamente.
     * Strings Datum und Typ (Dispenserbestückung | Medikamentengabe) für die Dokumentation.
     * strSeperator wird für die Umwandlung von Array in String bzw. String in Array benutzt.
     */

    public static boolean notEmpty;

    private int employee, patient;
    private String[] atc;
    private String date, type;
    public static String strSeparator = "__,__";

    private Toolbar documentation_toolbar;

    /**
     * Default Konstruktor und expliziter Konstruktor.
     */

    public Documentation () {}

    public Documentation (int employee, int patient, String[] atc, String date, String type)
    {
        this.employee = employee;
        this.patient = patient;
        this.atc = atc;
        this.date = date;
        this.type = type;

    }

    /**
     * Gibt den aktuellen Angestellten (ID) zurück.
     *
     * @return int
     */

    public int getEmployee() {
        return employee;
    }

    /**
     * Setzt den aktuellen Angestellten im Dokument.
     *
     * @param employee int Angestellter
     */

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    /**
     * Gibt den aktuellen Patienten (ID) zurück.
     *
     * @return int
     */

    public int getPatient() {
        return patient;
    }

    /**
     * Setzt den aktuellen Patienten im Dokument.
     *
     * @param patient int Patient
     */

    public void setPatient(int patient) {
        this.patient = patient;
    }

    /**
     * Gibt die aktuellen ATC-Nummern zurück.
     *
     * @return String[]
     */

    public String[] getAtc() {
        return atc;
    }

    /**
     * Setzt die aktuellen ATC-Nummern im Dokument.
     *
     * @param atc String[] ATC-Nummern
     */

    public void setAtc(String[] atc) {
        this.atc = atc;
    }

    /**
     * Gibt das aktuelle Datum zurück.
     *
     * @return String
     */

    public String getDate() {
        return date;
    }

    /**
     * Setzt das aktuelle Datum im Dokument
     *
     * @param date String Datum
     */

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gibt den aktuellen Typ zurück.
     *
     * @return String
     */

    public String getType() {
        return type;
    }

    /**
     * Setzt den aktuellen Typ im Dokument.
     *
     * @param type String Typ
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Wandelt ein String Array in ein String um.
     * Das String Array array wird durchlaufen und die Einträge in str geschrieben. Die if-Abfrage
     * dient nur dazu, dass nach dem letzten Element kein Komma eingefügt wird, sonst schon.
     *
     * @param array String Array das konvertiert werden soll
     * @return String
     */

    public static String convertArrayToString(String[] array){
        String str = "";
        try {
            for (int i = 0; i < array.length; i++) {
                str = str + array[i];
                if (i < array.length - 1) {
                    str = str + strSeparator;
                    notEmpty =  true;
                }
            }
        } catch (NullPointerException e)
        {
            notEmpty = false;
            Log.d("Leer", notEmpty + "");
        }
        return str;
    }

    /**
     * Wandelt ein String in ein String Array um.
     * Der String str wird durchlaufen und bei jedem Komma (strSeperator) wird ein neuer Eintrag
     * in das Array geschrieben.
     *
     * @param str String der konvertiert werden soll
     * @return String[]
     */

    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }

    /**
     * Gibt einen String zurück der die gespeicherten Daten enthält.
     *
     * @return String
     */

    @Override
    public String toString() {
        return "Mitarbeiter: " + employee +
                ", Patient: " + patient +
                ", Medikamente: " + Arrays.toString(atc) +
                ", Datum: " + date +
                ", Typ: " + type;
    }

    /**
     * Die Aktivität bekommt ein Layout zugeordnet (setContentView) und die Toolbar wird
     * initalisiert.
     * Dann wird die in Data vorhandene Variable Documentation List documentations initalisiert und
     * enthält alle geschriebenen Dokumentationen. Dann wird ein Set mit allen Dokumentationen
     * erstellt, durchlaufen und das String Array result mit den Dokumentationseinträgen befüllt.
     * Anschließend erhält der TextView doc diese Einträge und wird dem Nutzer präsentiert.
     *
     * @param savedInstanceState Daten der Aktivität
     */

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentation);

        documentations = documentationHandler.getAllDocumentations();
        Log.d("Documentations", documentations.size() + "");

        TextView doc = (TextView) findViewById(R.id.documentationTextViewResult);
        Set DocSet = new HashSet(documentations);
        String[] result = new String[DocSet.size()];
        String output = "";
        for (int i = 0; i < DocSet.size(); i++)
        {
            result[i] = documentations.get(i) + "";
        }

        for (int i = 0; i < result.length; i++)
        {
            output = output + result[i] + "\n";
        }

        doc.setText(output);
        setTextSize(doc);

        documentation_toolbar = (Toolbar)findViewById(R.id.toolbarDocumentation);
        documentation_toolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
        documentation_toolbar.setTitle("Dokumentation");
        setSupportActionBar(documentation_toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Wenn der Zurück-Pfeil in der Toolbar gedrückt wird, wird die Aktivität beendet und man
     * kommt in die Aktivität zuvor (MainActivity) zurück.
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
            Intent home = new Intent(Documentation.this, MainActivity.class);
            startActivity(home);
            Documentation.this.finish();
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

    @Override
    public void onBackPressed ()
    {
        Intent home = new Intent(Documentation.this, MainActivity.class);
        startActivity(home);
        Documentation.this.finish();
        clicked();
    }
}
