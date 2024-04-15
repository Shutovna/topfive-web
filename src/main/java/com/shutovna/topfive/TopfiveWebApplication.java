package com.shutovna.topfive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.content.commons.store.Store;
import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.content.rest.StoreRestResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@SpringBootApplication
public class TopfiveWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TopfiveWebApplication.class, args);
    }

    @Configuration
    @EnableFilesystemStores
    public static class StoreConfig {
        @Value("${topfive.file.store.dir}")
        private String fileStoreDir;
        File filesystemRoot() {
            return new File(fileStoreDir);
        }

        @Bean
        public FileSystemResourceLoader fsResourceLoader() throws Exception {
            return new FileSystemResourceLoader(filesystemRoot().getAbsolutePath());
        }
    }

    @StoreRestResource(path = "files")
    public interface FileStore extends Store<String> {
        //
    }
}
