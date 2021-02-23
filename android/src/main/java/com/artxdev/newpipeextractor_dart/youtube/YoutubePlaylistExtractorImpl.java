package com.artxdev.newpipeextractor_dart.youtube;

import com.artxdev.newpipeextractor_dart.downloader.DownloaderImpl;

import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.schabi.newpipe.extractor.ServiceList.YouTube;

public class YoutubePlaylistExtractorImpl {

    private YoutubePlaylistExtractor extractor;
    private ListExtractor.InfoItemsPage<StreamInfoItem> itemsPage;

    public Map<String, String> getPlaylistDetails(String url) throws Exception {
        NewPipe.init(DownloaderImpl.getInstance());
        extractor = (YoutubePlaylistExtractor) YouTube
                .getPlaylistExtractor(url);
        extractor.fetchPage();
        itemsPage = extractor.getInitialPage();
        Map<String, String> playlistDetails = new HashMap<>();
        playlistDetails.put("name", extractor.getName());
        playlistDetails.put("thumbnailUrl", extractor.getThumbnailUrl());
        playlistDetails.put("bannerUrl", extractor.getBannerUrl());
        playlistDetails.put("uploaderName", extractor.getUploaderName());
        playlistDetails.put("uploaderAvatarUrl", extractor.getUploaderAvatarUrl());
        playlistDetails.put("uploaderUrl", extractor.getUploaderUrl());
        playlistDetails.put("streamCount", String.valueOf(extractor.getStreamCount()));
        playlistDetails.put("id", extractor.getId());
        playlistDetails.put("url", extractor.getUrl());
        return playlistDetails;
    }

    public Map<Integer, Map<String, String>> getPlaylistStreams(String url) throws Exception {
        List<StreamInfoItem> items = extractor.getInitialPage().getItems();
        return _fetchResultsFromItems(items);
    }

    public Map<Integer, Map<String, String>> getNextPage() throws Exception {
        if (itemsPage.hasNextPage()) {
            itemsPage = extractor.getPage(itemsPage.getNextPage());
            List<StreamInfoItem> items = itemsPage.getItems();
            return _fetchResultsFromItems(items);
        } else {
            return new HashMap<>();
        }
    }

    private Map<Integer, Map<String, String>> _fetchResultsFromItems(List<StreamInfoItem> items) {
        Map<Integer, Map<String, String>> playlistResults = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            Map<String, String> itemMap = new HashMap<>();
            StreamInfoItem item = items.get(i);
            itemMap.put("name", item.getName());
            itemMap.put("uploaderName", item.getUploaderName());
            itemMap.put("uploaderUrl", item.getUploaderUrl());
            itemMap.put("uploadDate", item.getTextualUploadDate());
            itemMap.put("thumbnailUrl", item.getThumbnailUrl());
            itemMap.put("duration", String.valueOf(item.getDuration()));
            itemMap.put("viewCount", String.valueOf(item.getViewCount()));
            itemMap.put("url", item.getUrl());
            playlistResults.put(i, itemMap);
        }
        return playlistResults;
    }

}