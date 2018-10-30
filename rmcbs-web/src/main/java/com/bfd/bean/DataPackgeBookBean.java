package com.bfd.bean;

import lombok.Data;

@Data
public class DataPackgeBookBean {
    
    private Long id;
    
    private Long dataPackageId;
    
    private Long metadataId;
    
    private String createTime;
    
    public DataPackgeBookBean() {
        
    }
    
    public DataPackgeBookBean(Long dataPackageId, Long metadataId) {
        this.dataPackageId = dataPackageId;
        this.metadataId = metadataId;
    }
    
    public DataPackgeBookBean(Long id, Long dataPackageId, Long metadataId) {
        this.id = id;
        this.dataPackageId = dataPackageId;
        this.metadataId = metadataId;
    }
    
    public DataPackgeBookBean(Long id, Long dataPackageId, Long metadataId, String createTime) {
        this.id = id;
        this.dataPackageId = dataPackageId;
        this.metadataId = metadataId;
        this.createTime = createTime;
    }
    
}
