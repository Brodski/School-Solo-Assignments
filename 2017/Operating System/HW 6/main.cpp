//
//Chris Brodski
//

/* This program will exec child.cc five times and set up pipes with them.
// The first Kernel to Processsor message will fail to be read by the child
// for some unknown reason :(
//
*/



#include <iostream>
#include <list>
#include <iterator>
#include <unistd.h>
#include <signal.h>
#include <errno.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include <fcntl.h>
#include <pthread.h>
#include <string>
#include <sstream>

using namespace std;
#define WRITE(a) { const char *foo = a; write (1, foo, strlen (foo)); }

#define EBUG
#ifdef EBUG
#   define dmess(a) cout << "in " << __FILE__ <<  " at " << __LINE__ << " " << a << endl;
#   define dprint(a) cout << "in " << __FILE__ <<  " at " << __LINE__ << " " << (#a) << " = " << a << endl;
#   define dprintt(a,b) cout << "in " << __FILE__ << " at " << __LINE__ << " " << a << " " << (#b) << " = "  << b << endl
#else
#   define dprint(a)
#endif /* EBUG */

#undef NDEBUG
#include <assert.h>
#include <cstdlib>
#include <cstring>

#define NUM_SECONDS 30

#define READ_END 0
#define WRITE_END 1

#define NUM_CHILDREN 5
#define NUM_PIPES NUM_CHILDREN*2
//#define P2K i	//not used
//#define K2P i+1

int pipes[NUM_PIPES][2];
int child_count = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;


enum STATE { NEW, RUNNING, WAITING, READY, TERMINATED };

/*
** a signal handler for those signals delivered to this process, but
** not already handled.
*/
void grab (int signum) {
    dprint (signum);
}

// c++decl> declare ISV as array 32 of pointer to function (int) returning
// void
void (*ISV[32])(int) = {
/* 	00 01 02 03 04 05 06 07 08 09 */
/*  0 */ grab, grab, grab, grab, grab, grab, grab, grab, grab, grab,
/* 10 */ grab, grab, grab, grab, grab, grab, grab, grab, grab, grab,
/* 20 */ grab, grab, grab, grab, grab, grab, grab, grab, grab, grab,
/* 30 */ grab, grab
};

struct PCB
{
    STATE state;
    const char *name;   // name of the executable
    int pid;       	// process id from fork();
    int ppid;      	// parent process id
    int interrupts;	// number of times interrupted
    int switches;  	// may be < interrupts
    int started;   	// the time this process started

    int P2K[NUM_PIPES][2];
    int K2P[NUM_PIPES][2];

    // I added these two, to mathematically calculate the total system time the process took.
    int processingTime; // the time the process has been processing
    int recentStart;   // the time when the process changed to RUNNING
};

/*
** an overloaded output operator that prints a PCB
*/
ostream& operator << (ostream &os, struct PCB *pcb)
{
    os << "state:  	" << pcb->state << endl;
    os << "name:   	" << pcb->name << endl;
    os << "pid:    	" << pcb->pid << endl;
    os << "ppid:   	" << pcb->ppid << endl;
    os << "interrupts:   " << pcb->interrupts << endl;
    os << "switches:   " << pcb->switches << endl;
    os << "started:	" << pcb->started << endl;
    return (os);
}

/*
** an overloaded output operator that prints a list of PCBs
*/
ostream& operator << (ostream &os, list<PCB *> which)
{
    list<PCB *>::iterator PCB_iter;
    for (PCB_iter = which.begin(); PCB_iter != which.end(); PCB_iter++)
    {
        os << (*PCB_iter);
    }
    return (os);
}

PCB *running;
PCB *idle;


list<PCB *> new_list;
list<PCB *> processes;

list<PCB*>::iterator itr; // The iterator used in the round-robin
bool isIteratorStarted;
int sys_time;


int eye2eh (int i, char *buf, int bufsize, int base)
{
    if (bufsize < 1) {
        return (-1);
    }
    buf[bufsize-1] = '\0';
    if (bufsize == 1) {
        return (0);
    }
    if (base < 2 || base > 16)
    {
        for (int j = bufsize-2; j >= 0; j--)
        {
            buf[j] = ' ';
        }
        return (-1);
    }

    int count = 0;
    const char *digits = "0123456789ABCDEF";
    for (int j = bufsize-2; j >= 0; j--)
    {
        if (i == 0)
        {
            buf[j] = ' ';
        }
        else
        {
            buf[j] = digits[i%base];
            i = i/base;
            count++;
        }
    }
    if (i != 0) return (-1);
    return (count);
}
/*
**  send signal to process pid every interval for number of times.
*/
void send_signals (int signal, int pid, int interval, int number)
{
    dprintt ("at beginning of send_signals", getpid ());



    for (int i = 1; i <= number; i++)
    {
        sleep (interval);

        dprintt ("sending", signal);
        dprintt ("to", pid);

        if (kill (pid, signal) == -1)
        {
            perror ("kill");
            return;
        }
    }
    dmess ("at end of send_signals");
}

