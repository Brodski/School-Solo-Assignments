
rowBelow( [A,B],[X]) :- X is B - A.
rowBelow( [A,B|L], [X|L2]) :- X is B - A, rowBelow([B|L],L2).

getLast( [X,Y], A) :- A is Y.
getLast( [F|L], A) :- getLast(L,A).

nextItem([A,B], X) :- rowBelow([A,B], T), X is T+B.
nextItem(L,N) :- rowBelow(L,A), getLast(L,I), nextItem(A,N2), N is N2+I.



