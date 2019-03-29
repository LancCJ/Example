package com.lanccj.demo;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

/**
 * 工艺步骤周期时间对象
 */
@Setter
@Getter
public class StepTime {
    /**
     * 开始时间
     */
    private int startTime;
    /**
     * 结束时间
     */
    private int endTime;
}
