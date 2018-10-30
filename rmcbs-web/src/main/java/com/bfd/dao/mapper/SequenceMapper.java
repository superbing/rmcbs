package com.bfd.dao.mapper;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 实现类似Oracle的sequence
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年8月2日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public interface SequenceMapper {
    
    /**
     * 实现类似Oracle的sequence
     * 
     * @param param
     * @return
     * @see [类、类#方法、类#成员]
     */
    public void getNextVal(Map<String, Object> param);
    
    /**
     * 添加新的年份的sequence
     * 
     * @param name
     * @see [类、类#方法、类#成员]
     */
    public void addSequenceName(String name);
    
}
