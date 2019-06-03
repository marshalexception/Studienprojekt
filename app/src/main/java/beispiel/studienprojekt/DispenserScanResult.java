package beispiel.studienprojekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static beispiel.studienprojekt.DispenserMedicationScan.med0;
import static beispiel.studienprojekt.DispenserMedicationScan.med1;
import static beispiel.studienprojekt.DispenserMedicationScan.med2;
import static beispiel.studienprojekt.DispenserMedicationScan.med3;
import static beispiel.studienprojekt.DispenserMedicationScan.med4;
import static beispiel.studienprojekt.SettingsPreference.SetTextSize;
import static beispiel.studienprojekt.SettingsPreference.setTextSize;
import static beispiel.studienprojekt.SettingsPreference.textsizeindex;

/**
 * Klasse die aufgerufen wird, wenn Dispenserbestückung im Hauptmenü aufgerufen wurde und ein
 * gültiger Dispenser gescannt wurde. Sie zeigt die Medikamente an mit denen der Dispenser bestückt
 * werden muss.
 * Erweitert die Klasse Data, die Daten wie z.B. Medikamente enthält die für diese Aktivität
 * benötigt werden. Sie implementiert außerdem den View.OnClickListener, der eine zentrale Methode
 * onClick bereitstellt.
 *
 */

public class DispenserScanResult extends Data implements View.OnClickListener
{
    /**
     * Jede Aktivität hat auch seine eigene Toolbar, die in diesem Fall neben dem Titel auch einen
     * Zurück-Pfeil beinhaltet (setSupportActionBar).
     * Der TextView patient zeigt den zum Dispenser zugehörigen Patienten an.
     * Auch hier gibt es noch eine Fortschrittsleiste, die sich bei erfolgreichen Scannen von
     * Medikamenten füllt.
     * Der Prototyp verfügt bei den Checkboxen nicht über eine dynamische Erzeugung, sondern über
     * fünf festgelegte Checkboxen (ctv0 - ctv4), wir gehen also davon aus, dass der Patient maximal
     * fünf Medikamente erhält.
     * Es gibt zwei Buttons, wobei next für das Scannen von Medikamenten (DispenserMedicationScan)
     * oder Scannen von Dispensern (DispenserScan) zuständig ist und returnbutton für das
     * zurückkehren in das Hauptmenü.
     * Zudem gibt es mehrere Arrays und Strings, die für die Verarbeitung der im Dispenser
     * vorhandenen Medikamente zuständig sind.
     */

    protected Toolbar dispenser_toolbar;
    protected TextView patient;
    protected ProgressBar progressBar;
    protected static CheckedTextView ctv0, ctv1, ctv2, ctv3, ctv4;
    protected Button next, returnbutton;

    protected static ArrayList<String> mpATC = new ArrayList<String>();
    protected static ArrayList<Medication> dispenser_medication = new ArrayList<Medication>();
    protected static ArrayList<String> meds = new ArrayList<String>();
    protected Set medicationresult;
    public static String[] tmp, result;
    protected static int counter = 0;

