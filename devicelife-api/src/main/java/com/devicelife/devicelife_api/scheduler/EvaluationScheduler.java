package com.devicelife.devicelife_api.scheduler;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluationScheduler {

    private final SqsTemplate sqsTemplate;

    @Value("${custom.sqs.queue-name}")
    private String queueName;

    // 📝 평가 대기열: <ComboId, 마지막_수정_시간>
    // 동시성 문제를 피하기 위해 ConcurrentHashMap 사용
    private final Map<Long, LocalDateTime> pendingEvaluations = new ConcurrentHashMap<>();

    // ✅ 1. 컨트롤러가 호출하는 메서드: "이 콤보 방금 수정됐어요!"
    public void scheduleEvaluation(Long comboId) {
        pendingEvaluations.put(comboId, LocalDateTime.now());
        log.info("⏳ 콤보({}) 수정 감지 -> 3초 카운트다운 리셋", comboId);
    }

    // ✅ 2. 1초마다 실행되는 감시자
    @Scheduled(fixedRate = 1000) // 1000ms = 1초마다 실행
    public void checkAndTriggerEvaluations() {
        LocalDateTime now = LocalDateTime.now();

        // 맵에 있는 모든 대기열을 검사
        pendingEvaluations.forEach((comboId, lastModifiedTime) -> {

            // 3초(plusSeconds(3))가 지났는지 확인
            if (lastModifiedTime.plusSeconds(3).isBefore(now)) {

                // 🔥 3초 지남! 평가 실행 (SQS 전송)
                triggerSqs(comboId);

                // 대기열에서 삭제
                pendingEvaluations.remove(comboId);
            }
        });
    }

    private void triggerSqs(Long comboId) {
        try {
            // 평가 ID 생성 (임시로 타임스탬프 or DB에서 생성 후 가져오기)
            // 실제로는 여기서 Evaluation 엔티티를 'PENDING' 상태로 DB에 저장하고 그 ID를 써야 함
            Long evaluationId = comboId;

            // 메시지 생성 (DTO 패키지 경로에 맞게 수정)
            JobMessage message = new JobMessage(evaluationId, 0L, "CREATE");

            sqsTemplate.send(to -> to
                    .queue(queueName)
                    .payload(message)
            );
            log.info("🚀 [3초 경과] 콤보({}) 평가 요청 SQS 전송 완료! (EvalID: {})", comboId, evaluationId);

        } catch (Exception e) {
            log.error("❌ SQS 전송 실패", e);
        }
    }

    public record JobMessage(Long evaluationId, Long deviceId, String messageType) {}
}
