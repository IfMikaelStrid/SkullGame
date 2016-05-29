package skullgame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import skullgame.framework.Animation;


public class StartingClass extends Applet implements Runnable, KeyListener {
	
	private static Skull skull;
		
	
	public static SmallKnight sk1;
	public static GunDwarf sk2;
	
	private Image image, character,characterDown,characterIdleLeft,characterJumped,characterJumpedLeft,currentSprite,enemyGunDwarf,characterPile,characterRun,enemySmallKnight,characterRunLeft,background;
    public static Image tilegrassTop, tilegrassBot, tilegrassLeft, tilegrassRight, tiledirt, tileocean;
	
	private Graphics second;
	private URL base;
	private static Background bg1, bg2;
	private Animation anim, hanim;	
	public int showbound = 0;
	
	private int Playerdirection;
	
	private ArrayList<Tile> tilearray = new ArrayList<Tile>();

	@Override
	public void init() {

		setSize(800, 480);
//		setSize(1680, 1050);
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Super Dead Man dev");
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			// TODO: handle exception
		}

		character = getImage(base, "data/Skull_idle.gif");
		background = getImage(base, "data/background.png");
        characterRun = getImage(base, "data/skull_running.gif");
        characterRunLeft = getImage(base, "data/skull_running_left.gif");
        characterDown = getImage(base, "data/Skull_pile.gif");
        characterPile = getImage(base, "data/Skull_pile.gif");
        characterJumped = getImage(base, "data/Skull_jumping.gif");
        characterJumpedLeft = getImage(base, "data/skull_jumping_left.gif");
        characterIdleLeft = getImage(base, "data/skull_idle_left.gif");
        
        enemySmallKnight = getImage(base, "data/small_knight.gif");
        enemyGunDwarf = getImage(base, "data/dwarf_idle.gif");
        
		tiledirt = getImage(base, "data/Test_Tile.png");
        tilegrassTop = getImage(base, "data/Test_Tile.png");
        tilegrassBot = getImage(base, "data/Test_Tile.png");
        tilegrassLeft = getImage(base, "data/Test_Tile.png");
        tilegrassRight = getImage(base, "data/Test_Tile.png");
		
		tileocean = getImage(base, "data/Test_Tile_back.png");
		
        anim = new Animation();
		hanim = new Animation();
		
