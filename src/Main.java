import java.io.*;
import java.net.Socket;

/*
v0.0.1

Currently:
 - Joins IRC server (static) [Line 21]
 - Responds to pings [Line 34]
 - Sends "Hello Channel!" on join [Line 38]

 */

public class Main {

    public static void main(String[] args) throws Exception {

        // Create Socket & Buffers
        Socket socket = new Socket("irc.zoro.moe", 6667);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Set Nick & User
        writer.write("NICK IRCClient_TrueXPixels\r\n");
        writer.write("USER HelloWorld 8 * : discord.gg/plexidev\r\n");
        writer.flush();

        // Handle Lines
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("PING")) { // Respond to pings to stay connected
                writer.write("PONG :" + line.substring(6) + "\r\n");
                writer.flush();
            } else if (line.contains("004")) { // Runs on connection (Logging In)
                writer.write("JOIN #bots\r\n"); // Join Channel
                writer.write("PRIVMSG #bots :Hello Channel!\r\n"); // Send Message
                writer.flush();
            } else if (line.contains("433")) { // Runs on nickname in use, breaks
                System.out.println("That nickname is already being used!");
                break;
            }
        }

    }

}