package com.example.a41752347.planegame;

import android.graphics.Rect;

import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCPoint;

import java.util.Random;

public class AvionEnemigo {
    Sprite avionenemigo;
    CCPoint PosicionInicial;
    CCPoint PosicionFinal;
    Rect avenColision;
    Random rand;

    public AvionEnemigo(int x, int y) {
        avionenemigo = Sprite.sprite("AvionEnemigo.png");
        PosicionInicial = new CCPoint();
        PosicionFinal = new CCPoint();
        avenColision = new Rect();
        rand = new Random();

        PosicionInicial.x = x;
        PosicionInicial.y = rand.nextInt(y);

        avionenemigo.setPosition(PosicionInicial.x, PosicionInicial.y);

        PosicionFinal.y = PosicionInicial.y;
        PosicionFinal.x=0;
        avionenemigo.runAction(MoveTo.action(3, PosicionFinal.x, PosicionFinal.y));
    }

    public Sprite getAvionenemigo() {
        avenColision.set(Math.round(avionenemigo.getPositionX() - avionenemigo.getWidth()/2), Math.round(avionenemigo.getPositionY() - avionenemigo.getHeight()/2), Math.round(avionenemigo.getPositionX() + avionenemigo.getWidth()/2), Math.round(avionenemigo.getPositionY() + avionenemigo.getHeight()/2));
        return avionenemigo;
    }

    public Rect getAvenColision() { return avenColision; }
}
