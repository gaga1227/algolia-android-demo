/*
 * Copyright (c) 2012-2016 Algolia
 * http://www.algolia.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package algolia.com.demo.moviesearch.io;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

public class ImageLoaderManager {
    /** The singleton instance. */
    private static ImageLoaderManager instance;

    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

    // ----------------------------------------------------------------------
    // Initialization
    // ----------------------------------------------------------------------

    private ImageLoaderManager(@NonNull Context context) {
        this.context = context.getApplicationContext();
        imageLoader = ImageLoader.getInstance();
        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this.context)
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(new File(context.getCacheDir(), "img")))
                .diskCacheSize(128 * 1024 * 1024)
                .diskCacheFileCount(4000) // should be enough to hold all the synced movies plus online requests
                .build();
        imageLoader.init(configuration);
    }

    /**
     * Get the singleton instance.
     *
     * @param context An Android context.
     * @return The singleton instance.
     */
    public static @NonNull
    ImageLoaderManager getInstance(@NonNull Context context) {
        // NOTE: Double-check to prevent unnecessary synchronization in the common case.
        if (instance == null) {
            synchronized (ImageLoaderManager.class) {
                if (instance == null) {
                    instance = new ImageLoaderManager(context);
                }
            }
        }
        return instance;
    }

    // ----------------------------------------------------------------------
    // Accessors
    // ----------------------------------------------------------------------

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public DisplayImageOptions getDisplayImageOptions() {
        return displayImageOptions;
    }
}
