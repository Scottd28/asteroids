
/*
CLASS: Asteroids
DESCRIPTION: Extending Game, Asteroids is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.
Original code by Dan Leyzberg and Art Simon
 */
import java.awt.*;
import java.sql.SQLOutput;
import java.util.*;

public class Asteroids extends Game {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private static final int COLLISION_PERIOD = 100;

    static int counter = 0;

    // how we track asteroid collisions
    private boolean collision = false;
    private static int collisionTime = COLLISION_PERIOD;

    public Star[] stars;

    private boolean won;
    private boolean lost = false;
    private int lives = 5;

    private java.util.List<Asteroid> randomAsteroids = new ArrayList<Asteroid>();
    private java.util.List<Asteroid> takenAsteroids = new ArrayList<Asteroid>();

    public Ship ship;

    private UFO ufo = null;

    private int score = 0;

    public Asteroids() {
        super("Asteroids!", SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setFocusable(true);
        this.requestFocus();

        // create a number of random asteroid objects
        randomAsteroids = createRandomAsteroids(5, 60, 30);

        stars = createStars(25, 5);

        // create the ship
        ship = createShip();

        // register the ship as a KeyListener
        this.addKeyListener(ship);
    }

    // private helper method to create the Ship
    private Ship createShip() {
        // Look of ship
        Point[] shipShape = {
                new Point(0, 0),
                new Point(Ship.SHIP_WIDTH / 3.5, Ship.SHIP_HEIGHT / 2),
                new Point(0, Ship.SHIP_HEIGHT),
                new Point(Ship.SHIP_WIDTH, Ship.SHIP_HEIGHT / 2)
        };


        // Set ship at the middle of the screen
        Point startingPosition = new Point((width - Ship.SHIP_WIDTH) / 2, (height - Ship.SHIP_HEIGHT) / 2);
        int startingRotation = 0; // Start facing to the right

        return new Ship(shipShape, startingPosition, startingRotation);
    }


    private UFO createUFO(){
        Point[] ufoShape ={
//                new Point(0, 0),
//                new Point(-50, 50),
//                new Point(50, 50),
                new Point(0, 0),
                new Point(5, 0),
                new Point(10, 10),
                new Point(20, 15),
                new Point(10, 30),
                new Point(-10, 30),
                new Point(-20, 15),
                new Point(-10, 10),

        };
        //Point startingPoint = new Point(0, 0);
        return new UFO(ufoShape , 0.0, ship);
    }
    //  Create an array of random asteroids
    private java.util.List<Asteroid> createRandomAsteroids(int numberOfAsteroids, int maxAsteroidWidth,
                                                           int minAsteroidWidth) {
        java.util.List<Asteroid> asteroids = new ArrayList<>(numberOfAsteroids);

        for (int i = 0; i < numberOfAsteroids; ++i) {
            // Create random asteroids by sampling points on a circle
            // Find the radius first.
            int radius = (int) (Math.random() * maxAsteroidWidth);
            if (radius < minAsteroidWidth) {
                radius += minAsteroidWidth;
            }
            // Find the circles angle
            double angle = (Math.random() * Math.PI * 1.0 / 2.0);
            if (angle < Math.PI * 1.0 / 5.0) {
                angle += Math.PI * 1.0 / 5.0;
            }
            // Sample and store points around that circle
            ArrayList<Point> asteroidSides = new ArrayList<Point>();
            double originalAngle = angle;
            while (angle < 2 * Math.PI) {
                double x = Math.cos(angle) * radius;
                double y = Math.sin(angle) * radius;
                asteroidSides.add(new Point(x, y));
                angle += originalAngle;
            }
            // Set everything up to create the asteroid
            Point[] inSides = asteroidSides.toArray(new Point[asteroidSides.size()]);
            Point inPosition = new Point(Math.random() * SCREEN_WIDTH, Math.random() * SCREEN_HEIGHT);
            double inRotation = Math.random() * 360;
            asteroids.add(new Asteroid(inSides, inPosition, inRotation));
        }
        return asteroids;
    }

    // Create a certain number of stars with a given max radius
    public Star[] createStars(int numberOfStars, int maxRadius) {
        Star[] stars = new Star[numberOfStars];
        for (int i = 0; i < numberOfStars; ++i) {
            Point center = new Point
                    (Math.random() * SCREEN_WIDTH, Math.random() * SCREEN_HEIGHT);
            int radius = (int) (Math.random() * maxRadius);
            if (radius < 1) {
                radius = 1;
            }
            stars[i] = new Star(center, radius);
        }

        return stars;
    }


    public void paint(Graphics brush) {

        Random random = new Random();
        java.util.List<Bullet> ufoBullets = new ArrayList<Bullet>();
        if( ufo == null && random.nextDouble() < 0.01 ) {
            ufo = createUFO();
        }
        brush.setColor(Color.BLACK);
        brush.fillRect(0, 0, width, height);
        ArrayList<Bullet> bulletsShot = ship.getBullets();
        ArrayList<Bullet> takenBullets = new ArrayList<>();
        ArrayList<Bullet> takenBulletsUfo = new ArrayList<>();

        // sample code for printing message for debugging
        // counter is incremented and this message printed
        // each time the canvas is repainted
        counter++;
        brush.setColor(Color.white);
        brush.drawString("Counter is " + counter, 10, 10);

        brush.setColor(Color.white);
        brush.drawString("lives" + lives, 700, 10);

        brush.setColor(Color.white);
        brush.drawString("score" + score, 150, 50);

        if( ufo != null ) {
           // System.out.println("SHOT" + ufoBullets.size());
            brush.drawString("Status " + ufo.isAlive(), 50, 50);
            ufo.paint(brush, Color.white);
            ufo.move();
            ufoBullets = ufo.getBullets();

            if( ufo.checkForHit(bulletsShot) && !ufo.isAlive() ) {
                ufo = null;
                score = (score + 50);
            }

            for(Bullet bullet : ufoBullets){
                //System.out.println("SHOT" + ufoBullets.size());
                bullet.paint(brush, Color.white);
                bullet.move();
                if (bullet.OutOfBounds()) {
                    takenBulletsUfo.add(bullet);
                }
                if(ship.contains(bullet.getCenter())){
                   collision = true;
                }

            }
            for(Bullet bullet: takenBulletsUfo){
                ufoBullets.remove(bullet);
            }
        }

        // display the random asteroids
        for (Asteroid asteroid : randomAsteroids) {
            asteroid.paint(brush, Color.white);
            asteroid.move();

            // get collision status
            if (!collision) {
                collision = asteroid.collision(ship);
            }
            for (Bullet bullet : bulletsShot) {
                if (asteroid.contains(bullet.getCenter())) {
                    ////////////////////////////////////////////////
                    takenAsteroids.add(asteroid);
                    takenBullets.add(bullet);
                    score = (int)(score + (asteroid.getArea()/100));
                }
            }
        }

        for (Asteroid asteroid : takenAsteroids) {
            randomAsteroids.remove(asteroid);

            if (asteroid.getArea() > 1000.0 && !asteroid.shot) {
                asteroid.shot = true;
                Point[] newShape = asteroid.getPoints();
                Point x =  new Point(asteroid.position.x-5,asteroid.position.y-5) ;

                for(int i = 0; i < newShape.length; i++){
                    newShape[i].x = newShape[i].x/2;
                    newShape[i].y = newShape[i].y/2;
                }
                Asteroid a1 = new Asteroid(newShape,asteroid.position,45.0 );
                Asteroid a2 = new Asteroid(newShape,x,-45.0 );

                randomAsteroids.add(a1);
                randomAsteroids.add(a2);
                //System.out.println(asteroid.getArea());
            }
            if (randomAsteroids.size() == 0) {
                won = true;
            }
        }

        for (Bullet bullet : bulletsShot) {
            bullet.paint(brush, Color.white);
            bullet.move();
            if (bullet.OutOfBounds()) {
                takenBullets.add(bullet);
            }

        }

        for (Bullet bullet : takenBullets) {
            bulletsShot.remove(bullet);
        }
        for (Star star : stars) {
            star.paint(brush, Color.white);
        }

        /**
         * If there is a collision paint the ship a different color and track collision time.
         * After the period of time has elapsed, set the ship back to its default color.
         */
        if (collision) {
            ship.paint(brush, Color.gray);
            collisionTime -= 1;
            if (collisionTime <= 0) {
                collision = false;
                collisionTime = COLLISION_PERIOD;
                lives--;

            }
        } else {
            ship.paint(brush, Color.pink);
        }


        ship.move();
        if (won) {
            brush.setColor(Color.BLACK);
            brush.fillRect(0, 0, width, height);
            brush.setColor(Color.white);
            brush.drawString("WINNER", 400, 300);
            Star[] stars = createStars(50,3);
            for(Star star: stars){
                star.paint(brush, Color.white);
            }
        }
        if(lives == 0){
         lost = true;
        }
        if(lost == true){
            brush.setColor(Color.RED);
           brush.fillRect(0, 0, width, height);
           brush.setColor(Color.BLACK);
           brush.drawString("I OWN YOUR SOUL :)", 400, 300);
           Star[] stars = createStars(50,5);
            for(Star star: stars){
                star.paint(brush, Color.white);
            }
        }


    }


    public static void main(String[] args) {
        Asteroids a = new Asteroids();
        a.repaint();
    }


}