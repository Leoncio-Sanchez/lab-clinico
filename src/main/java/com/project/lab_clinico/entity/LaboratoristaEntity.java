package com.project.lab_clinico.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "laboratorista")
public class LaboratoristaEntity {
    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id_laboratorista")
private Long id;
    @JsonIgnoreProperties({"medico", "laboratorista", "paciente"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    private UserEntity userEntity;

    private String turno;
    private String correo;
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_creacion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;
    // Getters y setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Date getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(Date fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }
    @PrePersist
    protected void onCreate() {
        fecha_creacion = new Date();
        fecha_actualizacion = new Date();
    }
    @PreUpdate
    protected void onUpdate() {
        fecha_actualizacion = new Date();
    }
}
