import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Generator {
    Random rand = new Random();
    Node[][] field;
    boolean[][] check;
    Node root = null;
    int sizeX = 1, sizeY = 1;
    int startX = 0, startY = 0, endX = 0, endY = 0;

    public void SetSize(int x, int y) {
        sizeX = x;
        sizeY = y;
        field = new Node[x][y];
        check=new boolean[x][y];
    }

    public void SetCheck(int x,int y,boolean value){
        check[x][y]=value;
    }

    public void SetStart(int x, int y) {
        startX = x;
        startY = y;
    }

    public void SetEnd(int x, int y) {
        endX = x;
        endY = y;
    }

    public void Start() {
        LinkedList<Node> stack = new LinkedList<>();
        root = new Node(startX, startY);
        stack.push(root);
        while (stack.size() > 0) {
            Node now = stack.pop();
            LinkedList<Integer> directions = new LinkedList<>(List.of(0, 1, 2, 3));
            while (directions.size() > 0) {
                int tIndex = rand.nextInt(directions.size());
                int t = directions.get(tIndex);
                directions.remove(tIndex);

                if (now.neighbor[t] != null)
                    continue;
                int tX = now.x + Node.Xoffset[t], tY = now.y + Node.Yoffset[t];
                if (!validateCoord(tX, tY)) continue;
                if(check[tX][tY])continue;
                if (field[tX][tY] == null) {
                    field[tX][tY] = new Node(tX, tY, now, t);
                    stack.push(field[tX][tY]);
                }
            }
        }

    }

    public boolean validateCoord(int x, int y) {
        return x < sizeX && y < sizeY && x >= 0 && y >= 0;
    }

    //│─┘┴└┤┼├┐┬┌
    public void printConsole() {
        char[][] charField = new char[sizeX][sizeY];
        for(int i=0;i<sizeX;i++){
            for(int j=0;j<sizeY;j++){
                charField[i][j]='　';
            }
        }
        LinkedList<Node> stack = new LinkedList<>();
        stack.push(root);
        while (stack.size() > 0) {
            Node now = stack.pop();
            int flag = 0;
            for (int i = 0; i < 4; i++) {
                if (now.neighbor[i] != null) {
                    if (charField[now.neighbor[i].x][now.neighbor[i].y] == '　')
                        stack.push(now.neighbor[i]);
                    flag += (1 << i);
                }
            }
            switch (flag) {//URDL
                case 0b0:
                    charField[now.x][now.y] = '　';
                    break;
                case 0b1010:
                    charField[now.x][now.y] = '│';
                    break;
                case 0b0101:
                    charField[now.x][now.y] = '─';
                    break;
                case 0b1001:
                    charField[now.x][now.y] = '┘';
                    break;
                case 0b1101:
                    charField[now.x][now.y] = '┴';
                    break;
                case 0b1100:
                    charField[now.x][now.y] = '└';
                    break;
                case 0b1011:
                    charField[now.x][now.y] = '┤';
                    break;
                case 0b1111:
                    charField[now.x][now.y] = '┼';
                    break;
                case 0b1110:
                    charField[now.x][now.y] = '├';
                    break;
                case 0b0011:
                    charField[now.x][now.y] = '┐';
                    break;
                case 0b0111:
                    charField[now.x][now.y] = '┬';
                    break;
                case 0b0110:
                    charField[now.x][now.y] = '┌';
                    break;
                default:
                    charField[now.x][now.y] = '×';
                    break;
            }
        }
        charField[startX][startY]='◇';
        charField[endX][endY]='◆';

        for (char[] i : charField) {
            for (char j : i)
                System.out.print(j);
            System.out.println();
        }
    }

    public static void main(String args[]) {
        Generator gen = new Generator();
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
        gen.Start();
        gen.printConsole();
    }
    public void UpdateViewer(Viewer view){
        LinkedList<Node> stack = new LinkedList<>();
        boolean[][] checks=new boolean[sizeX][sizeY];
        stack.push(root);
        while (stack.size() > 0) {
            Node now = stack.pop();

            int flag = 0;
            for (int i = 0; i < 4; i++) {
                if (now.neighbor[i] != null) {
                    if (!checks[now.neighbor[i].x][now.neighbor[i].y]) {
                        stack.push(now.neighbor[i]);
                        checks[now.neighbor[i].x][now.neighbor[i].y] = true;
                    }
                    flag += (1 << i);
                }
            }
            view.field[now.y][now.x].flag=flag;
            view.field[now.y][now.x].status=2;
        }
    }
}

class Node {
    int x = -1, y = -1;
    Node[] neighbor = new Node[4];//URDL
    static int[] Xoffset = new int[]{0, 1, 0, -1};
    static int[] Yoffset = new int[]{-1, 0, 1, 0};

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Node(int x, int y, Node parent, int direction) {
        this.x = x;
        this.y = y;
        neighbor[(direction + 2) % 4] = parent;
        parent.neighbor[direction] = this;
    }
}
