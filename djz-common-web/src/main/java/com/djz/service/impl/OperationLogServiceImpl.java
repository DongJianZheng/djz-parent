package com.djz.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djz.entity.OperationLog;
import com.djz.mapper.OperationLogMapper;
import com.djz.service.IOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author djz
 * @since 2018-05-08
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

}
