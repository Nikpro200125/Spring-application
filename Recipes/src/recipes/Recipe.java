package recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotBlank
    String category;

    @UpdateTimestamp
    LocalDateTime date;

    @ElementCollection
    @NotEmpty
    List<@NotBlank String> ingredients;

    @ElementCollection
    @NotEmpty
    List<@NotBlank String> directions;

    @JsonIgnore
    String author;
}
