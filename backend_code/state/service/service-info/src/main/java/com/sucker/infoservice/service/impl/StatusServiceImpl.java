package com.sucker.infoservice.service.impl;

import com.sucker.infoservice.entity.Status;
import com.sucker.infoservice.mapper.StatusMapper;
import com.sucker.infoservice.service.StatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sucker
 * @since 2023-11-24
 */
@Service
public class StatusServiceImpl extends ServiceImpl<StatusMapper, Status> implements StatusService {

}
