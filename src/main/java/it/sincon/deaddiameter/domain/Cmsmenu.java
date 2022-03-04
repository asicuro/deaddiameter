package it.sincon.deaddiameter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.sincon.deaddiameter.domain.enumeration.Cmslanguage;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Cmsmenu.
 */
@Entity
@Table(name = "cmsmenu")
public class Cmsmenu implements Serializable {

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
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "css")
    private String css;

    @Column(name = "menu_type")
    private Integer menuType;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "active")
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Cmslanguage language;

    @Column(name = "last_modified")
    private Instant lastModified;

    @JsonIgnoreProperties(value = { "cmsmenu", "cmsroles", "cmspages" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Cmsmenu cmsmenu;

    @ManyToMany
    @JoinTable(
        name = "rel_cmsmenu__cmsroles",
        joinColumns = @JoinColumn(name = "cmsmenu_id"),
        inverseJoinColumns = @JoinColumn(name = "cmsroles_id")
    )
    @JsonIgnoreProperties(value = { "cmspages", "cmsmenus" }, allowSetters = true)
    private Set<Cmsroles> cmsroles = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_cmsmenu__cmspage",
        joinColumns = @JoinColumn(name = "cmsmenu_id"),
        inverseJoinColumns = @JoinColumn(name = "cmspage_id")
    )
    @JsonIgnoreProperties(value = { "cmsroles", "cmsmenus" }, allowSetters = true)
    private Set<Cmspage> cmspages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cmsmenu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cmsmenu name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public Cmsmenu title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Cmsmenu description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCss() {
        return this.css;
    }

    public Cmsmenu css(String css) {
        this.setCss(css);
        return this;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public Integer getMenuType() {
        return this.menuType;
    }

    public Cmsmenu menuType(Integer menuType) {
        this.setMenuType(menuType);
        return this;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Cmsmenu order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Cmsmenu active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Cmslanguage getLanguage() {
        return this.language;
    }

    public Cmsmenu language(Cmslanguage language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Cmslanguage language) {
        this.language = language;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public Cmsmenu lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public Cmsmenu getCmsmenu() {
        return this.cmsmenu;
    }

    public void setCmsmenu(Cmsmenu cmsmenu) {
        this.cmsmenu = cmsmenu;
    }

    public Cmsmenu cmsmenu(Cmsmenu cmsmenu) {
        this.setCmsmenu(cmsmenu);
        return this;
    }

    public Set<Cmsroles> getCmsroles() {
        return this.cmsroles;
    }

    public void setCmsroles(Set<Cmsroles> cmsroles) {
        this.cmsroles = cmsroles;
    }

    public Cmsmenu cmsroles(Set<Cmsroles> cmsroles) {
        this.setCmsroles(cmsroles);
        return this;
    }

    public Cmsmenu addCmsroles(Cmsroles cmsroles) {
        this.cmsroles.add(cmsroles);
        cmsroles.getCmsmenus().add(this);
        return this;
    }

    public Cmsmenu removeCmsroles(Cmsroles cmsroles) {
        this.cmsroles.remove(cmsroles);
        cmsroles.getCmsmenus().remove(this);
        return this;
    }

    public Set<Cmspage> getCmspages() {
        return this.cmspages;
    }

    public void setCmspages(Set<Cmspage> cmspages) {
        this.cmspages = cmspages;
    }

    public Cmsmenu cmspages(Set<Cmspage> cmspages) {
        this.setCmspages(cmspages);
        return this;
    }

    public Cmsmenu addCmspage(Cmspage cmspage) {
        this.cmspages.add(cmspage);
        cmspage.getCmsmenus().add(this);
        return this;
    }

    public Cmsmenu removeCmspage(Cmspage cmspage) {
        this.cmspages.remove(cmspage);
        cmspage.getCmsmenus().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cmsmenu)) {
            return false;
        }
        return id != null && id.equals(((Cmsmenu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cmsmenu{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", css='" + getCss() + "'" +
            ", menuType=" + getMenuType() +
            ", order=" + getOrder() +
            ", active='" + getActive() + "'" +
            ", language='" + getLanguage() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            "}";
    }
}
