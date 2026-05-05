package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import model.Movie;
import model.Show;

public class MovieBrowsingPanel extends JPanel {
    private JPanel cards;

    public MovieBrowsingPanel() {
        setOpaque(true);
        setBackground(UIConstants.BACKGROUND);
        setLayout(new BorderLayout(0, 18));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(createHeader(), BorderLayout.NORTH);
        add(createMovieGrid(), BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                filterMovies("");
            }
        });
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(0, 8));
        header.setOpaque(false);

        JLabel title = new JLabel("Now Playing");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_TITLE);

        JLabel subtitle = new JLabel("Explore the latest cinematic masterpieces currently screening.");
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setFont(UIConstants.FONT_REGULAR);

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        return header;
    }

    private JScrollPane createMovieGrid() {
        cards = new JPanel(new GridLayout(0, 3, 20, 20));
        cards.setOpaque(false);
        populateCards("");

        JScrollPane scroll = new JScrollPane(cards, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    public void filterMovies(String query) {
        populateCards(query);
    }

    private void populateCards(String query) {
        cards.removeAll();
        String q = query.toLowerCase();
        List<Movie> movies = ServiceContext.getInstance().getMovieService().getAllMovies();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(q) || movie.getGenre().toLowerCase().contains(q)) {
                int h = movie.getDuration() / 60;
                int m = movie.getDuration() % 60;
                String duration = h + "h " + m + "m";
                String[] tags = movie.getGenre().split(", ");
                cards.add(createCard(movie.getTitle(), duration + " • English", "8.5", tags, UIConstants.PRIMARY, movie.getImagePath()));
            }
        }
        cards.revalidate();
        cards.repaint();
    }

    private JPanel createCard(String titleText, String metaText, String score, String[] tags, Color accent, String imagePath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(UIConstants.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel image = new JPanel(new BorderLayout());
        image.setPreferredSize(new Dimension(0, 220));
        image.setBackground(UIConstants.BORDER);
        image.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaled = icon.getImage().getScaledInstance(300, 220, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                image.add(imgLabel, BorderLayout.CENTER);
            } catch (Exception ignored) {
            }
        }

        JLabel title = new JLabel(titleText);
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_SEMIBOLD);

        JLabel meta = new JLabel(metaText);
        meta.setForeground(UIConstants.TEXT_MUTED);
        meta.setFont(UIConstants.FONT_REGULAR);

        JLabel scoreLabel = new JLabel(score);
        scoreLabel.setForeground(accent);
        scoreLabel.setFont(UIConstants.FONT_SEMIBOLD);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);
        top.add(scoreLabel, BorderLayout.EAST);

        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tagsPanel.setOpaque(false);
        for (String tag : tags) {
            JLabel tagLabel = new JLabel(tag);
            tagLabel.setOpaque(true);
            tagLabel.setBackground(UIConstants.SURFACE_ALT);
            tagLabel.setForeground(UIConstants.TEXT_MUTED);
            tagLabel.setFont(UIConstants.FONT_REGULAR);
            tagLabel.setBorder(new EmptyBorder(4, 8, 4, 8));
            tagsPanel.add(tagLabel);
        }

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(top);
        textPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        textPanel.add(meta);
        textPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        textPanel.add(tagsPanel);

        card.add(image, BorderLayout.CENTER);
        card.add(textPanel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Show firstShow = null;
                for (Show s : ServiceContext.getInstance().getShowService().getAllShows()) {
                    if (s.getMovie().getTitle().equals(titleText)) {
                        firstShow = s;
                        break;
                    }
                }
                new SeatSelectionUI(firstShow).setVisible(true);
            }
        });

        return card;
    }
}
