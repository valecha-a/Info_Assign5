
import java.util.List;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author anmolvalecha
 */
public class MusicBonus {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an option:");
        System.out.println("1. Play Twinkle Twinkle Little Star(Bonus)");

        int choice = scanner.nextInt();

        if (choice == 1) {
            playTwinkleTwinkle();
        } else {
            System.out.println("Invalid choice. Exiting.");
        }
    }

    private static void playTwinkleTwinkle() {
        Object lock = new Object();

        List<String> twinkleTones = List.of(
                "do", "do", "sol", "sol", "la", "la", "sol", "fa", "fa", "mi", "mi", "re", "re", "do",
                "sol", "sol", "fa", "fa", "mi", "mi", "re", "sol", "sol", "fa", "fa", "mi", "mi", "re", "do", "do",
                "sol", "sol", "la", "la", "sol", "fa", "fa", "mi", "mi", "re", "re", "do"
        );

        List<String> allowedTonesThread1 = List.of("do", "mi", "sol", "si");
        List<String> allowedTonesThread2 = List.of("re", "fa", "la");

        SharedIndex sharedIndex = new SharedIndex();
        Thread thread1 = new Thread(new MusicThread(lock, "Thread 1", allowedTonesThread1, twinkleTones, sharedIndex));
        Thread thread2 = new Thread(new MusicThread(lock, "Thread 2", allowedTonesThread2, twinkleTones, sharedIndex));

        thread1.start();
        thread2.start();
    }
}

class MusicThread implements Runnable {

    private final Object lock;
    private final String threadName;
    private final List<String> allowedTones;
    private final List<String> tones;
    private final SharedIndex sharedIndex;

    public MusicThread(Object lock, String threadName, List<String> allowedTones, List<String> tones, SharedIndex sharedIndex) {
        this.lock = lock;
        this.threadName = threadName;
        this.allowedTones = allowedTones;
        this.tones = tones;
        this.sharedIndex = sharedIndex;
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (sharedIndex.getIndex() < tones.size()) {
                String currentTone = tones.get(sharedIndex.getIndex());
                if (allowedTones.contains(currentTone)) {
                    // Play the tone
                    playTone(currentTone);
                    // Update the index
                    sharedIndex.incrementIndex();
                }
                // Notify the other thread to proceed
                lock.notify();
                try {
                    // Wait for the other thread to finish playing its tone
                    if (sharedIndex.getIndex() < tones.size()) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void playTone(String tone) {
        // Use the FilePlayer to play the tone
        FilePlayer player = new FilePlayer();
        player.play("/Users/anmolvalecha/NetBeansProjects/MusicPlayerAssign5/src/" + tone + ".wav");
        System.out.println(threadName + " playing " + tone);
    }
}

class SharedIndex {
    private int index;

    public synchronized int getIndex() {
        return index;
    }

    public synchronized void incrementIndex() {
        index++;
    }
}

