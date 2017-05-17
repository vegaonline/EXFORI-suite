/*
 * EXFOR-I an EXFOR EDITOR for NRDC, IAEA
 * Developed by Dr. Abhijit Bhattacharyya
 *  Nuclear Data Physics Centre of India (NDPCI)
 * Nuclear Physics Division, Bhabha Atomic Research Centre
 *  Mumbai, INDIA
 */
package ExforI;

/**
 * This contains all utilities developed for this software
 *
 * @author vega
 */
public class exforUtil {

    /*
     * This code checks if a string is a number or not.
     */
    /**
     *
     * @param str which is tested to be whether Numeric or not
     * @return it returns a boolean which is true if Numeric input
     */
    public static boolean isNumeric(String str) {
        if ( str.length () == 0 ) {
            return false;
        }
        for ( char c : str.toCharArray () ) {
            if ( !Character.isDigit (c) &&
                    c != '.' && c != 'e' && c != 'E' && c != '+' && c != '-' &&
                    c != '<' && c != '>' && c != '~' ) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlpha(String str) {
        for ( char c : str.toCharArray () ) {
            if ( !Character.isAlphabetic (c) ) {
                return false;
            }
        }
        return true;
    }

    public static String getI2S(int i, int val) {
        String finalStr = null;

        if ( i < 10 ) {
            if ( val == 0 ) {
                finalStr = "00" + Integer.toString (i);
            } else {
                finalStr = " " + Integer.toString (i);
            }
        } else if ( i >
                9 && i < 100 ) {
            if ( val == 0 ) {
                finalStr = "0" +
                        Integer.toString (i);
            } else {
                finalStr = " " + Integer.toString (i);
            }
        } else {
            finalStr = Integer.toString (i);
        }
        return finalStr;
    }

    public static String fixStr0s(String sIn, int len) {
        String sOut = "";
        int lenN1N2 = len - sIn.length ();
        sIn = sIn.trim ();
        sOut = (len > sIn.length ())
                ? String.format ("%0" + lenN1N2 + "d%s", 0, sIn)
                : sIn;
        return sOut;
    }

    public static String fixString11(String sIn) {
        int lenN1N2 = 11;
        String sOut = "";
        sIn = sIn.trim ();
        if ( !sIn.isEmpty () ) {
            sOut = String.format ("%1$" + lenN1N2 + "s", sIn);
        } else {
            sOut = String.format ("%1$" + lenN1N2 + "s", " ");
        }
        return sOut;
    }

    public static String fixString11(String sIn, int len) {
        int lenN1N2 = len;
        //int diff = 0;
        String sOut = "";
        sIn = sIn.trim ();
        if ( !sIn.isEmpty () ) {
            sOut = String.format ("%1$" + lenN1N2 + "s", sIn);
        } else {
            sOut = String.format ("%1$" + lenN1N2 + "s", " ");
        }
        return sOut;
    }

    public static String fixString11(String sIn, boolean left) {
        int lenN1N2 = 10;

        String sOut = "";
        sIn = sIn.trim ();
        if ( !sIn.isEmpty () && left ) {
            sOut = String.format ("%1$-" + lenN1N2 + "s", sIn);
        } else {
            sOut = String.format ("%1$-" + lenN1N2 + "s", " ");
        }
        return sOut;
    }

    public static String fixString11(String sIn, int len, boolean left) {
        int lenN1N2 = len;
        String sOut = "";
        sIn = sIn.trim ();
        if ( !sIn.isEmpty () && left ) {
            sOut = String.format ("%1$-" + lenN1N2 + "s", sIn);
        } else {
            sOut = String.format ("%1$-" + lenN1N2 + "s", " ");
        }
        return sOut;
    }

    public static String fixGet(String s1) {
        String s2 = "";
        if ( s1 != null && !s1.contains ("Please") ) {
            if ( s1.contains ("-") ) {
                if ( s1.startsWith (" ") ) {
                    s2 = s1.substring (s1.indexOf (" ") + 1, 12);
                } else {
                    s2 = s1.substring (0, 12);
                }
            } else {
                s2 = s1.substring (0, s1.indexOf (" "));
            }
        } else {
            s2 = "";
        }
        s2 = s2.replaceAll ("\\s+", "");
        return s2;
    }

}
