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

import ru.caffeineim.protocols.icq.core.exceptions.LoginException;
import ru.caffeineim.protocols.icq.packet.received.AuthorizationReply;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPackedRegistry;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;
import ru.caffeineim.protocols.icq.packet.sent.generic.AuthorizationRequest;
import ru.caffeineim.protocols.icq.packet.sent.generic.SignonCommand;
import ru.caffeineim.protocols.icq.tool.Dumper;

/**
 * <p>Created by
 *   @author Fabrice Michellonet
 */
public class OscarPacketAnalyser {
    private OscarConnection connection;
    private ReceivedPackedClassLoader receivedClassLoader;
    private int nbPacket = 0;
    private boolean debug = false;
    private boolean dump = false;

    public OscarPacketAnalyser(OscarConnection connection) {
        this.connection = connection;
        receivedClassLoader = new ReceivedPackedClassLoader(new ReceivedPackedRegistry());
    }

    /**
     * The function is where we manage the received packets. Recognize them,
     * analyse and give an answer to the server.
     *
     * @param packet The byte array received from the ICQ server.
     * @throws LoginException
     */
    protected void handlePacket(byte[] packet) throws LoginException {
        nbPacket++;
        boolean handled;
        try {
            handled = handleInitialPackets(packet);
        } catch (LoginException E) {
            throw new LoginException(E.getErrorType(), E);
        }

        // "normal" service
        if (!handled) {
            try {
                handleService(packet);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("failed to service packet (continuing with other packets)");
            }
        }
    }

    private boolean handleInitialPackets(byte[] packet) throws LoginException {
        if (nbPacket == 1) {
            connection.sendFlap(new AuthorizationRequest(connection.getUserId(), connection.getPassword()));
        } else if (nbPacket == 2) {
            AuthorizationReply reply = new AuthorizationReply(packet);
            reply.execute(connection);
        } else if (nbPacket == 3) {
            /* BOS hello */
            connection.sendFlap(new SignonCommand(connection.getCookie()));
        } else {
            return false;
        }
        return true;
    }

    protected void handleService(byte[] packet) throws Exception {
        ReceivedPacket receivedFlap = new ReceivedPacket(packet, true);
        int familyId = receivedFlap.getSnac().getFamilyId();
        int subTypeId = receivedFlap.getSnac().getSubTypeId();

        if (debug)
            System.out.println("Received " + familyId + " - " + subTypeId);

        if (dump)
            System.out.println(Dumper.dump(packet, true, 8, 16));

        Class loadedClass = receivedClassLoader.loadClass(familyId, subTypeId);
        if (loadedClass != null) {
            Class[] constructorParam = new Class[] {packet.getClass()};
            Object[] param = new Object[] {packet};

            try {
                receivedFlap = (ReceivedPacket) loadedClass.getConstructor(constructorParam).newInstance(param);

                receivedFlap.execute(connection);
                receivedFlap.notifyEvent(connection);

                if (debug) {
                    System.out.println("--> Loaded class: " + loadedClass.getName());
                    System.out.println("--> Executed method: " + receivedFlap.getClass().getName() + ".execute(connection)");
                }

                packet = null; // clean packet byte array
            }
            catch (Exception ex) {
                System.out.println("Could not parse packet!");
                ex.printStackTrace();
            }
        }

        receivedFlap.matchRequest(connection);

        // if logged to the Server
        if (familyId == 9 && subTypeId == 3) {
            connection.setAuthorized(true);
        }
    }

    /**
     * Set or unset the debug mode.
     *
     * @param state True to enable debugging, false otherwise.
     */
    public void setDebug(boolean state) {
        debug = state;
    }

    /**
     * Set or unset the dumping mode.
     *
     * @param state True to enable packet dumping, false otherwise.
     */
    public void setDump(boolean state) {
        dump = state;
    }

    /**
     * This function give the debugging mode status.
     *
     * @return True if the debugging mode is enabled false in the other case.
     */
    public boolean isDebugging() {
        return debug;
    }

    /**
     * This function give the dumping mode status.
     *
     * @return True if the dumping mode is enabled false in the other case.
     */
    public boolean isDumping() {
        return dump;
    }

    /**
     * This method gives access to the OscarConnection class.
     * @return The filtering engine.
     */
    public OscarConnection getConnection() {
        return connection;
    }
}
