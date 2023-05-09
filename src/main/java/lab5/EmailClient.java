package lab5;

import java.util.Scanner;

import static lab5.EmailActions.*;

public class EmailClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String option;
        System.out.println("This is email client");
        while (true) {
            System.out.println("1. Operate emails using IMAP");
            System.out.println("2. Operate emails using POP3");
            System.out.println("0. Exit");
            option = scanner.nextLine();
            switch (option) {
                case "1":
                    while (true) {
                        System.out.println("1. Display emails");
                        System.out.println("2. Download email with attachment");
                        System.out.println("3. Send email");
                        System.out.println("4. Send email with attachment");
                        System.out.println("0. Exit");
                        option = scanner.nextLine();
                        switch (option) {
                            case "1":
                                getMailList(IMAP, USER, PASSWORD);
                                break;
                            case "2":
                                System.out.println("Enter the message index:");
                                int messageIndex = Integer.parseInt(scanner.nextLine()) - 1;
                                downloadEmailWithAttachment(IMAP, USER, PASSWORD, messageIndex);
                                break;
                            case "3":
                                System.out.println("To:");
                                String to = scanner.nextLine();
                                System.out.println("Subject:");
                                String subject = scanner.nextLine();
                                System.out.println("Text:");
                                String text = scanner.nextLine();
                                sendTextEmail(SMTP, USER, PASSWORD, to, subject, text);
                                break;
                            case "4":
                                System.out.println("To:");
                                to = scanner.nextLine();
                                System.out.println("Subject:");
                                subject = scanner.nextLine();
                                System.out.println("Text:");
                                text = scanner.nextLine();
                                sendEmailsWithAttachments(SMTP, USER, PASSWORD, to, subject, text);
                                break;
                            case "0":
                                break;
                            default:
                                System.out.println("Invalid option");
                        }
                    }
                case "2":
                    while (true) {
                        System.out.println("1. Display emails");
                        System.out.println("2. Download email with attachment");
                        System.out.println("3. Send email");
                        System.out.println("4. Send email with attachment");
                        System.out.println("0. Exit");
                        option = scanner.nextLine();
                        switch (option) {
                            case "1":
                                getMailList(POP3, USER, PASSWORD);
                                break;
                            case "2":
                                System.out.println("Enter the message index:");
                                int messageIndex = Integer.parseInt(scanner.nextLine()) - 1;
                                downloadEmailWithAttachment(POP3, USER, PASSWORD, messageIndex);
                                break;
                            case "3":
                                System.out.println("To:");
                                String to = scanner.nextLine();
                                System.out.println("Subject:");
                                String subject = scanner.nextLine();
                                System.out.println("Text:");
                                String text = scanner.nextLine();
                                sendTextEmail(SMTP, USER, PASSWORD, to, subject, text);
                                break;
                            case "4":
                                System.out.println("To:");
                                to = scanner.nextLine();
                                System.out.println("Subject:");
                                subject = scanner.nextLine();
                                System.out.println("Text:");
                                text = scanner.nextLine();
                                sendEmailsWithAttachments(SMTP, USER, PASSWORD, to, subject, text);
                                break;
                            case "0":
                                break;
                            default:
                                System.out.println("Invalid option");
                        }
                    }
                case "0":
                    System.exit(0);
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
