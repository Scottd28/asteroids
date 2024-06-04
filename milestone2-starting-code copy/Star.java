import java.awt.*;
import java.util.Random;


public class Star extends Circle{

    private boolean decrease;
    private boolean increase;

    private Random random = new Random();
    private int og  = radius;



    public Star(Point center, int radius) {
        super(center, radius);
    }

    @Override
    public void paint(Graphics brush, Color color) {

      boolean bright = random.nextBoolean();
      radius = og;
      if (bright) {
           radius--;
          brush.fillOval((int) center.x, (int) center.y, radius, radius);
          brush.setColor(color.brighter());
      } else{
          radius++;
          brush.fillOval((int) center.x, (int) center.y, radius, radius);
          brush.setColor(color.darker());
      }
       
    }

    @Override
    public void move() {

    }




}
