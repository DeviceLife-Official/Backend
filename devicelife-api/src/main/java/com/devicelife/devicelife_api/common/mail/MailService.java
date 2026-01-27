package com.devicelife.devicelife_api.common.mail;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.domain.user.AuthProvider;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.domain.user.dto.AuthDto;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static com.devicelife.devicelife_api.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
                </div>
            """.formatted(code);

            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new CustomException(MAIL_5001);
        }
    }


    private PwResetSession getSessionOrThrow(HttpSession session) {
        PwResetSession st = (PwResetSession) session.getAttribute(PwResetSession.KEY);
        if (st == null || st.email == null) {
            throw new CustomException(MAIL_4191);
        }
        return st;
    }

    public void sendCodeMail(AuthDto.sendMailReqDto req, HttpSession session) {

        String email = req.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(USER_4041));

        if (user.getPasswordHash()==null) {
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
                throw new CustomException(MAIL_4002);
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

    public AuthDto.verifyCodeResDto verifyCodeMail(AuthDto.verifyCodeReqDto req, HttpSession session) {

        PwResetSession st = getSessionOrThrow(session);

        Instant now = Instant.now();
        if (st.codeExpiresAt == null || now.isAfter(st.codeExpiresAt)) {
            throw new CustomException(MAIL_4003);
        }
        if (st.tries >= MAX_TRIES) {
            throw new CustomException(MAIL_4004);
        }

        st.tries++;
        String inputHash = ResetCodeUtil.sha256(req.getCode());
        if (!inputHash.equals(st.code)) {
            session.setAttribute(PwResetSession.KEY, st);
            throw new CustomException(MAIL_4005);
        }

        // 성공 -> verifiedToken 발급
        st.verifiedToken = UUID.randomUUID().toString();
        st.verifiedExpiresAt = now.plus(VERIFIED_TTL);

        // 코드 재사용 방지
        st.code = null;
        st.codeExpiresAt = null;

        session.setAttribute(PwResetSession.KEY, st);

        return AuthDto.verifyCodeResDto.builder()
                .verifyToken(st.verifiedToken)
                .build();
    }

    @Transactional
    public void resetPassword(AuthDto.resetPasswordReqDto req, HttpSession session) {

        PwResetSession st = getSessionOrThrow(session);
        Instant now = Instant.now();

        if (st.verifiedToken == null || st.verifiedExpiresAt == null) {
        throw new CustomException(AUTH_4011);
        }
        if (now.isAfter(st.verifiedExpiresAt)) {
            throw new CustomException(AUTH_4191);
        }
        if (!st.verifiedToken.equals(req.getVerifiedToken())) {
            throw new CustomException(AUTH_4012);
        }

        User user = userRepository.findByEmail(st.email)
                .orElseThrow(() -> new CustomException(USER_4041));

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        session.removeAttribute(PwResetSession.KEY);
    }
}
