package com.lyqf.bullet.logic.acl;

import com.lyqf.bullet.comet.client.CloseUserConnectionClient;
import com.lyqf.bullet.common.exception.ProviderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chenlang
 * @date 2022/5/30 2:41 下午
 */

@Slf4j
@Component
public class CloseUserConnectionFacadeAcl {

    @Autowired
    private CloseUserConnectionClient closeUserConnectionClient;

    /**
     *
     * @param userId
     * @param roomId
     * @return
     */
    public boolean close(Long userId, Long roomId) {
        try {
            closeUserConnectionClient.close(userId, roomId);
        } catch (ProviderNotFoundException e) {
            log.info("e", e);
        }
        return true;
    }



}
