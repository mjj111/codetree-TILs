MAX_N = 100001
MAX_D = 22

n, q = 0, 0
a = [0] * MAX_N  # 권한
p = [0] * MAX_N  # 부모
val = [0] * MAX_N  # 알림 개수
noti = [False] * MAX_N  # 알림 상태
nx = [[0 for _ in range(MAX_D)] for _ in range(MAX_N)]  # 자손 정보

def init(inputs):
    global n, a, p, val, nx
    for i in range(1, n + 1):
        p[i] = inputs[i]
        a[i] = inputs[i + n], 20)  # 권한 최대값 20으로 제한
    
    for i in range(1, n + 1):
        cur = i
        x = a[i]
        nx[cur][x] += 1
        while p[cur] and x:
            cur = p[cur]
            x -= 1
            if x:
                nx[cur][x] += 1
            val[cur] += 1

def toggle_noti(chat):
    cur = p[chat]
    num = 1
    while cur:
        for i in range(num, 22):
            val[cur] += nx[chat][i] if noti[chat] else -nx[chat][i]
            if i > num:
                nx[cur][i - num] += nx[chat][i] if noti[chat] else -nx[chat][i]
        if noti[cur]:
            break
        cur = p[cur]
        num += 1
    noti[chat] = not noti[chat]

def change_power(chat, power):
    bef_power = a[chat]
    power = min(power, 20)
    a[chat] = power

    nx[chat][bef_power] -= 1
    if not noti[chat]:
        cur = p[chat]
        num = 1
        while cur:
            if bef_power >= num:
                val[cur] -= 1
            if bef_power > num:
                nx[cur][bef_power - num] -= 1
            if noti[cur]:
                break
            cur = p[cur]
            num += 1

    nx[chat][power] += 1
    if not noti[chat]:
        cur = p[chat]
        num = 1
        while cur:
            if power >= num:
                val[cur] += 1
            if power > num:
                nx[cur][power - num] += 1
            if noti[cur]:
                break
            cur = p[cur]
            num += 1

def change_parent(chat1, chat2):
    bef_noti1, bef_noti2 = noti[chat1], noti[chat2]

    if not noti[chat1]:
        toggle_noti(chat1)
    if not noti[chat2]:
        toggle_noti(chat2)

    p[chat1], p[chat2] = p[chat2], p[chat1]

    if not bef_noti1:
        toggle_noti(chat1)
    if not bef_noti2:
        toggle_noti(chat2)

def print_count(chat):
    print(val[chat])

n, q = map(int, input().split())
inputs = list(map(int, input().split()))
init(inputs)

for _ in range(q - 1):
    query = list(map(int, input().split()))
    if query[0] == 200:
        toggle_noti(query[1])
    elif query[0] == 300:
        change_power(query[1], query[2])
    elif query[0] == 400:
        change_parent(query[1], query[2])
    elif query[0] == 500:
        print_count(query[1])