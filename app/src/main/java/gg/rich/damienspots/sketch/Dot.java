package gg.rich.damienspots.sketch;
import gg.rich.damienspots.DamienDots_Main;
import processing.core.*;

/**
 * Created by Rich on 10/03/2016.
 * WTF ?
 */
public class Dot {
    DamienDots_Main p5;
    ////////////////////////////////////////////////////////////class variables
    int x, y;
    PVector dotLoc;
    int col;
    float pSize;
    int index;

    /////////////////////////////////////////////////////////////constructor
    public Dot(DamienDots_Main p, int _x, int _y, float _pSize, int _col, int _index){
        p5 = p;
        //
        x = _x;
        y = _y;
        dotLoc = new PVector(x, y);
        pSize = _pSize;
        col = _col;
        index = _index;
    }


    /////////////////////////////////////////////////////////////method
    public void run() {
        p5.fill(col);
        p5.ellipse(x, y, pSize, pSize);
        isEaten();
    }

    public boolean isEaten() {

        if (p5.dist(x, y, p5.pacX, p5.pacY) < 10) {
            return true;
        } else {
            return false;
        }
    }

    public PVector getPos(){
        PVector dotPos = new PVector(x, y);
        return dotPos;
    }

    int getIndex(){
        return index;
    }
}
