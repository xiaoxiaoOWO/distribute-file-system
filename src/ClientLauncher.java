import impl.ClientImpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class ClientLauncher {
    static final String prompt = ">> ";
    static final String prefix = "INFO: ";

    public static void main(String[] args) throws IOException {
        ClientImpl client = new ClientImpl();
        Scanner scanner = new Scanner(System.in);


        System.out.println("Enter commands (type 'exit' to quit):");

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();


            if ("exit".equalsIgnoreCase(input)) {
                break; // 退出循环
            }

            // 处理输入的指令
            processCommand(client, input);
        }

        scanner.close();
        System.out.println(prefix + "Bye!");
    }

    private static void processCommand(ClientImpl client, String command) throws IOException {
        String[] split = command.split(" ", 3);
        String cmd = split[0];

        switch (cmd) {
            case "open":
                String filename = split[1];
                String modeStr = split[2];
                int mode = 0;
                switch (modeStr){
                    case "r":
                        mode = 0b01;
                        break;
                    case "w":
                        mode = 0b10;
                        break;
                    case "rw":
                        mode = 0b11;
                        break;
                }
                int fd = client.open(filename, mode);
                if (fd == -1) {
                    System.out.println(prefix + "OPEN not allowed");
                } else {
                    System.out.println(prefix + "fd = " + fd);
                }
                break;

            case "read":
                int fdRead = Integer.parseInt(split[1]);
                byte[] bytes = client.read(fdRead);
                if (bytes == null) {
                    System.out.println(prefix + "READ not allowed");
                } else {
                    System.out.println(new String(bytes));
                }
                break;

            case "append":
                int fdAppend = Integer.parseInt(split[1]);
                String content = split[2];
                client.append(fdAppend, content.getBytes());
                System.out.println(prefix + "write down");
                break;

            case "close":
                int fdClose = Integer.parseInt(split[1]);
                client.close(fdClose);
                System.out.println(prefix + "fd " + fdClose + " closed");
                break;



            default:
                // Handle an unknown command or add a comment if nothing is needed
                break;
        }
    }

}