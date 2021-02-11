package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String ISBN;
    @Column(length = 1000)
    private String synopsis;
    private String pdf;
    private String publishersAddress;
    private String year;
    private int numOfPages;
    private double price;
    private boolean openAccess;

    private Boolean plagiarism;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderBook> orders = new HashSet<>();

    @ManyToOne
    private Merchant publisher;

    @ManyToOne
    private Genre genre;

    @ManyToOne
    private Writer writer;

    @ManyToOne
    private Editor editor;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Manuscript manuscript;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Lecturer lecturer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_key_word",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "key_word_id", referencedColumnName = "id"))
    private Set<KeyWord> keyWords = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_editors",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "editor_id", referencedColumnName = "id"))
    private Set<Editor> otherEditors = new HashSet<>();

    @OneToMany(mappedBy = "myBook", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlagiarismComplaint> plagiarismComplaintsForMyBook = new HashSet<>();

    @OneToMany(mappedBy = "bookPlagiat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlagiarismComplaint> receivedPlagiarismComplaints = new HashSet<>();
}
