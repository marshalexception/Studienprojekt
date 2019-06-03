package beispiel.studienprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die eine Patienten-Datenbank erstellt und verwaltet.
 * Erweitert die Klasse SQLiteOpenHelper, die Methoden für die Verwaltung von Datenbanken
 * bereitstellt.
 */

public class PatientHandler extends SQLiteOpenHelper
{
    /**
     * Statische unveränderbare Variablen, z.B. Version und Name der Datenbank, aber auch die
     * einzelnen Spalten: bei Patienten die ID, der Vor- und Nachname, das Geschlecht, das Alter,
     * das Zimmer und das Bett.
     * Außerdem die beiden SQL-Befehle, die die Datebank erstellen (CREATE TABLE) bzw. löschen
     * (DROP TABLE IF EXISTS).
     */

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database_patient";
    private static final String TABLE_PATIENT = "patient";

    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_SEX = "sex";
    private static final String KEY_AGE = "age";
    private static final String KEY_ROOM = "room";
    private static final String KEY_BED = "bed";
    private static final String CREATE_PATIENT_TABLE = " CREATE TABLE " + TABLE_PATIENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FIRSTNAME + " TEXT,"
            + KEY_LASTNAME + " TEXT,"
            + KEY_SEX + " TEXT,"
            + KEY_AGE + " INTEGER,"
            + KEY_ROOM + " INTEGER,"
            + KEY_BED + " INTEGER" + ")";
    private static final String DELETE_PATIENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_PATIENT;

    /**
     * Konstruktor der dann in Data aufgerufen wird.
     * Die Datenbank bekommt einen Ort (Context), Namen und Version. Der Cursor der Datenbank wird
     * auf null gesetzt, also das Standard-Verhalten.
     *
     * @param context Ort wo die Datenbank erstellt wird
     */

    public PatientHandler (Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Wird aufgerufen, wenn die Datenbank erstellt wird.
     * Hier wird die Tabelle mithilfe von SQL-Befehlen initialisiert ("CREATE TABLE" + Namen der
     * Tabelle + die Spalten und den Typ des Inhalts, z.B. TEXT).
     *
     * @param sqLiteDatabase Datenbank
     */

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_PATIENT_TABLE);
    }

    /**
     * Wird aufgerufen, wenn die Datenbank geändert wird.
     * Die Methode prüft, ob bereits eine Tabelle besteht ("DROP TABLE IF EXISTS"). Ist dies der
     * Fall soll sie die Tabelle löschen.
     * Zudem wird die onCreate Methode aufgerufen.
     *
     * @param sqLiteDatabase Datenbank
     * @param i alte Version
     * @param i1 neue Version
     */

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL(DELETE_PATIENT_TABLE);
        onCreate(sqLiteDatabase);
    }

    /**
     * Fügt ein Patienten zur Datenbank hinzu.
     * Die Datenbank wird aufgerufen (getWritableDatabase), der neu erstellte Patient in diese
     * eingefügt und die Datenbank wieder geschlossen.
     * Dies geschieht über die Klasse ContentValues, die anhand der Klasse Patient die jeweiligen
     * Werte in die Tabelle schreibt.
     * Hier: Spalte 1 - ID, Spalte 2 - Vorname, Spalte 3 - Nachname, Spalte 4 - Geschlecht,
     * Spalte 5 - Alter, Spalte 6 - Zimmer, Spalte 7 - Bett
     *
     * @param patient Patient
     */

    public void addPatient (Patient patient)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, patient.getId());
        values.put(KEY_FIRSTNAME, patient.getFirstname());
        values.put(KEY_LASTNAME, patient.getLastname());
        values.put(KEY_SEX, patient.getSex());
        values.put(KEY_AGE, patient.getAge());
        values.put(KEY_ROOM, patient.getRoom());
        values.put(KEY_BED, patient.getBed());

        // sqLiteDatabase.insert(TABLE_EMPLOYEES, null, values);
        sqLiteDatabase.insertWithOnConflict(TABLE_PATIENT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }
/*
    public Patient getPatient (int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_PATIENT, new String[] { KEY_ID, KEY_FIRSTNAME,
                KEY_LASTNAME, KEY_SEX, KEY_AGE, KEY_ROOM, KEY_BED}, KEY_ID + "=? ",
                new String[] {String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Patient patient = new Patient(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)));

        return patient;
    }
*/
    /**
     * Erstellt eine Liste aller Einträge der Tabelle (Patient).
     * Anfrage query selektiert alles aus der Patienten Tabelle und der Cursor läuft über den
     * angefragten String.
     * Solange der cursor sich auf den nächsten Eintrag bewegen kann, also die Tabelle noch nicht
     * komplett durchlaufen ist, wird ein neuer Patient in die Liste MedicationList eingefügt,
     * der die jeweiligen Informationen aus der Tabelle bekommt.
     * Hier: Spalte 1 - ID, Spalte 2 - Vorname, Spalte 3 - Nachname, Spalte 4 - Geschlecht,
     * Spalte 5 - Alter, Spalte 6 - Zimmer, Spalte 7 - Bett
     *
     * @return List<Medication>
     */

    public List<Patient> getAllPatients ()
    {
        List<Patient> PatientList = new ArrayList<Patient>();
        String query = "SELECT * FROM " + TABLE_PATIENT;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst() )
        {
            do
            {
                Patient patient = new Patient();
                patient.setId(Integer.parseInt(cursor.getString(0)));
                patient.setFirstname(cursor.getString(1));
                patient.setLastname(cursor.getString(2));
                patient.setSex(cursor.getString(3));
                patient.setAge(Integer.parseInt(cursor.getString(4)));
                patient.setRoom(Integer.parseInt(cursor.getString(5)));
                patient.setBed(Integer.parseInt(cursor.getString(6)));

                PatientList.add(patient);
            } while (cursor.moveToNext());
        }

        return PatientList;
    }
}
