import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Board extends JPanel implements ActionListener {

    private Image food;
    private Image dot;
    private Image head;

    private int RANDOME_POSOTION = 22;
    private final int ALL_DOTS = 900;//we have declared "ALL_DOTS" as 160000 becuase that's the area of the frame. i.e width 400 pixels
    //height 400 pixels = area width*height(400*400) 160000, and this will not change
    private final int DOT_SIZE = 10;//to place the dots behind each dot we have to substract the width of the dot ahead to place a dot behind

    private int apple_x;
    private int apple_y; //these values are not initialised because the apple will be at a random place, plus we have not taken this as  a array
    //because the there will be only 1 apple at any random position

    private final int x[] = new int[ALL_DOTS];//this x array is the posotion of the images at x axis
    private final int y[] = new int[ALL_DOTS];//this y array is the posotion of the images at y axis

    private int body;//we have declared this as global variable because we have to use this in various methods
    private boolean inGame = true;
    private Timer timer;

    private boolean leftDirection = false;
    private boolean rightDirection = true;//we've initially set right direction as true because the snake will move to the right by default
    //when the game starts
    private boolean upDirection = false;
    private boolean downDirection = false;


    Board(){
        this.addKeyListener(new TAdapter());
        this.setBackground(Color.WHITE);
        //this.setPreferredSize(new Dimension(400,400));
        this.setFocusable(true);

        loadimages();//this method is called first because the body of the snake consists of images and we have to initialize the snake so the
        //the images have to be loaded first
        start_game();
    }

    public void loadimages(){
        ImageIcon f = new ImageIcon("C:\\Users\\patil\\Desktop\\Java Programming\\Snake Game\\src\\icons\\icons\\apple.png");
        food = f.getImage();
        ImageIcon d = new ImageIcon("C:\\Users\\patil\\Desktop\\Java Programming\\Snake Game\\src\\icons\\icons\\dot.png");
        dot = d.getImage();
        ImageIcon h = new ImageIcon("C:\\Users\\patil\\Desktop\\Java Programming\\Snake Game\\src\\icons\\icons\\head.png");
        head = h.getImage();
    }

    public void start_game(){
        body = 4;

        for(int i = 0; i<body; i++){
            y[i] = 50;//this is for the default placement of the snake when the game starts
            x[i] = 50 - i * DOT_SIZE;//x minus dot size so that the dots can be placed behind each other on "x axis" and we multiplies the dot
            //size with i* because the value of i will increment by 1 and then will place the dot further behind as the loop goes on. if we dont
            //multiply by i then againg ang again the dot will be places in the same place
        }

        locateApple();

        timer = new Timer(140,this);//at first this timer was in "locateApple()" method deu to which everytime when the apple
        //was relocated the timer would start with the previous timer running concurrently due to which "140" ,iliseconds uaed to be added
        //resulting in the snake moving faster everytime it ate the apple
        timer.start();
    }

    public void locateApple(){
        int r = (int) (Math.random() * RANDOME_POSOTION);
        apple_x = r * DOT_SIZE;
        r = (int) (Math.random() * RANDOME_POSOTION);
        apple_y = r * DOT_SIZE;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        draw(g);
    }

    public void draw(Graphics g){

        if(inGame) {
            g.drawImage(food, apple_x, apple_y, this);
            for (int i = 0; i < body; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        }else{
            gameOver(g);
        }
    }

    public void gameOver(Graphics g){
        String msg = "Game OVER Bitch!!!";
        Font font = new Font("MV Boli",Font.BOLD,25);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(msg,(400 - metrics.stringWidth(msg))/2 , 350/2);
    }

    public void move(){
        for (int i = body; i > 0; i--){
            x[i] = x[i - 1];//position of your current dot will be the previous position of the dot ahead(front dot)
            y[i] = y[i - 1];
        }

        if (leftDirection){//if left direction is true then dot side is substracted on x axis and the elements will move to the left
            x[0] = x[0] - DOT_SIZE;
        }
        if (rightDirection){
            x[0] = x[0] + DOT_SIZE;
        }
        if (upDirection){
            y[0] = y[0] - DOT_SIZE;
        }
        if (downDirection){
            y[0] = y[0] + DOT_SIZE;
        }
    }

    public void checkApple(){
        if ((x[0] == apple_x) && (y[0] == apple_y)){
            body++;
            locateApple();
        }
    }

    public void checkCollision(){
        for (int i = body; i > 0; i--){
            if ((i > 4) && (x[0] == x[1]) && (y[0] == y[1])){
                inGame = false;
            }
        }
        if (x[0] >= 400){
            inGame = false;
        }
        if (y[0] >= 400){
            inGame = false;
        }
        if (x[0] < 0){
            inGame = false;
        }
        if (y[0] < 0){
            inGame = false;
        }
        if (!inGame){
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame){
            checkApple ();
            checkCollision();
            move();
        }

        repaint();//will paint the screen after every movement as we cannot call the paint method explicitly
    }

    public class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_LEFT && (!rightDirection)){//if left key is pressed then leftDirection will be set true, also we have to
        //set left true only when right direction is false, because right direction will be set false in the downDirection/upDirection
        //condition because the snake will not directly move reverse from right to left first it has to go up/down to take a turn like a car
        //and when turned up/down that statement will set rightDirection to false hence here right direction has to be false
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_RIGHT && (!leftDirection)){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_UP && (!downDirection)){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if(key == KeyEvent.VK_DOWN && (!upDirection)){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
