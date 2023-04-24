package lab3;

import java.io.IOException;
import java.util.Scanner;

public class IpDNSApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("What do you want to do?");
            System.out.println("1. Get IP address by domain name");
            System.out.println("2. Get domain name by IP address");
            System.out.println("3. Change DNS server");
            System.out.println("4. Exit");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    System.out.println("Enter domain name:");
                    input = scanner.nextLine();
                    Utils.findIpByDomainName(input);
                    break;
                case "2":
                    System.out.println("Enter IP address:");
                    input = scanner.nextLine();
                    if (input.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                        Utils.findDomainNameByIp(input);
                    }
                    break;
                case "3":
                    System.out.println("You want to change/reset DNS server? (c/r/n)");
                    input = scanner.nextLine();
                    if (input.equals("c")) {
                        System.out.println("Enter DNS server IP address:");
                        input = scanner.nextLine();
                        if (input.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                            Utils.changeDNSServer(input);
                        }
                    } else if (input.equals("r")) {
                        Utils.changeDNSServer("192.168.1.1");
                    }
                    break;
                case "4":
                    System.exit(0);
                default:
                    System.out.println("Wrong input");
            }
        }
    }
}