    /**
     * Die Aktivität ruft beim Start die Methode loadActivity() auf.
     *
     * @param savedInstanceState Daten der Aktivität
     */

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dispenserscanresult);


        dispenser_toolbar = (Toolbar) findViewById(R.id.toolbarDispenserScanResult);
        dispenser_toolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
        dispenser_toolbar.setTitle("Dispenserbestückung");
        setSupportActionBar(dispenser_toolbar);

        SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
        int textsize = Integer.parseInt(sharedprefs.getString("pref_text_size", "2"));
        textsizeindex = textsize;

        patient = (TextView) findViewById(R.id.dispenserpatient);
        setTextSize(patient);
        patient.setTextColor(getResources().getColor(android.R.color.black));

        progressBar = (ProgressBar) findViewById(R.id.progressbar_dispenser);
        progressBar.setProgress(0);

        next = (Button) findViewById(R.id.dispensebutton);
        returnbutton = (Button) findViewById(R.id.returnbutton_dispenser);


        ctv0 = (CheckedTextView) findViewById(R.id.ctv0);
        ctv1 = (CheckedTextView) findViewById(R.id.ctv1);
        ctv2 = (CheckedTextView) findViewById(R.id.ctv2);
        ctv3 = (CheckedTextView) findViewById(R.id.ctv3);
        ctv4 = (CheckedTextView) findViewById(R.id.ctv4);
        ctv0.setText("");
        ctv1.setText("");
        ctv2.setText("");
        ctv3.setText("");
        ctv4.setText("");
        mpATC = new ArrayList<String>();
        dispenser_medication = new ArrayList<Medication>();
        meds = new ArrayList<String>();
        returnbutton.setVisibility(View.INVISIBLE);
        next.setText("Weiter");
        setTextSize(ctv0);
        setTextSize(ctv1);
        setTextSize(ctv2);
        setTextSize(ctv3);
        setTextSize(ctv4);


        String [] x = scannedDispenser.getDispenser_Medications();

        /**
         * Die Liste der MedicationPatient Relation in Data wird durchlaufen und gleichzeitig auch
         * das String Array x, das die MedicationPatientIDs des gescannten Dispenser enthält.
         * Sollten die Einträge gleich sein, werden den ArrayListen mpATC und meds die zugehörige
         * ATC Nummer hinzugefügt und der ArrayList dispenser_medication durch Aufruf der Methode
         * getMedication das dazugehörige Medikament.
         */

        for (MedicationPatient mp : mpList)
        {
            for (int i = 0; i < x.length; i++) {
                if (mp.getMedicationPatientId() == Integer.parseInt(x[i])) {
                    mpATC.add(mp.getAtc());
                    setScannedMedicationPatient(mp);
                    Medication tmp = getMedication(mp.getAtc());
                    dispenser_medication.add(tmp);
                    meds.add(tmp.getAtc());
                }
            }
        }

        /**
         * Die Patienten der Datenbank werden durchlaufen und mit der vorher gesetzten Variable
         * scannedMedicationPatient verglichen, bzw. die jeweiligen IDs. Bei Gleichheit wird die
         * Variable scannedPatient auf den passenden Patienten gesetzt und im TextView angezeigt.
         */

        for (Patient patient: patients)
        {
            Patient tmp = patient;
            if (tmp.getId() == scannedMedicationPatient.getId())
            {
                setScannedPatient(tmp);
            }
        }
        patient.setText("Patient: \n" + scannedPatient.toString());

        /**
         * Um doppelten Einträgen vorzubeugen, wird ein Set medicationresult (von meds)
         * initialisiert, dass diesen Fall ausschließt (einzigartige ATC-Nummern).
         * Dieses Set wird dann durchlaufen, und die String Arrays tmp und result gefüllt:
         * - result bekommt die ATC-Nummern der für den Dispenser relevanten Medikamente
         * - tmp bekommt die für das jeweilige Medikament relevanten Informationen (z.B. Dosierung)
         */

        medicationresult = new HashSet(meds);
        tmp = new String[medicationresult.size()];
        result = new String[medicationresult.size()];
        for (int i = 0; i < medicationresult.size(); i++)
        {
            result[i] = mpATC.get(i);
            tmp[i] = dispenser_medication.get(i).getName() + " (" +
                    getDose(dispenser_medication.get(i)) + "x " +
                    dispenser_medication.get(i).getUnit() + ") " + " - " +
                    getTaking(dispenser_medication.get(i)) + " (" +
                    dispenser_medication.get(i).getRoamoa() +") ";
        }
        /**
         * Je nach Größe von medicationresult (Anzahl Medikamente Dispenser), werden die einzelnen
         * Checkboxen mit den Informationen aus tmp gefüllt und die Checkboxen sichtbar für den
         * Nutzer.
         */

        int i = 0;
        if (i == 0 && i < medicationresult.size())
        {
            ctv0.setText(tmp[0]);
            ctv0.setVisibility(View.VISIBLE);
            i++;
        }
        if (i == 1 && i < medicationresult.size())
        {
            ctv1.setText(tmp[1]);
            ctv1.setVisibility(View.VISIBLE);
            i++;
        }
        if (i == 2 && i < medicationresult.size())
        {
            ctv2.setText(tmp[2]);
            ctv2.setVisibility(View.VISIBLE);
            i++;
        }
        if (i == 3 && i < medicationresult.size())
        {
            ctv3.setText(tmp[2]);
            ctv3.setVisibility(View.VISIBLE);
            i++;
        }
        if (i ==4 && i < medicationresult.size())
        {
            ctv4.setText(tmp[2]);
            ctv4.setVisibility(View.VISIBLE);
            i++;
        }

        next.setOnClickListener(this);
        returnbutton.setOnClickListener(this);

        /**
         * Falls die in DispenserMedicationScan vorhandenen boolschen Variablen med0 - med4 auf
         * true gesetzt wurden, also das zugehörige Medikament gescannt wurde, wird die passende
         * Checkbox gecheckt und die Fortschrittsleiste je nach Stand des counters gefüllt.
         */

        if (med0)
        {
            ctv0.setChecked(true);
            double tmp = ((double)counter/(double)medicationresult.size())*100;
            progressBar.setProgress((int)tmp);
        }
        if (med1)
        {
            ctv1.setChecked(true);
            double tmp = ((double)counter/(double)medicationresult.size())*100;
            progressBar.setProgress((int)tmp);
        }
        if (med2)
        {
            ctv2.setChecked(true);
            double tmp = ((double)counter/(double)medicationresult.size())*100;
            progressBar.setProgress((int)tmp);
        }
        if (med3)
        {
            ctv3.setChecked(true);
            double tmp = ((double)counter/(double)medicationresult.size())*100;
            progressBar.setProgress((int)tmp);
        }
        if (med4)
        {
            ctv4.setChecked(true);
            double tmp = ((double)counter/(double)medicationresult.size())*100;
            progressBar.setProgress((int)tmp);
        }

        /**
         * counter wird bei jedem erfolgreichen Scan eines Medikaments erhöht und damit die
         * if-Abfrage erfüllt, wenn alle Medikamente des Dispensers gescannt wurden. Der Nutzer
         * erhält darauf hin eine Popup-Nachricht, dass der Dispenser erfolgreich bestückt wurde
         * und die Buttons werden geändert, so dass jetzt die Optionen bestehen einen neuen
         * Dispenser zu befüllen oder zum Hautpmenü zurückzukehren.
         * Außerdem wird ein neues Dokument erstellt und damit dokumentiert, dass der Dispenser
         * bestückt wurde. Das Dokument erhält alle relevanten Informationen für die 7R Regel, also
         * z.B. den Mitarbeiter, die Medikamente, den Patienten und das Datum.
         */

        if (counter == medicationresult.size())
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(DispenserScanResult.this);
            ad.setIcon(R.drawable.dispenserbestueckung)
                    .setTitle("Dispenserbestückung").setCancelable(false)
                    .setMessage("Dispenser für " +
                            scannedPatient.getFirstname() + " " + scannedPatient.getLastname() +
                            " bestückt.")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            MakeDialog(ad);

            next.setText("Neuen Dispenser scannen");
            returnbutton.setVisibility(View.VISIBLE);

            stockedDispensers.add(scannedDispenser.getDispenser_Id());

            Documentation documentation = new Documentation();
            documentation.setEmployee(currentEmployee.getId());
            documentation.setPatient(getScannedPatient().getId());
            documentation.setAtc(result);
            String date = new SimpleDateFormat("dd-MM-yyyy', 'HH:mm").format(new Date());
            documentation.setDate(date);
            documentation.setType("Dispenserbestückung");
            documentationHandler.addDocumentation(documentation);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

       // loadActivity();

    }

    /**
     * Wenn der Zurück-Pfeil in der Toolbar gedrückt wird, wird die Aktivität beendet und man
     * kommt in die Aktivität zuvor (MainActivity) zurück.
     *
     * @param item
     * @return boolean
     */

    public boolean onOptionsItemSelected (MenuItem item)
    {
        clicked();
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            counter = 0;
            med0 = false;
            med1 = false;
            med2 = false;
            med3 = false;
            med4 = false;
            ctv0.setChecked(false);
            ctv1.setChecked(false);
            ctv2.setChecked(false);
            ctv3.setChecked(false);
            ctv4.setChecked(false);
            mpATC = new ArrayList<String>();
            dispenser_medication = new ArrayList<Medication>();
            meds = new ArrayList<String>();
            returnbutton.setVisibility(View.INVISIBLE);
            next.setText("Weiter");
        }
        if (item.getItemId() == R.id.back_to_home)
        {
            Intent backToHome = new Intent (DispenserScanResult.this, MainActivity.class);
            startActivity(backToHome);
            DispenserScanResult.this.finish();
            counter = 0;
            med0 = false;
            med1 = false;
            med2 = false;
            med3 = false;
            med4 = false;
            ctv0.setChecked(false);
            ctv1.setChecked(false);
            ctv2.setChecked(false);
            ctv3.setChecked(false);
            ctv4.setChecked(false);
            mpATC = new ArrayList<String>();
            dispenser_medication = new ArrayList<Medication>();
            meds = new ArrayList<String>();
            returnbutton.setVisibility(View.INVISIBLE);
            next.setText("Weiter");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.dispenserscanresult_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Hat die selbe Funktion wie onOptionsItemSelected ist aber für das Verhalten beim Drücken des
     * physischen Zurück-Knopfs zuständig.
     */

    @Override
    public void onBackPressed ()
    {
        counter = 0;
        med0 = false;
        med1 = false;
        med2 = false;
        med3 = false;
        med4 = false;
        ctv0.setChecked(false);
        ctv1.setChecked(false);
        ctv2.setChecked(false);
        ctv3.setChecked(false);
        ctv4.setChecked(false);
        mpATC = new ArrayList<String>();
        dispenser_medication = new ArrayList<Medication>();
        meds = new ArrayList<String>();
        returnbutton.setVisibility(View.INVISIBLE);
        next.setText("Weiter");
        Intent home = new Intent(DispenserScanResult.this, MainActivity.class);
        startActivity(home);
        DispenserScanResult.this.finish();
        clicked();
    }

    /**
     * Solange der counter kleiner als medicationresult.size ist, also noch nicht alle Medikamente
     * gescannt wurden, wird immer bei Klick auf R.id.dispensebutton (next) erneut die Scan-Funktion
     * (DispenserMedicationScan) aufgerufen.
     * Falls counter == medicationresult.size ist, wird der Text vom Button auf "Neuen Dispenser
     * scannen" gesetzt. Beim Klicken auf den Button wird dann der Ausgangszustand der Klasse
     * wiederhergestellt. Die Checkboxen werden wieder unchecked, der counter auf 0 gesetzt, etc.
     * Das Gleiche passiert beim Klicken auf den Button R.id.returnbutton_dispenser, nur dass hier
     * nicht DispenserScan.class aufgerufen wird sondern das Hauptmenü (MainActivity.class).
     * Außerdem wird die Methode clicked() aufgerufen, die den automatischen Logout zurücksetzt.
     *
     * @param view Das geklickte Objekt, z.B. der next-Button
     */

    @Override
    public void onClick (View view)
    {
        clicked();

        if (view.getId() == R.id.dispensebutton)
        {
            if (counter < medicationresult.size())
            {
                Intent MedScan = new Intent(DispenserScanResult.this, DispenserMedicationScan.class);
                startActivity(MedScan);

            }
            if (next.getText().equals("Neuen Dispenser scannen"))
            {
                counter = 0;
                med0 = false;
                med1 = false;
                med2 = false;
                med3 = false;
                med4 = false;
                ctv0.setChecked(false);
                ctv1.setChecked(false);
                ctv2.setChecked(false);
                ctv3.setChecked(false);
                ctv4.setChecked(false);
                mpATC = new ArrayList<String>();
                dispenser_medication = new ArrayList<Medication>();
                meds = new ArrayList<String>();
                returnbutton.setVisibility(View.INVISIBLE);
                next.setText("Weiter");
                Intent newDispScan = new Intent(DispenserScanResult.this, DispenserScan.class);
                startActivity(newDispScan);
                DispenserScanResult.this.finish();
            }
        }

        if (view.getId() == R.id.returnbutton_dispenser)
        {
            counter = 0;
            med0 = false;
            med1 = false;
            med2 = false;
            med3 = false;
            med4 = false;
            ctv0.setChecked(false);
            ctv1.setChecked(false);
            ctv2.setChecked(false);
            ctv3.setChecked(false);
            ctv4.setChecked(false);
            mpATC = new ArrayList<String>();
            dispenser_medication = new ArrayList<Medication>();
            meds = new ArrayList<String>();
            returnbutton.setVisibility(View.INVISIBLE);
            next.setText("Weiter");
            Intent returnToMain = new Intent(DispenserScanResult.this, MainActivity.class);
            startActivity(returnToMain);
        }
    }

}
