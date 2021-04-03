package com.embed.core.models.embeddable.vimeo;

import java.net.URISyntaxException;

import org.jetbrains.annotations.Nullable;

public interface Vimeo {

    /**
     * Name of the resource property that defines the id of the YouTube video.
     */
    String PN_VIDEO_ID = "vimeoVideoId";

    /**
     * Name of the resource property that defines the width of the iFrame hosting the YouTube video.
     */
    String PN_WIDTH = "vimeoWidth";

    /**
     * Name of the resource property that defines the height of the iFrame hosting the YouTube video.
     */
    String PN_HEIGHT = "vimeoHeight";

    /* The following resource property names are used for optional YouTube player paramters */
    String PN_AUTOPLAY = "vimeoAutoPlay";
    
    String PN_MUTE = "vimeoMute";

    String PN_LOOP = "vimeoLoop";

    String PN_PLAYS_INLINE = "vimeoPlaysInline";

    String PN_DESIGN_MUTE_ENABLED = "vimeoMuteEnabled";

    String PN_DESIGN_MUTE_DEFAULT_VALUE = "vimeoMuteDefaultValue";
    
    String PN_DESIGN_AUTOPLAY_ENABLED = "vimeoAutoPlayEnabled";

    String PN_DESIGN_AUTOPLAY_DEFAULT_VALUE = "vimeoAutoPlayDefaultValue";

    String PN_DESIGN_LOOP_ENABLED = "vimeoLoopEnabled";

    String PN_DESIGN_LOOP_DEFAULT_VALUE = "vimeoLoopDefaultValue";

    String PN_DESIGN_RELATED_VIDEOS_ENABLED = "vimeoRelatedVideosEnabled";

    String PN_DESIGN_RELATED_VIDEOS_DEFAULT_VALUE = "vimeoRelatedVideosDefaultValue";

    String PN_DESIGN_PLAYS_INLINE_ENABLED = "vimeoPlaysInlineEnabled";

    String  PN_DESIGN_PLAYS_INLINE_DEFAULT_VALUE = "vimeoPlaysInlineDefaultValue";

	default @Nullable String getIFrameWidth() {
        return null;
    }

	default @Nullable String getIFrameHeight() {
        return null;
    }

	default @Nullable String getIFrameSrc() throws URISyntaxException {
        return null;
    }

    default boolean isEmpty() {
        return false;
    }
}