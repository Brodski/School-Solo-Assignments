#Chris Brodski
#HW 6, eight queens problem

from __future__ import print_function
import copy
import time
import unittest

def solve_any_board(board_size):
    #start_time = time.time(); #I timed some stuff for curiosity
    if not type(board_size)==int:
        raise TypeError('Boardsize must be an integer')
    solutions = []
    array_of_solutions = []
    solve_queens(0, board_size, solutions, array_of_solutions)

    return array_of_solutions

'''Check if Queen1 threatens Queen2. We are expecting this input: Q = [x,y] '''
def safe(Q1, Q2):
    if Q1[0] == Q2[0]: return False #x1 == x2
    if Q1[1] == Q2[1]: return False #y1 == y2
    if abs(Q2[0]-Q1[0]) == abs(Q2[1]-Q1[1]): return False
    return True

''' This finds all the solutions'''
def solve_queens(row, boardSize, solution,array_of_solutions):
    if row == boardSize and not solution==[]:  #This is when we reach the end of the board.  The second statement is some cute error checking for when some punk asks if there is a solution for a board of size 0.
        array_of_solutions.append(solution)
        return
    for x in range(0,boardSize):
        open = True #'open' tells if we can place a queen on a spot. If the spot is threatened by another queen open will become False. We assume the spot isn't threatened.
        for Q in solution:
            if not safe( [x,row], Q ):
                open = False
                break
        if open:
            solution.append([x,row])
            if not solve_queens(row+1, boardSize, solution, array_of_solutions):
                 solution.pop()

def print_board(array_of_solutions):
    if array_of_solutions == []:
        print("no solution found")
        return
    length = len(array_of_solutions[0])
    print("for:", length)
    print('-' * length)
    for solution in array_of_solutions:
        for i in range(length):
            for j in range(length):
                if [i, j] in solution:
                    print("Q", end=' ' )
                else:
                    print(". ", end=' ')
            print()
        print('-' * length)

'''This is to check that I am getting the correct solutions. It shows up only as print statement.
It goes through each solved board. Then check every Queen with every other queen. If any queen
threatens another queen then the test fails and the method returns a false'''
def solution_Tester(array_of_solutions):
    if not array_of_solutions:
        print("no solution found")
        return False
    else:
        for solution in array_of_solutions:
            for Q1 in solution:
                for Q2 in solution:
                    if not Q1 == Q2:
                        if safe(Q1,Q2) == False:
                            return False
        return True

class test_remove_spaces (unittest.TestCase):

    #Exceptions tests for solve_any_board(int)
  #  def test_solveAny_execptionA(self):
  #      self.assertRaises(RuntimeError, lambda: solve_any_board(3))
  #  def test_solveAny_execptionB(self):
  #      self.assertRaises(RuntimeError, lambda: solve_any_board(-1))
    def test_solveAny_execptionA(self):
        self.assertRaises(TypeError, lambda: solve_any_board('hello'))
    def test_solveAny_execptionB(self):
        self.assertRaises(TypeError, lambda: solve_any_board(1.234))
    def test_solveAny_execptionC(self):
        silly_list = [1,2,3]
        self.assertRaises(TypeError, lambda: solve_any_board(silly_list))

    #Tests that we are getting the correct number of solutions for board size of 'x'
    def test_number_of_solutions_0(self):
        self.assertEquals(len(solve_any_board(0)), False)
    def test_number_of_solutions_65(self):
        self.assertEquals(len(solve_any_board(-65)), False)
    def test_number_of_solutions_2(self):
        self.assertEquals(len(solve_any_board(2)), False)
    def test_number_of_solutions_3(self):
        self.assertEquals(len(solve_any_board(3)), False)
    def test_number_of_solutions_1(self):
        self.assertEquals(len(solve_any_board(1)), 1)
    def test_number_of_solutions_4(self):
        self.assertEquals(len(solve_any_board(4)), 2)
    def test_number_of_solutions_5(self):
        self.assertEquals(len(solve_any_board(5)), 10)
    def test_number_of_solutions_6(self):
        self.assertEquals(len(solve_any_board(6)), 4)
    def test_number_of_solutions_7(self):
        self.assertEquals(len(solve_any_board(7)), 40)
    def test_number_of_solutions_8(self):
        self.assertEquals(len(solve_any_board(8)), 92)
    def test_number_of_solutions_10(self):
        self.assertEquals(len(solve_any_board(10)), 724)

    #Test my function that tests solutions, solution_Tester(array_of_solutions)
    def test_theTester(self):
        array_test1 = [ [[0,0], [0,1]] ]
        self.assertFalse(solution_Tester(array_test1))
    def test_theTester2(self):
        array_test2 = [ [[0,0], [1,1]] ]
        self.assertFalse(solution_Tester(array_test2))

    def test_theTester3(self):
        array_test3 = [ [[0,0], [3,3], [2,3], [4,3]] ]
        self.assertFalse(solution_Tester(array_test3))

    def test_theTester_actualSolution_to_4boardA(self):  # - Q - -
        array_test = [ [[1,0], [3,1], [0,2], [2,3]] ]   # - - - Q
        self.assertTrue(solution_Tester(array_test))    # Q - - -
                                                        # - - Q -

    def test_theTester_actualSolution_to_4boardB(self): # - - Q -
        array_test = [ [[2,0], [0,1], [3,2], [1,3]] ]   # Q - - -
        self.assertTrue(solution_Tester(array_test))    # - - - Q
                                                        # - Q - -
    #Test both the function that generates the solutions and solutions_tester, ie my little function that tests the board
    def test_solver_and_tester4(self):
        self.assertTrue(solution_Tester(solve_any_board(4) ))
    def test_solver_and_tester5(self):
        self.assertTrue(solution_Tester(solve_any_board(5) ))
    def test_solver_and_tester6(self):
        self.assertTrue(solution_Tester(solve_any_board(6) ))
    def test_solver_and_tester7(self):
        self.assertTrue(solution_Tester(solve_any_board(8) ))
    def test_solver_and_tester8(self):
        self.assertTrue(solution_Tester(solve_any_board(10) ))



if __name__ == '__main__':
    unittest.main()
    start_time = time.time(); #I timed some stuff for curiosity

    all_solutions = solve_any_board(8)
    #time_to_solve_all_boards =  time.time() - start_time
    print(solution_Tester(all_solutions))
    #start_time = time.time()
    print_board(all_solutions)
    print("number of solutions = ", len(all_solutions))
   # print('time_to_solve_all_boards =  ', time_to_solve_all_boards)
   # print('time to test all boards =', time.time() - start_time)

