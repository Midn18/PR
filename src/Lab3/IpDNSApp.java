package Lab3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class IpDNSApp {

    public static String initialDns;

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        IpDNSApp ipDNSApp = new IpDNSApp();
        initialDns = InetAddress.getLocalHost().getHostName();

        while (true) {
            System.out.println("What do you want to do?");
            System.out.println("1. Get IP address by domain name");
            System.out.println("2. Get domain name by IP address");
            System.out.println("3. Change DNS server");
            System.out.println("4. Reset DNS server to initial value");
            System.out.println("5. Exit");
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
                        System.out.println("Enter DNS server IP address:");
                        input = scanner.nextLine();
                        if (input.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                            ipDNSApp.changeDNSServer(input);
                        }
                    }
                    break;
                case "4":
                    System.out.println("Current DNS server:");
                    ipDNSApp.showDnsServer();
                    System.out.println("You want to reset DNS server? (y/n)");
                    input = scanner.nextLine();
                    if (input.equals("y")) {
                        ipDNSApp.changeDNSServer(initialDns);
                    }
                    break;
                case "5":
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

    public void showDnsServer() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println(ip);
    }

    public void changeDNSServer(String newDNS) throws IOException, InterruptedException {
        String command = "netsh interface ip set dns \"Conexiune la rețea locală\" static " + newDNS;
        Process process = Runtime.getRuntime().exec(command);

        int exitValue = process.waitFor();
        if (exitValue == 0) {
            System.out.println("DNS was successfully changed to: " + newDNS + ".");
        } else {
            System.out.println("DNS can't be changed to: " + newDNS + ". Please try with another address");
        }
    }
}
