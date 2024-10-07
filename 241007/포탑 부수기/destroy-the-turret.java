import java.util.*;
import java.io.*;

public class Main {

    private static int N, M;

    private static int[][] board;
    private static Map<String, Port> portMap;

    private static int[] dx = {0,1,0,-1}; // 우/하/좌/상으로 우선
    private static int[] dy = {1,0,-1,0};

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        portMap = new HashMap<>();
        board = new int[N][M];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < M; j++) {
                int value = Integer.parseInt(st.nextToken());
                board[i][j] = value;
                if(value != 0) {
                    portMap.put(i+" "+j, new Port(i,j,value));
                }
            }
        }

        int count = 0;
        while(K-- > 0 || portMap.size() > 1) {
            count++;
            attack(count);
            hill(count);
        }

        List<Port> portList = new ArrayList<>(portMap.values());
        Collections.sort(portList);
        System.out.println(portList.get(portList.size()-1).power);
    }

    private static void attack(int count) {
        List<Port> portList = new ArrayList<>(portMap.values());
        Collections.sort(portList);

        Port attacker = portList.get(0);
        Port target = portList.get(portList.size()-1);

        attacker.power += N + M;
        board[attacker.x][attacker.y] = attacker.power;
        attacker.used = count;
        attacker.count = count;

        if(!attacker.laizer(attacker.x, attacker.y, target, count)) {
            attacker.portAttack(target, count);
        } 

        // 공격 후 상태 업데이트
        for (Port p : portMap.values()) {
            board[p.x][p.y] = p.power;
        }
    }

    private static void hill(int count) {
        for(Port p : portMap.values()) {
            if(p.count != count) {
                p.power += 1;
                board[p.x][p.y] = p.power;
            }
        }
    }

    private static class Port implements Comparable<Port> {
        int x, y, power;
        int used = 0;
        int count = 0;

        public Port(int x, int y, int power) {
            this.x = x;
            this.y = y;
            this.power = power;
        }

        public boolean laizer(int startX, int startY, Port target, int count) {
            Queue<int[]> queue = new LinkedList<>();
            boolean[][] visited = new boolean[N][M];
            int[][] parent = new int[N][M];
            
            queue.offer(new int[]{startX, startY});
            visited[startX][startY] = true;
            
            while (!queue.isEmpty()) {
                int[] current = queue.poll();
                int x = current[0], y = current[1];
                
                if (x == target.x && y == target.y) {
                    List<int[]> path = getPath(parent, startX, startY, target.x, target.y);
                    performLaserAttack(path, target, count);
                    return true;
                }
                
                for (int i = 0; i < 4; i++) {
                    int nx = (x + dx[i] + N) % N;
                    int ny = (y + dy[i] + M) % M;
                    
                    if (!visited[nx][ny] && board[nx][ny] != 0) {
                        queue.offer(new int[]{nx, ny});
                        visited[nx][ny] = true;
                        parent[nx][ny] = i;
                    }
                }
            }
            
            return false;
        }
        
        private List<int[]> getPath(int[][] parent, int startX, int startY, int targetX, int targetY) {
            List<int[]> path = new ArrayList<>();
            int x = targetX, y = targetY;
            
            while (!(x == startX && y == startY)) {
                path.add(0, new int[]{x, y});
                int direction = parent[x][y];
                x = (x - dx[direction] + N) % N;
                y = (y - dy[direction] + M) % M;
            }
            
            return path;
        }
        
        private void performLaserAttack(List<int[]> path, Port target, int count) {
            for (int[] pos : path) {
                int attackedX = pos[0], attackedY = pos[1];
                String key = attackedX + " " + attackedY;
                Port attacked = portMap.get(key);
                if (attackedX == x && attackedY == y) continue;

                int attackPower = (attackedX == target.x && attackedY == target.y) ? power : power / 2;
                attacked.power -= attackPower;
                attacked.count = count;

                if (attacked.power <= 0) {
                    board[attackedX][attackedY] = 0;
                    portMap.remove(key);
                }
            }
        }

        public void portAttack(Port target, int count) {
            for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++){
                    int attackedX = (target.x + i + N) % N;
                    int attackedY = (target.y + j + M) % M;

                    if(attackedX == x && attackedY == y) continue;
                    if(board[attackedX][attackedY] == 0) continue;
                    
                    Port attacked = portMap.get(attackedX + " " + attackedY);
                     
                    int attackPower = (i==0 && j==0) ? power : power / 2; 
                    attacked.power -= attackPower;
                    attacked.count = count;

                    if(attacked.power <= 0) {
                        board[attackedX][attackedY] = 0;
                        portMap.remove(attackedX + " " + attackedY);
                    }
                }
            }
        }

        @Override
        public int compareTo(Port op) {
            if(this.power != op.power) return this.power - op.power;
            if(this.used != op.used) return op.used - this.used;
            int thisSum = this.x + this.y, opSum = op.x + op.y;
            if(thisSum != opSum) return opSum - thisSum;
            return op.y - this.y;
        }
    }
}