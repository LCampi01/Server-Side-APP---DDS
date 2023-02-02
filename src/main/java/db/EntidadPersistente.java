package db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class EntidadPersistente {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
}
