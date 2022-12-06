package com.codenjoy.dojo.snake.client;


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyAlgo {
    private List<LeePoint> obstacles=new ArrayList<>();
    private Point head;
    private Point tail;
    private Point apple;
    private Point stone;
    private int boardx;
    private int boardy;

    public MyAlgo(Board board) {

        this.head=board.getHead();
        this.tail=board.getTail();
        this.stone=board.getStones().get(0);
        if(this.head!=null){
            boardx=head.getX();
            boardy=head.getY();
        }
        else{
            boardx=9;
            boardy=8;
        }
        this.apple=board.getApples().get(0);
        List<Point> barriers = board.getBarriers();
        barriers.stream().map(b -> new LeePoint(b.getX(), b.getY())).forEach(lpoint->obstacles.add(lpoint));

    }

    public Direction solve(){
        int appleX=apple.getX();
        int appleY=apple.getY();
        int stoneX=stone.getX();
        int stoneY=stone.getY();

        LeePoint to= LeePoint.of(appleX,appleY);
        LeePoint from= LeePoint.of(boardx,boardy);
        Lee lee = new Lee(15, 15);

        LeePoint tailsnake = LeePoint.of(tail.getX(), tail.getY());

        LeePoint stone = LeePoint.of(stoneX,stoneY);



        Optional<List<LeePoint>> path = lee.trace(from, to, obstacles);
        Optional<List<LeePoint>> path2 = lee.trace(from, tailsnake, obstacles);
        Optional<List<LeePoint>> path3 = lee.trace(from, stone, obstacles);

        if (path.isPresent()) {
            return move(path,from);
        }
        else if(path2.isPresent()){
            return move(path2,from);
        }
        else if(path3.isPresent()){
            return move(path3,from);
        }

        return Direction.ACT;
    }

    public Direction move(Optional<List<LeePoint>> path,LeePoint from){
        Direction d=Direction.UP;

        LeePoint next_step = path.get().get(1);
        LeePoint curr=from;
        if (next_step.x < curr.x)  d= Direction.LEFT;
        else if (next_step.x > curr.x) d= Direction.RIGHT;
        else if (next_step.y > curr.y) d= Direction.UP;
        else if (next_step.y < curr.y) d= Direction.DOWN;


        return d;
    }


}