package beispiel.studienprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die eine Medikamenten-Patienten-Datenbank erstellt und verwaltet.
 * Erweitert die Klasse SQLiteOpenHelper, die Methoden für die Verwaltung von Datenbanken
 * bereitstellt.
 */

public class MedicationPatientHandler extends SQLiteOpenHelper
{
    /**
     * Statische unveränderbare Variablen, z.B. Version und Name der Datenbank, aber auch die
     * einzelnen Spalten: bei MedicationPatient die ID, die ATC-Nummer, die Patienten-ID, die
     * Dosierung, die Einnahmezeit und der Status.
     * Außerdem die beiden SQL-Befehle, die die Datebank erstellen (CREATE TABLE) bzw. löschen
     * (DROP TABLE IF EXISTS).
     */

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database_medicationpatient";
    private static final String TABLE_MEDICATIONPATIENT = "medicationpatient";

    private static final String KEY_MEDICATIONPATIENTID = "medicationpatientid";
    private static final String KEY_ATC = "atc";
    private static final String KEY_ID = "id";
    private static final String KEY_DOSE = "dose";
    private static final String KEY_TAKING = "taking";
    private static final String KEY_STATUS = "status";
    private static final String CREATE_MEDICATIONPATIENT_TABLE = " CREATE TABLE " +
            TABLE_MEDICATIONPATIENT + "("
            + KEY_MEDICATIONPATIENTID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ATC + " TEXT,"
            + KEY_ID + " INTEGER,"
            + KEY_DOSE + " INTEGER,"
            + KEY_TAKING + " TEXT,"
            + KEY_STATUS + " TEXT" + ")";
    private static final String DELETE_MEDICATIONPATIENT_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_MEDICATIONPATIENT;

    /**
     * Konstruktor der dann in Data aufgerufen wird.
     * Die Datenbank bekommt einen Ort (Context), Namen und Version. Der Cursor der Datenbank wird
     * auf null gesetzt, also das Standard-Verhalten.
     *
     * @param context Ort wo die Datenbank erstellt wird
     */

    public MedicationPatientHandler (Context context)
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
        sqLiteDatabase.execSQL(CREATE_MEDICATIONPATIENT_TABLE);
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
        sqLiteDatabase.execSQL(DELETE_MEDICATIONPATIENT_TABLE);
        onCreate(sqLiteDatabase);
    }

    /**
     * Fügt ein Medikamenten-Patienten Entität zur Datenbank hinzu.
     * Die Datenbank wird aufgerufen (getWritableDatabase), der neu erstellte MedicationPatient in
     * diese eingefügt und die Datenbank wieder geschlossen.
     * Dies geschieht über die Klasse ContentValues, die anhand der Klasse MedicationPatient die
     * jeweiligen Werte in die Tabelle schreibt.
     * Hier: Spalte 1 - MedicationPatientID, Spalte 2 - ATC-Nummer, Spalte 3 - Patienten-ID,
     * Spalte 4 - Dosierung, Spalte 5 - Einnahmezeit, Spalte 6 - Status
     *
     * @param mp MedicationPatient
     */

    public void addMedicationPatient (MedicationPatient mp)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MEDICATIONPATIENTID, mp.getMedicationPatientId());
        values.put(KEY_ATC, mp.getAtc());
        values.put(KEY_ID, mp.getId());
        values.put(KEY_DOSE, mp.getDose());
        values.put(KEY_TAKING, mp.getTaking());
        values.put(KEY_STATUS, mp.getStatus());

        sqLiteDatabase.insertWithOnConflict(TABLE_MEDICATIONPATIENT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }
/*
    public MedicationPatient getMedicationPatient (int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_MEDICATIONPATIENT, new String[]
                { KEY_MEDICATIONPATIENTID, KEY_ATC, KEY_ID, KEY_DOSE, KEY_TAKING, KEY_STATUS},
                KEY_MEDICATIONPATIENTID + "=? ",
                new String[] {String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        MedicationPatient mp = new MedicationPatient(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),
                cursor.getString(4),
                cursor.getString(5));

        return mp;
    }
*/
    /**
     * Erstellt eine Liste aller Einträge der Tabelle (MedicationPatient).
     * Anfrage query selektiert alles aus der MedicationPatient Tabelle und der Cursor läuft über
     * den angefragten String.
     * Solange der cursor sich auf den nächsten Eintrag bewegen kann, also die Tabelle noch nicht
     * komplett durchlaufen ist, wird ein neues Medikament in die Liste mpList eingefügt, der die
     * jeweiligen Informationen aus der Tabelle bekommt.
     * Hier: Spalte 1 - MedicationPatientID, Spalte 2 - ATC-Nummer, Spalte 3 - Patienten-ID,
     * Spalte 4 - Dosierung, Spalte 5 - Einnahmezeit, Spalte 6 - Status
     *
     * @return List<Medication>
     */

    public List<MedicationPatient> getAllMedicationPatients ()
    {
        List<MedicationPatient> mpList = new ArrayList<MedicationPatient>();
        String query = "SELECT * FROM " + TABLE_MEDICATIONPATIENT;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst() )
        {
            do {
                MedicationPatient mp = new MedicationPatient();
                mp.setMedicationPatientId(Integer.parseInt(cursor.getString(0)));
                mp.setAtc(cursor.getString(1));
                mp.setId(Integer.parseInt(cursor.getString(2)));
                mp.setDose(Integer.parseInt(cursor.getString(3)));
                mp.setTaking(cursor.getString(4));
                mp.setStatus(cursor.getString(5));

                mpList.add(mp);
            } while (cursor.moveToNext() );
        }

        return mpList;
    }

}
