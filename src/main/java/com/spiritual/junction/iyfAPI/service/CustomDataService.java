package com.spiritual.junction.iyfAPI.service;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spiritual.junction.iyfAPI.co.UserDetailsCO;
import com.spiritual.junction.iyfAPI.constants.AppConst;
import com.spiritual.junction.iyfAPI.constants.RoleConstant;
import com.spiritual.junction.iyfAPI.domain.Course;
import com.spiritual.junction.iyfAPI.domain.CourseCriteria;
import com.spiritual.junction.iyfAPI.domain.Participant;
import com.spiritual.junction.iyfAPI.domain.ParticipantSession;
import com.spiritual.junction.iyfAPI.domain.Role;
import com.spiritual.junction.iyfAPI.domain.Session;
import com.spiritual.junction.iyfAPI.domain.User;
import com.spiritual.junction.iyfAPI.repository.CourseRepository;
import com.spiritual.junction.iyfAPI.repository.ParticipantRepo;
import com.spiritual.junction.iyfAPI.repository.ParticipantSessionRepo;
import com.spiritual.junction.iyfAPI.repository.RoleRepository;
import com.spiritual.junction.iyfAPI.repository.SessionRepo;
import com.spiritual.junction.iyfAPI.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomDataService {

    @Autowired
    private RoleRepository   roleRepository;
    @Autowired
    private UserRepository   userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ParticipantRepo  participantRepo;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private JavaMailSender         javaMailSender;
    @Autowired
    private ParticipantSessionRepo participantSessionRepo;

    public void createUserData(List<UserDetailsCO> userDetailsCOList) {
        Role role = roleRepository.findByRole(RoleConstant.ROLE_USER);
        List<User> userList = new ArrayList<>();
        userDetailsCOList.forEach(userDetailsCO -> {
            log.info("User :: " + userDetailsCO.getEmail() + "  ::  " + userDetailsCO.getUsername());

            User userInfo = new User();
            userInfo.setEmail(userDetailsCO.getEmail());
            userInfo.setUsername(userDetailsCO.getFirstName() + userDetailsCO.getLastName());
            userInfo.setCollege(userDetailsCO.getCollegeName());
            userInfo.setAreaOfResidence(userDetailsCO.getAreaOfResidence());
            userInfo.setGender(userDetailsCO.getGender());
            userInfo.setFirstName(userDetailsCO.getFirstName());
            userInfo.setLastName(userDetailsCO.getLastName());
            userInfo.setProfession(userDetailsCO.getProfession());
            if (userDetailsCO.getPhoneNumber() != null) {
                userInfo.setNumber(userDetailsCO.getPhoneNumber());
            }
            if (userDetailsCO.getMobile() != null) {
                userInfo.setNumber(userDetailsCO.getMobile());
            }
            userInfo.setProvider(AppConst.Providers.CUSTOM);
            userInfo.setAge(userDetailsCO.getAge());
            userInfo.setPassword(new BCryptPasswordEncoder().encode(getDefaultPassword(userDetailsCO)));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            userInfo.setRoles(roles);
            userList.add(userInfo);
        });
        userRepository.saveAll(userList);
        log.info("Data saved successfully");
    }

    private String getDefaultPassword(UserDetailsCO userDetailsCO) {
        return userDetailsCO.getEmail().substring(0, 4) + userDetailsCO.getFirstName().substring(0, 2) + "default";
    }

    /* Create Inner Development Course with sessions */
    public void createCourse() {
        Course course = Course.builder()
                .title("Inner Development")
                .description("Through Vedic Wisdom")
                .endDate(LocalDate.parse("07-06-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .startDate(LocalDate.parse("03-06-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .type(AppConst.CourseType.ONLINE)
                .pricing(0)
                .build();
        CourseCriteria courseCriteria = new CourseCriteria();
        courseCriteria.setMinimumSessionAttended(4);
        courseCriteria.setPassingPercentage(51);
        course.setCourseCriteria(courseCriteria);
        LocalDate date = LocalDate.parse("03-06-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<Session> sessionList = new ArrayList<>();
        IntStream.range(0, 5).forEach(index -> {
            sessionList.add(createSession(date.plusDays(index + 1), index + 1));
        });
        course.setSessions(sessionList);
        courseRepository.save(course);
    }

    private Session createSession(LocalDate date, int index) {

        return Session.builder()
                .active(false)
                .date(date)
                .endTime(7)
                .startTime(9)
                .timeType("PM")
                .title(" session " + index).build();

    }

    @Transactional(rollbackFor = Exception.class)
    public void uploadParticipantsDataWithSession(List<UserDetailsCO> userDetailsCOList) {
        Set<Participant> participantList = new ModelMapper().map(userDetailsCOList, new TypeToken<Set<Participant>>() {
        }.getType());
        Session session = sessionRepo.findById(5l).get();

        participantList.forEach(participant1 -> {
            Participant participant = participantRepo.findByEmail(participant1.getEmail());
            if (participant == null) {
                participant = participantRepo.saveAndFlush(participant1);
            }

            ParticipantSession participantSession = new ParticipantSession();
            participantSession.setParticipant(participant);
            participantSession.setSession(session);
            participantSessionRepo.saveAndFlush(participantSession);


        });
        log.info("data saved for session 2");
    }

    public void send() throws IOException, MessagingException {
        List<String> emails = participantRepo.findAllByParticipationGreaterThanEqualToFour();
        List<Participant> participantList = participantRepo.findAllByEmailIn(emails);
       participantList = participantList.stream().filter(participant -> participant.getId() > 120).collect(Collectors.toList());
        participantList.forEach(participant -> {
            try {
                sendEmailWithAttachment(participant);
                System.out.println("Certificate sent to :: "+participant.getEmail() + " :: "+participant.getId());
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    void sendEmailWithAttachment(Participant participant) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(participant.getEmail());
        helper.addBcc("vermasahil269@gmail.com");

        helper.setSubject("Certificate of Participation || ISKCON Youth Forum || Inner Development Through Vedic Wisdom");

        // default = text/plain
        //helper.setText("Check attachment for image!");

        // true = text/html
        helper.setText("Hi "+participant.getUsername()+"<br/><br/><b>ISKCON Youth Forum Ghaziabad</b> extends heartfelt thanks for your active and " +
                "enthusiastic " +
                "participation in the 5 Session series of " +
                "'<b>Inner Development Through Vedic Wisdom</b>' and wishing you all the best for the future ahead. \n" +
                        "<br/>" +
                        "<br/>" +
                        "To Stay in touch with us for regular updates\n<br/><br/>" +
                        "<b>Whatsapp</b>: https://chat.whatsapp.com/I0LFDm4qrr4HFvguh4eCZD\n<br/>" +
                        "<b>Instagram</b>: https://www.instagram.com/invites/contact/?i=18kb06litwtvf&utm_content=gx6ncm1\n<br/>" +
                        "<b>Facebook</b>: https://www.facebook.com/EthiccraftGhaziabad <br/> <br/> regards<br/>//IYF Ghaziabad", true);

        // hard coded a file path
        //FileSystemResource file = new FileSystemResource(new File("path/android.png"));

        helper.addAttachment("Certificate.jpeg", new ByteArrayResource(IOUtils.toByteArray(getCertificate(participant))));

        javaMailSender.send(msg);

    }

    private InputStream getCertificate(Participant participant) throws IOException {
        int imgWidth = 1280;
        int imgHeight = 904;
        Resource resource = new ClassPathResource("cirtificate.jpeg");

        InputStream input = resource.getInputStream();
        final BufferedImage image = ImageIO.read(input);

        Graphics2D g = image.createGraphics();
        Font h = new Font("Lato", Font.BOLD, 30);
        Font z = new Font("ZapfDingbats", Font.PLAIN, 18);
        g.setColor(Color.BLACK);
        g.setFont(h);
        String text = participant.getUsername();

        TextLayout textLayout = new TextLayout(text, g.getFont(),
                g.getFontRenderContext());
        double textHeight = textLayout.getBounds().getHeight();
        double textWidth = textLayout.getBounds().getWidth();

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

// Draw the text in the center of the image
        g.drawString(text, imgWidth / 2 - (int) textWidth / 2,
                imgHeight / 2 + (int) textHeight / 2);
        g.setFont(z);
        g.drawString("Certificate No.", 1010, 779);
        String certificateNumber = String.valueOf(participant.getPhoneNumber()).substring(0,5);
        g.drawString("IYF-"+certificateNumber, 1025, 810);
        g.dispose();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", outStream);
        ImageIO.createImageInputStream(image);
        return new ByteArrayInputStream(outStream.toByteArray());
    }

}
