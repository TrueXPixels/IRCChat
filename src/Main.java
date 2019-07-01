import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/*
v0.0.1

Currently:
 - Joins IRC server (static) [Line 21]
 - Responds to pings [Line 34]
 - Sends "Hello Channel!" on join [Line 38]

 */

public class Main {

    private static void write(BufferedWriter writer, String out) throws Exception {
        writer.write(out + "\r\n");
        writer.flush();
    }

    public static void main(String[] args) throws Exception {

        // Variables
        String  server = "irc.zoro.moe",
                nickname = "IRCClient_TrueXPixels",
                username = "txp",
                realname = "IRC Client [TrueXPixels]",
                channel  = "#bots",
                joinMsg  = "Hello Channel!";

        // User Input
        Scanner sc = new Scanner(System.in);

        System.out.print("Server Address: ");
        server = sc.next();
        System.out.print("Nickname: ");
        nickname = sc.next();
        System.out.print("Channel/User: ");
        channel = sc.next();

        sc.close();

        // Create Socket & Buffers
        Socket socket = new Socket(server, 6667);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Set Nick & User
        write(writer, "NICK " + nickname);
        write(writer, "USER " + username + " 8 * : " + realname);

        // Handle Lines
        String line = null;
        Boolean destroy = false;
        while ((line = reader.readLine()) != null || !destroy) {

            // Handle Pings
            if (line.startsWith("PING")) write(writer, "PONG :" + line.substring(6));

            // Handle Codes
            String[] content = line.split(" ");
            switch(content[1]) {
                case "PRIVMSG":
                    System.out.println("[" + content[2] + "] " + content[0].split("!")[0].split(":")[1] + ": " + line.split(":")[2]);
                    break;
                case "001":
                    System.out.println("\n" + line.split(":")[2]);
                    break;
                case "004":
                    write(writer, "JOIN " + channel + "\r\n"); // Join Channel
                    System.out.println("Connected to " + channel + "...\n");
                    break;
                case "433":
                    System.out.println("Sorry, that nickname is already in use.");
                    destroy = true;
                    break;
            }

        }

    }

}