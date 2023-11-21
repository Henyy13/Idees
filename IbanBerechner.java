import java.util.Scanner;

public class IbanBerechnen {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String laenderkennung = sc.next();
        String blz = sc.next();
        String nummer = sc.next();
        String iban = erzeugeIban(laenderkennung, blz, nummer);
        System.out.println(iban);
    }

    public static String erzeugeIban(String laenderkennung, String blz, String nummer) {
        laenderkennung = normLaenderkennung(laenderkennung);
        String bban = erstelleBBAN(blz, nummer);
        String countryCodeNumber = konvertLC(laenderkennung);
        String ibanZwischenergebnis = bban + countryCodeNumber + "00";
        int modulo97 = berechneModulo97(ibanZwischenergebnis);
        String pruefziffer = berechnePruefziffer(modulo97);
        return laenderkennung + pruefziffer + bban;
    }

    public static String normLaenderkennung(String laenderkennung) {
        return laenderkennung.toUpperCase();
    }

    public static String erstelleBBAN(String bankleitzahl, String kontonummer) {
        String normKontonummer = normKontonummer(kontonummer);
        String bban = bankleitzahl + normKontonummer;
        while (bban.length() < 16) {
            bban += "0";
        }
        return bban;
    }

    public static String normKontonummer(String kontonummer) {
        return String.format("%010d", Long.parseLong(kontonummer));
    }

    public static String konvertLC(String laendercode) {
        StringBuilder nummer = new StringBuilder();
        for (int i = 0; i < laendercode.length(); i++) {
            char c = laendercode.charAt(i);
            int n = c - 'A' + 10;
            nummer.append(n);
        }
        return nummer.toString();
    }

    public static int berechneModulo97(String bban) {
        int modulo = 0;
        for (int i = 0; i < bban.length(); i += 7) {
            String teil = bban.substring(i, Math.min(i + 7, bban.length()));
            modulo = Integer.parseInt(modulo + teil) % 97;
        }
        return modulo;
    }

    public static String berechnePruefziffer(int modulo97) {
        int pruefziffer = (98 - modulo97);
        return String.format("%02d", pruefziffer);
    }
}
