
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

//using namespace std;

int main(int argc, char *argv[]) {

//    printf("\nMain pid is %d \n", getpid());
    pid_t pid = fork();
    if (pid < 0) {
        perror("Fork failed");
    }
    if (pid == 0) {
//        printf("pid == 0: Child ID: %d \n", getpid());
        execl("./count", "./count", "5", (char*) NULL); //execute count.cpp; and pass argv[] = { "./count", "5" }
    }

    // Below is only executed by the parent
    printf("pid > 0: Parent ID: %d \n",getpid());
    int status = 0;
    pid_t childpid = wait(&status);     //This and the code below is taken from DrBFraser's youtube vid "Linux fork() Introduction" and stackoverflow "Check shell command return value in c". Multiple resourced convinced me that I have to use this macro
    int childReturnValue = WEXITSTATUS(status);
    printf("Process %d exited with status: %d", (int) childpid, childReturnValue);

    return 0;
}
 
 
 
