# 多线程

## 一：实现多线程的方式

### 1.通过继承Thread类的方式：

```java
package my.thread;

public class First extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("i am studying");
        }
    }

    public static void main(String[] args) {
        First first = new First();
        first.start();
        for (int i = 0; i < 1000; i++) {
            System.out.println("i am playing");
        }
    }
}

```

### 2.通过实现Runnable接口方式：

```java
package my.thread;

public class Second implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("i am studying");
        }
    }

    public static void main(String[] args) {
        Second second = new Second();
        new Thread(second).start();
        for (int i = 0; i < 1000; i++) {
            System.out.println("i am playing");
        }
    }
}

```

```tip
推荐使用实现Runnable接口方式来使用多线程，避免oop单继承局限性，方便同一个对象被多个线程使用。
```



## 二：多线程操作同一个资源

### 当多个线程对一个资源进行获取时，若没有进行资源的保护，则会出现线程不安全的问题

```java
package my.thread;

public class Three implements Runnable{
    private int ticket = 20;
    @Override
    public void run() {
        while(ticket > 0){
            System.out.println(Thread.currentThread().getName() + " get the " +
                    ticket-- + "ticket");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Three three = new Three();
        new Thread(three, "wuKong").start();
        new Thread(three, "wuFan").start();
        new Thread(three, "biKe").start();

    }
}

```

![image-20201122155905356](thread_note.assets/image-20201122155905356.png)