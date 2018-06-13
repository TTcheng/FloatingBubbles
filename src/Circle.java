import java.awt.*;

public class Circle {
    private int x, y;
    private int r;
    private Color color;
    private Direction direction;
    private int offsetX, offsetY;

    public Circle() {
        this.r = getRandomRadius();
        this.color = getRandomColor();
        setOffset();
        direction = getDirectionByOffset();
    }

    //根据撞上的边界，反向
    public void redirection(Border border) {
        switch (direction) {
            case EAST:
            case NORTH:
            case WEST:
            case SOUTH:
                //垂直撞上墙，直接反向
                offsetX = -offsetX;
                offsetY = -offsetY;
                direction = getDirectionByOffset();
                break;
            case NORTH_EAST:
            case SOUTH_EAST:
            case NORTH_WEST:
            case SOUTH_WEST:
                if (border == Border.TOP || border == Border.BOTTOM) {
                    offsetY = -offsetY;
                } else {//Border.RIGHT or Border.LEFT
                    offsetX = -offsetX;
                }
                direction = getDirectionByOffset();
                break;
            default:
                break;
        }
    }

    private int getRandomRadius() {
        return (int) (Math.random() * 16 + 5);
    }

    //设置跳转方向，只有八种：上、下、左、右、左上，左下、右上、右下
    private void setOffset() {
        offsetX = getRandomOffset();
        offsetY = getRandomOffset();
        while (true) {
            if (offsetX == 0 && offsetY == 0) {//不能同时为0
                setOffset();
            } else break;
        }
    }

    //random return a digit of {-1,0,1}
    private int getRandomOffset() {
        double randomD = Math.random() * 3;//(0,3)
        if (randomD < 1) {
            return 1;
        } else if (randomD < 2) {
            return 0;
        } else {
            return -1;
        }
    }


    private Color getRandomColor() {
        return new Color(
                (int) (Math.random() * 255),
                (int) (Math.random() * 255),
                (int) (Math.random() * 255)
        );
    }

    private Direction getDirectionByOffset() {
        if (offsetX == 0 && offsetY == 1) {
            return Direction.NORTH;
        } else if (offsetX == 0 && offsetY == -1) {
            return Direction.SOUTH;
        } else if (offsetX == -1 && offsetY == 0) {
            return Direction.WEST;
        } else if (offsetX == 1 && offsetY == 0) {
            return Direction.EAST;
        } else if (offsetX == 1 && offsetY == 1) {
            return Direction.NORTH_EAST;
        } else if (offsetX == -1 && offsetY == 1) {
            return Direction.NORTH_WEST;
        } else if (offsetX == 1 && offsetY == -1) {
            return Direction.SOUTH_EAST;
        } else if (offsetX == -1 && offsetY == -1) {
            return Direction.SOUTH_WEST;
        }
        return null;
    }

    //setter and setter
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

    public int getR() {
        return r;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }


    public Color getColor() {
        return color;
    }


    @Override
    public String toString() {
        return "Circle{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", color=" + color +
                ", offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                '}';
    }
}
