package beispiel.studienprojekt;

/**
 * Diese Klasse enthält die Informationen und Methoden für die Angestellten.
 *
 */

public class Employee
{
    /**
     * Integer Variable id für die Angestelltennummer.
     * Strings firstname, lastname und password für den Vor-, Nachnamen und das Passwort der
     * Angestellten.
     */

    private int id;
    private String firstname, lastname, password;

    /**
     * Default Konstruktor und expliziter Konstruktor.
     */

    public Employee () {}

    public Employee (int id, String firstname, String lastname, String password)
    {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    /**
     * Gibt die aktuelle ID zurück.
     *
     * @return int
     */

    public int getId() {
        return id;
    }

    /**
     * Setzt die aktuelle ID des Angestellten.
     *
     * @param id int ID
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt den aktuellen Vornamen zurück.
     *
     * @return String
     */

    public String getFirstname() {
        return firstname;
    }

    /**
     * Setzt den aktuellen Vornamen des Angestellten.
     *
     * @param firstname String Vorname
     */

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Gibt den aktuellen Nachnamen zurück.
     *
     * @return String
     */

    public String getLastname() {
        return lastname;
    }

    /**
     * Setzt den aktuellen Nachnamen des Angestellten.
     *
     * @param lastname String Vorname
     */

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Gibt das aktuelle Passwort zurück.
     *
     * @return String
     */

    public String getPassword() {
        return password;
    }

    /**
     * Setzt das aktuelle Passwort des Angestellten.
     *
     * @param password String Vorname
     */

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gibt einen String zurück der die gespeicherten Daten enthält.
     *
     * @return String
     */

    public String toString()
    {
        return "[" +id+", " +firstname+", "+lastname+", "+password+"]";
    }
}
