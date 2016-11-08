package com.example.a41752347.planegame;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.layers.ColorLayer;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCSize;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class clsJuego {
    CCGLSurfaceView VistaDelJuego;
    CCSize TamañoPantalla;
    Avion avion;
    Sprite fondo;
    Sprite vida1;
    Sprite vida2;
    Sprite vida3;
    int vida;
    int puntos = 0;
    int enemyTags = 1;
    int dispTags = 100000;
    int tiempo = 6000;
    Label Title;
    Label Toca;
    Label puntaje;
    Label puntaje2;
    CCColor3B black;

    ArrayList<AvionEnemigo> arrEnemigos;
    ArrayList<Disparo> arrDisparos;

    public clsJuego(CCGLSurfaceView vp){
        VistaDelJuego = vp;
        avion = new Avion();
        arrEnemigos = new ArrayList<>();
        arrDisparos = new ArrayList<>();
        black = new CCColor3B(Color.BLACK, Color.BLACK, Color.BLACK);

        vida = 3;
    }

    public void ComenzarJuego(){
        Director.sharedDirector().attachInView(VistaDelJuego);

        TamañoPantalla = Director.sharedDirector().displaySize();

        Director.sharedDirector().runWithScene(EscenaInicio());
    }

    private Scene EscenaInicio(){
        Scene escenaInicio;
        escenaInicio = Scene.node();

        CapaDeFondoInicio capfon;
        capfon = new CapaDeFondoInicio();

        CapaDeFrenteInicio capfren;
        capfren = new CapaDeFrenteInicio();

        escenaInicio.addChild(capfon, -10);
        escenaInicio.addChild(capfren, 10);

        return  escenaInicio;
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

    class CapaDeFondoInicio extends Layer{
        public CapaDeFondoInicio(){
            fondo = Sprite.sprite("fondo.png");
            fondo.setPosition(TamañoPantalla.width / 2, TamañoPantalla.height / 2);
            fondo.runAction(ScaleBy.action(0.01f, 14.0f, 10.0f));
            super.addChild(fondo);
        }
    }

    class CapaDeFrenteInicio extends Layer{
        public CapaDeFrenteInicio(){
            this.setIsTouchEnabled(true);
            Title = Label.label("PlaneGame","Verdana" ,150);
            Title.setColor(black);
            Title.setPosition(TamañoPantalla.width / 2, TamañoPantalla.height / 2 + 200);
            super.addChild(Title);

            Toca = Label.label("Toca para jugar", "Verdana", 70);
            Toca.setColor(black);
            Toca.setPosition(TamañoPantalla.width / 2, TamañoPantalla.height / 2);
            super.addChild(Toca);
        }

        @Override
        public boolean ccTouchesBegan(MotionEvent event) {
            Director.sharedDirector().replaceScene(Escena());
            return true;
        }
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
            vida1 = Sprite.sprite("Vida.png");
            vida2 = Sprite.sprite("Vida.png");
            vida3 = Sprite.sprite("Vida.png");

            vida1.setPosition(0 + vida1.getWidth() / 2, 0 + vida1.getHeight() / 2);
            vida1.runAction(ScaleBy.action(0.01f, 0.5f, 0.5f));
            super.addChild(vida1, 1, 200000);

            vida2.setPosition(vida1.getPositionX() + vida1.getWidth(), 0 + vida2.getHeight() / 2);
            vida2.runAction(ScaleBy.action(0.01f, 0.5f, 0.5f));
            super.addChild(vida2, 1, 200001);

            vida3.setPosition(vida2.getPositionX() + vida2.getWidth(), 0 + vida3.getHeight() / 2);
            vida3.runAction(ScaleBy.action(0.01f, 0.5f, 0.5f));
            super.addChild(vida3, 1, 200002);

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
            RelojEnemigos.schedule(TareaPonerEnemigos, 0, tiempo);
            switch (puntos){
                case 10:
                    tiempo = 5500;
                    break;
                case 20:
                    tiempo = 5000;
                    break;
                case 30:
                    tiempo = 4500;
                    break;
                case 40:
                    tiempo = 4000;
                    break;
                case 50:
                    tiempo = 3500;
                    break;
                case 60:
                    tiempo = 3000;
                    break;
                case 70:
                    tiempo = 2500;
                    break;
                case 80:
                    tiempo = 2000;
                    break;
                case 90:
                    tiempo = 1500;
                    break;
                case 100:
                    tiempo = 1000;
                    break;
            }
            puntaje = Label.label("" + puntos,"Verdana" ,100);

            TimerTask TareaDetectarColisiones = new TimerTask() {
                @Override
                public void run() {
                    puntaje.setString("" + puntos);
                    puntaje.setColor(black);
                    puntaje.setPosition(0 + puntaje.getWidth() + 20, TamañoPantalla.height - puntaje.getHeight() - 20);
                    addChild(puntaje);

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
                                switch (vida){
                                    case 2:
                                        removeChild(200002, true);
                                        break;
                                    case 1:
                                        removeChild(200001, true);
                                        break;
                                    case 0:
                                        removeChild(200000, true);
                                        Director.sharedDirector().replaceScene(EscenaPerdio());
                                        break;
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
            this.setIsTouchEnabled(true);
            Title = Label.label("Has Perdido!","Verdana" ,150);
            Title.setColor(black);
            Title.setPosition(TamañoPantalla.width / 2, TamañoPantalla.height / 2 + 200);
            super.addChild(Title);

            Toca = Label.label("Toca para reintentar", "Verdana", 70);
            Toca.setColor(black);
            Toca.setPosition(TamañoPantalla.width / 2, TamañoPantalla.height / 2);
            super.addChild(Toca);

            puntaje2 = Label.label("Puntaje: " + puntos, "Verdana", 70);
            puntaje2.setColor(black);
            puntaje2.setPosition(TamañoPantalla.width / 2, TamañoPantalla.height / 2 - 200);
            addChild(puntaje2);

            puntos = 0;
        }

        @Override
        public boolean ccTouchesBegan(MotionEvent event) {
            Director.sharedDirector().replaceScene(Escena());
            vida = 3;
            return true;
        }

        @Override
        public boolean ccTouchesMoved(MotionEvent event) {
            return true;
        }

        @Override
        public boolean ccTouchesEnded(MotionEvent event) {
            return super.ccTouchesEnded(event);
        }
    }
}
