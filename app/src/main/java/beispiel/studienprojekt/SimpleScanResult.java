package beispiel.studienprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import static beispiel.studienprojekt.R.id.age_patient;
import static beispiel.studienprojekt.R.id.bed_patient;
import static beispiel.studienprojekt.R.id.firstname_patient;
import static beispiel.studienprojekt.R.id.id_patient;
import static beispiel.studienprojekt.R.id.lastname_patient;
import static beispiel.studienprojekt.R.id.room_patient;
import static beispiel.studienprojekt.R.id.sex_patient;
import static beispiel.studienprojekt.SettingsPreference.setTextSize;
import static beispiel.studienprojekt.SimpleScan.type;

/**
 * Klasse die aufgerufen wird, wenn bei dem Einzelscan ein Code erfolgreich gescannt wurde.
 * Erweitert die Klasse Data die Daten wie z.B. die Patienten enthält die für diese Aktivität
 * benötigt werden.
 */

public class SimpleScanResult extends Data
{
    /**
     * Aktivität bekommt nur eine Toolbar mit Titel und Zurück-Button
     */

    Toolbar simplescan_toolbar;

    /**
     * Die Aktivität überprüft zuerst den in DispenserScan gesetzten Typ (type) und ordnet je nach
     * Ausprägung das passende Layout dazu, z.B. für Medikamente R.layout.activity_simplescanmedication.
     * Die in jedem Layout vorhandenen TextViews bekommen dann die Werte, die in SimpleScan der
     * jeweiligen Variable zugeordnet wurde, z.B. für Patienten scannedPatient.
     *
     * @param savedInstanceState Daten der Aktivität
     */

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (type == 1)
        {
            setContentView(R.layout.activity_simplescanemployee);
            simplescan_toolbar = (Toolbar)findViewById(R.id.toolbarSimpleScan);
            simplescan_toolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
            simplescan_toolbar.setTitle("Mitarbeiter");
            setSupportActionBar(simplescan_toolbar);

            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            TextView ten = (TextView)findViewById(R.id.titleid_employee);
            TextView tfn = (TextView)findViewById(R.id.titlefirstname_employee);
            TextView tln = (TextView)findViewById(R.id.titlelastname_employee);

            TextView en = (TextView)findViewById(R.id.id_employee);
            TextView fn = (TextView)findViewById(R.id.firstname_employee);
            TextView ln = (TextView)findViewById(R.id.lastname_employee);

            en.setText(String.valueOf(scannedEmployee.getId()));
            fn.setText(scannedEmployee.getFirstname());
            ln.setText(scannedEmployee.getLastname());

            setTextSize(ten);
            setTextSize(tfn);
            setTextSize(tln);

            setTextSize(en);
            setTextSize(fn);
            setTextSize(ln);
        }

        if (type == 2)
        {
            setContentView(R.layout.activity_simplescanmedication);
            simplescan_toolbar = (Toolbar)findViewById(R.id.toolbarSimpleScan);
            simplescan_toolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
            simplescan_toolbar.setTitle("Medikament");
            setSupportActionBar(simplescan_toolbar);

            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            TextView tat = (TextView)findViewById(R.id.titleatc_medication);
            TextView tna = (TextView)findViewById(R.id.titlename_medication);
            TextView tun = (TextView)findViewById(R.id.titleunit_medication);
            TextView trm = (TextView)findViewById(R.id.titleroamoa_medication);

            TextView at = (TextView)findViewById(R.id.atc_medication);
            TextView na = (TextView)findViewById(R.id.name_medication);
            TextView un = (TextView)findViewById(R.id.unit_medication);
            TextView rm = (TextView)findViewById(R.id.roamoa_medication);

            at.setText(scannedMedication.getAtc());
            na.setText(scannedMedication.getName());
            na.append("\n");
            un.setText(scannedMedication.getUnit());
            rm.setText(scannedMedication.getRoamoa());

            setTextSize(tat);
            setTextSize(tna);
            setTextSize(tun);
            setTextSize(trm);

            setTextSize(at);
            setTextSize(na);
            setTextSize(un);
            setTextSize(rm);
        }

