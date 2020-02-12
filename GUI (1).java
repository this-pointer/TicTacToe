// ============================================================================
//     Taken From: http://programmingnotes.org/
// ============================================================================
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JFrame implements ActionListener
{
    // setting up ALL the variables
    JFrame window = new JFrame("Nandy Tic Tac Toe");

    JMenuBar mnuMain = new JMenuBar();
    JMenuItem   mnuNewGame = new JMenuItem("  New Game"), 
    mnuGameTitle = new JMenuItem("|Tic Tac Toe|  "),
    mnuStartingPlayer = new JMenuItem(" Starting Player"),
    mnuExit = new JMenuItem("    Quit");
    JButton btnEmpty[];
    JPanel  pnlNewGame = new JPanel(),
    pnlNorth = new JPanel(),
    pnlSouth = new JPanel(),
    pnlTop = new JPanel(),
    pnlBottom = new JPanel(),
    pnlPlayingField = new JPanel();
    JPanel radioPanel = new JPanel();

    private JRadioButton SelectX = new JRadioButton("User Plays X", false);
    private  JRadioButton SelectO = new JRadioButton("User Plays O", false);
    private ButtonGroup radioGroup;
    private  String startingPlayer= "";
    final int X = 800, Y = 480, color = 190; // size of the game window
    private boolean inGame = false;
    private boolean win = false;
    private boolean btnEmptyClicked = false;
    private boolean setTableEnabled = false;
    private String message;
    private Font font = new Font("Rufscript", Font.BOLD, 100);
    private int remainingMoves = 1;
    private String[][] board;
    private int n;
    private int players;

    //===============================  GUI  ========================================//
    public GUI() //This is the constructor
    {
        String boardSize = JOptionPane.showInputDialog("Input Board size");
        n = Integer.parseInt(boardSize);
        String numPlayer = JOptionPane.showInputDialog("How Many Players? (1 or 2)");
        players = Integer.parseInt(numPlayer);
        if(players != 1)
            players = 2;
        btnEmpty = new JButton[(n*n)+1];
        //Setting window properties:
        window.setSize(X, Y);
        window.setLocation(300, 180);
        window.setResizable(true);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  

        //------------  Sets up Panels and text fields  ------------------------//
        // setting Panel layouts and properties
        pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));

        pnlNorth.setBackground(new Color(70, 70, 70));
        pnlSouth.setBackground(new Color(color, color, color));

        pnlTop.setBackground(new Color(color, color, color));
        pnlBottom.setBackground(new Color(color, color, color));

        pnlTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setLayout(new FlowLayout(FlowLayout.CENTER));

        radioPanel.setBackground(new Color(color, color, color));
        pnlBottom.setBackground(new Color(color, color, color));
        radioPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Who Goes First?"));

        // adding menu items to menu bar
        mnuMain.add(mnuGameTitle);
        mnuGameTitle.setEnabled(false);
        mnuGameTitle.setFont(new Font("Purisa",Font.BOLD,18));
        mnuMain.add(mnuNewGame);
        mnuNewGame.setFont(new Font("Purisa",Font.BOLD,18));
        mnuMain.add(mnuStartingPlayer);
        mnuStartingPlayer.setFont(new Font("Purisa",Font.BOLD,18));
        mnuMain.add(mnuExit);
        mnuExit.setFont(new Font("Purisa",Font.BOLD,18));//---->Menu Bar Complete

        // adding X & O options to menu
        SelectX.setFont(new Font("Purisa",Font.BOLD,18));
        SelectO.setFont(new Font("Purisa",Font.BOLD,18));
        radioGroup = new ButtonGroup(); // create ButtonGroup
        radioGroup.add(SelectX); // add plain to group
        radioGroup.add(SelectO);
        radioPanel.add(SelectX);
        radioPanel.add(SelectO);

        // adding Action Listener to all the Buttons and Menu Items
        mnuNewGame.addActionListener(this);
        mnuExit.addActionListener(this);
        mnuStartingPlayer.addActionListener(this);

        // setting up the playing field
        pnlPlayingField.setLayout(new GridLayout(n, n, 2, 2));
        pnlPlayingField.setBackground(Color.red);
        board = new String[n][n];
        btnEmpty[0] = new JButton();
        for(int x=1; x <= (n*n); ++x)   
        {
            btnEmpty[x] = new JButton();
            if(n%2 == 1){
                if(x%2 == 0)
                    btnEmpty[x].setBackground(new Color(0, 0, 0));
                else 
                    btnEmpty[x].setBackground(new Color(240, 240, 240));
            }
            else{
                btnEmpty[x].setBackground(new Color(120, 40, 40));
            }
            btnEmpty[x].addActionListener(this);
            pnlPlayingField.add(btnEmpty[x]);
            btnEmpty[x].setEnabled(setTableEnabled);
        }

        // adding everything needed to pnlNorth and pnlSouth
        pnlNorth.add(mnuMain);
        BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);

        // adding to window and Showing window
        window.add(pnlNorth, BorderLayout.NORTH);
        window.add(pnlSouth, BorderLayout.CENTER);
        window.setVisible(true);
    }// End GUI

    // ===========  Start Action Performed  ===============//
    public void actionPerformed(ActionEvent click)  
    {
        // get the mouse click from the user
        Object source = click.getSource();

        // check if a button was clicked on the gameboard
        for(int currentMove=1; currentMove <= (n*n); ++currentMove) 
        {
            if(source == btnEmpty[currentMove] && remainingMoves < (n*n)+1)  
            {
                int computerPlay = 0;
                btnEmptyClicked = true;
                if(players == 2){
                    BusinessLogic.GetMove(currentMove, remainingMoves, font, 
                        btnEmpty, startingPlayer);    } 
                else{
                    ++remainingMoves;
                    BusinessLogic.OnePlayerMove(currentMove, remainingMoves, font, btnEmpty, startingPlayer);
                    if(!CheckWin().equals(" "))
                        CheckBoard();
                    else{
                        computerPlay = computerMove()+1;
                        //System.out.println(computerPlay);
                        btnEmpty[computerPlay].setFont(font);
                        if(startingPlayer.equals("X"))
                            btnEmpty[computerPlay].setText("O");
                        else
                            btnEmpty[computerPlay].setText("X");
                    }  
                btnEmpty[currentMove].setEnabled(false);
            }
                if(computerPlay != 0)
                    btnEmpty[computerPlay].setEnabled(false);
                pnlPlayingField.requestFocus();
                ++remainingMoves;
            }
        }

        // if a button was clicked on the gameboard, check for a winner
        if(btnEmptyClicked) 
        {
            CheckBoard();
        }

        // check if the user clicks on a menu item
        if(source == mnuNewGame)    
        {
            System.out.println(startingPlayer);
            BusinessLogic.ClearPanelSouth(pnlSouth,pnlTop,pnlNewGame,
                pnlPlayingField,pnlBottom,radioPanel);
            if(startingPlayer.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please Select a Starting Player", 
                    "Oops..", JOptionPane.ERROR_MESSAGE);
                BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);
            }
            else
            {
                if(inGame)  
                {
                    int option = JOptionPane.showConfirmDialog(null, "If you start a new game," +
                            " your current game will be lost..." + "n" +"Are you sure you want to continue?"
                        , "New Game?" ,JOptionPane.YES_NO_OPTION);
                    if(option == JOptionPane.YES_OPTION)    
                    {
                        inGame = false;
                        startingPlayer = "";
                        setTableEnabled = false;
                    }
                    else
                    {
                        BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);
                    }
                }
                // redraw the gameboard to its initial state
                if(!inGame) 
                {
                    RedrawGameBoard();
                }
            }       
        }       
        // exit button
        else if(source == mnuExit)  
        {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", 
                    "Quit" ,JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        }
        // select X or O player 
        else if(source == mnuStartingPlayer)  
        {
            if(inGame)  
            {
                JOptionPane.showMessageDialog(null, "Cannot select a new Starting "+
                    "Player at this time.nFinish the current game, or select a New Game "+
                    "to continue", "Game In Session..", JOptionPane.INFORMATION_MESSAGE);
                BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);
            }
            else
            {
                setTableEnabled = true;
                BusinessLogic.ClearPanelSouth(pnlSouth,pnlTop,pnlNewGame,
                    pnlPlayingField,pnlBottom,radioPanel);

                SelectX.addActionListener(new RadioListener());
                SelectO.addActionListener(new RadioListener());
                radioPanel.setLayout(new GridLayout(2,1));

                radioPanel.add(SelectX);
                radioPanel.add(SelectO);
                pnlSouth.setLayout(new GridLayout(2, 1, 2, 1));
                pnlSouth.add(radioPanel);
                pnlSouth.add(pnlBottom);
            }
        }
        pnlSouth.setVisible(false); 
        pnlSouth.setVisible(true);  
    }// End Action Performed

    // ===========  Start RadioListener  ===============//  
    private class RadioListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent event) 
        {
            JRadioButton theButton = (JRadioButton)event.getSource();
            if(theButton.getText().equals("User Plays X")) 
            {
                startingPlayer = "X";
            }
            if(theButton.getText().equals("User Plays O"))
            {
                startingPlayer = "O";
            }

            // redisplay the gameboard to the screen
            pnlSouth.setVisible(false); 
            pnlSouth.setVisible(true);          
            RedrawGameBoard();
        }
    }// End RadioListener
    /*
    ----------------------------------
    Start of all the other methods. |
    ----------------------------------
     */
    private void RedrawGameBoard()  
    {
        BusinessLogic.ClearPanelSouth(pnlSouth,pnlTop,pnlNewGame,
            pnlPlayingField,pnlBottom,radioPanel);
        BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);       

        remainingMoves = 1;

        for(int x=1; x <= (n*n); ++x)   
        {
            btnEmpty[x].setText("");
            btnEmpty[x].setEnabled(setTableEnabled);
        }

        win = false;        
    }

    public void setBoard(int x, String str){
        board[(x-1)/n][(x-1)%n] = str;
    }

    public void CheckBoard(){
        //inGame = true;
        String s = CheckWin();
        if(!s.equals(" ")){
            int option = 0;
            if(s.equals("TIE")){
                option = JOptionPane.showConfirmDialog(null, "TIE!"
                , "New Game?" ,JOptionPane.YES_NO_OPTION);
            }
            else{
                option = JOptionPane.showConfirmDialog(null, "Player " +s+
                    " won the game! Would you like to play again?"
                , "New Game?" ,JOptionPane.YES_NO_OPTION);}
            if(option == JOptionPane.YES_OPTION)    
            {
                RedrawGameBoard()  ;
            }
            else

                System.exit(0);
        }
        //btnEmptyClicked = false;    
    }

    public String CheckWin() 
    {   
        for(int z = 1; z<= (n*n); z++){
            setBoard(z,btnEmpty[z].getText());
        }

        for(int i = 0; i < board.length; i++){
            for(int x = 0; x+1 < board.length; x++){
                if(!board[i][x].equals(board[i][x+1]) || board[i][x].equals(""))
                    x = board.length+10;
                if(x+2 == board.length)
                    return board[i][x];
            }
        }

        for(int c = 0; c < board.length; c++){
            for(int r = 0; r+1 < board.length; r++){
                if(!board[r][c].equals(board[r+1][c]) || board[r][c].equals(""))
                    r = board.length+10;
                if(r+2 == board.length)
                    return board[r][c];
            }
        }

        for(int j = 0; j+1 < board.length; j++){
            if(!board[j][j].equals(board[j+1][j+1]) || board[j][j].equals(""))
                j = board.length+10;
            if(j+2 == board.length)
                return board[0][0];
        }

        int a = 0;
        for(int b = board.length-1; b>0; b--){
            if(!board[a][b].equals(board[a+1][b-1]) || board[a][b].equals(""))
                b = 0;
            if(b == 1)
                return board[0][board.length-1];
            a++;
        }
        int p;
        for(p = 0; p < board.length; p++){
            for(int e = 0; e < board.length; e++){
                if(board[p][e].equals("")){
                    e = board.length;
                    p = board.length+10;}
            }
        }
        if(p < board.length+10)
            return "TIE";
        return " ";

    }

    public int computerMove(){ 
        int r, c, stop, temp;

        for(int z = 1; z<= (n*n); z++){
            setBoard(z,btnEmpty[z].getText());
        }

        for(r = 0; r < board.length; r++){
            temp = 0;
            stop = 0;
            for(c = 0; c < board.length; c++){
                if (board[r][c].equals("")){
                    temp = c;
                    stop++;
                }
                if(stop==2)
                    c = board.length;
            }
            if(stop == 1){
                if(temp == 0)
                    board[r][0] = board[r][1];
                else board [r][temp] = board[r][0];
                for(c = 0; c+1< board.length; c++){
                    if(!board[r][c].equals(board[r][c+1])){
                        c = board.length + 10;     
                        board [r][temp] = "";
                    }
                    if(c+2 == board.length){
                        return (n*r)+temp;
                    }
                }
            }
        }

        r = 0; 
        c = 0;
        for(c = 0; c < board.length; c++){
            temp = 0;
            stop = 0;
            for(r = 0; r < board.length; r++){
                if (board[r][c].equals("")){
                    stop++;
                    temp = r;
                }
                if(stop==2)
                    r = board.length;
            }
            if(stop == 1){
                if(temp == 0)
                    board[0][c] = board[1][c];
                else board [temp][c] = board[0][c];
                for(r = 0; r+1< board.length; r++){
                    if(!board[r][c].equals(board[r+1][c])){
                        r = board.length + 10;     
                        board [temp][c] = "";
                    }
                    if(r+2 == board.length){
                        return (n*temp)+c;
                    }
                }
            }
        }

        stop = 0;
        temp = 0;
        for(int j = 0; j < board.length; j++){
            if(board[j][j].equals("")){
                stop++;
                temp = j;
            }
            if(stop == 2)
                j = board.length;
        }
        if(stop == 1){
            if(temp == 0)
                board[0][0] = board[1][1];
            else board [temp][temp] = board[0][0];
            for(r = 0; r+1< board.length; r++){
                if(!board[r][r].equals(board[r+1][r+1])){
                    r = board.length + 10;     
                    board [temp][temp] = "";
                }
                if(r+2 == board.length){
                    return ((n+1)*temp);
                }
            }
        }

        stop = 0;
        temp = 0;
        int temp2 = 0;
        c = 0;
        for(r = board.length-1; r>=0; r--){
            if(board[r][c].equals("")){
                stop++;
                temp = r;
                temp2 = c;
            }
            if(stop == 2)
                r = -1;
            c++;
        }
        if(stop == 1){
            if(temp == board.length-1)
                board[temp][0] = board[temp-1][1];
            else board [temp][temp2] = board[board.length-1][0];
            c = 0;
            for(r = board.length-1; r>0; r--){
                if(!board[r][c].equals(board[r-1][c+1])){
                    r = 0;     
                    board [temp][temp2] = "";
                }
                if(r == 1){
                    return (n*temp)+temp2;
                }
                c++;
            }
        }

        for(int i = 0; i < 1000; i++){
            int random = (int)(Math.random()*(n*n));
            if(board[random/board.length][random%board.length].equals("")){
                return random;
            }
        }
        System.out.println("ERROR");
        CheckBoard();
        return -1;
    }
}   