import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MineSweeper extends JFrame
{
    final int ROWS = 15, COLS = 15;
    int DIFFICULTY = 90;
    int bombCounter;
    int winCondition;

    ImageIcon RB;

    JMenuBar menuBar;
    JMenu menu;
    JMenuItem close;
    JMenuItem restart;

    JPanel panel;
    JButton[][] button;

    JPanel infoPanel;
    int counter;
    JLabel count;

    ImageIcon buttons;
    ImageIcon fail;
    ImageIcon pass;
    ImageIcon one;
    ImageIcon two;
    ImageIcon three;
    ImageIcon four;
    ImageIcon five;
    ImageIcon six;
    ImageIcon seven;
    ImageIcon eight;

    public static void main(String[] args)
{
    new MineSweeper().launch();
}

    // launches program
    public void launch()
    {
        initUI();
        initImage();
        initMenuBar();
        initGrid();
        initNumbers();
        initInfo();
        winCondition = 225 - bombCounter;
        this.setVisible(true);
    }

    public void initInfo()
    {
        counter = 0;
        infoPanel = new JPanel(new BorderLayout());
        count = new JLabel();
        count.setText("   "+counter);
        infoPanel.add(count, BorderLayout.WEST);
        this.add(infoPanel, BorderLayout.NORTH);
        revalidate();
    }

    // initializes all of the images needed
    public void initImage()
    {
        String location = "C:\\Users\\Chris\\IdeaProjects\\MineSweeper\\src\\resources\\images\\";

        RB = new ImageIcon(location+"RB.png"); // remove me
        buttons = new ImageIcon(location+"buttons.png");
        fail = new ImageIcon(location+"fail.png");
        pass = new ImageIcon(location+"pass.png");
        one = new ImageIcon(location+"one.png");
        two = new ImageIcon(location+"two.png");
        three = new ImageIcon(location+"three.png");
        four = new ImageIcon(location+"four.png");
        five = new ImageIcon(location+"five.png");
        six = new ImageIcon(location+"six.png");
        seven = new ImageIcon(location+"seven.png");
        eight = new ImageIcon(location+"eight.png");
    }

    // initialize the frame
    public void initUI()
    {
        this.setSize(675,675);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.gray);
        this.setTitle("Bomb Scanner Extreme");
    }

    // initialize the menu bar
    public void initMenuBar()
    {
        // initialize menu variables
        menuBar = new JMenuBar();
        menu = new JMenu(" File ");
        close = new JMenuItem("Close");
        restart = new JMenuItem("Restart Game");

        // set up the menu
        setJMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(close);
        menu.add(restart);

        // action listener for closing the program
        close.addActionListener(e->
        {
            try
            {
                System.exit(0);
            }
            catch(Exception e1)
            {
                e1.printStackTrace();
                System.out.println("Failed to close");
            }
        });
        restart.addActionListener(e->
        {
            reset();
        });

    }

    // initialize the grid starting conditions
    public void initGrid()
    {
        panel = new JPanel(new GridLayout(ROWS,COLS));
        button = new JButton[ROWS][COLS];
        for(int i = 0, c = 0 ; i<15;i++)
            for(int j = 0; j<15;j++,c++)
            {
                button[i][j] = new JButton();
                button[i][j].setBackground(Color.gray);
                button[i][j].setIcon(buttons);
                button[i][j].setName("safe");
                button[i][j].setText("0");
                if(bombMaker()>DIFFICULTY)
                {
                    bombCounter++;
                    button[i][j].setName("fail");
                    revalidate();
                }

                button[i][j].setBorderPainted(false);
                button[i][j].addMouseListener((new ButtonListener()));
                panel.add(button[i][j]);
            }
        this.add(panel);
    }

    // set the button numbers in proximity to the bombs
    public void initNumbers()
    {
        for(int i = 0, c = 0 ; i<15;i++)
            for(int j = 0; j<15;j++,c++)
            {
                if(button[i][j].getName().equalsIgnoreCase("fail"))
                {
                    counter(i,j-1);
                    counter(i,j+1);
                    counter(i-1,j);
                    counter(i-1,j-1);
                    counter(i-1,j+1);
                    counter(i+1,j);
                    counter(i+1,j-1);
                    counter(i+1,j+1);
                }
            }
    }

    public void counter(int i, int j)
    {
        try
        {
            int counter = Integer.parseInt(button[i][j].getText());
            counter++;
            button[i][j].setText(counter+"");
        }
        catch(ArrayIndexOutOfBoundsException e) {}
    }

    // randomly make bombs within the grid
    public int bombMaker()
    {
        int temp = (int) (Math.random()*100)+1;
        return temp;
    }

    // action listener for when the buttons have been pressed
    class ButtonListener implements MouseListener
    {
        @Override
        public void mousePressed(MouseEvent e)
        {
            JButton tempButton = (JButton) e.getSource();

            if (SwingUtilities.isRightMouseButton(e))
            {
                if (tempButton.getIcon().equals(RB))
                    tempButton.setIcon(buttons);
                else if (tempButton.getIcon().equals(buttons))
                    tempButton.setIcon(RB);
            }
            else
            {
                counter++;
                count.setText("   "+counter);

                if(tempButton.getText().equals("n"))
                {
                    counter--;
                    count.setText("   "+counter);
                }

                if (tempButton.getText().equalsIgnoreCase("0"))
                    tempButton.setIcon(pass);
                if (tempButton.getText().equalsIgnoreCase("1"))
                    tempButton.setIcon(one);
                if (tempButton.getText().equalsIgnoreCase("2"))
                    tempButton.setIcon(two);
                if (tempButton.getText().equalsIgnoreCase("3"))
                    tempButton.setIcon(three);
                if (tempButton.getText().equalsIgnoreCase("4"))
                    tempButton.setIcon(four);
                if (tempButton.getText().equalsIgnoreCase("5"))
                    tempButton.setIcon(five);
                if (tempButton.getText().equalsIgnoreCase("6"))
                    tempButton.setIcon(six);
                if (tempButton.getText().equalsIgnoreCase("7"))
                    tempButton.setIcon(seven);
                if (tempButton.getText().equalsIgnoreCase("8"))
                    tempButton.setIcon(eight);
                if (tempButton.getName().equalsIgnoreCase("fail"))
                {
                    tempButton.setIcon(fail);
                    bombHit(tempButton);
                }
            }
            if(!tempButton.getText().equals("n"))
            checkWin();
            tempButton.setText("n");
        }

        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
        @Override
        public void mouseClicked(MouseEvent e) {}
    }

    // respond to user clicking on a bomb
    public void bombHit(JButton tempButton)
    {
        int n = JOptionPane.showConfirmDialog(this, "Boom.\nWould you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.NO_OPTION)
            System.exit(0);
        else
            reset();
    }

    // the user has selected to play the game again
    public void reset()
    {
        for (int i = 0, c = 0; i < ROWS; i++)
            for (int j = 0; j < COLS; j++, c++)
            {
                panel.remove(button[i][j]);
            }
            remove(panel);
            initGrid();
            initNumbers();
            counter = 0;
            count.setText("   "+counter);
            this.revalidate();
    }

    // options to the player if he wins
    public void checkWin()
    {
        winCondition--;
        if (winCondition==0)
            endGame();
    }
    public void endGame()
    {
        int n = JOptionPane.showConfirmDialog(this, "Congratulations! You've Won!\nWould you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.NO_OPTION)
            System.exit(0);
        else
            reset();
    }
}
