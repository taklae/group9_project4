package edu.oregonstate.cs361.battleship;

/**
 * Created by Will on 2/28/17.
 */
public class MilitaryShip extends Ship {
    public MilitaryShip(String n, int l,Coordinate s, Coordinate e) {
        super(n, l, s, e);
        stealth = true;
    }
}
