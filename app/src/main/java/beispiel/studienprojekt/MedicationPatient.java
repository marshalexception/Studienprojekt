package beispiel.studienprojekt;

/**
 * Diese Klasse enthält die Informationen und Methoden für die Medikamenten-Patienten-Entität.
 *
 */

public class MedicationPatient
{
    /**
     * Integer Variablen medicationpatientid, id, und dose für die Medikamenten-Patienten-ID, die
     * ID des Patienten und die Dosierung des Medikaments.
     * Strings atc, name, taking und status für die ATC-Nummer, die Einnahmezeit und den Status.
     *
     */

    private int medicationpatientid, id, dose;
    private String atc, taking, status;

    /**
     * Default Konstruktor und expliziter Konstruktor.
     */

    public MedicationPatient () {}

    public MedicationPatient (int medicationpatientid, String atc, int id, int dose, String taking, String status)
    {
        this.medicationpatientid = medicationpatientid;
        this.atc = atc;
        this.id = id;
        this.dose = dose;
        this.taking = taking;
        this.status = status;
    }

    /**
     * Setzt die ID der Medikamenten-Patienten-Entität.
     *
     * @param medicationpatientid Int Medikamenten-Patienten-ID
     */

    public void setMedicationPatientId (int medicationpatientid)
    {
        this.medicationpatientid = medicationpatientid;
    }

    /**
     * Gibt die ID der Medikamenten-Patienten-Entität zurück.
     *
     * @return Int
     */

    public int getMedicationPatientId ()
    {
        return medicationpatientid;
    }

    /**
     * Setzt die ATC-Nummer des Medikaments.
     *
     * @param atc String ATC-Nummer
     */

    public void setAtc (String atc)
    {
        this.atc = atc;
    }

    /**
     * Gibt die ATC-Nummer des Medikaments zurück.
     *
     * @return Int
     */

    public String getAtc() {
        return atc;
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
     * @param id Int ID
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt die Dosierung des Medikaments zurück.
     *
     * @return Int
     */

    public int getDose() {
        return dose;
    }

    /**
     * Setzt die Dosierung des Medikaments.
     *
     * @param dose Int Dosierung
     */

    public void setDose(int dose) {
        this.dose = dose;
    }

    /**
     * Gibt die Einnahmezeit des Medikaments zurück.
     *
     * @return String
     */

    public String getTaking() {
        return taking;
    }

    /**
     * Setzt die Einnahmezeit des Medikaments.
     *
     * @param taking String Einnahmezeit
     */

    public void setTaking(String taking) {
        this.taking = taking;
    }

    /**
     * Gibt den Status der Medikamenten-Patienten-Entität zurück.
     *
     * @return String
     */

    public String getStatus() {
        return status;
    }

    /**
     * Setzt den Status der Medikamenten-Patienten-Entität.
     *
     * @param status String Status
     */

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gibt einen String zurück der die gespeicherten Daten enthält.
     *
     * @return String
     */

    public String toString ()
    {
        return dose + "x " +  " " + taking + "\n" +
                "Status: " + status;
    }
}
