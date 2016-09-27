package com.example.a41752347.planegame;


import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCPoint;

public class Avion {
    Sprite avion;
    CCPoint PosicionInicial;

    public Avion() {
        avion = Sprite.sprite("avion.png");
        PosicionInicial = new CCPoint();
        PosicionInicial.x = 0 + avion.getWidth() / 4;
        PosicionInicial.y = 0 + avion.getHeight() / 2;
        avion.setPosition(PosicionInicial.x, PosicionInicial.y);

        avion.runAction(ScaleBy.action(0.01f, 0.25f, 0.25f));
    }

    public CCPoint getPosicionInicial() {
        return PosicionInicial;
    }

    public Sprite getAvion() {
        return avion;
    }
}
