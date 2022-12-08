package com.codenjoy.dojo.snake.client;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Lee {

    private final List<LeePoint> deltas = Arrays.asList(
            LeePoint.of(0, -1), LeePoint.of(-1, 0),
            LeePoint.of(1, 0), LeePoint.of(0, 1));
    private final int width;
    private final int height;
    private final int[][] board;
    private final static int EMPTY = 0;
    private final static int OBSTACLE = -9; // any non-positive number
    private final static int START = -1;    // any non-positive number

    public Lee(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[height][width];
    }

    private int get(LeePoint p) {
        return board[p.y][p.x];
    }

    private void set(LeePoint p, int val) {
        board[p.y][p.x] = val;
    }

    private boolean isOnBoard(LeePoint p) {
        return p.x>=0 && p.y>=0 && p.x<width && p.y<height;
    }

    private boolean isUnvisited(LeePoint p) {
        return get(p) == EMPTY;
    }

    private Stream<LeePoint> neighbours(LeePoint point) {
        return deltas.stream()
                .map(d -> d.move(point))
                .filter(this::isOnBoard);
    }

    private Set<LeePoint> neighboursUnvisited(LeePoint point) {
        return neighbours(point)
                .filter(this::isUnvisited)
                .collect(Collectors.toSet());
    }

    public List<LeePoint> neighboursByValue(LeePoint point, int val) {
        return neighbours(point)
                .filter(p -> get(p) == val)
                .collect(Collectors.toList());
    }


    private List<LeePoint> neighboursByPositiveValue(LeePoint point) {
        return neighbours(point)
                .filter(p -> get(p) >= EMPTY)
                .collect(Collectors.toList());
    }

    private void initializeBoard(List<LeePoint> obstacles) {
        IntStream.range(0, width).boxed().flatMap(x ->
                IntStream.range(0, height).mapToObj(y ->
                        LeePoint.of(x, y)
                )).forEach(p -> set(p, EMPTY));
        obstacles.forEach(p -> set(p, OBSTACLE));
    }

    public Optional<List<LeePoint>> trace(LeePoint start, LeePoint finish, List<LeePoint> obstacles) {
        initializeBoard(obstacles);
        boolean found = false;
        set(start, START);
        Set<LeePoint> curr = new HashSet<>();
        curr.add(start);
        int[] counter = { 0 };
        while (!(curr.isEmpty() || found)) {
            counter[0]++;
            Set<LeePoint> next = curr.stream()
                    .map(this::neighboursUnvisited)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            next.forEach(p -> set(p, counter[0]));
            found = next.contains(finish);
            curr.clear();
            curr.addAll(next);
        }

        if(!found){

            return Optional.empty();
        }

        return returnPath(start,finish,counter);
    }

    public Optional<List<LeePoint>> returnPath( LeePoint start, LeePoint finish, int[] counter){
        LinkedList<LeePoint> path = new LinkedList<>();
        path.add(finish);
        set(start, 0);
        LeePoint curr_p = finish;
        while (counter[0] > 0) {
            counter[0]--;
            LeePoint prev_p = neighboursByValue(curr_p, counter[0]).get(0);
            path.addFirst(prev_p);
            curr_p = prev_p;
        }
        return Optional.of(path);
    }



}
