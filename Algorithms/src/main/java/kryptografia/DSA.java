package kryptografia;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DSA {
    int keyLength;
    MessageDigest md;

    BigInteger q, p, h, g, a, b, ps1, k, r, s, ks1;
    public BigInteger[] generateKeys() {
        int randomLength = (int) ((Math.random() * 1022) + 1);
        while (randomLength % 64 != 0) {
            randomLength++;
        }
        keyLength = randomLength;
        q = BigInteger.probablePrime(160, new Random());
        BigInteger temp1, temp2;
        do {
            temp1 = BigInteger.probablePrime(keyLength, new Random());
            temp2 = temp1.subtract(BigInteger.ONE);                     //q - dzielnik p-1
            temp1 = temp1.subtract(temp2.remainder(q));                 //remainder = %
        } while (!temp1.isProbablePrime(5));
        p = temp1;
        ps1 = p.subtract(BigInteger.ONE);
        BigInteger temp3;
        do {
            h = new BigInteger(keyLength - 2, new Random());                //Wybieramy liczbę g. g - Rząd w grupie multiplikatywnej p równy q. Szukamy g która będzie odpowidać temu: (g^q mod p) = 1.
            temp3 = h.modPow(ps1.divide(q), p);                                     //https://ru.wikipedia.org/wiki/DSA#Описание_алгоритма
        } while (temp3.equals(BigInteger.ONE));
        g = temp3;
        do {
            a = new BigInteger(159, new Random());
        }while(a.compareTo(q) > 0 && a.compareTo(BigInteger.ZERO) != 1);
        b = h.modPow(a, p);
        BigInteger[] ret = {q, p, h, g, a, b};
        return ret;
    }

    public BigInteger[] sign(byte[] fileBytes){
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(fileBytes);
        BigInteger hash =new BigInteger(1, md.digest());

        do {
            k = new BigInteger(159, new Random());
            r = g.modPow(k, p).mod(q);
            s = k.modInverse(q).multiply(hash.add(a.multiply(r))).mod(q);
        }
        while (r.equals(BigInteger.ZERO) && s.equals(BigInteger.ZERO));

        return new BigInteger[]{r, s};

    }

    public boolean verify(byte[] bytes, String rS, String sS) {
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
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
        //if(v.compareTo(r)==0)return true; else return false;
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
