package apankov.ironman;

import org.bukkit.entity.Player;

public class IronmanPlayer {
    
    private Player mc_player;    
    private boolean armorChanging;
    private boolean cheaterState;
    private boolean fullAccess;
    
    public Player getMCPlayer() {
        return mc_player;
    }
    
    public boolean hasArmorChanging() {
        return armorChanging;
    }
    
    public boolean isCheater() {
        return cheaterState;
    }

    public boolean hasFullAccess() {
        return fullAccess;
    }

    public void setArmorChanging(boolean state) {
        armorChanging = state;
    }

    public void setCheaterState(boolean state) {
        cheaterState = state;
    }

    public void setFullAccess(boolean state) {
        fullAccess = state;
    }
    
    public IronmanPlayer(Player player) {
        mc_player = player;
        armorChanging = true;
        fullAccess = true;
    }
}
