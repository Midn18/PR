package lab3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Utils {
    public static void findIpByDomainName(String domainName) {
        InetAddress[] addresses;

        try {
            addresses = InetAddress.getAllByName(domainName);
        } catch (UnknownHostException exception) {
            System.out.println("Invalid url: " + domainName);
            throw new RuntimeException(exception);
        }
        System.out.println(Arrays.toString(addresses));
    }

    public static void findDomainNameByIp(String ip) {
        InetAddress address = null;

        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException exception) {
            System.out.println("Unknown host: " + ip);
            System.out.println(exception.getMessage());
        }
        System.out.printf("Domain name: %s\n", address.getHostName());
    }

    public static void changeDNSServer(String newDns) throws IOException, InterruptedException {
        String[] command = new String[]{"netsh", "interface", "ipv4", "set", "dns", "name=", "Ethernet", "static", newDns};
        Process process = Runtime.getRuntime().exec(command);
        int response = process.waitFor();

        if (response == 0) {
            System.out.println("DNS was successfully changed to: " + newDns + ".");
        } else {
            System.out.println("DNS can't be changed to: " + newDns + ". Please try with another address");
        }
    }
}