        if (type == 3)
        {
            setContentView(R.layout.activity_simplescanpatient);
            simplescan_toolbar = (Toolbar)findViewById(R.id.toolbarSimpleScan);
            simplescan_toolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
            simplescan_toolbar.setTitle("Patient");
            setSupportActionBar(simplescan_toolbar);

            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            TextView tpn = (TextView)findViewById(R.id.titleid_patient);
            TextView tfn = (TextView)findViewById(R.id.titlefirstname_patient);
            TextView tln = (TextView)findViewById(R.id.titlelastname_patient);
            TextView tse = (TextView)findViewById(R.id.titlesex_patient);
            TextView tag = (TextView)findViewById(R.id.titleage_patient);
            TextView tro = (TextView)findViewById(R.id.titleroom_patient);
            TextView tbe = (TextView)findViewById(R.id.titlebed_patient);

            TextView pn = (TextView)findViewById(id_patient);
            TextView fn = (TextView)findViewById(firstname_patient);
            TextView ln = (TextView)findViewById(lastname_patient);
            TextView se = (TextView)findViewById(sex_patient);
            TextView ag = (TextView)findViewById(age_patient);
            TextView ro = (TextView)findViewById(room_patient);
            TextView be = (TextView)findViewById(bed_patient);

            pn.setText(String.valueOf(scannedPatient.getId()));
            fn.setText(scannedPatient.getFirstname());
            ln.setText(scannedPatient.getLastname());
            se.setText(scannedPatient.getSex());
            ag.setText(String.valueOf(scannedPatient.getAge()));
            ro.setText(String.valueOf(scannedPatient.getRoom()));
            be.setText(String.valueOf(scannedPatient.getBed()));

            setTextSize(tpn);
            setTextSize(tfn);
            setTextSize(tln);
            setTextSize(tse);
            setTextSize(tag);
            setTextSize(tro);
            setTextSize(tbe);

            setTextSize(pn);
            setTextSize(fn);
            setTextSize(ln);
            setTextSize(se);
            setTextSize(ag);
            setTextSize(ro);
            setTextSize(be);

        }

        if (type == 4)
        {
            setContentView(R.layout.activity_simplescandispenser);
            simplescan_toolbar = (Toolbar)findViewById(R.id.toolbarSimpleScan);
            simplescan_toolbar.setTitleTextColor(getResources().getColor(R.color.ToolbarTitle));
            simplescan_toolbar.setTitle("Dispenser");
            setSupportActionBar(simplescan_toolbar);

            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            String output = "";
            for (int i = 0; i < scannedDispenser.getDispenser_Medications().length; i++)
            {
                output = output + getMedication(getDispenserMedication
                        (scannedDispenser.getDispenser_Medications()[i]))+ "\n";
            }

            TextView tdn = (TextView) findViewById(R.id.titleid_dispenser);
            TextView tme = (TextView) findViewById(R.id.titlemedications_dispenser);
            TextView tda = (TextView) findViewById(R.id.titledate_dispenser);
            TextView tdp = (TextView) findViewById(R.id.titlepatient_dispenser);

            TextView dn = (TextView) findViewById(R.id.id_dispenser);
            TextView me = (TextView) findViewById(R.id.medications_dispenser);
            TextView da = (TextView) findViewById(R.id.date_dispenser);
            TextView dp = (TextView) findViewById(R.id.patient_dispenser);

            dn.setText(scannedDispenser.getDispenser_Id());
            me.setText(output);
            da.setText(scannedDispenser.getDate());
            dp.setText(getDispenserPatient(scannedDispenser.getDispenser_Medications()[0]));

            setTextSize(tdn);
            setTextSize(tme);
            setTextSize(tda);
            setTextSize(tdp);

            setTextSize(dn);
            setTextSize(me);
            setTextSize(da);
            setTextSize(dp);
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
            Intent home = new Intent(SimpleScanResult.this, MainActivity.class);
            startActivity(home);
            clicked();
            SimpleScanResult.this.finish();
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
        Intent home = new Intent(SimpleScanResult.this, MainActivity.class);
        startActivity(home);
        clicked();
        SimpleScanResult.this.finish();
    }

}
