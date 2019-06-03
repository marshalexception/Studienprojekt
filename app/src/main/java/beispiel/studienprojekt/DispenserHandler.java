package beispiel.studienprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static beispiel.studienprojekt.Dispenser.convertArrayToString;
import static beispiel.studienprojekt.Dispenser.convertStringToArray;

/**
 * Klasse die eine Dispenser Datenbank erstellt und verwaltet.
 * Erweitert die Klasse SQLiteOpenHelper, die Methoden für die Verwaltung von Datenbanken
 * bereitstellt.
 */

public class DispenserHandler extends SQLiteOpenHelper
{
    /**
     * Statische unveränderbare Variablen, z.B. Version und Name der Datenbank, aber auch die
     * einzelnen Spalten: bei Dispensern die ID, die Medikamente und das Datum.
     * Außerdem die beiden SQL-Befehle, die die Datebank erstellen (CREATE TABLE) bzw. löschen
     * (DROP TABLE IF EXISTS).
     */

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "database_dispenser";
    private static final String TABLE_DISPENSER = "dispenser";

    private static final String KEY_ID = "dispenser_id";
    private static final String KEY_MEDICATIONS = "dispenser_medications";
    private static final String KEY_DATE = "date";
    private static final String CREATE_DISPENSER_TABLE = " CREATE TABLE " + TABLE_DISPENSER + "("
            + KEY_ID + " TEXT,"
            + KEY_MEDICATIONS + " TEXT,"
            + KEY_DATE + " TEXT" + ")";
    private static final String DELETE_DISPENSER_TABLE = "DROP TABLE IF EXISTS " + TABLE_DISPENSER;

    /**
     * Konstruktor der dann in Data aufgerufen wird.
     * Die Datenbank bekommt einen Ort (Context), Namen und Version. Der Cursor der Datenbank wird
     * auf null gesetzt, also das Standard-Verhalten.
     *
     * @param context Ort wo die Datenbank erstellt wird
     */

    public DispenserHandler (Context context)
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
        sqLiteDatabase.execSQL(CREATE_DISPENSER_TABLE);
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
        sqLiteDatabase.execSQL(DELETE_DISPENSER_TABLE);
        onCreate(sqLiteDatabase);
    }

    /**
     * Fügt einen Dispenser zur Datenbank hinzu.
     * Die Datenbank wird aufgerufen (getWritableDatabase), der neu erstellte Dispenser in diese
     * eingefügt und die Datenbank wieder geschlossen.
     * Dies geschieht über die Klasse ContentValues, die anhand der Klasse Dispenser die jeweiligen
     * Werte in die Tabelle schreibt.
     * Hier: Spalte 1 - ID, Spalte 2 - Medikamente, Spalte 3 - Datum
     *
     * @param dispenser Dispenser
     */

    public void addDispenser (Dispenser dispenser)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, dispenser.getDispenser_Id());
        values.put(KEY_MEDICATIONS, convertArrayToString(dispenser.getDispenser_Medications()));
        values.put(KEY_DATE, dispenser.getDate());

        sqLiteDatabase.insertWithOnConflict(TABLE_DISPENSER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }
/*
    public Dispenser getDispenser (int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_DISPENSER, new String[] { KEY_ID,
                        KEY_MEDICATIONS, KEY_DATE }, KEY_ID + "=? ",
                new String[] {String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Dispenser dispenser = new Dispenser(cursor.getString(0),
                convertStringToArray(cursor.getString(1)),
                cursor.getString(2));

        return dispenser;
    }
*/
    /**
     * Erstellt eine Liste aller Einträge der Tabelle (Dispenser).
     * Anfrage query selektiert alles aus der Dispenser Tabelle und der Cursor läuft über den
     * angefragten String.
     * Solange der cursor sich auf den nächsten Eintrag bewegen kann, also die Tabelle noch nicht
     * komplett durchlaufen ist, wird ein neuer Dispenser in die Liste DispenserList eingefügt, der
     * die jeweiligen Informationen aus der Tabelle bekommt.
     * Hier: Spalte 1 - ID, Spalte 2 - Medikamente, Spalte 3 - Datum
     *
     * @return List<Dispenser>
     */

    public List<Dispenser> getAllDispensers ()
    {
        List<Dispenser> DispenserList = new ArrayList<Dispenser>();
        String query = "SELECT * FROM " + TABLE_DISPENSER;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst() )
        {
            do
            {
                Dispenser dispenser = new Dispenser();
                dispenser.setDispenser_Id(cursor.getString(0));
                dispenser.setDispenser_Medications(convertStringToArray(cursor.getString(1)));
                dispenser.setDate(cursor.getString(2));

                DispenserList.add(dispenser);
            } while (cursor.moveToNext());
        }


        return DispenserList;
    }
}
