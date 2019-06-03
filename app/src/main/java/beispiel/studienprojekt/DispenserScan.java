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

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import static beispiel.studienprojekt.MainActivity.index;
import static beispiel.studienprojekt.SettingsPreference.setSound;

/**
 * Klasse die aufgerufen wird, wenn im Hauptmenü Dispenserbestückung oder Medikamentengabe gedrückt
 * wird. Erweitert die Klasse Data, die Daten wie z.B. die Dispenser enthält die für diese Aktivität
 * benötigt werden. Sie implementiert außerdem den View.OnClickListener, der eine zentrale Methode
 * onClick bereitstellt.
 *
 */

public class DispenserScan extends Data implements View.OnClickListener
{
    /**
     * Flash ist der Button für den Kamerablitz und flash die dazugehörige boolsche Variable.
     * dispenser_scan ist der benutzte Scanner, der aus der eingefügten Sammlung "ZXing Android
     * Embedded" kommt. Diese unterstütz das Scannen von Barcodes bzw. Codes allgemein.
     * beepManager ist dafür zuständig eine akkustische Rückmeldung beim Scannen zu liefern.
     * Jede Aktivität hat auch seine eigene Toolbar, die in diesem Fall neben dem Titel auch einen
     * Zurück-Pfeil beinhaltet.
     */

    private Button Flash;
    private boolean flash;
    private DecoratedBarcodeView dispenser_scan;
    private BeepManager beepManager;
    private Toolbar dispenserscan;

