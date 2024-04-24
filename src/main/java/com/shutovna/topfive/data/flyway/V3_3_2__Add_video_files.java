package com.shutovna.topfive.data.flyway;

import org.apache.commons.io.IOUtils;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

@Component
@Profile("dev")
public class V3_3_2__Add_video_files extends BaseJavaMigration {
    private static final String filename = "example_video.mp4";
    @Value("classpath:" + filename)
    private Resource exampleFile;

    @Value("${topfive.file.store.dir}")
    private String fileStoreDir;


    @Override
    public void migrate(Context context) throws Exception {
        byte[] fileContent = exampleFile.getContentAsByteArray();

        File dir = new File(fileStoreDir);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File example = new File(fileStoreDir, filename);
        IOUtils.copy(new ByteArrayInputStream(fileContent), new FileOutputStream(example));
    }
}
