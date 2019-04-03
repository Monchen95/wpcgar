package edu.hawhamburg.shared.importer.skeleton;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SkeletonLock {
    public static final Lock mutex = new ReentrantLock(true);
}
