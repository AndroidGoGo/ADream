package com.lzq.adream.manager;

import com.lzq.adream.App;
import com.lzq.adream.R;
import com.lzq.adream.model.bean.VideoChannelTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ${廖昭启} on 2017/11/10.
 */

public class VideosChannelTableManager {

    public static List<VideoChannelTable> loadVideosChannelsMine() {
        List<String> channelName = Arrays.asList(App.getAppContext().getResources().getStringArray(R.array.video_channel_name));
        List<String> channelId = Arrays.asList(App.getAppContext().getResources().getStringArray(R.array.video_channel_id));
        ArrayList<VideoChannelTable> newsChannelTables=new ArrayList<>();
        for (int i = 0; i < channelName.size(); i++) {
            VideoChannelTable entity = new VideoChannelTable(channelId.get(i), channelName.get(i));
            newsChannelTables.add(entity);
        }
        return newsChannelTables;
    }
}
