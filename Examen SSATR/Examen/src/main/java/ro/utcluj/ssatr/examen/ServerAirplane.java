/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.utcluj.ssatr.examen;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *  
 * @author lenovo
 */
public class ServerAirplane {
    ArrayList<ClientHandler> clients = new ArrayList<>();
    List<String> PisteLibere = Collections.synchronizedList(new ArrayList(Arrays.asList("Pista1", "Pista2")));
    List<String> PisteOcupate = Collections.synchronizedList(new ArrayList<String>());
    AirportJFrame Interfata;
    
public void initInterfata(AirportJFrame Interfata) {
        this.Interfata = Interfata;
    }

    public void startServer() {

        ServerSocket server = null;
        try {
            server = new ServerSocket(7600);
        } catch (IOException ex) {
            Logger.getLogger(ServerAirplane.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            try {
                System.out.println("Asteapta avioane (client)...");
                Socket s;
                s = server.accept();
                System.out.println("Avionul detectat! (Client connected!)");
                ClientHandler h = new ClientHandler(s, this);
                h.start();
                clients.add(h);
            } catch (IOException ex) {
                Logger.getLogger(ServerAirplane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//Server

    public static void main(String[] args) {
        ServerAirplane server = new ServerAirplane();
        AirportJFrame Interfata = new AirportJFrame();
        server.initInterfata(Interfata);
        Interfata.setVisible(true);
        server.startServer();
      
    }

    public String PistaOcupata(String Airplane) {
        String Piste = PisteLibere.get(0);
        PisteLibere.removeIf(PisteLibere -> PisteLibere.equals(Piste));
        PisteOcupate.add(Piste);
        Interfata.setBussy(Piste, "Ocupata");
        Interfata.Plane(Piste, "Ocupata", Airplane);
        return Piste;
      
    } 

    void PistaLibera(String Piste) {
        PisteOcupate.remove(Piste);
        PisteLibere.add(Piste);
        Interfata.setBussy(Piste, "Libera");
        Interfata.Plane(Piste, "Libera", "");
   
    }
}


class ClientHandler extends Thread {

    Socket s;
    BufferedReader fluxIn;
    PrintWriter fluxOut;
    ServerAirplane server;

    public ClientHandler(Socket s, ServerAirplane server) throws IOException {
        this.s = s;
        fluxIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
        fluxOut = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
        this.server = server;
    }

    private Airplane getAirplane(String Airplane, String LandingTime) {
        StringTokenizer st = new StringTokenizer(Airplane);
        int number = Integer.parseInt(st.nextToken());
        return new Airplane(number, parseInt(LandingTime));
    }

    public void run() {
        try {
            while (true) {

                String msg = fluxIn.readLine();
                String LandingTime = RandomStringUtils.randomNumeric(4);
                Airplane a = getAirplane(msg, LandingTime);
                String Piste = server.PistaOcupata(String.valueOf(a.getNumber()));
                System.out.println("Avionul a aterizat: " + msg);
                System.out.println("Avionul " + a.getNumber() + " este pe pista " + Piste + " pentru " + Integer.parseInt(LandingTime) / 1000 + " minutes (" + LandingTime + " seconds)");
                String response = LandingTime + " " + Piste;
                sendMessage(response);
                msg = fluxIn.readLine();
                server.PistaLibera(Piste);
            }

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void sendMessage(String msg) {
        fluxOut.println(msg);
    }
}
