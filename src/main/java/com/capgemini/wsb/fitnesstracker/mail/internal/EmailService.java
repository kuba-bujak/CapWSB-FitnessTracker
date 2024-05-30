package com.capgemini.wsb.fitnesstracker.mail.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailProvider;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.DAY_OF_MONTH;

@Service
@Slf4j
class EmailService implements EmailProvider {
    @Override
    public EmailDto createEmail(String recipient, String title, List<Training> trainings) {
        final Date lastWeek = returnBeginningOfLastWeek();
        final Date yesterday = returnYesterday();
        final List<Training> lastWeekTrainings = trainings.stream().filter(training -> training.getStartTime().after(lastWeek) && training.getStartTime().before(yesterday)).toList();
        log.info("Creating email for {}", recipient);
        final StringBuilder builder = new StringBuilder("""
                You had %s trainings last week,
                completing %s units of distance
                You have completed %s trainings overall.
                Below you can find detailed rundown of your trainings last week:
                ----
                """.formatted(lastWeekTrainings.size(),
                trainings.isEmpty() ? 0 : lastWeekTrainings.stream().mapToDouble(Training::getDistance).sum(),
                trainings.size()
        ));
        lastWeekTrainings.forEach(training -> {
            builder.append("""
                training start: %s
                training end: %s
                activity type: %s
                distance: %s
                average speed: %s
                ----
                """.formatted(training.getStartTime(),
                    training.getEndTime() == null ? "-" : training.getEndTime(),
                    training.getActivityType(),
                    training.getDistance(),
                    training.getAverageSpeed()
            ));
        });
        EmailDto email = new EmailDto(recipient, title, builder.toString());
        log.info("Email created");
        System.out.println(builder);
        return email;
    }

    private Date returnBeginningOfLastWeek() {
        final Calendar now = Calendar.getInstance();
        now.add(DAY_OF_MONTH, -7);
        return now.getTime();
    }

    private Date returnYesterday() {
        final Calendar now = Calendar.getInstance();
        now.add(DAY_OF_MONTH, -1);
        return now.getTime();
    }
}