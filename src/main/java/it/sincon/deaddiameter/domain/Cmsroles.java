package it.sincon.deaddiameter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.sincon.deaddiameter.repository.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Cmsroles.
 */
@Entity
@Table(name = "cmsroles")
public class Cmsroles implements Serializable {

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
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "active")
    private Boolean active;

    @ManyToMany(mappedBy = "cmsroles")
    @JsonIgnoreProperties(value = { "cmsroles", "cmsmenus" }, allowSetters = true)
    private Set<Cmspage> cmspages = new HashSet<>();

    @ManyToMany(mappedBy = "cmsroles")
    @JsonIgnoreProperties(value = { "cmsmenu", "cmsroles", "cmspages" }, allowSetters = true)
    private Set<Cmsmenu> cmsmenus = new HashSet<>();

    @ManyToMany(mappedBy = "cmsroles")
    @JsonIgnoreProperties(value = { "cmsroles", "users" }, allowSetters = true)
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cmsroles id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cmsroles name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Cmsroles description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Cmsroles active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Cmspage> getCmspages() {
        return this.cmspages;
    }

    public void setCmspages(Set<Cmspage> cmspages) {
        if (this.cmspages != null) {
            this.cmspages.forEach(i -> i.removeCmsroles(this));
        }
        if (cmspages != null) {
            cmspages.forEach(i -> i.addCmsroles(this));
        }
        this.cmspages = cmspages;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeCmsroles(this));
        }
        if (cmspages != null) {
            cmspages.forEach(i -> i.addCmsroles(this));
        }
        this.cmspages = cmspages;
    }

    public Cmsroles cmspages(Set<Cmspage> cmspages) {
        this.setCmspages(cmspages);
        return this;
    }

    public Cmsroles addCmspage(Cmspage cmspage) {
        this.cmspages.add(cmspage);
        cmspage.getCmsroles().add(this);
        return this;
    }

    public Cmsroles removeCmspage(Cmspage cmspage) {
        this.cmspages.remove(cmspage);
        cmspage.getCmsroles().remove(this);
        return this;
    }

    public Set<Cmsmenu> getCmsmenus() {
        return this.cmsmenus;
    }

    public void setCmsmenus(Set<Cmsmenu> cmsmenus) {
        if (this.cmsmenus != null) {
            this.cmsmenus.forEach(i -> i.removeCmsroles(this));
        }
        if (cmsmenus != null) {
            cmsmenus.forEach(i -> i.addCmsroles(this));
        }
        this.cmsmenus = cmsmenus;
    }

    public Cmsroles cmsmenus(Set<Cmsmenu> cmsmenus) {
        this.setCmsmenus(cmsmenus);
        return this;
    }

    public Cmsroles addCmsmenu(Cmsmenu cmsmenu) {
        this.cmsmenus.add(cmsmenu);
        cmsmenu.getCmsroles().add(this);
        return this;
    }

    public Cmsroles removeCmsmenu(Cmsmenu cmsmenu) {
        this.cmsmenus.remove(cmsmenu);
        cmsmenu.getCmsroles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cmsroles)) {
            return false;
        }
        return id != null && id.equals(((Cmsroles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cmsroles{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
