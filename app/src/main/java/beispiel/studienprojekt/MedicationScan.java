package beispiel.studienprojekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static beispiel.studienprojekt.DispenserScanResult.result;
import static beispiel.studienprojekt.Documentation.notEmpty;
import static beispiel.studienprojekt.SettingsPreference.setSound;
import static beispiel.studienprojekt.SettingsPreference.setTextSize;
import static beispiel.studienprojekt.SettingsPreference.textsizeindex;

/**
 * Klasse die aufgerufen wird, wenn bei der Medikamentengabe der Dispenser gescannt wurde. Erweitert
 * die Klasse Data, die Daten wie z.B. die Medikamente enthält die für diese Aktivität benötigt
 * werden. Sie implementiert außerdem den View.OnClickListener, der eine zentrale Methode onClick
 * bereitstellt.
 *
 */

public class MedicationScan extends Data implements View.OnClickListener
{
    /**
     * Flash ist der Button für den Kamerablitz und flash die dazugehörige boolsche Variable.
     * Jede Aktivität hat auch seine eigene Toolbar, die in diesem Fall neben dem Titel auch einen
     * Zurück-Pfeil beinhaltet.
     * Der TextView dispenser_display zeigt den Patienten an der zu dem bereits gescannten Dispenser
     * gehört.
     * Die drei ArrayLists werden
     * medicationscan ist der benutzte Scanner, der aus der eingefügten Sammlung "ZXing Android
     * Embedded" kommt. Diese unterstützt das Scannen von Barcodes bzw. Codes allgemein.
     * beepManager ist dafür zuständig eine akkustische Rückmeldung beim Scannen zu liefern.
     *
     */

    private Button Flash;
    private boolean flash; //scan;
    private Toolbar medication_toolbar;
    public static TextView dispenser_display;
    private ArrayList<String> mpATC = new ArrayList<>();
    private ArrayList<String> meds = new ArrayList<>();
    private ArrayList<Medication> medicationscan = new ArrayList<>();
    //private static int counter = 0;

    private DecoratedBarcodeView medication_scan;
    private BeepManager beepManager;

