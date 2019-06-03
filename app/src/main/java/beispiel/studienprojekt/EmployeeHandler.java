package beispiel.studienprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die eine Angestellten Datenbank erstellt und verwaltet.
 * Erweitert die Klasse SQLiteOpenHelper, die Methoden für die Verwaltung von Datenbanken
 * bereitstellt.
 */

public class EmployeeHandler extends SQLiteOpenHelper
{
    /**
     * Statische unveränderbare Variablen, z.B. Version und Name der Datenbank, aber auch die
     * einzelnen Spalten: bei Angestellten die ID, den Vornamen, den Nachnamen und das Passwort.
     */

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database_employee";
    private static final String TABLE_EMPLOYEES = "employees";

    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_PASSWORD = "password";
    private static final String CREATE_EMPLOYEE_TABLE = " CREATE TABLE " + TABLE_EMPLOYEES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FIRSTNAME + " TEXT,"
            + KEY_LASTNAME + " TEXT,"
            + KEY_PASSWORD + " TEXT" + ")";
    private static final String DELETE_EMPLOYEE_TABLE = "DROP TABLE IF EXISTS " + TABLE_EMPLOYEES;

    /**
     * Konstruktor der dann in Data aufgerufen wird.
     * Datenbank bekommt einen Ort (Context), Namen und Version. Der Cursor der Datenbank wird auf
     * null gesetzt, also das Standard-Verhalten.
     *
     * @param context Ort wo die Datenbank erstellt wird
     */

    public EmployeeHandler (Context context)
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
        sqLiteDatabase.execSQL(CREATE_EMPLOYEE_TABLE);
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
        sqLiteDatabase.execSQL(DELETE_EMPLOYEE_TABLE);
        onCreate(sqLiteDatabase);
    }

    /**
     * Fügt einen Angestellten zur Datenbank hinzu.
     * Die Datenbank wird aufgerufen (getWritableDatabase), der neu erstellte Angestellte in diese
     * eingefügt und die Datenbank wieder geschlossen.
     * Dies geschieht über die Klasse ContentValues, die anhand der Klasse Employee die
     * jeweiligen Werte in die Tabelle schreibt.
     * Hier: Spalte 1 - ID, Spalte 2 - Vorname, Spalte 3 - Nachname, Spalte 4 - Passwort
     *
     * @param employee Employee
     */

    public void addEmployee (Employee employee)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, employee.getId());
        values.put(KEY_FIRSTNAME, employee.getFirstname());
        values.put(KEY_LASTNAME, employee.getLastname());
        values.put(KEY_PASSWORD, employee.getPassword());

       // sqLiteDatabase.insert(TABLE_EMPLOYEES, null, values);
        sqLiteDatabase.insertWithOnConflict(TABLE_EMPLOYEES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }
/*
    public Employee getEmployee (int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_EMPLOYEES, new String[] { KEY_ID,
                KEY_FIRSTNAME, KEY_LASTNAME, KEY_PASSWORD }, KEY_ID + "=? ",
                new String[] {String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Employee employee = new Employee(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));

        return employee;
    }
*/
    /**
     * Erstellt eine Liste aller Einträge der Tabelle (Angestellte).
     * Anfrage query selektiert alles aus der Angestellten Tabelle und der Cursor läuft über den
     * angefragten String.
     * Solange der cursor sich auf den nächsten Eintrag bewegen kann, also die Tabelle noch nicht
     * komplett durchlaufen ist, wird ein neuer Angestellter in die Liste EmployeeList
     * eingefügt, der die jeweiligen Informationen aus der Tabelle bekommt.
     * Hier: Spalte 1 - ID, Spalte 2 - Vorname, Spalte 3 - Nachname, Spalte 4 - Passwort
     *
     * @return List<Employee>
     */

    public List<Employee> getAllEmployees ()
    {
        List<Employee> EmployeeList = new ArrayList<Employee>();
        String query = "SELECT * FROM " + TABLE_EMPLOYEES;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst() )
        {
            do
            {
                Employee employee = new Employee();
                employee.setId(Integer.parseInt(cursor.getString(0)));
                employee.setFirstname(cursor.getString(1));
                employee.setLastname(cursor.getString(2));
                employee.setPassword(cursor.getString(3));

                EmployeeList.add(employee);
            } while (cursor.moveToNext());
        }

        return EmployeeList;
    }
}
