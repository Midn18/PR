package Lab3;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class IpDNSApp {
    public static String INITIAL_DNS = "";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IpDNSApp ipDNSApp = new IpDNSApp();

        while (true) {
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
                    ipDNSApp.getIpByDomainName(input);
                    break;
                case "2":
                    System.out.println("Enter IP address:");
                    input = scanner.nextLine();
                    if (input.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                        ipDNSApp.getDomainNameByIp(input);
                    }
                    break;
                case "3":
                    System.out.println("Current DNS server:");
                    ipDNSApp.showDnsServer();
                    System.out.println("You want to change DNS server? (y/n)");
                    input = scanner.nextLine();
                    if (input.equals("y")) {
                        System.out.println("You want to reset DNS server? (y/n)");
                        input = scanner.nextLine();
                        if (input.equals("y")) {
                            ipDNSApp.changeDNSServer(INITIAL_DNS);
                        }
                        System.out.println("Enter DNS server IP address:");
                        input = scanner.nextLine();
                        if (input.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                            ipDNSApp.changeDNSServer(input);
                        }
                    }
                    System.out.println("Enter DNS server IP address:");
                    input = scanner.nextLine();
                    if (input.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                        ipDNSApp.changeDNSServer(input);
                    }
                    break;
                case "4":
                    System.exit(0);
                default:
                    System.out.println("Wrong input");
            }
        }
    }

    public void getIpByDomainName(String domainName) {
        InetAddress[] address;
        try {
            address = InetAddress.getAllByName(domainName);
        } catch (UnknownHostException e) {
            System.out.println("Invalid url: " + domainName);
            throw new RuntimeException(e);
        }
        System.out.println(Arrays.toString(address));
    }

    public void getDomainNameByIp(String ip) {
        InetAddress host = null;
        try {
            host = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + ip);
            System.out.println(e.getMessage());
        }
        System.out.printf("Domain name: %s\n", host.getHostName());
    }

    public void changeDNSServer(String ip) {
        System.setProperty("dns.server", ip);
    }

    public void showDnsServer() {
        INITIAL_DNS = System.getProperty("dns.server");
        System.out.println(INITIAL_DNS);
    }
}
