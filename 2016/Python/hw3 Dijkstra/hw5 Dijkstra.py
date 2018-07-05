#Chris Brodski
#HW 5
from __future__ import print_function
from sys import stdin
import unittest

# set to True if your result includes the track
track_prev = True

class weighted_digraph:
    class __edge(object):
        def __init__(self, to_node, weight):
            self.to_node = to_node
            self.weight = weight

    class __node(object):
        def __init__(self, value):
            self.value = value
            self.edges = []

        def __str__(self):
            result = str(self.value)
            for edge in self.edges:
                result += "->" + str(edge.to_node.value) + \
                    "(" + str(edge.weight) + ")"
            return(result)

        def add_edge(self, new_edge):
            if not self.is_adjacent(new_edge.to_node):
                    self.edges.append(new_edge)

        def remove_edge(self, to_node):
            for edge in self.edges:
                if edge.to_node == to_node:
                    self.edges.remove(edge)

        def is_adjacent(self, node):
            for edge in self.edges:
                if edge.to_node == node:
                    return(True)
            return(False)

    def __init__(self, directed=True):
        self.__nodes = []
        self.__directed = directed

    def __len__(self): return(len(self.__nodes))

    def __str__(self):
        result = ""
        for node in self.__nodes:
            result += str(node) + '\n'
        return(result)

    def get_nodes(self): return self.__nodes[:]

    def find(self, value):
        for node in self.__nodes:
            if node.value == value:
                return(node)

        return(None)

    def add_nodes(self, nodes):
        for node in nodes:
            self.add_node(node)

    def add_node(self, value):
        if not self.find(value):
            self.__nodes.append(self.__node(value))

    def add_edges(self, edges):
        for edge in edges:
            self.add_edge(edge[0], edge[1], edge[2])

    """ Add an edge between two values. If the nodes
    for those values aren't already in the graph,
    add those. """
    def add_edge(self, from_value, to_value, weight):
        from_node = self.find(from_value)
        to_node = self.find(to_value)

        if not from_node:
            self.add_node(from_value)
            from_node = self.find(from_value)
        if not to_node:
            self.add_node(to_value)
            to_node = self.find(to_value)

        from_node.add_edge(self.__edge(to_node, weight))
        if not self.__directed:
            to_node.add_edge(self.__edge(from_node, weight))

    def remove_edge(self, from_value, to_value, weight):
        from_node = self.find(from_value)
        to_node = self.find(to_value)

        from_node.remove_edge(to_node)
        if not self.directed:
            to_node.remove_edge(from_node)

    def are_adjacent(self, value1, value2):
        return(self.find(value1).is_adjacent(self.find(value2)))


    def dijkstra(self, start):

        ''' This method finds the shortest path from 'start' to every other city.
        We will use a dictionary for the unvisited city. The key will be the city name, aka self.node.value, and
        the dictionary's value will be the node. '''
        ''' Set up for dijkstra's algorithm '''
        unvisted = { }
        for n in self.__nodes:
            unvisted[str(n.value)] = n #hope that no two nodes have the same name, aka n.value
            n.distance = float('inf')
            n.prev_node_of_min_path = None
        self.find(start).distance = 0

        while unvisted:
            ''' Find the node with the minimum distance '''
            min = float('inf') #
            for k, v in unvisted.items():
                if v.distance < min:
                    min = v.distance
                    current_node = v #this is the node where we do calculations and algorithm stuff.
            unvisted.pop(str(current_node.value))

            ''' Current_node is the final node on the current minimum known path of all our unvisited nodes'''
            for e in current_node.edges:
                '''  If the new distance is less than the previous distance, then the program calculates a possible
                new distance to the adjacent node, if the calculated distance is shorter than what is currenlty stored at node.distance
                then a new (or initial) shortest path is found. Another words, this is doing Dijkstra's algorithm '''
                if (current_node.distance + e.weight) < e.to_node.distance:
                    e.to_node.prev_node_of_min_path = current_node # updated path
                    e.to_node.distance = current_node.distance + e.weight # updated distance

        #We now know the shortest path to every node, what we need to do now is put it in an array and send it though return
        result = []
        if not track_prev:
            ''' For each node, create a list where the first element is the total distance and the second is the value '''
            for n in self.__nodes:
                min_path_of_a_city = []
                min_path_of_a_city.append(n.distance)
                min_path_of_a_city.append(n.value)
                result.append(min_path_of_a_city)

        else:
            ''' For each node, create a list where the first element is the total distance and the following values are the
            are the nodes traversed from end to start '''
            for n in self.__nodes:
                min_path_of_a_city = []
                min_path_of_a_city.append(n.distance)
                min_path_of_a_city.append(n.value) #We append the end node/city to the [1] index
                while n.prev_node_of_min_path:
                    n = n.prev_node_of_min_path
                    min_path_of_a_city.append(n.value) #The last while iteration gives the starting city

                result.append(min_path_of_a_city)
        return result

