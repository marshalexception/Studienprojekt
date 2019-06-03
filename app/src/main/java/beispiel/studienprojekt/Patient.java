package beispiel.studienprojekt;

/**
 * Diese Klasse enthält die Informationen und Methoden für die Patienten.
 *
 */

public class Patient
{
    /**
     * Integer Variablen id, age, room und bed für die Patienten-ID, das Alter, Zimmer und Bett
     * des Patienten.
     * Strings firstname, lastname und sex für den Vornamen, Nachnamen und das Geschlecht des
     * Patienten.
     */

    private int id, age, room, bed;
    private String firstname, lastname, sex;

    /**
     * Default Konstruktor und expliziter Konstruktor.
     */

    public Patient () {}

    public Patient (int id, String firstname, String lastname, String sex, int age, int room, int bed)
    {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.age = age;
        this.room = room;
        this.bed = bed;
    }

    /**
     * Gibt die ID des Patienten zurück.
     *
     * @return Int
     */

    public int getId() {
        return id;
    }

    /**
     * Setzt die ID des Patienten.
     *
     * @param id Integer ID
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt das Alter des Patienten zurück.
     *
     * @return Int
     */

    public int getAge() {
        return age;
    }

    /**
     * Setzt das Alter des Patienten.
     *
     * @param age Integer Alter
     */

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gibt das Zimmer des Patienten zurück.
     *
     * @return Int
     */

    public int getRoom() {
        return room;
    }

    /**
     * Setzt das Zimmer des Patienten.
     *
     * @param room Integer Zimmer
     */

    public void setRoom(int room) {
        this.room = room;
    }

    /**
     * Gibt das Bett des Patienten zurück.
     *
     * @return Int
     */

    public int getBed() {
        return bed;
    }

    /**
     * Setzt das Bett des Patienten.
     *
     * @param bed Integer Bett
     */

    public void setBed(int bed) {
        this.bed = bed;
    }

    /**
     * Gibt den Vornamen des Patienten zurück.
     *
     * @return String
     */

    public String getFirstname() {
        return firstname;
    }

    /**
     * Setzt den Vornamen des Patienten.
     *
     * @param firstname String Vorname
     */

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Gibt den Nachnamen des Patienten zurück.
     *
     * @return String
     */

    public String getLastname() {
        return lastname;
    }

    /**
     * Setzt den Nachnamen des Patienten.
     *
     * @param lastname String Nachname
     */

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Gibt das Geschlecht des Patienten zurück.
     *
     * @return String
     */

    public String getSex() {
        return sex;
    }

    /**
     * Setzt das Geschlecht des Patienten.
     *
     * @param sex String Geschlecht
     */

    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Gibt einen String zurück der die gespeicherten Daten enthält.
     *
     * @return String
     */

    public String toString ()
    {
        return firstname + " " + lastname + " (" + age + ", " + sex + "), Zimmer: "
                + room + ", Bett: " + bed;
    }
}
