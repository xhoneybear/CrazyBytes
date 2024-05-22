package dev.lisek.crazybytes.server;

import java.util.Random;
import java.util.stream.Stream;

class Base56 {
    static String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRST";
    static String to(int i) {
        String ret = "";
        while (i != 0) {
            ret = base.charAt(i % 56) + ret;
            i /= 56;
        }
        return ret;
    }

    static int from(char c) {
        return base.indexOf(c);
    }
    static int from(String s) {
        int ret = 0;
        for (int i = 0; i < s.length(); i++) {
            ret += from(s.charAt(s.length() - 1 - i)) * Math.pow(56, i);
        }
        return ret;
    }
}

public class GameCode {
    public static final String create(String addr) {
        char[] codeArr = new char[6];
        int[] addrArr = Stream
            .of(addr.split("[.|:]"))
            .mapToInt(Integer::parseInt)
            .toArray();
        for (int i = 0; i < 4; i++) {
            codeArr[i] = (char) (addrArr[i] + 32);
        }
        codeArr[4] = (char) (addrArr[4] / 256 + 32);
        codeArr[5] = (char) (addrArr[4] % 256 + 32);
        String code = new String(codeArr);
        return code;
    }
    public static final String resolve(String code) {
        int[] codeArr = Stream
            .of(code.split(""))
            .mapToInt(i -> i.charAt(0) - 32)
            .toArray();
        codeArr[4] *= 256 + codeArr[5];
        String addr = "%d.%d.%d.%d:%d".formatted(
            codeArr[0],
            codeArr[1],
            codeArr[2],
            codeArr[3],
            codeArr[4]
        );
        return addr;
    }

    public static void main(String[] args) {
        Random r = new Random();
        String ip, code, ipNew;
        for (int i = 0; i < 100; i++) {
            ip = "%d.%d.%d.%d:%d".formatted(
                r.nextInt(256),
                r.nextInt(256),
                r.nextInt(256),
                r.nextInt(256),
                r.nextInt(65536)
            );
            code = create(ip);
            ipNew = resolve(code);
            System.out.println(code + ": " + ip + " - " + ipNew);
            assert ip.equals(ipNew);
        }
    }
}
//! fix port