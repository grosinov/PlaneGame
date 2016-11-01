package com.example.a41752347.planegame;

import android.util.Log;
import android.view.MotionEvent;

import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCSize;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class clsJuego {
    CCGLSurfaceView VistaDelJuego;
    CCSize TamañoPantalla;
    Avion avion;
    Sprite fondo;
    int vida;
    int puntos = 0;
    int enemyTags = 1;
    int dispTags = 100000;

    ArrayList<AvionEnemigo> arrEnemigos;
    ArrayList<Disparo> arrDisparos;

    public clsJuego(CCGLSurfaceView vp){
        VistaDelJuego = vp;
        avion = new Avion();
        arrEnemigos = new ArrayList<>();
        arrDisparos = new ArrayList<>();

        vida = 3;
    }

    public void ComenzarJuego(){
        Director.sharedDirector().attachInView(VistaDelJuego);

        TamañoPantalla = Director.sharedDirector().displaySize();

        Director.sharedDirector().runWithScene(Escena());
    }

    private Scene Escena(){
        Scene escenajuego;
        escenajuego = Scene.node();

        CapaDeFondo capfon;
        capfon = new CapaDeFondo();

        CapaDeFrente capfren;
        capfren = new CapaDeFrente();

        escenajuego.addChild(capfon, -10);
        escenajuego.addChild(capfren, 10);

        return  escenajuego;
    }

    class CapaDeFondo extends Layer {
        public CapaDeFondo() {
            fondo = Sprite.sprite("fondo.png");
            fondo.setPosition(TamañoPantalla.width / 2, TamañoPantalla.height / 2);
            fondo.runAction(ScaleBy.action(0.01f, 14.0f, 10.0f));
            super.addChild(fondo);
        }
    }

    class CapaDeFrente extends Layer {
        public CapaDeFrente() {
            this.setIsTouchEnabled(true);
            MenuItemImage BotonDisparo;

            BotonDisparo = MenuItemImage.item("BotonDisparo.png", "avion.png", this, "PresionaBotonDisparo");
            BotonDisparo.setPosition(TamañoPantalla.width - BotonDisparo.getWidth(), 0 + BotonDisparo.getHeight() / 2 + 50);
            super.addChild(BotonDisparo);

            TimerTask TareaPonerEnemigos = new TimerTask() {
                @Override
                public void run() {
                    AvionEnemigo aven = new AvionEnemigo(Math.round(TamañoPantalla.width), Math.round(TamañoPantalla.height));
                    arrEnemigos.add(aven);
                    addChild(aven.getAvionenemigo(), 1, enemyTags);
                    enemyTags++;
                    for (int i = 0; i < arrEnemigos.size(); i++) {
                        aven = arrEnemigos.get(i);
                        if(aven != null){
                            for (int j = 0; j < arrDisparos.size(); j++) {
                                Disparo disp = arrDisparos.get(j);
                                if (disp != null) {
                                    if (disp.Colision(aven.getAvenColision())) {
                                        Log.d("asdasd", "Colisiono XD");
                                        removeChild(aven.getAvionenemigo().getTag(), true);
                                        arrEnemigos.remove(i);
                                        removeChild(disp.getDisparo().getTag(), true);
                                        arrDisparos.remove(j);
                                        puntos++;
                                    } else {
                                        if (disp.getDisparo().getPositionX() >= TamañoPantalla.width) {
                                            removeChild(disp.getDisparo().getTag(), true);
                                            arrDisparos.remove(j);
                                        }
                                    }
                                }
                            }
                            if (aven.getAvionenemigo().getPositionX() == 0) {
                                removeChild(aven.getAvionenemigo().getTag(), true);
                                arrEnemigos.remove(i);
                                vida--;
                                if (vida == 0) {
                                    //perdio
                                }
                            }
                        }
                    }
                }
            };

            Timer RelojEnemigos = new Timer();
            RelojEnemigos.schedule(TareaPonerEnemigos, 0, 1000);

            super.addChild(avion.getAvion());
        }

        @Override
        public boolean ccTouchesBegan(MotionEvent event) {
            if(event.getX() > TamañoPantalla.width / 2){
                Disparo disp = new Disparo(Math.round(avion.getAvion().getPositionX()), Math.round(avion.getAvion().getPositionY()), Math.round(TamañoPantalla.width));
                arrDisparos.add(disp);
                addChild(disp.getDisparo(), 1, dispTags);
                dispTags++;
            }
            return true;
        }

        @Override
        public boolean ccTouchesMoved(MotionEvent event) {
            float PosicionFinalY = avion.getAvion().getPositionY(), movimientovertical;
            movimientovertical = TamañoPantalla.getHeight() - event.getY() - TamañoPantalla.getHeight() / 2;
            int suavisadordemovimiento = 20;
            movimientovertical = movimientovertical / suavisadordemovimiento;
            if(event.getX() < TamañoPantalla.getWidth() / 2){
                if(avion.getAvion().getPositionY() <= 0){
                    PosicionFinalY = avion.getAvion().getPositionY() +1;
                } else if (avion.getAvion().getPositionY() >= TamañoPantalla.getHeight()){
                    PosicionFinalY = avion.getAvion().getPositionY() -1;
                } else {
                    PosicionFinalY = avion.getAvion().getPositionY() + movimientovertical;
                }
            }

            avion.getAvion().setPosition(avion.getAvion().getPositionX(), PosicionFinalY);
            return true;
        }

        @Override
        public boolean ccTouchesEnded(MotionEvent event) {
            return true;
        }
    }

    /*boolean InterseccionEntreSprites (Sprite Sprite1, Sprite Sprite2) {

        boolean Devolver;
        Devolver=false;

        int Sprite1Izquierda, Sprite1Derecha, Sprite1Abajo, Sprite1Arriba;
        int Sprite2Izquierda, Sprite2Derecha, Sprite2Abajo, Sprite2Arriba;
        Sprite1Izquierda=(int) (Sprite1.getPositionX() - Sprite1.getWidth()/2);
        Sprite1Derecha=(int) (Sprite1.getPositionX() + Sprite1.getWidth()/2);
        Sprite1Abajo=(int) (Sprite1.getPositionY() - Sprite1.getHeight()/2);
        Sprite1Arriba=(int) (Sprite1.getPositionY() + Sprite1.getHeight()/2);
        Sprite2Izquierda=(int) (Sprite2.getPositionX() - Sprite2.getWidth()/2);
        Sprite2Derecha=(int) (Sprite2.getPositionX() + Sprite2.getWidth()/2);
        Sprite2Abajo=(int) (Sprite2.getPositionY() - Sprite2.getHeight()/2);
        Sprite2Arriba=(int) (Sprite2.getPositionY() + Sprite2.getHeight()/2);

        if (EstaEntre(Sprite1Izquierda, Sprite2Izquierda, Sprite2Derecha) &&
        EstaEntre(Sprite1Abajo, Sprite2Abajo, Sprite2Arriba)) {
            Devolver = true;

        }
        if (EstaEntre(Sprite1Izquierda, Sprite2Izquierda, Sprite2Derecha) &&
        EstaEntre(Sprite1Arriba, Sprite2Abajo, Sprite2Arriba)) {
            Devolver = true;
        }
        if (EstaEntre(Sprite1Derecha, Sprite2Izquierda, Sprite2Derecha) &&
        EstaEntre(Sprite1Arriba, Sprite2Abajo, Sprite2Arriba)) {
            Devolver=true;
        }
        if (EstaEntre(Sprite1Derecha, Sprite2Izquierda, Sprite2Derecha) &&
        EstaEntre(Sprite1Abajo, Sprite2Abajo, Sprite2Arriba)) {
            Devolver=true;
        }
        if (EstaEntre(Sprite2Izquierda, Sprite1Izquierda, Sprite1Derecha) &&
        EstaEntre(Sprite2Abajo, Sprite1Abajo, Sprite1Arriba)) {
            Devolver=true;
        }
        if (EstaEntre(Sprite2Izquierda, Sprite1Izquierda, Sprite1Derecha) &&
        EstaEntre(Sprite2Arriba, Sprite1Abajo, Sprite1Arriba)) {
            Devolver=true;
        }
        if (EstaEntre(Sprite2Derecha, Sprite1Izquierda, Sprite1Derecha) &&
        EstaEntre(Sprite2Arriba, Sprite1Abajo, Sprite1Arriba)) {
            Devolver=true;
        }
        if (EstaEntre(Sprite2Derecha, Sprite1Izquierda, Sprite1Derecha) &amp;&amp;
        EstaEntre(Sprite2Abajo, Sprite1Abajo, Sprite1Arriba)) {
            Devolver=true;
        }

        return Devolver;
    }*/
}
