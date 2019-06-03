package beispiel.studienprojekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import static beispiel.studienprojekt.SettingsPreference.setSound;
import static beispiel.studienprojekt.SettingsPreference.sound_setting;

/**
 * Klasse die aufgerufen wird, wenn im Hauptmenü Einzelscan gedrückt wird.
 * Erweitert die Klasse Data die Daten wie z.B. die Patienten enthält die für diese Aktivität
 * benötigt werden.
 */

public class SimpleScan extends Data
{
    /**
     * Flash ist der Button für den Kamerablitz und flash die dazugehörige boolsche Variable.
     * simple_scan ist der benutzte Scanner, der aus der eingefügten Sammlung "ZXing Android
     * Embedded" kommt. Diese unterstütz das Scannen von Barcodes bzw. Codes allgemein.
     * beepManager ist dafür zuständig eine akkustische Rückmeldung beim Scannen zu liefern.
     * Jede Aktivität hat auch seine eigene Toolbar, die in diesem Fall neben dem Titel auch einen
     * Zurück-Pfeil beinhaltet. Außerdem hat die Aktivität eine Integer-Variable type, die zwischen
     * den verschiedenen möglichen Scanobjekten (Medikament, Patient, Mitarbeiter, ...) unterscheidet.
     */

    private Button Flash;
    private DecoratedBarcodeView simple_scan;
    private BeepManager beepManager;
    private boolean flash;
    public static int type = 0;
    private Toolbar simple_scan_toolbar;

    /**
     * Die Aktivität bekommt ein Layout zugeordnet (setContentView) und die oben deklarierten
     * Variablen die im Layout erstellten Objekte, z.B. der Codescanner simple_scan den Scanner
     * R.id.simplescan_scanner.
     *
     * @param savedInstanceState Daten der Aktivität
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplescan);

        simple_scan_toolbar = (Toolbar) findViewById(R.id.toolbarSimpleScan);
        simple_scan_toolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
        simple_scan_toolbar.setTitle("Einzelscan");
        setSupportActionBar(simple_scan_toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        simple_scan = (DecoratedBarcodeView) findViewById(R.id.simplescan_scanner);
        simple_scan.decodeSingle(callback);
        simple_scan.setStatusText("");

        Flash = (Button)findViewById(R.id.FlashButtonSimpleScan);
        flash = false;
        Flash.setOnClickListener(new View.OnClickListener() {

            /**
             * Die boolsche Variable flash wird überprüft und je nach Status der Blitz (torch) an-
             * bzw. abgeschaltet. Zudem wird ein passendes Bild angezeigt. Außerdem wird die Methode
             * clicked() aufgerufen, die den automatischen Logout zurücksetzt.
             *
             * @param view FlashButtonSimpleScan
             */

            @Override
            public void onClick(View view) {
                clicked();
                if (flash)
                {
                    Flash.setBackgroundResource(R.drawable.blitz_aus);
                    simple_scan.setTorchOff();
                    flash = false;
                    return;
                }
                if (!flash)
                {
                    Flash.setBackgroundResource(R.drawable.blitz_an);
                    simple_scan.setTorchOn();
                    flash = true;
                    return;
                }

            }
        });

        beepManager = new BeepManager(this);
        simple_scan.resume();
    }

    /**
     * Der Prototyp nimmt an dass die einzelnen Codes folgendermaßen aufgebaut sind:
     * - Mitarbeiter: 5-stelliger Code
     * - Medikamente: 7-stelliger Code
     * - Patient: 6-stelliger Code
     * - Dispenser: 5-stelliger Code der mit "D" beginnt
     * Nach diesen Eigenschaften wird der gescannte Code überprüft und je nachdem was zutrifft, die
     * Variable type geändert, dem gescannten Objekt eine Variable zugeordnet und die Aktivität
     * SimpleScanResult gestartet. Sollte keines der Eigenschaften auf den gescannten Code zutreffen,
     * bekommt der Nutzer eine Rückmeldung, dass die App diesen nicht erkannt hat.
     */

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            if (result.getText().length() == 5 && !result.getText().startsWith("D"))
            {
                for (Employee employee : employees)
                {
                    Employee tmp = employee;
                    if (result.getText().equals(tmp.getId() + ""))
                    {
                        type = 1;
                        setScannedEmployee(tmp);
                        setSound(beepManager);
                        simple_scan.pause();
                        Intent ScanResult = new Intent(SimpleScan.this, SimpleScanResult.class);
                        startActivity(ScanResult);
                        return;
                    }
                }
            } else if (result.getText().length() == 7) {
                for (Medication medication : medications) {
                    Medication tmp = medication;
                    if (result.getText().equals(tmp.getAtc() + "")) {
                        type = 2;
                        setScannedMedication(tmp);
                        setSound(beepManager);
                        simple_scan.pause();
                        Intent ScanResult = new Intent(SimpleScan.this, SimpleScanResult.class);
                        startActivity(ScanResult);
                        return;
                    }
                }
            } else if (result.getText().length() == 6) {
                for (Patient patient : patients) {
                    Patient tmp = patient;
                    if (result.getText().equals(tmp.getId() + "")) {
                        type = 3;
                        setScannedPatient(tmp);
                        setSound(beepManager);
                        simple_scan.pause();
                        Intent ScanResult = new Intent(SimpleScan.this, SimpleScanResult.class);
                        startActivity(ScanResult);
                        return;
                    }
                }
            } else if (result.getText().startsWith("D")) {
                for (Dispenser dispenser : dispensers) {
                    Dispenser tmp = dispenser;
                    if (result.getText().equals(tmp.getDispenser_Id())) {
                        type = 4;
                        setScannedDispenser(tmp);
                        setSound(beepManager);
                        simple_scan.pause();
                        Intent ScanResult = new Intent(SimpleScan.this, SimpleScanResult.class);
                        startActivity(ScanResult);
                        return;
                    }
                }
            } else
            {
                simple_scan.pause();
                AlertDialog.Builder ad = new AlertDialog.Builder(SimpleScan.this);
                ad.setIcon(R.drawable.einzelscan).setTitle("Einzelscan")
                        .setMessage("Code nicht erkannt.")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                simple_scan.resume();
                                simple_scan.decodeSingle(callback);
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
        Intent home = new Intent(SimpleScan.this, MainActivity.class);
        startActivity(home);
        clicked();
        SimpleScan.this.finish();
    }


}
