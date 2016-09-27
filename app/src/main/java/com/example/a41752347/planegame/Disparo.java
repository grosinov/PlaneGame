package com.example.a41752347.planegame;

import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCPoint;

public class Disparo {
    Sprite disparo;
    CCPoint PosicionInicial;
    CCPoint PosicionFinal;

    public Disparo(int y, int x) {
        disparo = Sprite.sprite("Disparo.png");
        PosicionInicial = new CCPoint();
        PosicionFinal = new CCPoint();

        PosicionInicial.x = x;
        PosicionInicial.y = y;

        PosicionFinal.y = PosicionInicial.y;
        PosicionFinal.x=0;
        disparo.runAction(MoveTo.action(3, PosicionFinal.x, PosicionFinal.y));
    }

    public Sprite getDisparo() {
        return disparo;
    }

    public void setPosicionInicial(int y, int x) {
        PosicionInicial.x = x;
        PosicionInicial.y = y;

        disparo.setPosition(PosicionInicial.x, PosicionInicial.y);
    }
}
