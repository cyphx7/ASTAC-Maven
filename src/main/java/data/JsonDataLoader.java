package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import logic.Question;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * Loads programming questions from JSON files and manages the question bank.
 * Supports generating randomized game sets with balanced subject distribution.
 */
public class JsonDataLoader {

    /** Holds all questions found in files, grouped by subject */
    private final Map<String, List<Question>> questionBank = new HashMap<>();

    /**
     * Loads JSON question files from the classpath under the given root (e.g., "MCQ").
     * Works both from IDE (file protocol) and from packaged JAR (jar protocol).
     */
    public void loadQuestionsFromDirectory(String rootClasspathDir) {
        if (rootClasspathDir.startsWith("/")) {
            rootClasspathDir = rootClasspathDir.substring(1);
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) cl = getClass().getClassLoader();

        // Try to resolve the root directory on the classpath
        try {
            URL rootUrl = cl.getResource(rootClasspathDir);
            if (rootUrl == null) {
                // Fallback to common source path when running from source
                Path fallback = Paths.get("src", "main", "resources", rootClasspathDir);
                if (Files.exists(fallback)) {
                    try (Stream<Path> paths = Files.walk(fallback)) {
                        paths.filter(Files::isRegularFile)
                                .filter(p -> p.toString().endsWith(".json"))
                                .forEach(p -> {
                                    try (InputStream is = Files.newInputStream(p)) {
                                        parseStream(p.toString(), is);
                                    } catch (IOException ignored) { }
                                });
                    } catch (IOException e) {
                        System.err.println("Error reading fallback directory: " + e.getMessage());
                    }
                } else {
                    System.err.println("Resource root not found on classpath: " + rootClasspathDir);
                }
                return;
            }

            String protocol = rootUrl.getProtocol();
            if ("file".equalsIgnoreCase(protocol)) {
                // Running from IDE/classes
                try (Stream<Path> paths = Files.walk(Paths.get(rootUrl.toURI()))) {
                    paths.filter(Files::isRegularFile)
                            .filter(p -> p.toString().endsWith(".json"))
                            .forEach(p -> {
                                try (InputStream is = Files.newInputStream(p)) {
                                    parseStream(p.toString(), is);
                                } catch (IOException ignored) { }
                            });
                }
            } else if ("jar".equalsIgnoreCase(protocol)) {
                // Running from a JAR
                JarURLConnection jarCon = (JarURLConnection) rootUrl.openConnection();
                try (JarFile jarFile = jarCon.getJarFile()) {
                    String rootEntry = jarCon.getEntryName();
                    if (rootEntry == null) rootEntry = rootClasspathDir;
                    if (!rootEntry.endsWith("/")) rootEntry += "/";

                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry e = entries.nextElement();
                        String name = e.getName();
                        if (!e.isDirectory() && name.startsWith(rootEntry) && name.endsWith(".json")) {
                            try (InputStream is = cl.getResourceAsStream(name)) {
                                if (is != null) parseStream(name, is);
                            }
                        }
                    }
                }
            } else {
                // Generic fallback using getResources for unusual protocols
                Enumeration<URL> urls = cl.getResources(rootClasspathDir);
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    if ("file".equalsIgnoreCase(url.getProtocol())) {
                        try (Stream<Path> paths = Files.walk(Paths.get(url.toURI()))) {
                            paths.filter(Files::isRegularFile)
                                    .filter(p -> p.toString().endsWith(".json"))
                                    .forEach(p -> {
                                        try (InputStream is = Files.newInputStream(p)) {
                                            parseStream(p.toString(), is);
                                        } catch (IOException ignored) { }
                                    });
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading questions from classpath: " + e.getMessage());
        }
    }

    private void parseStream(String sourceName, InputStream stream) {
        if (stream == null) return;
        Gson gson = new Gson();
        try (InputStreamReader reader = new InputStreamReader(stream)) {
            Type listType = new TypeToken<List<Question>>(){}.getType();
            List<Question> loadedQuestions = gson.fromJson(reader, listType);

            if (loadedQuestions != null) {
                for (Question q : loadedQuestions) {
                    if (q == null) continue;
                    questionBank.computeIfAbsent(q.getSubject(), k -> new ArrayList<>()).add(q);
                }
            }
        } catch (IOException e) {
            System.err.println("Error parsing resource " + sourceName + ": " + e.getMessage());
        }
    }

    public List<Question> generateGameSet() {
        List<Question> gameSet = new ArrayList<>();

        for (String subject : questionBank.keySet()) {
            List<Question> subjectQuestions = questionBank.get(subject);
            Collections.shuffle(subjectQuestions);
            int takeCount = Math.min(subjectQuestions.size(), 2);
            gameSet.addAll(subjectQuestions.subList(0, takeCount));
        }

        Collections.shuffle(gameSet);
        return gameSet;
    }
}
