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
#include <string>
#include <sstream>
using namespace std;

#include <pthread.h>
#define READ 0
#define WRITE 1

#define FROM_KERNEL 3 // swapped
#define TO_KERNEL 4


// Okay, the child actually will 100% continue to execute after write(...).
//  So I had to use pause() to prevent it from reading before the parent wrote back
// to the child. Even though on my machine it seems that using pause() does nothing
// for the output, but that's b/c I've been getting lucky but I'm actually still at risk.

void sigHandler (int sig) {
	assert(sig == SIGUSR1);
}

int main (int argc, char** argv)
{
	printf("child.cc: entering child\n");
	struct sigaction *action = new struct sigaction;
	action->sa_handler = sigHandler;
	sigemptyset(&(action->sa_mask));
	assert(sigaction(SIGUSR1,action, 0)==0);

	int pid = getpid();
	int ppid = getppid();

	const char *message = "from the process to the kernel";

	write (TO_KERNEL, message, strlen (message));

	printf("child.cc: I am: %d, sending to: %d\n", getpid(), getppid() );
	kill (ppid, SIGTRAP);

	pause();

	char buf[1024];
	int num_read = read (FROM_KERNEL, buf, 1023);
	buf[num_read] = '\0';
	printf ("child.cc: process %d read: %s\n", pid, buf);
	printf ("child.cc: End of message.............\n");

	exit (0);
}






