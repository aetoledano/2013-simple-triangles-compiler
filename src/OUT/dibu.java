package OUT;

import java.util.ArrayList;

public class dibu {

    static final TriangleView plattform = new TriangleView();
    static TriangleDrawingPanel p = plattform.getPanel();
    static ArrayList<Triangle> tList = new ArrayList<Triangle>();

    public static void Reiniciar() {
        tList.clear();
        plattform.setVisible(true);
    }

    public static void NewTriangle(int MemPosition) {
        plattform.setVisible(true);
        InOut.Write("Added new triangle, MemPosition -> " + MemPosition, null);
        tList.add(new Triangle(MemPosition));
    }

    public static void InitTriangle(int memPosition, int dim, int x, int y) {
        Triangle selection = selectTriangle(memPosition);
        if (selection != null) {
            InOut.Write("Triangle " + memPosition + " Initialized Coordinates: (" + x + "," + y + ") Dimension: " + dim, null);
            selection.setDim(dim);
            selection.setX(x);
            selection.setY(y);
            selection.setInit(true);
        }
        ActualizeList();
    }

    public static void MoveTriangle(int memPosition, int x, int y) {
        Triangle selection = selectTriangle(memPosition);
        if (selection != null) {
            p.moveTriangle(selection, x, y);
            InOut.Write("Triangle " + memPosition + " moved to Coordinates: (" + x + "," + y + ")", null);
            selection.setX(x);
            selection.setY(y);
        }
        ActualizeList();
    }

    private static Triangle selectTriangle(int memPosition) {
        for (Triangle t : tList) {
            if (t.getMemDir() == memPosition) {
                return t;
            }
        }
        return null;
    }

    private static void ActualizeList() {
        p.setList(tList);
        p.Pinta();
    }

    public static class Triangle {

        int memDir;
        boolean init;
        int dim;
        int x;
        int y;

        public Triangle(int memDir) {
            this.memDir = memDir;
            dim = 0;
            x = 0;
            y = 0;
            init = false;
        }

        public int getMemDir() {
            return memDir;
        }

        public boolean isInit() {
            return init;
        }

        public void setInit(boolean init) {
            this.init = init;
        }

        public int getDim() {
            return dim;
        }

        public void setDim(int dim) {
            this.dim = dim;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

    }

}
