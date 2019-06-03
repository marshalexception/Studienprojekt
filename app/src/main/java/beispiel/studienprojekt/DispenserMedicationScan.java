package beispiel.studienprojekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import static beispiel.studienprojekt.SettingsPreference.setSound;

/**
 * Klasse die für das Scannen der Medikamente bei der Dispenserbestückung zuständig ist.
 * Erweitert die Klasse DispenserScanResult, die z.B. für das Verwalten und Ändern der Checkboxen
 * ctv0 - ctv4 benötigt wird.
 *
 */

public class DispenserMedicationScan extends DispenserScanResult
{
    /**
     * Flash ist der Button für den Kamerablitz und flash die dazugehörige boolsche Variable.
     * dispensermedication_scan ist der benutzte Scanner, der aus der eingefügten Sammlung
     * "ZXing Android Embedded" kommt. Diese unterstütz das Scannen von Barcodes bzw. Codes
     * allgemein.
     * beepManager ist dafür zuständig eine akkustische Rückmeldung beim Scannen zu liefern.
     * Des weiteren gibt es noch eine Fortschrittsleiste, die sich bei erfolgreichen Scannen von
     * Medikamenten füllt.
     * Jede Aktivität hat auch seine eigene Toolbar, die in diesem Fall neben dem Titel auch einen
     * Zurück-Pfeil beinhaltet.
     * Zuletzt gibt es noch fünf boolsche Variablen med0 - med4, die für das Checken der
     * Checkboxen in der Dispenserbestückung (DispenserScanResult) benötigt werden. Der Prototyp
     * verfügt bei den Checkboxen nicht über eine dynamische Erzeugung, sondern über fünf festgelegte
     * Checkboxen, wir gehen also davon aus, dass der Patient maximal fünf Medikamente erhält.
     */

    private Button Flash;
    private boolean flash;
    private DecoratedBarcodeView dispensermedication_scan;
    private BeepManager beepManager;
    private ProgressBar medicationProgressBar;
    private Toolbar dispensermedicationscan;
    public static boolean med0 = false;
    public static boolean med1 = false;
    public static boolean med2 = false;
    public static boolean med3 = false;
    public static boolean med4 = false;

    private BarcodeCallback callback = new BarcodeCallback() {

        /**
         * Falls das gescannte Ergebnis nicht die Länge 7 hat (ATC-Nummern von Medikamenten haben
         * die Länge 7) bekommt der Nutzer eine Fehlermeldung und den Hinweis, dass es sich bei dem
         * gescannten Code nicht um einen eines Medikamentes handelt.
         * Zudem wird durch if-Abfragen das Scannen von Medikamenten, die nicht zum Dispenser
         * gehören oder das erneute Scannen von bereits gescannten Medikamenten, abgefangen. Auch
         * hier bekommt der Nutzer eine optische Rückmeldung.
         * Sind die ganzen Fälle ausgeschlossen und die jeweilige Checkbox noch nicht gecheckt, wird
         * geprüft ob das Ergebnis des Scans (ATC-Nummer) mit einem Eintrag des String Arrays result,
         * das alle ATC-Nummern für den Dispenser beinhaltet, übereinstimmt. Ist dies der Fall, wird
         * der Zähler um eins erhöht, die jeweilige boolsche Variable auf true gesetzt, z.B. für
         * result[1] - med1 = true. Der Scan wird pausiert und der Nutzer erhält wieder eine
         * optische Rückmeldung.
         *
         * @param barcodeResult Ergebnis des Scans
         */

        @Override
        public void barcodeResult(BarcodeResult barcodeResult)
        {

            if (barcodeResult.getText().length() != 7)
            {
                dispensermedication_scan.pause();
                setSound(beepManager);
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setMessage("Code gehört nicht zu einem (konformen) Medikament." +
                        "\n(siehe Hilfe)")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener()
                        {

                            /**
                             * Wird der Button des Dialogs geklickt, wird der Dialog beendet und
                             * erneutes Scannen ist wieder möglich.
                             *
                             * @param dialogInterface der Dialog
                             * @param i
                             */

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                                dispensermedication_scan.resume();
                                dispensermedication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);

            } else if (medicationresult.size() == 1 && !barcodeResult.getText().equals(result[0]) ) {
                dispensermedication_scan.pause();
                setSound(beepManager);
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setMessage("Medikament gehört nicht zum Dispenser.")
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
                                dispensermedication_scan.resume();
                                dispensermedication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);

            }else if (medicationresult.size() == 2 && !barcodeResult.getText().equals(result[0]) &&
                    !barcodeResult.getText().equals(result[1]) ) {
                dispensermedication_scan.pause();
                setSound(beepManager);
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setMessage("Medikament gehört nicht zum Dispenser.")
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
                                dispensermedication_scan.resume();
                                dispensermedication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);

            } else if (medicationresult.size() == 3 && !barcodeResult.getText().equals(result[0]) &&
                    !barcodeResult.getText().equals(result[1]) && !barcodeResult.getText().equals(result[2]))
            {
                dispensermedication_scan.pause();
                setSound(beepManager);AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
               ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                       .setMessage("Medikament gehört nicht zum Dispenser.")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener()
                        {

                            /**
                             * Wird der Button des Dialogs geklickt, wird der Dialog beendet und
                             * erneutes Scannen ist wieder möglich.
                             *
                             * @param dialogInterface der Dialog
                             * @param i
                             */

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                                dispensermedication_scan.resume();
                                dispensermedication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);

            } else if (medicationresult.size() == 4 && !barcodeResult.getText().equals(result[0]) &&
                    !barcodeResult.getText().equals(result[1]) && !barcodeResult.getText().equals(result[2]) &&
                    !barcodeResult.getText().equals(result[3]))
            {
                dispensermedication_scan.pause();
                setSound(beepManager);
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setMessage("Medikament gehört nicht zum Dispenser.")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener()
                        {

                            /**
                             * Wird der Button des Dialogs geklickt, wird der Dialog beendet und
                             * erneutes Scannen ist wieder möglich.
                             *
                             * @param dialogInterface der Dialog
                             * @param i
                             */

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                                dispensermedication_scan.resume();
                                dispensermedication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);

            }

