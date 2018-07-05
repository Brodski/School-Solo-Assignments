#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <assert.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <signal.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <time.h>
#include <pthread.h>
#define READ 0
#define WRITE 1

#define TO_KERNEL 3
#define FROM_KERNEL 4

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

int main (int argc, char** argv)
{
    pthread_mutex_lock(&mutex);
    printf("---CHILD\n");
    printf("---getpid: %d\n", getpid());
    const char *message = "from the process to the kernel";

    write (TO_KERNEL, message, strlen (message));
    kill(getppid(),SIGTRAP);
    printf("---SLEEP XD\n");
    sleep(2);
    char buf2[1024];
    int nread = read(4, buf2, 1023);
    printf("nread: %d\n", nread);
//    printf("--- process %d read: %s\n", getpid(),buf2);
    printf("---post read XD\n");
    printf("---buf: %s\n", buf2);
    exit(0);

}



