package org.springframework.samples.petclinic.feeding;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.pet.PetType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "feeding_types")
public class FeedingType extends BaseEntity{
    
    @Size(min = 3, max = 50)
	@Column(name = "name")
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @ManyToOne
    @NotNull
    private PetType petType;
}
