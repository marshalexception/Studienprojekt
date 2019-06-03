package beispiel.studienprojekt;

/**
 * Diese Klasse enthält die Informationen und Methoden für Dispenser.
 *
 */

public class Dispenser
{
    /**
     * Strings ID und Datum für den Dispenser.
     * String Array dispenser_medications enthält die MedicationPatientIDs.
     * Booleans stocked und application werden bei erfolgter Dispenserbestückung bzw.
     * Medikamentengabe auf true gesetzt.
     * strSeperator wird für die Umwandlung von Array in String bzw. String in Array benutzt.
     */

    private String dispenser_id, date;
    private String[] dispenser_medications;
    public static String strSeparator = "__,__";

    /**
     * Default Konstruktor und expliziter Konstruktor.
     */

    public Dispenser () {}

    public Dispenser (String dispenser_id, String[] dispenser_medications, String date)
    {
        this.dispenser_id = dispenser_id;
        this.dispenser_medications = dispenser_medications;
        this.date = date;
    }

    /**
     * Gibt das aktuelle Datum in Dispenser zurück.
     *
     * @return String
     */

    public String getDate() {
        return date;
    }

    /**
     * Setzt das aktuellen Datum in Dispenser.
     *
     * @param date String Datum
     */

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gibt die aktuelle Dispenser_ID in Dispenser zurück.
     *
     * @return String
     */

    public String getDispenser_Id() {
        return dispenser_id;
    }

    /**
     * Setzt die aktuelle Dispenser_ID in Dispenser
     *
     * @param dispenser_id String mit der ID
     */

    public void setDispenser_Id(String dispenser_id) {
        this.dispenser_id = dispenser_id;
    }

    /**
     * Gibt die aktuellen Medikationen in Dispenser zurück.
     *
     * @return String[]
     */

    public String[] getDispenser_Medications() {
        return dispenser_medications;
    }

    /**
     * Setzt die aktuellen Medikationen in Dispenser
     *
     * @param dispenser_medications String Array mit den Medikationen (Medication_Patient)
     */

    public void setDispenser_Medications(String[] dispenser_medications) {
        this.dispenser_medications = dispenser_medications;
    }

    /**
     * Wandelt ein String Array in ein String um.
     * Das String Array array wird durchlaufen und die Einträge in str geschrieben. Die if-Abfrage
     * dient nur dazu, dass nach dem letzten Element kein Komma eingefügt wird, sonst schon.
     *
     * @param array String Array das konvertiert werden soll
     * @return String
     */

    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }

    /**
     * Wandelt ein String in ein String Array um.
     * Der String str wird durchlaufen und bei jedem Komma (strSeperator) wird ein neuer Eintrag
     * in das Array geschrieben.
     *
     * @param str String der konvertiert werden soll
     * @return String[]
     */

    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
