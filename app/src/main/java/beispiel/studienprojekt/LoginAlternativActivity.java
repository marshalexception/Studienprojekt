package beispiel.studienprojekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Klasse die beim Klicken auf den TextView für den alternativen Login aufgerufen wird.
 * Erweitert die Klasse LoginActivity.
 */

public class LoginAlternativActivity extends LoginActivity
{

    /**
     * Es gibt zwei Eingabefelder für den Benutzernamen (BN) und das Passwort (PW) und ein Button
     * Login mit dem die Anmeldedaten überprüft werden.
     * Jede Aktivität hat auch seine eigene Toolbar, die in diesem Fall neben dem Titel auch einen
     * Zurück-Pfeil beinhaltet.
     */

    private Toolbar AltLogToolbar;
    private EditText BN, PW;
    private Button Login;
    private TextView ueber;

    /**
     * Die Aktivität bekommt ein Layout zugeordnet (setContentView) und die oben deklarierten
     * Variablen die im Layout erstellten Objekte, z.B. die Toolbar R.id.toolbaraltLogin.
     * Beim Klick auf den Login-Button wird die Mitarbeiterdatenbank durchlaufen und geprüft, ob
     * Benutzernamen und Passwort eine gültige Kombination bilden. Ist dies der Fall bekommt der
     * Nutzer eine optische Anmeldungsbestätigung.
     * Sonst bekommt der Nutzer eine optische Rückmeldung das die Anmeldung nicht erfolgreich war.
     *
     * @param savedInstanceState Daten der Aktivität (siehe Data)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_alternativ);

        AltLogToolbar = (Toolbar) findViewById(R.id.toolbaraltlogin);
        AltLogToolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
        AltLogToolbar.setTitle("Login");
        setSupportActionBar(AltLogToolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        BN = (EditText) findViewById(R.id.BenutzerTextView);
        PW = (EditText) findViewById(R.id.PasswortTextView);
        Login = (Button)findViewById(R.id.ButtonLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                for (Employee employee : employees)
                {
                    String tmp1 = employee.getFirstname();
                    String tmp2 = employee.getPassword();
                    Employee result = employee;
                    if ((BN.getText().toString().equals(tmp1)) && (PW.getText().toString().equals(tmp2)))
                    {
                        setCurrentEmployee(result);
                        if (loggedin) {
                            restart();
                        } else {
                            start();
                        }
                        Intent intent = new Intent(LoginAlternativActivity.this, MainActivity.class);
                        Toast.makeText(getApplicationContext(), "Herzlich Willkommen " +
                                BN.getText().toString() + "!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(),
                        "Ihre Anmeldung war leider nicht erfolgreich. Bitte prüfen Sie die " +
                                "Benutzerkennung und das Passwort", Toast.LENGTH_SHORT).show();
            }
        });

        ueber = (TextView) findViewById(R.id.TextViewImpressum);
        ueber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(LoginAlternativActivity.this);
                ad.setIcon(R.drawable.logo).setCancelable(true).setTitle("Über MediPocket")
                        .setMessage("MediPocket ist im Rahmen des Studienprojekts im Wintersemster " +
                                "2016/2017 an der Universität Trier entstanden.\n" +
                                "Betreuer: Prof. Andreas Goldschmidt, M. Sc. Peter Junk\n" +
                                "Auftraggeber: CompuGroup Medical (CGM)\n" +
                                "Studenten: Franziska Graf,\n" +
                                "Matthias Merkler,\n" +
                                "Manfred Rieder,\n" +
                                "Simon Trumm,\n" +
                                "Stefan Weinand\n" +
                                "\n" +
                                "Icons im Hauptmenü und das Zurück-Kreuz in der Dispenserbestückung " +
                                "von https://de.icons8.com")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                MakeDialog(ad);
            }
        });
    }

    /**
     * Wenn der Zurück-Pfeil in der Toolbar gedrückt wird, wird die Aktivität beendet und man
     * kommt in die Aktivität zuvor (LoginActivity) zurück.
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
        return super.onOptionsItemSelected(item);
    }

    /**
     * Hat die selbe Funktion wie onOptionsItemSelected ist aber für das Verhalten beim Drücken des
     * physischen Zurück-Knopfs zuständig.
     *
     */

    public void onBackPressed ()
    {
        Intent home = new Intent(LoginAlternativActivity.this, LoginActivity.class);
        startActivity(home);
        LoginAlternativActivity.this.finish();
    }
}