        currentSprite = character;
        
	}
    @Override
    public void start() {
        
        bg1 = new Background(0,0);
        bg2 = new Background(2160, 0);       
        
        skull = new Skull();
        
		try {
			loadMap("data/map1.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     		
        sk1 = new SmallKnight(340,428);
        sk2 = new GunDwarf(700,438);
        
        
        Thread thread = new Thread(this);
        thread.start();
    }

    private void loadMap(String filename) throws IOException {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                reader.close();
                break;
            }

            if (!line.startsWith("!")) {
                lines.add(line);
                width = Math.max(width, line.length());

            }
        }
        height = lines.size();

        for (int j = 0; j < 12; j++) {
            String line = (String) lines.get(j);
            for (int i = 0; i < width; i++) {

                if (i < line.length()) {
                    char ch = line.charAt(i);
                    Tile t = new Tile(i, j, Character.getNumericValue(ch));
                    tilearray.add(t);
                }

            }
        }

    }
	@Override
    public void stop() {
        // TODO Auto-generated method stub
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void run() {
        while (true) {
			skull .update();
            if (skull.isJumped()){
                if(skull.speedX > 0 || skull.speedX == 0){
            	currentSprite = characterJumped;
                }
                else if(skull.speedX < 0){
                currentSprite = characterJumpedLeft; 
                }
            }
            else if (skull.isJumped() == false && skull.isDucked() == false){
                if(skull.speedX > 0){
                    currentSprite = characterRun;
                    Playerdirection = 0;
                }
                else if(skull.speedX < 0){
                    currentSprite = characterRunLeft;
                    Playerdirection = 1;
                }
                else if(Playerdirection == 0){
                	currentSprite = character;
                }
                else if(Playerdirection == 1) {
                	currentSprite = characterIdleLeft;
                }
                
            }

            

			ArrayList projectiles = skull.getProjectiles();
			for (int i = 0; i < projectiles.size(); i++) {
				Projectile p = (Projectile) projectiles.get(i);
				if (p.isVisible() == true) {
					p.update();
				} else {
					projectiles.remove(i);
				}
			}
			updateTiles();
			
            sk1.update();
            sk2.update();
            bg1.update();
            bg2.update();
            repaint();
            try {
                Thread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Graphics g) {
        if (image == null) {
            image = createImage(this.getWidth(), this.getHeight());
            second = image.getGraphics();
        }

        second.setColor(getBackground());
        second.fillRect(0, 0, getWidth(), getHeight());
        second.setColor(getForeground());
        paint(second);

        g.drawImage(image, 0, 0, this);

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
        g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
        paintTiles(g);
        
        
		ArrayList projectiles = skull.getProjectiles();
		for (int i = 0; i < projectiles.size(); i++) {
			Projectile p = (Projectile) projectiles.get(i);
			g.setColor(Color.RED);
			g.fillRect(p.getX(), p.getY(), 5, 5);
		}
		//Paint collision boxes :D
		
		if(showbound == 1){
			g.drawRect((int)skull.rect.getX(), (int)skull.rect.getY(), (int)skull.rect.getWidth(), (int)skull.rect.getHeight());
			g.drawRect((int)skull.rect2.getX(), (int)skull.rect2.getY(), (int)skull.rect2.getWidth(), (int)skull.rect2.getHeight());
			g.drawRect((int)skull.yellowRed.getX(), (int)skull.yellowRed.getY(), (int)skull.yellowRed.getWidth(), (int)skull.yellowRed.getHeight());
			g.drawRect((int)skull.rect3.getX(), (int)skull.rect3.getY(), (int)skull.rect3.getWidth(), (int)skull.rect3.getHeight());
			g.drawRect((int)skull.rect4.getX(), (int)skull.rect4.getY(), (int)skull.rect4.getWidth(), (int)skull.rect4.getHeight());
			g.drawRect((int)skull.footleft.getX(), (int)skull.footleft.getY(), (int)skull.footleft.getWidth(), (int)skull.footleft.getHeight());
			g.drawRect((int)skull.footright.getX(), (int)skull.footright.getY(), (int)skull.footright.getWidth(), (int)skull.footright.getHeight());
			
		}
		//g.drawRect((int)skull.rect.getX(), (int)skull.rect.getY(), (int)skull.rect.getWidth(), (int)skull.rect.getHeight());
		//g.drawRect((int)skull.rect2.getX(), (int)skull.rect2.getY(), (int)skull.rect2.getWidth(), (int)skull.rect2.getHeight());
		//g.drawRect((int)skull.yellowRed.getX(), (int)skull.yellowRed.getY(), (int)skull.yellowRed.getWidth(), (int)skull.yellowRed.getHeight());
		g.drawImage(currentSprite, skull.getCenterX() - 61, skull.getCenterY() - 63, this);
        g.drawImage(enemySmallKnight, sk1.getCenterX() - 48, sk1.getCenterY() - 48, this);
        g.drawImage(enemyGunDwarf, sk2.getCenterX() - 48, sk2.getCenterY() - 48, this);

    }
	
    private void updateTiles() {

		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			t.update();
		}


	}


	private void paintTiles(Graphics g) {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY(), this);
		}
	}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
      
        
        case KeyEvent.VK_UP:
            break;

        case KeyEvent.VK_DOWN:
            currentSprite = characterDown;
            if (skull.isJumped() == false){
            	skull.setDucked(true);
            	skull.setSpeedX(0);
            }
            break;

        case KeyEvent.VK_LEFT:
        	skull.moveLeft();
        	skull.setMovingLeft(true);
            break;

        case KeyEvent.VK_RIGHT:   
        	skull.moveRight();    
        	skull.setMovingRight(true);
            break;

        case KeyEvent.VK_SPACE:
        	if(skull.isDucked() == true){            
        		skull.setSpeedY(0);
        	}
        	else{skull.jump();}


            break;
            
           	case KeyEvent.VK_CONTROL:
           		showbound = 1;
           	break;
		/*case KeyEvent.VK_CONTROL:
			if (Skull.isDucked() == false && Skull.isJumped() == false) {
				Skull.shoot();
				robot.setReadyToFire(false);
			}
			break;*/
        }

    }


    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
            System.out.println("Stop moving up");
            break;

        case KeyEvent.VK_DOWN:
            currentSprite = character;
            skull.setDucked(false);
            break;

        case KeyEvent.VK_LEFT:
        	currentSprite = character;
        	skull.stopLeft();
            break;

        case KeyEvent.VK_RIGHT:
        	currentSprite = character;
        	skull.stopRight();
            break;

        case KeyEvent.VK_SPACE:
            break;
        
 		case KeyEvent.VK_CONTROL:
 			skull.setReadyToFire(true);
 			showbound = 0;
			break;
			
 		
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public static Background getBg1() {
        return bg1;
    }

    public static Background getBg2() {
        return bg2;
    }
    public static Skull getSkull(){
		return skull;
	}

}
