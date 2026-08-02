package com.serdgio.http.server.controller;

import com.serdgio.http.server.bind.RequestMapping;
import com.serdgio.http.server.common.ApplicationParametrs;
import com.serdgio.http.server.common.HttpHeaders;
import com.serdgio.http.server.common.HttpMethod;
import com.serdgio.http.server.common.HttpStatus;
import com.serdgio.http.server.request.RequestContext;
import com.serdgio.http.server.response.ResponseContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ApplicationController {

    @RequestMapping(path = "/", method = HttpMethod.GET)
    public ResponseContext simpleOK(RequestContext requestContext) {
        return ResponseContext.build(HttpStatus.OK);
    }

    @RequestMapping(path = "/echo/{command}", method = HttpMethod.GET)
    public ResponseContext echo(RequestContext requestContext) {
        var responseBody = requestContext.getLastPart();

        return ResponseContext.build(
                HttpStatus.OK,
                HttpHeaders.fromHeaderMap(Map.of("Content-type", "text/plain",
                        "Content-Length", String.valueOf(responseBody.getBytes().length))),
                responseBody
        );
    }

    @RequestMapping(path = "/user-agent", method = HttpMethod.GET)
    public ResponseContext userAgent(RequestContext requestContext) {
        var responseBody = requestContext.getHeaders().getFirst("User-Agent");

        return ResponseContext.build(
                HttpStatus.OK,
                HttpHeaders.fromHeaderMap(Map.of("Content-type", "text/plain",
                        "Content-Length", String.valueOf(responseBody.getBytes().length))),
                responseBody
        );
    }

    @RequestMapping(path = "/files/{file}", method = HttpMethod.POST)
    public ResponseContext saveFile(RequestContext context) {
        if (!ApplicationParametrs.getInstance().isDirectorySet()) {
            return ResponseContext.build(HttpStatus.NOT_FOUND);
        }

        var directory = ApplicationParametrs.getInstance().getDirectory();
        directory = directory.endsWith("/") ? directory : directory + '/';
        var fileName = context.getLastPart();
        saveFile(String.format("%s%s", directory, fileName), context.getBody());
        return ResponseContext.build(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/files/{file}", method = HttpMethod.GET)
    public ResponseContext getFile(RequestContext context) {
        if (!ApplicationParametrs.getInstance().isDirectorySet()) {
            return ResponseContext.build(HttpStatus.NOT_FOUND);
        }

        var directory = ApplicationParametrs.getInstance().getDirectory();
        directory = directory.endsWith("/") ? directory : directory + '/';
        var fileName = context.getLastPart();
        var file = new File(String.format("%s%s", directory, fileName));
        if (!file.exists()) {
            return ResponseContext.build(HttpStatus.NOT_FOUND);
        }

        var fileContent = readFile(file);

        return ResponseContext.build(
                HttpStatus.OK,
                HttpHeaders.fromHeaderMap(Map.of("Content-type", "application/octet-stream",
                        "Content-Length", String.valueOf(fileContent.getBytes().length))),
                fileContent
        );
    }

    private String readFile(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            fileInputStream.read(bytes);
            return new String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveFile(String fileName, String content) {
        try {
            Path path = Paths.get(fileName);
            if (Files.exists(path)) {
                Files.delete(path);
            }

            Files.write(path, content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Exception trying to save file", e);
        }
    }
}
