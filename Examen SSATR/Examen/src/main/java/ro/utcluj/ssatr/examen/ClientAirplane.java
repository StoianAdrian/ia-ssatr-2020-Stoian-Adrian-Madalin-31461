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
import java.util.StringTokenizer;
import static java.lang.Integer.parseInt;
import org.apache.commons.lang3.RandomStringUtils;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author lenovo
 */
public class ClientAirplane extends Thread {
    BufferedReader fluxIn;
    PrintWriter fluxOut;


    public ClientAirplane() throws IOException {
        Socket s = new Socket("127.0.0.1", 7600);
        fluxIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
        fluxOut = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
    }

    private Airplane initAirplane() {
        int NumberAirplane = parseInt(RandomStringUtils.randomNumeric(3));
 
        return new Airplane(NumberAirplane);
    }
    private int getAirplaneString(Airplane Air) {
        return Air.getNumber();
    }

    public void run()
    {
        while (true) {
            String line;
            try {
                Airplane Air = initAirplane();
                int Airplane = getAirplaneString(Air);
                fluxOut.println(Airplane);
                line = fluxIn.readLine();
                StringTokenizer st = new StringTokenizer(line);
                String LandingTime = st.nextToken();
                String Piste = st.nextToken();
               
                if (parseInt(LandingTime) / 500 != 0) {
                    System.out.println( Air.getNumber() + " a aterizat pe " + Piste + "! Acesta va stationa " + parseInt(LandingTime) / 500 + " minute!");
                } else {
                    System.out.println( Air.getNumber() + " nu va ateriza");
                }
                try {
                    Thread.sleep(parseInt(LandingTime));
                    if (parseInt(LandingTime) / 500 != 0) {
                        System.out.println(Air.getNumber() + " a decolat");
                    } else {
                        System.out.println(Air.getNumber() + " nu a aterizat!");
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientAirplane.class.getName()).log(Level.SEVERE, null, ex);
                }
                fluxOut.println("Decolare de la pista " + Piste);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientAirplane.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientAirplane.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }

    public void sendMessage(String msg) {
        fluxOut.println(msg);
    }

   public static void main(String[] args) throws IOException {
    ClientAirplane Plane = new ClientAirplane();
    Plane.start();
   
   
   }
}


