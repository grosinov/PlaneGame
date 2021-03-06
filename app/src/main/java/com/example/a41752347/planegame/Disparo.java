package com.example.a41752347.planegame;

import android.graphics.Rect;

import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCPoint;

public class Disparo {
    Sprite disparo;
     Rect dispColision;
    CCPoint PosicionInicial;
    CCPoint PosicionFinal;

    public Disparo(int x, int y, int PosFin) {
        disparo = Sprite.sprite("Disparo.png");
        PosicionInicial = new CCPoint();
        PosicionFinal = new CCPoint();
        dispColision = new Rect();

        PosicionInicial.x = x;
        PosicionInicial.y = y;

        disparo.setPosition(PosicionInicial.x, PosicionInicial.y);

        PosicionFinal.y = PosicionInicial.y;
        PosicionFinal.x = PosFin;

        disparo.runAction(MoveTo.action(3, PosicionFinal.x, PosicionFinal.y));
    }

    public Rect getDispColision() {
        dispColision.set(Math.round(disparo.getPositionX() - disparo.getWidth()/2), Math.round(disparo.getPositionY() - disparo.getHeight()/2), Math.round(disparo.getPositionX() + disparo.getWidth()/2), Math.round(disparo.getPositionY() + disparo.getHeight()/2));
        return dispColision;
    }

    public Sprite getDisparo() {
        return disparo;
    }

    public boolean Colision(Rect r){
        return dispColision.intersect(r);
    }
}
