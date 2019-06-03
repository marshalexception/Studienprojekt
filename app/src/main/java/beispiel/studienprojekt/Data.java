package beispiel.studienprojekt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static beispiel.studienprojekt.LoginActivity.isautologout;
import static beispiel.studienprojekt.LoginActivity.loggedin;
import static beispiel.studienprojekt.MainActivity.MA;
import static beispiel.studienprojekt.MainActivity.displayLogoutTime;
import static beispiel.studienprojekt.SettingsPreference.MainMenuLogoutTime;
import static beispiel.studienprojekt.SettingsPreference.logout_time;
import static beispiel.studienprojekt.SettingsPreference.logouttimeindex;

/**
 * Diese Klasse enthält die interne Datenbank mit Angestellten, Medizin, Patienten, Dispensern
 * und Dokumentationen.
 * Außerdem enthält sie Methoden um Daten aus der Datenbank auszulesen, z.B. das Medikament passend
 * zur ATC-Nummer zurückzugeben, und die Getter und Setter für alle Klassen der Datenbank.
 * Fast alle anderen Klassen erben von Data, was wiederrum nötig ist um von überall auf die Datenbank
 * zuzugreifen. Sie selbst erweitert die Klasse AppCompatActivity, die von Android bereit gestellt
 * wird um App spezifische Aktionen zu implementieren, z.B. das Verhalten der Toolbar.
 *
 * @author Simon Trumm
 */

public class Data extends AppCompatActivity
{
    /**
     * Die Daten für die jeweilige Datenbank, z.B. Angstellte = employeeHandler.
     * Liste von Angestellten, Medikamenten, Patienten, ...
     * Einzelne statische Variablen, die zur einfachereren Datenweitergabe benutzt werden, z.B.
     * wird der beim Login gescannte Angstellte auf scannedEmployee gesetzt, so dass bei der
     * Dokumentation einfacher auf diesen zugegriffen werden kann.
     */
    private static Context loginContext;

    public EmployeeHandler employeeHandler = new EmployeeHandler(this);
    public List<Employee> employees;
    public static Employee currentEmployee = new Employee();
    public static Employee scannedEmployee = new Employee();

    public MedicationHandler medicationHandler = new MedicationHandler(this);
    public List<Medication> medications;
    public static Medication scannedMedication = new Medication();

    public PatientHandler patientHandler = new PatientHandler(this);
    public List<Patient> patients;
    public static Patient scannedPatient = new Patient();

    public MedicationPatientHandler mpHandler = new MedicationPatientHandler(this);
    public List<MedicationPatient> mpList;
    public static MedicationPatient scannedMedicationPatient = new MedicationPatient();

    public DispenserHandler dispenserHandler = new DispenserHandler(this);
    public List<Dispenser> dispensers;
    public static List<String> stockedDispensers = new ArrayList<>();
    public static List<String> applicatedDispensers = new ArrayList<>();
    public static Dispenser scannedDispenser = new Dispenser();

    public DocumentationHandler documentationHandler = new DocumentationHandler(this);
    public List<Documentation> documentations;

    /**
     * Methode die beim Erstellen der Klasse aufgerufen wird.
     * Der jeweiligen Datenbank werden Einträge hinzugefügt und alles in eine Liste gepackt, die
     * dann bei Suchbedarf durchlaufen werden kann. Dies geschieht für die Angstellten, Medikamente,
     * Patienten, Medizin_Patient Entität und Dispenser. Die Dokumentation bleibt am Anfang leer und
     * wird erst bei erfolgreicher Dispenserbestückung bzw. Medikamentengabe befüllt.
     *
     * @param savedInstanceState Daten der Aktivität
     */

    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Wenn die Aktivität wiederhergestellt wird werden Daten in savedInstanceState gespeichert,
         * so dass keien Informationen verloren gehen.
         */

        super.onCreate(savedInstanceState);
        loginContext = this;