struct sigaction *create_handler (int signum, void (*handler)(int))
{
    struct sigaction *action = new (struct sigaction);

    action->sa_handler = handler;
/*
**  SA_NOCLDSTOP
**  If  signum  is  SIGCHLD, do not receive notification when
**  child processes stop (i.e., when child processes  receive
**  one of SIGSTOP, SIGTSTP, SIGTTIN or SIGTTOU).
*/
    if (signum == SIGCHLD)
    {
        action->sa_flags = SA_NOCLDSTOP;
    }
    else
    {
        action->sa_flags = 0;
    }

    sigemptyset (&(action->sa_mask));

    assert (sigaction (signum, action, NULL) == 0);
    return (action);
}

PCB* choose_process ()
{
    printf("choose_process: entering\n");
    if ( !new_list.empty() ){
        PCB* somePCB = new_list.back();
        new_list.pop_back();
        somePCB->ppid = getpid();
        somePCB->started = sys_time;
        somePCB->switches = somePCB->switches - 1;
        somePCB->recentStart = sys_time;
        somePCB->processingTime = 0;
        running = somePCB; //Have to update the running PCB before we exec the child. Else we risk accessing running before it has the correct PCB.

        pthread_mutex_lock(&mutex); //comment on this mutex at process_trap()
        processes.push_back(somePCB);


        int P2K_aux = (processes.size()-1) * 2;
        int K2P_aux = P2K_aux + 1;
        assert (pipe(pipes[P2K_aux]) == 0);
        assert (pipe(pipes[K2P_aux]) == 0);
        assert (fcntl (pipes[P2K_aux][READ_END], F_SETFL,
                       fcntl(pipes[P2K_aux][READ_END], F_GETFL) | O_NONBLOCK) == 0);
        pid_t pid = fork();
        somePCB->pid = pid; // When executed in parent we get the pid that we want.
        if (pid == -1) {
            perror("Fork failed in choose_process()");
        }
        if (pid == 0) { //Child
            // assign fildes 3 and 4 to the pipe ends in the child
            dup2(pipes[P2K_aux][WRITE_END], 4);
            dup2(pipes[K2P_aux][READ_END], 3);
            close(pipes[P2K_aux][READ_END]);
            close(pipes[K2P_aux][WRITE_END]);
            // execute "./processes.front->name";  pass argv[] = {"./processes.front()->name"}
            char execThis[40];
            strcpy(execThis, "./");
            strcat(execThis, processes.back()->name);
            execl(execThis, execThis, (char *) NULL);
        }
        else if (pid == -1 ) {
            perror("fork");
        }
        return somePCB;
    }

    /*
     * Round Robin processing is below.
     * It looks ugly but I couldn't find a clean convenient answer in reasonable time.
    */
    bool areNoneReady = true; // Assume no processes are ready
    int counter = 0;
    list<PCB*>::iterator auxItr;

    if (!isIteratorStarted) {
        itr = processes.begin();
        isIteratorStarted = true;
    }
    while (counter != processes.size() ) {
        counter = counter + 1;
        if ( (*itr)->state == READY && (*itr) != running) {
            return (*itr);
        }
        auxItr = itr;
        ++itr;
        if ( ++auxItr == processes.end()){  //If we reached the end, loop back to the beginning
            itr = processes.begin();
        }
    }
    printf("choose_process: about returning idle\n");
    return idle;  //If we have iterated over all processes & non are READY, then return idle
}

void scheduler (int signum)
{
    assert (signum == SIGALRM);
    sys_time++;
    PCB* tocont = choose_process();
    dprintt ("continuing", tocont->pid);

    tocont->recentStart = sys_time;
    tocont->switches = tocont->switches + 1;
    tocont->state = RUNNING;
    running = tocont;

    if (kill (tocont->pid, SIGCONT) == -1)
    {
        perror ("kill");
        return;
    }
}

void process_done (int signum)
{

    assert (signum == SIGCHLD);
    WRITE("---- entering child_done\n");
    int status, cpid;
    cpid = waitpid (-1, &status, WNOHANG);

    dprintt ("in process_done", cpid);

    if  (cpid == -1)
    {
        WRITE("cpid < 0\n");
        kill (0, SIGTERM);

    }
    else if (cpid == 0)
    {
        WRITE("cpid == 0\n");
        if (errno == EINTR) { return; }
        perror ("no children");
    }
    else
    {
        printf( "process_done: interrupts: %d\n", running->interrupts) ;
        printf( "process_done: switches: %d\n", running->switches);
        printf( "process_done: started: %d\n", running->started);
        printf( "process_done: sys_time: %d\n", sys_time);
        printf( "process_done: processingTime: %d\n", running->processingTime);
        running->state =TERMINATED;

        char buf[10];
        assert (eye2eh (cpid, buf, 10, 10) != -1);
        WRITE("process exited:");
        WRITE(buf);
        WRITE("\n");

        dprint (WEXITSTATUS (status));
    }

// WRITE("---- leaving child_done\n");
}

