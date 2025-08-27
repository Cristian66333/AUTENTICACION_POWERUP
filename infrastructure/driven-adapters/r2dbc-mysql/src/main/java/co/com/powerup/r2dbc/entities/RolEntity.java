package co.com.powerup.r2dbc.entities;

import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("rol")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RolEntity {
    @Id
    @Column("id")
    private Integer id;
    @Column("nombre")
    private String name;
    @Column("descripcion")
    private String description;
}
