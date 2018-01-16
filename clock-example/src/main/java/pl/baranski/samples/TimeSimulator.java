package pl.baranski.samples;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import lombok.Getter;
import lombok.Setter;

public class TimeSimulator {

    private static final long ONE_HOUR = 3600000l;
    private static final long ONE_MINUTE = 60000l;
    private static final long ONE_SECOND = 1000l;
    private ClockLabel dateLable;
    private ClockLabel timeLable;
    private ClockLabel dayLable;

    public static void main(final String[] args) {
        new TimeSimulator().start();
    }

    private void start() {
        dateLable = new ClockLabel("date");
        timeLable = new ClockLabel("time");
        dayLable = new ClockLabel("day");

        JFrame.setDefaultLookAndFeelDecorated(true);
        final JFrame f = new JFrame("Time simulator");
        f.setSize(300, 150);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(4, 1));

        final JPanel buttonPannel = new JPanel(new GridLayout(1, 3));
        final JButton bSeconds = new JButton("Seconds");
        bSeconds.addActionListener(e -> updateClockLabels(ONE_SECOND));
        final JButton bMinutes = new JButton("Minutes");
        bMinutes.addActionListener(e -> updateClockLabels(ONE_MINUTE));
        final JButton bHours = new JButton("Hours");
        bHours.addActionListener(e -> updateClockLabels(ONE_HOUR));
        buttonPannel.add(bSeconds);
        buttonPannel.add(bMinutes);
        buttonPannel.add(bHours);
        f.add(buttonPannel);
        f.add(dateLable);
        f.add(timeLable);
        f.add(dayLable);

        f.getContentPane().setBackground(Color.black);

        f.setVisible(true);
    }

    public void updateClockLabels(final long append) {
        dateLable.setAppend(append);
        timeLable.setAppend(append);
        dayLable.setAppend(append);
    }

}

class ClockLabel extends JLabel implements ActionListener {

    private static final long serialVersionUID = -8647403051153610188L;
    private transient DateTimeFormatter formatter;
    @Getter
    private long actualMillis = new Date().getTime();
    @Getter
    @Setter
    // by default 1 second is added to the time
    long append = 1000l;

    public ClockLabel(final String type) {

        setForeground(Color.green);

        switch (type) {
        case "date":
            formatter = DateTimeFormatter.ISO_DATE;
            setFont(new Font("sans-serif", Font.PLAIN, 12));
            setHorizontalAlignment(SwingConstants.LEFT);
            break;
        case "time":
            formatter = DateTimeFormatter.ISO_TIME;
            setFont(new Font("sans-serif", Font.PLAIN, 40));
            setHorizontalAlignment(SwingConstants.CENTER);
            break;
        case "day":
            formatter = DateTimeFormatter.ofPattern(" EEEE");
            setFont(new Font("sans-serif", Font.PLAIN, 16));
            setHorizontalAlignment(SwingConstants.RIGHT);
            break;
        default:
            throw new IllegalArgumentException("Type=" + type + " not recognized.");
        }

        final Timer t = new Timer(1000, this);
        t.start();
    }

    @Override
    public void actionPerformed(final ActionEvent ae) {
        actualMillis = actualMillis + append;
        final Instant instant = Instant.ofEpochMilli(actualMillis);
        final LocalDateTime ofInstant = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        final String format = formatter.format(ofInstant);
        setText(format);
    }
}
