package beispiel.studienprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die eine Medikations-Datenbank erstellt und verwaltet.
 * Erweitert die Klasse SQLiteOpenHelper, die Methoden für die Verwaltung von Datenbanken
 * bereitstellt.
 */

public class MedicationHandler extends SQLiteOpenHelper
{
    /**
     * Statische unveränderbare Variablen, z.B. Version und Name der Datenbank, aber auch die
     * einzelnen Spalten: bei Medikation die ATC-Nummer, der Name, die Einheit und die Einnahmeart.
     * Außerdem die beiden SQL-Befehle, die die Datebank erstellen (CREATE TABLE) bzw. löschen
     * (DROP TABLE IF EXISTS).
     */

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database_medication";
    private static final String TABLE_MEDICATION = "medication";

    private static final String KEY_ATC = "atc";
    private static final String KEY_NAME = "name";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_ROAMOA = "roamoa";
    private static final String CREATE_MEDICATION_TABLE = " CREATE TABLE " + TABLE_MEDICATION + "("
            + KEY_ATC + " TEXT,"
            + KEY_NAME + " TEXT,"
            + KEY_UNIT + " TEXT,"
            + KEY_ROAMOA + " TEXT" + ")";
    private static final String DELETE_MEDICATION_TABLE = "DROP TABLE IF EXISTS " + TABLE_MEDICATION;

    /**
     * Konstruktor der dann in Data aufgerufen wird.
     * Die Datenbank bekommt einen Ort (Context), Namen und Version. Der Cursor der Datenbank wird
     * auf null gesetzt, also das Standard-Verhalten.
     *
     * @param context Ort wo die Datenbank erstellt wird
     */

    public MedicationHandler (Context context)
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
        sqLiteDatabase.execSQL(CREATE_MEDICATION_TABLE);
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
        sqLiteDatabase.execSQL(DELETE_MEDICATION_TABLE);
        onCreate(sqLiteDatabase);
    }

    /**
     * Fügt ein Medikament zur Datenbank hinzu.
     * Die Datenbank wird aufgerufen (getWritableDatabase), das neu erstellte Medikament in diese
     * eingefügt und die Datenbank wieder geschlossen.
     * Dies geschieht über die Klasse ContentValues, die anhand der Klasse Medication die jeweiligen
     * Werte in die Tabelle schreibt.
     * Hier: Spalte 1 - ATC-Nummern, Spalte 2 - Name, Spalte 3 - Einheit, Spalte 4 - Einnahmeart
     *
     * @param medication Medikament
     */

    public void addMedication (Medication medication)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ATC, medication.getAtc());
        values.put(KEY_NAME, medication.getName());
        values.put(KEY_UNIT, medication.getUnit());
        values.put(KEY_ROAMOA, medication.getRoamoa());

        // sqLiteDatabase.insert(TABLE_EMPLOYEES, null, values);
        sqLiteDatabase.insertWithOnConflict(TABLE_MEDICATION, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }
/*
    public Medication getMedication (int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_MEDICATION, new String[] { KEY_ATC,
                        KEY_NAME, KEY_UNIT, KEY_ROAMOA }, KEY_ATC + "=? ",
                new String[] {String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Medication medication = new Medication(cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));

        return medication;
    }
*/
    /**
     * Erstellt eine Liste aller Einträge der Tabelle (Medikament).
     * Anfrage query selektiert alles aus der Medikamenten Tabelle und der Cursor läuft über den
     * angefragten String.
     * Solange der cursor sich auf den nächsten Eintrag bewegen kann, also die Tabelle noch nicht
     * komplett durchlaufen ist, wird ein neues Medikament in die Liste MedicationList eingefügt,
     * der die jeweiligen Informationen aus der Tabelle bekommt.
     * Hier: Spalte 1 - ATC-Nummern, Spalte 2 - Name, Spalte 3 - Einheit, Spalte 4 - Einnahmeart
     *
     * @return List<Medication>
     */

    public List<Medication> getAllMedications ()
    {
        List<Medication> MedicationList = new ArrayList<Medication>();
        String query = "SELECT * FROM " + TABLE_MEDICATION;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst() )
        {
            do
            {
                Medication medication = new Medication();
                medication.setAtc(cursor.getString(0));
                medication.setName(cursor.getString(1));
                medication.setUnit(cursor.getString(2));
                medication.setRoamoa(cursor.getString(3));

                MedicationList.add(medication);
            } while (cursor.moveToNext());
        }

        return MedicationList;
    }
}
