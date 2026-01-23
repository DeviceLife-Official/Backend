package com.devicelife.devicelife_api.common.mail;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.user.AuthProvider;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.domain.user.dto.AuthDto;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

import static com.devicelife.devicelife_api.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private static final Duration CODE_TTL = Duration.ofMinutes(3);
    private static final Duration VERIFIED_TTL = Duration.ofMinutes(10);

    // 재전송 제한
    private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(30);
    private static final int MAX_RESEND = 5;
    private static final int MAX_TRIES = 5;

    private void sendResetCodeMail(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("[DeviceLife] 비밀번호 재설정 인증번호");

            String html = """
                <div style="font-family:Arial,sans-serif;line-height:1.6">
                  <h2>비밀번호 재설정</h2>
                  <p>아래 인증번호를 3분 내 입력해 주세요.</p>
                  <div style="font-size:28px;font-weight:700;letter-spacing:6px;
                              padding:12px 16px;border:1px solid #ddd;display:inline-block">
                    %s
                  </div>
                  <p style="color:#666;margin-top:16px">본인이 요청하지 않았다면 이 메일을 무시해 주세요.</p>
                </div>
            """.formatted(code);

            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("메일 전송 실패", e);
        }
    }

    public void sendCodeMail(AuthDto.findPasswordReqDto req, HttpSession session) {

        String email = req.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(USER_4041));

        if (user.getProvider()!= AuthProvider.GENERAL) {
            throw new CustomException(USER_4005);
        }

        Instant now = Instant.now();
        PwResetSession st = (PwResetSession) session.getAttribute(PwResetSession.KEY);

        // 재전송 제한(쿨타임/횟수)
        if (st != null && st.lastSentAt != null) {
            if (st.resendCount >= MAX_RESEND) {
                throw new CustomException(MAIL_4001);
            }
            if (now.isBefore(st.lastSentAt.plus(RESEND_COOLDOWN))) {
                throw new CustomException(MAIL_4001);
            }
        }

        String code = ResetCodeUtil.generate6Digits();

        PwResetSession ns = new PwResetSession();
        ns.email = email;
        ns.code = ResetCodeUtil.sha256(code);
        ns.codeExpiresAt = now.plus(CODE_TTL);
        ns.tries = 0;
        ns.resendCount = (st == null) ? 0 : st.resendCount; // 같은 세션이면 누적
        ns.resendCount++;
        ns.lastSentAt = now;
        ns.verifiedToken = null;
        ns.verifiedExpiresAt = null;

        session.setAttribute(PwResetSession.KEY, ns);

        sendResetCodeMail(email, code);
    }
}
