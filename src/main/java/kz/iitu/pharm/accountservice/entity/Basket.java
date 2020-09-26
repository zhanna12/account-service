package kz.iitu.pharm.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name="basket_drugs",
            joinColumns = @JoinColumn(name="basket_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="drugs_id", referencedColumnName = "id"))
    private Set<Drug> drugs = new HashSet<>();

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDrugs(Set<Drug> drugs) {
        this.drugs = drugs;
    }

    public Set<Drug> getDrugs() {
        return drugs;
    }

}