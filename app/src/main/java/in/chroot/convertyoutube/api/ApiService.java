package in.chroot.convertyoutube.api;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yogi on 07/11/16.
 */
public interface ApiService {


    @GET("youtube-converter")
    Observable<VideoMdl> getVideo(
            @Query("u") String url,
            @Query("k") String key
    );

}