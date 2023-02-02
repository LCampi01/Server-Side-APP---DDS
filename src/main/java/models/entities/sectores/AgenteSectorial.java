package models.entities.sectores;

import db.EntidadPersistente;
import lombok.Getter;
import lombok.Setter;
import models.entities.usuarios.Usuario;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="agente_sectorial")
public class AgenteSectorial extends EntidadPersistente {

    @Column(name = "nombre")
    private String nombre;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sector_id", referencedColumnName = "id", unique = true)
    private Sector sector;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    public AgenteSectorial(String nombre, Sector sector) {
        this.nombre = nombre;
        this.sector = sector;
    }


    public AgenteSectorial() {
    }

}
