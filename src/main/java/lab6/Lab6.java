package lab6;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Lab6 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceți zona geografică (GMT+X sau GMT-X): ");
        String zona = scanner.nextLine();
        scanner.close();

        int offset = Integer.parseInt(zona.substring(4));
        if (zona.charAt(3) == '-') {
            offset = -offset;
        }

        LocalDateTime oraGMT = LocalDateTime.now(ZoneId.ofOffset("GMT", ZoneOffset.ofHours(offset)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String oraFormata = oraGMT.format(formatter);

        System.out.println("Ora în zona " + zona + " este: " + oraFormata);
    }
}

