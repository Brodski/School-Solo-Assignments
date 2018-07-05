#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>

int main(int argc, char *argv[]) {

//  printf("\nCount PID is: %d\n", getpid() );
    int num = atoi(argv[1]);

    printf("Child PID: %d \n", getpid());
    printf("Parent PID: %d \n", getppid());
    for (int i =0; i< num; i ++){
        printf("Process: %d %d \n", getpid(), i+1);
    }
    exit(num);
    printf("this should not be seen");
    // Raise_execption()
    return 0;
}
