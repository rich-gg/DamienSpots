package gg.rich.damienspots.sketch;
import processing.core.PApplet;

/**
 * Created by Rich on 10/03/2016.
 */
public class Stripe {
    PApplet p5;
    //
    float x;
    float speed;
    float w;
    boolean mouse;

    Stripe(PApplet p){
        p5 = p;
        //
        x = 0;
        speed = p5.random(1);
        w = p5.random(10, 30);
        mouse = false;
    }

    void display() {
        p5.fill(255, 100);
        p5.noStroke();
        p5.rect(x, 0, w, p5.height);
    }
    void move(){
        x+= speed;
        if(x > p5.width + 20){
            x = -20;
        }
    }
}
