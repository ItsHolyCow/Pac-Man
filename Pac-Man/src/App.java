import javax.swing.JFrame;   

public class App {
    public static void main(String[] args) throws Exception {
        int rowCount=21;
        int colCount=19;
        int tileSize=32;
        int boardHeight=rowCount*tileSize;
        int boardWidth=colCount*tileSize;

        javax.swing.JFrame frame=new JFrame("Pac-Man");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        Pacman pacmanGame=new Pacman();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}