class test_weighted_digraph(unittest.TestCase):
    def test_empty(self):
        self.assertEqual(len(weighted_digraph()), 0)

    def test_one(self):
        g = weighted_digraph()
        g.add_node(1)
        self.assertEqual(len(g), 1)

    def test_duplicate(self):
        g = weighted_digraph()
        g.add_node(1)
        g.add_node(1)
        self.assertEqual(len(g), 1)

    def test_two(self):
        g = weighted_digraph()
        g.add_node(1)
        g.add_node(2)
        self.assertEqual(len(g), 2)

    def test_edge(self):
        g = weighted_digraph()
        g.add_node(1)
        g.add_node(2)
        g.add_edge(1, 2, 3)
        self.assertEqual(str(g), '1->2(3)\n2\n')

    def test_adding_ints(self):
        g = weighted_digraph()
        g.add_nodes([1, 2])
        g.add_edges([(1, 2, 3), (2, 1, 3)])
        self.assertEqual(str(g), '1->2(3)\n2->1(3)\n')

    def test_adding_strings(self):
        g = weighted_digraph()
        g.add_nodes(['Denver', 'Boston'])
        g.add_edges([('Denver', 'Boston', 1971.8), ('Boston', 'Denver', 1971.8)])
        self.assertEqual(str(g), 'Denver->Boston(1971.8)\nBoston->Denver(1971.8)\n')

    def test_are_adjacent(self):
        g = weighted_digraph()
        g.add_nodes(['Denver', 'Boston'])
        g.add_edges([('Denver', 'Boston', 1971.8), ('Boston', 'Denver', 1971.8)])
        self.assertTrue(g.are_adjacent('Denver', 'Boston'))

    def test_arent_adjacent(self):
        g = weighted_digraph()
        g.add_nodes(['Denver', 'Boston', 'Milano'])
        g.add_edges([('Denver', 'Boston', 1971.8), ('Boston', 'Denver', 1971.8)])
        self.assertFalse(g.are_adjacent('Denver', 'Milano'))

    def test_arent_adjacent_directed(self):
        g = weighted_digraph()
        g.add_edges([('Denver', 'Boston', 1971.8)])
        self.assertFalse(g.are_adjacent('Denver', 'Milano'))
        self.assertFalse(g.are_adjacent('Boston', 'Denver'))
        self.assertTrue(g.are_adjacent('Denver', 'Boston'))

    def test_arent_adjacent_undirected(self):
        g = weighted_digraph(False)
        g.add_edges([('Denver', 'Boston', 1971.8)])
        self.assertTrue(g.are_adjacent('Boston', 'Denver'))
        self.assertTrue(g.are_adjacent('Denver', 'Boston'))

    def test_add_edges_without_nodes(self):
        g = weighted_digraph()
        g.add_edges([('Denver', 'Boston', 1971.8), ('Boston', 'Denver', 1971.8)])
        self.assertEqual(str(g), \
            'Denver->Boston(1971.8)\nBoston->Denver(1971.8)\n')

    def test_dijkstra(self):
        g = weighted_digraph()
        g.add_edges([(1,2,2), (1,3,1), (2,3,1), (2,4,1), \
            (2,5,2), (3,5,5), (4,5,3), (4,6,6), (5,6,1)])
        if not track_prev:
            self.assertEquals(g.dijkstra(1), [[0, 1], [2, 2], [1, 3], \
                [3, 4], [4, 5], [5, 6]])
        else:
            self.assertEquals(g.dijkstra(1), [[0, 1], [2, 2, 1], [1, 3, 1], \
                [3, 4, 2, 1], [4, 5, 2, 1], [5, 6, 5, 2, 1]])

if '__main__' == __name__:

    unittest.main()
    g = weighted_digraph(False)
    file = open('highways.txt', "r")
    for line in file:
        a = line.strip().split(" ")
         #the highways.txt file was kind of messed up. This below fixes that.
        if not a==['']:
            g.add_edge(a[0], a[1], int(a[2]))
    result = g.dijkstra("Denver")
    for city in result:
        print(city[1], "is", city[0], 'miles from Denver')
        if track_prev:
            for path in city[2:]:
                print("     ", path)