void process_trap (int signum)
{

    running->state = READY;
    running->interrupts =  running->interrupts + 1;

    assert (signum == SIGTRAP);
    WRITE("---- entering process_trap\n");

    int P2K_aux = (processes.size()-1) * 2;
    int K2P_aux = P2K_aux + 1;


    // Note this, we do NOT poll all the pipes b/c the pipe array (pipes[][2]) still has
    // the data in each child's pipe, so if we did poll we will only read the message
    // from the first pipe.
    // So I used mutex locks to make the program wait until process_trap
    // is finished before the CPU spawns a new child.
    // for (int i = 0; i < NUM_PIPES; i++)
    char buf[1024];
    int num_read = read (pipes[P2K_aux][READ_END], buf, 1023);
    if (num_read > 0)
    {
        buf[num_read] = '\0';
        WRITE("kernel read: ");
        WRITE(buf);
        WRITE("\n");

        // Create Message
        char preMsg[1024];
        const char *finalMsg;
        const char *message2 = "from the kernel to the process";

        std::stringstream ss;	//4 lines of code to convert the number sys_time to a string.
        ss << sys_time;
        string timeString = ss.str();
        strcpy(preMsg, message2);
        strcat(preMsg, "\nsys_time: ");
        strcat(preMsg, timeString.c_str());

        finalMsg = preMsg;

        //The while loop below gets all processors' names & pids, and writes that info to child
        list<PCB*>::iterator proIter;
        proIter = processes.begin();
        strcat(preMsg, "\nCurrent processes: \n(name): (pid)");
        while (proIter != processes.end() ) {
            strcat(preMsg, "\n");
            strcat(preMsg, (*proIter)->name);
            strcat(preMsg, ": ");
            std::stringstream newSS;
            newSS << (*proIter)->pid;
            string pidString = newSS.str();
            strcat(preMsg, pidString.c_str());
            proIter++;
        }

        //Writing message
        finalMsg = preMsg;
        write (pipes[K2P_aux][WRITE_END], finalMsg, strlen (finalMsg));
        pthread_mutex_unlock(&mutex);
        kill(running->pid, SIGUSR1);

    }

    WRITE("---- leaving process_trap\n");
}







/*
** stop the running process and index into the ISV to call the ISR
*/
void ISR (int signum)
{
    if (running->state != TERMINATED) {

        if (kill (running->pid, SIGSTOP) == -1)
        {
            perror ("kill");
            return;
        }

        dprintt ("stopped", running->pid);
        running->state = READY;
        running->interrupts = running->interrupts + 1;
        running->processingTime = running->processingTime + (sys_time+1 - running->recentStart); //Adjust for order
    }

    ISV[signum](signum);
}


void create_idle ()
{
    int idlepid;

    if ((idlepid = fork ()) == 0)
    {
        dprintt ("idle", getpid ());

        // the pause might be interrupted, so we need to
        // repeat it forever.
        for (;;)
        {
            dmess ("going to sleep");

            pause ();
            if (errno == EINTR)
            {
                dmess ("waking up");
                continue;
            }
            perror ("pause");
        }
    }
    idle = new (PCB);
    idle->state = RUNNING;
    idle->name = "IDLE";
    idle->pid = idlepid;
    idle->ppid = 0;
    idle->interrupts = 0;
    idle->switches = 0;
    idle->started = sys_time;
}


/*
** set up the "hardware"
*/
void boot (int pid)
{
    ISV[SIGALRM] = scheduler;
    ISV[SIGCHLD] = process_done;
    ISV[SIGTRAP] = process_trap;
    create_handler (SIGALRM, ISR);
    create_handler (SIGCHLD, ISR);
    create_handler (SIGTRAP, ISR);

    // start up clock interrupt
    int ret;
    if ((ret = fork ()) == 0)
    {
        send_signals (SIGALRM, pid, 1, NUM_SECONDS);

        // once that's done, really kill everything...
        kill (0, SIGTERM);
    }
    if (ret < 0)
    {
        perror ("fork");
    }
}
int main (int argc, char **argv)
{
    int pid = getpid();
    dprintt ("main", pid);

    isIteratorStarted = false;
    sys_time = 0;


    for (int i =0; i < NUM_CHILDREN; i++) {
        PCB *process = new (PCB);
        process->state = NEW;
        //  process->name = argv[i];
        process->name = "child";
        process->ppid = 0;
        process->interrupts = 0;
        process->switches = 0;
        new_list.push_back(process);

    }

    boot (pid);

    // create a process to soak up cycles
    create_idle ();
    running = idle;

    cout << running;

    // we keep this process around so that the children don't die and
    // to keep the IRQs in place.
    for (;;)
    {
        pause();
        if (errno == EINTR) {
            continue;
        }
        perror ("pause");
    }
    printf("main: about to exit(0)\n");
    exit(0);
}
























