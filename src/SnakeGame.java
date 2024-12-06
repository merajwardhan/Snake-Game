import  javax.swing.*;

public class SnakeGame extends JFrame{

    SnakeGame(){
        super("Snake Game");//in java "this()" & "super()" must be called first because, The parent class' constructor needs to be called
        // before the subclass' constructor
        this.add(new Board());//added panel to the frame which is actually our class but as it extends JPanel its also a swing component as we
        //can directly pass the class inside the constructor
        this.pack();//"pack()" here pack function needs to be below the subclass constructor call because, because it will pack elements as
        //as soon as the frame refreshes (as the snake moves)
        this.setResizable(false);

        this.setSize(400,400);
        this.setLocationRelativeTo(null);

    }

    public static void main(String[] args) {
        new SnakeGame().setVisible(true);
    }
}