package manager;

import java.io.IOException;

public class GProcessManager {

	// start New JVM Process
    public void launchNewJavaProcess(String mainClassName) {
        String javaBin = System.getProperty("java.home") + "/bin/java";
        String classpath = System.getProperty("java.class.path");

        ProcessBuilder builder = new ProcessBuilder(
            javaBin, "-cp", classpath, mainClassName
        );

        try {
            Process process = builder.start();
            System.out.println("New Java process started: " + process);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
