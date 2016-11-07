package in.chroot.convertyoutube.api;

import java.util.List;

/**
 * Created by yogi on 07/11/16.
 */
public class VideoMdl {

    public String status;
    public String id;
    public String title;
    public String img;
    public String url;
    public List<DATA> data;

    public class DATA {
        public String format;
        public String quality;
        public String size;
        public String link;
    }
}