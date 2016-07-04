#java的四种引用

##1. 强引用（Strong Reference）
最常用的引用类型，如Object obj = new Object(); 。只要强引用存在则GC时则必定不被回收。

##2. 软引用（Soft Reference）
 用于描述还游泳但非必须的对象，当堆将发生OOM（Out Of Memory）时则会回收软引用所指向的内存空间，若回收后依然空间不足才会抛出OOM。
 
	 ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
	 SoftReference<Object> objs = new SoftReference<Object>(new Object(), queue);
	 
##3. 弱引用（Weak Reference）
**发生GC时必定回收弱引用指向的内存空间。

##4. 虚引用（Phantom Reference）
又称为幽灵引用或幻影引用，，虚引用既不会影响对象的生命周期，也无法通过虚引用来获取对象实例，仅用于在发生GC时接收一个系统通知。

  那现在问题来了，若一个对象的引用类型有多个，那到底如何判断它的可达性呢？其实规则如下：

　　1. 单条引用链的可达性以最弱的一个引用类型来决定；
　　2. 多条引用链的可达性以最强的一个引用类型来决定；

OOM代码：

	public static void main(String[] args) throws Exception {

        List<WeakHashMap<byte[][], byte[][]>> maps = new ArrayList<WeakHashMap<byte[][], byte[][]>>();

        for (int i = 0; i < 1000; i++) {
            WeakHashMap<byte[][], byte[][]> d = new WeakHashMap<byte[][], byte[][]>();
            d.put(new byte[1000][1000], new byte[1000][1000]);
            maps.add(d);
            System.gc();
            System.err.println(i);
        }
    }
 
 原因：List有WeakHashMap的引用，被回收后再调用size、clear或put等直接或间接调用私有expungeStaleEntries方法的实例方法时，则这些键对象已被回收的项目（Entry）将被移除出键值对集合中

	public static void main(String[] args) throws Exception {  
  
        List<WeakHashMap<byte[][], byte[][]>> maps = new ArrayList<WeakHashMap<byte[][], byte[][]>>();  
  
        for (int i = 0; i < 1000; i++) {  
            WeakHashMap<byte[][], byte[][]> d = new WeakHashMap<byte[][], byte[][]>();  
            d.put(new byte[1000][1000], new byte[1000][1000]);  
            maps.add(d);  
            System.gc();  
            System.err.println(i);  
  
            for (int j = 0; j < i; j++) {
                // 触发移除Entry操作
                System.err.println(j+  " size" + maps.get(j).size());  
            }  
        }  
    }  