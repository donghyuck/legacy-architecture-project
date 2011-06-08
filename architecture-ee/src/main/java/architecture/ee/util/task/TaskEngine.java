package architecture.ee.util.task;

import java.util.Date;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.component.AdminService;
import architecture.ee.component.task.CancellableTask;

public class TaskEngine {
   
	
    private static class GuardedRunnable
    implements Runnable
	{
    	
	    private Runnable runnable;
	    private volatile boolean running;
	
	    GuardedRunnable(Runnable runnable)
	    {
	        this.runnable = runnable;
	    }
	    
	    public void run()
	    {
	        if(!running){		   
	        	try{
		        runnable.run();
		        running = true;
	        	}catch(Exception e){
	        		running = false;
	        	}
	        }
	    }
		
	    public Runnable getRunnable()
	    {
	        return runnable;
	    }
	    
	    public String toString()
	    {
	        return runnable.toString();
	    }

	}
    

    private static class ScheduledTask extends TimerTask
    {

        private TaskEngine engine;
        private int priority;
        private Runnable task;

        ScheduledTask(TaskEngine engine, int priority, Runnable task, boolean allowConcurrentTasks)
        {
            this.engine = engine;
            this.priority = priority;
            if(allowConcurrentTasks)
                this.task = task;
            else
                this.task = new GuardedRunnable(task);
        }
        
        public void run()
        {
            if(task instanceof GuardedRunnable)
            {
                if(shouldCancelTask(((GuardedRunnable)task).getRunnable()))
                {
                    cancel();
                    return;
                }
            } else
            if(shouldCancelTask(task))
            {
                cancel();
                return;
            }
            engine.addTask(priority, task);
        }

        private boolean shouldCancelTask(Runnable scheduledTask)
        {
            if(scheduledTask instanceof CancellableTask)
            {
                CancellableTask cancellable = (CancellableTask)scheduledTask;
                if(cancellable.shouldCancel())
                    return true;
            }
            return false;
        }

    }
    
    private class TaskEngineWorker extends Thread {
    	
    	private boolean done;
    	
    	TaskEngineWorker(String name)
        {
            super(threadGroup, name);
            done = false;
        }
    	

        public void stopWorker()
        {
            done = true;
        }

        private TaskWrapper nextTask()
        {
            TaskWrapper wrapper = null;
            synchronized(lock)
            {
                while(!done && (taskQueue.isEmpty() || !started)) 
                    try
                    {
                        lock.wait();
                    }
                    catch(InterruptedException ie)
                    {
                        lock.notify();
                    }
                if(!taskQueue.isEmpty() && adminService.isReady() )// && JiveApplication.isContextInitialized())
                    wrapper = (TaskWrapper)taskQueue.poll();
            }
            return wrapper;
        }

