# 10시 시작 
# 노드추가 2만 
# 색변경 5만
# 색조회 2만
# 점수조회 100
# question 10만 
import collections

class Node :
    def __init__(self, parent_number, number, color, max_depth):
        self.parent_number = parent_number
        self.number = number
        self.color = color
        self.max_depth = max_depth
        self.children = []
        self.colors = collections.defaultdict(int)
        self.colors[color] += 1
    
parents = []
nodes = {}

def plus(line):
    global parents, nodes
    node_number = line[0]
    parent_number = line[1]
    color = line[2]
    max_depth = line[3]
    
    if parent_number == -1:
        new_node = Node(parent_number, node_number, color, max_depth)
        parents.append(new_node)
        nodes[node_number] = new_node
        return

    # 부모노드 찾아서 넣어주기 
    tmp_parent_number = parent_number 
    now_depth = 1
    trace = []

    while True:
        if tmp_parent_number not in nodes:
            return 

        now_depth += 1
        parent_node = nodes[tmp_parent_number]
        trace.append(parent_node)
        if now_depth > parent_node.max_depth :
            return 

        if parent_node.parent_number != -1 :
            tmp_parent_number = parent_node.parent_number
            continue 

        new_node = Node(parent_number, node_number, color, max_depth)
        nodes[parent_number].children.append(new_node)
        nodes[node_number] = new_node

        for p in trace:
            p.colors[color] += 1
        break
        

def change(line):
    global nodes
    node_number = line[0]
    color = line[1]

    queue = collections.deque()
    queue.append(nodes[node_number])

    now_node = nodes[node_number]
    parent_node = nodes[now_node.parent_number]
    parent_node.colors[color] += 1
    for c, count in now_node.colors.items():
        parent_node.colors[c] -= count

    while queue:
        now_node = queue.popleft()
        now_node.color = color
        now_node.colors = collections.defaultdict(int)
        now_node.colors[color] += 1

        for child in now_node.children:
            queue.append(child)

def select_color(line):
    global nodes
    node_number = line[0]
    print(nodes[node_number].color)
        

def select_score():
    global nodes
    score = 0
    for node in nodes.values():
        node_value = 0
        for color, count in node.colors.items():
            if count >= 1:
                node_value += 1
        score += node_value ** 2
    print(score)



PLUS = 100
CHANGE = 200
SELECT_COLOR = 300
SELECT_SOCRE = 400

question = int(input())
for _ in range(question):

    line = list(map(int, input().split()))
    commend = line[0]

    if commend == PLUS:
        plus(line[1:])
    elif commend == CHANGE :
        change(line[1:])
    elif commend == SELECT_COLOR :
        select_color(line[1:])
    elif commend == SELECT_SOCRE :
        select_score()