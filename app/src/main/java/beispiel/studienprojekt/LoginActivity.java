package beispiel.studienprojekt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import java.util.List;

import static beispiel.studienprojekt.SettingsPreference.setSound;

/**
 * Klasse die beim Start der App aufgerufen wird.
 * Erweitert die Klasse Data, die Daten wie z.B. die Mitarbeiter enthält die für diese Aktivität
 * benötigt werden. Sie implementiert außerdem den View.OnClickListener, der eine zentrale Methode
 * onClick bereitstellt.
 */

public class LoginActivity extends Data implements View.OnClickListener{

    /**
     * Flash ist der Button für den Kamerablitz und flash die dazugehörige boolsche Variable.
     * employee_login ist der benutzte Scanner, der aus der eingefügten Sammlung "ZXing Android
     * Embedded" kommt. Diese unterstütz das Scannen von Barcodes bzw. Codes allgemein.
     * beepManager ist dafür zuständig eine akkustische Rückmeldung beim Scannen zu liefern.
     * Außerdem gibt es noch ein TextView AL, der den alternativen Login (Aktivität
     * LoginAlternativActivity) aufruft und eine boolsche Variable loggedin, die prüft ob bereits
     * ein Login erfolgt ist oder dies der erste Login ist (Bedeutung für den automatischen Logout).
     * Jede Aktivität hat auch seine eigene Toolbar, die in diesem Fall neben dem Titel auch einen
     * Zurück-Pfeil beinhaltet.
     */

    private Button Flash;
    private TextView AL;
    private DecoratedBarcodeView employee_login;
    private BeepManager beepManager;
    private boolean flash;
    public static boolean loggedin = false;
    public static boolean isautologout = false;

    /**
     * Der Prototyp nimmt an, dass Mitarbeiter einen fünfstelligen Code haben.
     * Ist dies also der Fall, wird die Mitarbeiterdatenbank durchlaufen und geprüft, ob der
     * gescannte Code mit einem Eintrag in der Datenbank übereinstimmt. Bei erfolgreicher Anmeldung
     * bekommt der Nutzer eine optische und akkustische Anmeldungsbestätigung.
     * Sonst bekommt der Nutzer eine optische Rückmeldung, dass es sich bei dem gescannten Code
     * nicht um einen Mitarbeiterausweis handelt.
     *
     * @param barcodeResult Ergebnis des Scans
     */

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText().length() != 5 || result.getText().startsWith("D")) {
                employee_login.pause();
                AlertDialog.Builder ad = new AlertDialog.Builder(LoginActivity.this);
                ad.setIcon(R.drawable.medikamentengabe).setTitle("Login")
                        .setMessage("Code gehört nicht zu einem Mitarbeiter").setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                                employee_login.resume();
                                employee_login.decodeSingle(callback);
                            }
                        });
                MakeDialog(ad);
            } else {
                for (Employee employee : employees) {
                    String tmp = employee.getId() + "";
                    Employee e = employee;
                    if (result.getText().equals(tmp)) {
                        employee_login.pause();
                        setCurrentEmployee(e);
                        setSound(beepManager);
                        Intent Erfolg = new Intent(LoginActivity.this, MainActivity.class);
                        Toast.makeText(getApplicationContext(), "Ihre Anmeldung war erfolgreich " +
                                employee.getFirstname() + "!", Toast.LENGTH_SHORT).show();
                        startActivity(Erfolg);
                        if (loggedin) {
                            restart();
                        } else {
                            start();
                        }
                        return;
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
     * Variablen die im Layout erstellten Objekte, z.B. der Codescanner employee_login den Scanner
     * R.id.barcode_scanner.
     * Zudem wird die Dokumentationsdatenbank gelöscht bzw. zurückgesetzt und die automatische
     * Logoutzeit auf 10 gesetzt.
     *
     * @param savedInstanceState Daten der Aktivität (siehe Data)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        this.deleteDatabase("database_documentation");
        documentationHandler.delete();

        AL = (TextView)findViewById(R.id.TextViewAltLogin);
        AL.setOnClickListener(this);
        employee_login = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        employee_login.pause();
        employee_login.decodeSingle(callback);
        employee_login.setStatusText("Mitarbeiterausweis scannen");
        employee_login.resume();
        Flash = (Button)findViewById(R.id.FlashButtonLogin);
        flash = false;
        Flash.setOnClickListener(this);
        beepManager = new BeepManager(this);

        if (isautologout)
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(LoginActivity.this);
            ad.setIcon(R.drawable.logo).setCancelable(false).setTitle("Automatischer Logout")
                    .setMessage("Ihre Sitzung ist abgelaufen. Bitte melden Sie sich erneut an.")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            MakeDialog(ad);
        }
    }

    /**
     * Methode, die das Scannen wieder aufnimmt.
     */

    @Override
    protected void onResume() {
        super.onResume();

        employee_login.resume();
    }

    /**
     * Methode, die das Scannen pausiert.
     */

    @Override
    protected void onPause() {
        super.onPause();

        employee_login.pause();
    }
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return employee_login.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
*/
    /**
     * Hat die selbe Funktion wie onOptionsItemSelected ist aber für das Verhalten beim Drücken des
     * physischen Zurück-Knopfs zuständig.
     * Die App wird beim zurück Klicken beendet.
     *
     */

    public void onBackPressed ()
    {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * Wenn das geklickte Objekt der TextView zum alternativen Login ist wird die dazu gehörige
     * Aktivität aufgerufen (LoginAlternativActivity).
     * Wenn das geklickte Objekt der Kamerablitz-Button aus dem festgelegten Layout
     * (activity_login.xml) ist, wird die boolsche Variable flash überprüft und je nach
     * Status der Blitz (torch) an- bzw. abgeschaltet. Zudem wird ein passendes Bild angezeigt.
     *
     * @param view Das geklickte Objekt, z.B. der Kamerablitz-Button
     */

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.TextViewAltLogin)
        {
            Intent intent = new Intent(LoginActivity.this, LoginAlternativActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.FlashButtonLogin)
        {
            if (flash)
            {
                Flash.setBackgroundResource(R.drawable.blitz_aus);
                employee_login.setTorchOff();
                flash = false;
                return;
            }

            if (!flash)
            {
                Flash.setBackgroundResource(R.drawable.blitz_an);
                employee_login.setTorchOn();
                flash = true;
                return;
            }
        }

    }
}
