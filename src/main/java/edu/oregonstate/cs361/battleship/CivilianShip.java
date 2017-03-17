package edu.oregonstate.cs361.battleship;

/**
 * Created by fbolanos on 2/28/2017.
 */
public class CivilianShip extends Ship {
    public CivilianShip(String n, int l,Coordinate s, Coordinate e) {
        super(n, l, s, e);
    }

    //Function "getAmorless" is only in the CivilianShip class to follow OO principles.
    public String getArmorless() {
        return "armorless";
    }
}
