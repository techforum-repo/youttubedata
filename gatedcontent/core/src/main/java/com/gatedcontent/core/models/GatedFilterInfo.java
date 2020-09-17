package com.gatedcontent.core.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GatedFilterInfo {
    public static final GatedFilterInfo DO_NOT_GATE = builder().gate(false).build();
    private boolean gate;
    private String redirectPath;
    private String gatedURI;
    private GatedResource gatedResource;
}