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
    printf("---CHILD getpid: %d\n", getpid());
	printf("---About to write\n");
	const char *message = "from the process to the kernel";
//	write (TO_KERNEL, message, strlen (message));
	write (4, message, strlen (message));
	printf("---SLEEP XD\n");
    char buf2[1024];
    sleep(2);
    int nread = read(5, buf2, 1023);
    printf("--- process %d read: %s\n", getpid(),buf2);
	exit(0);
    return 0;

}
