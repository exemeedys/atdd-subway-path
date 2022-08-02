package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    private Sections sections;

    public Line() {
    }

    protected Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = newSections();
    }

    protected Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections = newSections();
        this.sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public static Line makeLine(String name, String color) {
        return new Line(name, color);
    }

    public static Line makeLine(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private Sections newSections() {
        return new Sections();
    }

    public List<Section> getSections() {
        return sections.list();
    }

    public boolean addSection(Station upStation, Station downStation, int distance) {
        return this.sections.addSection(this, upStation, downStation, distance);
    }

    public boolean removeSection(Station station) {
        return this.sections.removeSection(station);
    }

    public List<Station> stations() {

        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        return this.sections.stations();
    }

    public int sectionSize() {
        return this.sections.size();
    }


    public List<Section> sections() {
        return this.sections.list();
    }
}
