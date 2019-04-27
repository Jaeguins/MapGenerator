import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class Viewer extends JFrame {
    public static void main(String args[]) throws IOException {
        Generator gen=new Generator();
        CustPanel.sprite=new ImageIcon(ImageIO.read(gen.getClass().getResourceAsStream("sprite.png")));
        Viewer view=new Viewer();
        gen.SetSize(20, 20);
        gen.SetStart(0, 0);
        gen.SetEnd(19, 19);
        for(int i=0;i<gen.sizeX;i++){
            for(int j=0;j<gen.sizeY;j++){
                if((i-10)*(i-10)+(j-10)*(j-10)<25){
                    gen.SetCheck(i,j,true);
                }
            }
        }
        view.setFieldSize(20,20);
        view.setDefaultCloseOperation(EXIT_ON_CLOSE);
        view.setVisible(true);
        gen.Start();
        gen.UpdateViewer(view);
        gen.printConsole();
        view.repaint();

    }
    JPanel back=new JPanel();
    CustPanel[][] field;
    public void setFieldSize(int x,int y){
        this.setSize(30*x+20,30*y+20);
        back.setSize(30*x,30*y);
        back.removeAll();
        field=new CustPanel[x][y];
        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                field[i][j]=new CustPanel(i,j);
                back.add(field[i][j]);
            }
        }
        back.repaint();
    }
    public Viewer(){
        add(back);
        back.setLayout(null);
    }
}
class CustPanel extends JPanel{
    public static ImageIcon sprite;
    int x,y,flag=0,status=0;
    public CustPanel(int x,int y){
        setBorder(new EmptyBorder(0,0,0,0));
        this.x=x;
        this.y=y;
        setSize(30,30);
        setLocation(x*30,y*30);
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(status==2)
            g.drawImage(sprite.getImage(),-(flag/4)*30,-(flag%4)*30,120,120,this);
        else
            g.drawImage(sprite.getImage(),0,0,120,120,this);
        switch(status){
            case 0:
                setBackground(Color.GREEN);
                break;
            case 1:
                setBackground(Color.RED);
                break;
            case 2:
                setBackground(new Color(255,255,255,0));
                break;
            default:
                setBackground(Color.MAGENTA);
                break;
        }
    }
}
