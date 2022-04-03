package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Calculator client implementation
 */
public class Client {

    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    /**
     * Main function to run client
     *
     * @param args no args required
     */
    public static void main(String[] args) throws IOException {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        /* TODO: Implement the client here, according to your specification
         *   The client has to do the following:
         *   - connect to the server
         *   - initialize the dialog with the server according to your specification
         *   - In a loop:
         *     - read the command from the user on stdin (already created)
         *     - send the command to the server
         *     - read the response line from the server (using BufferedReader.readLine)
         */


        String address = "127.0.0.1";
        int port = 4567;

        Socket socket = new Socket(address, port);

        try (
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
        ) {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter out = new PrintWriter(outputStream);

            String welcome = in.readLine();
            System.out.println(welcome);

            while (socket.isConnected()) {
                String input = stdin.readLine();

                out.println(input);
                out.flush();

                String response = in.readLine();
                if (response != null)
                    System.out.println(response);

                if (input.toLowerCase().equals("exit"))
                    return;
            }

            socket.close();
        }
    }
}
