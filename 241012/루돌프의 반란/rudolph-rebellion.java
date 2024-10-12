import java.util.*;

public class Main {
    static int n, m, santa_count, rudolph_power, santa_power;
    static int[][] board;
    static final int RUDOLPH = -1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        santa_count = scanner.nextInt();
        rudolph_power = scanner.nextInt();
        santa_power = scanner.nextInt();

        int x = scanner.nextInt();
        int y = scanner.nextInt();
        Rudolph rudolph = new Rudolph(x, y, rudolph_power);

        board = new int[n][n];
        board[rudolph.x][rudolph.y] = RUDOLPH;

        Santas santas = new Santas(santa_power);
        for (int i = 0; i < santa_count; i++) {
            int number = scanner.nextInt();
            x = scanner.nextInt();
            y = scanner.nextInt();
            Santa santa = new Santa(number, x, y);
            santas.append(santa);
            board[santa.x][santa.y] = santa.number;
        }

        santas.sort();
        int turn = 0;

        while (santas.has_living() && turn < m) {
            turn++;
            rudolph.move(santas, board, turn);
            santas.move(rudolph, board, turn);
            santas.alive_plus();
        }

        santas.print_points();
        scanner.close();
    }

    static void print_board(int[][] board) {
        for (int[] row : board) {
            System.out.println(Arrays.toString(row));
        }
    }

    static boolean is_out(int x, int y) {
        return x < 0 || x >= n || y < 0 || y >= n;
    }

    static int get_distance(int a, int b, int c, int d) {
        return (int) (Math.pow(Math.abs(a - c), 2) + Math.pow(Math.abs(b - d), 2));
    }

    static class Santas {
        List<Santa> santa_list;
        Map<Integer, Santa> santa_dict;
        int power;
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1}; // 상우하좌

        Santas(int power) {
            this.santa_list = new ArrayList<>();
            this.santa_dict = new HashMap<>();
            this.power = power;
        }

        void print_points() {
            for (Santa santa : santa_list) {
                System.out.print(santa.point + " ");
            }
            System.out.println();
        }

        void sort() {
            santa_list.sort(Comparator.comparingInt(s -> s.number));
        }

        void append(Santa santa) {
            santa_list.add(santa);
            santa_dict.put(santa.number, santa);
        }

        boolean has_living() {
            for (Santa santa : santa_list) {
                if (!santa.is_out) {
                    return true;
                }
            }
            return false;
        }

        void alive_plus() {
            for (Santa santa : santa_list) {
                if (!santa.is_out) {
                    santa.point++;
                }
            }
        }

        Santa get_clost(int x, int y) {
            List<Santa> colsts = new ArrayList<>();
            int min_distance = Integer.MAX_VALUE;
            for (Santa santa : santa_list) {
                if (santa.is_out) continue;
                min_distance = Math.min(min_distance, get_distance(santa.x, santa.y, x, y));
            }

            for (Santa santa : santa_list) {
                if (santa.is_out) continue;
                if (min_distance == get_distance(santa.x, santa.y, x, y)) {
                    colsts.add(santa);
                }
            }
            colsts.sort((s1, s2) -> {
                if (s2.x != s1.x) return Integer.compare(s2.x, s1.x);
                return Integer.compare(s2.y, s1.y);
            });

            return colsts.get(0);
        }

        void check(Santa santa, int[][] board, int move_x, int move_y) {
            if (board[santa.x][santa.y] == 0) {
                board[santa.x][santa.y] = santa.number;
                return;
            }

            Santa located_santa = santa_dict.get(board[santa.x][santa.y]);
            board[santa.x][santa.y] = santa.number;

            located_santa.x += move_x;
            located_santa.y += move_y;

            if (is_out(located_santa.x, located_santa.y)) {
                located_santa.is_out = true;
                return;
            }

            check(located_santa, board, move_x, move_y);
        }

        void move(Rudolph rudolph, int[][] board, int turn) {
            for (Santa santa : santa_list) {
                if (santa.is_out || santa.is_sturned >= turn) {
                    continue;
                }

                int now_distance = get_distance(santa.x, santa.y, rudolph.x, rudolph.y);
                int best_distance = now_distance;
                int[] best_move = null;

                for (int i = 0; i < 4; i++) {
                    int next_x = santa.x + dx[i];
                    int next_y = santa.y + dy[i];

                    if (is_out(next_x, next_y) || board[next_x][next_y] >= 1) {
                        continue;
                    }

                    int next_distance = get_distance(next_x, next_y, rudolph.x, rudolph.y);
                    if (next_distance < best_distance) {
                        best_distance = next_distance;
                        best_move = new int[]{next_x, next_y, i};
                    }
                }

                if (best_move != null) {
                    int next_x = best_move[0], next_y = best_move[1], direction = best_move[2];
                    board[santa.x][santa.y] = 0;
                    santa.x = next_x;
                    santa.y = next_y;

                    if (board[next_x][next_y] == 0) {
                        board[santa.x][santa.y] = santa.number;
                    } else if (board[next_x][next_y] == RUDOLPH) {
                        santa.point += power;
                        santa.is_sturned = turn + 1;

                        direction = (direction + 2) % 4;
                        int move_x = dx[direction], move_y = dy[direction];

                        next_x = santa.x + move_x * power;
                        next_y = santa.y + move_y * power;

                        if (is_out(next_x, next_y)) {
                            santa.is_out = true;
                            continue;
                        }

                        santa.x = next_x;
                        santa.y = next_y;
                        check(santa, board, move_x, move_y);
                    }
                }
            }
        }
    }

    static class Santa {
        int point;
        int number;
        int x;
        int y;
        boolean is_out;
        int is_sturned;

        Santa(int number, int x, int y) {
            this.point = 0;
            this.number = number;
            this.x = x - 1;
            this.y = y - 1;
            this.is_out = false;
            this.is_sturned = 0;
        }
    }

    static class Rudolph {
        int x;
        int y;
        int power;
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        Rudolph(int x, int y, int power) {
            this.x = x - 1;
            this.y = y - 1;
            this.power = power;
        }

        void move(Santas santas, int[][] board, int turn) {
            Santa santa = santas.get_clost(x, y);

            int min_distance = Integer.MAX_VALUE;
            int direction = -1;

            for (int i = 0; i < 8; i++) {
                int next_x = x + dx[i];
                int next_y = y + dy[i];

                if (is_out(next_x, next_y)) {
                    continue;
                }

                int distance = get_distance(next_x, next_y, santa.x, santa.y);
                if (distance <= min_distance) {
                    direction = i;
                    min_distance = distance;
                }
            }

            int next_x = x + dx[direction];
            int next_y = y + dy[direction];

            if (santa.x == next_x && santa.y == next_y) {
                board[x][y] = 0;
                x = next_x;
                y = next_y;
                board[x][y] = RUDOLPH;

                santa.point += power;
                santa.is_sturned = turn + 1;

                santa.x += dx[direction] * power;
                santa.y += dy[direction] * power;

                if (is_out(santa.x, santa.y)) {
                    santa.is_out = true;
                } else {
                    santas.check(santa, board, dx[direction], dy[direction]);
                }
            } else {
                board[x][y] = 0;
                x = next_x;
                y = next_y;
                board[x][y] = RUDOLPH;
            }
        }
    }
}