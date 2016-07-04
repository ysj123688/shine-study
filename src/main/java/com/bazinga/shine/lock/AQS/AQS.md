 
#AbstractQueuedSynchronizer源码理解
 
 
##acquire方法
    

    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
    
tryAcquire我们查看ReentrantLock的代码

	protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }

当某个方法调用lock方法的时候，lock方法调用AQS的tryAcquire方法

首先获取当前调用者的线程，getState方法第一次调用的时候返回0(其实就是目前还没有其他的线程调用setState(!0))这个方法的时候,走c==0的分支代码

	public final boolean hasQueuedPredecessors() {
        // The correctness of this depends on head being initialized
        // before tail and on head.next being accurate if the current
        // thread is first in queue.
        Node t = tail; // Read fields in reverse initialization order
        Node h = head;
        Node s;
        return h != t &&
            ((s = h.next) == null || s.thread != Thread.currentThread());
    }

获取到头结点，获取到尾结点，如果尾！=头，说明队列不为空，当前线程不等于head的下一个线程，说明该队列中已经有了其他的node等待获取lock，说明该队列不为空有先驱者

如果没有先驱者尝试的去compareAndSetState(0,1)方法，然后设置当前的thread设置为currentthread方法

设置成功返回true

else if分支就是判断当前线程是否与获取lock的线程为同一线程，如果为同一线程，则为可重入线程，则任务也是获取到lock

也返回true，即获取到lock，可执行代码

 
附：
   
unsafe.compareAndSwapInt(this, stateOffset, expect, update)
    
this---->当前对象
stateOffset---->当前对象某个值相对于当前类在系统内存的偏移量，相当于找到这个类值在内存的位置
    
tryAcquire方法如果失败的情况下，返回false，取反，则为true接着走acquireQueued(addWaiter(Node.EXCLUSIVE), arg)这个方法

addWaiter()方法：

    private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }
 
 首先这个方法的入参是Node mode是"mode"不是"node"传递的是等待者waiter的属性
 Node.EXCLUSIVE for exclusive, Node.SHARED for shared
 
 addWaiter这个方法首先创建一个Node,然后获取到这个队列的最末节点，如果最末节点不是null，则将刚才创建的节点的前置节点设置为当前的末节点，然后原子设置compareAndSetTail设置末节点，
 这是一种乐观的快速尝试设置末节点的方法，如果在高并发N多线程竞争的情况下一般会失败，不会进入return方法
 
 
 如果失败了，走enq方法
 
    private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }

先进入死循环，先获取一个尾部节点，如果尾部节点为空，说明当前队列还没有等待，则创建一个空的Node节点，当前尾部就是创建这个头部节点

创建当前节点的前置节点是当前的尾部节点，然后原子替换尾部节点，如果替换成功才会退出死循环，否则会一直阻塞当前线程


    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

addWaiter方法成功设置好tail节点之后，返回tail节点，可能返回的时候，其他线程已经在此"tail"节点上增加了后置节点，所以严格地讲这不能称为tail节点

但有一点可以确认此"tail"节点的前面的所有节点的位置已经确认好了，不可能再改变了


进入死循环，注意final Node p = node.predecessor()这个方法是在死循环内部的，获取当前节点的前置节点

如果前置节点是head节点的时候，也就是那个new Node()那个空的节点了，说明自己已经是节点的第一节点了，可以去tryAcquire获取锁了

如果成功了~说明前面一个线程成功获取到锁了

成功获取之后，设置当前节点为head节点，此时该队列的前面的那个node已经没有意义了，p.next = null，不需再有引用指向当前节点的，设置为null，**帮助GC

    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            return true;
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }


如果获取失败，则需要检查当前队列中有没有CANCELLED的，如果有CANCELLED的则将其移除队列，这样一直递归，移除当前队列中无效的节点



##relase方法

	public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }

tryRelease方法我们以ReentrantLock的tryRelease方法

	protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

getState方法返回的是单例中AQS中的state，因为release说明他已经获取到lock了，所以state应该返回1，入参是1，那么int c = 0

Thread.currentThread() != getExclusiveOwnerThread()这个方法则验证一下当前thread是否是获取到lock的线程，如果不等于，则说明当前线程在卖萌，

如果等于c == 0 则说明可以释放，再设置独占线程ExclusiveOwnerThread ==null

再次返回true