    /**
     * Der Prototyp nimmt an, dass Patienten einen sechsstelligen Code haben.
     * Demnach wird nach drei Fällen unterschieden:
     * - richtiger Patient
     * - falscher Patient
     * - kein Patient
     * Im ersten Fall (richtiger Patient) bekommt der Nutzer eine optische Rückmeldung, dass der
     * Dispenser zum richtigen Patienten gehört. Wird dann auch bestätigt, dass der Patient die
     * Medikation erhalten hat, erstellt die App ein neues Dokument, dass dies festhält. Danach hat
     * der Nutzer die Möglichkeit zwischen einer neuen Medikamentengabe oder dem Zurück ins
     * Hauptmenü auszuwählen.
     * In den anderen beiden Fällen bekommt der Nutzer ebenfalls eine optische Rückmeldung mit den
     * selben Möglichkeiten wie in Fall 1, nur die Dokumentation erfolgt nicht.
     *
     * @param barcodeResult Ergebnis des Scans
     */

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult barcodeResult) {

            if (barcodeResult.getText().equals(scannedPatient.getId()+""))
            {
                medication_scan.pause();
                setSound(beepManager);
                AlertDialog.Builder ad = new AlertDialog.Builder(MedicationScan.this);
                ad.setIcon(R.drawable.medikamentengabe)
                        .setTitle("Medikamentengabe").setMessage("Dispenser gehört zum richtigen Patienten")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                AlertDialog.Builder ad_one = new AlertDialog.Builder(MedicationScan.this);
                               ad_one.setIcon(R.drawable.medikamentengabe)
                                        .setTitle("Medikamentengabe").setMessage("Patient hat Medikation erhalten")
                                        .setCancelable(false)
                                        .setNeutralButton("Ja", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();

                                                Documentation documentation = new Documentation();
                                                documentation.setEmployee(currentEmployee.getId());
                                                documentation.setPatient(scannedPatient.getId());
                                                documentation.setAtc(result);
                                                String date = new SimpleDateFormat("dd-MM-yyyy', 'HH:mm").format(new Date());
                                                documentation.setDate(date);
                                                documentation.setType("Medikamentengabe");
                                                documentationHandler.addDocumentation(documentation);

                                                applicatedDispensers.add(scannedDispenser.getDispenser_Id());

                                                AlertDialog.Builder ad_two = new AlertDialog.Builder(MedicationScan.this);
                                                ad_two.setIcon(R.drawable.medikamentengabe)
                                                        .setTitle("Medikamentengabe").setMessage("Medikamentengabe abgeschlossen")
                                                        .setCancelable(false)
                                                        .setNegativeButton("Medikamentengabe\nbeenden", new DialogInterface.OnClickListener()
                                                                {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i)
                                                                    {
                                                                        Intent backToMain = new Intent(MedicationScan.this, MainActivity.class);
                                                                        startActivity(backToMain);
                                                                        MedicationScan.this.finish();
                                                                    }
                                                                })
                                                        .setNeutralButton("Neue \nMedikamentengabe", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                Intent newMedScan = new Intent (MedicationScan.this, DispenserScan.class);
                                                                startActivity(newMedScan);
                                                                MedicationScan.this.finish();
                                                            }
                                                        });
                                                MakeDialog(ad_two);
                                            }
                                        })
                                        /*
                                        .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                Intent backToMain = new Intent(MedicationScan.this, MainActivity.class);
                                                startActivity(backToMain);
                                                MedicationScan.this.finish();
                                            }
                                        })*/;
                                MakeDialog(ad_one);
                            }
                        });
                MakeDialog(ad);
                //write_document_medication(documentation, MedicationScan.this);

            } else if (barcodeResult.getText().length() == 6 && !barcodeResult.getText().equals(scannedPatient.getId()+"")){
                medication_scan.pause();
                setSound(beepManager);
                AlertDialog.Builder ad = new AlertDialog.Builder(MedicationScan.this);
                ad.setIcon(R.drawable.medikamentengabe)
                        .setTitle("Medikamentengabe").setMessage("Falscher Patient")
                        .setCancelable(false)
                        .setNegativeButton("Medikamentengabe\nabbrechen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent backToMain = new Intent(MedicationScan.this, MainActivity.class);
                                startActivity(backToMain);
                                MedicationScan.this.finish();
                            }
                        })
                        .setNeutralButton("Erneut scannen", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                                medication_scan.resume();
                                medication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);
            } else if (barcodeResult.getText().length() != 6 ){
                medication_scan.pause();
                setSound(beepManager);
                AlertDialog.Builder ad = new AlertDialog.Builder(MedicationScan.this);
                ad.setIcon(R.drawable.medikamentengabe)
                        .setTitle("Medikamentengabe").setMessage("Code gehört nicht zu einem Patienten")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                                medication_scan.resume();
                                medication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);

            }
        }

        /**
         * BarcodeCallback muss die Methode seiner Oberklasse überschreiben, sie wird aber
         * nicht benutzt.
         *
         * @param resultPoints
         */

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    /**
     * Methode, die das Scannen wieder aufnimmt.
     */

    @Override
    protected void onResume() {
        super.onResume();

        medication_scan.resume();
    }

    /**
     * Methode, die das Scannen pausiert.
     */

    @Override
    protected void onPause() {
        super.onPause();

        medication_scan.pause();
    }
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return medication_scan.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
*/

    /**
     * Die Aktivität bekommt ein Layout zugeordnet (setContentView) und die oben deklarierten
     * Variablen die im Layout erstellten Objekte, z.B. der Codescanner medication_scan den Scanner
     * R.id.barcode_scanner.
     * Durch einen try-catch Block wird der Fall abgefangen, dass ein Dispenser verarbreicht wird,
     * der jedoch noch gar nicht bestückt wurde.
     * Im unteren Bereich der Methode wird durch zwei for-Schleifen der zum bereits gescannten
     * Dispenser passende Patient ermittelt und im TextView dispenser_display angezeigt.
     */
