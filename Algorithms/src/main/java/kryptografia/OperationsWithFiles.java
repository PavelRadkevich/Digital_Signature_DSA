package kryptografia;

import java.io.*;
import java.math.BigInteger;

public class OperationsWithFiles {

    public static byte[] readFileBytesDSA(String sciezka) throws FileNotFoundException {
        try(FileInputStream fis = new FileInputStream(sciezka);
        ) {
            int rozmiar = fis.available();
            byte[] result = new byte[rozmiar];
            fis.read(result);
            fis.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static BigInteger[] readFileBigIntegerDSA(String sciezka) {
        try(FileInputStream fis = new FileInputStream(sciezka);
        ) {
            int rozmiar = fis.available();
            byte[] result = new byte[rozmiar];
            fis.read(result);
            fis.close();
            String tab[]=(new String(result)).split(" ");
            return new BigInteger[]{new BigInteger(tab[0]), new BigInteger(tab[1])};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeFileDSA(String sciezka, byte[] dane){
        try (FileOutputStream fos = new FileOutputStream(sciezka);){
            fos.write(dane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String bytesToHex(byte[] wiadomosc) {
        byte[] text = wiadomosc;
        StringBuilder hexTekst = new StringBuilder();
        String tempHex = null;
        int HexLength = 0;
        for (int i = 0; i < text.length; i++) {
            int pos = text[i] & 0x000000FF;
            tempHex = Integer.toHexString(pos);
            HexLength = tempHex.length();
            while (HexLength < 2) {
                hexTekst.append("0");
                HexLength++;
            }
            hexTekst.append(tempHex);
        }
        return hexTekst.toString();
    }

    public static byte[] hexToBytes(String wiadomosc) {
        if (wiadomosc.length() < 2) {
            return null;
        }
        if (wiadomosc.length() % 2 != 0) {
            wiadomosc += "0";
        }
        int dlugosc = wiadomosc.length() / 2;
        byte[] result = new byte[dlugosc];
        for (int i = 0; i < dlugosc; i++) {
            result[i] = (byte) Integer.parseInt(wiadomosc.substring(i * 2, i * 2 + 2), 16);
        }
        return result;
    }
}
