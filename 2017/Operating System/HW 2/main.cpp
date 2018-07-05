// Chris Brodski
#include <stdio.h>
#include <sys/types.h>
#include <assert.h>
#include <csignal>
#include <cstdlib>
#include <signal.h>
#include <unistd.h>
#include <sys/wait.h>
#include <errno.h>

void sigHandler(int sig) {
    if (sig == SIGUSR1) {
        printf("Signal is SIGUSR1. int value: %d \n", sig);
    }
    else if (sig == SIGUSR2) {
        printf("Signal is SIGUSR2. int value: %d \n", sig);
    }
    else if (sig == SIGIO) {
        printf("Signal is SIGIO. int value: %d \n", sig);
    }
    else {
        printf("fool what did you input?! \n");
    }
}

int main() {
    struct sigaction *action = new struct sigaction;
    action->sa_handler = sigHandler;
    sigemptyset(&(action->sa_mask));
    assert(sigaction(SIGUSR1, action, 0) == 0);
    assert(sigaction(SIGUSR2, action, 0) == 0);
    assert(sigaction(SIGIO, action, 0) == 0);

    pid_t pid = fork();
    if (pid <0) { //Error
        perror("Fork failed \n");
        exit(1);
    }
    if (pid ==0) { //Child
        pid_t parentPid = getppid();
        int ret1, ret2, ret3, ret4,ret5,ret6;
        printf("Sending (via kill) SIGUSR1, SIGUSR2, & SIGIO to parent\n");
        ret1 = kill(parentPid, SIGUSR1);
        ret2 = kill(parentPid, SIGUSR2);
        ret3 = kill(parentPid, SIGIO);
        printf("Sending three SIGUSR1 signals \n");
        ret4 = kill(parentPid, SIGUSR1);
        ret5 = kill(parentPid, SIGUSR1);
        ret6 = kill(parentPid, SIGUSR1); // Each ret value is 0.
        printf("Finished sending signals\n");

        //printf("ret4 : %d, ret5 : %d, ret6 : %d \n", ret4,ret5,ret6);

        exit(0); // exit w/ success
    }
    //Else we must be in parent
    int status;
    pid_t waitVal = waitpid(pid, &status, 0);
    while(waitVal == -1) {
        //printf("Waiting on child...\n");
        pid_t waitVal = waitpid(pid, &status, 0);
        if (errno == EINTR) {
            continue; }
        perror("waitpid");
        break;
    }
    printf("Parent is finished.\n");


    return 0;
}





