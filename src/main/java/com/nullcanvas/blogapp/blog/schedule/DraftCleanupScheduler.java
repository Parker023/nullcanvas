package com.nullcanvas.blogapp.blog.schedule;

import com.nullcanvas.blogapp.blog.service.BlogManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Anirudh
 * @since 21/06/26
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DraftCleanupScheduler {

    private final BlogManagementService blogManagementService;

    @Scheduled(cron = "0 */30 * * * *")
    public void cleanupDrafts() {

        int deleted =
                blogManagementService
                        .cleanupAbandonedDrafts();

        log.info(
                "Deleted {} abandoned drafts",
                deleted
        );
    }


}
