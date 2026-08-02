package com.serdgio.http.server.service;

public class PathPattern {

    private final Segment[] segments;
    private final String originalPath;

    private PathPattern(String path) {
        this.originalPath = path;
        String[] inSegments = path.split("/");
        this.segments = new Segment[inSegments.length];
        for (int i = 0; i < inSegments.length; i++) {
            if (inSegments[i].startsWith("{") && inSegments[i].endsWith("}")) {
                segments[i] = new Segment(true, inSegments[i].substring(1, inSegments[i].length() - 1));
            } else {
                segments[i] = new Segment(false, inSegments[i]);
            }
        }
    }

    public static PathPattern path(String path) {
        return new PathPattern(path);
    }

    public boolean matches(String path) {
        if (path.equals("/") && !originalPath.equals("/")) {
            return false;
        }
        if (!path.equals("/") && originalPath.equals("/")) {
            return false;
        }

        for (int segmentIndex = 0; segmentIndex < segments.length; segmentIndex++) {
            int i = path.indexOf("/");
            int j = i + 1;

            if (i == -1) {
                i = path.length();
                j = path.length();
                if (segmentIndex != segments.length - 1) {
                    return false;
                }
            } else {
                if (segmentIndex == segments.length - 1) {
                    return false;
                }
            }

            Segment segment = segments[segmentIndex];

            if (!segment.isParam && !path.substring(0, i).equals(segment.constant)) {
                return false;
            }
            path = path.substring(j);
        }
        return true;
    }

    private static class Segment {
        private final boolean isParam;
        private final String param;
        private final String constant;

        public Segment(boolean isParam, String value) {
            this.isParam = isParam;
            if (isParam) {
                this.param = value;
                this.constant = null;
            } else {
                this.param = null;
                this.constant = value;
            }
        }
    }
}
