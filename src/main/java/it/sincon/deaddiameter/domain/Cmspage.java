package it.sincon.deaddiameter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.sincon.deaddiameter.domain.enumeration.Cmslanguage;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Type;

/**
 * A Cmspage.
 */
@Entity
@Table(name = "cmspage")
public class Cmspage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "alias", nullable = false)
    private String alias;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "content")
    private String content;

    @Column(name = "created")
    private Instant created;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "active")
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Cmslanguage language;

    @Column(name = "last_modified")
    private Instant lastModified;

    @ManyToMany
    @JoinTable(
        name = "rel_cmspage__cmsroles",
        joinColumns = @JoinColumn(name = "cmspage_id"),
        inverseJoinColumns = @JoinColumn(name = "cmsroles_id")
    )
    @JsonIgnoreProperties(value = { "cmspages", "cmsmenus", "users" }, allowSetters = true)
    private Set<Cmsroles> cmsroles = new HashSet<>();

    @ManyToMany(mappedBy = "cmspages")
    @JsonIgnoreProperties(value = { "cmsmenu", "cmsroles", "cmspages" }, allowSetters = true)
    private Set<Cmsmenu> cmsmenus = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cmspage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cmspage name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return this.alias;
    }

    public Cmspage alias(String alias) {
        this.setAlias(alias);
        return this;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getContent() {
        return this.content;
    }

    public Cmspage content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Cmspage created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Boolean getPublished() {
        return this.published;
    }

    public Cmspage published(Boolean published) {
        this.setPublished(published);
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Cmspage order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Cmspage active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Cmslanguage getLanguage() {
        return this.language;
    }

    public Cmspage language(Cmslanguage language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Cmslanguage language) {
        this.language = language;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public Cmspage lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public Set<Cmsroles> getCmsroles() {
        return this.cmsroles;
    }

    public void setCmsroles(Set<Cmsroles> cmsroles) {
        this.cmsroles = cmsroles;
    }

    public Cmspage cmsroles(Set<Cmsroles> cmsroles) {
        this.setCmsroles(cmsroles);
        return this;
    }

    public Cmspage addCmsroles(Cmsroles cmsroles) {
        this.cmsroles.add(cmsroles);
        cmsroles.getCmspages().add(this);
        return this;
    }

    public Cmspage removeCmsroles(Cmsroles cmsroles) {
        this.cmsroles.remove(cmsroles);
        cmsroles.getCmspages().remove(this);
        return this;
    }

    public Set<Cmsmenu> getCmsmenus() {
        return this.cmsmenus;
    }

    public void setCmsmenus(Set<Cmsmenu> cmsmenus) {
        if (this.cmsmenus != null) {
            this.cmsmenus.forEach(i -> i.removeCmspage(this));
        }
        if (cmsmenus != null) {
            cmsmenus.forEach(i -> i.addCmspage(this));
        }
        this.cmsmenus = cmsmenus;
    }

    public Cmspage cmsmenus(Set<Cmsmenu> cmsmenus) {
        this.setCmsmenus(cmsmenus);
        return this;
    }

    public Cmspage addCmsmenu(Cmsmenu cmsmenu) {
        this.cmsmenus.add(cmsmenu);
        cmsmenu.getCmspages().add(this);
        return this;
    }

    public Cmspage removeCmsmenu(Cmsmenu cmsmenu) {
        this.cmsmenus.remove(cmsmenu);
        cmsmenu.getCmspages().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cmspage)) {
            return false;
        }
        return id != null && id.equals(((Cmspage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cmspage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alias='" + getAlias() + "'" +
            ", content='" + getContent() + "'" +
            ", created='" + getCreated() + "'" +
            ", published='" + getPublished() + "'" +
            ", order=" + getOrder() +
            ", active='" + getActive() + "'" +
            ", language='" + getLanguage() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            "}";
    }
}
