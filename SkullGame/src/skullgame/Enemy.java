package skullgame;

import java.awt.Rectangle;

public class Enemy {

    private int power, speedX, centerX, centerY;
    private Background bg = StartingClass.getBg1();

    public Rectangle r = new Rectangle(0,0,0,0);
    public int health = 5;

    // Behavioral Methods
    public void update() {
        centerX += speedX;
        speedX = bg.getSpeedX();
        r.setBounds(centerX - 25, centerY-25, 50, 60);
        
        if (r.intersects(Skull.yellowRed)){
            checkCollision();
        }
        
    }



    private void checkCollision() {
        if (r.intersects(Skull.rect) || r.intersects(Skull.rect2) || r.intersects(Skull.rect3) || r.intersects(Skull.rect4)){
            System.out.println("collision");
            
            }
        }

    public void die() {

    }

    public void attack() {

    }

    public int getPower() {
        return power;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public Background getBg() {
        return bg;
    }



    public void setPower(int power) {
        this.power = power;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setBg(Background bg) {
        this.bg = bg;
    }

    
}