/*
    public void loadActivity ()
    {
        setContentView(R.layout.activity_medication);

        SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
        int textsize = Integer.parseInt(sharedprefs.getString("pref_text_size", "2"));
        textsizeindex = textsize;

        medication_scan = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        medication_scan.decodeSingle(callback);
        medication_scan.setStatusText("Patient scannen");
        medication_scan.resume();

        try {
            medication_scan.pause();
            int x = result.length;
        } catch (NullPointerException e)
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(MedicationScan.this);
            ad.setIcon(R.drawable.medikamentengabe).setTitle("Medikamentengabe")
                    .setMessage("Dispenser noch nicht bestückt.")
                    .setCancelable(false)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent backToMain = new Intent(MedicationScan.this, MainActivity.class);
                            startActivity(backToMain);
                            MedicationScan.this.finish();
                        }
                    });
            MakeDialog(ad);
        }
        medication_scan.resume();

        Flash = (Button)findViewById(R.id.FlashButtonMedicationScan);
        flash = false;
        Flash.setOnClickListener(this);

        beepManager = new BeepManager(this);

        medication_toolbar = (Toolbar) findViewById(R.id.toolbarMedicationScan);
        medication_toolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
        medication_toolbar.setTitle("Medikamentengabe");
        setSupportActionBar(medication_toolbar);

        dispenser_display = (TextView) findViewById(R.id.dispenserpatient);
        setTextSize(dispenser_display);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String [] x = scannedDispenser.getDispenser_Medications();

        for (MedicationPatient mp : mpList)
        {
            for (int i = 0; i < x.length; i++) {
                if (mp.getMedicationPatientId() == Integer.parseInt(x[i])) {
                    mpATC.add(mp.getAtc());
                    setScannedMedicationPatient(mp);
                    Medication tmp = getMedication(mp.getAtc());
                    medicationscan.add(tmp);                        //Medication Array
                    meds.add(tmp.getAtc());
                }
            }
        }

        for (Patient patient: patients)
        {
            Patient tmp = patient;
            if (tmp.getId() == scannedMedicationPatient.getId())
            {
                setScannedPatient(tmp);
            }
        }

        dispenser_display.setText(scannedPatient.toString());
    }
*/
    /**
     * Die Methode die beim Start der Aktivität aufgerufen wird. Bei MedicationScan ruft sie selbst
     * nur die Methode loadActivity() auf.
     *
     * @param savedInstanceState Daten der Aktivität (siehe Data)
     */

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadActivity();

        setContentView(R.layout.activity_medication);

        SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
        int textsize = Integer.parseInt(sharedprefs.getString("pref_text_size", "2"));
        textsizeindex = textsize;

        medication_scan = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        medication_scan.decodeSingle(callback);
        medication_scan.setStatusText("Patient scannen");
        medication_scan.resume();

        /*
        try {
            medication_scan.pause();
            int x = result.length;
        } catch (NullPointerException e)
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(MedicationScan.this);
            ad.setIcon(R.drawable.medikamentengabe).setTitle("Medikamentengabe")
                    .setMessage("Dispenser noch nicht bestückt.")
                    .setCancelable(false)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent backToMain = new Intent(MedicationScan.this, MainActivity.class);
                            startActivity(backToMain);
                            MedicationScan.this.finish();
                        }
                    });
            MakeDialog(ad);
        }
        medication_scan.resume();
        */

        Flash = (Button)findViewById(R.id.FlashButtonMedicationScan);
        flash = false;
        Flash.setOnClickListener(this);

        beepManager = new BeepManager(this);

        medication_toolbar = (Toolbar) findViewById(R.id.toolbarMedicationScan);
        medication_toolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
        medication_toolbar.setTitle("Medikamentengabe");
        setSupportActionBar(medication_toolbar);

        dispenser_display = (TextView) findViewById(R.id.dispenserpatient);
        setTextSize(dispenser_display);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String [] x = scannedDispenser.getDispenser_Medications();

        for (MedicationPatient mp : mpList)
        {
            for (int i = 0; i < x.length; i++) {
                if (mp.getMedicationPatientId() == Integer.parseInt(x[i])) {
                    mpATC.add(mp.getAtc());
                    setScannedMedicationPatient(mp);
                    Medication tmp = getMedication(mp.getAtc());
                    medicationscan.add(tmp);                        //Medication Array
                    meds.add(tmp.getAtc());
                }
            }
        }

        for (Patient patient: patients)
        {
            Patient tmp = patient;
            if (tmp.getId() == scannedMedicationPatient.getId())
            {
                setScannedPatient(tmp);
            }
        }

        dispenser_display.setText(scannedPatient.toString());

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
        Intent home = new Intent(MedicationScan.this, MainActivity.class);
        startActivity(home);
        MedicationScan.this.finish();
        clicked();
    }

    /**
     * Wenn das geklickte Objekt der Kamerablitz-Button aus dem festgelegten Layout
     * (activity_medication.xml) ist, wird die boolsche Variable flash überprüft und je nach
     * Status der Blitz (torch) an- bzw. abgeschaltet. Zudem wird ein passendes Bild angezeigt.
     * Zudem wird die Methode clicked() aufgerufen, die den automatischen Logout zurücksetzt.
     *
     * @param view Das geklickte Objekt, z.B. der Kamerablitz-Button
     */

    @Override
    public void onClick(View view) {
        clicked();
        if (view.getId() == R.id.FlashButtonMedicationScan)
        {
            if (flash)
            {
                Flash.setBackgroundResource(R.drawable.blitz_aus);
                medication_scan.setTorchOff();
                flash = false;
                return;
            }

            if (!flash)
            {
                Flash.setBackgroundResource(R.drawable.blitz_an);
                medication_scan.setTorchOn();
                flash = true;
                return;
            }
        }
    }
}
