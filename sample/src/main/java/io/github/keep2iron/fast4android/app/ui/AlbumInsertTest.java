package io.github.keep2iron.fast4android.app.ui;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;

public class AlbumInsertTest {

    private MediaScannerConnection conn = null;

    void insertIntoAlbum(Context context, File file) throws FileNotFoundException {

        String name = file.getName();
        int index;
        if ((index = name.lastIndexOf(".")) != -1) {
            name = name.substring(0, index);
        }
        MediaStore.Images.Media.insertImage(
                context.getContentResolver(),
                file.getAbsolutePath(),
                name,
                null
        );


        conn = new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                conn.scanFile(file.getAbsolutePath(), null);
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
            }
        });
        conn.connect();
    }

}
