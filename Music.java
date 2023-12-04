
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author anmolvalecha
 */

// Part 1 of Assignment

import java.util.Scanner;

public class Music {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an option:");
        System.out.println("1. Play Music from Assignment 1");

        int choice = scanner.nextInt();

        if (choice == 1) {
            playAssignment1();
        } else {
            System.out.println("Invalid choice. Exiting.");
        }
    }

    private static void playAssignment1() {
        // Existing code for Assignment 1
        Object lock = new Object();
        Thread thread1 = new Thread(new MusicThread(lock, "Thread 1",
                "/Users/anmolvalecha/NetBeansProjects/MusicPlayerAssign5/src/do.wav",
                "/Users/anmolvalecha/NetBeansProjects/MusicPlayerAssign5/src/mi.wav",
                "/Users/anmolvalecha/NetBeansProjects/MusicPlayerAssign5/src/sol.wav",
                "/Users/anmolvalecha/NetBeansProjects/MusicPlayerAssign5/src/si.wav",
                "/Users/anmolvalecha/NetBeansProjects/MusicPlayerAssign5/src/do-octave.wav"));
        Thread thread2 = new Thread(new MusicThread(lock, "Thread 2",
                "/Users/anmolvalecha/NetBeansProjects/MusicPlayerAssign5/src/re.wav",
                "/Users/anmolvalecha/NetBeansProjects/MusicPlayerAssign5/src/fa.wav",
                "/Users/anmolvalecha/NetBeansProjects/MusicPlayerAssign5/src/la.wav",
                "/Users/anmolvalecha/NetBeansProjects/MusicPlayerAssign5/src/do-octave.wav"));

        thread1.start();
        thread2.start();
    }
}

class MusicThread implements Runnable {

    private final Object lock;
    private final String threadName;
    private final String[] tones;

    public MusicThread(Object lock, String threadName, String... tones) {
        this.lock = lock;
        this.threadName = threadName;
        this.tones = tones;
    }

    @Override
    public void run() {
        for (String tone : tones) {
            synchronized (lock) {
                // Play the tone
                playTone(tone);
                // Notify the other thread to proceed
                lock.notify();
                try {
                    // Wait for the other thread to finish playing its tone
                    if (!tone.equals(tones[tones.length - 1])) {
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
        player.play(tone);
        System.out.println(threadName + " playing " + tone);
    }
}