            if (ctv0.isChecked() && barcodeResult.getText().equals(result[0]))
            {
                setSound(beepManager);
                dispensermedication_scan.pause();
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setCancelable(false)
                        .setMessage(dispenser_medication.get(0).getName() + " bereits gescannt.")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                            /**
                             * Wird der Buttoon des Dialogs geklickt, wird der Dialog beendet,
                             * der Scan wieder aufgenommen und die Aktivität DispenserScanResult
                             * gestartet.
                             *
                             * @param dialogInterface der Dialog
                             * @param i
                             */

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                dispensermedication_scan.resume();
                                dispensermedication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);
            }
            if (ctv1.isChecked() && barcodeResult.getText().equals(result[1]))
            {
                setSound(beepManager);
                dispensermedication_scan.pause();
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setCancelable(false)
                        .setMessage(dispenser_medication.get(1).getName() + " bereits gescannt.")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                            /**
                             * Wird der Buttoon des Dialogs geklickt, wird der Dialog beendet,
                             * der Scan wieder aufgenommen und die Aktivität DispenserScanResult
                             * gestartet.
                             *
                             * @param dialogInterface der Dialog
                             * @param i
                             */

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                dispensermedication_scan.resume();
                                dispensermedication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);
            }
            if (ctv2.isChecked() && barcodeResult.getText().equals(result[2]))
            {
                setSound(beepManager);
                int width = WindowManager.LayoutParams.WRAP_CONTENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dispensermedication_scan.pause();
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setCancelable(false)
                        .setMessage(dispenser_medication.get(2).getName() + " bereits gescannt.")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                            /**
                             * Wird der Buttoon des Dialogs geklickt, wird der Dialog beendet,
                             * der Scan wieder aufgenommen und die Aktivität DispenserScanResult
                             * gestartet.
                             *
                             * @param dialogInterface der Dialog
                             * @param i
                             */

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                dispensermedication_scan.resume();
                                dispensermedication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);
            }
            if (ctv3.isChecked() && barcodeResult.getText().equals(result[3]))
            {
                setSound(beepManager);
                dispensermedication_scan.pause();
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setCancelable(false)
                        .setMessage(dispenser_medication.get(3).getName() + " bereits gescannt.")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                            /**
                             * Wird der Buttoon des Dialogs geklickt, wird der Dialog beendet,
                             * der Scan wieder aufgenommen und die Aktivität DispenserScanResult
                             * gestartet.
                             *
                             * @param dialogInterface der Dialog
                             * @param i
                             */

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                dispensermedication_scan.resume();
                                dispensermedication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);
            }
            if (ctv4.isChecked() && barcodeResult.getText().equals(result[4]))
            {
                setSound(beepManager);
                dispensermedication_scan.pause();
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setCancelable(false)
                        .setMessage(dispenser_medication.get(4).getName() + " bereits gescannt.")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                            /**
                             * Wird der Buttoon des Dialogs geklickt, wird der Dialog beendet,
                             * der Scan wieder aufgenommen und die Aktivität DispenserScanResult
                             * gestartet.
                             *
                             * @param dialogInterface der Dialog
                             * @param i
                             */

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                dispensermedication_scan.resume();
                                dispensermedication_scan.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);
            }

            if (!ctv0.isChecked() && barcodeResult.getText().equals(result[0]))
            {
                setSound(beepManager);
                dispensermedication_scan.pause();
                med0 = true;
                AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                        .setCancelable(false)
                        .setMessage(ctv0.getText() + " gescannt")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                            /**
                             * Wird der Buttoon des Dialogs geklickt, wird der Dialog beendet,
                             * der Scan wieder aufgenommen und die Aktivität DispenserScanResult
                             * gestartet.
                             *
                             * @param dialogInterface der Dialog
                             * @param i
                             */

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                counter++;
                                dialogInterface.cancel();
                                dispensermedication_scan.resume();
                                Intent menu = new Intent(DispenserMedicationScan.this, DispenserScanResult.class);
                                startActivity(menu);
                            }
                        });
                MakeDialog(ad);
            }
            if (!ctv1.isChecked() && result.length > 1)
            {
                if (barcodeResult.getText().equals(result[1]))
                {
                    setSound(beepManager);
                    dispensermedication_scan.pause();
                    med1 = true;
                    AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                    ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                            .setCancelable(false)
                            .setMessage(ctv1.getText() + " gescannt")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                                /**
                                 * Wird der Buttoon des Dialogs geklickt, wird der Dialog beendet,
                                 * der Scan wieder aufgenommen und die Aktivität DispenserScanResult
                                 * gestartet.
                                 *
                                 * @param dialogInterface der Dialog
                                 * @param i
                                 */

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    counter++;
                                    dialogInterface.cancel();
                                    dispensermedication_scan.resume();
                                    Intent menu = new Intent(DispenserMedicationScan.this, DispenserScanResult.class);
                                    startActivity(menu);
                                }
                            });
                    MakeDialog(ad);
                }
            }
            if (!ctv2.isChecked() && result.length > 2)
            {
                if (barcodeResult.getText().equals(result[2]))
                {
                    setSound(beepManager);
                    dispensermedication_scan.pause();
                    med2 = true;
                    AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                    ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                            .setCancelable(false)
                            .setMessage(ctv2.getText() + " gescannt")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                                /**
                                 * Wird der Buttoon des Dialogs geklickt, wird der Dialog beendet,
                                 * der Scan wieder aufgenommen und die Aktivität DispenserScanResult
                                 * gestartet.
                                 *
                                 * @param dialogInterface der Dialog
                                 * @param i
                                 */

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    counter++;
                                    dialogInterface.cancel();
                                    dispensermedication_scan.resume();
                                    Intent menu = new Intent(DispenserMedicationScan.this, DispenserScanResult.class);
                                    startActivity(menu);
                                }
                            });
                    MakeDialog(ad);
                }
            }
            if (!ctv3.isChecked() && result.length > 3)
            {
                if (barcodeResult.getText().equals(result[3]))
                {
                    setSound(beepManager);
                    dispensermedication_scan.pause();
                    med3 = true;
                    AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                    ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                            .setCancelable(false)
                            .setMessage(ctv3.getText() + " gescannt")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                                /**
                                 * Wird der Buttoon des Dialogs geklickt, wird der Dialog beendet,
                                 * der Scan wieder aufgenommen und die Aktivität DispenserScanResult
                                 * gestartet.
                                 *
                                 * @param dialogInterface der Dialog
                                 * @param i
                                 */

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    counter++;
                                    dialogInterface.cancel();
                                    dispensermedication_scan.resume();
                                    Intent menu = new Intent(DispenserMedicationScan.this, DispenserScanResult.class);
                                    startActivity(menu);
                                }
                            });
                    MakeDialog(ad);
                }
            }
            if (!ctv4.isChecked() && result.length > 4)
            {
                if (barcodeResult.getText().equals(result[4]))
                {
                    setSound(beepManager);
                    dispensermedication_scan.pause();
                    med4 = true;
                    AlertDialog.Builder ad = new AlertDialog.Builder(DispenserMedicationScan.this);
                    ad.setIcon(R.drawable.dispenserbestueckung).setTitle("Dispenserbestückung")
                            .setCancelable(false)
                            .setMessage(ctv4.getText() + " gescannt")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                                /**
                                 * Wird der Buttoon des Dialogs geklickt, wird der Dialog beendet,
                                 * der Scan wieder aufgenommen und die Aktivität DispenserScanResult
                                 * gestartet.
                                 *
                                 * @param dialogInterface der Dialog
                                 * @param i
                                 */

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    counter++;
                                    dialogInterface.cancel();
                                    dispensermedication_scan.resume();
                                    Intent menu = new Intent(DispenserMedicationScan.this, DispenserScanResult.class);
                                    startActivity(menu);
                                }
                            });
                    MakeDialog(ad);
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
     * Nimmt das Scannen wieder auf.
     */

    @Override
    protected void onResume() {
        super.onResume();

        dispensermedication_scan.resume();
    }

    /**
     * Pausiert das Scannen der Codes.
     */

    @Override
    protected void onPause() {
        super.onPause();

        dispensermedication_scan.pause();
    }

    /**
     * Wenn das geklickte Objekt der Kamerablitz-Button aus dem festgelegten Layout
     * (activity_dispensermedication.xml) ist, wird die boolsche Variable flash überprüft und je
     * nach Status der Blitz (torch) an- bzw. abgeschaltet. Zudem wird ein passendes Bild angezeigt.
     * Außerdem wird die Methode clicked() aufgerufen, die den automatischen Logout zurücksetzt.
     *
     * @param view Das geklickte Objekt, z.B. der Kamerablitz-Button
     */

    public void onClick (View view)
    {
        clicked();
        if (view.getId() == R.id.FlashButtonDispenserMedication)
        {
            if (flash)
            {
                Flash.setBackgroundResource(R.drawable.blitz_aus);
                dispensermedication_scan.setTorchOff();
                flash = false;
                return;
            }

            if (!flash)
            {
                Flash.setBackgroundResource(R.drawable.blitz_an);
                dispensermedication_scan.setTorchOn();
                flash = true;
                return;
            }
        }
    }

    /**
     * Die Aktivität bekommt ein Layout zugeordnet (setContentView) und die oben deklarierten
     * Variablen die im Layout erstellten Objekte, z.B. der Codescanner dispensermedication_scan
     * den Scanner R.id.dispenser_medication_scanner.
     * Die Fortschrittsleiste wird je nach Stand des Zählers (counter) gefüllt, also z.B. wenn eins
     * von drei Medikamenten gescannt ist zu 33%.
     *
     * @param savedInstanceState Daten der Aktivität (siehe Data)
     */

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispensermedication);

        medicationProgressBar = (ProgressBar) findViewById(R.id.progressbar_dispensermedication);
        medicationProgressBar.setProgress(0);
        double tmp = ((double)counter/(double)medicationresult.size())*100;
        medicationProgressBar.setProgress((int)tmp);

        dispensermedicationscan = (Toolbar) findViewById(R.id.toolbarDispenserMedication);
        dispensermedicationscan.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
        dispensermedicationscan.setTitle("Dispenserbestückung");
        setSupportActionBar(dispensermedicationscan);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        dispensermedication_scan = (DecoratedBarcodeView) findViewById(R.id.dispenser_medication_scanner);
        //dispensermedication_scan.decodeContinuous(callback);
        dispensermedication_scan.decodeSingle(callback);
        dispensermedication_scan.setStatusText("Medikament scannen");
        dispensermedication_scan.resume();

        Flash = (Button) findViewById(R.id.FlashButtonDispenserMedication);
        flash = false;
        Flash.setOnClickListener(this);

        beepManager = new BeepManager(this);

        Log.d("counter dispensermedic", counter + "");
    }

    /**
     * Wenn der Zurück-Pfeil in der Toolbar gedrückt wird, wird die Aktivität beendet und man
     * kommt in die Aktivität zuvor (DispenserScanResult) zurück.
     *
     * @param item
     * @return boolean
     */

    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        if (item.getItemId() == R.id.back_to_home)
        {
            DispenserMedicationScan.this.finish();
        }

        return true;
    }

    /**
     * Hat die selbe Funktion wie onOptionsItemSelected ist aber für das Verhalten beim Drücken des
     * physischen Zurück-Knopfs zuständig.
     *
     */

    @Override
    public void onBackPressed ()
    {
        DispenserMedicationScan.this.finish();
    }

}
