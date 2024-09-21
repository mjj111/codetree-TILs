import collections

class Rail:
    def __init__(self, size):
        self.size = size
        self.now_index = 0
        # 빈 자리를 포함하지 않고 초밥이 있는 자리만 저장
        self.sits = collections.defaultdict(lambda: collections.defaultdict(int))

    def get_foods(self, x):
        location = (x - self.now_index) % self.size
        return self.sits[location]

    def put(self, x, name):
        location = (x - self.now_index) % self.size
        self.sits[location][name] += 1

    def remove_food(self, location, name):
        # 초밥을 다 먹으면 해당 자리의 초밥 정보를 삭제하여 메모리 절약
        if self.sits[location][name] == 0:
            del self.sits[location][name]
        if not self.sits[location]:
            del self.sits[location]

    def getCount(self):
        return sum(sum(food_count.values()) for food_count in self.sits.values())

class Clinet:
    def __init__(self, x, name, n):
        self.x = x
        self.name = name
        self.n = n

    def __hash__(self):
        return hash((self.x, self.name))

    def __eq__(self, other):
        return (self.x, self.name) == (other.x, other.name)

def rotate(t):
    global now
    rotate_amount = t - now
    now = t
    rail.now_index = (rail.now_index + rotate_amount) % rail.size

    done_clients = []
    for client in clients:
        foods = rail.get_foods(client.x)
        while client.n > 0 and foods[client.name] > 0:
            client.n -= 1
            foods[client.name] -= 1

        rail.remove_food(client.x, client.name)

        if client.n == 0:
            done_clients.append(client)

    for client in done_clients:
        clients.remove(client)

def offer_food(t, x, name):
    rotate(t)
    rail.put(x, name)
    update_clients()

def sit(t, x, name, n):
    rotate(t)
    new_client = Clinet(x, name, n)
    clients.add(new_client)
    update_clients()

def update_clients():
    done_clients = []
    for client in clients:
        foods = rail.get_foods(client.x)
        while client.n > 0 and foods[client.name] > 0:
            client.n -= 1
            foods[client.name] -= 1

        rail.remove_food(client.x, client.name)

        if client.n == 0:
            done_clients.append(client)

    for client in done_clients:
        clients.remove(client)

def take_picture(t):
    rotate(t)
    print(f"{len(clients)} {rail.getCount()}")

L, Q = map(int, input().split())
clients = set()
rail = Rail(L)
now = 0

for _ in range(Q):
    line = input().split()
    command = int(line[0])

    if command == 100:
        t = int(line[1])
        x = int(line[2])
        name = line[3]
        offer_food(t, x, name)

    elif command == 200:
        t = int(line[1])
        x = int(line[2])
        name = line[3]
        n = int(line[4])
        sit(t, x, name, n)

    elif command == 300:
        t = int(line[1])
        take_picture(t)