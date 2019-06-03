package beispiel.studienprojekt;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static beispiel.studienprojekt.Documentation.convertArrayToString;
import static beispiel.studienprojekt.Documentation.convertStringToArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die eine Dokumentations-Datenbank erstellt und verwaltet.
 * Erweitert die Klasse SQLiteOpenHelper, die Methoden für die Verwaltung von Datenbanken
 * bereitstellt.
 */

public class DocumentationHandler extends SQLiteOpenHelper
{
    /**
     * Statische unveränderbare Variablen, z.B. Version und Name der Datenbank, aber auch die
     * einzelnen Spalten: bei Dokumentation die ID des Angestellten und Patienten, die ATC-Nummern
     * der Medikamente, das Datum und der Typ.
     * Außerdem die beiden SQL-Befehle, die die Datebank erstellen (CREATE TABLE) bzw. löschen
     * (DROP TABLE IF EXISTS).
     */

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database_documentation";
    private static final String TABLE_DOCUMENTATION = "documentation";
    private static final String KEY_EMPLOYEE = "employee";
    private static final String KEY_PATIENT = "patient";
    private static final String KEY_ATC = "atc";
    private static final String KEY_DATE = "date";
    private static final String KEY_TYPE = "type";
    private static final String CREATE_DOCUMENTATION_TABLE = " CREATE TABLE " + TABLE_DOCUMENTATION + "("
            + KEY_EMPLOYEE + " INTEGER,"
            + KEY_PATIENT + " INTEGER,"
            + KEY_ATC + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_TYPE + " TEXT" + ")";
    private static final String DELETE_DOCUMENTATION_TABLE = "DROP TABLE IF EXISTS " + TABLE_DOCUMENTATION;

    /**
     * Konstruktor der dann in Data aufgerufen wird.
     * Die Datenbank bekommt einen Ort (Context), Namen und Version. Der Cursor der Datenbank wird
     * auf null gesetzt, also das Standard-Verhalten.
     *
     * @param context Ort wo die Datenbank erstellt wird
     */

    public DocumentationHandler (Context context)
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
        sqLiteDatabase.execSQL(CREATE_DOCUMENTATION_TABLE);
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
        sqLiteDatabase.execSQL(DELETE_DOCUMENTATION_TABLE);
        onCreate(sqLiteDatabase);
    }

    /**
     * Wird aufgerufen, wenn die vorhandene Datenbank gelöscht werden soll.
     * Die Datenbank wird aufgerufen (getWritableDatabase) gelöscht, neu initialisiert und
     * geschlossen.
     *
     */


    public void delete ()
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(DELETE_DOCUMENTATION_TABLE);
        database.execSQL(CREATE_DOCUMENTATION_TABLE);
        database.close();
    }

    /**
     * Fügt eine Dokumentation zur Datenbank hinzu.
     * Die Datenbank wird aufgerufen (getWritableDatabase), die neu erstellte Dokumentation in diese
     * eingefügt und die Datenbank wieder geschlossen.
     * Dies geschieht über die Klasse ContentValues, die anhand der Klasse Documentation die
     * jeweiligen Werte in die Tabelle schreibt.
     * Hier: Spalte 1 - Angestellter, Spalte 2 - Patient, Spalte 3 - ATC, Spalte 4 - Datum,
     * Spalte 5 - Typ
     *
     * @param documentation Dokumentation
     */

    public void addDocumentation (Documentation documentation)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMPLOYEE, documentation.getEmployee());
        values.put(KEY_PATIENT, documentation.getPatient());
        values.put(KEY_ATC, convertArrayToString(documentation.getAtc()));
        values.put(KEY_DATE, documentation.getDate());
        values.put(KEY_TYPE, documentation.getType());

        sqLiteDatabase.insertWithOnConflict(TABLE_DOCUMENTATION, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }
/*
    public Documentation getDocumentation (int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_DOCUMENTATION, new String[] { KEY_EMPLOYEE,
                        KEY_PATIENT, KEY_ATC, KEY_DATE, KEY_TYPE }, KEY_EMPLOYEE + "=? ",
                new String[] {String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Documentation documentation = new Documentation(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)),
                convertStringToArray(cursor.getString(2)),
                cursor.getString(3),
                cursor.getString(4));

        return documentation;
    }
*/

    /**
     * Erstellt eine Liste aller Einträge der Tabelle (Dokumentation).
     * Anfrage query selektiert alles aus der Dokumentation Tabelle und der Cursor läuft über den
     * angefragten String.
     * Solange der cursor sich auf den nächsten Eintrag bewegen kann, also die Tabelle noch nicht
     * komplett durchlaufen ist, wird ein neues Dokumentation in die Liste DocumentationList
     * eingefügt, der die jeweiligen Informationen aus der Tabelle bekommt.
     * Hier: Spalte 1 - Angestellter, Spalte 2 - Patient, Spalte 3 - ATC, Spalte 4 - Datum,
     * Spalte 5 - Typ
     *
     * @return List<Documentation>
     */

    public List<Documentation> getAllDocumentations ()
    {
        List<Documentation> DocumentationList = new ArrayList<Documentation>();
        String query = "SELECT * FROM " + TABLE_DOCUMENTATION;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst() )
        {
            do
            {
                Documentation documentation = new Documentation();
                documentation.setEmployee(Integer.parseInt(cursor.getString(0)));
                documentation.setPatient(Integer.parseInt(cursor.getString(1)));
                documentation.setAtc(convertStringToArray(cursor.getString(2)));
                documentation.setDate(cursor.getString(3));
                documentation.setType(cursor.getString(4));

                DocumentationList.add(documentation);

            } while (cursor.moveToNext());
        }

        return DocumentationList;
    }
}
