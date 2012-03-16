package apankov.ironman;

import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Vector;

public class IMData {
    public Vector players = new Vector();
    public int goldTaskId;
    public int ironTaskId;
    public int totalPlayers = 0;

    public IronmanPlayer getIronmanPlayer(Player mcPlayer) {
        Iterator itr = players.iterator();
        IronmanPlayer imPlayer = null;
        IronmanPlayer temp = null;
        while(itr.hasNext()) {
            temp = (IronmanPlayer) itr.next();
            if(temp.getMCPlayer().equals(mcPlayer)) {
                imPlayer = temp;
                break;
            }
        }
        return imPlayer;
    }

    public IronmanPlayer getIronmanPlayer(String playerName) {
        Iterator itr = players.iterator();
        IronmanPlayer imPlayer = null;
        IronmanPlayer temp = null;
        while(itr.hasNext()) {
            temp = (IronmanPlayer) itr.next();
            if(temp.getMCPlayer().getName().equalsIgnoreCase(playerName)) {
                imPlayer = temp;
                break;
            }
        }
        return imPlayer;
    }

    public IronmanPlayer getIronmanPlayer(int index) {
        IronmanPlayer imPlayer = null;
        imPlayer = (IronmanPlayer) players.elementAt(index);
        return imPlayer;
    }
}
