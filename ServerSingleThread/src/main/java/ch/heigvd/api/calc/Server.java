package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator server implementation - single threaded
 */
public class Server {

    protected static class NNumber {
        private final Number number;
        private boolean isDouble = false;

        public NNumber(Number number, boolean isDouble) {
            this.number = number;
            this.isDouble = isDouble;
        }

        public NNumber(String number) {
            Number n;
            try {
                n = Integer.valueOf(number);
            } catch (Exception ignored) {
                // Let error
                n = Double.valueOf(number);
                this.isDouble = true;
            }

            this.number = n;
        }
    }

    private final static Logger LOG = Logger.getLogger(Server.class.getName());
    private final static int PORT = 4567;

    /**
     * Main function to start the server
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        (new Server()).start();
    }

    /**
     * Start the server on a listening socket.
     */
    private void start() {
        /* TODO: implement the receptionist server here.
         *  The receptionist just creates a server socket and accepts new client connections.
         *  For a new client connection, the actual work is done by the handleClient method below.
         */

        ServerSocket server = null;
        try {
            server = new ServerSocket(Server.PORT);

            while (true) {
                try (Socket client = server.accept()) {
                    this.handleClient(client);
                }
            }
        } catch (Exception exception) {
            LOG.log(Level.SEVERE, null, exception);

            try {
                if (server != null)
                    server.close();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Handle a single client connection: receive commands and send back the result.
     *
     * @param clientSocket with the connection with the individual client.
     */
    private void handleClient(Socket socket) throws IOException {
        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */

        try (
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
        ) {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter out = new PrintWriter(outputStream);

            out.println("Welcome! To request a computation, send \"[Operator] [Operand1] [Operand2]\". To get more information, send \"help\".");
            out.flush();

            while (true) {
                String line = in.readLine();

                if (line == null)
                    return;

                if (line.toLowerCase().equals("exit")) {
                    out.println("Goodbye!");
                    out.flush();
                    return;
                }

                String[] instructions = line.split(" ");

                if (instructions.length != 3) {
                    out.println("error-Invalid request");
                    out.flush();
                    continue;
                }

                String operation = instructions[0];
                NNumber n1, n2;

                try {
                    n1 = new NNumber(instructions[1]);
                } catch (Exception ignored) {
                    out.println("error-Invalid operand 1");
                    out.flush();
                    continue;
                }

                try {
                    n2 = new NNumber(instructions[2]);
                } catch (Exception ignored) {
                    out.println("error-Invalid operand 2");
                    out.flush();
                    continue;
                }


                Number result = null;
                switch (operation) {
                    case "+":
                        result = (n1.isDouble ? n1.number.doubleValue() : n1.number.intValue())
                            + (n2.isDouble ? n2.number.doubleValue() : n2.number.intValue());
                        break;
                    case "-":
                        result = (n1.isDouble ? n1.number.doubleValue() : n1.number.intValue())
                            - (n2.isDouble ? n2.number.doubleValue() : n2.number.intValue());
                        break;
                    case "*":
                        result = (n1.isDouble ? n1.number.doubleValue() : n1.number.intValue())
                            * (n2.isDouble ? n2.number.doubleValue() : n2.number.intValue());
                        break;
                    case "/":
                        // TODO: / 0
                        result = (n1.isDouble ? n1.number.doubleValue() : n1.number.intValue())
                            / (n2.isDouble ? n2.number.doubleValue() : n2.number.intValue());
                        break;
                    default:
                        out.println("error-Invalid operator");
                        out.flush();
                        break;
                }

                NNumber re = new NNumber(result, n1.isDouble || n2.isDouble);
                out.printf(
                    "%s %s %s = %s%n",
                    instructions[1],
                    instructions[0],
                    instructions[2],
                    re.isDouble ? String.valueOf(re.number.doubleValue()) : String.valueOf(re.number.intValue())
                );
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}