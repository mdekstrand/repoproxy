package net.elehack.repoproxy.util;

import java.util.*;
import java.util.stream.Collectors;

public class PathUtils {
    public static List<String> tokenize(String path) {
        ArrayList<String> elements = new ArrayList<>();
        int searchPoint = 0;
        while (searchPoint < path.length()) {
            int end = path.indexOf('/', searchPoint);
            if (end < 0) {
                elements.add(path.substring(searchPoint));
                searchPoint = path.length();
            } else {
                elements.add(path.substring(searchPoint, end));
                searchPoint = end + 1;
                if (searchPoint == path.length()) {
                    // tack on an empty element
                    elements.add("");
                }
            }
        }
        elements.trimToSize();
        return elements;
    }

    /**
     * Normalize a path.
     * @param path The path to normalize.
     * @return The normalized path, or an empty {@link Optional} if the path is invalid.
     */
    public static Optional<String> normalize(String path) {
        Deque<String> stack = new ArrayDeque<>();
        for (String elt: tokenize(path)) {
            switch (elt) {
            case "":
                if (stack.isEmpty() || !stack.peekLast().equals("")) {
                    stack.addLast(elt);
                }
                break;
            case ".":
                break; /* break */
            case "..":
                if (stack.size() == 1 && stack.peekLast().isEmpty()) {
                    return Optional.empty();
                } else if (stack.isEmpty() || stack.peekLast().equals("..")) {
                    stack.push("..");
                } else {
                    stack.removeLast();
                }
                break;
            default:
                if (stack.size() > 1 && "".equals(stack.peekLast())) {
                    stack.removeLast();
                }
                stack.addLast(elt);
            }
        }
        if (stack.isEmpty()) {
            return Optional.of(".");
        } else {
            return Optional.of(stack.stream().collect(Collectors.joining("/")));
        }
    }

    /**
     * Test if a path is absolute.
     * @param path The path to test.
     * @return Whether the path is absolute.
     */
    public static boolean isAbsolute(String path) {
        return !path.isEmpty() && path.charAt(0) == '/';
    }
}
