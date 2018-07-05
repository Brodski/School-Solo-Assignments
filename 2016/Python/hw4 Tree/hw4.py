#Chris Brodski

from __future__ import print_function
from sys import stdin
import unittest
import copy

class family_tree:
    def __init__ (self, init = None):
        self.__value = self.__name = self.__parent = None
        self.__left = self.__right = None

        if init:
            try:
                for i in init:
                    self.add(i[0], i[1])
            except TypeError:
                self.add(init[0], init[0])

    def __iter__(self):
        if self.__left:
            for node in self.__left:
                yield(node)

        yield(self.__value, self.__name)

        if self.__right:
            for node in self.__right:
                yield(node)

    """ Return a preorder list """
    def preorder(self):
        if self .__left and self.__right:
            return [self.__value] + self.__left.preorder() + self.__right.preorder()
        if self.__left:
            return [self.__value] + self.__left.preorder()
        if self.__right:
            return [self.__value] + self.__right.preorder()
        if self.__value == None: #if the user sends an empty tree then .preorder returns None. This behavior is questionable, but
            return None             #I'm going to assume the user doesn't want any node to have .value = None
        if not self.__left and not self.__right:
            return [self.__value]
    """ Return a inorder list """
    def inorder(self):
        if self.__left and self.__right:
            return self.__left.inorder() + [self.__value] + self.__right.inorder()
        if self.__right:
            return [self.__value] + self.__right.inorder()
        if self.__left:
            return self.inorder() + [self.__value]
        if self.__value == None:
            return None
        if not self.__left and not self.__right:
            return [self.__value]

    """ Return a postorder list """
    def postorder(self):
        if self.__left and self.__right:
            return self.__left.postorder() + self.__right.postorder() + [self.__value]
        if self.__left:
            return self.__left.postorder() + [self.__value]
        if self.__right:
            return self.__right.postorder() + [self.__value]
        if self.__value == None:
            return None
        if not self.__left and not self.__right:
            return [self.__value]

    def __str__(self):
        return(','.join(str(node) for node in self))

    def add(self, value, name):
        if self.__value == self.__left == self.__right == None:
            self.__value = value
            self.__name = name
            self.__parent = None
            return

        if value < self.__value:
            if not self.__left:
                self.__left = family_tree()
                self.__left.__parent = self
                self.__left.__value = value
                self.__left.__name = name
            else:
                self.__left.add(value, name)
        else:
            if not self.__right:
                self.__right = family_tree()
                self.__right.__parent = self
                self.__right.__value = value
                self.__right.__name = name
            else:
                self.__right.add(value, name)

    """ Given a value, return the node with that value. Useful in the
    next two methods """
    def __find(self, value):
        if self.__value == value: return self

        if self.__value > value:
            if self.__left:
                return self.__left.__find(value)
            else:
                raise(LookupError)

        if self.__value < value:
            if self.__right:
                return self.__right.__find(value)
            else:
                raise(LookupError)

    """ Given a value, return the name of that node's parent """
    def find_parent(self, value):
        if self.__find(value).__parent:
            return self.__find(value).__parent.__name
        else:
            raise(LookupError)

    """ Given a value, return the name of that node's grand parent """
    def find_grand_parent(self, value):
        if self.__find(value).__parent.__parent:
            return self.__find(value).__parent.__parent.__name
        else:
            raise(LookupError)

    """ Create a list of lists, where each of the inner lists
        is a generation """
    def generations(self):

        root = self #We assume that the trees are initialized correctly. So the first node will always be the root. We'll let someone else rearrange the tree to however he/she finds best.
        this_level = [root]
        next_level = []
        result =[]
        names = []
        while this_level:
            for node in this_level:
                if node.__left:
                    next_level.append(node.__left)
                if node.__right:
                    next_level.append(node.__right)
                names.append(node.__name)
            result.append(copy.deepcopy(names))
            this_level = copy.deepcopy(next_level)
            del next_level[:]
            del names[:]
        return result

        """ First, create a list 'this_level' with the root,
                and three empty lists: 'next_level', 'result', and
                'names' """

        """ While 'this_level' has values """
        """ Get the first element and append its name to 'names' """

        """ If the first element has a left, append it to 'next_level'
                Do the same for the right """

        """ If 'this_level' is now empty """
        """ Append 'names' to 'result', set "this_level' to
                    'next_level', and 'next_level' and 'names' to empty
                    lists """

class test_family_tree (unittest.TestCase):
    """
      20
     /  \
    10  30
       /  \
      25  35
    """
    def test_empty(self):
        self.assertEquals(str(family_tree()), '(None, None)')

    def setUp(self):
        self.tree = family_tree([(20, "Grandpa"), (10, "Herb"), \
        (30, "Homer"),(25, "Bart"), (35, "Lisa")])
        self.expected = "(10, 'Herb'),(20, 'Grandpa'),(25, 'Bart'),\
(30, 'Homer'),(35, 'Lisa')"

    def test_add(self):
        bt = family_tree()
        bt.add(20, "Grandpa")
        bt.add(10, "Herb")
        bt.add(30, "Homer")
        bt.add(25, "Bart")
        bt.add(35, "Lisa")
        self.assertEquals(str(bt), self.expected)

    def test_init(self):
        self.assertEquals(str(self.tree), self.expected)

    def test_parent(self):
        self.assertEquals(self.tree.find_parent(35), "Homer")

    def test_grand_parent(self):
        self.assertEquals(self.tree.find_grand_parent(35), "Grandpa")

    def test_generations(self):
        self.assertEquals(self.tree.generations(), \
            [['Grandpa'], ['Herb', 'Homer'], ['Bart', 'Lisa']])

###########################################################################

    def test_inorder(self):
        self.assertEquals(self.tree.inorder(), [10,20,25,30,35])

    def test_preoder(self):
        self.assertEquals(self.tree.preorder(), [20,10,30,25,35])

    def test_postorder(self):
        self.assertEquals(self.tree.postorder(),[10,25,35,30,20] )

    def test_single_inorder(self):
        small_tree = family_tree()
        small_tree.add(10,'John')
        self.assertEquals( small_tree.inorder(), [10])

    def test_single_preoder(self):
        small_tree = family_tree()
        small_tree.add(10,'John')
        self.assertEquals(small_tree.preorder(), [10])

    def test_single_postoder(self):
        small_tree = family_tree()
        small_tree.add(10,'John')
        self.assertEquals(small_tree.postorder(), [10])

    def test_empty_inorder(self):
        empty_tree = family_tree()
        self.assertEquals(empty_tree.inorder(), None)

    def test_empty_preorder(self):
        empty_tree = family_tree()
        self.assertEquals(empty_tree.preorder(), None)

    def test_lookup_error(self):
        bt = family_tree()
        bt.add(10, 'John')
        self.assertRaises(LookupError, lambda: bt.find_parent(15)) #I guess putting lambda makes it work

    def test_lookup_error2(self):
        bt = family_tree()
        bt.add(10, 'John')
        self.assertRaises(LookupError, lambda: bt.find_parent(10)) #Searches for value of 10, which does exist but there doesn't exist a parent
                                                                      #  for 10. The previous test searches for 15 which does not exist at all.

if '__main__' == __name__:
    """ Read a file with lines of '# name'. Add each to a
    familty tree, and print out the resulting generations. """

    unittest.main()
    ft = family_tree()
    stdin = open('readnames.txt', 'r')
    for line in stdin:
        a = line.strip().split(" ")
        ft.add(a[0], a[1])
    print(ft.generations())