        public void run()
        {
            do
            {
                if(done)
                    break;
                int currentThreadPriority = getPriority();
                int newThreadPriority = currentThreadPriority;
                try
                {
                    TaskWrapper wrapper = nextTask();
                    if(wrapper != null)
                    {
                        int desiredTaskPriority = wrapper.getPriority();
                        newThreadPriority = desiredTaskPriority != 2 ? ((int) (desiredTaskPriority != 1 ? 2 : 5)) : 9;
                        if(newThreadPriority != currentThreadPriority)
                            try
                            {
                                //TaskEngine.log.finest((new StringBuilder()).append("Running task engine worker (").append(wrapper.getTask().getClass()).append(") at thread priority ").append(newThreadPriority).toString());
                                setPriority(newThreadPriority);
                            }
                            catch(Exception e)
                            {
                               // TaskEngine.log.log(Level.SEVERE, e.getMessage(), e);
                            }
                        
                            /*TaskEngine.log.finest((new StringBuilder()).append("Executing task (").append(wrapper.getTask().getClass()).append(")").toString());
                        SecurityContext sc = SecurityContextHolder.getContext();
                        if(sc instanceof JiveSecurityContext)
                        {
                            JiveSecurityContext jsc = (JiveSecurityContext)SecurityContextHolder.getContext();
                            jsc.setAuthentication(new SystemAuthentication(), true);
                        } else
                        {
                            sc.setAuthentication(new SystemAuthentication());
                        }*/
                        
                        wrapper.getTask().run();
                       // TaskEngine.log.finest((new StringBuilder()).append("Completed execution task (").append(wrapper.getTask().getClass()).append(")").toString());
                        if(newThreadPriority != currentThreadPriority)
                            try
                            {
                               // TaskEngine.log.finest((new StringBuilder()).append("Restoring task engine worker thread to thread priority - ").append(currentThreadPriority).toString());
                                setPriority(currentThreadPriority);
                            }
                            catch(Exception e)
                            {
                               // TaskEngine.log.log(Level.SEVERE, e.getMessage(), e);
                            }
                    }
                }
                catch(Exception e)
                {
                    //TaskEngine.log.log(Level.SEVERE, e.getMessage(), e);
                    if(newThreadPriority != currentThreadPriority)
                        try
                        {
                            //TaskEngine.log.finest((new StringBuilder()).append("Restoring task engine worker thread to thread priority - ").append(currentThreadPriority).toString());
                            setPriority(currentThreadPriority);
                        }
                        catch(Exception e2)
                        {
                           // TaskEngine.log.log(Level.SEVERE, e2.getMessage(), e);
                        }
                }
            } while(true);
        }
    }    
    
    
    private static final Log log = LogFactory.getLog(TaskEngine.class); 
    
    public static final int HIGH_PRIORITY = 2;
    public static final int MEDIUM_PRIORITY = 1;
    public static final int LOW_PRIORITY = 0;
    private PriorityQueue taskQueue;
    private ThreadGroup threadGroup;
    private TaskEngineWorker workers[];
    private Timer taskTimer;
    private final Object lock = new Object();
    private long newWorkerTimestamp;
    private long busyTimestamp;
    private boolean mockMode;
    private boolean started;
    private AdminService adminService;

    public void setMockMode(boolean mockMode)
    {
        this.mockMode = mockMode;
    }

    public void initialize()
    {
        synchronized(lock)
        {
        	this.started = true;
        	this.lock.notifyAll();
        }
    }

    public TaskEngine(AdminService adminService)
    {
    	this.adminService = adminService;
    	this.taskQueue = null;
    	this.workers = null;
    	this.taskTimer = null;
    	this.newWorkerTimestamp = System.currentTimeMillis();
    	this.busyTimestamp = System.currentTimeMillis();
    	this. mockMode = false;
    	this. started = false;
    	this.taskTimer = new Timer(true);
    	this.taskQueue = new PriorityQueue();
    	this.threadGroup = new ThreadGroup("Task Engine Workers");
    	this.workers = new TaskEngineWorker[5];
        
        for(int i = 0; i < workers.length; i++)
        {
        	this.workers[i] = new TaskEngineWorker((new StringBuilder()).append("Task Engine Worker ").append(i).toString());
        	this.workers[i].setDaemon(true);
        	this.workers[i].start();
        }

    }

    private void stopAllWorkers()
    {
        synchronized(lock)
        {
            started = false;
            if(workers != null)
            {
                TaskEngineWorker arr$[] = workers;
                int len$ = arr$.length;
                for(int i$ = 0; i$ < len$; i$++)
                {
                    TaskEngineWorker worker = arr$[i$];
                    worker.stopWorker();
                }

                lock.notifyAll();
                workers = null;
            }
        }
    }

    public int size()
    {
        return taskQueue.size();
    }

    public int getNumWorkers()
    {
        return workers.length;
    }

    public void addTask(Runnable task)
    {
        addTask(1, task);
    }

