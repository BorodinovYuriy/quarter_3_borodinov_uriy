package lesson_4_thread_pro_1;

class ABCABCABC implements Runnable {
    //создаем одну переменную для всех потоков
    static char tmp = 'A';
    //служебные переменные (алгоритм)
    private final char current;
    private final char next;
    //имя потока
    private final String threadName;
    //монитор
    static final Object mon = new Object();
    ABCABCABC(String threadName, char current, char next) {
        this.threadName = threadName;
        this.current = current;
        this.next = next;
    }
    @Override
    public void run() {
        synchronized (mon) {
            for (int i = 5; i > 0; i--) {
                try {
                    while (tmp != current) mon.wait();
                    //печать имени потока и символа, с последующей заменой
                    System.out.println(" ["+threadName+"]"+current);
                    //замена символа
                    tmp = next;
                    //запуск остальных потоков
                    mon.notifyAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        new Thread(new ABCABCABC("1",'A', 'B')).start();
        new Thread(new ABCABCABC("2",'B', 'C')).start();
        new Thread(new ABCABCABC("3",'C', 'A')).start();
    }
}