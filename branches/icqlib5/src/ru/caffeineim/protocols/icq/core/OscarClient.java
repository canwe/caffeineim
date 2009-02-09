/**
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ru.caffeineim.protocols.icq.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.Queue;

import ru.caffeineim.protocols.icq.tool.Dumper;

/**
 * @author Fabrice Michellonet
 * @author Samolisov Pavel
 * @author Дмитрий Пролубников
 */
public class OscarClient implements Runnable {

	public static final String THREAD_NAME = "OscarClientThread";

    private OscarPacketAnalyser analyser;
    private String host;
    private int port;
    private Socket socketClient;
    private InputStream in;
    private DataOutputStream out;
    private Thread runner;
    private boolean running = true;
    private Queue<byte[]> messagesQueue;

    /**
     * This create a socket that will be connected to an Oscar Server.
     *
     * @param host The address of the server. <i>[i.e: <b>login.icq.com</b> for ICQ]</i>
     * @param port The port on which the server is awaiting connection.
     * @param analyser The instance of the analyser that should be called
     * to parse the incoming packets.
     */
    public OscarClient(String host, int port, OscarPacketAnalyser analyser) {
        this.analyser = analyser;
        this.host = host;
        this.port = port;
        runner = new Thread(this, THREAD_NAME);
        messagesQueue = new LinkedList<byte[]>();
    }

    /**
     * This function simply start the client.
     */
    public void connectToServer() {
    	runner.start();

    	// start packet handler thread
        new OscarPacketHandler(this);
    }

    /**
     * Simply stop the client.
     *
     * @throws IOException
     */
    public synchronized void disconnect() throws IOException {
        running = false;
        // TODO разобраться
        //socketClient.shutdownInput();
        //socketClient.shutdownOutput();
    }

    /**
     * This function is ran by the Thread.
     * It keeps reading the socket until 6 bytes at least are present.
     * Those 6 bytes represents the <code>packet header</code>, from which we
     * can determine the lenght of the packet.
     * Then, the thread wait for <code>packetLen</code> bytes to be present on
     * the socket, this is the <code>packet body</code>.
     * Finaly, the <code>packet header</code> and the <code>packet body</code> are merged and sent to
     * the packet analyser.
     */
    public void run() {
        byte header[] = new byte[6];
        byte packet[] = null;
        int packetLen = 0;
        boolean waitData = false;

        try {
        	// TODO рефакторинг подсистемы прокси
            if (System.getProperty("socks.proxyHost") != null) {

            	SocketAddress addr = new InetSocketAddress(
                		System.getProperty("socks.proxyHost"),
                        Integer.parseInt(System.getProperty("socks.proxyPort")));

                Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);
                socketClient = new Socket(proxy);
                InetSocketAddress dest = new InetSocketAddress(host, port);
                socketClient.connect(dest);
            } else {
                socketClient = new Socket(host, port);
            }

            out = new DataOutputStream(socketClient.getOutputStream());
            in = socketClient.getInputStream();

            while (running) {
                if (!waitData) {
                    /* waiting for at least 6 bytes */
                    if (in.available() >= 6) {
                        in.read(header, 0, 6);
                        /* retreiving flap datafield len */
                        packetLen = getPacketLen(header);
                        packet = new byte[packetLen + 6];
                        waitData = true;
                    }
                } else {
                    /* waiting for the entiere datafield */
                    if (in.available() >= packetLen) {
                        in.read(packet, 6, packetLen);
                        /* adding the header to the packet */
                        System.arraycopy(header, 0, packet, 0, 6);
                        getMessageQueue().add(packet);
                        waitData = false;
                    }
                }
                Thread.sleep(10);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Return the Inet address of the socket that is connected to
     * the Oscar Server.
     * @return The Inet address of the the socket.
     */
    public byte[] getInetaddress() {
        return socketClient.getLocalAddress().getAddress();
    }

    public Queue<byte[]> getMessageQueue() {
    	return messagesQueue;
    }

    public OscarPacketAnalyser getAnalyser() {
    	return analyser;
    }

    /**
     * <b>MUST BE MOVED TO SOME UTILS IN PACKAGE JOscarLib.Utils</b>
     * @todo MUST BE MOVED TO SOME UTILS IN PACKAGE JOscarLib.Utils
     * @param header
     * @return
     */
    private static int getPacketLen(byte[] header) {
        int result;
        result = (header[4] << 8) & 0xFF00;
        result |= header[5] & 0xFF;
        return result;
    }

    /**
     * This function gives you the possibility to send
     * packets throught the socket.
     *
     * @param packet The byte array representation of the packet to be sent.
     */
    public void sendPacket(byte[] packet) throws IOException {
    	if (analyser.isDumping()) {
    		System.out.println("Send: ");
    		System.out.println(Dumper.dump(packet, true, 8, 16));
    	}
    	out.write(packet);
    	out.flush();
    }
}