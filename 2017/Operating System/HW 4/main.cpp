//Chris Brodski

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

/*
** Compile and run this program, and make sure you get the 'aargh' error
** message. Fix it using a pthread mutex. The one command-line argument is
** the number of times to loop. Here are some suggested initial values, but
** you might have to tune them to your machine.
** Debian 8: 100000000
** Gouda: 10000000
** OS X: 100000
** You will need to compile your program with a "-lpthread" option.
*/

#define NUM_THREADS 2

int i;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER; // According to IBM, yes the manual didn't explain much, I should use this macro b/c I think our mutex is a static mutex.  https://www.ibm.com/support/knowledgecenter/en/ssw_aix_61/com.ibm.aix.basetrf1/PTHREAD_MUTEX_INITIALIZER.htm
void *foo (void *bar)
{
    pthread_t *me = new pthread_t (pthread_self());
    printf("in a foo thread, ID %ld\n", *me);

    pthread_mutex_lock(&mutex); // The lock must happen before the for loop, else i risks being incremented.

    for (i = 0; i < *((int *) bar); i++)
    {
        int tmp = i;
        if (tmp != i)
        {
            printf ("aargh: %d != %d\n", tmp, i);
        }
    }
    pthread_mutex_unlock(&mutex);

    pthread_exit (me);
}

int main(int argc, char **argv)
{
    int iterations = strtol(argv[1], NULL, 10);
    pthread_t threads[NUM_THREADS];

    for (int i = 0; i < NUM_THREADS; i++)
    {
        if (pthread_create(&threads[i], NULL, foo, (void *) &iterations))
        {
            perror ("pthread_create");
            return (1);
        }
    }

    for (int i = 0; i < NUM_THREADS; i++)
    {
        void *status;
        if (pthread_join (threads[i], &status))
        {
            perror ("pthread_join");
            return (1);
        }
        printf("joined a foo thread, number %ld\n", *((pthread_t *) status));
    }

    return (0);
}



