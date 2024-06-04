import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class UFO extends Polygon {

    private Random random = new Random();
    private ArrayList <Bullet> Bullets = new ArrayList<Bullet>();
    private int direction; // in degrees
    private boolean alive = true;
    private double timeToMove = 0;

    private boolean right;

    public Ship ship;


    final static private Point[] corners = {
            new Point(20, 20), // top left
            new Point(0, Asteroids.SCREEN_HEIGHT - 50), //bottom left
            new Point(Asteroids.SCREEN_WIDTH - 50, Asteroids.SCREEN_HEIGHT - 50), // bottom right
            new Point(Asteroids.SCREEN_WIDTH - 50, 50)
            // top right
    };

    public UFO(Point[] inShape, double inRotation, Ship ship) {
        super(inShape, new Point(-1000, -1000), inRotation);
        this.ship = ship;
        setInitialPosition();
        setDirection();
    }

    public ArrayList<Bullet> getBullets(){
        return Bullets;
    }
    @Override
    public void paint(Graphics brush, Color color) {
        Point[] pts = getPoints();
        int[] xpts = new int[pts.length];
        int[] ypts = new int[pts.length];
        int npts = pts.length;

        for (int i = 0; i < npts; i++) {
            xpts[i] = (int) pts[i].x;
            ypts[i] = (int) pts[i].y;
        }

        brush.setColor(color);
        brush.fillPolygon(xpts, ypts, npts);

    }

    @Override
    public void move() {
        update();
        if ( position.x  < 0 || position.x > Asteroids.SCREEN_WIDTH) {
            alive = false;
        }
    }

    public void setInitialPosition() {
        int i = random.nextInt(4);
        position = corners[i].clone();
        right = ( i <= 1 );
        // System.out.println("HEREEERERERERERRE");
        // System.out.println("pos: "+ position.x + ", " + position.y);
    }

    public void update() {
        boolean shot = false;
        // boolean up = random.nextBoolean();
        //System.out.println("pos1: "+ position.x + ", " + position.y);

        timeToMove = timeToMove + 1;
        if (timeToMove == 40) { // at 80fps 240counts is 3s
            timeToMove = 0;
            setDirection();
        }

        position.x += Math.cos(Math.toRadians(direction));
        position.y += Math.sin(Math.toRadians(direction));
        // System.out.println("pos2: "+ position.x + ", " + position.y);
        if(shot == false && timeToMove/13 == 0){
            Bullets.add(new Bullet(getPoints()[3], findTheta()));
         //   System.out.println(findTheta());
            shot = true;
        }

    }

    public boolean isAlive() {
        return alive;
    }

    public void setDirection() {
        int[] dirs;// = new int[2];
        // int i = random.nextInt(2);

        if (right) {
             int i;
             if(position.y < 100) {// if near the top
                 dirs = new int[]{45, 0};
                 i = random.nextInt(2);
             } else if(position.y > Asteroids.SCREEN_HEIGHT -100){ // else if near the bottom
                 dirs = new int[]{-45, 0};
                 i = random.nextInt(2);
             } else{
                 dirs = new int[]{-45, 0, 45};
                 i = random.nextInt(3);
             }

            direction = dirs[i];// dir = possible directions of i
        }
        else { // going left
            int i;
            if(position.y < 100) {// if near the top
                dirs = new int[]{135, 180};
                i = random.nextInt(2);
            } else if(position.y > Asteroids.SCREEN_HEIGHT -100){ // else if near the bottom
                dirs = new int[]{225, 180};
                i = random.nextInt(2);
            } else{
                dirs = new int[]{225, 180, 135};
                i = random.nextInt(3);
            }

            direction = dirs[i];// dir = possible directions of i
        }
    }

    public boolean checkForHit(ArrayList<Bullet> bullets){
        for(Bullet b: bullets){
            if(contains(b.getCenter())){
                alive = false;
                return true;
            }
        }
        return false;

    }

    public double findTheta(){

        double x = ship.position.x - position.x;
        double y = ship.position.y - position.y;
        double theta = Math.atan2(y, x);
        theta = Math.toDegrees(theta);
       // System.out.println(theta);
        return theta;
    }


}