    public void addTask(int priority, Runnable task)
    {
        if(mockMode)
        {
            executeMockModeTask(task);
            return;
        }
        synchronized(lock)
        {
            if((double)taskQueue.size() > Math.ceil(workers.length / 2))
            {
                busyTimestamp = System.currentTimeMillis();
                addWorker();
            } else
            if(workers.length > 3)
                removeWorker();
            TaskWrapper wrapper = new TaskWrapper(priority, task);
            taskQueue.offer(wrapper);
            lock.notify();
        }
    }

    public TimerTask scheduleTask(Runnable task, Date date)
    {
        return scheduleTask(1, task, date);
    }

    public TimerTask scheduleTask(int priority, Runnable task, Date date)
    {
        TimerTask timerTask = new ScheduledTask(this, priority, task, true);
        taskTimer.schedule(timerTask, date);
        return timerTask;
    }

    public TimerTask scheduleTask(Runnable task, long delay, long period)
    {
        return scheduleTask(1, task, delay, period, false);
    }

    public TimerTask scheduleTask(int priority, Runnable task, long delay, long period, boolean allowMultiple)
    {
        TimerTask timerTask = new ScheduledTask(this, priority, task, allowMultiple);
        taskTimer.scheduleAtFixedRate(timerTask, delay, period);
        return timerTask;
    }

    public void destroy()
    {
        taskTimer.cancel();
        stopAllWorkers();
    }

    private void addWorker()
    {
        if(workers.length < 30 && System.currentTimeMillis() > newWorkerTimestamp + 2000L)
        {
            int newSize = workers.length + 1;
            int lastIndex = newSize - 1;
            TaskEngineWorker newWorkers[] = new TaskEngineWorker[newSize];
            System.arraycopy(workers, 0, newWorkers, 0, workers.length);
            newWorkers[lastIndex] = new TaskEngineWorker((new StringBuilder()).append("Task Engine Worker ").append(lastIndex).toString());
            newWorkers[lastIndex].setDaemon(true);
            newWorkers[lastIndex].start();
            workers = newWorkers;
            newWorkerTimestamp = System.currentTimeMillis();
        }
    }

    private void removeWorker()
    {
        if(workers.length > 3 && System.currentTimeMillis() > busyTimestamp + 5000L)
        {
            workers[workers.length - 1].stopWorker();
            int newSize = workers.length - 1;
            TaskEngineWorker newWorkers[] = new TaskEngineWorker[newSize];
            System.arraycopy(workers, 0, newWorkers, 0, newSize);
            workers = newWorkers;
            busyTimestamp = System.currentTimeMillis();
        }
    }

    public void addLowPriorityTask(Runnable task)
    {
        addTask(0, task);
    }

    public void addMediumPriorityTask(Runnable task)
    {
        addTask(1, task);
    }

    public void addHighPriorityTask(Runnable task)
    {
        addTask(2, task);
    }

    private void executeMockModeTask(final Runnable task)
    {    	
    	if(adminService.isReady()){
    		Callable c = new Callable() {
				public Object call() throws Exception {
					task.run();
					return null;
				}};
    		
    	}else{
    		task.run();
    	}
       /* if(JiveApplication.isInitialized())
        {
            Callable c = new Callable() {

                public Object call()
                    throws Exception
                {
                    task.run();
                    return null;
                }

                final Runnable val$task;
                final TaskEngine this$0;

            
            {
                this$0 = TaskEngine.this;
                task = runnable;
                super();
            }
            }
;*/
            /*SudoExecutor se = new SudoExecutor(JiveApplication.getEffectiveContext().getAuthenticationProvider(), new SystemAuthentication());
            try
            {
               // log.log(Level.INFO, "Executing task using SystemAuthentication");
                se.executeCallable(c);
            }
            catch(Exception e)
            {
               // log.log(Level.SEVERE, "Cannot execute mockMode task as system", e);
            }*/
       // } else
       // {
           // log.log(Level.INFO, "JiveApplication is not initialized, executing task using current Authentication");
            task.run();
      //  }
    }
}