        employeeHandler.addEmployee(new Employee(21564, "Max", "Mustermann", "Passwort123"));
        employeeHandler.addEmployee(new Employee(12521, "Martina", "Mustefrau", "123Passwort"));
        employeeHandler.addEmployee(new Employee(56213, "Julia", "Sommer", "Autobahn"));
        employeeHandler.addEmployee(new Employee(54614, "Sophia", "Schmidt", "Urlaubskartenmechanismus"));
        employeeHandler.addEmployee(new Employee(92143, "Dieter", "Bleich", "9126BreakingBad"));
        employees = employeeHandler.getAllEmployees();

        medicationHandler.addMedication(new Medication("A11HA30", "Panthenol JENAPHARM","100 mg", "p.o."));
        medicationHandler.addMedication(new Medication("C01AA05", "Dixogin (Lenoxin)","0,25 mg", "p.o."));
        medicationHandler.addMedication(new Medication("A06AH03", "Moventig Eurim","25 mg", "p.o."));
        medicationHandler.addMedication(new Medication("N02BA01", "Aspirin","500 mg", "p.o."));
        medicationHandler.addMedication(new Medication("A10AC01", "Insulatard Penfill Eurim","100 ml", "s.c."));
        medications = medicationHandler.getAllMedications();

        patientHandler.addPatient(new Patient(421522, "Sven", "Herz", "männlich", 42, 304, 2));
        patientHandler.addPatient(new Patient(627137, "Ute", "Abend", "weiblich", 21, 152, 1));
        patientHandler.addPatient(new Patient(816353, "Tom", "Hoffmann", "männlich", 62, 625, 1));
        patientHandler.addPatient(new Patient(148346, "Jana", "Lange", "weiblich", 35, 167, 4));
        patientHandler.addPatient(new Patient(259872, "Uwe", "Freud", "männlich", 36, 201, 3));
        patients = patientHandler.getAllPatients();

        mpHandler.addMedicationPatient(new MedicationPatient(1234567, "A10AC01", 816353, 1, "morgens", "offen"));
        mpHandler.addMedicationPatient(new MedicationPatient(5494982, "N02BA01", 816353, 1, "abends", "offen"));
        mpHandler.addMedicationPatient(new MedicationPatient(1615621, "N02BA01", 259872, 1, "morgens", "offen"));
        mpHandler.addMedicationPatient(new MedicationPatient(9369415, "A06AH03", 259872, 1, "mittags", "offen"));
        mpHandler.addMedicationPatient(new MedicationPatient(3564582, "C01AA05", 259872, 1, "abends", "offen"));
        mpList = mpHandler.getAllMedicationPatients();

        String[] dp1 = {"1234567", "5494982"};
        String[] dp2 = {"1615621", "9369415", "3564582"};

