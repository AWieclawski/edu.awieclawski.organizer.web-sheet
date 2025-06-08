package edu.awieclawski.organizer.data.models.base;

import edu.awieclawski.organizer.data.decriptor.Cryptor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = {"id", "hashId"})
public class BaseEntity {
    private final Integer hashId = Cryptor.decryptWord(this.id);
    private String id;
}
