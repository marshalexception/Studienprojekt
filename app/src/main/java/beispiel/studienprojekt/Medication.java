package beispiel.studienprojekt;

/**
 * Diese Klasse enthält die Informationen und Methoden für die Medikamente.
 *
 */

public class Medication
{
    /**
     * Strings medatc, name, unit und roamoa für die ATC-Nummer, den Namen, die Einheit und die
     * Einnahmeart des Medikaments.
     */

    protected String medatc, name, unit, roamoa;

    /**
     * Default Konstruktor und expliziter Konstruktor.
     */

    public Medication () {}

    public Medication (String atc, String name, String unit, String roamoa)
    {
        this.medatc = atc;
        this.name = name;
        this.unit = unit;
        this.roamoa = roamoa;
    }

    /**
     * Gibt die ATC-Nummer des Medikaments zurück.
     *
     * @return String
     */

    public String getAtc() {
        return medatc;
    }

    /**
     * Setzt die ATC-Nummer des Medikaments.
     *
     * @param atc String ATC-Nummer
     */

    public void setAtc(String atc) {
        this.medatc = atc;
    }

    /**
     * Gibt den Namen des Medikaments zurück.
     *
     * @return String
     */

    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Medikaments.
     *
     * @param name String Namen
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die Einheit des Medikaments zurück.
     *
     * @return String
     */

    public String getUnit() {
        return unit;
    }

    /**
     * Setzt die Einheit des Medikaments.
     *
     * @param unit String Einheit
     */

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Gibt die Einnahmeart des Medikaments zurück.
     *
     * @return String
     */

    public String getRoamoa() {
        return roamoa;
    }

    /**
     * Setzt die Einnahmeart des Medikaments.
     *
     * @param roamoa String Einnahmeart
     */

    public void setRoamoa(String roamoa) {
        this.roamoa = roamoa;
    }

    /**
     * Gibt einen String zurück der die gespeicherten Daten enthält.
     *
     * @return String
     */

    public String toString ()
    {
        return this.medatc + " " + this.name + " " + this.unit + " " + this.roamoa;
    }
}
