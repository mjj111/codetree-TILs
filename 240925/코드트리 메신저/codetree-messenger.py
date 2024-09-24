import collections
class Node :
    def __init__(self, number):
        self.number = number
        self.parent = None
        self.left = None
        self.right = None
        self.authority = -1
        self.notification = True

    def child_change(self, old_child, new_child):
        if self.left == old_child:
            self.left = new_child

        elif self.right == old_child:
            self.right = new_child

    def put_parent(self, parent):
        self.parent = parent

    def put_authority(self, authority):
        self.authority = authority

    def put_child(self, child):
        if self.left:
            self.right = child
        else :
            self.left = child 

    def notification_change(self):
        if self.notification :
            self.notification = False
        else :
            self.notification = True
    
def get_available_notification_count(node, count=0):
    q = collections.deque()
    q.append(node)
    total = 0

    while q :
        count += 1
        lenq = len(q)
        for _ in range(lenq):

            now = q.popleft()
            if now.left and now.left.notification:
                q.append(now.left)
                if now.left.authority >= count : total += 1

            if now.right and now.right.notification:
                q.append(now.right)
                if now.right.authority >= count : total += 1
    return total


root = Node(0)
root.put_parent(-1)

nodes_dict = {}
nodes_dict[0] = root 

n, q = map(int,input().split())
input_line = list(map(int,input().split()))

for i in range(1,n+1):
    nodes_dict[i] = Node(i)

parents = input_line[1:n+1]
for i in range(0,n):
    parent = nodes_dict[parents[i]]
    child = nodes_dict[i+1]

    parent.put_child(child)
    child.put_parent(parent)

authoritys = input_line[n+1:]
for i in range(0,n):
    nodes_dict[i+1].put_authority(authoritys[i])

while q > 1:
    q -= 1
    input_line = list(map(int, input().split()))
    commend = input_line[0]

    if commend == 200:
        target = input_line[1]
        target_node = nodes_dict[target]
        target_node.notification_change()

    if commend == 300:    
        target = input_line[1]
        authority = input_line[2]

        target_node = nodes_dict[target]
        target_node.put_authority(authority)

    if commend == 400:  
        target1 = input_line[1]
        target_node1 = nodes_dict[target1]

        target2 = input_line[2]
        target_node2 = nodes_dict[target2]

        target1_parent = target_node1.parent
        target2_parent = target_node2.parent

        target_node1.put_parent(target1_parent)
        target_node2.put_parent(target2_parent)  

        target1_parent.child_change(target_node1, target_node2)
        target2_parent.child_change(target_node2, target_node1)

    if commend == 500:
        target = input_line[1]
        target_node = nodes_dict[target]
        count = get_available_notification_count(target_node,0)
        print(count)