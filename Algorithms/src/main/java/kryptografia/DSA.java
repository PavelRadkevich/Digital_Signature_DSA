package kryptografia;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECField;
import java.util.Random;

public class DSA {
    int keyLength;
    MessageDigest md;

    BigInteger q, p, h, g, a, b, ps1, k, r, s, ks1;

    Random random = new Random(1);

    public BigInteger[] generateKeys() {
        int randomLength = (int) ((Math.random() * 512) + 512);
        while (randomLength % 64 != 0) {
            randomLength++;
            if (randomLength > 1024 || randomLength <= 512) {
                randomLength = (int) ((Math.random() * 512) + 512);
            }
        }
        keyLength = randomLength;
        q = BigInteger.probablePrime(160, random);
        BigInteger temp1, temp2;
        do {
            temp1 = BigInteger.probablePrime(keyLength, random);
            temp2 = temp1.subtract(BigInteger.ONE);                     //q - dzielnik p-1
            temp1 = temp1.subtract(temp2.remainder(q));                 //remainder = %
        } while (!temp1.isProbablePrime(5));
        p = temp1;
        ps1 = p.subtract(BigInteger.ONE);
        BigInteger temp3;
        do {
            h = new BigInteger(keyLength - 1, random);                //Wybieramy liczbę g. g - Rząd w grupie multiplikatywnej p równy q. Szukamy g która będzie odpowidać temu: (g^q mod p) = 1.
            temp3 = h.modPow(ps1.divide(q), p);                                     //https://ru.wikipedia.org/wiki/DSA#Описание_алгоритма
        } while (temp3.equals(BigInteger.ONE));
        g = temp3;
        do {
            a = new BigInteger(159, random);
        }while(a.compareTo(q) > 0 && a.compareTo(BigInteger.ZERO) != 1);
        b = g.modPow(a, p);
        BigInteger[] ret = {q, p, h, g, a, b};
        return ret;
    }

    public BigInteger[] sign(byte[] fileBytes){
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(fileBytes);
        BigInteger hash =new BigInteger(1, md.digest());

        do {
            k = new BigInteger(158, new Random());
            r = g.modPow(k, p).mod(q);
            s = BigInteger.ZERO;
        }
        while (r.equals(BigInteger.ZERO));
        while(s.equals(BigInteger.ZERO)) {
            s = k.modInverse(q).multiply(hash.add(a.multiply(r))).mod(q);
            k = new BigInteger(158, new Random());
        }

        return new BigInteger[]{r, s};

    }

    public boolean verify(byte[] bytes, String rS, String sS) {
        try {
        md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
        }
        md.update(bytes);
        BigInteger hash =new BigInteger(1, md.digest());
        BigInteger r = new BigInteger(rS);
        BigInteger s = new BigInteger(sS);

        BigInteger w = s.modInverse(q);
        BigInteger u1 = hash.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = g.modPow(u1, p).multiply(b.modPow(u2, p)).mod(p).mod(q);
        return v.equals(r);
    }

    public void setQ(BigInteger q) {
        this.q = q;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    public void setG(BigInteger g) {
        this.g = g;
    }

    public void setA(BigInteger a) {
        this.a = a;
    }

    public void setB(BigInteger b) {
        this.b = b;
    }
}
