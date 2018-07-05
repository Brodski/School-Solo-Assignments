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

#define READ 0
#define WRITE 1

#define TO_KERNEL 3
#define FROM_KERNEL 4

int main (int argc, char** argv)
{

	pid_t pid;
	int pipez[2];
	int ret;
	char buf[20];
	
	pipe(pipez);
	
	int child = fork();
	if (child == 0)) {
		dup2(pipez[0], 3);
		dup2(pipez[1], 4);
		close(pipez[0]);
		close(pipez[1]);
		execl("./babyPipesChild", "./babyPipesChild", (char*) NULL);
	}
	cpid = waitpid (-1, &status, WNOHANG);
	read(pipez[0], buf, 100);
	printf("buf: %s\n", buf);
	
}