        dispenserHandler.addDispenser(new Dispenser("D5631", dp1, "22.03.2017"));
        dispenserHandler.addDispenser(new Dispenser("D1538", dp2, "22.03.2017"));
        dispensers = dispenserHandler.getAllDispensers();

    }


    /**
     * Setzt den aktuellen Angestellten in Data.
     *
     * @param e
     */
    public void setCurrentEmployee (Employee e)
    {
        currentEmployee = e;
    }

    /**
     * Gibt den aktuellen Angestellen in Data zurück.
     *
     * @return Employee
     */
    public Employee getCurrentEmployee ()
    {
        return currentEmployee;
    }

    /**
     * Setzt den gescannten Angestellten in Data.
     *
     * @param e
     */
    public void setScannedEmployee (Employee e)
    {
        scannedEmployee = e;
    }

    /**
     * Gibt den gescannten Angestellen in Data zurück.
     *
     * @return Employee
     */
    public Employee getScannedEmployee ()
    {
        return scannedEmployee;
    }

    /**
     * Setzt das gescannte Medikament in Data.
     *
     * @param m
     */
    public void setScannedMedication (Medication m)
    {
        scannedMedication = m;
    }

    /**
     * Gibt das gescannte Medikament in Data zurück.
     *
     * @return Medication
     */
    public Medication getScannedMedication ()
    {
        return scannedMedication;
    }

    /**
     * Setzt den gescannten Patienten in Data.
     *
     * @param p
     */
    public void setScannedPatient (Patient p)
    {
        scannedPatient = p;
    }

    /**
     * Gibt den gescannten Patienten in Data zurück.
     *
     * @return Patient
     */
    public Patient getScannedPatient ()
    {
        return scannedPatient;
    }

    /**
     * Setzt die gescannte Medikament_Patient Entität in Data.
     *
     * @param mp
     */
    public void setScannedMedicationPatient (MedicationPatient mp)
    {
        scannedMedicationPatient = mp;
    }

    /**
     * Gibt die gescannte Medikament_Patient Entität in Data zurück.
     *
     * @return MedicationPatient
     */
    public MedicationPatient getScannedMedicationPatient ()
    {
        return scannedMedicationPatient;
    }

    /**
     * Setzt den gescannten Dispenser in Data.
     *
     * @param d
     */
    public void setScannedDispenser (Dispenser d)
    {
        scannedDispenser = d;
    }

    /**
     * Gibt den gescannten Dispenser in Data zurück.
     *
     * @return Dispenser
     */
    public Dispenser getScannedDispenser ()
    {
        return scannedDispenser;
    }

    /**
     * Gibt ein Medikament result zurück.
     * Liste der Medikamente wird durchlaufen und falls die ATC-Nummer eines Medikaments gleich des
     * Strings atc sein sollte wird das Medikament result auf dieses gesetzt und zurückgegeben.
     *
     * @param atc String der eine ATC-Nummer enthalten sollte.
     * @return Medication
     */

    public Medication getMedication (String atc)
    {
        Medication result = new Medication();
        for (Medication medication : medications)
        {
            if (atc.equals(medication.getAtc()))
            {
                result = medication;
            }
        }
        return result;
    }

    /**
     * Gibt ein String result zurück der die ATC-Nummer einer Medication_Patient Entität enthält.
     * Liste der Medication_Patient Einträge wird durchlaufen und falls die MedicationPatientId
     * eines Eintrags gleich x (dispenser_mp in Integer gecastet, da MedicationPatientID auch
     * Integer ist) ist, bekommt der String result die dazugehörige ATC-Nummer die dann
     * zurückgegeben wird.
     *
     * @param dispenser_mp String der eine Medication_Patient ID enthalten sollte.
     * @return String
     */

    public String getDispenserMedication (String dispenser_mp) {
        String result = "";
        int x = Integer.parseInt(dispenser_mp);
        for (MedicationPatient mp : mpList) {
            if (mp.getMedicationPatientId() == x) {
                result = mp.getAtc();
            }
        }
        return result;
    }

    /**
     * Gibt ein String result zurück der den Vor- und Nachnamen eines Patienten enthält.
     * Liste der Medication_Patient Einträge wird durchlaufen und falls die MedicationPatientId
     * eines Eintrags gleich dispenser_medication ist, bekommt die Integer-Variable tmp den Wert
     * der in Medication_Patient gespeicherten Patienten ID.
     * Dann wird die Liste der Patienten durchlaufen und bei Gleichheit der Patienten_ID und tmp
     * wird der String result mit Vor- und Nachnamen befüllt und zurückgegeben.
     *
     * @param dispenser_medication String der eine Medication_Patient ID enthalten sollte.
     * @return String
     */

    public String getDispenserPatient (String dispenser_medication)
    {
        String result = "";

        for (MedicationPatient mp : mpList)
        {
            if (mp.getMedicationPatientId() == Integer.parseInt(dispenser_medication))
            {
                int tmp = mp.getId();
                for (Patient patient : patients)
                {
                    if (patient.getId() == tmp)
                    {
                        result = patient.getFirstname() + " " + patient.getLastname();
                    }
                }
            }
        }
        return result;
    }

    /**
     * Gibt ein String mit dem Zeitpunkt der Medikamenteneinnahme zurück.
     * Liste der Medication_Patient Einträge wird durchlaufen und zwei Vergleiche abgefragt:
     * 1. ATC-Nummer vom Eintrag und ATC-Nummer von tmp müssen gleich sein.
     * 2. Patienten-Id vom Eintrag und ID des gescannten Patienten müssen gleich sein.
     * Es wird also nach dem richtigen Medikament und dem richtigen Patienten geprüft.
     * Ist dies beides der Fall wird die Medication_Patient Variable result auf den Eintrag gesetzt
     * und zurückgegeben.
     *
     * @param tmp Medikament
     * @return String
     */

    public String getTaking (Medication tmp)
    {
        MedicationPatient result = new MedicationPatient();
        for (MedicationPatient mp : mpList)
        {
            if (tmp.getAtc().equals(mp.getAtc()) &&
                    (scannedPatient.getId() == mp.getId()))
            {
                result = mp;
            }
        }
        return result.getTaking();
    }

    /**
     * Gibt ein Integer mit der Dosierung des Medikaments zurück.
     * Äquivalent zu getTaking, nur dass die Dosierung (getDose()) zurückgegeben wird.
     *
     * @param tmp Medikament
     * @return int
     */

    public int getDose (Medication tmp)
    {
        MedicationPatient result = new MedicationPatient();
        for (MedicationPatient mp : mpList)
        {

            if (tmp.getAtc().equals(mp.getAtc()) &&
                    (scannedPatient.getId() == mp.getId()))
            {
                result = mp;
            }
        }
        return result.getDose();
    }


    /*
    public void write_document_medication (Documentation documentation, Context context)
    {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("dokument.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(documentation.toString());
            outputStreamWriter.close();
            Log.d("Document written", documentation.toString());
        } catch (IOException e)
        {
            Log.d("Exception", "File write failed: " + e.toString());
        }

    }

    public String read_document_medication (Context context)
    {
        String result = "";
        try {
            InputStream inputStream = context.openFileInput("dokument.txt");
            if (inputStream != null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String getString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (getString = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(getString);
                }

                inputStream.close();
                result = stringBuilder.toString();
            }
        } catch (FileNotFoundException e)
        {
            Log.d("File not found", "");
        } catch (IOException e)
        {
            Log.d("Can not read file", "");
        }

        return result;
    }
*/

    public static int secondsPassed, currentTime;
    public static Timer timer = new Timer();
    public static TimerTask task = new TimerTask() {
        @Override
        public void run() {
            secondsPassed++;
            Log.d("Sekunden vergangen: ", secondsPassed+" Logout-Time " + MainMenuLogoutTime(logouttimeindex) *60);
            if (secondsPassed == MainMenuLogoutTime(logouttimeindex) * 60)
            {
                logout();
            }
            if ((secondsPassed % 60 == 0) && (currentTime > 0))
            {
                currentTime = currentTime - 1;
                MA.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayLogoutTime(currentTime);
                    }
                });
                /*
                int time = Integer.parseInt(logouttime.getText().toString());
                displayLogoutTime(time--);
                */
           }
        }
    };

    public static void start ()
    {
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public static void restart()
    {
        secondsPassed = 0;
        //timer.cancel();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                secondsPassed++;
                Log.d("Sekunden vergangen: ", secondsPassed+" Logout-Time " + MainMenuLogoutTime(logouttimeindex)*60);
                if (secondsPassed == MainMenuLogoutTime(logouttimeindex) * 60)
                {
                    logout();
                }
                if ((secondsPassed % 60 == 0) && (currentTime > 0)) {
                    currentTime = currentTime - 1;
                    MA.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayLogoutTime(currentTime);
                        }
                    });
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public static void reset()
    {
        timer.cancel();
        //timer = new Timer();
        secondsPassed = 0;
        Log.d("RESET", "");
    }

    public static void clicked ()
    {
        secondsPassed = 0;
        currentTime = MainMenuLogoutTime(logouttimeindex);
        MA.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayLogoutTime(currentTime);
            }
        });
    }

    public static void logout ()
    {
        isautologout = true;
        Log.d("LOGOUT", "LOGOUT");
        reset();
        Intent logout = new Intent (loginContext, LoginActivity.class);
        loginContext.startActivity(logout);
        loggedin = true;
    }

    public static void MakeDialog (AlertDialog.Builder builder)
    {
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        AlertDialog alertDialog =  builder.show();
        alertDialog.getWindow().setLayout(width,height);
        alertDialog.getWindow().setDimAmount(0.7f);
    }

}
