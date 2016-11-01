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

    private Scene EscenaPerdio(){
        Scene escenaperdio;
        escenaperdio = Scene.node();

        CapaFondoPerdio capfonper;
        capfonper = new CapaFondoPerdio();

        CapaFrentePerdio capfrenper;
        capfrenper = new CapaFrentePerdio();

        escenaperdio.addChild(capfonper, -10);
        escenaperdio.addChild(capfrenper, 10);

        return  escenaperdio;
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
                }
            };

            Timer RelojEnemigos = new Timer();
            RelojEnemigos.schedule(TareaPonerEnemigos, 0, 2000);

            TimerTask TareaDetectarColisiones = new TimerTask() {
                @Override
                public void run() {
                    for (int i = 0; i < arrEnemigos.size(); i++) {
                        AvionEnemigo aven = arrEnemigos.get(i);
                        if(aven != null){
                            for (int j = 0; j < arrDisparos.size(); j++) {
                                Disparo disp = arrDisparos.get(j);
                                if (disp != null) {
                                    aven.getAvenColision().set(Math.round(aven.getAvionenemigo().getPositionX()), Math.round(aven.getAvionenemigo().getPositionY()), Math.round(aven.getAvionenemigo().getPositionX() + aven.getAvionenemigo().getWidth()), Math.round(aven.getAvionenemigo().getPositionY() + aven.getAvionenemigo().getHeight()));
                                    disp.getDispColision().set(Math.round(disp.getDisparo().getPositionX()), Math.round(disp.getDisparo().getPositionY()), Math.round(disp.getDisparo().getPositionX() + disp.getDisparo().getWidth()), Math.round(disp.getDisparo().getPositionY() + disp.getDisparo().getHeight()));
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
                                    Director.sharedDirector().runWithScene(EscenaPerdio());
                                }
                            }
                        }
                    }
                }
            };

            Timer RelojColision = new Timer();
            RelojColision.schedule(TareaDetectarColisiones, 0, 1);

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

    class CapaFondoPerdio extends Layer {
        public CapaFondoPerdio(){
            fondo = Sprite.sprite("fondo.png");
            fondo.setPosition(TamañoPantalla.width / 2, TamañoPantalla.height / 2);
            fondo.runAction(ScaleBy.action(0.01f, 14.0f, 10.0f));
            super.addChild(fondo);
        }
    }

    class CapaFrentePerdio extends Layer {
        public CapaFrentePerdio(){
        }

        @Override
        public boolean ccTouchesBegan(MotionEvent event) {

            return true;
        }

        @Override
        public boolean ccTouchesMoved(MotionEvent event) {
            Director.sharedDirector().runWithScene(Escena());
            vida = 3;
            return true;
        }

        @Override
        public boolean ccTouchesEnded(MotionEvent event) {
            return super.ccTouchesEnded(event);
        }
    }
}