    /**
     * Der Prototyp nimmt an, dass Dispenser einen fünfstelligen Code der mit "D" startet haben.
     * Ist dies also der Fall, wird je nachdem ob index = 1 oder = 2 ist die Medikamentengabe
     * (MedicationScan) oder die Dispenserbestückung (DispenserScanResult) gestartet. In beiden
     * Fällen wird der gescannte Dispenser auf ScannedDispenser gesetzt (Data).
     * Sonst bekommt der Nutzer eine optische Rückmeldung, dass es sich bei dem gescannten Code
     * nicht um einen Dispenser handelt.
     *
     * @param barcodeResult Ergebnis des Scans
     */

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result)
        {
            if (!stockedDispensers.contains(result.getText().toString()) && index == 1
                    && result.getText().startsWith("D"))
            {
                dispenser_scan.pause();
                setSound(beepManager);
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserScan.this);
                ad.setIcon(R.drawable.medikamentengabe).setTitle("Medikamentengabe")
                        .setMessage("Dispenser noch nicht bestückt.")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent backToMain = new Intent(DispenserScan.this, MainActivity.class);
                                startActivity(backToMain);
                                DispenserScan.this.finish();
                                return;
                            }
                        });
                MakeDialog(ad);
            } else if (applicatedDispensers.contains(result.getText().toString()) && index == 1) {
                dispenser_scan.pause();
                setSound(beepManager);
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserScan.this);
                ad.setIcon(R.drawable.medikamentengabe).setTitle("Medikamentengabe")
                        .setMessage("Dispenser bereits vergeben.")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent backToMain = new Intent(DispenserScan.this, MainActivity.class);
                                startActivity(backToMain);
                                DispenserScan.this.finish();
                            }
                        });
                MakeDialog(ad);
            } else if (stockedDispensers.contains(result.getText().toString()) && index == 2) {
                dispenser_scan.pause();
                setSound(beepManager);
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setMessage("Dispenser bereits bestückt.")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent backToMain = new Intent(DispenserScan.this, MainActivity.class);
                                startActivity(backToMain);
                                DispenserScan.this.finish();
                            }
                        });
                MakeDialog(ad);
            } else {
                if (result.getText().length() == 5 && result.getText().startsWith("D")) {
                    dispenser_scan.pause();
                    for (Dispenser dispenser : dispensers) {
                        Dispenser tmp = dispenser;
                        if (result.getText().equals(tmp.getDispenser_Id())) {
                            if (index == 1) {
                                setScannedDispenser(tmp);
                                setSound(beepManager);
                                Intent DispeScan = new Intent(DispenserScan.this, MedicationScan.class);
                                startActivity(DispeScan);
                                DispenserScan.this.finish();
                                return;
                            }
                            if (index == 2) {

                                setScannedDispenser(tmp);
                                setSound(beepManager);
                                Intent DispeScan = new Intent(DispenserScan.this, DispenserScanResult.class);
                                startActivity(DispeScan);
                                DispenserScan.this.finish();
                                return;
                            }
                        }
                    }
                } else {
                    dispenser_scan.pause();
                    if (index == 1) {
                        setSound(beepManager);
                        AlertDialog.Builder ad = new AlertDialog.Builder(DispenserScan.this);
                        ad.setIcon(R.drawable.medikamentengabe)
                                .setTitle("Medikamentengabe").setMessage("Code gehört nicht zu einem Dispenser")
                                .setCancelable(false)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    /**
                                     * Wird der Button des Dialogs geklickt, wird der Dialog beendet und
                                     * erneutes Scannen ist wieder möglich.
                                     *
                                     * @param dialogInterface der Dialog
                                     * @param i
                                     */

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        dispenser_scan.resume();
                                        dispenser_scan.decodeSingle(callback);
                                    }
                                });
                        MakeDialog(ad);

                    }
                    if (index == 2) {
                        setSound(beepManager);
                        AlertDialog.Builder ad = new AlertDialog.Builder(DispenserScan.this);
                        ad.setIcon(R.drawable.dispenserbestueckung)
                                .setTitle("Dispenserbestückung").setMessage("Code gehört nicht zu einem Dispenser")
                                .setCancelable(false)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    /**
                                     * Wird der Button des Dialogs geklickt, wird der Dialog beendet und
                                     * erneutes Scannen ist wieder möglich.
                                     *
                                     * @param dialogInterface der Dialog
                                     * @param i
                                     */

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        dispenser_scan.resume();
                                        dispenser_scan.decodeSingle(callback);
                                    }
                                });
                        MakeDialog(ad);
                    }
                }
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
     * Die Aktivität bekommt ein Layout zugeordnet (setContentView) und die oben deklarierten
     * Variablen die im Layout erstellten Objekte, z.B. der Codescanner dispenser_scan den Scanner
     * R.id.dispenser_scan.
     * Außerdem wird zwischen Medikamentengabe (index 1) und Dispenserbestückung (index 2)
     * unterschieden. Sie unterscheiden sich jedoch nur im Titel der Toolbar.
     *
     * @param savedInstanceState Daten der Aktivität (siehe Data)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispenserscan);

        Log.d("applicateddispenser", applicatedDispensers.size() + "");
        Log.d("stockeddispenser", stockedDispensers.size() + "");

        if (index == 1)
        {
            dispenserscan = (Toolbar) findViewById(R.id.toolbarDispenserScan);
            dispenserscan.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
            dispenserscan.setTitle("Medikamentengabe");
            setSupportActionBar(dispenserscan);
        }
        if (index == 2)
        {
            dispenserscan = (Toolbar) findViewById(R.id.toolbarDispenserScan);
            dispenserscan.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
            dispenserscan.setTitle("Dispenserbestückung");
            setSupportActionBar(dispenserscan);
        }

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dispenser_scan = (DecoratedBarcodeView) findViewById(R.id.dispenser_scanner);
        dispenser_scan.decodeSingle(callback);
        dispenser_scan.setStatusText("Bitte Dispenser scannen");
        dispenser_scan.resume();

        Flash = (Button)findViewById(R.id.FlashButtonDispenserScan);
        flash = false;
        Flash.setOnClickListener(this);

        beepManager = new BeepManager(this);

    }

    /**
     * Wenn das geklickte Objekt der Kamerablitz-Button aus dem festgelegten Layout
     * (activity_dispenserscan.xml) ist, wird die boolsche Variable flash überprüft und je nach
     * Status der Blitz (torch) an- bzw. abgeschaltet. Zudem wird ein passendes Bild angezeigt.
     * Zudem wird die Methode clicked() aufgerufen, die den automatischen Logout zurücksetzt.
     *
     * @param view Das geklickte Objekt, z.B. der Kamerablitz-Button
     */

    @Override
    public void onClick(View view) {
        clicked();
        if (view.getId() == R.id.FlashButtonDispenserScan)
        {
            if (flash)
            {
                Flash.setBackgroundResource(R.drawable.blitz_aus);
                dispenser_scan.setTorchOff();
                flash = false;
                return;
            }

            if (!flash)
            {
                Flash.setBackgroundResource(R.drawable.blitz_an);
                dispenser_scan.setTorchOn();
                flash = true;
                return;
            }
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
            dispenser_scan.resume();
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
        Intent home = new Intent(DispenserScan.this, MainActivity.class);
        startActivity(home);
        DispenserScan.this.finish();
        dispenser_scan.resume();
        clicked();
    }
}
